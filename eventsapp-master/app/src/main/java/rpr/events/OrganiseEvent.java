package rpr.events;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


// Fragment pour la création d'événements
public class OrganiseEvent extends Fragment {

    @Nullable
    private static Context context = null;

    private static TextView txtDate;
    private static TextView txtDate2;

    private static TextView txtTime;
    private static TextView txtTime2;

    private static Spinner categorySpinner;

    public static String t;

    private static String debutDate;
    private static String endDate;

    private static boolean isClickedImage;

    private static boolean overlappingDate;
    private static boolean overlappingHour;
    private static boolean isAfterOrEqualCurrentDate;

    public static Bitmap imageGal;



    Button btntimepicker, btndatepicker;
    Button btntimepicker2, btndatepicker2;

    ImageView img;

    StorageReference storageReference;



    UserSessionManager session;
    RequestQueue queue;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        session = new UserSessionManager(context);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Organize new event");
        return inflater.inflate(R.layout.fragment_organise_event, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categorySpinner = (Spinner) getView().findViewById(R.id.category_spinner);


        txtDate = (TextView) getView().findViewById(R.id.in_date);
        txtDate2 = (TextView) getView().findViewById(R.id.in_date2);

        txtTime = (TextView) getView().findViewById(R.id.in_time);
        txtTime2 = (TextView) getView().findViewById(R.id.in_time2);

        queue = Volley.newRequestQueue(context);


        btndatepicker = (Button) getView().findViewById(R.id.btn_date);
        btntimepicker = (Button) getView().findViewById(R.id.btn_time);

        img = (ImageView) getView().findViewById(R.id.imageView);

        btndatepicker2 = (Button) getView().findViewById(R.id.btn_date2);
        btntimepicker2 = (Button) getView().findViewById(R.id.btn_time2);


        getcategory();

        final Calendar c = Calendar.getInstance();

        txtDate.setText(DateFormat.getDateInstance().format(new Date()));
        txtDate2.setText(DateFormat.getDateInstance().format(new Date()));


        txtTime.setText(new SimpleDateFormat("hh:mm aa").format(new Date()));
        txtTime2.setText(new SimpleDateFormat("hh:mm aa").format(new Date()));



        final String[] selectedDate = new String[1];
        final String[] selectedDate2 = new String[1];
        final String[] currentDate = new String[1];
        SimpleDateFormat formatterdefault = new SimpleDateFormat("dd/MM/yyyy");
        String dateInStringdeafult = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        Date datedefault = null;
        try {
            datedefault = formatterdefault.parse(dateInStringdeafult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatterdefault = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate[0] = formatterdefault.format(datedefault).toString();
        selectedDate2[0] = formatterdefault.format(datedefault).toString();
        currentDate[0] = formatterdefault.format(datedefault).toString();
        final String[] selectedTime = new String[1];
        final String[] selectedTime2 = new String[1];

        String dtStartdefault = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(c.get(Calendar.MINUTE));
        SimpleDateFormat formatdefault = new SimpleDateFormat("HH:mm");

        java.sql.Time timeValuedefault = null;
        try {
            timeValuedefault = new java.sql.Time(formatdefault.parse(dtStartdefault).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectedTime[0] = String.valueOf(timeValuedefault);
        selectedTime2[0] = String.valueOf(timeValuedefault);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClickedImage = true;
                showPictureDialog();

            }
        });


        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    debutDate = selectedDate[0];
                                    System.out.println("DATE DEBUT : " + selectedDate[0]);

                                    boolean isAfterOrEqualCurrentDate = formatter.parse(selectedDate[0]).equals(formatter.parse(currentDate[0])) || formatter.parse(selectedDate[0]).after(formatter.parse(currentDate[0]));

                                    if(!isAfterOrEqualCurrentDate){
                                        Snackbar.make(v,"Date debut : after or equal the current date !",Snackbar.LENGTH_LONG).show();
                                    }


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

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
                                    endDate = selectedDate2[0];


                                    boolean isBefore =  formatter.parse(selectedDate[0]).before(formatter.parse(selectedDate2[0]));
                                    boolean isEqual = formatter.parse(selectedDate[0]).equals(formatter.parse(selectedDate2[0]));
                                    boolean isAfter  =  formatter.parse(selectedDate2[0]).after(formatter.parse(selectedDate[0]));

                                    overlappingDate = isBefore && isAfter || isEqual;

                                    if(!overlappingDate){
                                        Snackbar.make(v,"DATE DEBUT BEFORE END DATE !",Snackbar.LENGTH_LONG).show();
                                    }


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

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

                                    boolean isBefore =  format.parse(selectedTime[0]).before(format.parse(selectedTime2[0]));
                                    boolean isAfter  =  format.parse(selectedTime2[0]).after(format.parse(selectedTime[0]));

                                    overlappingHour = isBefore && isAfter;


                                    if(debutDate.equals(endDate)){
                                        overlappingHour = isBefore && isAfter;
                                        if(!overlappingHour){
                                            Snackbar.make(v,"Start Time before End Time ! (Same dates)",Snackbar.LENGTH_LONG).show();

                                        }
                                    }



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

        final EditText etEventName = (EditText) getView().findViewById(R.id.event_name);
        final EditText etVenue = (EditText) getView().findViewById(R.id.venue);
        final EditText etDetails = (EditText) getView().findViewById(R.id.details);
        final EditText etPrice = (EditText) getView().findViewById(R.id.price);
        final EditText etAttendance = (EditText) getView().findViewById(R.id.attendance);


        final Button bAddNew = (Button) getView().findViewById(R.id.bAdd);


        bAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                final String event_name = etEventName.getText().toString().trim();
                final String venue = etVenue.getText().toString().trim();
                final int category = categorySpinner.getSelectedItemPosition() + 1;
                final String details = etDetails.getText().toString().trim();
                final String time = selectedDate[0] + " " + selectedTime[0];
                final String time_end = selectedDate2[0] + " " + selectedTime2[0];
                final String image = t;
                final String price = etPrice.getText().toString().trim();
                final String attendance = etAttendance.getText().toString().trim();


                HashMap<String, String> user = session.getUserDetails();

                final String user_id = user.get(UserSessionManager.KEY_USER_ID);
                if (event_name.equals("")){
                    Snackbar.make(v, "Please specify a name for your event", Snackbar.LENGTH_SHORT).show();
                }
                else if (venue.equals("")){
                    Snackbar.make(v, "Please specify a location for your event", Snackbar.LENGTH_SHORT).show();
                }

                else if (price.equals("")){
                    Snackbar.make(v, "Please specify a price for your event", Snackbar.LENGTH_SHORT).show();
                }

                else if (attendance.equals("")){
                    Snackbar.make(v, "Please specify an attendance number for your event", Snackbar.LENGTH_SHORT).show();
                }

                else if (details.equals("")){
                    Snackbar.make(v, "Please specify a description for your event", Snackbar.LENGTH_SHORT).show();
                }

                else if (!isClickedImage){
                    Snackbar.make(v, "Please select an image for your event", Snackbar.LENGTH_SHORT).show();
                }


                else{

                    StringRequest registerRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/addEvent2.php",
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                    try{
                                        System.out.println("REPONSE : " + response);
                                        //String data = jsonResponse.getString("id");
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");

                                        if (success) {

                                            Toast.makeText(context, "Event " + event_name + " was added Successfully ", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), Navigation.class));

                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                                System.out.println("CHECK IT OUT");
                                                NotificationChannel channel = new NotificationChannel("New Event","New Event", NotificationManager.IMPORTANCE_DEFAULT);
                                                NotificationManager manager = getContext().getSystemService(NotificationManager.class);
                                                manager.createNotificationChannel(channel);

                                            }


                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"New Event");
                                            builder.setContentTitle("New event : " + event_name);
                                            builder.setContentText(venue + " - " + time);
                                            builder.setSmallIcon(R.drawable.inverse_logo);
                                            builder.setColor(ContextCompat.getColor(getContext(),R.color.blueNav));
                                            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                                            builder.setAutoCancel(true);

                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
                                            managerCompat.notify(1, builder.build());



                                        } else {

                                            Toast.makeText(context, "Event adding Failed", Toast.LENGTH_SHORT).show();

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
                            params.put("name", event_name);
                            params.put("user_id",user_id);
                            params.put("category_id", category+"");
                            params.put("venue",venue);
                            params.put("time",time);
                            params.put("time_end",time_end);
                            params.put("details",details);
                            params.put("image",image);
                            params.put("price",price);
                            params.put("attendance",attendance);


                            return params;
                        }

                    };
                    queue.add(registerRequest);
                }
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


    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                Uri contentURI = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    imageGal = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String imgPath = contentURI.toString();
                t = imgPath;
                String t = imgPath;
                img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 160, 160, false));

                storageReference = FirebaseStorage.getInstance().getReference("images/"+contentURI.toString().split("document/")[1]+".jpg");
                storageReference.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getContext(),"Firebase : Uploading successful !",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Firebase : Uploading failure !",Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } else if (requestCode == 2) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageBitmap, "title", null);
            storageReference = FirebaseStorage.getInstance().getReference("camera/"+path.split("images/media/")[1]+".jpg");
            storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(),"Firebase : Uploading successful !",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Firebase : Uploading failure !",Toast.LENGTH_SHORT).show();
                }
            });
            System.out.println(" IMG PATH CAMERA : " + path);

            img.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 160, 160, false));

        }
    }


    public void choosePhotoFromGallery() {
        //Intent galleryIntent = new Intent(Intent.ACTION_PICK,
          //      MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, 1);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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