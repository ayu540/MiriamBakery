package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.anshultech.miriambakery.Adapters.BakeryRecipiesListRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Connection.ConnectionURL;
import com.example.anshultech.miriambakery.Connection.VolleyConnectionClass;
import com.example.anshultech.miriambakery.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BakeryHome extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecipiListRecyclerView;
    private BakeryRecipiesListRecyclerViewAdapter mBakeryRecipiesListRecyclerViewAdapter;
    private final int RECIPIE_MASTER_LIST_LISTENER_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_home);
        mContext = BakeryHome.this;
        mRecipiListRecyclerView = (RecyclerView) findViewById(R.id.recipiesMasterListRecyclerView);
        mRecipiListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        getSupportActionBar().setTitle("Miriam Recipie List");


        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(ConnectionURL.BAKING_RECIPIES_URL
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            Gson gson = new Gson();
                            ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    BakeryRecipiesListBean bakeryRecipiesListBean = gson.fromJson(jsonObject.toString(), BakeryRecipiesListBean.class);
                                    int a = jsonObject.getInt("id");
                                    bakeryRecipiesListBeans.add(bakeryRecipiesListBean);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (bakeryRecipiesListBeans != null || bakeryRecipiesListBeans.size() > 0) {
                                mBakeryRecipiesListRecyclerViewAdapter = new BakeryRecipiesListRecyclerViewAdapter(mContext, bakeryRecipiesListBeans, new BakeryRecipiesListRecyclerViewAdapter.BakeryRecipiesListOnClickListener() {
                                    @Override
                                    public void onRecipiesClickItem(int position, ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans) {
                                        Intent intent = new Intent(mContext, BakeryIngredientsStepOptionsChoose.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("CLICKED_POSITION", position);
                                        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", bakeryRecipiesListBeans);
                                        bundle.putParcelableArrayList("INGREDINET_LIST", bakeryRecipiesListBeans.get(position).getBakeryIngridentsListBeans());
                                        bundle.putParcelableArrayList("STEPS_LIST", bakeryRecipiesListBeans.get(position).getBakeryStepsListBeans());
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, RECIPIE_MASTER_LIST_LISTENER_CODE);
                                    }
                                });
                                mRecipiListRecyclerView.setAdapter(mBakeryRecipiesListRecyclerViewAdapter);
                            }


                        } else {
                            Toast.makeText(mContext, "Server Error, please try again", Toast.LENGTH_SHORT).show();
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
}
