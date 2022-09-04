package rpr.events;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// Fragment de recherche des events
public class ListEventsTabs extends Fragment{


    private Context context = null;

    public SQLiteDatabase db;

    RequestQueue queue;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    public static BottomNavigationView bottomNav;

    public static Fragment active;

    public static Fragment fav;
    public static Fragment listEvents;
    public static Fragment listBookings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.activity_main2,null);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        bottomNav = (BottomNavigationView) x.findViewById(R.id.bottom_navigation);


        final Fragment fragment1 = new ListEventsTabs();
        final Fragment fragment2 = new BookmarksList();
        final Fragment fragment3 = new OrganiseEvent();
        final Fragment fragment4 = new UserProfile();
        final Fragment fragment5 = new BookingsList();
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        active = fragment1;



        bottomNav.setSelectedItemId(R.id.home);
        UserSessionManager session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        Menu nav_Menu = bottomNav.getMenu();
        if(user.get(UserSessionManager.KEY_USERTYPE).equals("User")){
            nav_Menu.findItem(R.id.nav_newEvent).setVisible(false);
            nav_Menu.findItem(R.id.nav_bookings).setVisible(true);
        }

        if(user.get(UserSessionManager.KEY_USERTYPE).equals("Provider")){
            nav_Menu.findItem(R.id.nav_favorites).setVisible(false);
        }



        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        listEvents = fragment1;
                        ft.replace(R.id.fragment_container2, active);
                        ft.commit();
                        return true;

                    case R.id.nav_favorites:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        fav = fragment2;
                        ft.replace(R.id.fragment_container2, active);
                        ft.commit();
                        tabLayout.setVisibility(View.INVISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        return true;


                    case R.id.nav_newEvent:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        ft.replace(R.id.fragment_container2, active);
                        ft.commit();
                        tabLayout.setVisibility(View.INVISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        return true;

                    case R.id.nav_search:
                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;
                        ft.replace(R.id.fragment_container2, active);
                        ft.commit();
                        tabLayout.setVisibility(View.INVISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        return true;

                    case R.id.nav_bookings:
                        fm.beginTransaction().hide(active).show(fragment5).commit();
                        active = fragment5;
                        listBookings = fragment5;
                        ft.replace(R.id.fragment_container2, active);
                        ft.commit();
                        tabLayout.setVisibility(View.INVISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        return true;

                }

                return false;
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search Events");

        context = getActivity();
        queue = Volley.newRequestQueue(context);
        createDatabase();
        getcategory();


        return x;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null){

            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        if(db != null){
            db.close();
        }
    }

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB1", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS category (category_id INTEGER NOT NULL, name VARCHAR NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS usertype (usertype_id INTEGER NOT NULL, name VARCHAR NOT NULL);");

    }

    protected void insertIntoDBcategory(String category_id, String name){
        String query = "INSERT INTO category (category_id,name) VALUES('"+category_id+"', '"+name+"');";
        db.execSQL(query);
    }

    protected void insertIntoDBusertype(String usertype_id, String name){
        String query = "INSERT INTO usertype (usertype_id,name) VALUES('"+usertype_id+"', '"+name+"');";
        db.execSQL(query);
    }


    private void getcategory(){

        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, "https://eventplannerapp.000webhostapp.com/category.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                JSONArray result = jsonResponse.getJSONArray("category");
                                ArrayList<String> categories = new ArrayList<>();

                                db.execSQL("DELETE FROM category");
                                for(int i=0;i<result.length();i++) {

                                    JSONObject obj = result.getJSONObject(i);

                                    categories.add(obj.getString("name"));
                                    insertIntoDBcategory(obj.getString("category_id"), obj.getString("name"));

                                }
                                viewPager.setAdapter(new dynamicAdapter(getChildFragmentManager(),categories));

                                tabLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tabLayout.setupWithViewPager(viewPager);
                                    }
                                });
                            }
                            else{

                                use_old_category();
                            }




                        } catch (JSONException e) {
                            use_old_category();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Couldn't connect to internet",Toast.LENGTH_SHORT).show();

                        use_old_category();
                    }
                });


        queue.add(usertypeRequest);

    }

    @SuppressLint("Range")
    private void use_old_category(){
        String query = "SELECT * FROM category ORDER BY category_id ASC";
        Cursor recordSet = db.rawQuery(query, null);
        if (recordSet.getCount() == 0){
            db.execSQL("DELETE FROM category");
            // Affichage de la suite de catégories d'event au dessus de la search view
            insertIntoDBcategory("0", "All");
            insertIntoDBcategory("1", "Cultural");
            insertIntoDBcategory("2", "Technical");
            insertIntoDBcategory("3", "Sports");
            insertIntoDBcategory("4", "Seminar");

            recordSet = db.rawQuery(query, null);
        }
        recordSet.moveToFirst();
        ArrayList<String> categories = new ArrayList<>();
        while (!recordSet.isAfterLast()) {


            categories.add(recordSet.getString(recordSet.getColumnIndex("name")));
            recordSet.moveToNext();

        }
        recordSet.close();
        db.close();

        viewPager.setAdapter(new dynamicAdapter(getChildFragmentManager(),categories));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    private void getusertypes(){


        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, "https://eventplannerapp.000webhostapp.com/usertype.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                JSONArray result = jsonResponse.getJSONArray("usertype");

                                db.execSQL("DELETE FROM usertype");
                                for(int i=0;i<result.length();i++) {

                                    JSONObject obj = result.getJSONObject(i);

                                    insertIntoDBusertype(obj.getString("usertype_id"), obj.getString("name"));

                                }


                            }
                            else{

                            }




                        } catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        queue.add(usertypeRequest);

    }

        class dynamicAdapter extends FragmentPagerAdapter{

        ArrayList<String> pass_category;

        public dynamicAdapter(FragmentManager fm, ArrayList<String> category) {
            super(fm);
            this.pass_category = category;


        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = new DisplayEventList();
            Bundle args = new Bundle();
            args.putInt("category_id", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {

            return pass_category.size();

        }



        @Override
        public CharSequence getPageTitle(int position) {

            return  pass_category.get(position);
        }
    }

}