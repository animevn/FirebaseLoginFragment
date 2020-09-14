package com.animevn.firebaselogin.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.animevn.firebaselogin.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentRegister extends Fragment {


    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.textViewForgotPassword)
    TextView textViewForgotPassword;
    @BindView(R.id.checkBoxRememberMe)
    CheckBox checkBoxRememberMe;
    @BindView(R.id.textViewGoogleSignIn)
    TextView textViewGoogleSignIn;
    @BindView(R.id.textViewLogin)
    TextView textViewLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            //hide action bar
            ((MainActivity) getActivity()).hideActionBar();

            //lock orientation to portrait
            ((MainActivity) getActivity()).setScreenOrientationPortrait();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }


    @OnClick({R.id.buttonRegister, R.id.textViewForgotPassword,
            R.id.textViewGoogleSignIn, R.id.textViewLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                signup();
                break;
            case R.id.textViewForgotPassword:
                break;
            case R.id.textViewGoogleSignIn:
                break;
            case R.id.textViewLogin:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentRegister_to_fragmentLogin);
                break;
        }
    }

    private void loginWithUserEmailAndPassword(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("login error: ", "login failed");
            } else {
                if (getView() != null){
                    Navigation.findNavController(getView())
                            .navigate(R.id.action_fragmentRegister_to_fragmentMain);
                }
            }
        });
    }

    private void registerWithFirebase(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task->{
            if (task.isSuccessful()){
                loginWithUserEmailAndPassword(email, password);
            } else {
                Log.e("register: ", "signup failed");
            }
        });
    }

    private void signup(){
        if (editTextPassword.getText() != null && editTextEmail.getText() != null){
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (TextUtils.isEmpty(email)){
                editTextEmail.setError(getString(R.string.email_empty));
                return;
            }

            if (TextUtils.isEmpty(password)){
                editTextPassword.setError(getString(R.string.password_empty));
                return;
            }

            if (password.length() < 6){
                editTextPassword.setError(getString(R.string.minimum_password));
                return;
            }

            registerWithFirebase(email, password);
        }
    }


}
