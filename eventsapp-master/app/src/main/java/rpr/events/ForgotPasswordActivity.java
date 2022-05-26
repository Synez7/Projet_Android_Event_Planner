package rpr.events;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

// Classe qui représente l'activité de changement de mot de passe pour un profil quelconque
public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Button recoverBtn = findViewById(R.id.recover_btn);
        EditText usernameView = findViewById(R.id.username);

        TextView mdpRecover = findViewById(R.id.mdprecover);


        // Lancement de la récupération de mot de passe via le pseudo du profil utilisateur
        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernameView.getText().toString().trim().equals("")) {
                    Snackbar.make(view, "Please input your username !", Snackbar.LENGTH_SHORT).show();
                }

                else {

                        StringRequest signupRequest = new StringRequest(Request.Method.POST, "https://eventplannerapp.000webhostapp.com/ForgotPassword2.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mdpRecover.setText(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Nullable
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("pseudo", usernameView.getText().toString());
                            return params;
                        }

                    };

                    RequestQueue r = Volley.newRequestQueue(ForgotPasswordActivity.this);
                    r.add(signupRequest);



                }
            }


        });
    }
}