package com.drovo.quickquiz.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.drovo.quickquiz.service.remoteDB.model.QuizListModel;
import com.drovo.quickquiz.service.remoteDB.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<QuizListModel>> quizListLiveData = new MutableLiveData<>();
    private QuizListRepository repository = new QuizListRepository(this);

    public QuizListViewModel(){
        repository.getQuizTopicData();
    }

    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {
        return quizListLiveData;
    }

    @Override
    public void quizTopicDataLodaed(List<QuizListModel> quizListModels) {
        quizListLiveData.setValue(quizListModels);
    }

    @Override
    public void onError(Exception exception) {
        Log.d("QuizTopicLoadError", "onError: "+exception.getMessage());
    }
}
