package com.example.lishamanandhar.miniproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lishamanandhar.miniproject.MainActivity;
import com.example.lishamanandhar.miniproject.R;
import com.example.lishamanandhar.miniproject.datamodels.CommentDataModel;
import com.example.lishamanandhar.miniproject.fragments.CommentFrag;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentClick;
import com.example.lishamanandhar.miniproject.interfaces.OnCommentsClick;
import com.example.lishamanandhar.miniproject.json_classes.Post;

import java.util.ArrayList;

/**
 * Created by Lisha Manandhar on 10/29/2017.
 */

public class CommentRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CommentDataModel> comDataList;
    OnCommentClick onCommentClick;


    public CommentRecViewAdapter(OnCommentClick listener,Context context,ArrayList<CommentDataModel> comDataList){
        this.context = context;
        this.comDataList = comDataList;
        onCommentClick = (OnCommentClick) listener;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==2){
            View v = LayoutInflater.from(context).inflate(R.layout.commenting_rec_item,parent,false);
            return new CommentViewHolder(v);
        }
        else if(viewType==3){
            View v = LayoutInflater.from(context).inflate(R.layout.comment_list_rec_item,parent,false);
            return new CommentListViewHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof CommentViewHolder){

            ((CommentViewHolder) holder).btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String comment = ((CommentViewHolder) holder).etComment.getText().toString();
                    if(comment.isEmpty()){
                        ((CommentViewHolder) holder).etComment.setError("Comment is empty");
                    }else{

                        onCommentClick.onClickedComment(comment);
                        ((CommentViewHolder) holder).etComment.setText("");
                        ((CommentViewHolder) holder).etComment.setHint("Write your comment!");
                    }

                }
            });

        }
        else if(holder instanceof CommentListViewHolder){
            ((CommentListViewHolder) holder).txtCommentUsername.setText(comDataList.get(position).getUsername());
            ((CommentListViewHolder) holder).txtComment.setText(comDataList.get(position).getData());
        }

    }

    @Override
    public int getItemCount() {
        return comDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return comDataList.get(position).getType();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        EditText etComment;
        Button btnComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            etComment = (EditText) itemView.findViewById(R.id.etcomment);
            btnComment = (Button) itemView.findViewById(R.id.btncomment);
        }
    }

    public class CommentListViewHolder extends RecyclerView.ViewHolder{

        TextView txtCommentUsername,txtComment;
        public CommentListViewHolder(View itemView) {
            super(itemView);
            txtCommentUsername = (TextView) itemView.findViewById(R.id.txtcommentusername);
            txtComment = (TextView) itemView.findViewById(R.id.txtcomment);
        }
    }
}
