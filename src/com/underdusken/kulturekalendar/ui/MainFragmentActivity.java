package com.underdusken.kulturekalendar.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TabHost;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.mainhandler.MainHandler;

import java.util.HashMap;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments.  It uses a trick (see the code below) to allow
 * the tabs to switch between fragments instead of simple views.
 */
public class MainFragmentActivity extends FragmentActivity {
    TabHost mTabHost;
    TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_view);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);



        mTabManager.addTab(mTabHost.newTabSpec("Featured").setIndicator(getString(R.string.tab1)),
                Tab1Fragments.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("All").setIndicator(getString(R.string.tab2)),
                Tab2Fragments.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("Free").setIndicator(getString(R.string.tab3)),
                Tab3Fragments.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("My").setIndicator(getString(R.string.tab4)),
                Tab4Fragments.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("Favorites").setIndicator(getString(R.string.tab5)),
                Tab4Fragments.class, null);



        int imageViewIndex = 1;
        if (Build.VERSION.SDK_INT >= 15) {
            imageViewIndex = 0;
        }

        // Set tabs Image
        ViewGroup idView = (ViewGroup) mTabHost.getTabWidget().getChildAt(0);
        ImageView imageView = (ImageView) idView.getChildAt(imageViewIndex);
        imageView.setImageResource(R.drawable.ic_tab_important);

        idView = (ViewGroup) mTabHost.getTabWidget().getChildAt(1);
        imageView = (ImageView) idView.getChildAt(imageViewIndex);
        imageView.setImageResource(R.drawable.ic_tab_list);

        idView = (ViewGroup) mTabHost.getTabWidget().getChildAt(2);
        imageView = (ImageView) idView.getChildAt(imageViewIndex);
        imageView.setImageResource(R.drawable.ic_tab_user);

        idView = (ViewGroup) mTabHost.getTabWidget().getChildAt(3);
        imageView = (ImageView) idView.getChildAt(imageViewIndex);
        imageView.setImageResource(R.drawable.ic_tab_setup);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        MainHandler.getInstance(this).onStartApplication();
    }
    @Override
    protected void onPause() {
       super.onPause();
       MainHandler.getInstance(this).onCloseApplication();
    }







    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.tab4_checkbox_1:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_2:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_3:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_4:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_5:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_6:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_7:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
}