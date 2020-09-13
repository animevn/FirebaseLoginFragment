package com.animevn.firebaselogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    private void handleLoginCheckBox(){
        checkBoxRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
    }

    @OnClick({R.id.buttonLogin, R.id.textViewForgotPassword,
            R.id.textViewGoogleSignIn, R.id.textViewRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                break;
            case R.id.textViewForgotPassword:
                break;
            case R.id.textViewGoogleSignIn:
                break;
            case R.id.textViewRegister:
                break;
        }
    }
}
