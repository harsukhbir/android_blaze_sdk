package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

public class NavigationXFragment extends Fragment {
    protected FragmentActivity context;
    protected MainVM model;
    private OnNavigationXControlListener nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            context = getActivity();
            model = new ViewModelProvider(context).get(MainVM.class);
            nav = (OnNavigationXControlListener) context;
        } catch (Exception e) {
            Loggers.error(e.getMessage());
        }
    }

    protected void gotoF(int action_graph_id) {
        gotoF(action_graph_id, null);
    }

    protected void gotoF(int action_graph_id, Bundle b) {
        try {
            nav.getNavController().navigate(action_graph_id, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will be called from child fragments.Fragment exists in graph which is attached to activity
     */
    protected boolean goBack() {
        try {
            return goBack(nav.getNavController());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean goBack(NavController controller) {
        try {
            return controller.navigateUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
