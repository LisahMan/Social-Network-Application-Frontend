package com.example.lishamanandhar.miniproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.datamodels.PostDataModel;
import com.example.lishamanandhar.miniproject.interfaces.OnPostSavedClicked;
import com.example.lishamanandhar.miniproject.interfaces.OnPostUnsavedClicked;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/22/2017.
 */

public class SavedPostAdapter extends RecyclerView.Adapter<SavedPostAdapter.SavedPostViewHolder> {

    Context context;
    ArrayList<PostDataModel> postList;

    public SavedPostAdapter(Context context,ArrayList<PostDataModel> postList){
        this.context = context;
        this.postList = postList;
    }

    @Override
    public SavedPostAdapter.SavedPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.saved_post_list_rec_item,parent,false);
        return new SavedPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SavedPostAdapter.SavedPostViewHolder holder, final int position) {
       holder.txtSavedUserPostUsername.setText(postList.get(position).getUsername());
       holder.txtSavedPost.setText(postList.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class SavedPostViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSavedUserPostUsername , txtSavedPost;
        public SavedPostViewHolder(View itemView) {
            super(itemView);
            txtSavedUserPostUsername = (TextView) itemView.findViewById(R.id.txtsavedpostusername);
            txtSavedPost = (TextView) itemView.findViewById(R.id.txtsavedpost);
        }

    }
}
