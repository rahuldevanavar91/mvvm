package com.android.shopping.repository;

import android.app.Application;
import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.android.shopping.model.ProductItem;
import com.android.shopping.network.Resource;
import com.android.shopping.viewModel.ProductLisViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
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
        Application app = mock(Application.class);
        repository = mock(ProductListRepository.class);
        viewModel = new ProductLisViewModel(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProductList() {
        Resource<List<ProductItem>> data = Resource.success(getEmptyList());
        when(repository.getProductList())
                .thenReturn(Flowable.fromArray(getEmptyList()).map(new Function<List<ProductItem>, Resource<List<ProductItem>>>() {
                    @Override
                    public Resource<List<ProductItem>> apply(List<ProductItem> productItems) throws Exception {
                        return data;
                    }
                }));
        viewModel.getProductListResult().observeForever(observer);
      //  viewModel.getProductList();
        verify(observer).onChanged(data);
    }

    @Test
    public void getProdutListError() {
        Throwable errror = new Throwable("No product found");

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