package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity implements OnNavigationXControlListener {

    private NavController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        ActionBar a = getSupportActionBar();
        if (a != null) {
            a.setDisplayHomeAsUpEnabled(true);
            a.setDisplayShowTitleEnabled(true);
        }
        MainVM model = new ViewModelProvider(this).get(MainVM.class);
        model.init(this);


        /*code commented on 15 oct 2020 to remove login page */

       /* model.init(this, new OnCompletedListener() {
            @Override
            public void onSuccess() {

                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
                if (navHostFragment == null)
                    throw new RuntimeException("navHostFragment is null");
                controller = navHostFragment.getNavController();
                NavGraph graph = controller.getGraph();
                graph.setStartDestination(R.id.nav_hub_list);

                controller.setGraph(graph);

                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
                NavigationUI.setupWithNavController(toolbar, controller, appBarConfiguration);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }

            @Override
            public void onFailure() {
                throw new RuntimeException(("Unable to sign-in"));
            }
        });*/

        /*-----------removed login page on 15 oct 2020---------------*/

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (navHostFragment == null)
            throw new RuntimeException("navHostFragment is null");
        controller = navHostFragment.getNavController();
        NavGraph graph = controller.getGraph();
        graph.setStartDestination(R.id.nav_hub_list);
        controller.setGraph(graph);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, controller, appBarConfiguration);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        /*-------------------------------------------------------------*/


    }

    @Override
    public NavController getNavController() {
        return controller;
    }

    /*    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            profile = menu.findItem(R.id.profile);
            return super.onCreateOptionsMenu(menu);
        }
    */
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, controller)
                || super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
