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
        return view;
    }


    @OnClick({R.id.buttonRegister, R.id.textViewForgotPassword, R.id.textViewGoogleSignIn, R.id.textViewLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
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
}
