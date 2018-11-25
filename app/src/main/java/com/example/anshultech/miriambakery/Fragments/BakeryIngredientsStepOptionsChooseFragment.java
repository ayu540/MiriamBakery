package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
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

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;


public class BakeryIngredientsStepOptionsChooseFragment extends Fragment {

    private Button mIngredientsButton;
    private Button mStepButton;
    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
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

        mBakeryRecipiesListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
        mRecipeMasterListClickedPosition = getArguments().getInt(getResources().getString(R.string.clicked_position));
        mBakeryIngridentsListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
        mBakeryStepsListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.steps_list));
        mTwoPane = getArguments().getBoolean(getResources().getString(R.string.is_two_pane));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View attachedRootView = inflater.inflate(R.layout.indridentasstepsclicklayout, container, false);


        mIngredientsButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseIngriedientsButton);
        mStepButton = (Button) attachedRootView.findViewById(R.id.recipeOptyionChooseStepsButton);


        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps_list));
            mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.is_two_pane));
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
        outState.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
        outState.putParcelableArrayList(getResources().getString(R.string.ingredient_list), mBakeryIngridentsListBeans);
        outState.putParcelableArrayList(getResources().getString(R.string.steps_list), mBakeryStepsListBeans);
        outState.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
    }

    private void prepareRecipieButtonData() {

        if (mBakeryStepsListBeans != null && mBakeryIngridentsListBeans != null) {
            final Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
            bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
            bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelableArrayList(getResources().getString(R.string.ingredient_list), mBakeryIngridentsListBeans);
                    bundle.putString(getResources().getString(R.string.list_type), "ingredients");
                    loadNextActivity(bundle);

                }
            });

            mStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelableArrayList(getResources().getString(R.string.steps_list), mBakeryStepsListBeans);
                    bundle.putString(getResources().getString(R.string.list_type), "Steps");
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
                                bakerryRecipieDetailViewFragment, getResources().getString(R.string.bakerryRecipieDetailViewFragment))
                        .addToBackStack(null).commit();
            } else {
                fragmentTransaction.show(bakerryRecipieDetailViewFragment);
            }
            
        }

    }

}
