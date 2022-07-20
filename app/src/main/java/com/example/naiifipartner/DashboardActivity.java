package com.example.naiifipartner;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;

import Fragments.AddFragment;
import Fragments.EditFragment;
import Fragments.HelpFragment;
import Fragments.HomeFragment;
import Fragments.ImageFragment;
import Fragments.InsightsFragment;
import Fragments.SettingsFragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ImageView menuIcon;
    private FirebaseFirestore db;
    private Fragment navFragment ;

    private Dialog dialog;
    private Switch status_switch;
    private TextView status_text , test_text;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_dashboard);

        switch_control();

        deleteCache(DashboardActivity.this);

        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "");
        fragmentTransaction.commit();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        check_salon_status();

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        View header = navigationView.getHeaderView(0);

        status_switch = header.findViewById(R.id.status_switch);
        status_text = header.findViewById(R.id.status_text);







        String[] seats =  {"1","2","3","4","5","6","7","8","9","10","11"};



        status_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_switch.isChecked()){
                    update_salon_status("Closed");



                }
                else{
                    update_salon_status("Open");



                }
            }
        });



        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {

                int id = item.getItemId();

                switch (id) {

                    case R.id.nav_home:
                        navFragment = new HomeFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_edit:

                        navFragment = new EditFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;


                    case R.id.nav_add:

                        navFragment = new AddFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;


                    case R.id.salon_images:
                        navFragment = new ImageFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.current_seat:

                        
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);

                        LayoutInflater inflater1 = DashboardActivity.this.getLayoutInflater();
                        View dialogView1 = inflater1.inflate(R.layout.custom_seat_dialog, null);
                        builder1.setView(dialogView1);

                        MaterialTextView seat_text = dialogView1.findViewById(R.id.current_seat_text);
                        Button ok = dialogView1.findViewById(R.id.btn_ok);
                        Button cancel1 = dialogView1.findViewById(R.id.btn_cancel);

                        AlertDialog dialog1 = builder1.create();
                        dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                        seat_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gen_single_dialog(seats,seat_text);
                            }
                        });

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(seat_text.getText().toString().isEmpty()){
                                    Toast.makeText(DashboardActivity.this, "No option selected !", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    updateSeat(seat_text.getText().toString().trim(),dialog1);

                                }

                            }
                        });

                        cancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                        break;


                    case R.id.nav_settings:

                        navFragment = new SettingsFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_help:

                        navFragment = new HelpFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_insight:

                        navFragment = new InsightsFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:

                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

                        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
                        builder.setView(dialogView);

                        Button logout = dialogView.findViewById(R.id.btn_okay);
                        Button cancel = dialogView.findViewById(R.id.btn_cancel);

                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawerLayout.closeDrawer(GravityCompat.START);
                                dialog.dismiss();
                                mAuth.signOut();
                                Intent intent = new Intent(DashboardActivity.this , LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(DashboardActivity.this, "Logged out ",Toast.LENGTH_SHORT).show();

                            }
                        });


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        });

                        dialog.show();
                        break;




                    case R.id.nav_share:
                        Toast.makeText(DashboardActivity.this, "Share is clicked",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_rate:
                        Toast.makeText(DashboardActivity.this, "Rate us is Clicked",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }
                if (navFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, navFragment).commit();
                }
                return true;
            }
        });



        FirebaseUser muser = mAuth.getCurrentUser();
        if(muser==null){
            Intent i = new Intent(DashboardActivity.this , LoginActivity.class);
            startActivity(i);
            finish();

        }


    }
    public void onStart() {

        super.onStart();
        FirebaseUser muser = mAuth.getCurrentUser();
        if(muser==null){
            Intent i = new Intent(DashboardActivity.this , LoginActivity.class);
            startActivity(i);
            finish();

        }




    }

    private  void gen_single_dialog(String[] arr , MaterialTextView txt ){

        final int[] checkedItem = {-1};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Choose an option");

        alertDialog.setSingleChoiceItems(arr, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                checkedItem[0] = which;
                txt.setText(arr[which]);
                dialogInterface.dismiss();

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog customAlertDialog = alertDialog.create();
        customAlertDialog.show();

    }

    private void updateSeat(String currentSeat , AlertDialog alertDialog){

        try{

            alertDialog.dismiss();

            db=FirebaseFirestore.getInstance();
            mAuth=FirebaseAuth.getInstance();
            String user = mAuth.getCurrentUser().getUid().toString();

            dialog = new Dialog(DashboardActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_wait);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


            LayoutInflater li = getLayoutInflater();
            View layout = li.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.custom_toast_container),false);
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);


            HashMap<String , Object> seatMap = new HashMap<>();

            seatMap.put("currentSeats",currentSeat);
            db.collection(user).document("basicData").update(seatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        toast.show();


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DashboardActivity.this, "Some Error Occured !", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Log.d("seat error", "updateSeat: "+e.getMessage());
        }



    }

    private void update_salon_status(String status){



        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast_status,(ViewGroup) findViewById(R.id.custom_toast_container),false);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        HashMap<String , Object> statusMap = new HashMap<>();

        statusMap.put("salonStatus",status);

        db.collection(user).document("basicData").update(statusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    status_text.setText("Salon "+status);
                    toast.show();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashboardActivity.this, "Error updating salon status", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void check_salon_status(){

        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

        LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.salon_status_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String value = task.getResult().getString("salonStatus").toString();

                    if(value.equals("Closed")){
                        dialog.show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("check error", "onFailure: "+e.getMessage());
            }
        });


    }
    private void switch_control(){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String status = task.getResult().get("salonStatus").toString();

                    if(status.equals("Closed")){
                        status_switch.setChecked(true);
                        status_text.setText("Salon Closed");
                    }
                    else if(status.equals("Open")){
                        status_switch.setChecked(false);
                        status_text.setText("Salon Open");
                    }
                }
            }
        });
    }

    private void data_loading(String category){

        SharedPreferences sh = getSharedPreferences("NaiifiData",MODE_PRIVATE);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        HashMap<String , Object> hashMap = new HashMap<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            String data  = task.getResult().toString();

                        }
                    }
                });

            }
        });
        thread.start();


    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir.list() != null) {
                deleteDir2(dir);
            }
        } catch (Exception e) { e.printStackTrace();}
    }

    public boolean deleteDir2(File dir) {
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                boolean success = deleteDir2(child);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }






  }