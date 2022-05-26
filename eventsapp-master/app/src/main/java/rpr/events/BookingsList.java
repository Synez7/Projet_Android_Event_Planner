package rpr.events;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BookingsList extends Fragment {


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
    private static String usertype;
    private static String creator_id;
    private static String nameParticipant;
    public static String nameBooker;
    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private GridLayoutManager gridLayoutManager;
    private BookingAdapter adapter;
    private TextView counter;

    private int loadlimit;
    boolean loading;
    boolean error_load;
    RequestQueue queue;


    ArrayList<eventItem> eventList;
    ArrayList<BookingItem> bookingList;
    public static ArrayList<String> bookerList = new ArrayList<>();

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS Bookings (event_id INTEGER NOT NULL, name VARCHAR NOT NULL, time VARCHAR NOT NULL, venue VARCHAR NOT NULL, details VARCHAR NOT NULL, usertype VARCHAR NOT NULL,creator_id INTEGER NOT NULL, creator VARCHAR NOT NULL, category_id INTEGER NOT NULL, category VARCHAR NOT NULL, image VARCHAR NOT NULL,time_end VARCHAR NOT NULL, price INTEGER NOT NULL, attendance INTEGER NOT NULL, nameParticipant VARCHAR NOT NULL, confirm INTEGER NOT NULL, confirmDate VARCHAR NOT NULL);");


    }

    protected void insertIntoDB(BookingItem b){

        String query = "INSERT INTO Bookings (event_id,name,time,venue,details,usertype,creator_id,creator,category_id,category,image,time_end,price,attendance,nameParticipant,confirm,confirmDate) VALUES('"+b.getEvent_id()+"','"+b.getName()+"','"+b.getTime()+"','"+b.getVenue()+"', '"+b.getDetails()+"','"+b.getUsertype()+"','"+b.getCreator_id()+"', '"+b.getCreator()+"', '"+b.getCategory_id()+"', '"+b.getCategory()+"','"+b.getImage()+"','"+b.getTime_end()+"','"+b.getPrice()+"','"+b.getAttendance()+"','"+b.getNameParticipant()+"','"+b.getConfirm()+"','"+b.getConfirmDate()+"');";

        db.execSQL(query);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        queue = Volley.newRequestQueue(context);
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        usertype = user.get(UserSessionManager.KEY_USERTYPE);
        nameParticipant = user.get(UserSessionManager.KEY_USERNAME);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Manage bookings");
        createDatabase();
        return inflater.inflate(R.layout.display_bookings_list_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        counter = (TextView) getView().findViewById(R.id.counter);
        searchView = (SearchView) getView().findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(context,bookingList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), 0));


    }


    private void filterList(String newText) {
        ArrayList<BookingItem> filteredList = new ArrayList<>();
        for(BookingItem b : bookingList){
            if(b.getName().toLowerCase().contains(newText.toLowerCase()) ||
                    b.getVenue().toLowerCase().contains(newText.toLowerCase()) ||
                    b.getTime().toLowerCase().contains(newText.toLowerCase()) ||
                    b.getTime_end().toLowerCase().contains(newText.toLowerCase()) ){
                filteredList.add(b);
            }

            if(filteredList.size() == 1){
                counter.setText(filteredList.size() + " booking found.");
            }

            if(filteredList.size() > 1){
                counter.setText(filteredList.size() + " bookings found.");
            }

        }
        if(filteredList.isEmpty()){
            Toast.makeText(context,"No data found !",Toast.LENGTH_LONG).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bookingList.clear();
        adapter.notifyDataSetChanged();

        loadlimit = 0;
        loading = false;
        error_load = false;
        load_data_from_server(loadlimit++);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(!error_load && !loading && gridLayoutManager.findLastCompletelyVisibleItemPosition() == bookingList.size()-1){
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

        StringRequest eventRequest= new StringRequest(Request.Method.POST,"https://eventplannerapp.000webhostapp.com/listBookings.php",
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
                                db.execSQL("DELETE FROM Bookings");
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    BookingItem data = new BookingItem(e.getInt("event_id"),e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"),e.getString("image"),e.getString("time_end"),e.getInt("price"),e.getInt("attendance"),e.getString("nameParticipant"),e.getInt("confirm"),e.getString("confirmDate"));
                                    //nameBooker = e.getString("nameParticipant");
                                    //bookerList.add(nameBooker);
                                    bookingList.add(data);
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
                params.put("usertype",usertype);
                params.put("nameParticpant",nameParticipant);
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
        String query = "SELECT * FROM Bookings";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();


        while (!recordSet.isAfterLast()) {

            //@SuppressLint("Range") eventItem data = new eventItem(recordSet.getInt(recordSet.getColumnIndex("event_id")),recordSet.getString(recordSet.getColumnIndex("name")),recordSet.getString(recordSet.getColumnIndex("time")),recordSet.getString(recordSet.getColumnIndex("venue")),recordSet.getString(recordSet.getColumnIndex("details")),recordSet.getString(recordSet.getColumnIndex("usertype")),recordSet.getInt(recordSet.getColumnIndex("creator_id")),recordSet.getString(recordSet.getColumnIndex("creator")),recordSet.getInt(recordSet.getColumnIndex("category_id")),recordSet.getString(recordSet.getColumnIndex("category")),recordSet.getString(recordSet.getColumnIndex("image")),recordSet.getString(recordSet.getColumnIndex("time_end")),recordSet.getInt(recordSet.getColumnIndex("price")),recordSet.getInt(recordSet.getColumnIndex("attendance")));
            //eventList.add(data);
            @SuppressLint("Range") BookingItem data = new BookingItem(recordSet.getInt(recordSet.getColumnIndex("event_id")),recordSet.getString(recordSet.getColumnIndex("name")),recordSet.getString(recordSet.getColumnIndex("time")),recordSet.getString(recordSet.getColumnIndex("venue")),recordSet.getString(recordSet.getColumnIndex("details")),recordSet.getString(recordSet.getColumnIndex("usertype")),recordSet.getInt(recordSet.getColumnIndex("creator_id")),recordSet.getString(recordSet.getColumnIndex("creator")),recordSet.getInt(recordSet.getColumnIndex("category_id")),recordSet.getString(recordSet.getColumnIndex("category")),recordSet.getString(recordSet.getColumnIndex("image")),recordSet.getString(recordSet.getColumnIndex("time_end")),recordSet.getInt(recordSet.getColumnIndex("price")),recordSet.getInt(recordSet.getColumnIndex("attendance")), recordSet.getString(recordSet.getColumnIndex("nameParticipant")),recordSet.getInt(recordSet.getColumnIndex("confirm")),recordSet.getString(recordSet.getColumnIndex("confirmDate")));
            bookingList.add(data);
            recordSet.moveToNext();

        }
        recordSet.close();
        adapter.notifyDataSetChanged();
    }




}