package com.redditjokes.twistedjokes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.redditjokes.twistedjokes.bookmarks.BookmarksActivity;
import com.redditjokes.twistedjokes.purchases.PurchaseActivity;
import com.redditjokes.twistedjokes.utils.About;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    NavController navController;
    private DrawerLayout myDrawerMain;
    private NavigationView navView;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //host all the fragments

        initWidgets();
        initNav();
        navigationSetup();


    }



    private void initWidgets() {
        myDrawerMain = findViewById(R.id.myDrawer);

        navView = findViewById(R.id.navigationView);
        bottomNav = findViewById(R.id.bottom_navigation);



    }
    @Override
    public void onBackPressed() {
        if (myDrawerMain.isDrawerOpen(GravityCompat.START)) {
            myDrawerMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNav() {
        navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this,navController);
        NavigationUI.setupWithNavController(bottomNav,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                //changes toolbar title when going through fragments
                if(destination.getId() == R.id.ourJokesFragment){

                }




            }
        });

        //navigationView.setNavigationItemSelectedListener(this);

    }


    private void navigationSetup() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.menu_home:

                        myDrawerMain.closeDrawers();

                        break;

                    case R.id.bookmarks:


                        startActivity(new Intent(MainActivity.this, BookmarksActivity.class));

                        break;


                        case R.id.removeads:

                            startActivity(new Intent(MainActivity.this,PurchaseActivity.class));

                            break;

                    case R.id.menu_about:

                        startActivity(new Intent(MainActivity.this, About.class));


                        break;
                    case R.id.menu_send:

                        openIntentForSharing();

                        break;

                    case R.id.rate_menu:


                        directToGooglePlay(getPackageName());


                        break;

                    case R.id.similar_apps:


                        directToGooglePlay("com.homie.psychq");

                        break;


                }

                return true;
            }
        });
    }

    public void updateStatusBarColor(int color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void openIntentForSharing(){
        //opens app chooser to share the app
        Intent myIntent=new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareSub="The biggest pool of twisted and dark humor. Try it now. It's Free!! ";
        String shareBody="http://play.google.com/store/apps/details?id=" + getPackageName();
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(myIntent,"Share App Link via "));

    }


    private void directToGooglePlay(String packageName) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}
