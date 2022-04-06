package com.example.naiifipartner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GridItemView extends FrameLayout {

    private FirebaseFirestore db ;
    private MaterialTextView textView ;
    private MaterialCardView grid_card_view ;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_grid, this);
        textView = getRootView().findViewById(R.id.item_txt);
        grid_card_view = getRootView().findViewById(R.id.grid_card_view);


    }


    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);

        createInfo(textView);
    }

    public void display(boolean isSelected) {
        grid_card_view.setBackgroundResource(isSelected ? R.drawable.purple_square : R.drawable.grey_square);
    }

    private void createInfo(MaterialTextView materialTextView){
        db = FirebaseFirestore.getInstance();
        String category = materialTextView.getText().toString();
        String[] a = new String[1];
        db.collection("Description").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        try{
                            a[0] = document.getData().get(category).toString();

                        }
                        catch (Exception e){

                        }

                    }
                    try{

                        String[] b = a.clone();
                        if (!b[0].isEmpty()) {
                            materialTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info, 0);
                            // materialTextView.setPadding(8,8,8,8);
                        }

                    }
                    catch (Exception e){

                    }


                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("info tag:", "onFailure: "+e.getMessage());
            }
        });
    }




}