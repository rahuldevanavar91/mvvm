package com.android.shopping.repository;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.OrderDetails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.rxjava3.subscribers.TestSubscriber;

import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ProductListRepositoryTest {
    @Mock
    ProductResponseDao dao;

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws Exception {
        mockContext = mock(Context.class);
        dao = AppDatabase.getInstance(ApplicationProvider.getApplicationContext()).responseDao();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProductList() {
        TestSubscriber<List<OrderDetails>> testSubscriber = new TestSubscriber<>();
        dao.getOrderDetails().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        // testSubscriber.onNext();
    }

    @Test
    public void cancelOrder() {
    }
}