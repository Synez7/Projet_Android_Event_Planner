package rpr.events;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


// Classe matérialisant le composant de la BottomNavigationBar à l'écran
public class BottomNavigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new ListEventsTabs();
                        break;
                    case R.id.nav_favorites:
                        selectedFragment = new BookmarksList();
                        break;
                    case R.id.nav_search:
                        selectedFragment = new UserProfile();
                        break;

                    case R.id.nav_newEvent:
                        selectedFragment = new OrganiseEvent();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                        selectedFragment).commit();

                return false;
            }
        });
    }
}