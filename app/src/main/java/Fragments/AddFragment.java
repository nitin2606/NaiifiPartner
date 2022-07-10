package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.naiifipartner.R;
import com.example.naiifipartner.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {


    //private LinearLayout add_more_layout_common , add_more_layout_uni , add_more_layout_male , add_more_layout_female ;

    FragmentAddBinding fragmentAddBinding ;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_add, container,false);






        return view;
    }


}