package com.drovo.quickquiz.view.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drovo.quickquiz.R;
import com.drovo.quickquiz.service.remoteDB.model.QuestionModel;
import com.drovo.quickquiz.viewmodel.QuestionViewModel;
import com.drovo.quickquiz.viewmodel.QuizListViewModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private QuestionViewModel viewModel;
    private NavController navController;
    private String quizId;
    private long totalQuestions;
    private int currentQuestionNumber = 0;
    private boolean canAnswer = false;

    private long timer;
    private CountDownTimer countDownTimer;

    private int notAnswered = 0;
    private int correctAnswered = 0;
    private int wrongAnswered = 0;

    private String answer = "";

    private ImageView imageView_CloseQuiz;

    private TextView textView_QuestionNumber;

    private ProgressBar progressBar_Timer;

    private TextView textView_Timer;

    private TextView textView_Question;

    private Button button_OptionOne;
    private Button button_OptionTwo;
    private Button button_OptionThree;
    private Button button_OptionFour;

    private TextView textView_AnswerFeedback;

    private Button button_NextQuestion;


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
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        imageView_CloseQuiz = (ImageView) view.findViewById(R.id.imageView_closeQuiz);

        textView_QuestionNumber = (TextView) view.findViewById(R.id.textView_questionNumber);

        progressBar_Timer = (ProgressBar) view.findViewById(R.id.progressBar_timer);

        textView_Timer = (TextView) view.findViewById(R.id.textView_timer);

        textView_Question = (TextView) view.findViewById(R.id.textView_question);

        button_OptionOne = (Button) view.findViewById(R.id.button_optionOne);
        button_OptionTwo = (Button) view.findViewById(R.id.button_optionTwo);
        button_OptionThree = (Button) view.findViewById(R.id.button_optionThree);
        button_OptionFour = (Button) view.findViewById(R.id.button_optionFour);

        textView_AnswerFeedback = (TextView) view.findViewById(R.id.textView_answerFeedback);

        button_NextQuestion = (Button) view.findViewById(R.id.button_nextQuestion);

        //in nav fraph we passed quizId
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        viewModel.setQuizId(quizId);
        viewModel.getQuestions();

        totalQuestions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestionCount();

        imageView_CloseQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_quizFragment_to_listFragment);
            }
        });

        button_OptionOne.setOnClickListener(this);
        button_OptionTwo.setOnClickListener(this);
        button_OptionThree.setOnClickListener(this);
        button_OptionFour.setOnClickListener(this);

        button_NextQuestion.setOnClickListener(this);



        loadData();
    }

    private void loadData(){
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions(){
        button_OptionOne.setVisibility(View.VISIBLE);
        button_OptionTwo.setVisibility(View.VISIBLE);
        button_OptionThree.setVisibility(View.VISIBLE);
        button_OptionFour.setVisibility(View.VISIBLE);

        //enable buttons
        button_OptionOne.setEnabled(true);
        button_OptionTwo.setEnabled(true);
        button_OptionThree.setEnabled(true);
        button_OptionFour.setEnabled(true);

        //hide feedback
        textView_AnswerFeedback.setVisibility(View.INVISIBLE);

        //hide next q button
        button_NextQuestion.setVisibility(View.INVISIBLE);
    }

    private void loadQuestions(int i){
        currentQuestionNumber = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModelList) {
                textView_Question.setText(questionModelList.get(i-1).getQuestion());
                button_OptionOne.setText(questionModelList.get(i-1).getOption_a());
                button_OptionTwo.setText(questionModelList.get(i-1).getOption_b());
                button_OptionThree.setText(questionModelList.get(i-1).getOption_c());
                button_OptionFour.setText(questionModelList.get(i-1).getOption_d());

                answer = questionModelList.get(i-1).getAnswer();

                textView_QuestionNumber.setText(String.valueOf(currentQuestionNumber));
                timer = questionModelList.get(i-1).getTimer();

                startTimer();
            }
        });

        canAnswer = true;
    }

    private void startTimer(){
        textView_Timer.setText(String.valueOf(timer));

        progressBar_Timer.setVisibility(View.VISIBLE);

        //first parameter: total time, second parameter: time duration
        countDownTimer = new CountDownTimer(timer*1000, 1000) {
            @Override
            public void onTick(long l) {
                //update time
                textView_Timer.setText(l/1000 + "");
                Long percent = l/(timer*10);
                progressBar_Timer.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                textView_AnswerFeedback.setText("time is up. no answer selected.");
                notAnswered++;
                showNextButton();
            }
        }.start();
    }

    private void showNextButton(){
        if (currentQuestionNumber == totalQuestions){
            button_NextQuestion.setText("Submit");
            button_NextQuestion.setVisibility(View.VISIBLE);
            button_NextQuestion.setEnabled(true);
        } else {
            button_NextQuestion.setVisibility(View.VISIBLE);
            button_NextQuestion.setEnabled(true);
            textView_AnswerFeedback.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == button_OptionOne){
            verifyAnswer(button_OptionOne);
        }
        if (view == button_OptionTwo){
            verifyAnswer(button_OptionTwo);
        }
        if (view == button_OptionThree){
            verifyAnswer(button_OptionThree);
        }
        if (view == button_OptionFour){
            verifyAnswer(button_OptionFour);
        }
        if (view == button_NextQuestion){
            if (currentQuestionNumber == totalQuestions){
                submitResults();
            } else {
                currentQuestionNumber++;
                loadQuestions(currentQuestionNumber);
                resetOptions();
            }
        }
    }

    private void submitResults(){
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswered);
        resultMap.put("wrong", wrongAnswered);
        resultMap.put("notAnswered", notAnswered);

        viewModel.addResults(resultMap);

        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);
    }

    private void resetOptions(){
        textView_AnswerFeedback.setVisibility(View.INVISIBLE);

        button_NextQuestion.setVisibility(View.INVISIBLE);
        button_NextQuestion.setEnabled(false);

        button_OptionOne.setBackground(ContextCompat.getDrawable(getContext(), R.color.light_sky));
        button_OptionTwo.setBackground(ContextCompat.getDrawable(getContext(), R.color.light_sky));
        button_OptionThree.setBackground(ContextCompat.getDrawable(getContext(), R.color.light_sky));
        button_OptionFour.setBackground(ContextCompat.getDrawable(getContext(), R.color.light_sky));
    }

    private void verifyAnswer(Button button){
        if (canAnswer){
            if (answer.equals(button.getText())){
                button.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
                correctAnswered++;
                textView_AnswerFeedback.setText("correct answer");
            } else {
                button.setBackground(ContextCompat.getDrawable(getContext(), R.color.red));
                wrongAnswered++;
                textView_AnswerFeedback.setText("wrong answer\ncorrect answer: "+ answer);
            }
        }
        canAnswer = false;
        countDownTimer.cancel();
        showNextButton();
    }
}