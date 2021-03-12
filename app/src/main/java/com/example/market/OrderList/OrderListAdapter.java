package com.example.market.OrderList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.market.R;
import com.example.market.DetailOrderListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                showContent(orderItemList.get(position).getMarketName(),orderItemList.get(position).getOrderContent(), orderItemList.get(position).getDate());
            }
        });

        return view;
    }

    private void showContent(String marketName, String content, String date) {

        Intent intent = new Intent(context, DetailOrderListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("marketName", marketName);
        intent.putExtra("content", content);
        intent.putExtra("date", date);
        context.startActivity(intent);

        /*final Dialog dialog = new Dialog(context, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.custom_dialog);
        // 8-9번 참고
        dialog.show();


        TextView dContent = dialog.findViewById(R.id.dialog_content),
                dDate = dialog.findViewById(R.id.dialog_date),
                dStore = dialog.findViewById(R.id.dialog_title);

        dStore.setText(marketName);
        dContent.setText(content);
        dDate.setText(setDateForm(date));
        dContent.setMovementMethod(new ScrollingMovementMethod());*/
    }

    //날짜 형식 변경
    public String setDateForm(String date) {
        String newDate = null;
        SimpleDateFormat receiveDate = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
        SimpleDateFormat changeDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        try {
            Date receiveForm = receiveDate.parse(date);
            newDate = changeDate.format(receiveForm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

}
