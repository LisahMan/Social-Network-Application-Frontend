package com.example.lishamanandhar.miniproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.datamodels.UserDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentsClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostSavedClicked;
import com.example.lishamanandhar.miniproject.interfaces.OnPostUnsavedClicked;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 10/29/2017.
 */

public class MainRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<PostDataModel> postDataList;
    OnPostClick onPostClick;
    OnCommentsClick onCommentsClick;
    OnPostSavedClicked onPostSavedClicked;
    OnPostUnsavedClicked onPostUnsavedClicked;

    public MainRecViewAdapter(Context context,ArrayList<PostDataModel> postDataList){
        this.context = context;
        this.postDataList = postDataList;
        onPostClick = (OnPostClick) context;
        onCommentsClick = (OnCommentsClick)context;
        onPostSavedClicked = (OnPostSavedClicked) context;
        onPostUnsavedClicked = (OnPostUnsavedClicked) context;
    }

    public MainRecViewAdapter(OnCommentsClick listener ,Context context,ArrayList<PostDataModel> postDataList){
        this.context = context;
        this.postDataList = postDataList;
        onCommentsClick = (OnCommentsClick) listener;
        onPostSavedClicked = (OnPostSavedClicked) listener;
        onPostUnsavedClicked = (OnPostUnsavedClicked) listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            View v = LayoutInflater.from(context).inflate(R.layout.posting_rec_tiem,parent,false);
            return new PostViewHolder(v);
        }
        else if(viewType==1){
            View v = LayoutInflater.from(context).inflate(R.layout.post_list_rec_item,parent,false);
            return new PostListViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof PostViewHolder){


            ((PostViewHolder) holder).btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   String body = ((PostViewHolder) holder).etPost.getText().toString();

                    if (body.isEmpty()) {
                        ((PostViewHolder) holder).etPost.setError("Post is empty");
                    } else {
                        onPostClick.onClickPost(body);
                        ((PostViewHolder) holder).etPost.setText("");
                        ((PostViewHolder) holder).etPost.setHint("Write your post!");
                    }

                }


            });

        }
        else if(holder instanceof PostListViewHolder){
            ((PostListViewHolder) holder).txtPostUsername.setText(postDataList.get(position).getUsername());
            ((PostListViewHolder) holder).txtPost.setText(postDataList.get(position).getData());
            ((PostListViewHolder) holder).btnComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     onCommentsClick.OnClickComments(postDataList.get(position).getId());
                }
            });

            if(postDataList.get(position).getSaved().contains("1")){
                ((PostListViewHolder) holder).btnunsaved.setVisibility(View.VISIBLE);
            }else if(postDataList.get(position).getSaved().contains("0")){
                ((PostListViewHolder) holder).btnsaved.setVisibility(View.VISIBLE);
            }

            ((PostListViewHolder) holder).btnsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postId = postDataList.get(position).getId();
                    PostDataModel postDataModel = postDataList.get(position);
                    onPostSavedClicked.onSavedPostClicked(postDataModel);
                    postDataList.get(position).setSaved("1");
                    ((PostListViewHolder) holder).btnsaved.setVisibility(View.INVISIBLE);
                    ((PostListViewHolder) holder).btnunsaved.setVisibility(View.VISIBLE);
                }
            });
            ((PostListViewHolder) holder).btnunsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String postId = postDataList.get(position).getId();
                    onPostUnsavedClicked.onUnsavedPostClicked(postId);
                    postDataList.get(position).setSaved("0");
                    ((PostListViewHolder) holder).btnunsaved.setVisibility(View.INVISIBLE);
                    ((PostListViewHolder) holder).btnsaved.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return postDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return postDataList.get(position).getType();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
         EditText etPost;
         Button btnPost;
         public PostViewHolder(View itemView) {
             super(itemView);
             etPost = (EditText) itemView.findViewById(R.id.etpost);
             btnPost = (Button) itemView.findViewById(R.id.btnpost);
         }
     }

     public class PostListViewHolder extends RecyclerView.ViewHolder{
         TextView txtPostUsername , txtPost;
         Button btnComments;
         ImageButton btnsaved , btnunsaved;
         public PostListViewHolder(View itemView) {
             super(itemView);
             txtPostUsername = (TextView) itemView.findViewById(R.id.txtpostusername);
             txtPost = (TextView) itemView.findViewById(R.id.txtpost);
             btnComments = (Button) itemView.findViewById(R.id.btncomments);
             btnsaved = (ImageButton) itemView.findViewById(R.id.btnsaved);
             btnunsaved = (ImageButton) itemView.findViewById(R.id.btnunsaved);
         }
     }
}
