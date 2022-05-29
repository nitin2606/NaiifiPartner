package Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.naiifipartner.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    @NonNull


    private Context mComtext ;
    private Map<String,Object> map ;


    public ImageAdapter(Context mComtext , Map<String,Object> map){
        this.mComtext = mComtext;
        this.map=map;
    }


    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {

        Picasso.get().load(String.valueOf(map.get("url"+String.valueOf(position)))).fit().into((holder.zoom_imageView));
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView zoom_imageView ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            zoom_imageView = itemView.findViewById(R.id.zoom_imageView);


        }
    }

    /*private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return true;
    }*/
}
