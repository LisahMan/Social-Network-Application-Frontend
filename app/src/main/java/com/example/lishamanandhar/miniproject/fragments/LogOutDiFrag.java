package com.example.lishamanandhar.miniproject.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lishamanandhar.miniproject.LoginActivity;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.StartActivity;
import com.example.lishamanandhar.miniproject.session.SessionManager;

/**
 * Created by Lisha Manandhar on 11/13/2017.
 */

public class LogOutDiFrag extends DialogFragment {

    Button btnLogOut , btnCancel;
    DialogInterface dialog;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_frag_log_out,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogOut = (Button) view.findViewById(R.id.btnlogout);
        btnCancel = (Button) view.findViewById(R.id.btncancel);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logOut();
                Intent i = new Intent(getActivity(), StartActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismiss(dialog);
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


}
