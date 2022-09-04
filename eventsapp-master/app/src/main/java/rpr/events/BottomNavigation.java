package rpr.events;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;



// Classe matérialisant le composant de la BottomNavigationBar à l'écran
public class BottomNavigation extends AppCompatActivity {

    public static Fragment fBookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        //selectedFragment = new ListEventsTabs();
                        System.out.println("WESH WESH 1 !");
                        replaceFragment(new ListEventsTabs());
                        break;
                    case R.id.nav_favorites:
                        System.out.println("WESH WESH 2 !");
                        //selectedFragment = new BookmarksList();
                        replaceFragment(new BookmarksList());
                        break;
                    case R.id.nav_search:
                        //selectedFragment = new UserProfile();
                        System.out.println("WESH WESH 3 !");
                        replaceFragment(new UserProfile());
                        break;

                    case R.id.nav_newEvent:
                        //selectedFragment = new OrganiseEvent();
                        System.out.println("WESH WESH 4 !");
                        replaceFragment(new OrganiseEvent());
                        break;
                }

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                  //      selectedFragment).commit();

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2,fragment);
        fragmentTransaction.commit();
    }
}