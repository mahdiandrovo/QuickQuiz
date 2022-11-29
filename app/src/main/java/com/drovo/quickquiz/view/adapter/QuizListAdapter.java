package com.drovo.quickquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drovo.quickquiz.R;
import com.drovo.quickquiz.service.remoteDB.model.QuizListModel;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {

    private List<QuizListModel> quizList;

    public void setQuizList(List<QuizListModel> quizList) {
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_topic, parent, false);
        return new QuizListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListViewHolder holder, int position) {
        QuizListModel model = quizList.get(position);
        holder.textView_quizTitle.setText(model.getTitle());
        Glide.with(holder.itemView).load(model.getImage()).into(holder.imageView_quizTtileImage);
    }

    @Override
    public int getItemCount() {
        if (quizList == null){
            return 0;
        }
        return quizList.size();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder{

        private TextView textView_quizTitle;
        private ImageView imageView_quizTtileImage;

        public QuizListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_quizTitle = (TextView) itemView.findViewById(R.id.textView_quizTitle);
            imageView_quizTtileImage = (ImageView) itemView.findViewById(R.id.imageView_quizTitleImage);
        }
    }

}
