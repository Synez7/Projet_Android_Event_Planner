package rpr.events;


import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

// Classe de gestion des informations de connexion
public class UserSessionManager {

        SharedPreferences pref;

        Editor editor;

        // Context
        Context _context;

        // Shared pref mode
        int PRIVATE_MODE = 0;

        private static final String PREFER_NAME = "eventsApp";

        private static final String IS_USER_LOGIN = "IsUserLoggedIn";

        public static final String KEY_NAME = "name";

        public static final String KEY_EMAIL = "email";

        public static final String KEY_USERNAME = "username";

        public static final String KEY_USER_ID = "user_id";

        public static final String KEY_USERTYPE = "usertype"; // {Provider | User }
        public static final String KEY_USERTYPE_ID = "usertype_id";
        public static final String KEY_USERTYPES = "usertypes";
        public static final String KEY_CREATED = "created";

        // Constructeur
        public UserSessionManager(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }



    public void createUserLoginSession(String username, String email, int user_id, int usertype_id, String usertype, int usertypes){
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_USERNAME, username);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_ID, user_id+"");
        editor.putString(KEY_USERTYPE_ID, usertype_id+"");
        editor.putString(KEY_USERTYPE, usertype);
        editor.putString(KEY_USERTYPES, usertypes+"");

        editor.commit();
    }


        public boolean checkLogin(){
            if(!this.isUserLoggedIn()){

                Intent i = new Intent(_context, LoginActivity2.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                _context.startActivity(i);

                return true;
            }
            return false;
        }




        public HashMap<String, String> getUserDetails(){

            HashMap<String, String> user = new HashMap<String, String>();


            // Pseudo de l'utilisateur
            user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

            // Email de l'utilisateur
            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

            // ID de l'utilisateur
            user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

            // ID du type d'utilisateur
            user.put(KEY_USERTYPE_ID, pref.getString(KEY_USERTYPE_ID, null));

            // Nom du type d'utilisateur
            user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));

            // Nombre de types d'utilisateur (3) => [1 : Provider, 2 : User]
            user.put(KEY_USERTYPES, pref.getString(KEY_USERTYPES, null));


            return user;
        }


        public void logoutUser(){

            editor.clear();
            editor.commit();

            Intent i = new Intent(_context, LoginActivity2.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            _context.startActivity(i);
        }


        public boolean isUserLoggedIn(){

        return pref.getBoolean(IS_USER_LOGIN, false);
        }

}
