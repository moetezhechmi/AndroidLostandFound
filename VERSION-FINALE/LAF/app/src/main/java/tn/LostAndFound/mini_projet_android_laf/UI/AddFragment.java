package tn.LostAndFound.mini_projet_android_laf.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tn.LostAndFound.mini_projet_android_laf.R;


public class AddFragment extends Fragment {

Button btnTrouve,btnPerdu ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        btnPerdu = view.findViewById(R.id.btnPerdu);
        btnTrouve = view.findViewById(R.id.btnTrouve);

btnTrouve.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity().getApplication(), FormObjTrouveActivity.class);
        startActivity(intent);    }
});

btnPerdu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity().getApplication(), FormObjPerduActivity.class);
        startActivity(intent);

    }
});
        return  view;
    }
}