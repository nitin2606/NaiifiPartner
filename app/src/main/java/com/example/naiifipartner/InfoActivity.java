package com.example.naiifipartner;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Adapter.GridViewAdapter;


public class InfoActivity extends AppCompatActivity {

    private static final int PICK_IMG = 1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private int uploads = 0;

    private ArrayList<String> arr ;
    private static final int ADDRESS_PICKER_REQUEST = 1020;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    private MaterialTextView category_txt , days_closed , upload_image_txt ,

            category_shaving , category_manicurespa , category_hair_styling , category_threading , category_waxing , category_facemask , category_nailart , category_makeup;

    private MaterialTextView seat_aval , price_instruction ,  txtLat , txtLong ,txtCity , txtState , time_opening , time_closing;




    private GridView grid_view_hairCut , grid_view_shaving , grid_view_hairSpa , grid_view_hairColor ,grid_view_bleach , grid_view_other , grid_view_hairwash , grid_view_headmassage,
            grid_view_hairtreatment , grid_view_cleanup , grid_view_facial , grid_view_dtan , grid_view_pedicure , grid_view_manicure , grid_view_body_polishing ,
            grid_view_manicurespa , grid_view_hair_styling , grid_view_threading , grid_view_waxing , grid_view_facemask , grid_view_nailart  , grid_view_makeup;

    private LinearLayout linear_layout , linear_dynamic , linear_grid_data , linear_save_clear_bt ,lin_time;
    private ScrollView scr_view;

    private FloatingActionButton floating_bt_clear  , floating_bt_save ;
    private EditText editText , txtAddress ,txtPostal ;
    private TextView grid_txt , information ;


    private TextInputLayout text_layout1;

    private ExtendedFloatingActionButton btnUpload , submit  ,location_bt , btnChoose , finalUpload;



    private ArrayList<String> selectedStrings;
    private ArrayList<String> arraylist1;
    private ArrayList<String> arrayList2;

    //private ArrayList<String> services ;

    private HashMap<String , Integer> idsMap  ;
    //private ArrayList<String> total_services = new ArrayList<>();




    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    public static final int RESULT_IMAGE = 10;

    private DatabaseReference mRootRef;
    private FirebaseFirestore db;


    private int tHour , tMinute ;
    private Dialog dialog;

