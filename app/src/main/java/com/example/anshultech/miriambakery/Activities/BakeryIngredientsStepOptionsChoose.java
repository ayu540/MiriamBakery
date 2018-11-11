package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BakeryIngredientsStepOptionsChoose extends AppCompatActivity {

    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE= 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indridentasstepsclicklayout);
        mContext = BakeryIngredientsStepOptionsChoose.this;

        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();
        mIngredientsButton = (Button) findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) findViewById(R.id.recipeOptyionChooseStepsButton);


        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
        }
        prepareRecipieButtonData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RECIPIE_CHOOSER_LIST_CODE) {
                if (data != null) {
                    mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
                    mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
                    mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
                    mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
                }
                prepareRecipieButtonData();
            }
        }
    }

    private void prepareRecipieButtonData() {


        if (mBakeryStepsListBeans != null && mBakeryIngridentsListBeans != null) {
            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BakerryRecipieDetailView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
                    bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
                    bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryIngridentsListBeans);
                    bundle.putString("LIST_TYPE", "Ingredients");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RECIPIE_CHOOSER_LIST_CODE);
                }
            });

            mStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BakerryRecipieDetailView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
                    bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
                    bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
                    bundle.putString("LIST_TYPE", "Steps");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RECIPIE_CHOOSER_LIST_CODE);
                }
            });
        }
    }
}
