package com.android.shopping.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.android.shopping.model.ProductItem;
import com.android.shopping.network.Resource;
import com.android.shopping.viewModel.ProductLisViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ProductListRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    ProductListRepository repository;

    @Mock
    ProductLisViewModel viewModel;

    @Mock
    Observer<Resource<List<ProductItem>>> observer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        repository = mock(ProductListRepository.class);
        viewModel = mock(ProductLisViewModel.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProductList() {
        List<ProductItem> data = getEmptyList();
        when(repository.getProductList())
                .thenReturn(Flowable.fromArray(data).map(productItems -> null));
        // viewModel.getProductListResult().observeForever(observer);
        viewModel.getProductList();

        verify(observer).onChanged(Resource.success(data));
    }

    @Test
    public void getProdutListError() {
        Throwable errror = new Throwable("No product found");
        when(repository.getProductList())
                .thenReturn(Flowable.just(getEmptyList()));

    }

    @Test
    public void cancelOrder() {
        when(repository.cancelOrder(0, 0))
                .thenReturn(Observable.fromArray(getEmptyList()));
        viewModel.cancelOrder(0, 0);
        verify(observer).onChanged(Resource.loading());


    }

    private List<ProductItem> getEmptyList() {
        return new ArrayList<>();
    }
}