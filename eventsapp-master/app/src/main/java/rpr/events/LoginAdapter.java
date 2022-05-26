package rpr.events;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

// Adapter utilisé pour l'interface d'authentification
// En cliquant sur l'onglet "Login" le fragment associé à la connexion défile
// En cliquant sur l'onglet "Sign Up" le fragment associé à l'inscription défile
public class LoginAdapter extends FragmentStatePagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;

            case 1:
                SignupFragment signupFragment = new SignupFragment();
                return signupFragment;

            default:
                return null;
        }
    }
}
