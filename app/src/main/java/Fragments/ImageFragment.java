package Fragments;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import Adapter.ImageAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ImageFragment extends Fragment {


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ImageAdapter imageAdapter ;
    private RecyclerView  imageRecyclerView ;
    private FloatingActionButton add_images_button ;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_image, container, false);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        imageRecyclerView = view.findViewById(R.id.image_recyclerView);
        add_images_button = view.findViewById(R.id.add_images_button);

        imageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        imageRecyclerView.setLayoutManager(linearLayoutManager);



        fetch_url();


        return view ;


    }

    private void fetch_url(){

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        String user = mAuth.getCurrentUser().getUid();

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docRef = rootRef.collection(user).document("imageUrl");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        //create_imageview(map);

                        backgroundAct(map);
                    }
                }
            }
        });
    }



    private void backgroundAct(Map<String ,Object> map){

        Observable<Map<String ,Object>> observable = Observable.just(map);

        Observer<Map<String ,Object>> observer = new Observer<Map<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Map<String, Object> map) {
                ImageAdapter imageAdapter = new ImageAdapter(getContext() , map);
                imageRecyclerView.setAdapter(imageAdapter);



            }

            @Override
            public void onError(Throwable e) {
                Log.d("Image fetching error", "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    /*private void delteImage(final String pid, String imageUrl) {


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Deleting");
        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 HashMap<String , Object> map  = new HashMap<>();
                 map.put(pid , FieldValue.delete());
                db.collection(user).document("imageUrl").update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Image Deleted Successfully .", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }*/
}