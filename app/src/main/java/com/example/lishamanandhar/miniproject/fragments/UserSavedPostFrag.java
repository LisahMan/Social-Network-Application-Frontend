package com.example.lishamanandhar.miniproject.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.adapters.SavedPostAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.database.DatabaseManager;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnPostSavedClicked;
import com.example.lishamanandhar.miniproject.interfaces.OnPostUnsavedClicked;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class UserSavedPostFrag extends Fragment {

    RecyclerView recViewSavedPost;
    SessionManager sessionManager;
    String user;
//    MyApplication myApp;
    DatabaseManager dbm;
    SavedPostAdapter adapter;
    ArrayList<PostDataModel> postList;
//    String getSavePostUrl = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        myApp = MyApplication.getInstance();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUser();
        dbm = DatabaseManager.getInstance(getActivity());
        postList = dbm.fetchSavedPost();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_saved_post,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recViewSavedPost = (RecyclerView) view.findViewById(R.id.recviewsavedpost);
        adapter = new SavedPostAdapter(getActivity(),postList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recViewSavedPost.setLayoutManager(layoutManager);
        recViewSavedPost.setAdapter(adapter);

//        startGetSavedPostService();
    }

//    public void startGetSavedPostService(){
//        getSavePostUrl = "http://10.0.2.2:8000/post/savedpost/";
//        getSavePostUrl = getSavePostUrl+user+"/";
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getSavePostUrl,onGetSuccess,onGetError);
//        myApp.getRequestQueue().add(jsonArrayRequest);
//    }
//
//    Response.Listener<JSONArray> onGetSuccess = new Response.Listener<JSONArray>() {
//        @Override
//        public void onResponse(JSONArray response) {
//            ArrayList<PostDataModel> postList = new ArrayList<>();
//            for(int i=0;i<response.length();i++){
//                try {
//                    JSONObject jsonObject = response.getJSONObject(i);
//                    String sPostId = jsonObject.getString("id");
//                    String sUser = jsonObject.getString("user");
//                    String sUsername = jsonObject.getString("username");
//                    String sBody = jsonObject.getString("body");
//                    String sSaved = jsonObject.getString("saved");
//                    PostDataModel postDataModel = new PostDataModel(sUser,sUsername,sPostId,"",sSaved,2);
//                    postList.add(postDataModel);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            adapter = new SavedPostAdapter(getActivity(),postList);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//            recViewSavedPost.setLayoutManager(layoutManager);
//            recViewSavedPost.setAdapter(adapter);
//        }
//    };
//
//    Response.ErrorListener onGetError = new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
//        }
//    };


}
