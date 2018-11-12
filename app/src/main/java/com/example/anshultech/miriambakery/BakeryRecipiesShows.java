package com.example.anshultech.miriambakery;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.anshultech.miriambakery.Activities.BakeryIngredientsStepOptionsChoose;

/**
 * Implementation of App Widget functionality.
 */
public class BakeryRecipiesShows extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object


        RemoteViews views = getBakeryListRemoteViews(context);


//        Intent intent=new Intent(context, BakeryHome.class);
//        PendingIntent pendingIntent= PendingIntent.getActivity();

        for (int appwidgetId : appWidgetId) {
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appwidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAppWidget(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getBakeryListRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakery_recipies_shows_widget);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, WidgetReViService.class);
        views.setRemoteAdapter(R.id.widget_recycler_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, BakeryIngredientsStepOptionsChoose.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_recycler_view, appPendingIntent);

        return views;
    }

}

