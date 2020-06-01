package com.example.lishamanandhar.miniproject.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.adapters.CommentRecViewAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.datamodels.CommentDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentClick;
import com.example.lishamanandhar.miniproject.json_classes.Comment;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lisha Manandhar on 10/30/2017.
 */

public class CommentFrag extends Fragment implements OnCommentClick  {

    MyApplication myApp;
    SessionManager sessionManager;
    RecyclerView commentRecView;
    CommentRecViewAdapter commentRecViewAdapter;
    SwipeRefreshLayout comSwipeLay;
    String id;
    String commentUrl = "http://172.16.4.246:8000/comment/post/";
    String postCommentUrl="http://172.16.4.246:8000/comment/";





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        commentUrl = commentUrl+id+"/";
        myApp = MyApplication.getInstance();
        sessionManager = new SessionManager(getActivity());




    }

    public void getCommentService(){
        comSwipeLay.setRefreshing(false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(commentUrl,onGetSuccess,onGetError);
        myApp.getRequestQueue().add(jsonArrayRequest);
    }

    Response.Listener<JSONArray> onGetSuccess = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {

            ArrayList<CommentDataModel> dataList = new ArrayList<>();
            CommentDataModel commentDataModel1 = new CommentDataModel("","","",CommentDataModel.COMMENT_TYPE);
            dataList.add(commentDataModel1);

            for(int i=0;i<response.length();i++){

                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String user = jsonObject.getString("user");
                    String username = jsonObject.getString("username");
                    String post= jsonObject.getString("post");
                    String data = jsonObject.getString("content");

                    Log.i("cuser",user);
                    Log.i("cusername",username);
                    Log.i("post",post);
                    Log.i("data",data);

                    CommentDataModel commentDataModel2 = new CommentDataModel(user,username,data,CommentDataModel.COMMENT_LIST_TYPE);
                    dataList.add(commentDataModel2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            commentRecViewAdapter = new CommentRecViewAdapter(CommentFrag.this,getActivity(),dataList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            commentRecView.setLayoutManager(layoutManager);
            commentRecView.setAdapter(commentRecViewAdapter);


        }
    };

    Response.ErrorListener onGetError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comSwipeLay = (SwipeRefreshLayout) view.findViewById(R.id.comswipelay);
        comSwipeLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentService();
            }
        });
        ArrayList<CommentDataModel> dataList = new ArrayList<>();
        CommentDataModel comDataModel = new CommentDataModel("","","",CommentDataModel.COMMENT_TYPE);
        dataList.add(comDataModel);
        commentRecView = (RecyclerView) view.findViewById(R.id.commentrecview);
        commentRecViewAdapter = new CommentRecViewAdapter(CommentFrag.this,getActivity(),dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        commentRecView.setLayoutManager(layoutManager);
        commentRecView.setAdapter(commentRecViewAdapter);
        getCommentService();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_comment,container,false);
        return v;
    }


    @Override
    public void onClickedComment(String comment) {
        startPostCommentService(comment);

    }

        public void startPostCommentService(final String comment){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,postCommentUrl,onPostCommentSuccess,onPostCommentError){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user",sessionManager.getUser());
                params.put("post",id);
                params.put("content",comment);
                return params;
            }
        };

        myApp.getRequestQueue().add(stringRequest);
    }

    Response.Listener<String> onPostCommentSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          Log.i("CommentResponse",response);
            getCommentService();
        }
    };

    Response.ErrorListener onPostCommentError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
          Log.i("commenterror",error.toString());
        }
    };
}
