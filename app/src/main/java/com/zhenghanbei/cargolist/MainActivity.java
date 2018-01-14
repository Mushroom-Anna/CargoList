package com.zhenghanbei.cargolist;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhenghanbei.cargolist.data.CargoContract;

/**
 * @author zhenghanbei
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CARGO_LOADER = 0;
    private CargoCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init loader
        getLoaderManager().initLoader(CARGO_LOADER, null, this);

        //find listView and set Adapter
        ListView cargoList = (ListView) findViewById(R.id.list_view_cargo);
        mCursorAdapter = new CargoCursorAdapter(this, null);
        cargoList.setAdapter(mCursorAdapter);

        //find add button @fab and set listener
        FloatingActionButton addNewCategory = (FloatingActionButton) findViewById(R.id.fab);
        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddCategory = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intentAddCategory);
            }
        });

        //listView on click listener
        cargoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentEditor = new Intent(MainActivity.this, EditorActivity.class);
                Uri contentUri = ContentUris.withAppendedId(CargoContract.CargoEntry.CONTENT_URI,id);
                intentEditor.setData(contentUri);
                startActivity(intentEditor);
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //load cursor by URI: com.zhenghanbei.cargolist/cargo
        return new CursorLoader(this, CargoContract.CargoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("MainActivity Loader","swap cursor");
        //swap new cursor in cursorAdapter
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //close cursor
        mCursorAdapter.swapCursor(null);
    }
}
