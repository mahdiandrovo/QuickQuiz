package com.drovo.quickquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drovo.quickquiz.R;
import com.drovo.quickquiz.service.remoteDB.model.QuizListModel;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {

    private List<QuizListModel> quizList;
    private OnItemClickedListener onItemClickedListener;

    public void setQuizList(List<QuizListModel> quizList) {
        this.quizList = quizList;
    }

    public QuizListAdapter(OnItemClickedListener onItemClickedListener){
        this.onItemClickedListener = onItemClickedListener;
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

    public class QuizListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView_quizTitle;
        private ImageView imageView_quizTtileImage;
        private ConstraintLayout constraintLayout_quizTopic;

        public QuizListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_quizTitle = (TextView) itemView.findViewById(R.id.textView_quizTitle);
            imageView_quizTtileImage = (ImageView) itemView.findViewById(R.id.imageView_quizTitleImage);
            constraintLayout_quizTopic = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_quizTopic);
            constraintLayout_quizTopic.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener{
        void onItemClicked(int position);
    }

}
