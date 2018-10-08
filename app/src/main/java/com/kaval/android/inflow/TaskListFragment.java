package com.kaval.android.inflow;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class TaskListFragment extends ListFragment {


    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    SimpleAdapter adapter;

    private final TaskState state;


    public TaskListFragment(TaskState state) {
        this.state= state;
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {

                Toast.makeText(getActivity(), pos+ " - posClicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String getFormattedDateFromTimeStamp(long timestampInMilliSeconds) {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(timestampInMilliSeconds);  //here your time in miliseconds
        return "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + cl.get(Calendar.MONTH) + ":" + cl.get(Calendar.YEAR) + "  " + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        new AsyncTask<Void, Void, List<Task>>() {
            @Override
            protected List<Task> doInBackground(Void... voids) {
//                return AppDatabase.getInstance(getContext()).taskDao().getAll();
                return new ArrayList<Task>();
            }

            protected void onPostExecute(List<Task> tasks) {
                updateView(tasks);
            }
        }.execute();


        HashMap<String, String> map = new HashMap<String, String>();


        //KEYS IN MAP
        String[] from = {"Name", "Date"};

        //IDS OF VIEWS
        int[] to = {R.id.task_listitem_name_textview, R.id.task_listitem_date_textview};



        //ADAPTER
        adapter = new SimpleAdapter(getActivity(), data, R.layout.fragment_main, from, to);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void updateView(List<Task> tasks){
        HashMap<String, String> map = new HashMap<String, String>();
        //FILL
        for (int i = 0; i < tasks.size(); i++) {
            map = new HashMap<String, String>();
            map.put("Name", tasks.get(i).getName());
            map.put("Duration", getFormattedDateFromTimeStamp(tasks.get(i).getDuration()));

            data.add(map);
        }

        //KEYS IN MAP
        String[] from = {"Name", "Date"};

        //IDS OF VIEWS
        int[] to = {R.id.task_listitem_name_textview, R.id.task_listitem_date_textview};
        adapter = new SimpleAdapter(getActivity(), data, R.layout.fragment_main, from, to);
        setListAdapter(adapter);
    }

}
