package com.example.lishamanandhar.miniproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.datamodels.UserDataModel;
import com.example.lishamanandhar.miniproject.fragments.SearchFrag;
import com.example.lishamanandhar.miniproject.interfaces.OnFollowClick;
import com.example.lishamanandhar.miniproject.interfaces.OnUnfollowClick;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 11/4/2017.
 */

public class SearchRecViewAdapter extends RecyclerView.Adapter<SearchRecViewAdapter.SearchViewHolder> {

    Context context;
    ArrayList<UserDataModel> userDataList;
   OnFollowClick onFollowClick;
   OnUnfollowClick onUnfollowClick;


    public SearchRecViewAdapter(OnFollowClick listener, Context context, ArrayList<UserDataModel> userDataList){
        Log.i("searchadapter","created");
        this.context = context;
        this.userDataList = userDataList;
        this.onFollowClick = (OnFollowClick) listener;
        this.onUnfollowClick = (OnUnfollowClick) listener;

    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_list,parent,false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        Log.i("onBindusername",userDataList.get(position).getUsername());
        holder.txtSearchName.setText(userDataList.get(position).getUsername());


        if(userDataList.get(position).getFollowField().contains("1")) {
            holder.btnUnfollow.setVisibility(View.VISIBLE);
        }
        else if (userDataList.get(position).getFollowField().contains("0")){
            holder.btnFollow.setVisibility(View.VISIBLE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String following = userDataList.get(position).getId();
                onFollowClick.onClickFollow(following);
                userDataList.get(position).setFollowField("1");
                holder.btnFollow.setVisibility(View.INVISIBLE);
                holder.btnUnfollow.setVisibility(View.VISIBLE);
            }
        });

        holder.btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String following = userDataList.get(position).getId();
                onUnfollowClick.onClickUnfollow(following);
                userDataList.get(position).setFollowField("0");
                holder.btnUnfollow.setVisibility(View.INVISIBLE);
                holder.btnFollow.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("size", String.valueOf(userDataList.size()));
        return userDataList.size();

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSearchName;
        public ImageButton btnFollow , btnUnfollow;
        public SearchViewHolder(View itemView) {
            super(itemView);
            txtSearchName = (TextView) itemView.findViewById(R.id.txtsearchname);
            btnFollow = (ImageButton) itemView.findViewById(R.id.btnfollow);
            btnUnfollow = (ImageButton) itemView.findViewById(R.id.btnunfollow);
        }
    }


}
