package Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.naiifipartner.GridItemView;
import com.example.naiifipartner.InfoActivity;
import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import Adapter.GridViewAdapter_add;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddFragment extends Fragment {


    private FirebaseFirestore db ;
    private FirebaseAuth mAuth;

    private HashMap<String , Object> grid_data_map ;

    private ArrayList<String>selectedStrings;
    private ArrayList<String> arraylist1;
    private ArrayList<String> arrayList2;

    private EditText editText ;

    private FloatingActionButton add_floating_bt_save  ,add_floating_bt_clear ;
    private MaterialButton retrieve_button;



    private LinearLayout add_more_layout_common , add_more_layout_uni , add_more_layout_male , add_more_layout_female  ,add_linear_dynamic ;



    private GridView grid_more_bleach  ,grid_more_bodyPolishing , grid_more_cleanUp , grid_more_dTan , grid_more_facial , grid_more_hairColor ,
            grid_more_hairCut , grid_more_hairSpa , grid_more_hairTreatment , grid_more_hairWash , grid_more_headMassage , grid_more_manicure ,
            grid_more_pedicure ,

            grid_more_uni_faceMask  , grid_more_uni_hairStyling , grid_more_uni_makeUp , grid_more_uni_manicureSpa ,
            grid_more_uni_nailArt , grid_more_uni_shaving , grid_more_uni_threading , grid_more_uni_waxing ,grid_more_uni_others,

            grid_more_male_manicureSpa , grid_more_male_shaving , grid_more_male_others ,

            grid_more_female_faceMask , grid_more_female_hairStyling , grid_more_female_makeUp , grid_more_female_nailArt , grid_more_female_threading
                    ,grid_more_female_waxing , grid_more_female_others;

    private TextView  text_more_bleach  ,text_more_bodyPolishing , text_more_cleanUp , text_more_dTan , text_more_facial , text_more_hairColor ,
            text_more_hairCut , text_more_hairSpa , text_more_hairTreatment , text_more_hairWash , text_more_headMassage , text_more_manicure ,
            text_more_pedicure ,

            text_more_uni_faceMask , text_more_uni_hairStyling , text_more_uni_makeUp , text_more_uni_manicureSpa ,
            text_more_uni_nailArt , text_more_uni_shaving , text_more_uni_threading , text_more_uni_waxing ,text_more_uni_others,

            text_more_male_manicureSpa , text_more_male_shaving , text_more_male_others ,

            text_more_female_faceMask , text_more_female_hairStyling , text_more_female_makeUp , text_more_female_nailArt , text_more_female_threading
            ,text_more_female_waxing , text_more_female_others;

    private ProgressBar pro_more_bleach  ,pro_more_bodyPolishing , pro_more_cleanUp , pro_more_dTan , pro_more_facial , pro_more_hairColor ,
            pro_more_hairCut , pro_more_hairSpa , pro_more_hairTreatment , pro_more_hairWash , pro_more_headMassage , pro_more_manicure ,
            pro_more_pedicure ,

            pro_more_uni_faceMask ,  pro_more_uni_hairStyling , pro_more_uni_makeUp , pro_more_uni_manicureSpa ,
            pro_more_uni_nailArt , pro_more_uni_shaving , pro_more_uni_threading , pro_more_uni_waxing ,pro_more_uni_others,

            pro_more_male_manicureSpa , pro_more_male_shaving , pro_more_male_others ,

            pro_more_female_faceMask , pro_more_female_hairStyling , pro_more_female_makeUp , pro_more_female_nailArt , pro_more_female_threading
            ,pro_more_female_waxing , pro_more_female_others;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_add, container,false);

        add_floating_bt_save = view.findViewById(R.id.add_floating_bt_save);
        add_floating_bt_clear = view.findViewById(R.id.add_floating_bt_clear);

        retrieve_button = view.findViewById(R.id.retrieve_button);

        add_more_layout_common = view.findViewById(R.id.add_more_layout_common);
        add_more_layout_uni = view.findViewById(R.id.add_more_layout_uni);
        add_more_layout_male = view.findViewById(R.id.add_more_layout_male);
        add_more_layout_female = view.findViewById(R.id.add_more_layout_female);
        add_linear_dynamic = view.findViewById(R.id.add_linear_dynamic);

        grid_more_bleach = view.findViewById(R.id.grid_more_bleach);
        grid_more_bodyPolishing = view.findViewById(R.id.grid_more_bodyPolishing);
        grid_more_cleanUp = view.findViewById(R.id.grid_more_cleanUp);
        grid_more_dTan = view.findViewById(R.id.grid_more_dTan);
        grid_more_facial = view.findViewById(R.id.grid_more_facial);
        grid_more_hairColor = view.findViewById(R.id.grid_more_hairColor);
        grid_more_hairCut  = view.findViewById(R.id.grid_more_hairCut);
        grid_more_hairSpa = view.findViewById(R.id.grid_more_hairSpa);
        grid_more_hairTreatment = view.findViewById(R.id.grid_more_hairTreatment);
        grid_more_hairWash = view.findViewById(R.id.grid_more_hairWash);
        grid_more_headMassage = view.findViewById(R.id.grid_more_headMassage);
        grid_more_manicure = view.findViewById(R.id.grid_more_manicure);
        grid_more_pedicure = view.findViewById(R.id.grid_more_pedicure);

        grid_more_uni_faceMask = view.findViewById(R.id.grid_more_uni_faceMask);
        grid_more_uni_hairStyling = view.findViewById(R.id.grid_more_uni_hairStyling);
        grid_more_uni_makeUp = view.findViewById(R.id.grid_more_uni_makeUp);
        grid_more_uni_manicureSpa = view.findViewById(R.id.grid_more_uni_manicureSpa);
        grid_more_uni_nailArt = view.findViewById(R.id.grid_more_uni_nailArt);
        grid_more_uni_shaving = view.findViewById(R.id.grid_more_uni_shaving);
        grid_more_uni_threading = view.findViewById(R.id.grid_more_uni_threading);
        grid_more_uni_waxing = view.findViewById(R.id.grid_more_uni_waxing);
        grid_more_uni_others = view.findViewById(R.id.grid_more_uni_others);

        grid_more_male_manicureSpa = view.findViewById(R.id.grid_more_male_manicureSpa);
        grid_more_male_shaving = view.findViewById(R.id.grid_more_male_shaving);
        grid_more_male_others  = view.findViewById(R.id.grid_more_male_others);

        grid_more_female_faceMask = view.findViewById(R.id.grid_more_female_faceMask);
        grid_more_female_hairStyling = view.findViewById(R.id.grid_more_female_hairStyling);
        grid_more_female_makeUp = view.findViewById(R.id.grid_more_female_makeUp);
        grid_more_female_nailArt = view.findViewById(R.id.grid_more_female_nailArt);
        grid_more_female_threading = view.findViewById(R.id.grid_more_female_threading);
        grid_more_female_waxing = view.findViewById(R.id.grid_more_female_waxing);
        grid_more_female_others = view.findViewById(R.id.grid_more_female_others);

