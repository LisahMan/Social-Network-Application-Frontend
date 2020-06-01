package com.example.lishamanandhar.miniproject.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.adapters.SearchRecViewAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.datamodels.UserDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnFollowClick;
import com.example.lishamanandhar.miniproject.interfaces.OnSearchClick;
import com.example.lishamanandhar.miniproject.interfaces.OnUnfollowClick;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lisha Manandhar on 11/4/2017.
 */

public class SearchFrag extends Fragment implements OnFollowClick ,OnUnfollowClick{

    EditText etSearch;
    Button btnSearch;
    MyApplication myApp;
    SessionManager sessionManager;
    String user;
    String username;

    String searchUrl = "http://10.0.2.2:8000/user/search/";
    String socialCreateUrl = "http://10.0.2.2:8000/social/create/";
    String socialDeleteUrl="";

    SearchRecViewAdapter searchRecViewAdapter;
    RecyclerView recViewSearch;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = MyApplication.getInstance();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUser();
        searchUrl = searchUrl+user+"/";


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_search,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = (EditText) view.findViewById(R.id.etsearch);
        btnSearch = (Button) view.findViewById(R.id.btnsearch);
        recViewSearch = (RecyclerView) view.findViewById(R.id.recviewsearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etSearch.getText().toString();
                Log.i("susername",username);
                startSearchService(username);


            }
        });
    }
    public void startSearchService(final String username){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,searchUrl,onSearchSuccess,onSearchError){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                return params;
            }
        };
        myApp.getRequestQueue().add(stringRequest);
    }

    Response.Listener<String> onSearchSuccess = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.i("sresponse",response);
            ArrayList<UserDataModel> userDataList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String sUser = jsonObject.getString("id");
                    String sUsername = jsonObject.getString("username");
                    String sFollowField = jsonObject.getString("email");
                    Log.i("sUser",sUser);
                    Log.i("sUsername",sUsername);
                    Log.i("sFollowField",sFollowField);

                    UserDataModel userDataModel = new UserDataModel(sUser,sUsername);
                    userDataModel.setFollowField(sFollowField);
                    userDataList.add(userDataModel);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
             Log.i("usersize", String.valueOf(userDataList.size()));
            searchRecViewAdapter = new SearchRecViewAdapter(SearchFrag.this,getActivity(),userDataList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewSearch.setLayoutManager(layoutManager);
            recViewSearch.setAdapter(searchRecViewAdapter);

        }
    };

    Response.ErrorListener onSearchError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),"No User Found",Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClickFollow(String following) {
        Log.i("onFollow","onFollowclicked");
        Log.i("cfollower",user);
        Log.i("cfollowing",following);
        startSocialCreateService(following);

    }

    public void startSocialCreateService(final String following){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,socialCreateUrl,onSocialCreateSuccess,onSocialCreateError){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("follower",user);
                params.put("following",following);
                return params;
            }
        };
        myApp.getRequestQueue().add(stringRequest);
    }

    Response.Listener<String> onSocialCreateSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            startSearchService(username);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewSearch.setLayoutManager(layoutManager);
            recViewSearch.setAdapter(searchRecViewAdapter);
        }
    };

    Response.ErrorListener onSocialCreateError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
             Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClickUnfollow(String following) {
        Log.i("onUnfollow","onUnfollowedClicked");
        socialDeleteUrl = "http://10.0.2.2:8000/social/delete/";
        socialDeleteUrl = socialDeleteUrl+user+"/"+following+"/";
        startSocialDeleteService();
    }

    public void startSocialDeleteService(){
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,socialDeleteUrl,onSocialDeleteSuccess,onSocialDeleteError);
        myApp.getRequestQueue().add(stringRequest);
    }

    Response.Listener<String> onSocialDeleteSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            startSearchService(username);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewSearch.setLayoutManager(layoutManager);
            recViewSearch.setAdapter(searchRecViewAdapter);
        }
    };

    Response.ErrorListener onSocialDeleteError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };


}
