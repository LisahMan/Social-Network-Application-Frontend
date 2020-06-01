package com.example.lishamanandhar.miniproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentsClick;
import com.example.lishamanandhar.miniproject.interfaces.OnPostDeleteClick;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.MyViewHolder> {

    Context context;
    ArrayList<PostDataModel> postList;
    OnPostDeleteClick onPostDeleteClick;
    OnCommentsClick onCommentsClick;

    public UserPostAdapter(OnPostDeleteClick listener , Context context , ArrayList<PostDataModel> postList){
        this.onPostDeleteClick = listener;
        this.onCommentsClick = (OnCommentsClick) listener;
        this.context = context;
        this.postList = postList;
    }


    @Override
    public UserPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_post_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserPostAdapter.MyViewHolder holder, final int position) {
        holder.txtUserPostUsername.setText(postList.get(position).getUsername());
        holder.txtUserPost.setText(postList.get(position).getData());
        holder.btnUserPostComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentsClick.OnClickComments(postList.get(position).getId());
            }
        });

        holder.btnUserPostDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostDeleteClick.onClickPostDelete(postList.get(position).getId(),position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtUserPostUsername , txtUserPost;
        public Button btnUserPostComments;
        public ImageButton btnUserPostDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtUserPostUsername = (TextView) itemView.findViewById(R.id.txtuserpostusername);
            txtUserPost = (TextView) itemView.findViewById(R.id.txtuserpost);
            btnUserPostComments = (Button) itemView.findViewById(R.id.btnuserpostcomments);
            btnUserPostDelete = (ImageButton) itemView.findViewById(R.id.btnuserpostdelete);
        }
    }

    public void postRemove(int position){
        postList.remove(position);
        notifyDataSetChanged();
    }
}
