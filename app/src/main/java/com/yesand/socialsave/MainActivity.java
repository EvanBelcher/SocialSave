package com.yesand.socialsave;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        if( savedInstanceState == null)
        {
            addFragment(new TabMainFragment(), TabMainFragment.TAG);
            // new instance of the different fragments
        }

    }

    protected void addFragment(Fragment fragment, String tag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        if ( fragmentTransaction.isAddToBackStackAllowed() && fragmentManager.getFragments() != null)
        {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();

    }

}
