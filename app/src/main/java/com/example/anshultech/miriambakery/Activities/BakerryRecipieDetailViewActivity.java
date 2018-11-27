package com.example.anshultech.miriambakery.Activities;


import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;


public class BakerryRecipieDetailViewActivity extends AppCompatActivity {

    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private final int BAKERY_STEPS_CLICKED = 13;
 //   private boolean mTwoPane = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_recipie_detail_view);
        mContext = BakerryRecipieDetailViewActivity.this;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();

        mRecipiDetailsViewRecyClerView = (RecyclerView) findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.ingredient_list));
            RECIPE_LIST_TYPE = getIntent().getExtras().getString(getResources().getString(R.string.list_type));
       //     mTwoPane = getIntent().getExtras().getBoolean(getResources().getString(R.string.is_two_pane));
        }

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.instance_clicked_position));
            RECIPE_LIST_TYPE = savedInstanceState.getString(getResources().getString(R.string.instance_list_type));
        //    mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.instance_is_two_pane));
        }
        loadRecipieListItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.instance_clicked_position), mRecipeMasterListClickedPosition);
        outState.putString(getResources().getString(R.string.instance_list_type), RECIPE_LIST_TYPE);
  //      outState.putBoolean(getResources().getString(R.string.instance_is_two_pane), mTwoPane);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("Ingredients")) {
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieIngredients));
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.ingredient_list));


            if (mBakeryIngridentsListBeans != null) {

                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryIngridentsListBeans
                        , new BakeryDetailsRecyclerViewAdapter.BakeryDetailsIngredientsOnClickListener() {
                    @Override
                    public void onBakeryDetailsIngredientsCliCkListenerr(int position,
                                                                         ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans) {
                        //do nothing
                    }
                }, RECIPE_LIST_TYPE
                );

                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }
        } else if (RECIPE_LIST_TYPE.equalsIgnoreCase("Steps")) {
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
            getSupportActionBar().setTitle(getResources().getString(R.string.RecipieSteps));
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(int position,
                                                                           ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {

                                Bundle bundle = new Bundle();
                                bundle.putInt(getResources().getString(R.string.steps_clicked_position), position);
                                bundle.putParcelableArrayList(getResources().getString(R.string.video_steps_list), bakeryStepsListBeans);
                                /*bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
                                BakeryRecipeStepsVideoPlayerFragment bakeryRecipeStepsVideoPlayerFragment = new BakeryRecipeStepsVideoPlayerFragment();
                                bakeryRecipeStepsVideoPlayerFragment.setArguments(bundle);
                                if (mTwoPane == false) {*/
                                    Intent intent = new Intent(mContext, BakeryRecipeStepsVideoPlayerActivity.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, BAKERY_STEPS_CLICKED);
                                /*}*/

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }
    }
}
