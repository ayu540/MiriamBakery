package com.example.anshultech.miriambakery.Activities;



import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.Fragments.BakerryRecipieDetailViewFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BakeryIngredientsStepOptionsChooseActivity extends AppCompatActivity {
    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE = 12;
    private boolean mTwoPane=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indridentasstepsclicklayout);
        mContext= BakeryIngredientsStepOptionsChooseActivity.this;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();
        mIngredientsButton = (Button) findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) findViewById(R.id.recipeOptyionChooseStepsButton);

        if(getIntent()!=null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
            mTwoPane = getIntent().getExtras().getBoolean("IS_TWO_PANE");
        }

        prepareRecipieButtonData();
    }

    private void prepareRecipieButtonData() {

        if (mBakeryStepsListBeans != null && mBakeryIngridentsListBeans != null) {
            final Bundle bundle = new Bundle();
            bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
            bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
            bundle.putBoolean("IS_TWO_PANE", mTwoPane);
            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryIngridentsListBeans);
                    bundle.putString("LIST_TYPE", "Ingredients");
                    loadNextActivity(bundle);

                }
            });

            mStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
                    bundle.putString("LIST_TYPE", "Steps");
                    loadNextActivity(bundle);
                }
            });
        }
    }


    private void loadNextActivity(Bundle bundle) {
      /*  BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
        bakerryRecipieDetailViewFragment.setArguments(bundle);*/
        if (mTwoPane == false) {
 /*           FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            if (!bakerryRecipieDetailViewFragment.isAdded()) {
                fragmentTransaction
                        .replace(R.id.tabletViewFrameLayout, bakerryRecipieDetailViewFragment)
                        .addToBackStack(null).commit();
            } else {
                fragmentTransaction.show(bakerryRecipieDetailViewFragment);
            }*/


            /*     } else {*/

//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction
//                    .replace(R.id.frameLayoutPhoneOptionsDetails, bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
//                    .addToBackStack(null).commit();

            Intent intent = new Intent(mContext, BakerryRecipieDetailViewActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, RECIPIE_CHOOSER_LIST_CODE);

//        }
        }
    }

}
