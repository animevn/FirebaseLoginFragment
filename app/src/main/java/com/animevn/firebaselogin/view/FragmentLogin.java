package com.animevn.firebaselogin.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.animevn.firebaselogin.viewmodel.ShareModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentLogin extends Fragment {

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.textViewForgotPassword)
    TextView textViewForgotPassword;
    @BindView(R.id.checkBoxRememberMe)
    CheckBox checkBoxRememberMe;
    @BindView(R.id.textViewGoogleSignIn)
    TextView textViewGoogleSignIn;
    @BindView(R.id.textViewRegister)
    TextView textViewRegister;

    public static final int RC_SIGN_IN = 2204;
    private FirebaseAuth firebaseAuth;
    private ShareModel viewModel;
    private FragmentActivity activity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null){
            //hide action bar
            ((MainActivity)getActivity()).hideActionBar();
            //lock orientation to portrait
            ((MainActivity)getActivity()).setScreenOrientationPortrait();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        handleLoginCheckBox();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkUserSignIn(view);
        viewModel = new ViewModelProvider(activity).get(ShareModel.class);
    }

    private void checkUserSignIn(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            Navigation.findNavController(view)
                    .navigate(R.id.action_fragmentLogin_to_fragmentMain);
        }
    }

    private void handleLoginCheckBox(){
        checkBoxRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
    }

    @OnClick({R.id.buttonLogin, R.id.textViewForgotPassword,
            R.id.textViewGoogleSignIn, R.id.textViewRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                signIn();
                break;
            case R.id.textViewForgotPassword:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentLogin_to_fragmentReset);
                break;
            case R.id.textViewGoogleSignIn:
                startActivityForResult(viewModel.getGoogleClient().getSignInIntent(), RC_SIGN_IN);
                break;
            case R.id.textViewRegister:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentLogin_to_fragmentRegister);
                break;
        }
    }

    private void signIn(){
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

            loginWithUserEmailAndPassword(email, password);
        }
    }

    private void loginWithUserEmailAndPassword(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("login error: ", "login failed");
            } else {
                if (getView() != null){
                    Navigation.findNavController(getView())
                            .navigate(R.id.action_fragmentLogin_to_fragmentMain);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK && data != null){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    getFirebaseAuthFromGoogleSignIn(account);
                }
            }catch (ApiException e){
                Log.e("error1", "error");
            }
        }
    }

    private void getFirebaseAuthFromGoogleSignIn(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful() && getView() != null){
                Navigation.findNavController(getView())
                        .navigate(R.id.action_fragmentLogin_to_fragmentMain);
            }else {
                Log.e("error2", "error");
            }
        });
    }
}
