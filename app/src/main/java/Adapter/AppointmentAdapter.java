package Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.naiifipartner.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context mContext ;
    private ArrayList<HashMap<String , String>> arrayList ;

    public AppointmentAdapter (ArrayList<HashMap<String , String>> arrayList , Context mContext){

        this.arrayList = arrayList ;
        this.mContext = mContext ;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View appointmentLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent,false);
        AppointmentViewHolder appointmentViewHolder = new AppointmentAdapter.AppointmentViewHolder(appointmentLayout);

        return appointmentViewHolder;

    }



    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {

        holder.unique_id.setText(arrayList.get(position).get("id").toString());
        holder.appointment_cost.setText(arrayList.get(position).get("cost").toString());
        holder.appointment_services.setText(arrayList.get(position).get("services").toString());
        holder.appointment_time.setText(arrayList.get(position).get("time").toString());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView unique_id , appointment_time , appointment_services , appointment_cost ;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            unique_id = itemView.findViewById(R.id.unique_id);
            appointment_time = itemView.findViewById(R.id.appointment_time);
            appointment_services = itemView.findViewById(R.id.appointment_services);
            appointment_cost = itemView.findViewById(R.id.appointment_cost);
        }
    }


}