// -------------------------------------------------------------------------------------------------------------
        text_more_bleach = view.findViewById(R.id.text_more_bleach);
        text_more_bodyPolishing = view.findViewById(R.id.text_more_bodyPolishing);
        text_more_cleanUp = view.findViewById(R.id.text_more_cleanUp);
        text_more_dTan = view.findViewById(R.id.text_more_dTan);
        text_more_facial = view.findViewById(R.id.text_more_facial);
        text_more_hairColor = view.findViewById(R.id.text_more_hairColor);
        text_more_hairCut  = view.findViewById(R.id.text_more_hairCut);
        text_more_hairSpa = view.findViewById(R.id.text_more_hairSpa);
        text_more_hairTreatment = view.findViewById(R.id.text_more_hairTreatment);
        text_more_hairWash = view.findViewById(R.id.text_more_hairWash);
        text_more_headMassage = view.findViewById(R.id.text_more_headMassage);
        text_more_manicure = view.findViewById(R.id.text_more_manicure);
        text_more_pedicure = view.findViewById(R.id.text_more_pedicure);

        text_more_uni_faceMask = view.findViewById(R.id.text_more_uni_faceMask);
        text_more_uni_hairStyling = view.findViewById(R.id.text_more_uni_hairStyling);
        text_more_uni_makeUp = view.findViewById(R.id.text_more_uni_makeUp);
        text_more_uni_manicureSpa = view.findViewById(R.id.text_more_uni_manicureSpa);
        text_more_uni_nailArt = view.findViewById(R.id.text_more_uni_nailArt);
        text_more_uni_shaving = view.findViewById(R.id.text_more_uni_shaving);
        text_more_uni_threading = view.findViewById(R.id.text_more_uni_threading);
        text_more_uni_waxing = view.findViewById(R.id.text_more_uni_waxing);
        text_more_uni_others = view.findViewById(R.id.text_more_uni_others);

        text_more_male_manicureSpa = view.findViewById(R.id.text_more_male_manicureSpa);
        text_more_male_shaving = view.findViewById(R.id.text_more_male_shaving);
        text_more_male_others  = view.findViewById(R.id.text_more_male_others);

        text_more_female_faceMask = view.findViewById(R.id.text_more_female_faceMask);
        text_more_female_hairStyling = view.findViewById(R.id.text_more_female_hairStyling);
        text_more_female_makeUp = view.findViewById(R.id.text_more_female_makeUp);
        text_more_female_nailArt = view.findViewById(R.id.text_more_female_nailArt);
        text_more_female_threading = view.findViewById(R.id.text_more_female_threading);
        text_more_female_waxing = view.findViewById(R.id.text_more_female_waxing);
        text_more_female_others = view.findViewById(R.id.text_more_female_others);

