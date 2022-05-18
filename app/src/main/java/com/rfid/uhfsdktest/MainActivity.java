package com.rfid.uhfsdktest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    private ActionBar toolbar;
    ModuleConnector connector = new ReaderConnector();
    RFIDReaderHelper mReader;

    RXObserver rxObserver = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {
            Log.d("TAG",tag.strEPC);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RFID Tracking System v1.0");
        getSupportActionBar().setIcon(getDrawable(R.drawable.smalllogodsr));


        loadFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationMenu);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        if (connector.connectCom("dev/ttyS4",115200)) {
//            ModuleManager.newInstance().setUHFStatus(true);
//            try {
//                mReader = RFIDReaderHelper.getDefaultHelper();
//                mReader.registerObserver(rxObserver);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    //toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.profile:
                   // toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.settings:
                   // toolbar.setTitle("Settings");
                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
            }

            //return false;
        }

    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReader != null) {
            mReader.unRegisterObserver(rxObserver);
        }
        if (connector != null) {
            connector.disConnect();
        }

        ModuleManager.newInstance().setUHFStatus(false);
        ModuleManager.newInstance().release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
        return true;
    }
}
