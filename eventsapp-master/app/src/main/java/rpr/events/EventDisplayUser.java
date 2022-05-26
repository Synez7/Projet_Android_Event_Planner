package rpr.events;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Classe qui matérialise tout le descriptif d'un événement lorsque
// l'utilisateur clique sur sa carte correspondante à l'écran
public class EventDisplayUser extends AppCompatActivity {

    private int event_id;
    private String name;
    private String time;
    private String venue;
    private String details;
    private int usertype_id;
    private String usertype;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;
    private String time_end;
    private String image;
    private int price;
    private int attendance;

    private String nameParticpant;
    private String nameParticpant2;

    private int confirmBook;
    private String confirmDate;


    TextView nametv;
    TextView datetv;
    TextView datetv2;
    TextView timetv;
    TextView timetv2;
    TextView venuetv;
    TextView pricetv;
    TextView attendancetv;
    TextView organisertv;
    TextView detailstv;
    TextView categorytv;
    TextView participanttv;
    TextView waitConfirm;
    ImageView imgPerson;
    ImageView imgIcon;
    ImageView confirmBooking;
    Button bookmark;
    Button addcal;
    Button booking;
    Button confirm;
    Button cancel;

    UserSessionManager session;
    private static String user_id;
    RequestQueue queue;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final LinearLayout layout = (LinearLayout)findViewById(R.id.buttonPanel);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) findViewById(R.id.datapanel).getLayoutParams();
                mlp.setMargins(0,0,0,layout.getHeight());
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });




        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(UserSessionManager.KEY_USER_ID);


        nametv = (TextView) findViewById(R.id.tvname);
        datetv = (TextView) findViewById(R.id.tvDate);
        datetv2 = (TextView) findViewById(R.id.tvDate2);

        timetv = (TextView) findViewById(R.id.tvTime);
        timetv2 = (TextView) findViewById(R.id.tvTime2);
        venuetv = (TextView) findViewById(R.id.tvVenue);
        pricetv = (TextView) findViewById(R.id.tvPrice);
        attendancetv = (TextView) findViewById(R.id.tvAttendance);
        organisertv = (TextView) findViewById(R.id.tvOrganiser);
        detailstv = (TextView) findViewById(R.id.tvdetails);
        categorytv = (TextView) findViewById(R.id.tvCategory);
        participanttv  = (TextView) findViewById(R.id.tvBookingPerson);
        waitConfirm = (TextView) findViewById(R.id.textView2);
        bookmark = (Button) findViewById(R.id.tvBookmark);
        addcal = (Button) findViewById(R.id.tvAddCalender);
        booking = (Button) findViewById(R.id.tvBooking);
        confirm = (Button) findViewById(R.id.tvConfirm);
        cancel = (Button) findViewById(R.id.tvReject);
        imgPerson = (ImageView) findViewById(R.id.chkState6);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);

        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id",0);
        name =  intent.getStringExtra("name");
        time =  intent.getStringExtra("time");
        time_end =  intent.getStringExtra("time_end");
        venue =  intent.getStringExtra("venue");
        price =  intent.getIntExtra("price",0);
        attendance =  intent.getIntExtra("attendance",0);
        details =  intent.getStringExtra("details");
        usertype_id =  intent.getIntExtra("usertype_id",0);
        usertype =  intent.getStringExtra("usertype");
        creator_id =  intent.getIntExtra("creator_id",0);
        creator =  intent.getStringExtra("creator");
        category_id =  intent.getIntExtra("category_id",0);
        category =  intent.getStringExtra("category");
        image = intent.getStringExtra("image");

        nameParticpant2 = user.get(UserSessionManager.KEY_USERNAME);
        nameParticpant = intent.getStringExtra("nameParticipant");
        confirmBook = intent.getIntExtra("confirm",0);
        confirmDate = intent.getStringExtra("confirmDate");


        Date date = new Date();
        Date date2 = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nametv.setText(name);
        detailstv.setText(details);
        datetv.setText(new SimpleDateFormat("dd MMM, yyyy").format(date));
        datetv2.setText(new SimpleDateFormat("dd MMM, yyyy").format(date2));
        timetv.setText(new SimpleDateFormat("hh:mm aa").format(date));
        timetv2.setText(new SimpleDateFormat("hh:mm aa").format(date2));
        organisertv.setText(creator);

        imgIcon.setImageURI(Uri.parse(image));

        InputStream f =  this.getClass().getClassLoader().getResourceAsStream(image);
        if(f == null){
            imgIcon.setBackgroundResource(R.drawable.event_people);
        }

        // Si utilisateur connecté est Provider
        if(user.get(UserSessionManager.KEY_USERTYPE).equals("Provider")){
            // Dans le fragment de recherche d'event, le fournisseur
            // peut seulement voir le descriptif de ses events créés
            if(Navigation.f != null && Navigation.f.isVisible()){
            addcal.setVisibility(View.GONE);
            booking.setVisibility(View.GONE);
            bookmark.setVisibility(View.GONE);
            waitConfirm.setVisibility(View.GONE);
            imgPerson.setVisibility(View.GONE);

            confirm.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            }

            // Dans le fragment de gestion des réservations (bookings)
            // le fournisseur peut soit valider soit annuler une réservation d'un User
            if(Navigation.f2 != null && Navigation.f2.isVisible()){

                participanttv.setText("Participant : " + nameParticpant);
                participanttv.setVisibility(View.VISIBLE);

                addcal.setVisibility(View.GONE);
                booking.setVisibility(View.GONE);
                bookmark.setVisibility(View.GONE);
                waitConfirm.setVisibility(View.GONE);


                confirm.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)confirm.getLayoutParams();
                ll.setMargins(120, 20, 12, 22);
                confirm.setHeight(90);
                confirm.setLayoutParams(ll);



            }


        }
        else{
            participanttv.setText("Participant : " + nameParticpant2);
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);

        }

        // Si Utilisateur de type User connecté
        if(user.get(UserSessionManager.KEY_USERTYPE).equals("User")){

            // Dans le fragment de recherche d'events
            // en cliquant sur un event, un User a deux options possibles (Réserver et mettre en favori)
            if(Navigation.f != null && Navigation.f.isVisible()){
                addcal.setVisibility(View.GONE);
                waitConfirm.setVisibility(View.GONE);
                booking.setVisibility(View.VISIBLE);
                bookmark.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)booking.getLayoutParams();
                ll.setMargins(130, 20, 12, 26);
                booking.setHeight(90);
                booking.setLayoutParams(ll);


                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }


            // Dans le fragment de consultation des réservations
            // un User voit le descriptif de l'event reservé +
            // une zone de texte lui indiquant que sa réservation est en attente de confirmation
            if(Navigation.f2 != null && Navigation.f2.isVisible()){


                participanttv.setText("Participant : " + nameParticpant);
                waitConfirm.setText("( Waiting for confirmation )");
                participanttv.setVisibility(View.VISIBLE);

                addcal.setVisibility(View.GONE);
                booking.setVisibility(View.GONE);
                bookmark.setVisibility(View.GONE);


                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);



            }

            // Dans le fragement des évents favoris
            // un User peut enlever un event de ses events favoris
            if(Navigation.f3 != null && Navigation.f3.isVisible()){

                participanttv.setVisibility(View.GONE);
                imgPerson.setVisibility(View.GONE);

                bookmark.setVisibility(View.VISIBLE);
                addcal.setVisibility(View.GONE);
                booking.setVisibility(View.GONE);
                waitConfirm.setVisibility(View.GONE);

                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)bookmark.getLayoutParams();
                ll.setMargins(350, 20, 12, 10);
                bookmark.setHeight(90);
                bookmark.setLayoutParams(ll);



            }



        }

        // Si la réservation faite par un User est confirmée
        // Un check vert s'ajoute en bas de la carte de l'event reservé
        if(confirmBook == 1 ){
            confirm.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            imgPerson.setVisibility(View.GONE);
            bookmark.setVisibility(View.INVISIBLE);
            imgPerson.setBackgroundResource(R.drawable.baseline_check_circle_24);
            participanttv.setText("Confirmed : " + confirmDate);
            addcal.setVisibility(View.VISIBLE);
            waitConfirm.setVisibility(View.GONE);
            addcal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_calendar_month_24, 0);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)addcal.getLayoutParams();
            ll.setMargins(340, 20, 180, 10);
            addcal.setText("Add to my calendar");
        }

        // Quand un Provider confirme une réservation (place déjà réservée)
        // il peut l'annuler
        if(confirmBook == 1 && user.get(UserSessionManager.KEY_USERTYPE).equals("Provider")){
            confirm.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)cancel.getLayoutParams();
            ll.setMargins(10, 20, 3, 10);
            cancel.setHeight(90);
            cancel.setLayoutParams(ll);
            cancel.setGravity(Gravity.CENTER);

            imgPerson.setVisibility(View.VISIBLE);
            imgPerson.setBackgroundResource(R.drawable.baseline_check_circle_24);
            participanttv.setText("Confirmed : " + confirmDate);
            addcal.setVisibility(View.GONE);
            waitConfirm.setVisibility(View.GONE);
        }



        venuetv.setText(venue);
        pricetv.setText(price + " $");
        attendancetv.setText(attendance + " persons");
        categorytv.setText(category);
        final Date finalDate = date;
        final Date finalDate2 = date2;
        // Intent qui lance l'appli Google calendar afin d'ajouter un event dans son agenda
        // et de le consulter
        addcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");


                intent.putExtra(CalendarContract.Events.TITLE, name);
                intent.putExtra(CalendarContract.Events.DESCRIPTION,  details);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);

                Calendar beginTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();

                beginTime.setTime(finalDate);
                long startMillis = beginTime.getTimeInMillis();
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);


                endTime.setTime(finalDate2);
                long startMillis2 = endTime.getTimeInMillis();
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis() + 60 * 60 * 1000);



                startActivity(intent);
            }
        });


        checkbookmark();
        checkbooking();
        // Action de mettre en favori un event
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookmark.getText().equals("Bookmark")){
                    Snackbar.make(view,"Event " + name + " added to your bookmarks",Snackbar.LENGTH_LONG).show();
                }else if(bookmark.getText().equals("Un-Bookmark")){
                    Snackbar.make(view,"Event " + name + " has been removed from your bookmarks",Snackbar.LENGTH_LONG).show();
                }
                toggleBookmark();

            }
        });

        // Action de réserver un event
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booking.getText().equals("Book")){
                    Snackbar.make(view,"Event " + name + " added to your bookings",Snackbar.LENGTH_SHORT).show();
                }
                toggleBooking();
            }
        });

        // Action de confirmer un event
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBooking();
            }
        });

        // Action d'annuler un event
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBooking();
            }
        });



    }
    // Methode de gestion des events favoris
    private void checkbookmark() {
        bookmark.setEnabled(false);
        StringRequest bookmarkcheckRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/getBookmark.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                bookmark.setText("Un-Bookmark");


                            } else {
                                bookmark.setEnabled(true);
                                bookmark.setText("Bookmark");

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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        queue2.add(bookmarkcheckRequest);
    }

    // Action d'enlever un event de ses favoris
    private void toggleBookmark() {
        bookmark.setEnabled(false);
        StringRequest togglebookmarkRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/toggleBookmark.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                bookmark.setEnabled(true);
                                checkbookmark();
                                onBackPressed();

                            } else {
                                bookmark.setEnabled(true);
                                checkbookmark();
                                participanttv.setVisibility(View.INVISIBLE);
                                imgPerson.setVisibility(View.INVISIBLE);
                                onBackPressed();
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(togglebookmarkRequest);
    }

    // Méthode qui commande la récupération des bookings pour vérifier qu'une réservation a pas déjà été
    // faite.
    private void checkbooking() {
        booking.setEnabled(false);
        StringRequest bookingcheckRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/getBookings.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                booking.setEnabled(false);
                                participanttv.setVisibility(View.VISIBLE);
                                imgPerson.setVisibility(View.VISIBLE);



                            } else {
                                booking.setEnabled(true);
                                booking.setText("Book");
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                params.put("nameParticipant", nameParticpant2);
                return params;
            }

        };
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        queue2.add(bookingcheckRequest);
    }



    // Action de réserver une place à un event
    private void toggleBooking() {
        booking.setEnabled(false);
        StringRequest togglebookingRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/toggleBooking.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                booking.setEnabled(true);
                                checkbooking();

                            } else {
                                booking.setEnabled(true);
                                checkbooking();

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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", user_id);
                params.put("nameParticipant",nameParticpant2);
                return params;
            }

        };
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(togglebookingRequest);
    }
    // Confirmation de la réservation par le fournisseur
    private void confirmBooking() {
        booking.setEnabled(false);
        StringRequest togglebookingRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/confirmBooking.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(EventDisplayUser.this,"Booking confirmed !",Toast.LENGTH_SHORT).show();
                                onBackPressed();


                            } else {
                                //checkbooking();
                                Toast.makeText(EventDisplayUser.this,"Plus de places disponibles pour cet évenement !",Toast.LENGTH_SHORT).show();
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", creator_id+"");
                params.put("nameParticipant",nameParticpant);
                return params;
            }

        };
        RequestQueue queue4 = Volley.newRequestQueue(getApplicationContext());
        queue4.add(togglebookingRequest);
    }

    // Annulation d'une réservation confirmée auparavant
    private void deleteBooking() {
        booking.setEnabled(false);
        StringRequest togglebookingRequest= new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/toggleBooking2.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(EventDisplayUser.this,"Booking canceled !",Toast.LENGTH_SHORT).show();
                                onBackPressed();


                            } else {
                                //checkbooking();
                              Toast.makeText(EventDisplayUser.this,"Failure to cancel the booking !",Toast.LENGTH_SHORT).show();
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
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("event_id", event_id+"");
                params.put("user_id", creator_id+"");
                params.put("nameParticipant",nameParticpant);
                return params;
            }

        };
        RequestQueue queue4 = Volley.newRequestQueue(getApplicationContext());
        queue4.add(togglebookingRequest);
    }


}
