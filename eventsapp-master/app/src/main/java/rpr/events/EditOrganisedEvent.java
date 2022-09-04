package rpr.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Classe matérialisant la mise à jour des événements par l'organisateur
public class EditOrganisedEvent extends AppCompatActivity {

    Context context;
    UserSessionManager session;
    // Champs d'informations pouvant être changés sur un événement
    private TextView txtName;
    private TextView txtVenue;
    private TextView txtDetails;

    private TextView txtDate;
    private TextView txtDate2;

    private TextView txtTime;
    private TextView txtTime2;

    private TextView txtPrice;
    private TextView txtAttendance;

    ImageView img;


    private Spinner categorySpinner;

    Button btntimepicker, btntimepicker2, btndatepicker, btndatepicker2;

    private int event_id;
    private String name;
    private String time;
    private String time_end;
    private String venue;
    private String details;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;
    private String image;
    private int price;
    private int attendance;

    public static String t;
    private static boolean isClickedImage;


    StorageReference storageReference;
    public static String pathNewImage;


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
        context = this;
        setContentView(R.layout.activity_edit_organised_event);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session = new UserSessionManager(this);

        // Démarrage d'une nouvelle session de connexion utilisateur
        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        queue = Volley.newRequestQueue(context);
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        // Récupération des données de l'event à modifier
        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id",0);
        name =  intent.getStringExtra("name");
        time =  intent.getStringExtra("time");
        time_end = intent.getStringExtra("time_end");

        venue =  intent.getStringExtra("venue");
        details =  intent.getStringExtra("details");

        price = intent.getIntExtra("price",0);
        attendance = intent.getIntExtra("attendance",0);
        creator_id =  intent.getIntExtra("creator_id",0);
        creator =  intent.getStringExtra("creator");
        category_id =  intent.getIntExtra("category_id",0);
        category =  intent.getStringExtra("category");
        image = intent.getStringExtra("image");

        String oldPic = image;

        txtName = (TextView) findViewById(R.id.event_name);
        txtDate = (TextView) findViewById(R.id.in_date);
        txtDate2 = (TextView) findViewById(R.id.in_date2);
        txtTime = (TextView) findViewById(R.id.in_time);
        txtTime2 = (TextView) findViewById(R.id.in_time2);

        txtPrice = (TextView) findViewById(R.id.price);
        txtAttendance = (TextView) findViewById(R.id.attendance);

        txtVenue = (TextView) findViewById(R.id.venue);
        txtDetails = (TextView) findViewById(R.id.details);

        img = (ImageView) findViewById(R.id.imageView);

        //img.setImageURI(Uri.parse(image));
        System.out.println("IMAGE PATH MON POTE : " + image);

        // CAS où l'image de l'événement est une photo sélectionnée depuis les documents du terminal
        if(!image.contains("external")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + image.split("document/")[1] + ".jpg");

