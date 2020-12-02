package com.animevn.firebaselogin.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.animevn.firebaselogin.R;
import com.animevn.firebaselogin.viewmodel.ShareModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentMainEmail extends Fragment {

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.editTextNewEmail)
    TextInputEditText editTextNewEmail;
    @BindView(R.id.buttonChangeEmailFragment)
    Button buttonChangeEmailFragment;
    @BindView(R.id.textViewReturn)
    TextView textViewReturn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String oldEmail;
    private FragmentActivity activity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideActionBar();
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
        View view = inflater.inflate(R.layout.fragment_main_change_email, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ShareModel viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        if (viewModel.getEmail() != null) {
            oldEmail = viewModel.getEmail();
            editTextEmail.setText(oldEmail);
        }
        return view;
    }


    @OnClick({R.id.buttonChangeEmailFragment, R.id.textViewReturn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonChangeEmailFragment:
                reAuthenticateUserAndChangeEmail();
                break;
            case R.id.textViewReturn:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentMainEmail_to_fragmentMain);
                break;
        }
    }

    private void reAuthenticateUserAndChangeEmail(){
        if (editTextNewEmail.getText() != null && user != null
                && editTextPassword.getText() != null && oldEmail != null){

            String newEmail = editTextNewEmail.getText().toString().trim();
            String email = oldEmail.trim();
            String password = editTextPassword.getText().toString().trim();

            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful() && getView() != null){
                            firebaseAuth.signOut();
                            Navigation.findNavController(getView())
                                    .navigate(R.id.action_fragmentMainEmail_to_fragmentLogin);
                        }
                    });
                }else {
                    Toast.makeText(activity, "Error changing email", Toast.LENGTH_SHORT).show();
                }
            });
        }else if (TextUtils.isEmpty(editTextNewEmail.getText())){
            editTextNewEmail.setError(getString(R.string.email_empty));
        } else if (TextUtils.isEmpty(editTextPassword.getText())){
            editTextNewEmail.setError(getString(R.string.password_empty));
        }
    }

}
