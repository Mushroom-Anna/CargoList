package com.zhenghanbei.cargolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.zhenghanbei.cargolist.data.CargoContract;

/**
 *
 * @author zhenghanbei
 * @date 2017/12/22
 */

public class CargoCursorAdapter extends CursorAdapter {

    /**context*/
    private Context mContext;
    /**view holder*/
    static private class ViewHolder{
        TextView tvName;
        TextView tvPrice;
        TextView tvQuantity;
        TextView tvSales;
        Button buttonSell;
    }
    public CargoCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
        mContext = context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.cargo_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = new ViewHolder();
        //get Views in the cargo_item
        holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
        holder.tvPrice = (TextView) view.findViewById(R.id.tv_item_price);
        holder.tvQuantity = (TextView) view.findViewById(R.id.tv_item_quantity);
        holder.tvSales = (TextView) view.findViewById(R.id.tv_item_sales);
        holder.buttonSell = (Button)view.findViewById(R.id.button_sell);
        //get information from cursor
        String name = cursor.getString(cursor.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_NAME));
        int price = cursor.getInt(cursor.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY));
        int sales = cursor.getInt(cursor.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_SALES));
        //set text
        holder.tvName.setText(name);
        holder.tvPrice.setText(String.valueOf(price));
        holder.tvQuantity.setText(String.valueOf(quantity));
        holder.tvSales.setText(String.valueOf(sales));
        //button set tag
        holder.buttonSell.setTag(cursor.getColumnIndex(CargoContract.CargoEntry._ID));
        Log.e("Cursor id", holder.buttonSell.getTag() + "");
        //set button listener
        holder.buttonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag();
                Uri currentUri = Uri.withAppendedPath(CargoContract.CargoEntry.CONTENT_URI,String.valueOf(id));
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString().trim());
                int sales = Integer.parseInt(holder.tvSales.getText().toString().trim());
                //change quantity and sales in the database
                if (quantity > 0){
                    quantity--;
                    sales++;
                    Log.e("Inside CursorAdapter", "id="+id);
                    //ContentValues
                    ContentValues values = new ContentValues();
                    values.put(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY, quantity);
                    values.put(CargoContract.CargoEntry.COLUMN_CARGO_SALES, sales);
                    int rows = context.getContentResolver().update(currentUri, values, null, null);
                    Log.e("Inside CursorAdapter", currentUri.toString());
                    Log.e("Inside CursorAdapter", "SELL id = "+ rows);
                }
            }
        });
    }
}
