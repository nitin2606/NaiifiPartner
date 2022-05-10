package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class ImageFragment extends Fragment {
    private LinearLayout ll_image ;
    private FrameLayout image_frame ;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_image, container, false);

        ll_image = view.findViewById(R.id.ll_image);
        image_frame = view.findViewById(R.id.image_frame);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String user = mAuth.getCurrentUser().getUid().toString();

        db.collection(user).document("imageUrl").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String url = task.getResult().get("url0").toString();
                    create_imageview(image_frame , url);

                }
            }
        });



        return view ;
    }


    private void create_imageview(FrameLayout frameLayout , String url ){


        ImageView imageView = new ImageView(getContext());
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(500, 500);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(500 , 500);
        imageView.setLayoutParams(lp);
        frameLayout.addView(imageView);
        Picasso.get().load(url).into(imageView);

    }
}