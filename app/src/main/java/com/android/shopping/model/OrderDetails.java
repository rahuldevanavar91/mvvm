package com.android.shopping.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.android.shopping.model.converter.PriceTyepConverter;
import com.android.shopping.model.converter.ProductListConverter;

import java.util.List;

@Entity(tableName = "order_details")
public class OrderDetails {

    @Ignore
    private int viewType;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private int id;

    @ColumnInfo(name = "product_items")
    @TypeConverters(ProductListConverter.class)
    private List<ProductItem> productList;

    @Ignore
    private ProductItem productItem;

    @ColumnInfo(name = "order_place_date")
    private String date;

    @ColumnInfo(name = "price_details")
    @TypeConverters(PriceTyepConverter.class)
    private PriceDetails priceDetails;

    public int getId() {
        return id;
    }

    public OrderDetails() {

    }

    public OrderDetails(List<ProductItem> productItem, String time, PriceDetails priceDetails) {
        this.productList = productItem;
        this.date = time;
        this.priceDetails = priceDetails;
    }


    public List<ProductItem> getProductList() {
        return productList;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setProductList(List<ProductItem> productItem) {
        this.productList = productItem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof OrderDetails) {
            return id == ((OrderDetails) obj).id;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 17 * 7 + this.id;
    }
}
