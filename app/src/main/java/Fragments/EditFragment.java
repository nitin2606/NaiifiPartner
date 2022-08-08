package Fragments;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import Adapter.EditDataAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EditFragment extends Fragment {

    private FirebaseFirestore db ;
    private FirebaseAuth mAuth;
    private EditDataAdapter editDataAdapter ;
    private RecyclerView edit_recycler_haircut , edit_recycler_hairColor , edit_recycler_shaving , edit_recycler_bleach , edit_recycler_others , edit_recycler_hairSpa,
                         edit_recycler_hairWash , edit_recycler_hairTreatment, edit_recycler_headMassage , edit_recycler_cleanUp , edit_recycler_facial , edit_recycler_dTan , edit_recycler_pedicure ,
                         edit_recycler_manicure , edit_recycler_bodyPolishing ,edit_recycler_manicureSpa , edit_recycler_hairStyling , edit_recycler_threading ,
                         edit_recycler_waxing , edit_recycler_faceMask , edit_recycler_nailArt , edit_recycler_makeUp ,
                         edit_recycler_female_hairStyling , edit_recycler_female_threading , edit_recycler_female_faceMask , edit_recycler_female_nailArt ,
                         edit_recycler_female_makeUp ,edit_recycler_female_waxing;
    
    private MaterialTextView category ,days_closed , time_opening , time_closing , seat_aval ;

    private ProgressBar haircut , hairColor , shaving , bleach , others , hairSpa , hairWash , headMassage , hairTreatment , cleanUp , facial , dTan , pedicure ,
                        manicure , bodyPolishing , manicureSpa , hairStyling , threading , waxing , faceMask , nailArt , makeUp , female_hairStyling , female_threading ,
                        female_waxing , female_faceMask , female_nailArt , female_makeUp ;

    private MaterialTextView text_haircut , text_hairColor  , text_shaving , text_bleach , text_hairSpa , text_others  , text_hairWash , text_headMassage , text_hairTreatment,
                             text_cleanUp , text_facial , text_dTan , text_pedicure , text_manicure , text_bodyPolishing , text_manicureSpa , text_hairStyling ,
                             text_threading , text_waxing , text_faceMask , text_nailArt , text_makeUp ,
                             text_female_hairStyling , text_female_threading , text_female_waxing , text_female_faceMask , text_female_nailArt , text_female_makeUp;


    private MaterialTextView add_haircut , add_hairColor  , add_shaving , add_bleach , add_hairSpa , add_others  , add_hairWash , add_headMassage , add_hairTreatment,
            add_cleanUp , add_facial , add_dTan , add_pedicure , add_manicure , add_bodyPolishing , add_manicureSpa ,

            add_unisex_hairStyling , add_unisex_threading , add_unisex_waxing , add_unisex_faceMask , add_unisex_nailArt , add_unisex_makeUp ,

            add_female_hairStyling , add_female_threading , add_female_waxing , add_female_faceMask , add_female_nailArt , add_female_makeUp;


    private LinearLayout common_services , female_services  , unisex_services , extra_data_layout ;


    String[] items =  {"Unisex Salon (Male and Female Both)", "Male Salon","Female Salon"};
    String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    String[] seats =  {"1","2","3","4","5","6","7","8","9","10","11"};



    private int tHour , tMinute;

    private ImageView btn_refresh;

    private ArrayList<String> extraList ;








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);


        edit_recycler_haircut = view.findViewById(R.id.edit_recycler_haircut);
        edit_recycler_hairColor = view.findViewById(R.id.edit_recycler_hairColor);
        edit_recycler_bleach = view.findViewById(R.id.edit_recycler_bleach);
        edit_recycler_shaving = view.findViewById(R.id.edit_recycler_shaving);
        edit_recycler_hairSpa = view.findViewById(R.id.edit_recycler_hairSpa);
        edit_recycler_others = view.findViewById(R.id.edit_recycler_others);
        edit_recycler_hairWash = view.findViewById(R.id.edit_recycler_hairWash);
        edit_recycler_headMassage = view.findViewById(R.id.edit_recycler_headMassage);
        edit_recycler_hairTreatment = view.findViewById(R.id.edit_recycler_hairTreatment);
        edit_recycler_cleanUp = view.findViewById(R.id.edit_recycler_cleanup);
        edit_recycler_facial = view.findViewById(R.id.edit_recycler_facial);
        edit_recycler_dTan = view.findViewById(R.id.edit_recycler_dTan);
        edit_recycler_pedicure = view.findViewById(R.id.edit_recycler_pedicure);
        edit_recycler_manicure = view.findViewById(R.id.edit_recycler_manicure);
        edit_recycler_bodyPolishing = view.findViewById(R.id.edit_recycler_bodyPolishing);
        edit_recycler_manicureSpa = view.findViewById(R.id.edit_recycler_manicureSpa);
        edit_recycler_hairStyling = view.findViewById(R.id.edit_recycler_hairStyling);
        edit_recycler_threading = view.findViewById(R.id.edit_recycler_threading);
        edit_recycler_waxing = view.findViewById(R.id.edit_recycler_waxing);
        edit_recycler_faceMask = view.findViewById(R.id.edit_recycler_faceMask);
        edit_recycler_nailArt = view.findViewById(R.id.edit_recycler_nailArt);
        edit_recycler_makeUp = view.findViewById(R.id.edit_recycler_makeUp);

        edit_recycler_female_hairStyling = view.findViewById(R.id.edit_recycler_female_hairStyling);
        edit_recycler_female_threading = view.findViewById(R.id.edit_recycler_female_threading);
        edit_recycler_female_waxing = view.findViewById(R.id.edit_recycler_female_waxing);
        edit_recycler_female_faceMask = view.findViewById(R.id.edit_recycler_female_faceMask);
        edit_recycler_female_nailArt = view.findViewById(R.id.edit_recycler_female_nailArt);
        edit_recycler_female_makeUp = view.findViewById(R.id.edit_recycler_female_makeUp);


        haircut = view.findViewById(R.id.haircut);
        hairColor = view.findViewById(R.id.hairColor);
        shaving = view.findViewById(R.id.shaving);
        bleach = view.findViewById(R.id.bleach);
        hairSpa = view.findViewById(R.id.hairSpa);
        others  = view.findViewById(R.id.others);
        hairWash = view.findViewById(R.id.hairWash);
        headMassage = view.findViewById(R.id.headMassage);
        hairTreatment = view.findViewById(R.id.hairTreatment);
        cleanUp = view.findViewById(R.id.cleanUp);
        facial = view.findViewById(R.id.facial);
        dTan = view.findViewById(R.id.dTan);
        pedicure = view.findViewById(R.id.pedicure);
        manicure = view.findViewById(R.id.manicure);
        bodyPolishing = view.findViewById(R.id.bodyPolishing);
        manicureSpa = view.findViewById(R.id.manicureSpa);
        hairStyling = view.findViewById(R.id.hairStyling);
        threading = view.findViewById(R.id.threading);
        waxing = view.findViewById(R.id.waxing);
        faceMask = view.findViewById(R.id.faceMask);
        nailArt = view.findViewById(R.id.nailArt);
        makeUp = view.findViewById(R.id.makeUp);

        female_hairStyling = view.findViewById(R.id.female_hairStyling);
        female_threading = view.findViewById(R.id.female_threading);
        female_waxing = view.findViewById(R.id.female_waxing);
        female_faceMask = view.findViewById(R.id.female_faceMask);
        female_nailArt = view.findViewById(R.id.female_nailArt);
        female_makeUp = view.findViewById(R.id.female_makeUp);


        text_hairColor = view.findViewById(R.id.text_hairColor);
        text_haircut = view.findViewById(R.id.text_haircut);
        text_bleach = view.findViewById(R.id.text_bleach);
        text_hairSpa = view.findViewById(R.id.text_hairSpa);
        text_others = view.findViewById(R.id.text_others);
        text_shaving = view.findViewById(R.id.text_shaving);
        text_hairWash = view.findViewById(R.id.text_hairWash);
        text_headMassage = view.findViewById(R.id.text_headMassage);
        text_hairTreatment = view.findViewById(R.id.text_hairTreatment);
        text_cleanUp = view.findViewById(R.id.text_cleanUp);
        text_facial  = view.findViewById(R.id.text_facial);
        text_dTan = view.findViewById(R.id.text_dTan);
        text_pedicure = view.findViewById(R.id.text_pedicure);
        text_manicure = view.findViewById(R.id.text_manicure);
        text_bodyPolishing = view.findViewById(R.id.text_bodyPolishing);
        text_manicureSpa = view.findViewById(R.id.text_manicureSpa);
        text_hairStyling = view.findViewById(R.id.text_hairStyling);
        text_threading = view.findViewById(R.id.text_threading);
        text_waxing = view.findViewById(R.id.text_waxing);
        text_faceMask = view.findViewById(R.id.text_faceMask);
        text_nailArt = view.findViewById(R.id.text_nailArt);
        text_makeUp = view.findViewById(R.id.text_makeUp);

        text_female_hairStyling = view.findViewById(R.id.text_female_hairStyling);
        text_female_threading = view.findViewById(R.id.text_female_threading);
        text_female_waxing = view.findViewById(R.id.text_female_waxing);
        text_female_faceMask = view.findViewById(R.id.text_female_faceMask);
        text_female_nailArt = view.findViewById(R.id.text_female_nailArt);
        text_female_makeUp = view.findViewById(R.id.text_female_makeUp);



        add_haircut = view.findViewById(R.id.add_haircut);
        add_hairColor= view.findViewById(R.id.add_hairColor);
        add_shaving = view.findViewById(R.id.add_shaving);
        add_bleach = view.findViewById(R.id.add_bleach);
        add_hairSpa = view.findViewById(R.id.add_hairSpa);
        add_others = view.findViewById(R.id.add_others);
        add_hairWash = view.findViewById(R.id.add_hairWash);
        add_headMassage = view.findViewById(R.id.add_headMassage);
        add_hairTreatment= view.findViewById(R.id.add_hairTreatment);
        add_cleanUp = view.findViewById(R.id.add_cleanUp);
        add_facial = view.findViewById(R.id.add_facial);
        add_dTan = view.findViewById(R.id.add_dtan);
        add_pedicure = view.findViewById(R.id.add_pedicure);
        add_manicure = view.findViewById(R.id.add_manicure);
        add_bodyPolishing = view.findViewById(R.id.add_bodyPolishing);
        add_manicureSpa = view.findViewById(R.id.add_manicureSpa);

        add_unisex_hairStyling = view.findViewById(R.id.add_unisex_hairStyling);
        add_unisex_threading= view.findViewById(R.id.add_unisex_threading);
        add_unisex_waxing = view.findViewById(R.id.add_unisex_waxing);
        add_unisex_faceMask= view.findViewById(R.id.add_unisex_faceMask);
        add_unisex_nailArt = view.findViewById(R.id.add_unisex_nailArt);
        add_unisex_makeUp = view.findViewById(R.id.add_unisex_makeUp);


        add_female_hairStyling  = view.findViewById(R.id.add_female_hairStyling);
        add_female_threading = view.findViewById(R.id.add_female_threading);
        add_female_waxing = view.findViewById(R.id.add_female_waxing);
        add_female_faceMask = view.findViewById(R.id.add_female_faceMask);
        add_female_nailArt = view.findViewById(R.id.add_female_nailArt);
        add_female_makeUp= view.findViewById(R.id.add_female_makeUp);



        category = view.findViewById(R.id.category);
        days_closed = view.findViewById(R.id.days_closed);
        time_opening = view.findViewById(R.id.time_opening);
        time_closing = view.findViewById(R.id.time_closing);
        seat_aval = view.findViewById(R.id.seat_aval);

        common_services = view.findViewById(R.id.common_services);
        unisex_services = view.findViewById(R.id.unisex_services);
        female_services = view.findViewById(R.id.female_services);

        extra_data_layout = view.findViewById(R.id.extra_data_layout);


        

        btn_refresh = view.findViewById(R.id.btn_refresh);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncCaller().execute();
            }
        });

        setBasicData();
        create_edit_screen();



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

    private  void create_edit_screen(){


        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();


        try{

            db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String a = task.getResult().get("salonCategory").toString().trim();


                        if(a.equals("Unisex Salon (Male and Female Both)")){

                            unisex_services.setVisibility(View.VISIBLE);

                            service_category("hairCut",edit_recycler_haircut , haircut , text_haircut);
                            service_category("hairColor",edit_recycler_hairColor , hairColor , text_hairColor);
                            service_category("bleach" , edit_recycler_bleach , bleach , text_bleach);
                            service_category("shaving",edit_recycler_shaving , shaving, text_shaving);
                            service_category("hairSpa" , edit_recycler_hairSpa , hairSpa , text_hairSpa);
                            service_category("others", edit_recycler_others , others , text_others);
                            service_category("hairWash" , edit_recycler_hairWash , hairWash ,text_hairWash);
                            service_category("headMassage",edit_recycler_headMassage,headMassage ,text_headMassage);
                            service_category("hairTreatment",edit_recycler_hairTreatment,hairTreatment,text_hairTreatment);
                            service_category("cleanUp",edit_recycler_cleanUp,cleanUp,text_cleanUp);
                            service_category("facial",edit_recycler_facial,facial,text_facial);
                            service_category("dTan",edit_recycler_dTan,dTan,text_dTan);
                            service_category("pedicure",edit_recycler_pedicure,pedicure,text_pedicure);
                            service_category("manicure",edit_recycler_manicure,manicure,text_manicure);
                            service_category("bodyPolishing",edit_recycler_bodyPolishing,bodyPolishing,text_bodyPolishing);
                            service_category("manicureSpa",edit_recycler_manicureSpa,manicureSpa,text_manicureSpa);
                            service_category("hairStyling",edit_recycler_hairStyling,hairStyling,text_hairStyling);
                            service_category("threading",edit_recycler_threading,threading,text_threading);
                            service_category("waxing",edit_recycler_waxing,waxing,text_waxing);
                            service_category("faceMask",edit_recycler_faceMask,faceMask,text_faceMask);
                            service_category("nailArt",edit_recycler_nailArt,nailArt,text_nailArt);
                            service_category("makeUp",edit_recycler_makeUp,makeUp,text_makeUp);


                        }
                        else if(a.equals("Male Salon")){

                            service_category("hairCut",edit_recycler_haircut , haircut , text_haircut);
                            service_category("hairColor",edit_recycler_hairColor , hairColor , text_hairColor);
                            service_category("bleach" , edit_recycler_bleach , bleach , text_bleach);
                            service_category("shaving",edit_recycler_shaving , shaving, text_shaving);
                            service_category("hairSpa" , edit_recycler_hairSpa , hairSpa , text_hairSpa);
                            service_category("others", edit_recycler_others , others , text_others);
                            service_category("hairWash" , edit_recycler_hairWash , hairWash ,text_hairWash);
                            service_category("headMassage",edit_recycler_headMassage,headMassage ,text_headMassage);
                            service_category("hairTreatment",edit_recycler_hairTreatment,hairTreatment,text_hairTreatment);
                            service_category("cleanUp",edit_recycler_cleanUp,cleanUp,text_cleanUp);
                            service_category("facial",edit_recycler_facial,facial,text_facial);
                            service_category("dTan",edit_recycler_dTan,dTan,text_dTan);
                            service_category("pedicure",edit_recycler_pedicure,pedicure,text_pedicure);
                            service_category("manicure",edit_recycler_manicure,manicure,text_manicure);
                            service_category("bodyPolishing",edit_recycler_bodyPolishing,bodyPolishing,text_bodyPolishing);
                            service_category("manicureSpa",edit_recycler_manicureSpa,manicureSpa,text_manicureSpa);

                        }
                        else if(a.equals("Female Salon")){


                            female_services.setVisibility(View.VISIBLE);


                            service_category("hairCut",edit_recycler_haircut , haircut , text_haircut);
                            service_category("hairColor",edit_recycler_hairColor , hairColor , text_hairColor);
                            service_category("bleach" , edit_recycler_bleach , bleach , text_bleach);
                            service_category("shaving",edit_recycler_shaving , shaving, text_shaving);
                            service_category("hairSpa" , edit_recycler_hairSpa , hairSpa , text_hairSpa);
                            service_category("others", edit_recycler_others , others , text_others);
                            service_category("hairWash" , edit_recycler_hairWash , hairWash ,text_hairWash);
                            service_category("headMassage",edit_recycler_headMassage,headMassage ,text_headMassage);
                            service_category("hairTreatment",edit_recycler_hairTreatment,hairTreatment,text_hairTreatment);
                            service_category("cleanUp",edit_recycler_cleanUp,cleanUp,text_cleanUp);
                            service_category("facial",edit_recycler_facial,facial,text_facial);
                            service_category("dTan",edit_recycler_dTan,dTan,text_dTan);
                            service_category("pedicure",edit_recycler_pedicure,pedicure,text_pedicure);
                            service_category("manicure",edit_recycler_manicure,manicure,text_manicure);
                            service_category("bodyPolishing",edit_recycler_bodyPolishing,bodyPolishing,text_bodyPolishing);
                            service_category("manicureSpa",edit_recycler_manicureSpa,manicureSpa,text_manicureSpa);

                            service_category("hairStyling",edit_recycler_female_hairStyling,female_hairStyling,text_female_hairStyling);
                            service_category("threading",edit_recycler_female_threading,female_threading,text_female_threading);
                            service_category("waxing",edit_recycler_female_waxing,female_waxing,text_female_waxing);
                            service_category("faceMask",edit_recycler_female_faceMask,female_faceMask,text_female_faceMask);
                            service_category("nailArt",edit_recycler_female_nailArt,female_nailArt,text_female_nailArt);
                            service_category("makeUp",edit_recycler_female_makeUp,female_makeUp,text_female_makeUp);





                        }

                    }
                }
            });
        }catch (Exception e){
            Log.d("Data Fetch error", "create_edit_screen: "+e.getMessage());
        }


    }

    private void service_category(String category , RecyclerView recyclerView  , ProgressBar progressBar , MaterialTextView materialTextView){

        ArrayList<String> serv_category=new ArrayList<>();

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

        db.collection(user).document("serviceData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{

                        String data = task.getResult().get(category).toString();

                        String[] arr = data.split(";");


                        for(int i=0 ; i< arr.length ; i++){
                            serv_category.add(arr[i].trim());
                        }
                        create_edt_recyclr(recyclerView , serv_category , progressBar , materialTextView);

                    }
                    catch(Exception e){
                        create_edt_recyclr(recyclerView , serv_category , progressBar , materialTextView);


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

    private void create_edt_recyclr(RecyclerView recyclerView , ArrayList<String> arrayList , ProgressBar progressBar , MaterialTextView materialTextView){

        progressBar.setVisibility(View.VISIBLE);
        String t = recyclerView.getTag().toString().trim();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        editDataAdapter = new EditDataAdapter(arrayList , getContext() , t);
        recyclerView.setAdapter(editDataAdapter);


        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                progressBar.setVisibility(View.GONE);



                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

        if (arrayList.isEmpty()){
            progressBar.setVisibility(View.GONE);
            materialTextView.setVisibility(View.VISIBLE);

        }
        else{
            materialTextView.setVisibility(View.GONE);
        }


    }

    private void setBasicData(){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

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
        String user = mAuth.getCurrentUser().getUid();


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

            service_category("haircut",edit_recycler_haircut , haircut , text_haircut);
            service_category("hairColor",edit_recycler_hairColor , hairColor , text_hairColor);
            service_category("bleach" , edit_recycler_bleach , bleach , text_bleach);
            service_category("shaving",edit_recycler_shaving , shaving , text_shaving);
            service_category("hairSpa" , edit_recycler_hairSpa , hairSpa , text_hairSpa);
            service_category("others", edit_recycler_others , others , text_others);


        }

    }






    private void generate_dynamic_edittext(ArrayList<String>  arrayList, LinearLayout linearLayout , AlertDialog dialog){

        EditText editText ;

        for(int k=0 ; k< arrayList.size() ;k++){

                    editText = new EditText(getLayoutInflater().getContext());
                    editText.setId(k);
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupees,0,0,0);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setTag(arrayList.get(k));
                    editText.setHint(arrayList.get(k));
                    editText.setHint(arrayList.get(k));
                    linearLayout.addView(editText);
                    
        }

    }


    private HashMap<String, String> retrieve_dynamic_editText_data(LinearLayout linearLayout){


        HashMap<String , String> map =  new HashMap<>();
        map.clear();

        for(int i =0 ; i<linearLayout.getChildCount() ; i++){

            String tag = linearLayout.getChildAt(i).getTag().toString();
            String price = ((EditText) linearLayout.getChildAt(i)).getText().toString();

            map.put(tag.trim() , price.trim());
        }
        return map ;
    }




}




