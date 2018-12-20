package xyz.godi.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Ingredients;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.ui.activities.MainActivity;
import xyz.godi.bakingapp.ui.activities.StepsListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static List<Ingredients> ingredients = new ArrayList<>();
    private static String text;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(StepsListActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(StepsListActivity.ID_PREF, 0);

        text = sharedPreferences.getString(StepsListActivity.NAME_PREF, "no recipe");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.appwidget_text, text);

        //Set adapter
        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);

        //open mainActivity when title is clicked
        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        //Now update all widgets
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(StepsListActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(StepsListActivity.NAME_PREF);
        editor.remove(StepsListActivity.ID_PREF);
        editor.apply();
    }
}

