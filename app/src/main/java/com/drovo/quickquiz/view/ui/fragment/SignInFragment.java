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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drovo.quickquiz.R;
import com.drovo.quickquiz.viewmodel.AuthenticationViewModel;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }
    private AuthenticationViewModel viewModel;
    private NavController navController;

    private EditText editText_Email;
    private EditText editText_Password;

    private Button button_SignIn;

    private TextView textView_GoToSignUp;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        editText_Email = (EditText) view.findViewById(R.id.editText_email);
        editText_Password = (EditText) view.findViewById(R.id.editText_password);

        button_SignIn = (Button) view.findViewById(R.id.button_signIn);
        button_SignIn.setOnClickListener(this);

        textView_GoToSignUp = (TextView) view.findViewById(R.id.textView_goToSignUp);
        textView_GoToSignUp.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);
    }

    @Override
    public void onClick(View view) {
        if (view == button_SignIn){
            String email;
            email = editText_Email.getText().toString();
            String password;
            password = editText_Password.getText().toString();
            if (email != null && password != null){
                viewModel.signIn(email, password);
                Toast.makeText(getContext(), "login successfully", Toast.LENGTH_LONG).show();
                viewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser firebaseUser) {
                        if (firebaseUser != null){
                            navController.navigate(R.id.action_signInFragment_to_listFragment);
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "email or password is empty", Toast.LENGTH_LONG).show();
            }
        }
        if (view == textView_GoToSignUp){
            navController.navigate(R.id.action_signInFragment_to_signUpFragment);
        }
    }
}