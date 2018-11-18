package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;

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
    private final int BAKERY_STEPS_CLICKED = 13;
    private boolean mTwoPane = false;

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
        mBakeryRecipiesListBeans = getArguments().getParcelableArrayList("BAKERY_MASTER_LIST");
        mRecipeMasterListClickedPosition = getArguments().getInt("CLICKED_POSITION");
        RECIPE_LIST_TYPE = getArguments().getString("LIST_TYPE");
        mTwoPane = getArguments().getBoolean("IS_TWO_PANE");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_bakery_recipie_detail_view, container, false);

        mRecipiDetailsViewRecyClerView = (RecyclerView) rootView.findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = savedInstanceState.getInt("CLICKED_POSITION");
            RECIPE_LIST_TYPE = savedInstanceState.getString("LIST_TYPE");
            mTwoPane = savedInstanceState.getBoolean("IS_TWO_PANE");
        }


        loadRecipieListItems();



        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("INSTANCE_BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        outState.putInt("INSTANCE_CLICKED_POSITION", mRecipeMasterListClickedPosition);
        outState.putString("INSTANCE_LIST_TYPE", RECIPE_LIST_TYPE);
        outState.putBoolean("INSTANCE_IS_TWO_PANE", mTwoPane);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("Ingredients")) {
            //    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recipie Ingredients");
            mBakeryIngridentsListBeans = getArguments().getParcelableArrayList("INGREDINET_LIST");
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
            mBakeryStepsListBeans = getArguments().getParcelableArrayList("STEPS_LIST");
            //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Recipie Steps");
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(int position,
                                                                           ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {

                                Bundle bundle = new Bundle();
                                bundle.putInt("STEPS_CLICKED_POSITION", position);
                                bundle.putParcelableArrayList("VIDEO_STEPS_LIST", bakeryStepsListBeans);
                                bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                                BakeryRecipeStepsVideoPlayerFragment bakeryRecipeStepsVideoPlayerFragment = new BakeryRecipeStepsVideoPlayerFragment();
                                bakeryRecipeStepsVideoPlayerFragment.setArguments(bundle);
                                //  intent.putExtras(bundle);
                                if (mTwoPane == true) {


                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    if (!bakeryRecipeStepsVideoPlayerFragment.isAdded()) {
                                        fragmentTransaction
                                                .replace(R.id.tabletViewFrameLayout, bakeryRecipeStepsVideoPlayerFragment, "bakeryRecipeStepsVideoPlayerFragment")
                                                .addToBackStack(null).commit();
                                    } else {
                                        fragmentTransaction.show(bakeryRecipeStepsVideoPlayerFragment);
                                    }


                                } else {

                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    fragmentTransaction
                                            .replace(R.id.frameLayoutPhoneOptionsDetails,
                                                    bakeryRecipeStepsVideoPlayerFragment, "bakeryRecipeStepsVideoPlayerFragment")
                                            .addToBackStack(null).commit();

                                }

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }
    }


//
//    @Override
//    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//        //super.onBackPressed();
//        Intent bakeryStepsReturnIntent = getActivity().getIntent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
//        bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
//        bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryIngridentsListBeans());
//        bundle.putParcelableArrayList("STEPS_LIST", mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryStepsListBeans());
//        bakeryStepsReturnIntent.putExtras(bundle);
//        getActivity().setResult(RESULT_OK, bakeryStepsReturnIntent);
//        getActivity().finish();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == BAKERY_STEPS_CLICKED) {
//                if (data != null) {
//                    mBakeryRecipiesListBeans = data.getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
//                    //  mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
//                    RECIPE_LIST_TYPE = data.getExtras().getString("LIST_TYPE");
//                }
//                loadRecipieListItems();
//            }
//        }
//    }
}
