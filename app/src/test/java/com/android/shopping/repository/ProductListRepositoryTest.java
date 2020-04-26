package com.android.shopping.repository;

import android.app.Application;

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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
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
    }

    @Test
    public void getProdutListError() {
        Throwable errror = new Throwable("No product found");

    }

    @Test
    public void cancelOrder() {
    }

    private List<ProductItem> getEmptyList() {
        return new ArrayList<>();
    }
}