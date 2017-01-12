package com.yesand.socialsave;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by puyus on 1/12/2017.
 */

public class SocialPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();
    private final int NUMBER_OF_TABS = 3;
    public SocialPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                  arrayList.add(new TeamFragment());
                break;
            case 1:
                arrayList.add(new UserFragment());
                break;
            case 2:
                arrayList.add(new TransFragment());
                break;
            default:
                    return null;
        }
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    public int getIconDefaultAtPosition(int position) {
        int icon;
        switch (position) {
            case 0:
                icon = R.mipmap.ic_launcher;
                break;
            case 1:
                icon = R.mipmap.ic_launcher;
                break;
            case 2:
                icon = R.mipmap.ic_launcher;
                break;
            default:
                icon = 0;
        }

        return icon;
    }

    public int getIconHighLightAtPosition(int position) {
        int icon;
        switch (position) {
            case 0:
                icon = R.mipmap.ic_launcher;
                break;
            case 1:
                icon = R.mipmap.ic_launcher;
                break;
            case 2:
                icon = R.mipmap.ic_launcher;
                break;
            default:
                icon = 0;
        }

        return icon;
    }
    public CharSequence getPageTitle(int position) {
        String pageTitle;
        switch (position) {
            case 0:
                pageTitle = "Team";
                break;
            case 1:
                pageTitle = "User";
                break;
            case 2:
                pageTitle = "Transaction";
                break;
            default:
                pageTitle = null;
        }

        return pageTitle;
    }

    public Fragment getRegisteredFragment(int position) {
        return arrayList.get(position);
    }

}
