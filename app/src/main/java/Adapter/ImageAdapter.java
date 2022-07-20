package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
        holder.zoom_imageView.setTag("url"+String.valueOf(position));

        holder.zoom_imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.image_progressBar.setVisibility(View.GONE);
                holder.zoom_imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView zoom_imageView ;
        private ProgressBar image_progressBar ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            zoom_imageView = itemView.findViewById(R.id.zoom_imageView);
            image_progressBar = itemView.findViewById(R.id.image_progressBar);

            /*zoom_imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    image_progressBar.setVisibility(View.GONE);
                    zoom_imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                }
            });*/


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
