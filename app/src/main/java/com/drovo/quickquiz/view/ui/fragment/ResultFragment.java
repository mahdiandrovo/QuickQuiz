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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drovo.quickquiz.R;
import com.drovo.quickquiz.viewmodel.QuestionViewModel;

import java.util.HashMap;

public class ResultFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private QuestionViewModel viewModel;

    private ProgressBar progressBar_AnswerPercentage;

    private TextView textView_AnswerPercentage;
    private TextView textView_CorrectAnswer;
    private TextView textView_WrongAnswer;
    private TextView textView_QuestionMissed;

    private Button button_GoToHome;

    private String quizId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        progressBar_AnswerPercentage = (ProgressBar) view.findViewById(R.id.progressBar_answerPercentage);

        textView_AnswerPercentage = (TextView) view.findViewById(R.id.textView_answerPercentage);
        textView_CorrectAnswer = (TextView) view.findViewById(R.id.textView_correctAnswer);
        textView_WrongAnswer = (TextView) view.findViewById(R.id.textView_wrongAnswer);
        textView_QuestionMissed = (TextView) view.findViewById(R.id.textView_questionMissed);

        button_GoToHome = (Button) view.findViewById(R.id.button_goToHome);
        button_GoToHome.setOnClickListener(this);

        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();
        viewModel.setQuizId(quizId);

        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                Long correctAnswer = stringLongHashMap.get("correct");
                Long wrongAnswer = stringLongHashMap.get("wrong");
                Long notAnswered = stringLongHashMap.get("notAnswered");

                textView_CorrectAnswer.setText(correctAnswer.toString());
                textView_WrongAnswer.setText(wrongAnswer.toString());
                textView_QuestionMissed.setText(notAnswered.toString());

                Long totalQuestions = correctAnswer+wrongAnswer+notAnswered;
                Long percent = (correctAnswer*100)/totalQuestions;

                textView_AnswerPercentage.setText(String.valueOf(percent));

                progressBar_AnswerPercentage.setProgress(percent.intValue());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == button_GoToHome){
            navController.navigate(R.id.action_resultFragment_to_listFragment);
        }
    }
}