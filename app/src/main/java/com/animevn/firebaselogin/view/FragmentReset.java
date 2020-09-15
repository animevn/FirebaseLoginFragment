package com.animevn.firebaselogin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class FragmentReset extends Fragment {

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.buttonReset)
    Button buttonReset;
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
            ((MainActivity)getActivity()).hideActionBar();
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
        View view = inflater.inflate(R.layout.fragment_reset, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        return view;
    }

    @OnClick({R.id.buttonReset, R.id.textViewGoogleSignIn, R.id.textViewLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonReset:
                resetPassword();
                break;
            case R.id.textViewGoogleSignIn:
                startActivityForResult(viewModel.getGoogleClient().getSignInIntent(), RC_SIGN_IN);
                break;
            case R.id.textViewLogin:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentReset_to_fragmentLogin);
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
                            .navigate(R.id.action_fragmentReset_to_fragmentMain));
                }
            }catch (ApiException e){
                Log.e("SignIn Error: ", "error");
            }
        }
    }

    private void sendRequestResetPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task->{
            if (task.isSuccessful() && getView() != null){
                firebaseAuth.signOut();
                Navigation.findNavController(getView())
                        .navigate(R.id.action_fragmentReset_to_fragmentLogin);
            } else {
                Log.e("Reset Password: ", "reset failed");
            }
        });
    }

    private void resetPassword(){
        if (editTextEmail.getText() != null){
            String mail = editTextEmail.getText().toString();
            if (TextUtils.isEmpty(mail)){
                editTextEmail.setError(getString(R.string.email_empty));
            }else {
                sendRequestResetPassword(mail);
            }
        }
    }

}
