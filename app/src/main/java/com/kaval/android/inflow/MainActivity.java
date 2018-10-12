package com.kaval.android.inflow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    MenuItem menuDelete;
    MenuItem menuToBacklog;
    MenuItem menuToDone;
    MenuItem menuToProgress;
    Toolbar toolbar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int selectedTab = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                EventBus.getDefault().post(new Event.ChangeSelectedTabEvent(TaskState.getFromPosition(i)));
                if (selectedTab != i) {
                    hideMenuItems();
                }
                selectedTab = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addNewTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void hideMenuItems() {
        menuDelete.setVisible(false);
        menuToBacklog.setVisible(false);
        menuToDone.setVisible(false);
        menuToProgress.setVisible(false);
        menuDelete.setVisible(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.ModeChangeTaskStateEvent event) {
        for (TaskState state : event.possibleStates) {
            switch (state) {
                case IN_PROGRESS:
                    menuToProgress.setVisible(true);
                    break;
                case TODO:
                    menuToBacklog.setVisible(true);
                    break;
                case DONE:
                    menuToDone.setVisible(true);
                    break;
                default:
                    throw new IllegalArgumentException("wrong state in possible state list:" + state);
            }
        }
        menuDelete.setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuDelete = menu.findItem(R.id.action_delete);
        menuDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventBus.getDefault().post(new Event.DeleteCurrentSelectedItemEvent());
                hideMenuItems();
                return false;
            }
        });
        menuToBacklog = menu.findItem(R.id.action_to_backlog);
        menuToBacklog.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventBus.getDefault().post(new Event.MoveCurrentSelectedItemEvent(TaskState.TODO));
                hideMenuItems();
                return false;
            }
        });
        menuToDone = menu.findItem(R.id.action_to_done);
        menuToDone.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventBus.getDefault().post(new Event.MoveCurrentSelectedItemEvent(TaskState.DONE));
                hideMenuItems();
                return false;
            }
        });
        menuToProgress = menu.findItem(R.id.action_to_progress);
        menuToProgress.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventBus.getDefault().post(new Event.MoveCurrentSelectedItemEvent(TaskState.IN_PROGRESS));
                hideMenuItems();
                return false;
            }
        });
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new TaskListFragment(TaskState.TODO);
                case 1:
                    return new TaskListFragment(TaskState.IN_PROGRESS);
                case 2:
                    return new TaskListFragment(TaskState.DONE);
            }
            return null;
        }

//        public void refreshFragmentAdapters() {
//            ((TaskListFragment) getItem(mViewPager.getCurrentItem())).refreshAdapter();
//        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewPager.getCurrentItem() == 0 || mViewPager.getCurrentItem() == 2) {
            TaskListFragment frag1 = (TaskListFragment) mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, 1);
            frag1.updateAdapter();
        }
        TaskListFragment frag1 = (TaskListFragment) mViewPager
                .getAdapter()
                .instantiateItem(mViewPager, mViewPager.getCurrentItem());
        frag1.updateAdapter();
    }

    @Override
    public void onBackPressed() {
        TaskListFragment fragment = (TaskListFragment) mViewPager
                .getAdapter()
                .instantiateItem(mViewPager, mViewPager.getCurrentItem());
        if(!fragment.onBackPressed()){
            super.onBackPressed();
        }
    }
}