// --------------------------------------------------------------------------------------------------------------------------------

        pro_more_bleach = view.findViewById(R.id.pro_more_bleach);
        pro_more_bodyPolishing = view.findViewById(R.id.pro_more_bodyPolishing);
        pro_more_cleanUp = view.findViewById(R.id.pro_more_cleanUp);
        pro_more_dTan = view.findViewById(R.id.pro_more_dTan);
        pro_more_facial = view.findViewById(R.id.pro_more_facial);
        pro_more_hairColor = view.findViewById(R.id.pro_more_hairColor);
        pro_more_hairCut  = view.findViewById(R.id.pro_more_hairCut);
        pro_more_hairSpa = view.findViewById(R.id.pro_more_hairSpa);
        pro_more_hairTreatment = view.findViewById(R.id.pro_more_hairTreatment);
        pro_more_hairWash = view.findViewById(R.id.pro_more_hairWash);
        pro_more_headMassage = view.findViewById(R.id.pro_more_headMassage);
        pro_more_manicure = view.findViewById(R.id.pro_more_manicure);
        pro_more_pedicure = view.findViewById(R.id.pro_more_pedicure);

        pro_more_uni_faceMask = view.findViewById(R.id.pro_more_uni_faceMask);
        pro_more_uni_hairStyling = view.findViewById(R.id.pro_more_uni_hairStyling);
        pro_more_uni_makeUp = view.findViewById(R.id.pro_more_uni_makeUp);
        pro_more_uni_manicureSpa = view.findViewById(R.id.pro_more_uni_manicureSpa);
        pro_more_uni_nailArt = view.findViewById(R.id.pro_more_uni_nailArt);
        pro_more_uni_shaving = view.findViewById(R.id.pro_more_uni_shaving);
        pro_more_uni_threading = view.findViewById(R.id.pro_more_uni_threading);
        pro_more_uni_waxing = view.findViewById(R.id.pro_more_uni_waxing);
        pro_more_uni_others = view.findViewById(R.id.pro_more_uni_others);

        pro_more_male_manicureSpa = view.findViewById(R.id.pro_more_male_manicureSpa);
        pro_more_male_shaving = view.findViewById(R.id.pro_more_male_shaving);
        pro_more_male_others  = view.findViewById(R.id.pro_more_male_others);

        pro_more_female_faceMask = view.findViewById(R.id.pro_more_female_faceMask);
        pro_more_female_hairStyling = view.findViewById(R.id.pro_more_female_hairStyling);
        pro_more_female_makeUp = view.findViewById(R.id.pro_more_female_makeUp);
        pro_more_female_nailArt = view.findViewById(R.id.pro_more_female_nailArt);
        pro_more_female_threading = view.findViewById(R.id.pro_more_female_threading);
        pro_more_female_waxing = view.findViewById(R.id.pro_more_female_waxing);
        pro_more_female_others = view.findViewById(R.id.pro_more_female_others);


