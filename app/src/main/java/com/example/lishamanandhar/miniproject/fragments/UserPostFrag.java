package com.example.lishamanandhar.miniproject.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.adapters.UserPostAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.database.DatabaseManager;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentsClick;
import com.example.lishamanandhar.miniproject.interfaces.OnDeleteClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostDeleteClick;
import com.example.lishamanandhar.miniproject.json_classes.Comment;
import com.example.lishamanandhar.miniproject.json_classes.Feed;
import com.example.lishamanandhar.miniproject.json_classes.Post;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/12/2017.
 */

public class UserPostFrag extends Fragment implements OnCommentsClick , OnPostDeleteClick {

    MyApplication myApp;
    SessionManager sessionManager;
    RecyclerView userPostRecView;
    String getUserPostUrl = "http://10.0.2.2:8000/post/user/";
//    String postSaveUrl = "http://10.0.2.2:8000/post/save/";
//    String postUnsaveUrl = "";
    String deleteUserPost = "";
    String user;
    UserPostAdapter adapter;
    ArrayList<PostDataModel> postDataModelList;
    Bundle bundle;
    DatabaseManager dbm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = MyApplication.getInstance();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUser();
        getUserPostUrl = getUserPostUrl+user+"/";
        dbm  = DatabaseManager.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.frag_user_post,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPostRecView = (RecyclerView) view.findViewById(R.id.recviewuserpost);
        getUserPostService();
    }

    public void getUserPostService(){
        postDataModelList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getUserPostUrl,onGetUserPostSuccess,onGetUserPostError);
        myApp.getRequestQueue().add(jsonArrayRequest);
    }
    Response.Listener<JSONArray> onGetUserPostSuccess = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {

            ArrayList<Post> postList  = new ArrayList<>();
            Feed feed = new Feed();

            for (int i=0;i<response.length();i++){

                try {

                    Post post = new Post();
                    ArrayList<Comment> comList = new ArrayList<>();

                    JSONObject jsonObject = response.getJSONObject(i);
                    String pUser = jsonObject.getString("user");
                    String pUsername = jsonObject.getString("username");
                    String pId= jsonObject.getString("id");
                    String pBody = jsonObject.getString("body");
                    String pSaved = jsonObject.getString("saved");
                    post.setUser(jsonObject.getString("user"));
                    post.setUsername(jsonObject.getString("username"));
                    post.setBody(jsonObject.getString("body"));
                    post.setDate(jsonObject.getString("date"));

                    PostDataModel postDataModel = new PostDataModel(pUser,pUsername,pId,pBody,pSaved,PostDataModel.POST_LIST_TYPE);
                    postDataModelList.add(postDataModel);

                    JSONArray jsonArray = jsonObject.getJSONArray("comments");

                    for(int j=0;j<jsonArray.length();j++){
                        Comment comment = new Comment();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                        String cuser = jsonObject1.getString("user");
                        String cusername = jsonObject1.getString("username");
                        String content = jsonObject1.getString("content");

                        comment.setUser(jsonObject1.getString("user"));
                        comment.setUsername(jsonObject1.getString("username"));
                        comment.setPost(jsonObject1.getString("post"));
                        comment.setContent(jsonObject1.getString("content"));

                        comList.add(comment);
                    }

                    post.setComList(comList);
                    postList.add(post);
                    feed.setPostList(postList);



                    adapter = new UserPostAdapter(UserPostFrag.this,getActivity(),postDataModelList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    userPostRecView.setLayoutManager(layoutManager);
                    userPostRecView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }
    };

    Response.ErrorListener onGetUserPostError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void OnClickComments(String id) {
        Log.i("userPostComments","clicked");
        Log.i("id",id);
        bundle = new Bundle();
        userPostRecView.setVisibility(View.GONE);
        Log.i("Button","Clicked");
        bundle.putString("id",id);
        CommentFrag commentFrag = new CommentFrag();
        commentFrag.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.replacelay,commentFrag,"CommentFrag").addToBackStack(null).commit();
    }


    @Override
    public void onClickPostDelete(String postId,int position) {
        Bundle bundle = new Bundle();
        bundle.putString("postId",postId);
        bundle.putInt("position",position);
        PostDeleteDiFrag dialog = new PostDeleteDiFrag();
        dialog.setArguments(bundle);
        dialog.setTargetFragment(UserPostFrag.this,1);
        dialog.show(getFragmentManager(),"deleteDialog");
    }



//    @Override
//    public void onSavedPostClicked(final PostDataModel postDataModel) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, postSaveUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dbm.savePost(postDataModel);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("user" ,user);
//                params.put("post",postDataModel.getId());
//                return params;
//            }
//        };
//        myApp.getRequestQueue().add(stringRequest);
//
//    }
//
//
//
//    @Override
//    public void onUnsavedPostClicked(final String postId) {
//        postUnsaveUrl = "http://10.0.2.2:8000/post/unsave/";
//        postUnsaveUrl = postUnsaveUrl+user+"/"+postId+"/";
//        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postUnsaveUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dbm.unSavePost(postId);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
//            }
//        });
//        myApp.getRequestQueue().add(stringRequest);
//
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            String postId = bundle.getString("postId");
            Integer postion = bundle.getInt("position");
            Log.i("dPostId",postId);
            Log.i("position",postion.toString());

            deleteUserPost = "http://10.0.2.2:8000/post/";
            deleteUserPost = deleteUserPost+postId+"/";
            startDeletePostService(postion);
        }
    }

    void startDeletePostService(final int position){
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUserPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("delete","success");
                adapter.postRemove(position);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.i("onError","error");
            }
        });

        myApp.getRequestQueue().add(stringRequest);

    }

}
