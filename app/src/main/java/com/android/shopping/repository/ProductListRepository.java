package com.android.shopping.repository;

import android.content.Context;
import android.util.Log;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.ProductItem;
import com.android.shopping.network.ApiEndPoint;
import com.android.shopping.network.RetrofitService;
import com.android.shopping.ui.adapter.ProductListAdapter;
import com.android.shopping.util.Util;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class ProductListRepository {

    private ProductResponseDao mProductResponseDao;
    private ApiEndPoint mApiEndPoint;
    private List<ProductItem> productItems;
    private CompositeDisposable compositeDisposable;
    private Context mContext;

    public ProductListRepository(Context context) {
        mProductResponseDao = AppDatabase.getInstance(context).responseDao();
        mApiEndPoint = RetrofitService.getRetrofitInstance().create(ApiEndPoint.class);
        productItems = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        mContext = context;
    }

    public Flowable<List<ProductItem>> getProductList() {
        if (Util.isNetworkConnected(mContext)) {
            return getDatFromAPI();
        } else {
            return getDataFromDb();
        }
    }

    private Flowable<List<ProductItem>> getDatFromAPI() {
        return mApiEndPoint.getProducts().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .map(productResponse -> {
                    getOrderList();
                    if (productResponse != null && productResponse.getProducts() != null) {
                        insertProductsToDb(productResponse.getProducts());
                        productItems.addAll(0, productResponse.getProducts());
                    }
                    return productItems;
                }).doOnError(throwable -> Log.i("Test", "data " + throwable.getMessage()));

    }

    private Flowable<List<ProductItem>> getDataFromDb() {
        return mProductResponseDao.getListingProducts().map(new Function<List<ProductItem>, List<ProductItem>>() {
            @Override
            public List<ProductItem> apply(List<ProductItem> products) throws Exception {
                productItems = products;
                getOrderList();
                return productItems;
            }
        });
    }

    private void insertProductsToDb(List<ProductItem> products) {
        Completable.fromCallable(() -> mProductResponseDao.insertAllProducts(products))
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    private void getOrderList() {
        compositeDisposable.add(mProductResponseDao.getOrderDetails().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableSubscriber<List<OrderDetails>>() {
                    @Override
                    public void onNext(List<OrderDetails> list) {
                        if (list != null) {
                            addOrderToProductList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private void addOrderToProductList(List<OrderDetails> list) {
        for (OrderDetails orderDetails : list) {
            ProductItem productItem = new ProductItem();
            productItem.setViewType(ProductListAdapter.VIEW_TYPE_ORDER_PLACED);
            productItem.setOrderItem(orderDetails);
            if (!productItems.contains(productItem)) {
                productItems.add(productItem);
            }
        }
    }

    public Observable<List<ProductItem>> cancelOrder(int parentPosition, int orderId) {
        return Observable.fromCallable(() -> mProductResponseDao.cancelOrder(orderId)).subscribeOn(Schedulers.io())
                .map(integer -> {
                    productItems.remove(parentPosition);
                    return productItems;
                });
    }
}
