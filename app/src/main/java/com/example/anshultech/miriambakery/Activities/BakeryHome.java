package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import com.example.anshultech.miriambakery.Utilities.SimpleIdlingResource;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BakeryHome extends AppCompatActivity implements VolleyConnectionClass.NetworkConnectionInferface {

    private Context mContext;
    private RecyclerView mRecipiListRecyclerView;
    private BakeryRecipiesListRecyclerViewAdapter mBakeryRecipiesListRecyclerViewAdapter;
    private final int RECIPIE_MASTER_LIST_LISTENER_CODE = 11;
 //   private FrameLayout tabletViewFrameLayout;
  //  private boolean mTwoPane = false;
   /* private OnBackPressedListener onBackPressedListener;
    private OnBackOptionChoosePressedListener onBackOptionChoosePressedListener;*/
    private boolean doubleBackToExitPressedOnce = false;

    ArrayList<BakeryRecipiesListBean> mBakeryRecipiesArrayListBeans;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    public BakeryHome() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_home);
        mContext = BakeryHome.this;
        mRecipiListRecyclerView = (RecyclerView) findViewById(R.id.recipiesMasterListRecyclerView);
        mRecipiListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBakeryRecipiesArrayListBeans = new ArrayList<BakeryRecipiesListBean>();

       // tabletViewFrameLayout = (FrameLayout) findViewById(R.id.tabletViewFrameLayout);

        getSupportActionBar().setTitle(getResources().getString(R.string.MiriamRecipieList));


/*        if (tabletViewFrameLayout != null) {
            mTwoPane = true;
        }*/

        networkCallToLoadData();
        getIdlingResource();


    }

    private void networkCallToLoadData() {

        JsonArrayRequest jsonArrayRequest = VolleyConnectionClass.getInstance(mContext).volleyJSONArrayRequest(ConnectionURL.BAKING_RECIPIES_URL
                , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            Gson gson = new Gson();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    BakeryRecipiesListBean bakeryRecipiesListBean = gson.fromJson(jsonObject.toString(), BakeryRecipiesListBean.class);
                                    int a = jsonObject.getInt("id");
                                    mBakeryRecipiesArrayListBeans.add(bakeryRecipiesListBean);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (mBakeryRecipiesArrayListBeans != null || mBakeryRecipiesArrayListBeans.size() > 0) {
                                mBakeryRecipiesListRecyclerViewAdapter = new BakeryRecipiesListRecyclerViewAdapter(mContext,
                                        mBakeryRecipiesArrayListBeans, new BakeryRecipiesListRecyclerViewAdapter.BakeryRecipiesListOnClickListener() {
                                    @Override
                                    public void onRecipiesClickItem(int position, ArrayList<BakeryRecipiesListBean> lBakeryRecipiesListBeans) {


                                        //Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(getResources().getString(R.string.clicked_position), position);
                                        bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), lBakeryRecipiesListBeans);
                                        bundle.putParcelableArrayList(getResources().getString(R.string.ingredient_list), lBakeryRecipiesListBeans.get(position).getBakeryIngridentsListBeans());
                                        bundle.putParcelableArrayList(getResources().getString(R.string.steps_list), lBakeryRecipiesListBeans.get(position).getBakeryStepsListBeans());
                                      /*  bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
                                        BakeryIngredientsStepOptionsChooseFragment bakeryIngredientsStepOptionsChooseFragment = new BakeryIngredientsStepOptionsChooseFragment();
                                        bakeryIngredientsStepOptionsChooseFragment.setArguments(bundle);
                                        //  intent.putExtras(bundle);
                                        if (mTwoPane == true) {


                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                    .beginTransaction();
                                            if (!bakeryIngredientsStepOptionsChooseFragment.isAdded()) {
                                                fragmentTransaction
                                                        .replace(R.id.tabletViewFrameLayout,
                                                                bakeryIngredientsStepOptionsChooseFragment, getResources().getString(R.string.bakeryIngredientsStepOptionsChooseFragment))
                                                        .addToBackStack(null).commit();
                                            } else {
                                                fragmentTransaction.show(bakeryIngredientsStepOptionsChooseFragment);
                                            }


                                        } else {*/
                                            Intent intent = new Intent(mContext, BakeryIngredientsStepOptionsChooseActivity.class);
                                            intent.putExtras(bundle);
                                            startActivityForResult(intent, RECIPIE_MASTER_LIST_LISTENER_CODE);

                                        /*}*/
                                    }
//                                    }
                                });
                                mRecipiListRecyclerView.setAdapter(mBakeryRecipiesListRecyclerViewAdapter);
                                mIdlingResource.setIdleState(true);
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

        VolleyConnectionClass.getInstance(mContext).addToRequestQueue(jsonArrayRequest, this);
    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
 /*       if (onBackPressedListener != null) {

            if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.bakerryRecipieDetailViewFragment)) instanceof BakerryRecipieDetailViewFragment) {
                onBackPressedListener.forDetailsPageBackPressed(count);
            }
        } else if (onBackOptionChoosePressedListener != null) {
            if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.bakeryIngredientsStepOptionsChooseFragment)) instanceof BakeryIngredientsStepOptionsChooseFragment) {
                onBackOptionChoosePressedListener.forOptionChooseBackPressed(count);
            }
        } else {*/
            super.onBackPressed();
        /*}*/
    }

/*    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public void setOnOptionChooseBackPressedListener(OnBackOptionChoosePressedListener onOptionChooseBackPressedListener) {
        this.onBackOptionChoosePressedListener = onOptionChooseBackPressedListener;
    }*/

    @Override
    public void isNetworkAvailable() {
        Toast.makeText(mContext, "Network not Available, please ccheck your internet connection", Toast.LENGTH_SHORT).show();

    }

/*    public interface OnBackPressedListener {
        public void forDetailsPageBackPressed(int currentFragmentCount);
    }

    public interface OnBackOptionChoosePressedListener {

        public void forOptionChooseBackPressed(int currentFragmentCount);
    }*/

}
