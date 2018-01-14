package com.zhenghanbei.cargolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghanbei.cargolist.data.CargoContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    /**views*/
    private EditText mTvCargoName;
    private EditText mTvCargoPrice;
    private TextView mTvCargoQuantity;
    private TextView mTvCargoSales;
    /**arguments to hold data from TextView*/
    private String mCargoName;
    private int mCargoPrice = 0;
    private int mCargoQuantity = 0;
    private int mCargoSales = 0;
    /**uri passed in from mainActivity*/
    private Uri mCurrentUri;
    /**cargo edit loader*/
    private final static int CARGO_EDIT_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //find views
        mTvCargoName = (EditText) findViewById(R.id.tv_cargo_name);
        mTvCargoPrice = (EditText) findViewById(R.id.tv_cargo_price);
        mTvCargoQuantity = (TextView) findViewById(R.id.tv_cargo_quant);
        mTvCargoSales = (TextView) findViewById(R.id.tv_cargo_sales);
        //four buttons to add or minus quantity and sales
        Button quantityAdd = (Button) findViewById(R.id.button_quantity_add);
        Button quantityMinus = (Button) findViewById(R.id.button_quantity_minus);
        Button salesAdd = (Button) findViewById(R.id.button_sales_add);
        Button salesMinus = (Button) findViewById(R.id.button_sales_minus);
        quantityAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCargoQuantity++;
                mTvCargoQuantity.setText(String.valueOf(mCargoQuantity));
            }
        });
        quantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCargoQuantity > 0){
                    mCargoQuantity--;
                    mTvCargoQuantity.setText(String.valueOf(mCargoQuantity));
                }
            }
        });
        salesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCargoSales++;
                mTvCargoSales.setText(String.valueOf(mCargoSales));
            }
        });
        salesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCargoSales > 0){
                    mCargoSales--;
                    mTvCargoSales.setText(String.valueOf(mCargoSales));
                }
            }
        });
        //receive intent data
        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if (mCurrentUri == null){
            setTitle("New Cargo");
        }else{
            setTitle("Change Cargo");
            getSupportLoaderManager().initLoader(CARGO_EDIT_LOADER, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentUri == null){
            MenuItem delete = menu.findItem(R.id.delete_item);
            delete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                //save
                saveCargo();
                return true;
            case R.id.delete_item:
                //delete
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{
                CargoContract.CargoEntry._ID,
                CargoContract.CargoEntry.COLUMN_CARGO_NAME,
                CargoContract.CargoEntry.COLUMN_CARGO_PRICE,
                CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY,
                CargoContract.CargoEntry.COLUMN_CARGO_SALES};
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        //move cursor to first
        if (data.moveToFirst()) {
            mCargoQuantity = data.getInt(data.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY));
            mCargoSales = data.getInt(data.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_SALES));
            mTvCargoName.setText(data.getString(data.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_NAME)));
            mTvCargoPrice.setText(String.valueOf(data.getInt(data.getColumnIndex(CargoContract.CargoEntry.COLUMN_CARGO_PRICE))));
            mTvCargoQuantity.setText(String.valueOf(mCargoQuantity));
            mTvCargoSales.setText(String.valueOf(mCargoSales));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //cursor is invalid
        mTvCargoName.setText("");
        mTvCargoPrice.setText("0");
        mTvCargoQuantity.setText("0");
        mTvCargoSales.setText("0");
    }

    /**
     * save item when click done
     */
    private void saveCargo(){
        //get values
        String name = mTvCargoName.getText().toString().trim();
        String price = mTvCargoPrice.getText().toString().trim();
        String quantity = mTvCargoQuantity.getText().toString().trim();
        String sales = mTvCargoSales.getText().toString().trim();
        //check data
        if (name == null || TextUtils.isEmpty(name)){
            Toast.makeText(this, "Item name is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price == null || TextUtils.isEmpty(price)){
            Toast.makeText(this, "Item price is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        mCargoName = name;
        mCargoPrice = Integer.parseInt(price);
        mCargoQuantity = Integer.parseInt(quantity);
        mCargoSales = Integer.parseInt(sales);
        //ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(CargoContract.CargoEntry.COLUMN_CARGO_NAME, mCargoName);
        contentValues.put(CargoContract.CargoEntry.COLUMN_CARGO_PRICE, mCargoPrice);
        contentValues.put(CargoContract.CargoEntry.COLUMN_CARGO_QUANTITY, mCargoQuantity);
        contentValues.put(CargoContract.CargoEntry.COLUMN_CARGO_SALES, mCargoSales);
        if (mCurrentUri == null){
            Uri uri = getContentResolver().insert(CargoContract.CargoEntry.CONTENT_URI, contentValues);
        } else {
            int updateRows = getContentResolver().update(mCurrentUri, contentValues, null, null);
        }
        //if everything is fine, finish the activity
        finish();
    }

    /**
     * delete item when click done
     */
    private void deleteCargo(){
        if (mCurrentUri == null){
            return;
        }else{
            int deleteRows = getContentResolver().delete(mCurrentUri, null, null);
        }
        finish();
    }

    /**
     * show the dialog to confirm delete action
     */
    private void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // delete item
                        deleteCargo();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel the dialog
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        //show the dialog
        builder.create().show();
    }
}
