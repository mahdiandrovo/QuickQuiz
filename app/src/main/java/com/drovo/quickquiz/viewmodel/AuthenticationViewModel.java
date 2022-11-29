package com.drovo.quickquiz.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.drovo.quickquiz.service.remoteDB.repository.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseUser currentUser;
    private AuthenticationRepository authenticationRepository;

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public AuthenticationRepository getAuthenticationRepository() {
        return authenticationRepository;
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticationRepository = new AuthenticationRepository(application);
        currentUser = authenticationRepository.getCurrentUser();
        firebaseUserMutableLiveData = authenticationRepository.getFirebaseUserMutableLiveData();
    }

    public void signUp(String email, String password){
        authenticationRepository.signUp(email, password);
    }

    public void signIn(String email, String password){
        authenticationRepository.signIn(email, password);
    }

    public void signOut(){
        authenticationRepository.signOut();
    }

}
