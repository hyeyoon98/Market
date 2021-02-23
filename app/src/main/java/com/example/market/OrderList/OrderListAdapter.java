package com.example.market.OrderList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.market.R;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrderListAdapter(Context context, List<OrderItem> orderItemList){
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @Override
    public int getCount() {
        return orderItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.order_item_list, null);

        TextView userName = (TextView)v.findViewById(R.id.tv_market_name);
        TextView userOrder = (TextView)v.findViewById(R.id.tv_order_content);
        TextView time = (TextView)v.findViewById(R.id.tv_date);

        userName.setText(orderItemList.get(position).getMarketName());
        userOrder.setText(orderItemList.get(position).getOrderContent());
        time.setText(orderItemList.get(position).getDate());

        return v;
    }
}
