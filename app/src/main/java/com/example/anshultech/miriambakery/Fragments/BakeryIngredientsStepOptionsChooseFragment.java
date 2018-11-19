package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.Fragments.BakerryRecipieDetailViewFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class BakeryIngredientsStepOptionsChooseFragment extends Fragment {

    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE = 12;
    private boolean mTwoPane = false;
    private RecyclerView recipiesMasterListRecyclerView1;


    public BakeryIngredientsStepOptionsChooseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();

        mBakeryRecipiesListBeans = getArguments().getParcelableArrayList("BAKERY_MASTER_LIST");
        mRecipeMasterListClickedPosition = getArguments().getInt("CLICKED_POSITION");
        mBakeryIngridentsListBeans = getArguments().getParcelableArrayList("INGREDINET_LIST");
        mBakeryStepsListBeans = getArguments().getParcelableArrayList("STEPS_LIST");
        mTwoPane = getArguments().getBoolean("IS_TWO_PANE");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //  ((BakeryShowViews)getActivity()).setOnBackPressedListener(BakeryIngredientsStepOptionsChooseFragment.this);

        View attachedRootView = inflater.inflate(R.layout.indridentasstepsclicklayout, container, false);


        mIngredientsButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseStepsButton);


        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = savedInstanceState.getInt("CLICKED_POSITION");
            mBakeryIngridentsListBeans = savedInstanceState.getParcelableArrayList("INGREDINET_LIST");
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList("STEPS_LIST");
            mTwoPane = savedInstanceState.getBoolean("IS_TWO_PANE");
        }
        View view = getActivity().findViewById(R.id.recipiesMasterListRecyclerView);
        if (view instanceof RecyclerView) {
            recipiesMasterListRecyclerView1 = (RecyclerView) view;
            recipiesMasterListRecyclerView1.setVisibility(View.VISIBLE);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        prepareRecipieButtonData();

        return attachedRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        outState.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
        outState.putParcelableArrayList("INGREDINET_LIST", mBakeryIngridentsListBeans);
        outState.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
        outState.putBoolean("IS_TWO_PANE", mTwoPane);
    }


    //
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.indridentasstepsclicklayout);
//        mContext = BakeryIngredientsStepOptionsChooseFragment.this;
//
//
//    }


    /*@Override
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
    }*/

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
        BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
        bakerryRecipieDetailViewFragment.setArguments(bundle);
        if (mTwoPane == true) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            if (!bakerryRecipieDetailViewFragment.isAdded()) {
                fragmentTransaction
                        .replace(R.id.tabletViewFrameLayout,
                                bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                        .addToBackStack(null).commit();
            } else {
                fragmentTransaction.show(bakerryRecipieDetailViewFragment);
            }


        }

        /*else {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction
                    .replace(R.id.frameLayoutPhoneOptionsDetails, bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                    .addToBackStack(null).commit();

        }*/
    }

   /* @Override
    public void doBack() {
        // getFragmentManager().popBackStack("CHOOSE_OPTION_LAYOUT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }*/

}
