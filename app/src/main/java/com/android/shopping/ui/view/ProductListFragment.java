package com.android.shopping.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.android.shopping.network.RequestType;
import com.android.shopping.network.Resource;
import com.android.shopping.ui.adapter.ProductListAdapter;
import com.android.shopping.util.NoPredicativeAnimGridLayoutManager;
import com.android.shopping.util.RecyclerViewMargin;
import com.android.shopping.viewModel.ProductLisViewModel;

import java.util.List;

public class ProductListFragment extends BaseFragment {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private ProductLisViewModel mViewModel;

    public ProductListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.product_list_recycler);
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, 2);
        mRecyclerView.addItemDecoration(decoration);
        mProgressBar = view.findViewById(R.id.progress_bar);
        GridLayoutManager gridLayoutManager = new NoPredicativeAnimGridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter != null && position < mAdapter.getItemCount()) {
                    return mAdapter.getItemViewType(position) == ProductListAdapter.VIEW_TYPE_PRODUCT_LIST ? 1 : 2;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Shopping", true);
        mViewModel = new ViewModelProvider(this).get(ProductLisViewModel.class);
        mViewModel.getProductListResult().observe(getViewLifecycleOwner(), this::onChange);
    }

    private void onChange(Resource<List<ProductItem>> listResource) {
        switch (listResource.getStatus()) {
            case ERROR:
                mProgressBar.setVisibility(View.GONE);
                break;
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                if (listResource.getRequestType() == RequestType.cancelOrder) {
                    mAdapter.update(listResource.getData(), listResource.getPosition());
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mAdapter = new ProductListAdapter(getContext(), listResource.getData(), this::onItemClick);
                    mRecyclerView.setAdapter(mAdapter);
                }
                break;
        }
    }


    private void onItemClick(View view, int position, Object data) {
        if (view.getId() == R.id.button) {
            mViewModel.cancelOrder(position, (int) data);
        } else {
            Bundle args = new Bundle();
            FragmentNavigator.Extras.Builder extras = new FragmentNavigator.Extras.Builder();
            extras.addSharedElement(view, view.getTransitionName());

            args.putString(getString(R.string.product_id), (String) data);
            Navigation.
                    findNavController(view).
                    navigate(R.id.action_mainFragment_to_productDetailFragment
                            , args, null, extras.build());
        }
    }


}
