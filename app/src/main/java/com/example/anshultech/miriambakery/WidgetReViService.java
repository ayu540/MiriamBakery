package com.example.anshultech.miriambakery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.anshultech.miriambakery.Adapters.BakeryRecipiesListRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Connection.ConnectionURL;
import com.example.anshultech.miriambakery.Connection.VolleyConnectionClass;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetReViService extends RemoteViewsService {
    public WidgetReViService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetReViRemoteViewsFactory(this.getApplicationContext());
    }

}

class WidgetReViRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private BakeryRecipiesListRecyclerViewAdapter mBakeryRecipiesListRecyclerViewAdapter;
    ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private boolean mTwoPane = false;

    public WidgetReViRemoteViewsFactory(Context context) {
        mContext = context;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
    }

    @Override
    public void onCreate() {

        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(
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

        VolleyConnectionClass.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onDataSetChanged() {
        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(
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

        VolleyConnectionClass.getInstance(mContext).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mBakeryRecipiesListBeans == null) {
            return 0;
        }
        return mBakeryRecipiesListBeans.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.masterrecipelistlayout);
        String recipiName = mBakeryRecipiesListBeans.get(position).getName();
        int servings = mBakeryRecipiesListBeans.get(position).getServings();

        views.setTextViewText(R.id.masterListRecipeNameTV, recipiName);
        views.setTextViewText(R.id.servingValueTextView, Integer.toString(servings));


        if (mBakeryRecipiesListBeans.get(position).getImage() != null) {
            String imageSource = mBakeryRecipiesListBeans.get(position).getImage().toString();

            if (!imageSource.equalsIgnoreCase("")) {
                views.setImageViewResource(R.id.recipiMasterListImageView, Integer.parseInt(imageSource));
                //  Picasso.get().load(imageSource).into(holder.recipiMasterListImageView);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt("CLICKED_POSITION", position);
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryRecipiesListBeans.get(position).getBakeryIngridentsListBeans());
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryRecipiesListBeans.get(position).getBakeryStepsListBeans());
        bundle.putBoolean("IS_TWO_PANE", mTwoPane);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.masterRecipieListContainer, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
