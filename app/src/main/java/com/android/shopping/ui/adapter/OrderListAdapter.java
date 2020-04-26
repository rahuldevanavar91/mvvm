package com.android.shopping.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.android.shopping.util.ItemClickListener;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductItem> mOrderList;
    private Context mContext;
    private ItemClickListener mListener;

    public OrderListAdapter(Context context, List<ProductItem> list, ItemClickListener listener, int parentPosition) {
        mOrderList = list;
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.cart_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductItem item = mOrderList.get(position);
        OrderItemViewHolder orderItemViewHolder = (OrderItemViewHolder) holder;
        orderItemViewHolder.setPriceData(mContext, item, holder.getAdapterPosition());
        orderItemViewHolder.qty.setText("Qty : " + item.getQty());

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public void updateData(List<ProductItem> orderDetails, int position) {
        mOrderList = orderDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mOrderList.get(position).getViewType();
    }


    public class OrderItemViewHolder extends PriceViewHolder {
        private TextView qty;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            itemView.findViewById(R.id.qty_spinner).setVisibility(View.GONE);
            itemView.findViewById(R.id.remove).setVisibility(View.GONE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
        }
    }
}
