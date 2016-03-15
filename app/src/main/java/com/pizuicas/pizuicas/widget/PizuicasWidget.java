package com.pizuicas.pizuicas.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.pizuicas.pizuicas.R;
import com.pizuicas.pizuicas.SplashActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PizuicasWidget extends AppWidgetProvider {

    @SuppressLint("NewApi")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = new RemoteViews(context.getPackageName(), R.layout.pizuicas_widget);

            // Adding collection list item handler
            Intent intent = new Intent(context, SplashActivity.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0,
                    intent, 0);
            mView.setOnClickPendingIntent(R.id.widgetLayoutMain, pending);

            mView.setRemoteAdapter(R.id.widget_collection_list,
                    new Intent(context, WidgetService.class));

            appWidgetManager.updateAppWidget(widgetId, mView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

