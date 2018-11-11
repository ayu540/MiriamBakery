package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
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

public class BakerryRecipieDetailView extends AppCompatActivity {

    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private final int BAKERY_STEPS_CLICKED = 13;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_recipie_detail_view);
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();
        mContext = BakerryRecipieDetailView.this;

        mRecipiDetailsViewRecyClerView = (RecyclerView) findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
            RECIPE_LIST_TYPE = getIntent().getExtras().getString("LIST_TYPE");
        }


        loadRecipieListItems();

    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("Ingredients")) {
            getSupportActionBar().setTitle("Recipie Ingredients");
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
            if (mBakeryIngridentsListBeans != null) {

                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryIngridentsListBeans
                        , new BakeryDetailsRecyclerViewAdapter.BakeryDetailsIngredientsOnClickListener() {
                    @Override
                    public void onBakeryDetailsIngredientsCliCkListenerr(int position,
                                                                         ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans) {

                    }
                }, RECIPE_LIST_TYPE
                );

                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }
        } else if (RECIPE_LIST_TYPE.equalsIgnoreCase("Steps")) {
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
            getSupportActionBar().setTitle("Recipie Steps");
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(int position,
                                                                           ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {
                                Intent intent = new Intent(mContext, BakeryRecipeStepsVideoPlayer.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("STEPS_CLICKED_POSITION", position);
                                bundle.putParcelableArrayList("VIDEO_STEPS_LIST", bakeryStepsListBeans);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, BAKERY_STEPS_CLICKED);

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent bakeryStepsReturnIntent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
        bundle.putParcelableArrayList("INGREDINET_LIST",mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryIngridentsListBeans() );
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryStepsListBeans());
        bakeryStepsReturnIntent.putExtras(bundle);
        setResult(RESULT_OK, bakeryStepsReturnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BAKERY_STEPS_CLICKED) {
                if (data != null) {
                    mBakeryRecipiesListBeans = data.getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
                    //  mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
                    RECIPE_LIST_TYPE = data.getExtras().getString("LIST_TYPE");
                }
                loadRecipieListItems();
            }
        }
    }
}
