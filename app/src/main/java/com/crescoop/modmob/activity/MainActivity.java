package com.crescoop.modmob.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crescoop.modmob.R;
import com.crescoop.modmob.fragments.ConnectionConfigurationFragment;
import com.crescoop.modmob.fragments.SlaveConfigurationFragment;
import com.crescoop.modmob.fragments.StatusFragment;
import com.crescoop.modmob.interfaces.InterfaceSwap;
import com.crescoop.modmob.modbus.ModbusConfiguration;
import com.crescoop.modmob.servico.SwapService;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private InterfaceSwap interfaceSwap;

    private ServiceConnection conexao = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SwapService.SwapServiceBinder conexao = (SwapService.SwapServiceBinder )service;
            interfaceSwap = conexao.getInterface();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            interfaceSwap = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Runnable runnable = new Runnable() {
                        public void run() {

                            TCPMasterConnection con = null; //the connection
                            ModbusTCPTransaction trans = null; //the transaction
                            WriteSingleRegisterRequest req = null; //the request
                            WriteSingleRegisterResponse res = null; //the response


                            InetAddress addr = null; //the slave's address
                            try {
                                addr = InetAddress.getByName("192.168.5.44");
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            int port = Modbus.DEFAULT_PORT;
                            int ref = 700; //the reference; offset where to start reading from
                            int count = 10; //the number of DI's to read
                            int repeat = 0; //a loop for repeating the transaction

                            //2. Open the connection
                            con = new TCPMasterConnection(addr);
                            con.setPort(port);
                            try {
                                con.connect();
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            //3. Prepare the request
                            req = new WriteSingleRegisterRequest();

                            //4. Prepare the transaction
                            trans = new ModbusTCPTransaction(con);
                            trans.setRequest(req);

                            //5. Execute the transaction repeat times
                            int k = 0;
                            do {
                                try {
                                    trans.execute();
                                } catch (ModbusException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Digital Inputs Status=" + res.getRegisterValue());
                                k++;
                            } while (k < repeat);

                            //6. Close the connection
                            con.close();


                        }
                    };
                    Thread mythread = new Thread(runnable);
                    mythread.start();
                }
            });
        }

        Intent intent = new Intent(this,SwapService.class);
        startService(intent);

        boolean b = bindService(intent,conexao, Context.BIND_AUTO_CREATE);

    }

    public void onClickStart(){
        if (interfaceSwap != null){
            interfaceSwap.start(new ModbusConfiguration()); // testar padrão
        }
    }

    public void stop(){
        if (interfaceSwap != null){
            interfaceSwap.stop();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (interfaceSwap != null && interfaceSwap.isRunning()){
            unbindService(conexao);
        }else {
            unbindService(conexao);
            stopService(new Intent(this,SwapService.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position) {
                case 0:
                    f = ConnectionConfigurationFragment.newInstance();
                    break;
                case 1:
                    f = SlaveConfigurationFragment.newInstance();
                    break;
                case 2:
                    f = StatusFragment.newInstance();
                    break;
                default:
                    break;

            }

            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CONFIGURAÇÃO";
                case 1:
                    return "REGISTROS";
                case 2:
                    return "ESTADO";
            }
            return null;
        }
    }
}
