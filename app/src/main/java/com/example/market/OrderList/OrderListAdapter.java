package com.example.market.OrderList;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

public class OrderListAdapter extends BaseAdapter {

    private Context context;
    private List<OrderItem> orderItemList;

    public OrderListAdapter(List<OrderItem> orderItemList){
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position; // ListView 위치(첫 번째 = 0)
        context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater != null)
                view = inflater.inflate(R.layout.order_item_list, viewGroup, false);
        }

        TextView marketName = view.findViewById(R.id.tv_market_name),
                orderContent = view.findViewById(R.id.tv_order_content),
                date = view.findViewById(R.id.tv_date);

        OrderItem orderItem = orderItemList.get(position);

        marketName.setText(orderItem.getMarketName());
        orderContent.setText(orderItem.getOrderContent());
        //setDateFormat(date, orderItem.getDate());
        date.setText(orderItem.getDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시글 터치 시 내용 보여주는 다이얼로그 띄우기
                showContent(orderItemList.get(position).getOrderContent(), orderItemList.get(position).getDate());
            }
        });

        return view;
    }

    private void showContent(String content, String date) {
        final Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.custom_dialog);
        // 8-9번 참고
        dialog.setCancelable(false);
        dialog.show();

        TextView dContent = dialog.findViewById(R.id.dialog_content),
                dDate = dialog.findViewById(R.id.dialog_date);

        ImageView dClose = dialog.findViewById(R.id.dialog_close_img);
        Button bClose = dialog.findViewById(R.id.dialog_close);

        dContent.setText(content);
        dDate.setText(date);
        dContent.setMovementMethod(new ScrollingMovementMethod());
        dClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


   /* public void setDateFormat(TextView tvDate, String date) {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", java.util.Locale.getDefault());
        strDate = dateFormat.format(date);
        dateView = (TextView)view.findViewById(R.id.tv_date);
        date.setText;
    }*/
}
