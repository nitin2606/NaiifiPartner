package Adapter;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.naiifipartner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import Fragments.EditFragment;


public class EditDataAdapter extends RecyclerView.Adapter<EditDataAdapter.EditDataViewHolder> {

    private final ArrayList<String> serviceData;
    private final Context mContext;
    private final String tag;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Dialog dialog1 ;

    private EditFragment editFragment ;



    public EditDataAdapter(ArrayList<String> serviceData, Context mContext , String tag) {
        this.serviceData = serviceData;
        this.mContext = mContext;
        this.tag = tag;


    }

    @NonNull
    @Override
    public EditDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View editLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_edit_layout, parent,false);
        EditDataViewHolder editDataViewHolder = new EditDataViewHolder(editLayout);

        return editDataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EditDataViewHolder holder, int position) {
        holder.service_name.setText("Service : "+serviceData.get(position));
        servicePrice(serviceData.get(position), holder.service_price );


    }



    @Override
    public int getItemCount() {
        return serviceData.size();
    }


    public class EditDataViewHolder extends RecyclerView.ViewHolder{


        MaterialTextView service_name , service_price , service_time;

        public EditDataViewHolder(@NonNull View itemView) {
            super(itemView);

            service_name = itemView.findViewById(R.id.service_name);
            service_price = itemView.findViewById(R.id.service_price);
            service_time = itemView.findViewById(R.id.service_time);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String serv = service_name.getText().toString().trim();
                    serv = serv.replace("Service : ","").trim();
                    String pr = service_price.getText().toString().trim();
                    pr = pr.replace("Price: ","").trim();
                    updateServiceData(service_price,serv,pr ,tag ,getAdapterPosition() );


                    return true;
                }
            });

        }
    }

    private void servicePrice(String service , MaterialTextView textView ){

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

        try{

            db.collection(user).document("priceData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        try{
                            String data = task.getResult().get(service).toString();
                            textView.setText("Price: "+data);
                        }
                        catch (Exception e){

                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("price err", "onFailure: "+e.getMessage());
                }
            });

        }
        catch (Exception e){
            Log.d("err:", "servicePrice: "+e.getMessage());
        }

    }



   private void updateServiceData(MaterialTextView materialTextView,String service , String price , String t , int pos){


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();

        dialog1 = new Dialog(mContext);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setContentView(R.layout.dialog_wait);
        dialog1.setCanceledOnTouchOutside(false);


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.custom_edit_confirm, null);
        builder.setView(dialogView);

        MaterialButton edit = dialogView.findViewById(R.id.btn_edit);
        MaterialButton delete = dialogView.findViewById(R.id.btn_delete);
        ImageView cancel = dialogView.findViewById(R.id.cancel);

        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();



       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteData(service , t , pos );
               dialog.dismiss();
           }
       });

       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });


        AlertDialog.Builder builder_edit = new AlertDialog.Builder(mContext);
        LayoutInflater inflater_edit = LayoutInflater.from(mContext);
        View dialogView_edit = inflater_edit.inflate(R.layout.basic_edit_layout,null);
        builder_edit.setView(dialogView_edit);

        MaterialTextView service_name = dialogView_edit.findViewById(R.id.service_name);
        EditText service_price = dialogView_edit.findViewById(R.id.service_price);
        MaterialButton btn_ok = dialogView_edit.findViewById(R.id.btn_ok);
        MaterialButton btn_cancel = dialogView_edit.findViewById(R.id.btn_cancel);

        service_name.setText(service);
        service_price.setText(price);

        AlertDialog dialog_edit = builder_edit.create();
        dialog_edit.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


       edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
               dialog_edit.show();

           }
       });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_edit.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(service_price.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "Price field must not be empty !", Toast.LENGTH_SHORT).show();
                }

                else{
                    dialog1.show();
                    String price = service_price.getText().toString().trim();
                    HashMap<String , Object> map  = new HashMap<>();
                    map.put(service.trim() , price);


                    db.collection(user).document("priceData").update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                materialTextView.setText("Price: "+price);
                                dialog_edit.dismiss();
                                dialog1.dismiss();
                                Toast.makeText(mContext, "Price updated successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private void deleteData(String service , String t ,int pos){


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();
        //System.out.println("This is recyclerviwe tag: "+t);

        db.collection(user).document("serviceData").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String s = task.getResult().get(t).toString();

                    String[] arr = s.split(";");

                    if(arr.length==1){
                        Map<String ,Object> temp = new HashMap<>();
                        temp.put(t.trim(),FieldValue.delete());
                        db.collection(user).document("serviceData").update(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    temp.clear();
                                    temp.put(service.trim() , FieldValue.delete());

                                    db.collection(user).document("priceData").update(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                EditFragment editFragment = new EditFragment();

                                                //editFragment.referenceMethod();
                                                Toast.makeText(mContext, "Data Deleted Successfully .", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }

                    else{
                        String[] arr1 = new String[arr.length-1];

                        for(int i=0 , k=0 ; i<arr.length ; i++){
                            if(!service.equals(arr[i])){

                                arr1[k] = arr[i];
                                k++;
                            }
                        }

                        String modifiedData = Arrays.toString(arr1);

                        //modifiedData = modifiedData.replace(" ","");
                        modifiedData = modifiedData.replace(", ",";");
                        modifiedData = modifiedData.replace("[","");
                        modifiedData = modifiedData.replace("]","");


                        HashMap<String , Object> map = new HashMap<>();
                        map.put(t,modifiedData);

                        db.collection(user).document("serviceData").update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    DocumentReference docRef = db.collection(user).document("priceData");

                                    Map<String,Object> updates = new HashMap<>();
                                    updates.put(service.trim(), FieldValue.delete());

                                    docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){


                                                Toast.makeText(mContext, "Data deleted successfully .", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }
                            }
                        });

                    }



                }
            }
        });
        notifyDataSetChanged();


    }





}
