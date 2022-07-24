package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.naiifipartner.R;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.AppointmentAdapter;
import Adapter.EditDataAdapter;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView ;

    private AppointmentAdapter appointmentAdapter ;

    private HashMap<String , String>  hashMap , hashMap2 , hashMap3 , hashMap4 ,hashMap5 , hashMap6 , hashMap7;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<HashMap<String , String>> arrayList = new ArrayList<>();

        hashMap  = new HashMap<>();
        hashMap2 = new HashMap<>();
        hashMap3 = new HashMap<>();
        hashMap4 = new HashMap<>();
        hashMap5 = new HashMap<>();
        hashMap6 = new HashMap<>();
        hashMap7 = new HashMap<>();

        hashMap.put("id","45bcs");
        hashMap.put("time","12:30 pm");
        hashMap.put("services","HairCut , Hair Styling");
        hashMap.put("cost","600");


        hashMap2.put("id","46bcs");
        hashMap2.put("time","1:30 pm");
        hashMap2.put("services","Bleach , Hair Styling");
        hashMap2.put("cost","650");

        hashMap3.put("id","47bcs");
        hashMap3.put("time","2:30 pm");
        hashMap3.put("services","HairCut , Hair Styling");
        hashMap3.put("cost","500");

        hashMap4.put("id","48bcs");
        hashMap4.put("time","4:30 pm");
        hashMap4.put("services","HairCut , Spa");
        hashMap4.put("cost","800");

        hashMap5.put("id","49bcs");
        hashMap5.put("time","12:30 pm");
        hashMap5.put("services","HairCut , Hair Styling");
        hashMap5.put("cost","900");

        hashMap6.put("id","50bcs");
        hashMap6.put("time","2:30 pm");
        hashMap6.put("services","HairCut , Hair Styling");
        hashMap6.put("cost","900");

        hashMap7.put("id","51bcs");
        hashMap7.put("time","12:30 pm");
        hashMap7.put("services","HairCut , Hair Styling");
        hashMap7.put("cost","900");



        arrayList.add(hashMap);
        arrayList.add(hashMap2);
        arrayList.add(hashMap3);
        arrayList.add(hashMap4);
        arrayList.add(hashMap5);
        arrayList.add(hashMap6);
        arrayList.add(hashMap7);



        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        appointmentAdapter = new AppointmentAdapter(arrayList ,getContext());
        recyclerView.setAdapter(appointmentAdapter);

        return view;
    }

}
