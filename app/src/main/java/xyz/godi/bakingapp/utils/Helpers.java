package xyz.godi.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class Helpers {

    // helper method to make a Snackbar
    public static void createSnackBar(Context context, View view, String message) {
        final Snackbar snackbar;
        snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView text = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        text.setMaxLines(3);
        // show the snackBar
        snackbar.show();
    }

    // helper mACCESS_NETWORK_STATEethod to check if network is available
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() !=  null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}