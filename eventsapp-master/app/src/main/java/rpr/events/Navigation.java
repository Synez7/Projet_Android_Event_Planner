package rpr.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;



// Activité pour la barre de navigation latérale
public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    //private static final String TAG_USERNAME = "username";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";
    public static Fragment f;
    public static Fragment f2;
    public static Fragment f3;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();

        FirebaseMessaging.getInstance().subscribeToTopic("Test");
        FirebaseMessaging.getInstance().getToken();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


        TextView usernameView = (TextView) header.findViewById(R.id.tvUsername);
        TextView emailView = (TextView) header.findViewById(R.id.tvEmail);

        HashMap<String, String> user = session.getUserDetails();

        String username = user.get(UserSessionManager.KEY_USERNAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);

        String usertype = user.get(UserSessionManager.KEY_USERTYPE);

        // User => (Pas de possibilité d'organiser un événement ou d'en éditer un)
        if(usertype.equals("User")){
            this.hideItem();
        }

        if(usertype.equals("Provider")){
            NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = nav.getMenu();
            nav_Menu.findItem(R.id.nav_agenda).setVisible(false);
            nav_Menu.findItem(R.id.nav_bookmarks).setVisible(false);

        }


        usernameView.setText(username);
        emailView.setText(email);
        displaySelectedScreen(R.id.nav_list_events);




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PastEvent.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void hideItem() {
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = nav.getMenu();
        nav_Menu.findItem(R.id.nav_organise_event).setVisible(false);
        nav_Menu.findItem(R.id.nav_manage_organise_event).setVisible(false);

    }

    private void displaySelectedScreen(int itemId) {

        //Initilisation d'un fragment de défilement suivant l'item cliqué sur la barre de
        // navigation latérale
        Fragment fragment = null;

        //Fragment défilé suivant l'item id
        switch (itemId) {
            case R.id.nav_list_events:
                fragment = new ListEventsTabs();
                f = fragment;
                break;
            case R.id.nav_bookmarks:
                fragment = new BookmarksList();
                f3 = fragment;
                break;
            case R.id.nav_organise_event:
            fragment = new OrganiseEvent();
            break;
            case R.id.nav_manage_organise_event:
                fragment = new OrganisedList();
                break;

            case R.id.nav_manage_bookings:
                fragment = new BookingsList();
                f2 = fragment;
                break;

            case R.id.nav_agenda:
                Uri calendarUri = CalendarContract.CONTENT_URI
                        .buildUpon()
                        .appendPath("time")
                        .build();
                startActivity(new Intent(Intent.ACTION_VIEW, calendarUri));
                break;

            case R.id.nav_profile_manage:
                fragment = new UserProfile();
                break;
            case R.id.nav_logout:
                session.logoutUser();
                finish();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}