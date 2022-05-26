package rpr.events;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// Fragment des events créés par l'organisateur
public class OrganisedList extends Fragment {
    public SQLiteDatabase db;
    UserSessionManager session;

    private static Context context = null;
    private static final String TAG = UserProfile.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";
    private static String user_id;

    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private eventAdapter adapter;

    private int loadlimit;
    boolean loading;
    boolean error_load;
    RequestQueue queue;
    ArrayList<eventItem> eventList;

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB1", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS organisedEvents (event_id INTEGER NOT NULL, name VARCHAR NOT NULL, time VARCHAR NOT NULL, venue VARCHAR NOT NULL, details VARCHAR NOT NULL, usertype VARCHAR NOT NULL, creator_id INTEGER NOT NULL, creator VARCHAR NOT NULL, category_id INTEGER NOT NULL, category VARCHAR NOT NULL, image VARCHAR,time_end VARCHAR NOT NULL, price INTEGER NOT NULL, attendance INTEGER NOT NULL);");

    }

    protected void insertIntoDB(eventItem e){

        String query = "INSERT INTO organisedEvents (event_id,name,time,venue,details,usertype,creator_id,creator,category_id,category,image,time_end,price,attendance) VALUES('"+e.getEvent_id()+"','"+e.getName()+"','"+e.getTime()+"','"+e.getVenue()+"', '"+e.getDetails()+"','"+e.getUsertype()+"','"+e.getCreator_id()+"', '"+e.getCreator()+"', '"+e.getCategory_id()+"', '"+e.getCategory()+"','"+e.getImage()+"','"+e.getTime_end()+"','"+e.getPrice()+"','"+e.getAttendance()+"');";

        db.execSQL(query);
    }
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        queue = Volley.newRequestQueue(context);
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Manage organized events");
        createDatabase();
        return inflater.inflate(R.layout.display_bookmark_list_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        eventList = new ArrayList<>();
        adapter = new eventAdapter(context,eventList, true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), 0));


    }

    @Override
    public void onStart() {
        super.onStart();
        eventList.clear();
        adapter.notifyDataSetChanged();

        loadlimit = 0;
        loading = false;
        error_load = false;
        load_data_from_server(loadlimit++);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(!error_load && !loading && gridLayoutManager.findLastCompletelyVisibleItemPosition() == eventList.size()-1){
                    loading = true;
                    load_data_from_server(loadlimit++);
                }

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null){
            db.close();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    private void load_data_from_server(final int id) {
        final ProgressDialog dialog = new ProgressDialog(context);

        StringRequest eventRequest= new StringRequest(Request.Method.POST, getResources().getString(R.string.listOrganised_url),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                JSONArray array = jsonResponse.getJSONArray("events");
                                db.execSQL("DELETE FROM organisedEvents");
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    eventItem data = new eventItem(e.getInt("event_id"),e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"),e.getString("image"),e.getString("time_end"),e.getInt("price"),e.getInt("attendance"));
                                    eventList.add(data);
                                    insertIntoDB(data);

                                }

                                adapter.notifyDataSetChanged();
                                loading = false;

                            } else {
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                if (!error_load){
                    error_load = true;
                    use_old_data();
                }


            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("limit", id+"");
                params.put("user_id", user_id+"");
                return params;
            }

        };

        queue.add(eventRequest);


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void use_old_data(){
        String query = "SELECT * FROM organisedEvents";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();


        while (!recordSet.isAfterLast()) {

            @SuppressLint("Range") eventItem data = new eventItem(recordSet.getInt(recordSet.getColumnIndex("event_id")),recordSet.getString(recordSet.getColumnIndex("name")),recordSet.getString(recordSet.getColumnIndex("time")),recordSet.getString(recordSet.getColumnIndex("venue")),recordSet.getString(recordSet.getColumnIndex("details")),recordSet.getString(recordSet.getColumnIndex("usertype")),recordSet.getInt(recordSet.getColumnIndex("creator_id")),recordSet.getString(recordSet.getColumnIndex("creator")),recordSet.getInt(recordSet.getColumnIndex("category_id")),recordSet.getString(recordSet.getColumnIndex("category")),recordSet.getString(recordSet.getColumnIndex("image")),recordSet.getString(recordSet.getColumnIndex("time_end")),recordSet.getInt(recordSet.getColumnIndex("price")),recordSet.getInt(recordSet.getColumnIndex("attendance")));
            // Ajout d'un event créé par l'organisateur
            eventList.add(data);
            recordSet.moveToNext();

        }
        recordSet.close();
        adapter.notifyDataSetChanged();
    }
}

