package com.drovo.quickquiz.view.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drovo.quickquiz.R;
import com.drovo.quickquiz.service.remoteDB.model.QuizListModel;
import com.drovo.quickquiz.viewmodel.QuizListViewModel;


import java.util.List;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private QuizListViewModel viewModel;

    private ProgressBar progressBar_DetailsLoadingProgress;

    private ImageView imageView_TopicImage;

    private TextView textView_Title;
    private TextView textView_Difficulty;
    private TextView textView_TotalQuestion;

    private Button button_StartQuiz;

    private int position;
    private String quizId;
    private int totalQuestions;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView_TopicImage = (ImageView) view.findViewById(R.id.imageView_titleImage);

        progressBar_DetailsLoadingProgress = (ProgressBar) view.findViewById(R.id.progressBar_detalsLoadProgress);

        textView_Title = (TextView) view.findViewById(R.id.textView_title);
        textView_Difficulty = (TextView) view.findViewById(R.id.textView_difficulty);
        textView_TotalQuestion = (TextView) view.findViewById(R.id.textView_totalQuestions);

        button_StartQuiz = (Button) view.findViewById(R.id.button_startQuiz);
        button_StartQuiz.setOnClickListener(this);

        navController = Navigation.findNavController(view);

        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quizListModel = quizListModels.get(position);
                Glide.with(view).load(quizListModel.getImage()).into(imageView_TopicImage);
                textView_Title.setText(quizListModel.getTitle());
                textView_TotalQuestion.setText(String.valueOf(quizListModel.getQuestions()));
                textView_Difficulty.setText(quizListModel.getDifficulty());
                
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar_DetailsLoadingProgress.setVisibility(View.GONE);
                    }
                }, 2000);
                quizId = quizListModel.getQuizId();
                totalQuestions = quizListModel.getQuestions();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == button_StartQuiz){
            DetailFragmentDirections.ActionDetailFragmentToQuizFragment action =
                    DetailFragmentDirections.actionDetailFragmentToQuizFragment();
            action.setQuizId(quizId);
            action.setTotalQuestionCount(totalQuestions);
            navController.navigate(action);
        }
    }
}