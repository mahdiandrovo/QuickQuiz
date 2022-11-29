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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.drovo.quickquiz.R;
import com.drovo.quickquiz.service.remoteDB.model.QuizListModel;
import com.drovo.quickquiz.view.adapter.QuizListAdapter;
import com.drovo.quickquiz.viewmodel.AuthenticationViewModel;
import com.drovo.quickquiz.viewmodel.QuizListViewModel;

import java.util.List;

public class ListFragment extends Fragment {

    private NavController navController;
    private QuizListViewModel viewModel;
    private QuizListAdapter adapter;

    private ProgressBar progressBar_quizTopicLoadingProgress;
    private RecyclerView recyclerView_quizList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        progressBar_quizTopicLoadingProgress = (ProgressBar) view.findViewById(R.id.progressBar_quizTopicLoadingProgress);

        recyclerView_quizList = (RecyclerView) view.findViewById(R.id.recyclerView_quizList);
        recyclerView_quizList.setHasFixedSize(true);
        recyclerView_quizList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new QuizListAdapter();
        recyclerView_quizList.setAdapter(adapter);

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                progressBar_quizTopicLoadingProgress.setVisibility(View.GONE);
                adapter.setQuizList(quizListModels);
                adapter.notifyDataSetChanged();
            }
        });
    }
}