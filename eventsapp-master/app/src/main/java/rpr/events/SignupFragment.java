package rpr.events;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Fragment associé à l'inscription d'un profil quelconque
public class SignupFragment extends Fragment {

    EditText email;
    EditText username;
    EditText password;
    Spinner typeUser;
    Button signup;
    private Context context = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment,container,false);

        email = root.findViewById(R.id.email);
        username = root.findViewById(R.id.username2);
        password = root.findViewById(R.id.password2);
        typeUser = root.findViewById(R.id.usertype_spinner);
        signup = root.findViewById(R.id.signup);


        context = getActivity().getApplicationContext();

        getusertype();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().trim().equals("") || username.getText().toString().trim().equals("")
                || password.getText().toString().equals("")) {
                    Snackbar.make(view, "Please fill all the required fields !", Snackbar.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(email.getText())){
                    Snackbar.make(view, "Please input an email address with a correct format !", Snackbar.LENGTH_SHORT).show();
                }else {


                    final int usertype_id = typeUser.getSelectedItemPosition()+1;




                    StringRequest signupRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/Test.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("TEST:"+response);
                                if(response.trim().equals("Nouvel utilisateur enregistré dans la base !")){
                                    Snackbar.make(view, "Registration successful !", Snackbar.LENGTH_SHORT).show();
                                    email.getText().clear();
                                    username.getText().clear();
                                    password.getText().clear();

                                }
                                else{
                                    Snackbar.make(view, response.trim().toString(), Snackbar.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Nullable
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email.getText().toString());
                                params.put("pseudo", username.getText().toString());
                                params.put("motdepasse", password.getText().toString());
                                params.put("usertype_id", usertype_id+"");


                                return params;
                            }

                        };

                        RequestQueue r = Volley.newRequestQueue(getActivity().getApplicationContext());
                        r.add(signupRequest);



                    }
                }


        });
        return root;
    }




    private void getusertype(){

        StringRequest usertypeRequest = new StringRequest(Request.Method.GET, "https://eventplannerapp.000webhostapp.com/usertypeRegister2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            System.out.println(jsonResponse.toString());
                            JSONArray result = jsonResponse.getJSONArray("usertype2");
                            ArrayList<String> usertypes = new ArrayList<String>();
                            for(int i=0;i<result.length();i++){

                                JSONObject obj = result.getJSONObject(i);

                                usertypes.add(obj.getString("name"));

                            }

                            typeUser.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, usertypes));

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


        //queue.add(usertypeRequest);

        RequestQueue r = Volley.newRequestQueue(getActivity().getApplicationContext());
        r.add(usertypeRequest);

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
