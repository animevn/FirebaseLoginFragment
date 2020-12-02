package com.animevn.firebaselogin.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.animevn.firebaselogin.R;
import com.animevn.firebaselogin.viewmodel.ShareModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class FragmentMainReset extends Fragment {

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.textInputEmail)
    TextInputLayout textInputEmail;
    @BindView(R.id.buttonReset)
    Button buttonReset;
    @BindView(R.id.textViewReturn)
    TextView textViewReturn;
    private FirebaseAuth firebaseAuth;
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
        View view = inflater.inflate(R.layout.fragment_main_reset_password, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        ShareModel viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        if (viewModel.getEmail() != null){
            editTextEmail.setText(viewModel.getEmail());
        }
        return view;
    }

    @OnClick({R.id.buttonReset, R.id.textViewReturn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonReset:
                resetPassword();
                break;
            case R.id.textViewReturn:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentMainReset_to_fragmentMain);
                break;
        }
    }

    private void sendRequestResetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful() && getView() != null) {
                firebaseAuth.signOut();
                Navigation.findNavController(getView())
                        .navigate(R.id.action_fragmentMainReset_to_fragmentLogin);
            } else {
                Log.e("Reset Password: ", "reset failed");
            }
        });
    }

    private void resetPassword() {
        if (editTextEmail.getText() != null) {
            String mail = editTextEmail.getText().toString();
            if (TextUtils.isEmpty(mail)) {
                editTextEmail.setError(getString(R.string.email_empty));
            } else {
                sendRequestResetPassword(mail);
            }
        }
    }


}
