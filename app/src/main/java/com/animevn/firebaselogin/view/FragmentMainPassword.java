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

public class FragmentMainPassword extends Fragment {


    @BindView(R.id.editTextPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.editTextNewPassword)
    TextInputEditText editTextNewPassword;
    @BindView(R.id.buttonChangePasswordFragment)
    Button buttonChangePasswordFragment;
    @BindView(R.id.textViewReturn)
    TextView textViewReturn;
    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String currentEmail;
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
        View view = inflater.inflate(R.layout.fragment_main_change_password, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ShareModel viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        if (viewModel.getEmail() != null) {
            currentEmail = viewModel.getEmail();
            editTextEmail.setText(currentEmail);
        }
        return view;
    }

    @OnClick({R.id.buttonChangePasswordFragment, R.id.textViewReturn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonChangePasswordFragment:
                reAuthenticateUserAndChangePassword();
                break;
            case R.id.textViewReturn:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentMainPassword_to_fragmentMain);
                break;
        }
    }

    private void reAuthenticateUserAndChangePassword() {
        if (editTextNewPassword.getText() != null && user != null
                && editTextPassword.getText() != null && currentEmail != null) {

            String newPassword = editTextNewPassword.getText().toString().trim();
            String email = currentEmail.trim();
            String password = editTextPassword.getText().toString().trim();

            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful() && getView() != null) {
                            firebaseAuth.signOut();
                            Navigation.findNavController(getView())
                                    .navigate(R.id.action_fragmentMainPassword_to_fragmentLogin);
                        }
                    });
                } else {
                    Toast.makeText(activity, "Error changing password", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (TextUtils.isEmpty(editTextNewPassword.getText())) {
            editTextNewPassword.setError(getString(R.string.password_empty));
        } else if (TextUtils.isEmpty(editTextPassword.getText())) {
            editTextPassword.setError(getString(R.string.password_empty));
        }
    }

}
