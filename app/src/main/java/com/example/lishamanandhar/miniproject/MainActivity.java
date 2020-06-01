package com.example.lishamanandhar.miniproject;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.lishamanandhar.miniproject.adapters.MainRecViewAdapter;
import com.example.lishamanandhar.miniproject.adapters.SearchRecViewAdapter;
import com.example.lishamanandhar.miniproject.application.MyApplication;
import com.example.lishamanandhar.miniproject.database.DatabaseManager;
import com.example.lishamanandhar.miniproject.datamodels.CommentDataModel;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.datamodels.UserDataModel;
import com.example.lishamanandhar.miniproject.fragments.CommentFrag;
import com.example.lishamanandhar.miniproject.fragments.LogOutDiFrag;
import com.example.lishamanandhar.miniproject.fragments.SearchFrag;
import com.example.lishamanandhar.miniproject.fragments.UserFollowingFrag;
import com.example.lishamanandhar.miniproject.fragments.UserPostFrag;
import com.example.lishamanandhar.miniproject.fragments.UserSavedPostFrag;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentClick;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentsClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostSavedClicked;
import com.example.lishamanandhar.miniproject.interfaces.OnPostUnsavedClicked;
import com.example.lishamanandhar.miniproject.interfaces.OnSearchClick;
import com.example.lishamanandhar.miniproject.json_classes.Comment;
import com.example.lishamanandhar.miniproject.json_classes.Feed;
import com.example.lishamanandhar.miniproject.json_classes.Post;
import com.example.lishamanandhar.miniproject.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnPostClick , OnCommentsClick , OnPostSavedClicked , OnPostUnsavedClicked{


    MyApplication myApp;
    MainRecViewAdapter adapter;
    RecyclerView mainRecView;
    ArrayList<PostDataModel> postDataModelList;
    String getUserFeedUrl = "http://172.16.4.246:8000/post/userfeed/";
    String postUrl = "http://172.16.4.246:8000/post/";
    String postSaveUrl = "http://172.16.4.246:8000/post/save/";
    String postUnsaveUrl = "";
    SessionManager sessionManager;
    String user;
    FrameLayout replaceLay;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseManager dbm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApp = MyApplication.getInstance();
        replaceLay = (FrameLayout) findViewById(R.id.replacelay);
        sessionManager = new SessionManager(getApplicationContext());
        dbm = DatabaseManager.getInstance(MainActivity.this);
        Log.i("mainuser",sessionManager.getUser());
        Log.i("mainusername",sessionManager.getUsername());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserFeedService();
            }
        });
        user = sessionManager.getUser();
        getUserFeedUrl = getUserFeedUrl+user+"/";
        Log.i("url",getUserFeedUrl);
        mainRecView = (RecyclerView) findViewById(R.id.mainrecview);
        postDataModelList = new ArrayList<>();

        PostDataModel postDataModel = new PostDataModel("","","","","",PostDataModel.POST_TYPE);
        postDataModelList.add(postDataModel);
        adapter = new MainRecViewAdapter(MainActivity.this,postDataModelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mainRecView.setLayoutManager(layoutManager);
        mainRecView.setAdapter(adapter);

        getUserFeedService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                mainRecView.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.replacelay,new SearchFrag(),"SearchFrag").addToBackStack(null).commit();
                break;
            case R.id.userpost:
                mainRecView.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.replacelay,new UserPostFrag()).addToBackStack(null).commit();
                break;
            case R.id.userfollowing:
                mainRecView.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.replacelay,new UserFollowingFrag()).addToBackStack(null).commit();
                break;
            case R.id.userSavedPost:
                mainRecView.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.replacelay,new UserSavedPostFrag()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                LogOutDiFrag dialog = new LogOutDiFrag();
                dialog.show(getFragmentManager(),"dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Getting Userfeed
    private void getUserFeedService(){
        swipeRefreshLayout.setRefreshing(false);
        postDataModelList = new ArrayList<>();

        PostDataModel postDataModel = new PostDataModel("","","","","",PostDataModel.POST_TYPE);
        postDataModelList.add(postDataModel);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getUserFeedUrl,onGetFeedSuccess,onGetFeedError);
        myApp.getRequestQueue().add(jsonArrayRequest);
    }


    Response.Listener<JSONArray> onGetFeedSuccess = new Response.Listener<JSONArray>() {
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

                    Log.i("saved",pSaved);


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



                    adapter = new MainRecViewAdapter(MainActivity.this,postDataModelList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    mainRecView.setLayoutManager(layoutManager);
                    mainRecView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }
    };

    Response.ErrorListener onGetFeedError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
         Toast.makeText(MainActivity.this,"Cannot connect to internet!",Toast.LENGTH_LONG).show();
        }
    };

    //Post Operations
    @Override
    public void onClickPost(String body) {
        postService(body);
    }

    public void postService(final String body){

         StringRequest stringRequest = new StringRequest(Request.Method.POST,postUrl,onPostSuccess,onPostError){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                Log.i("body",body);
                Log.i("user",sessionManager.getUser());
                params.put("user",sessionManager.getUser());
                params.put("body",body);
                return params;
            }
        };
        myApp.getRequestQueue().add(stringRequest);
    }

    Response.Listener<String> onPostSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
           Log.i("responseinfo",response);
           getUserFeedService();
        }
    };

    Response.ErrorListener onPostError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("error",error.toString());
            Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
        }
    };

    //Comment Operations

    @Override
    public void OnClickComments(String id) {
        Log.i("id",id);
        bundle = new Bundle();
        mainRecView.setVisibility(View.GONE);
        Log.i("Button","Clicked");
        bundle.putString("id",id);
        CommentFrag commentFrag = new CommentFrag();
        commentFrag.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.replacelay,commentFrag,"CommentFrag").addToBackStack(null).commit();
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        }

        else if(getFragmentManager().getBackStackEntryCount()>1) {

            getFragmentManager().popBackStack();

        }
        else{
            getUserFeedService();
            getFragmentManager().popBackStack();
            mainRecView.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onSavedPostClicked(final PostDataModel postDataModel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postSaveUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbm.savePost(postDataModel);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user" ,user);
                params.put("post",postDataModel.getId());
                return params;
            }
        };
        myApp.getRequestQueue().add(stringRequest);
    }





    @Override
    public void onUnsavedPostClicked(final String postId) {
        postUnsaveUrl = "http://10.0.2.2:8000/post/unsave/";
        postUnsaveUrl = postUnsaveUrl+user+"/"+postId+"/";
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, postUnsaveUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dbm.unSavePost(postId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        myApp.getRequestQueue().add(stringRequest);
    }


}