            try {
                final File localFile = File.createTempFile(image.split("document/")[1].split("\\.")[0], "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                img.setImageBitmap(bitmap);
                                System.out.println("PATH DE MON BITMAP : " + localFile.getPath());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        img.setBackgroundResource(R.drawable.concert);
                        Toast.makeText(EditOrganisedEvent.this, "Firebase : Picture not retrieved !", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // CAS où l'image de l'événement est une photo prise avec l'appareil photo du terminal
        else{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("camera/"+image.split("images/media/")[1]+".jpg");

            try {
                final File localFile = File.createTempFile(image.split("images/media/")[1].split("\\.")[0], "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                img.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        img.setBackgroundResource(R.drawable.concert);
                        Toast.makeText(EditOrganisedEvent.this, "Firebase : Picture not retrieved !", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        txtName.setText(name);
        txtVenue.setText(venue);
        txtDetails.setText(details);
        txtAttendance.setText(attendance+"");
        txtPrice.setText(price+"");


        Date date = new Date();
        Date date2 = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtDate.setText(DateFormat.getDateInstance().format(date));
        txtTime.setText(new SimpleDateFormat("hh:mm aa").format(date));

        txtDate2.setText(DateFormat.getDateInstance().format(date2));
        txtTime2.setText(new SimpleDateFormat("hh:mm aa").format(date2));


        // DATE DE DEBUT
        final String[] selectedDate = new String[1];
        selectedDate[0] = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();

        final String[] selectedTime = new String[1];
        selectedTime[0] = new SimpleDateFormat("HH:mm:ss").format(date).toString();

        final Calendar c = Calendar.getInstance();
        c.setTime(date);

        // DATE DE FIN
        final String[] selectedDate2 = new String[1];
        selectedDate2[0] = new SimpleDateFormat("yyyy-MM-dd").format(date2).toString();

       final String[] selectedTime2 = new String[1];
       selectedTime2[0] = new SimpleDateFormat("HH:mm:ss").format(date2).toString();

        final Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);


        categorySpinner = (Spinner) findViewById(R.id.category_spinner);


        btndatepicker = (Button) findViewById(R.id.btn_date);
        btndatepicker2 = (Button) findViewById(R.id.btn_date2);


        btntimepicker = (Button) findViewById(R.id.btn_time);
        btntimepicker2 = (Button) findViewById(R.id.btn_time2);

        getcategory();

        // Mise à jour nom
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Name");
                final EditText input = new EditText(context);
                input.setText(txtName.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtName.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }});
        // Mise à jour localisation
        txtVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Venue");
                final EditText input = new EditText(context);
                input.setText(txtVenue.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtVenue.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }});
        // Mise à jour des infos de l'event
        txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Details");
                final EditText input = new EditText(context);
                input.setText(txtDetails.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtDetails.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }});