// --------------------------------------------------------------------------------------------------------------------


        deleteCache(getContext());

        initiate_gridView();

        add_floating_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(selectedStrings.isEmpty()){
                        add_linear_dynamic.removeAllViews();
                        Toast.makeText(getContext(), "No Service Selected !", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        add_linear_dynamic.removeAllViews();
                        dynamic_edt(selectedStrings , add_linear_dynamic);
                        //price_instruction.setVisibility(View.VISIBLE);
                        //submit.setVisibility(View.VISIBLE);
                        //scr_view.smoothScrollBy(0,80000);
                        //System.out.println(grid_data_map);
                        System.out.println(grid_data_map);


                    }

                }
                catch (Exception e){
                    Log.d("save bt error", "Exception: "+e.getMessage());
                }

            }
        });


        add_floating_bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dynamic_del(add_linear_dynamic);
                    //price_instruction.setVisibility(View.GONE);

                }
                catch (Exception e){
                    Log.d("Clear bt exception", "onClick: "+e.getMessage());
                }
            }
        });

        retrieve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(retrieve_edt_data(add_linear_dynamic));
                processData(grid_data_map);
            }
        });





        return view;
    }

// ****************************************************************************************************************************





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

    private void initiate_gridView(){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();


        db.collection(user).document("basicData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String salonType = task.getResult().getString("salonCategory").toString();


                    if(salonType.equals("Unisex Salon (Male and Female Both)")){

                        String type = "unisex";

                        recieveSalonData(type);

                    }

                    else if (salonType.equals("Male Salon")){

                        String type = "male";
                        add_more_layout_male.setVisibility(View.VISIBLE);
                        recieveSalonData(type);
                    }

                    else if(salonType.equals("Female Salon")){

                        String type = "female";
                        add_more_layout_female.setVisibility(View.VISIBLE);
                        recieveSalonData(type );
                    }

                }
            }
        });
    }


    private void recieveSalonData(String type ){




        if(type.equals("unisex")){

            add_more_layout_uni.setVisibility(View.VISIBLE);

            fetchData(grid_more_bleach ,type, pro_more_bleach , text_more_bleach);
            fetchData(grid_more_bodyPolishing , type , pro_more_bodyPolishing , text_more_bodyPolishing);
            fetchData(grid_more_cleanUp ,type, pro_more_cleanUp , text_more_cleanUp);
            fetchData(grid_more_dTan ,type, pro_more_dTan , text_more_dTan);
            fetchData(grid_more_facial ,type, pro_more_facial , text_more_facial);
            fetchData(grid_more_hairColor ,type, pro_more_hairColor , text_more_hairColor);
            fetchData(grid_more_hairCut ,type, pro_more_hairCut , text_more_hairCut);
            fetchData(grid_more_hairSpa ,type, pro_more_hairSpa , text_more_hairSpa);
            fetchData(grid_more_hairTreatment ,type, pro_more_hairTreatment , text_more_hairTreatment);
            fetchData(grid_more_hairWash ,type, pro_more_hairWash , text_more_hairWash);
            fetchData(grid_more_headMassage ,type, pro_more_headMassage , text_more_headMassage);
            fetchData(grid_more_manicure ,type, pro_more_manicure , text_more_manicure);
            fetchData(grid_more_pedicure ,type, pro_more_pedicure , text_more_pedicure);

            fetchData(grid_more_uni_faceMask ,type, pro_more_uni_faceMask , text_more_uni_faceMask);
            fetchData(grid_more_uni_hairStyling ,type, pro_more_uni_hairStyling , text_more_uni_hairStyling);
            fetchData(grid_more_uni_makeUp ,type, pro_more_uni_makeUp , text_more_uni_makeUp);
            fetchData(grid_more_uni_manicureSpa ,type, pro_more_uni_manicureSpa , text_more_uni_manicureSpa);
            fetchData(grid_more_uni_nailArt ,type, pro_more_uni_nailArt , text_more_uni_nailArt);
            fetchData(grid_more_uni_shaving ,type, pro_more_uni_shaving , text_more_uni_shaving);
            fetchData(grid_more_uni_threading ,type, pro_more_uni_threading , text_more_uni_threading);
            fetchData(grid_more_uni_waxing ,type, pro_more_uni_waxing , text_more_uni_waxing);
            fetchData(grid_more_uni_others ,type, pro_more_uni_others , text_more_uni_others);

        }
        else if (type.equals("male")){

            add_more_layout_male.setVisibility(View.VISIBLE);

            fetchData(grid_more_bleach ,type, pro_more_bleach , text_more_bleach);
            fetchData(grid_more_bodyPolishing , type , pro_more_bodyPolishing , text_more_bodyPolishing);
            fetchData(grid_more_cleanUp ,type, pro_more_cleanUp , text_more_cleanUp);
            fetchData(grid_more_dTan ,type, pro_more_dTan , text_more_dTan);
            fetchData(grid_more_facial ,type, pro_more_facial , text_more_facial);
            fetchData(grid_more_hairColor ,type, pro_more_hairColor , text_more_hairColor);
            fetchData(grid_more_hairCut ,type, pro_more_hairCut , text_more_hairCut);
            fetchData(grid_more_hairSpa ,type, pro_more_hairSpa , text_more_hairSpa);
            fetchData(grid_more_hairTreatment ,type, pro_more_hairTreatment , text_more_hairTreatment);
            fetchData(grid_more_hairWash ,type, pro_more_hairWash , text_more_hairWash);
            fetchData(grid_more_headMassage ,type, pro_more_headMassage , text_more_headMassage);
            fetchData(grid_more_manicure ,type, pro_more_manicure , text_more_manicure);
            fetchData(grid_more_pedicure ,type, pro_more_pedicure , text_more_pedicure);

            fetchData(grid_more_male_manicureSpa ,type, pro_more_male_manicureSpa , text_more_male_manicureSpa);
            fetchData(grid_more_male_shaving ,type, pro_more_male_shaving , text_more_male_shaving);
            fetchData(grid_more_male_others ,type, pro_more_male_others , text_more_male_others);
        }

        else if(type.equals("female")){

            add_more_layout_female.setVisibility(View.VISIBLE);

            fetchData(grid_more_bleach ,type, pro_more_bleach , text_more_bleach);
            fetchData(grid_more_bodyPolishing , type , pro_more_bodyPolishing , text_more_bodyPolishing);
            fetchData(grid_more_cleanUp ,type, pro_more_cleanUp , text_more_cleanUp);
            fetchData(grid_more_dTan ,type, pro_more_dTan , text_more_dTan);
            fetchData(grid_more_facial ,type, pro_more_facial , text_more_facial);
            fetchData(grid_more_hairColor ,type, pro_more_hairColor , text_more_hairColor);
            fetchData(grid_more_hairCut ,type, pro_more_hairCut , text_more_hairCut);
            fetchData(grid_more_hairSpa ,type, pro_more_hairSpa , text_more_hairSpa);
            fetchData(grid_more_hairTreatment ,type, pro_more_hairTreatment , text_more_hairTreatment);
            fetchData(grid_more_hairWash ,type, pro_more_hairWash , text_more_hairWash);
            fetchData(grid_more_headMassage ,type, pro_more_headMassage , text_more_headMassage);
            fetchData(grid_more_manicure ,type, pro_more_manicure , text_more_manicure);
            fetchData(grid_more_pedicure ,type, pro_more_pedicure , text_more_pedicure);

            fetchData(grid_more_female_faceMask ,type, pro_more_female_faceMask , text_more_female_faceMask);
            fetchData(grid_more_female_hairStyling ,type, pro_more_female_hairStyling , text_more_female_hairStyling);
            fetchData(grid_more_female_makeUp ,type, pro_more_female_makeUp , text_more_female_makeUp);
            fetchData(grid_more_female_nailArt ,type, pro_more_female_nailArt , text_more_female_nailArt);
            fetchData(grid_more_female_threading ,type, pro_more_female_threading , text_more_female_threading);
            fetchData(grid_more_female_waxing ,type, pro_more_female_waxing , text_more_female_waxing);
            fetchData(grid_more_female_others ,type, pro_more_female_others , text_more_female_others);
        }


    }


    private void fetchData(GridView gridView , String type  , ProgressBar progressBar , TextView textView){

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        String category2 = gridView.getTag().toString().trim();
        String categ = category2.substring(0,1).toUpperCase() + category2.substring(1);
        String category1 = type+categ ;

        HashMap<String , String> extraMap = new HashMap<>();

        try{

            db.collection("Services").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            String completeData = Objects.requireNonNull(documentSnapshot.getData().get(category1)).toString();

                            extraMap.put("completeData",completeData);

                            //System.out.println("Complete Data Task : "+documentSnapshot.getData().get(category1).toString());


                        }
                    }
                }
            });


            db.collection(user).document("serviceData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().get(category2)!=null){

                            String selectedData = task.getResult().get(category2).toString();
                            extraMap.put("selectedData",selectedData);
                            // System.out.println("Selected Data Task : "+task.getResult().get(category2).toString());
                            recieveData(extraMap  , gridView , progressBar , textView);

                        }
                        else{

                            //extraList.add(null);
                            extraMap.put("selectedData","");
                            recieveData(extraMap  , gridView , progressBar , textView);
                        }

                    }
                }
            });

        } catch (Exception e){
            System.out.println("Data Fetching exp : "+e.getMessage());
        }



    }

    private void recieveData(HashMap<String , String> hashMap, GridView gridView , ProgressBar progressBar , TextView textView){

        Observable<HashMap<String, String>> observable = Observable.just(hashMap);

        Observer<HashMap<String , String>> observer = new Observer<HashMap<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {


            }

            @Override
            public void onNext(HashMap<String, String> hashMap) {


                //System.out.println("Name of Data Processing Thread : "+Thread.currentThread().getName().toString());


                if(hashMap.get("selectedData")==""){

                    try{

                        String[] resultArray = hashMap.get("completeData").split(",");

                        //create_grid(resultArray , gridView);

                        recieveArray(resultArray , gridView , progressBar , textView);

                    }catch (Exception e){
                        Log.d("onNext Error :", "onNext: "+e.getMessage());
                    }



                }

                else{

                    try {

                        String[] completeArray = hashMap.get("completeData").split(",");
                        String[] selectedArray = hashMap.get("selectedData").split(";");



                        if(selectedArray.length>0){

                            ArrayList<String> leftData = new ArrayList<>();


                            for(int i=0 ; i<completeArray.length ; i++){
                                int c=0;

                                for(int j=0 ; j<selectedArray.length ; j++){

                                    if(completeArray[i].equals(selectedArray[j])){
                                        c=c+1;
                                    }
                                }
                                if (c==0){
                                    leftData.add(completeArray[i]);

                                }

                            }


                            String[] leftArray = new String[leftData.size()];

                            for(int i = 0 ; i<leftData.size() ; i++){
                                leftArray[i]=leftData.get(i);

                            }


                           //create_grid(leftArray , gridView);
                            recieveArray(leftArray , gridView , progressBar , textView);

                        }
                        else{


                            //create_grid(completeArray , gridView);

                            recieveArray(completeArray , gridView , progressBar , textView);

                        }

                    }catch (Exception e){
                        Log.d("Background Error : ", "onNext: "+e.getMessage());
                    }
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {



            }
        };


        observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io()).subscribe(observer);


    }





    private void recieveArray(String[] array , GridView gridView , ProgressBar progressBar , TextView textView){



        class AsyncCaller extends AsyncTask<Void, Void, Void> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread


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
                create_grid(array , gridView , progressBar , textView);

                //this method will be running on UI thread


            }

        }

        new AsyncCaller().execute();



    }


    private  void create_grid(String[] arr , GridView gridView , ProgressBar progressBar , TextView textView){


        grid_data_map = new HashMap<>();
        selectedStrings = new ArrayList<>();
        ArrayList<String> services = new ArrayList<>();
        //services = new ArrayList<>();

        if(arr.length==0){
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Services Available");

        }

        final GridViewAdapter_add adapter = new GridViewAdapter_add(arr, (AppCompatActivity) getContext() );
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions_add.indexOf(position);
                if (selectedIndex > -1) {

                    adapter.selectedPositions_add.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                    services.remove((String) parent.getItemAtPosition(position));
                    String t = gridView.getTag().toString();
                    String serv = services.toString();
                    grid_data_map.put(t,serv);


                } else {
                    adapter.selectedPositions_add.add(position);
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

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                progressBar.setVisibility(View.GONE);
                gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });



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
            builder = new AlertDialog.Builder(getContext());
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

    private void dynamic_edt(ArrayList<String> arr , LinearLayout linearLayout){


        int s = arr.size();

        for(int i=0 ; i<s ;i++){
            editText = new EditText(getContext());
            String tname = "task"+Integer.toString(i);
            editText.setId(i);

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

    private HashMap<String , Object> retrieve_edt_data(LinearLayout linearLayout){


        HashMap<String , Object> dataMap = new HashMap<>();
        EditText editText = new EditText(getContext());

        for(int i=0 ; i<linearLayout.getChildCount();i++){
            editText = (EditText) linearLayout.getChildAt(i);
            String tag = linearLayout.getChildAt(i).getTag().toString();
            String data  = editText.getText().toString();
            dataMap.put(tag.trim(),data.trim());

        }

        return  dataMap;

    }

    private void processData(HashMap<String , Object> hashMap){

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid().toString();

        HashMap<String , Object> revisedMap = new HashMap<>();

        Observable<HashMap<String , Object>> observable = Observable.just(hashMap);

        Observer<HashMap<String , Object>> observer = new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HashMap<String, Object> hashMap) {



                db.collection(user).document("serviceData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)throws NullPointerException {
                        if(task.isSuccessful()){

                            for(Map.Entry<String , Object> entry : hashMap.entrySet()){
                                String key = entry.getKey();
                                String value = entry.getValue().toString().trim();

                                String initialData = task.getResult().get(key).toString().trim();

                                revisedMap.put(key , initialData+";"+value);

                            }

                            System.out.println("Revised Map : " + revisedMap);

                            db.collection(user).document("serviceData").update(revisedMap);
                            db.collection(user).document("priceData").update(retrieve_edt_data(add_linear_dynamic)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Services Added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {


            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(observer);


    }





}