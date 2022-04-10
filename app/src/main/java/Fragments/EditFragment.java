package Fragments;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;



import Adapter.EditDataAdapter;


public class EditFragment extends Fragment {

    private FirebaseFirestore db ;
    private FirebaseAuth mAuth;

    private EditDataAdapter editDataAdapter ;
    private RecyclerView edit_recycler_haircut , edit_recycler_haircolor , edit_recycler_shaving , edit_recycler_bleach , edit_recycler_others , edit_recycler_hairspa;

    private MaterialTextView category ,days_closed , time_opening , time_closing , seat_aval ;
    private ProgressBar haircut , hairColor , shaving , bleach , others , hairSpa ;


    String[] items =  {"Unisex Salon (Male and Female Both)", "Male Salon","Female Salon"};
    String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","None"};
    String[] seats =  {"1","2","3","4","5","6","7","8","9","10","11"};

    private int tHour , tMinute;

    private ImageView btn_refresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);


        edit_recycler_haircut = view.findViewById(R.id.edit_recycler_haircut);
        edit_recycler_haircolor = view.findViewById(R.id.edit_recycler_haircolor);
        edit_recycler_bleach = view.findViewById(R.id.edit_recycler_bleach);
        edit_recycler_shaving = view.findViewById(R.id.edit_recycler_shaving);
        edit_recycler_hairspa = view.findViewById(R.id.edit_recycler_hairspa);
        edit_recycler_others = view.findViewById(R.id.edit_recycler_others);

        haircut = view.findViewById(R.id.haircut);
        hairColor = view.findViewById(R.id.hairColor);
        shaving = view.findViewById(R.id.shaving);
        bleach = view.findViewById(R.id.bleach);
        hairSpa = view.findViewById(R.id.hairSpa);
        others  = view.findViewById(R.id.others);

        category = view.findViewById(R.id.category);
        days_closed = view.findViewById(R.id.days_closed);
        time_opening = view.findViewById(R.id.time_opening);
        time_closing = view.findViewById(R.id.time_closing);
        seat_aval = view.findViewById(R.id.seat_aval);

        btn_refresh = view.findViewById(R.id.btn_refresh);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncCaller().execute();
            }
        });

        setBasicData();

        service_category("haircut",edit_recycler_haircut , haircut);
        service_category("hairColor",edit_recycler_haircolor , hairColor);
        service_category("bleach" , edit_recycler_bleach , bleach);
        service_category("shaving",edit_recycler_shaving , shaving);
        service_category("hairSpa" , edit_recycler_hairspa , hairSpa);
        service_category("others", edit_recycler_others , others);

        category.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                generate_dialog(items,category,"salonCategory");
                return true;
            }
        });

        days_closed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                generate_dialog(days , days_closed ,"closedDays");
                return true;
            }
        });

        time_opening.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gen_time_dialog(time_opening ,"openingTime");
                return true;
            }
        });

        time_closing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gen_time_dialog(time_closing,"closingTime");
                return true;
            }
        });

        seat_aval.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gen_single_dialog(seats ,seat_aval , "availableSeats");
                return true;
            }
        });


        return view;

    }

    private void service_category(String category , RecyclerView recyclerView  , ProgressBar progressBar){

        ArrayList<String> serv_category=new ArrayList<>();

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        db.collection(user).document("serviceCategory").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        String data = task.getResult().get(category).toString();
                        data = data.substring(1, data.length() - 1);

                        String[] arr = data.split(",");


                        for(int i=0 ; i< arr.length ; i++){
                            serv_category.add(arr[i].trim());
                        }
                        create_edt_recyclr(recyclerView , serv_category , progressBar);

                    }
                    catch(Exception e){

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("recycler err", "onFailure: "+e.getMessage());
            }
        });


    }

    private void create_edt_recyclr(RecyclerView recyclerView , ArrayList<String> arrayList , ProgressBar progressBar){

        progressBar.setVisibility(View.VISIBLE);
        String t = recyclerView.getTag().toString().trim();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        editDataAdapter = new EditDataAdapter(arrayList , getContext() , t);
        recyclerView.setAdapter(editDataAdapter);

        int count = recyclerView.getAdapter().getItemCount();

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                progressBar.setVisibility(View.GONE);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



    }

    private void setBasicData(){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        try{

            db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String categ = task.getResult().get("salonCategory").toString();
                        String closedDays = task.getResult().get("closedDays").toString();
                        String opening = task.getResult().get("openingTime").toString();
                        String closing  = task.getResult().get("closingTime").toString();
                        String seatAval = task.getResult().get("availableSeats").toString();

                        category.setText(categ);
                        days_closed.setText(closedDays);
                        time_opening.setText(opening);
                        time_closing.setText(closing);
                        seat_aval.setText(seatAval);

                    }

                }
            });

        }
        catch (Exception e){

        }

    }

    private void updateBasicData(MaterialTextView materialTextView , String category){

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();


        String val = materialTextView.getText().toString();
        HashMap<String , Object> map = new HashMap<>();
        map.put(category,val);

        db.collection(user).document("basicData").update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Data updated successfully .", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void generate_dialog(String[] arr , MaterialTextView txt , String category){

        boolean[] selected1 ;
        selected1=new boolean[arr.length];
        ArrayList<Integer> titlelst1 = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select options whichever applicable :");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(arr, selected1, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if(b){
                    titlelst1.add(i);
                    Collections.sort(titlelst1);
                }
                else{
                    titlelst1.remove(Integer.valueOf(i));
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < titlelst1.size(); j++) {
                    stringBuilder.append(arr[titlelst1.get(j)]);

                    if (j != titlelst1.size() - 1) {

                        stringBuilder.append(", ");
                    }
                }
                txt.setText(stringBuilder.toString());
                updateBasicData(txt,category);
            }

        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selected1.length; j++) {
                    // remove all selection
                    selected1[j] = false;
                    // clear language list
                    titlelst1.clear();
                    // clear text view value
                    txt.setText("");
                }
            }
        });
        builder.show();

    }

    private void gen_time_dialog(MaterialTextView materialTextView , String category){


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                tHour = hourOfDay ;
                tMinute = minute;

                Calendar calendar = Calendar.getInstance();

                calendar.set(0,0,0,tHour,tMinute);
                materialTextView.setText(DateFormat.format("hh:mm aa",calendar));

                updateBasicData(materialTextView , category);


            }
        },12,0,false);

        timePickerDialog.updateTime(tHour,tMinute);
        timePickerDialog.show();
    }

    private  void gen_single_dialog(String[] arr , MaterialTextView txt , String category ){

        final int[] checkedItem = {-1};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Choose an option");

        alertDialog.setSingleChoiceItems(arr, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                checkedItem[0] = which;
                txt.setText(arr[which]);
                dialogInterface.dismiss();
                updateBasicData(txt ,category);

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

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            Animation anti_clock_rot = AnimationUtils.loadAnimation(getContext(), R.anim.anti_clock_rot);
            btn_refresh.startAnimation(anti_clock_rot);

        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI from here
            //do your long running http tasks here,you don't want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            service_category("haircut",edit_recycler_haircut , haircut);
            service_category("hairColor",edit_recycler_haircolor , hairColor);
            service_category("bleach" , edit_recycler_bleach , bleach);
            service_category("shaving",edit_recycler_shaving , shaving);
            service_category("hairSpa" , edit_recycler_hairspa , hairSpa);
            service_category("others", edit_recycler_others , others);


        }

    }
}




