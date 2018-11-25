package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.example.anshultech.miriambakery.Utilities.BakeryChooseOptionBackPressedListener;

import java.util.ArrayList;

public class BakerryRecipieDetailViewFragment extends Fragment {

    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private boolean mTwoPane = false;
    private RecyclerView recipiesMasterListRecyclerView1;

    public BakerryRecipieDetailViewFragment() {
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
        RECIPE_LIST_TYPE = getArguments().getString(getResources().getString(R.string.list_type));
        mTwoPane = getArguments().getBoolean(getResources().getString(R.string.is_two_pane));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bakery_recipie_detail_view, container, false);

        mRecipiDetailsViewRecyClerView = (RecyclerView) rootView.findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list));
            mRecipeMasterListClickedPosition = savedInstanceState.getInt(getResources().getString(R.string.instance_clicked_position));
            RECIPE_LIST_TYPE = savedInstanceState.getString(getResources().getString(R.string.instance_list_type));
            mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.instance_is_two_pane));
        }

        ((BakeryHome) getActivity()).setOnOptionChooseBackPressedListener(new BakeryChooseOptionBackPressedListener(getActivity(),
                mBakeryRecipiesListBeans, mRecipeMasterListClickedPosition, mTwoPane));

        View view = getActivity().findViewById(R.id.recipiesMasterListRecyclerView);
        if (view instanceof RecyclerView) {
            recipiesMasterListRecyclerView1 = (RecyclerView) view;
            recipiesMasterListRecyclerView1.setVisibility(View.VISIBLE);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        loadRecipieListItems();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.instance_bakery_master_list), mBakeryRecipiesListBeans);
        outState.putInt(getResources().getString(R.string.instance_clicked_position), mRecipeMasterListClickedPosition);
        outState.putString(getResources().getString(R.string.instance_list_type), RECIPE_LIST_TYPE);
        outState.putBoolean(getResources().getString(R.string.instance_is_two_pane), mTwoPane);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("ingredients")) {
            mBakeryIngridentsListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
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
            mBakeryStepsListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.steps_list));
            //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recipie Steps");
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(int position,
                                                                           ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {

                                Bundle bundle = new Bundle();
                                bundle.putInt(getResources().getString(R.string.steps_clicked_position), position);
                                bundle.putParcelableArrayList(getResources().getString(R.string.video_steps_list), bakeryStepsListBeans);
                                bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
                                BakeryRecipeStepsVideoPlayerFragment bakeryRecipeStepsVideoPlayerFragment = new BakeryRecipeStepsVideoPlayerFragment();
                                bakeryRecipeStepsVideoPlayerFragment.setArguments(bundle);
                                if (mTwoPane == true) {
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    if (!bakeryRecipeStepsVideoPlayerFragment.isAdded()) {
                                        fragmentTransaction
                                                .replace(R.id.tabletViewFrameLayout, bakeryRecipeStepsVideoPlayerFragment, getResources().getString(R.string.bakeryRecipeStepsVideoPlayerFragment))
                                                .addToBackStack(null).commit();
                                    } else {
                                        fragmentTransaction.show(bakeryRecipeStepsVideoPlayerFragment);
                                    }

                                }

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }
    }

}
