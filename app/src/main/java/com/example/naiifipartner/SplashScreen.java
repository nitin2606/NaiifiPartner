package com.example.naiifipartner;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {



    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth=FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(SplashScreen.this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

            finish();

        } else {

            try {

                String uid = FirebaseAuth.getInstance().getUid().toString();
                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("PhoneNo").child(uid);

                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Salons").child(value).child("info");

                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String status = snapshot.getValue(String.class);

                                if (status.equals("incomplete")) {
                                    Intent mainIntent = new Intent(SplashScreen.this, InfoActivity.class);

                                    startActivity(mainIntent);
                                    finish();

                                } else if (status.equals("complete")) {
                                    Intent mainIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                                    //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);

                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } catch (Exception e) {
                Log.d("Status check error :", e.getMessage());
            }

        }

    }




}