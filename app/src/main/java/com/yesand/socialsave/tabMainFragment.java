package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by puyus on 1/12/2017.
 */

@SuppressWarnings("ConstantConditions")
public class TabMainFragment extends Fragment {

    View view;

    public final static String TAG = TabMainFragment.class.getSimpleName();
    private SocialPageAdapter pageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to save data with roation
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (view == null) {
            Log.e(TAG, "The View is Null");
            return;
        }

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.view = view;

        if (toolbar == null) {
            return;
        }
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setUpTab_Layout(tabLayout);
        setUpViewPager(tabLayout, viewPager, toolbar);
        setupTabLayoutListener(toolbar, tabLayout, viewPager);
        Log.e(TAG, viewPager.getCurrentItem() + "The CurrentItem");
        tabLayout.getTabAt(viewPager.getCurrentItem()).setIcon(ResourcesCompat.getDrawable(getResources(), pageAdapter.getIconHighLightAtPosition(viewPager.getCurrentItem()), null));
    }

    private void setUpTab_Layout(TabLayout tab_layout) {
        tab_layout.addTab(tab_layout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.group_icon, null)));
        tab_layout.addTab(tab_layout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.user_logo, null)));
        tab_layout.addTab(tab_layout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.dollar_icon, null)));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    public void setUpViewPager(TabLayout tabLayout, ViewPager viewPager, Toolbar toolbar) {
        // loading pages close by
        viewPager.setOffscreenPageLimit(3);
        // tells which screen u are on
        pageAdapter = new SocialPageAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new FragmentPageChangeListener(tabLayout, toolbar, (SocialPageAdapter) viewPager.getAdapter()));

    }

    private void setupTabLayoutListener(final Toolbar toolbar, final TabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(pageAdapter.getPageTitle(tab.getPosition()));
                tabLayout.getTabAt(tab.getPosition()).setIcon(ResourcesCompat.getDrawable(getResources(), pageAdapter.getIconHighLightAtPosition(tab.getPosition()), null));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).setIcon(
                        ResourcesCompat.getDrawable(getResources(), pageAdapter.getIconDefaultAtPosition(tab.getPosition()), null));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public class FragmentPageChangeListener implements ViewPager.OnPageChangeListener {

        private final WeakReference<Toolbar> mToolbarRef;

        private final WeakReference<TabLayout> mTabLayoutRef;

        private final WeakReference<SocialPageAdapter> mPagerAdapterRef;

        private int mScrollState;

        FragmentPageChangeListener(TabLayout tabLayout, Toolbar toolbar, SocialPageAdapter pagerAdapter) {
            this.mTabLayoutRef = new WeakReference(tabLayout);
            this.mToolbarRef = new WeakReference(toolbar);
            this.mPagerAdapterRef = new WeakReference(pagerAdapter);
        }

        public void onPageScrollStateChanged(int state) {
            this.mScrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                tabLayout.setScrollPosition(position, positionOffset, this.mScrollState == 1);
            }
        }

        public void onPageSelected(int position) {
            TabLayout tabLayout = mTabLayoutRef.get();
            SocialPageAdapter pageAdapter = mPagerAdapterRef.get();
            if (tabLayout != null && pageAdapter != null) {
                tabLayout.getTabAt(position).select();
                tabLayout.getTabAt(position).setIcon(ResourcesCompat.getDrawable(getResources(), pageAdapter.getIconHighLightAtPosition(position), null));
            }

            Toolbar toolbar = mToolbarRef.get();
            if (toolbar != null && pageAdapter != null) {
                toolbar.setTitle(pageAdapter.getPageTitle(position));
            }
        }
    }

}