        // Mise à jour prix de l'event
        txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Price");
                final EditText input = new EditText(context);
                input.setText(txtPrice.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtPrice.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }});

        // Mise à jour du nombre de places disponibles pour un event
        txtAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Attendance");
                final EditText input = new EditText(context);
                input.setText(txtAttendance.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtAttendance.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }});

        // Mise à jour date début event
        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.MONTH, monthOfYear);
                                    cal.set(Calendar.DATE, dayOfMonth);
                                    Date selected = cal.getTime();
                                    txtDate.setText(DateFormat.getDateInstance().format(selected));



                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);



                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    selectedDate[0] = formatter.format(date).toString();


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

        // Mise à jour date fin event
        btndatepicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.MONTH, monthOfYear);
                                    cal.set(Calendar.DATE, dayOfMonth);
                                    Date selected = cal.getTime();
                                    txtDate2.setText(DateFormat.getDateInstance().format(selected));



                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);



                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    selectedDate2[0] = formatter.format(date).toString();


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

        // Mise à jour heure début event
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    cal.set(Calendar.SECOND, 0);
                                    Date selected = cal.getTime();

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    txtTime.setText(new SimpleDateFormat("hh:mm aa").format(selected));



                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    selectedTime[0] = String.valueOf(timeValue);

                                } catch (Exception ex) {
                                }
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(context)
                );
                td.show();
            }
        });

        // Mise à jour heure fin event
        btntimepicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    cal.set(Calendar.SECOND, 0);
                                    Date selected = cal.getTime();

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    txtTime2.setText(new SimpleDateFormat("hh:mm aa").format(selected));



                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    selectedTime2[0] = String.valueOf(timeValue);

                                } catch (Exception ex) {
                                }
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(context)
                );
                td.show();
            }
        });

        // Mise à jour image event
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClickedImage = true;
                showPictureDialog();
            }

        });


        final Button bUpdate = (Button) findViewById(R.id.bUpdate);

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = txtName.getText().toString().trim();
                venue = txtVenue.getText().toString().trim();
                category_id = categorySpinner.getSelectedItemPosition() + 1;
                details = txtDetails.getText().toString().trim();
                time = selectedDate[0] + " " + selectedTime[0];
                time_end = selectedDate2[0] + " " + selectedTime2[0];
                price = Integer.parseInt(txtPrice.getText().toString().trim());
                attendance = Integer.parseInt(txtAttendance.getText().toString().trim());

                if(isClickedImage != true){
                    image = oldPic;
                }
                else{
                    image = pathNewImage;
                }




                if (name.equals("")){
                    Toast.makeText(context, "Please specify Name", Toast.LENGTH_SHORT).show();
                }
                else if (venue.equals("")){
                    Toast.makeText(context, "Please specify Venue", Toast.LENGTH_SHORT).show();
                }
                else if (details.equals("")){
                    Toast.makeText(context, "Please specify Details", Toast.LENGTH_SHORT).show();
                }

                else if (Objects.equals(price, "")){
                    Snackbar.make(v, "Please specify a price for your event", Snackbar.LENGTH_SHORT).show();
                }

                else if (Objects.equals(attendance, "")){
                    Snackbar.make(v, "Please specify an attendance number for your event", Snackbar.LENGTH_SHORT).show();
                }



                else{

                    StringRequest registerRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/updateEvent.php",
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {

                                            Toast.makeText(context, "Event " + name + " was updated Successfully ", Toast.LENGTH_SHORT).show();
                                            onBackPressed();

                                        } else {
                                            Toast.makeText(context, "Event update Failed", Toast.LENGTH_SHORT).show();
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
                            params.put("name", name);
                            params.put("user_id",user_id);
                            params.put("event_id",event_id+"");
                            params.put("category_id", category_id+"");
                            params.put("venue",venue);
                            params.put("time",time);
                            params.put("time_end",time_end);
                            params.put("details",details);
                            params.put("image",image);
                            params.put("price",price+"");
                            params.put("attendance",attendance+"");



                            return params;
                        }

                    };
                    queue.add(registerRequest);
                }
            }
        });

        final Button bDelete = (Button) findViewById(R.id.bDelete);

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    StringRequest registerRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/deleteEvent.php",
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{

                                        System.out.println("REPONSE DELETE : " + response);
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {


                                            Toast.makeText(context, "Event " + name + " was deleted ", Toast.LENGTH_SHORT).show();

                                            onBackPressed();


                                        } else {

                                            Toast.makeText(context, "Event deletion Failed", Toast.LENGTH_SHORT).show();

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
                            params.put("user_id",user_id);
                            params.put("event_id",event_id+"");


                            return params;
                        }

                    };
                    queue.add(registerRequest);

            }
        });
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

    // Requête GET pour la récupération des catégories d'event
    private void getcategory(){


        StringRequest categoryRequest = new StringRequest(Request.Method.GET, "https://eventplannerapp.000webhostapp.com/categoryRegister.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            JSONArray result = jsonResponse.getJSONArray("category");
                            ArrayList<String> categorys = new ArrayList<String>();
                            for(int i=0;i<result.length();i++){

                                JSONObject obj = result.getJSONObject(i);
                                categorys.add(obj.getString("name"));

                            }
                            categorySpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categorys));
                            categorySpinner.setSelection(category_id-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });



        queue.add(categoryRequest);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EditOrganisedEvent.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                Uri contentURI = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String imgPath = contentURI.toString();
                pathNewImage = imgPath;
                System.out.println("PATH MY IMAGE : " + imgPath);
                img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 160, 160, false));
                storageReference = FirebaseStorage.getInstance().getReference("images/"+contentURI.toString().split("document/")[1]+".jpg");
                storageReference.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(EditOrganisedEvent.this,"Firebase : Uploading successful !",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditOrganisedEvent.this,"Firebase : Uploading failure !",Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } else if (requestCode == 2) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), imageBitmap, "title", null);
            storageReference = FirebaseStorage.getInstance().getReference("camera/"+path.split("images/media/")[1]+".jpg");
            storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(EditOrganisedEvent.this,"Firebase : Uploading successful !",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditOrganisedEvent.this,"Firebase : Uploading failure !",Toast.LENGTH_SHORT).show();
                }
            });
            t = path;
            String t = path;
            img.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 160, 160, false));
        }
    }


    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 1);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


}
