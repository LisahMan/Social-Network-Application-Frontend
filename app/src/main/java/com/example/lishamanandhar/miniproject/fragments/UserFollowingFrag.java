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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.adapters.SearchRecViewAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.datamodels.UserDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnFollowClick;
import com.example.lishamanandhar.miniproject.interfaces.OnUnfollowClick;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lisha Manandhar on 11/12/2017.
 */

public class UserFollowingFrag extends Fragment implements OnFollowClick,OnUnfollowClick {
    RecyclerView recViewFollwing;
    MyApplication myApp;
    SessionManager sessionManager;
    String getFollowingUrl = "http://10.0.2.2:8000/user/following/";
    String user;
    SearchRecViewAdapter adapter;
    String socialCreateUrl = "http://10.0.2.2:8000/social/create/";
    String socialDeleteUrl = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = MyApplication.getInstance();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUser();
        getFollowingUrl=getFollowingUrl+user+"/";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_following,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recViewFollwing = (RecyclerView) view.findViewById(R.id.recviewfollowing);
        startGetFollowingService();
    }

    public void startGetFollowingService(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getFollowingUrl,onGetFollowingSuccess,onGetFollowingError);
        myApp.getRequestQueue().add(jsonArrayRequest);
    }

    Response.Listener<JSONArray> onGetFollowingSuccess = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<UserDataModel> userDataList = new ArrayList<>();
            try {
                for(int i=0;i<response.length();i++){
                    JSONObject jsonObject = response.getJSONObject(i);
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

            adapter = new SearchRecViewAdapter(UserFollowingFrag.this,getActivity(),userDataList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewFollwing.setLayoutManager(layoutManager);
            recViewFollwing.setAdapter(adapter);
        }
    };

    Response.ErrorListener onGetFollowingError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClickFollow(String following) {
        startSocialCreateService(following);
    } public void startSocialCreateService(final String following){
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewFollwing.setLayoutManager(layoutManager);
            recViewFollwing.setAdapter(adapter);

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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recViewFollwing.setLayoutManager(layoutManager);
            recViewFollwing.setAdapter(adapter);

        }
    };

    Response.ErrorListener onSocialDeleteError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };
}
