package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class BakeryIngredientsStepOptionsChoose extends Fragment implements BakeryHome.OnBackPressedListener {

    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE = 12;
    private boolean mTwoPane=false;


    public BakeryIngredientsStepOptionsChoose() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

      //  ((BakeryShowViews)getActivity()).setOnBackPressedListener(BakeryIngredientsStepOptionsChoose.this);

        View attachedRootView = inflater.inflate(R.layout.indridentasstepsclicklayout, container, false);

        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();
        mIngredientsButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseStepsButton);

        mBakeryRecipiesListBeans = getArguments().getParcelableArrayList("BAKERY_MASTER_LIST");
        mRecipeMasterListClickedPosition = getArguments().getInt("CLICKED_POSITION");
        mBakeryIngridentsListBeans = getArguments().getParcelableArrayList("INGREDINET_LIST");
        mBakeryStepsListBeans = getArguments().getParcelableArrayList("STEPS_LIST");
        mTwoPane = getArguments().getBoolean("IS_TWO_PANE");



        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = savedInstanceState.getInt("CLICKED_POSITION");
            mBakeryIngridentsListBeans = savedInstanceState.getParcelableArrayList("INGREDINET_LIST");
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList("STEPS_LIST");
            mTwoPane = getArguments().getBoolean("IS_TWO_PANE");
        }
        prepareRecipieButtonData();

        return attachedRootView;
    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.indridentasstepsclicklayout);
//        mContext = BakeryIngredientsStepOptionsChoose.this;
//
//
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RECIPIE_CHOOSER_LIST_CODE) {
                if (data != null) {
                    mBakeryRecipiesListBeans = data.getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
                    mRecipeMasterListClickedPosition = data.getExtras().getInt("CLICKED_POSITION");
                    mBakeryIngridentsListBeans = data.getExtras().getParcelableArrayList("INGREDINET_LIST");
                    mBakeryStepsListBeans = data.getExtras().getParcelableArrayList("STEPS_LIST");
                }
                prepareRecipieButtonData();
            }
        }
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
        BakerryRecipieDetailView bakerryRecipieDetailView = new BakerryRecipieDetailView();
        bakerryRecipieDetailView.setArguments(bundle);
        if (mTwoPane == true) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            if (!bakerryRecipieDetailView.isAdded()) {
                fragmentTransaction
                        .replace(R.id.tabletViewFrameLayout, bakerryRecipieDetailView)
                        .addToBackStack(null).commit();
            } else {
                fragmentTransaction.show(bakerryRecipieDetailView);
            }


        } else {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction
                    .replace(R.id.frameLayoutPhoneOptionsDetails, bakerryRecipieDetailView)
                    .addToBackStack(null).commit();

        }
    }

    @Override
    public void doBack() {
        getFragmentManager().popBackStack("CHOOSE_OPTION_LAYOUT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
