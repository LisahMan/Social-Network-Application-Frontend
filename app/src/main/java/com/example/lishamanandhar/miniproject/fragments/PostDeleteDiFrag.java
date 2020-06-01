package com.example.lishamanandhar.miniproject.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lishamanandhar.miniproject.R;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class PostDeleteDiFrag extends DialogFragment {

    String postId;
    int position;
    Button btnDelete , btnDeleteCancel;

    DialogInterface dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString("postId");
        position = getArguments().getInt("position");
        Log.i("diPostId",postId);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.dialog_frag_delete_post,container,false);
      return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDelete = (Button) view.findViewById(R.id.btndelete);
        btnDeleteCancel = (Button) view.findViewById(R.id.btndeletecancel);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("postId",postId);
                i.putExtra("position",position);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,i);
                onDismiss(dialog);

            }
        });

        btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
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
