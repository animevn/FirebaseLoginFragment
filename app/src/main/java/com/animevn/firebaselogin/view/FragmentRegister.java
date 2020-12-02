package com.animevn.firebaselogin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.animevn.firebaselogin.Model.Repo;
import com.animevn.firebaselogin.R;
import com.animevn.firebaselogin.viewmodel.ShareModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.app.Activity.RESULT_OK;
import static com.animevn.firebaselogin.Model.Constants.RC_SIGN_IN;

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
    private ShareModel viewModel;
    private FragmentActivity activity;

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        return view;
    }

    @OnClick({R.id.buttonRegister, R.id.textViewForgotPassword,
            R.id.textViewGoogleSignIn, R.id.textViewLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                Repo.signIn(editTextPassword, editTextEmail, activity, this::registerWithFirebase);
                break;
            case R.id.textViewForgotPassword:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentRegister_to_fragmentReset);
                break;
            case R.id.textViewGoogleSignIn:
                startActivityForResult(viewModel.getGoogleClient().getSignInIntent(), RC_SIGN_IN);
                break;
            case R.id.textViewLogin:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentRegister_to_fragmentLogin);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK && data != null){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    Repo.getFirebaseAuthFromGoogleSignIn(account, firebaseAuth, getView(), view ->
                            Navigation.findNavController(view)
                                    .navigate(R.id.action_fragmentRegister_to_fragmentMain));
                }
            }catch (ApiException e){
                Log.e("Google Login error: ", "error");
            }
        }
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

}
