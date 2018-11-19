package com.example.anshultech.miriambakery.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.anshultech.miriambakery.Adapters.BakeryRecipiesListRecyclerViewAdapter;
import com.example.anshultech.miriambakery.BakeryRecipiesHomwWidget;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Connection.ConnectionURL;
import com.example.anshultech.miriambakery.Connection.VolleyConnectionClass;
import com.example.anshultech.miriambakery.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BakeryRecipieShowService extends IntentService {
//    // TODO: Rename actions, choose action names that describe tasks that this
//    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//    private static final String ACTION_FOO = "com.example.anshultech.miriambakery.Services.action.FOO";
//    private static final String ACTION_BAZ = "com.example.anshultech.miriambakery.Services.action.BAZ";
//
//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.example.anshultech.miriambakery.Services.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.example.anshultech.miriambakery.Services.extra.PARAM2";

    private BakeryRecipiesListRecyclerViewAdapter mBakeryRecipiesListRecyclerViewAdapter;
    ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private boolean mTwoPane = false;

    public BakeryRecipieShowService() {
        super("BakeryRecipieShowService");
    }

//    /**
//     * Starts this service to perform action Foo with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, BakeryRecipieShowService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
//
//    /**
//     * Starts this service to perform action Baz with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, BakeryRecipieShowService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    public static void startActionLoadDataOfWidget(Context context) {
        Intent intent = new Intent(context, BakeryRecipieShowService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            loadRecyclerViewData();
        }
    }

    private void loadRecyclerViewData() {
        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(this).volleyJSONArrayRequest(
                ConnectionURL.BAKING_RECIPIES_URL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Gson gson = new Gson();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject = response.getJSONObject(i);
                                BakeryRecipiesListBean bakeryRecipiesListBean = gson.fromJson(jsonObject.toString(), BakeryRecipiesListBean.class);
                                int a = jsonObject.getInt("id");
                                mBakeryRecipiesListBeans.add(bakeryRecipiesListBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        );

        VolleyConnectionClass.getInstance(this).addToRequestQueue(jsonArrayRequest);
//
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakeryRecipiesHomwWidget.class));
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recycler_view);
//        BakeryRecipiesHomwWidget.updateAppWidget(this,appWidgetManager,appWidgetIds);

    }

}