    private HashMap<String , Object> grid_data_map ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_info);

        //Window window = this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.setStatusBarColor(this.getResources().getColor(R.color.custom_status));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);


        dialog = new Dialog(InfoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);

        db = FirebaseFirestore.getInstance();
        category_txt = findViewById(R.id.category);
        days_closed =  findViewById(R.id.days_closed);
        seat_aval = findViewById(R.id.seat_aval);
        time_opening =findViewById(R.id.time_opening);
        time_closing= findViewById(R.id.time_closing);
        upload_image_txt = findViewById(R.id.upload_image_txt);
        price_instruction = findViewById(R.id.price_instruction);
        text_layout1 = findViewById(R.id.text_layout1);

        linear_grid_data = findViewById(R.id.linear_grid_data);
        linear_save_clear_bt = findViewById(R.id.linear_save_clear_bt);
        lin_time = findViewById(R.id.lin_time);

        //category_haircut = findViewById(R.id.category_haircut);
        //category_HairColor = findViewById(R.id.category_hairColor);
        //category_bleach = findViewById(R.id.category_bleach);
       // category_other  = findViewById(R.id.category_other);

        //category_HairSpa  =findViewById(R.id.category_hairSpa);
        category_shaving = findViewById(R.id.category_shaving);
        category_manicurespa = findViewById(R.id.category_manicurespa);
        category_hair_styling = findViewById(R.id.category_hair_styling);
        category_threading = findViewById(R.id.category_threading);
        category_waxing  = findViewById(R.id.category_waxing);
        category_facemask = findViewById(R.id.category_facemask);
        category_nailart = findViewById(R.id.category_nailart);
        category_makeup = findViewById(R.id.category_makeup);


        txtAddress = findViewById(R.id.txtAddress);
        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        txtPostal = findViewById(R.id.txtPostal);
        txtCity = findViewById(R.id.txtCity);
        txtState = findViewById(R.id.txtState);


        grid_view_hairCut = findViewById(R.id.grid_view_haircut);
        grid_view_bleach = findViewById(R.id.grid_view_bleach);
        grid_view_hairColor = findViewById(R.id.grid_view_hairColor);
        grid_view_hairSpa = findViewById(R.id.grid_view_hairSpa);
        grid_view_shaving = findViewById(R.id.grid_view_shaving);  /*male*/
        grid_view_other = findViewById(R.id.grid_view_other);
        grid_view_hairwash = findViewById(R.id.grid_view_hairwash);
        grid_view_headmassage = findViewById(R.id.grid_view_headmassage);
        grid_view_hairtreatment = findViewById(R.id.grid_view_hairtreatment);
        grid_view_cleanup = findViewById(R.id.grid_view_cleanup);
        grid_view_facial = findViewById(R.id.grid_view_facial);
        grid_view_dtan = findViewById(R.id.grid_view_dtan);
        grid_view_pedicure = findViewById(R.id.grid_view_pedicure);
        grid_view_manicure = findViewById(R.id.grid_view_manicure);
        grid_view_body_polishing = findViewById(R.id.grid_view_body_polishing);
        grid_view_manicurespa = findViewById(R.id.grid_view_manicurespa);   /* male */
        grid_view_hair_styling = findViewById(R.id.grid_view_hair_styling);  /* female */
        grid_view_threading = findViewById(R.id.grid_view_threading);  /* female */
        grid_view_waxing = findViewById(R.id.grid_view_waxing);        /* female */
        grid_view_facemask = findViewById(R.id.grid_view_facemask);    /* female */
        grid_view_nailart = findViewById(R.id.grid_view_nailart);      /* female */
        grid_view_makeup = findViewById(R.id.grid_view_makeup);        /* female */


        linear_layout = findViewById(R.id.linear_layout);
        linear_dynamic= findViewById(R.id.linear_dynamic);
        floating_bt_save = findViewById(R.id.floating_bt_save);
        floating_bt_clear = findViewById(R.id.floating_bt_clear);
        location_bt = findViewById(R.id.location_bt);
        finalUpload = findViewById(R.id.finalUpload);


        grid_txt = findViewById(R.id.item_txt);
        scr_view = findViewById(R.id.scr_view);
        scr_view.setSmoothScrollingEnabled(true);
        information =findViewById(R.id.information);
        submit = findViewById(R.id.hash);
        btnChoose=findViewById(R.id.btnChoose);
        btnUpload=findViewById(R.id.btnUpload);



        mAuth=FirebaseAuth.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        finalUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadLocation();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMG);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages();

            }
        });

        location_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);

            }
        });





        String[] items =  {"Unisex Salon (Male and Female Both)", "Male Salon","Female Salon"};

        String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","None"};

        String[] seats =  {"1","2","3","4","5","6","7","8","9","10","11"};

        //String[] services = {"Java","Python","Ruby","HTML","C++","React","JSON"};


        time_opening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gen_time_dialog(time_opening);
            }
        });
        time_closing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gen_time_dialog(time_closing);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(category_txt.getText().toString().isEmpty() || days_closed.getText().toString().isEmpty() || time_opening.getText().toString().isEmpty() ||

                time_closing.getText().toString().isEmpty() || seat_aval.getText().toString().isEmpty()){

                    Toast.makeText(InfoActivity.this, "Fill all fields first !", Toast.LENGTH_SHORT).show();
                }

                else if(retrieve_edt_data(linear_dynamic).isEmpty()){

                    Toast.makeText(InfoActivity.this, "Price of services empty !", Toast.LENGTH_SHORT).show();
                }

                else{
                    uploadBasicData();

                }
                //uploadBasicData();


            }
        });



        floating_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(selectedStrings.isEmpty()){
                        linear_dynamic.removeAllViews();
                        Toast.makeText(InfoActivity.this, "No Service Selected !", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        linear_dynamic.removeAllViews();
                        dynamic_edt(selectedStrings , linear_dynamic);
                        price_instruction.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        scr_view.smoothScrollBy(0,80000);
                        //System.out.println(grid_data_map);
                        System.out.println(grid_data_map);


                    }

                }
                catch (Exception e){
                    Log.d("save bt error", "Exception: "+e.getMessage());
                }

            }
        });

        floating_bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dynamic_del(linear_dynamic);
                    price_instruction.setVisibility(View.GONE);

                }
                catch (Exception e){
                    Log.d("Clear bt exception", "onClick: "+e.getMessage());
                }

            }
        });



        seat_aval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gen_single_dialog(seats , seat_aval);
            }
        });

        category_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gen_single_dialog(items , category_txt);

            }
        });

        days_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generate_dialog(days , days_closed);
            }
        });

        category_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().equals("Unisex Salon (Male and Female Both)")){

                    data_grid(grid_view_hairCut , "unisexHairCut");
                    data_grid(grid_view_shaving , "unisexShaving");
                    data_grid(grid_view_hairSpa , "unisexHairSpa");
                    data_grid(grid_view_hairColor , "unisexHairColor");
                    data_grid(grid_view_bleach , "unisexBleach");

                    data_grid(grid_view_hairwash , "unisexHairWash");
                    data_grid(grid_view_headmassage , "unisexHeadMassage");
                    data_grid(grid_view_hairtreatment , "unisexHairTreatment");
                    data_grid(grid_view_cleanup , "unisexCleanUp");
                    data_grid(grid_view_facial , "unisexFacial");
                    data_grid(grid_view_dtan , "unisexDTan");
                    data_grid(grid_view_pedicure , "unisexPedicure");
                    data_grid(grid_view_manicure , "unisexManicure");
                    data_grid(grid_view_body_polishing , "unisexBodyPolishing");
                    data_grid(grid_view_manicurespa , "unisexManicureSpa");
                    data_grid(grid_view_hair_styling , "unisexHairStyling");
                    data_grid(grid_view_threading , "unisexThreading");
                    data_grid(grid_view_waxing , "unisexWaxing");
                    data_grid(grid_view_facemask , "unisexFaceMask");
                    data_grid(grid_view_nailart , "unisexNailArt");
                    data_grid(grid_view_makeup , "unisexMakeUp");

                    data_grid(grid_view_other , "unisexOthers");

                    linear_grid_data.setVisibility(View.VISIBLE);

                    category_shaving.setVisibility(View.VISIBLE);
                    grid_view_shaving.setVisibility(View.VISIBLE);

                    category_manicurespa.setVisibility(View.VISIBLE);
                    grid_view_manicurespa.setVisibility(View.VISIBLE);

                    category_hair_styling.setVisibility(View.VISIBLE);
                    grid_view_hair_styling.setVisibility(View.VISIBLE);

                    category_threading.setVisibility(View.VISIBLE);
                    grid_view_threading.setVisibility(View.VISIBLE);

                    category_waxing.setVisibility(View.VISIBLE);
                    grid_view_waxing.setVisibility(View.VISIBLE);

                    category_facemask.setVisibility(View.VISIBLE);
                    grid_view_facemask.setVisibility(View.VISIBLE);

                    category_nailart.setVisibility(View.VISIBLE);
                    grid_view_nailart.setVisibility(View.VISIBLE);

                    category_makeup.setVisibility(View.VISIBLE);
                    grid_view_makeup.setVisibility(View.VISIBLE);


                    linear_save_clear_bt.setVisibility(View.VISIBLE);

                }
                else if(editable.toString().equals("Male Salon")){

                    data_grid(grid_view_hairCut , "maleHairCut");
                    data_grid(grid_view_shaving , "maleShaving");
                    data_grid(grid_view_hairSpa , "maleHairSpa");
                    data_grid(grid_view_hairColor , "maleHairColor");
                    data_grid(grid_view_bleach , "maleBleach");

                    data_grid(grid_view_hairwash , "maleHairWash");
                    data_grid(grid_view_headmassage , "maleHeadMassage");
                    data_grid(grid_view_hairtreatment , "maleHairTreatment");
                    data_grid(grid_view_cleanup , "maleCleanUp");
                    data_grid(grid_view_facial , "maleFacial");
                    data_grid(grid_view_dtan , "maleDTan");
                    data_grid(grid_view_pedicure , "malePedicure");
                    data_grid(grid_view_manicure , "maleManicure");
                    data_grid(grid_view_body_polishing , "maleBodyPolishing");
                    data_grid(grid_view_manicurespa , "maleManicureSpa");
                    data_grid(grid_view_other , "maleOthers");

                    linear_grid_data.setVisibility(View.VISIBLE);
                    category_shaving.setVisibility(View.VISIBLE);
                    grid_view_shaving.setVisibility(View.VISIBLE);

                    category_makeup.setVisibility(View.GONE);
                    grid_view_makeup.setVisibility(View.GONE);

                    category_hair_styling.setVisibility(View.GONE);
                    grid_view_hair_styling.setVisibility(View.GONE);

                    category_threading.setVisibility(View.GONE);
                    grid_view_threading.setVisibility(View.GONE);

                    category_waxing.setVisibility(View.GONE);
                    grid_view_waxing.setVisibility(View.GONE);

                    category_facemask.setVisibility(View.GONE);
                    grid_view_facemask.setVisibility(View.GONE);

                    category_nailart.setVisibility(View.GONE);
                    grid_view_nailart.setVisibility(View.GONE);
                    linear_save_clear_bt.setVisibility(View.VISIBLE);

                }

                else if(editable.toString().equals("Female Salon")){

                    data_grid(grid_view_hairCut , "femaleHairCut");
                    data_grid(grid_view_hairSpa , "femaleHairSpa");
                    data_grid(grid_view_hairColor , "femaleHairColor");
                    data_grid(grid_view_bleach , "femaleBleach");

                    data_grid(grid_view_hairwash , "femaleHairWash");
                    data_grid(grid_view_headmassage , "femaleHeadMassage");
                    data_grid(grid_view_hairtreatment , "femaleHairTreatment");
                    data_grid(grid_view_cleanup , "femaleCleanUp");
                    data_grid(grid_view_facial , "femaleFacial");
                    data_grid(grid_view_dtan , "femaleDTan");
                    data_grid(grid_view_pedicure , "femalePedicure");
                    data_grid(grid_view_manicure , "femaleManicure");
                    data_grid(grid_view_body_polishing , "femaleBodyPolishing");
                    data_grid(grid_view_hair_styling , "femaleHairStyling");
                    data_grid(grid_view_threading , "femaleThreading");
                    data_grid(grid_view_waxing , "femaleWaxing");
                    data_grid(grid_view_facemask , "femaleFaceMask");
                    data_grid(grid_view_nailart , "femaleNailArt");
                    data_grid(grid_view_makeup , "femaleMakeUp");

                    data_grid(grid_view_other , "femaleOthers");

                    linear_grid_data.setVisibility(View.VISIBLE);

                    category_hair_styling.setVisibility(View.VISIBLE);
                    grid_view_hair_styling.setVisibility(View.VISIBLE);

                    category_threading.setVisibility(View.VISIBLE);
                    grid_view_threading.setVisibility(View.VISIBLE);

                    category_waxing.setVisibility(View.VISIBLE);
                    grid_view_waxing.setVisibility(View.VISIBLE);

                    category_facemask.setVisibility(View.VISIBLE);
                    grid_view_facemask.setVisibility(View.VISIBLE);

                    category_nailart.setVisibility(View.VISIBLE);
                    grid_view_nailart.setVisibility(View.VISIBLE);

                    category_makeup.setVisibility(View.VISIBLE);
                    grid_view_makeup.setVisibility(View.VISIBLE);

                    category_shaving.setVisibility(View.GONE);
                    grid_view_shaving.setVisibility(View.GONE);

                    category_manicurespa.setVisibility(View.GONE);
                    grid_view_manicurespa.setVisibility(View.GONE);

                    linear_save_clear_bt.setVisibility(View.VISIBLE);

                }



            }
        });

    }

    private void generate_dialog(String[] arr , MaterialTextView txt){

        boolean[] selected1 ;
        selected1=new boolean[arr.length];
        ArrayList<Integer> titlelst1 = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
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

    private void gen_time_dialog(MaterialTextView materialTextView){


        TimePickerDialog timePickerDialog = new TimePickerDialog(InfoActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                tHour = hourOfDay ;
                tMinute = minute;

                Calendar calendar = Calendar.getInstance();

                calendar.set(0,0,0,tHour,tMinute);
                materialTextView.setText(DateFormat.format("hh:mm aa",calendar));


            }
        },12,0,false);

        timePickerDialog.updateTime(tHour,tMinute);
        timePickerDialog.show();
    }


    private  void gen_single_dialog(String[] arr , MaterialTextView txt ){

        final int[] checkedItem = {-1};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoActivity.this);
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

    private  void create_grid(String[] arr , GridView gridView){

        grid_data_map = new HashMap<>();
        selectedStrings = new ArrayList<>();
        ArrayList<String> services = new ArrayList<>();
        //services = new ArrayList<>();

        final GridViewAdapter adapter = new GridViewAdapter(arr, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {

                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                    services.remove((String) parent.getItemAtPosition(position));
                    String t = gridView.getTag().toString();
                    String serv = services.toString();
                    grid_data_map.put(t,serv);


                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));

                    services.add((String) parent.getItemAtPosition(position));
                    String t = gridView.getTag().toString();
                    String serv = services.toString();
                    StringBuffer stringBuffer = new StringBuffer();
                    for(int i = 0 ; i<services.size() ; i++){


                        if(i==services.size()-1){
                            stringBuffer.append(services.get(i));
                        }
                        else {
                            stringBuffer.append(services.get(i)+";");
                        }
                    }
                    grid_data_map.put(t,stringBuffer);





                }
                /*String t = gridView.getTag().toString();
                String serv = services.toString();
                grid_data_map.put(t,serv);*/

            }

        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                grid_desc(gridView , arr[i].trim());
                return true;
            }
        });

    }



    private void dynamic_edt(ArrayList<String> arr , LinearLayout linearLayout){

        idsMap = new HashMap<>();
        int s = arr.size();

        for(int i=0 ; i<s ;i++){
            editText = new EditText(this);
            String tname = "task"+Integer.toString(i);
            editText.setId(i);
            idsMap.put(tname ,i);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupees,0,0,0);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setHint(arr.get(i));
            editText.setTag(arr.get(i));
            linearLayout.addView(editText);
        }

    }



    private void dynamic_del(LinearLayout linearLayout ){

        linearLayout.removeAllViews();

    }

    private HashMap<String , String> retrieve_edt_data(LinearLayout linearLayout){


        HashMap<String , String> dataMap = new HashMap<>();
        EditText editText = new EditText(this);

        for(int i=0 ; i<linearLayout.getChildCount();i++){
            editText = (EditText) linearLayout.getChildAt(i);
            String tag = linearLayout.getChildAt(i).getTag().toString();
            String data  = editText.getText().toString();
            dataMap.put(tag.trim(),data.trim());

        }

        return  dataMap;

    }

    @SuppressLint("NewApi")


    private void uploadBasicData(){


        dialog.show();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        mRootRef = FirebaseDatabase.getInstance().getReference(user);


        String salonCategory = category_txt.getText().toString();
        String closedDays = days_closed.getText().toString();
        String openingTime = time_opening.getText().toString();
        String closingTime = time_closing.getText().toString();
        String availableSeats = seat_aval.getText().toString();

        HashMap<String , String> basicData = new HashMap<>();

        basicData.put("salonCategory" ,salonCategory);
        basicData.put("closedDays",closedDays);
        basicData.put("openingTime",openingTime);
        basicData.put("closingTime",closingTime);
        basicData.put("availableSeats",availableSeats);
        basicData.put("salonStatus","open");
        basicData.put("currentSeats",availableSeats);


        try{

            db.collection(user).document("basicData").set(basicData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Log.d("myTag", "BASIC DATA uploaded Successfully");

                        for(Map.Entry<String , Object> entry : grid_data_map.entrySet()){
                            String key = entry.getKey();
                            String value = entry.getValue().toString().trim();
                            String data = value;
                            grid_data_map.replace(key , data);
                        }

                        db.collection(user).document("serviceData").set(grid_data_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("myTag","Service DataUploaded Successfully");
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ErrorTag", "Service Data Not Upoaded");
                            }
                        });


                        db.collection(user).document("priceData").set(retrieve_edt_data(linear_dynamic)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("myTag","Price Data Uploaded Successfully .");
                                    dialog.dismiss();
                                    text_layout1.setVisibility(View.GONE);
                                    category_txt.setVisibility(View.GONE);
                                    days_closed.setVisibility(View.GONE);
                                    lin_time.setVisibility(View.GONE);
                                    seat_aval.setVisibility(View.GONE);
                                    linear_grid_data.setVisibility(View.GONE);
                                    linear_save_clear_bt.setVisibility(View.GONE);
                                    price_instruction.setVisibility(View.GONE);
                                    linear_dynamic.setVisibility(View.GONE);
                                    submit.setVisibility(View.GONE);

                                    upload_image_txt.setVisibility(View.VISIBLE);
                                    btnChoose.setVisibility(View.VISIBLE);
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ErrorTag","Price Data Not Uploaded");
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InfoActivity.this, "Some error occurred !", Toast.LENGTH_SHORT).show();
                }
            });


        }
        catch (Exception e){
            Log.d("myTag", "Basic Data Error "+e.getMessage());
        }


    }



    private void data_grid(GridView grid_view , String category){

        db = FirebaseFirestore.getInstance();
        arraylist1 = new ArrayList<>();


        try{

            db.collection("Services").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            arraylist1.add(document.getData().get(category).toString());

                        }

                        String[] title = arraylist1.get(0).split(",");
                        arraylist1.clear();

                        create_grid(title ,grid_view);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
        catch (Exception e){
            Log.d("Naiifi Grid Exception :", "run: "+e.getMessage());
        }

    }

    private void grid_desc(GridView gridView , String subCategory){

        db = FirebaseFirestore.getInstance();
        arrayList2 = new ArrayList<>();

        try{
            db.collection("Description").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot document : task.getResult()){
                            arrayList2.add(document.getData().get(subCategory).toString());


                        }
                        String description = arrayList2.get(0);
                        create_info(description,subCategory);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Description error:", "description: "+e.getMessage());
                }
            });

        }
        catch (Exception e){
            Log.d("Description error", "run:"+e.getMessage());
        }

    }

    private void create_info( String desc , String item){

        if(desc.isEmpty()){

        }
        else{
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setMessage(desc) .setTitle(item);
            builder.setTitle(item);

            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Information");
            alert.show();

        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data);

        try{
            switch (requestCode){
                case PICK_IMG :

                    if (requestCode == PICK_IMG) {
                        if (resultCode == RESULT_OK) {
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                if(count>4){
                                    Toast.makeText(this, "At max only 4 images can be selected ! Reselect images .", Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    int CurrentImageSelect = 0;

                                    while (CurrentImageSelect < count) {
                                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                                        ImageList.add(imageuri);
                                        CurrentImageSelect = CurrentImageSelect + 1;
                                    }

                                    upload_image_txt.setVisibility(View.VISIBLE);
                                    upload_image_txt.setText("You Have Selected "+ ImageList.size() +" Pictures" );
                                    btnChoose.setVisibility(View.GONE);
                                    btnUpload.setVisibility(View.VISIBLE);

                                }


                            }

                        }

                    }

                    break;

                case ADDRESS_PICKER_REQUEST:

                    if (requestCode == ADDRESS_PICKER_REQUEST) {
                        try {
                            if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                                location_bt.setText("Re-select Location");
                                txtAddress.setVisibility(View.VISIBLE);
                                txtCity.setVisibility(View.VISIBLE);
                                txtPostal.setVisibility(View.VISIBLE);
                                txtState.setVisibility(View.VISIBLE);
                                txtLong.setVisibility(View.VISIBLE);
                                txtLat.setVisibility(View.VISIBLE);
                                finalUpload.setVisibility(View.VISIBLE);
                                // String address = data.getStringExtra(MapUtility.ADDRESS);
                                double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                                double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                                Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */

                                txtAddress.setText(new StringBuilder().append
                                        (completeAddress.getString("addressline2")));

                                txtLat.setText(new StringBuilder().append(currentLatitude).toString());
                                txtLong.setText(new StringBuilder().append(currentLongitude).toString());
                                txtPostal.setText(completeAddress.getString("postalcode").toString());
                                txtCity.setText(completeAddress.getString("city"));
                                txtState.setText(completeAddress.getString("state"));

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


            }
        }
        catch (Exception e){
            Log.d("OnActiivty Err:", "onActivityResult: "+e.getMessage());
        }


    }

    public void uploadImages() {

        mAuth=FirebaseAuth.getInstance();
        arr =new ArrayList<>();
        dialog.show();
        String user = mAuth.getCurrentUser().getUid().toString();


        upload_image_txt.setText("Please Wait ... If Uploading takes Too much time please click the button again ");

        int c=0;
        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child(user);
        for (uploads=0; uploads < ImageList.size(); uploads++) {
            c=c+1;

            String field = "url"+Integer.toString(uploads);
            Uri Image  = ImageList.get(uploads);
            final StorageReference imagename = ImageFolder.child("image/"+Image.getLastPathSegment());



            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);

                            arr.add(url);
                            //System.out.println(arr);
                            //System.out.println(ImageList.size());

                            upload_image_txt.setVisibility(View.GONE);
                            SendLink();

                        }
                    });

                }
            });

        }


    }


   private void SendLink() {

        if (arr.size()==ImageList.size()){
            HashMap<String , Object> map = new HashMap<>();
            for (int i=0 ; i<ImageList.size();i++){

                String field="url"+String.valueOf(i);
                String url=arr.get(i);

                map.put(field,url);

            }
            mAuth=FirebaseAuth.getInstance();
            String user = mAuth.getCurrentUser().getUid().toString();
            db.collection(user).document("imageUrl").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        ImageList.clear();
                        arr.clear();
                        Toast.makeText(InfoActivity.this, "Images uploaded Successfully.", Toast.LENGTH_SHORT).show();
                        btnUpload.setVisibility(View.GONE);
                        location_bt.setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("url err:", e.getMessage());
                }
            });

        }


    }

    public void uploadLocation(){
        String address = txtAddress.getText().toString();
        String pincode = txtPostal.getText().toString();
        String latitude = txtLat.getText().toString();
        String longitude = txtLong.getText().toString();
        String city = txtCity.getText().toString();
        String state = txtState.getText().toString();

        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        String info = "complete";
        HashMap<String , Object> map  = new HashMap<>();

        map.put("address",address);
        map.put("pincode",pincode);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("city",city);
        map.put("state",state);


        db.collection(user).document("locationData").set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("PhoneNo").child(user);

                    firebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue(String.class);
                            FirebaseDatabase.getInstance().getReference("Salons").child(value).child("info").setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(InfoActivity.this, "Salon profile completed !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InfoActivity.this , DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("info err:", "onFailure: "+e.getMessage());
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InfoActivity.this, "Some error occured !", Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void onBackPressed() {

    }


}