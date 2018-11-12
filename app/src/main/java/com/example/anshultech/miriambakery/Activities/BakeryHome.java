package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
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
    private FrameLayout tabletViewFrameLayout;
    private boolean mTwoPane = false;
    private OnBackPressedListener onBackPressedListener;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_home);
        mContext = BakeryHome.this;
        mRecipiListRecyclerView = (RecyclerView) findViewById(R.id.recipiesMasterListRecyclerView);
        mRecipiListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        tabletViewFrameLayout = (FrameLayout) findViewById(R.id.tabletViewFrameLayout);

        getSupportActionBar().setTitle("Miriam Recipie List");


        if (tabletViewFrameLayout != null) {
            mTwoPane = true;
        }


        networkCallToLoadData();


    }

    private void networkCallToLoadData() {

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
                                mBakeryRecipiesListRecyclerViewAdapter = new BakeryRecipiesListRecyclerViewAdapter(mContext,
                                        bakeryRecipiesListBeans, new BakeryRecipiesListRecyclerViewAdapter.BakeryRecipiesListOnClickListener() {
                                    @Override
                                    public void onRecipiesClickItem(int position, ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans) {


                                        //Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("CLICKED_POSITION", position);
                                        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", bakeryRecipiesListBeans);
                                        bundle.putParcelableArrayList("INGREDINET_LIST", bakeryRecipiesListBeans.get(position).getBakeryIngridentsListBeans());
                                        bundle.putParcelableArrayList("STEPS_LIST", bakeryRecipiesListBeans.get(position).getBakeryStepsListBeans());
                                        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                                        //  intent.putExtras(bundle);
                                        if (mTwoPane == true) {

                                            BakeryIngredientsStepOptionsChoose bakeryIngredientsStepOptionsChoose = new BakeryIngredientsStepOptionsChoose();
                                            bakeryIngredientsStepOptionsChoose.setArguments(bundle);
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                    .beginTransaction();
                                            if (!bakeryIngredientsStepOptionsChoose.isAdded()) {
                                                fragmentTransaction
                                                        .replace(R.id.tabletViewFrameLayout, bakeryIngredientsStepOptionsChoose)
                                                        .addToBackStack("CHOOSE_OPTION_LAYOUT").commit();
                                            } else {
                                                fragmentTransaction.show(bakeryIngredientsStepOptionsChoose);
                                            }


                                        } else {
                                            BakeryIngredientsStepOptionsChoose bakeryIngredientsStepOptionsChoose = new BakeryIngredientsStepOptionsChoose();
                                            bakeryIngredientsStepOptionsChoose.setArguments(bundle);

                                        }
                                    }
//                                    }
                                });
                                mRecipiListRecyclerView.setAdapter(mBakeryRecipiesListRecyclerViewAdapter);
                            }


                        } else {
                            Toast.makeText(mContext, "Server Error, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleyConnectionClass.getInstance(mContext).

                addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();*/

        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            super.onBackPressed();
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public interface OnBackPressedListener {
        public void doBack();
    }
}
