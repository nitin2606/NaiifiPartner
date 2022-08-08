package Adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.naiifipartner.GridItemView;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter_add extends BaseAdapter {




    private final AppCompatActivity activity;
    private final String[] strings_add;
    public List selectedPositions_add;


    public GridViewAdapter_add(String[] strings, AppCompatActivity activity ) {
        this.strings_add = strings;
        this.activity = activity;

        selectedPositions_add = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return strings_add.length;
    }

    @Override
    public Object getItem(int position) {
        return strings_add[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(strings_add[position], selectedPositions_add.contains(position));

        return customView;
    }


}