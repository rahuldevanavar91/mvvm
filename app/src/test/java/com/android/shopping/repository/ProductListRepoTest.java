package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductListRepoTest {

    @Mock
    ProductResponseDao dao;

    @Mock
    Context mockContext;

    @Before
    public void setUp() {
        dao = AppDatabase.getInstance(mockContext).responseDao();
    }

    @Test
    public void test() {


    }
}

