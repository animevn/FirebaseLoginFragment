package com.animevn.firebaselogin.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.animevn.firebaselogin.R;
import com.animevn.firebaselogin.viewmodel.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentMain extends Fragment {

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;
    @BindView(R.id.buttonChangeEmail)
    Button buttonChangeEmail;
    @BindView(R.id.buttonChangePassword)
    Button buttonChangePassword;
    @BindView(R.id.buttonResetPassowrd)
    Button buttonResetPassowrd;
    @BindView(R.id.buttonRemoveUser)
    Button buttonRemoveUser;
    @BindView(R.id.buttonLogOut)
    Button buttonLogOut;

    private MyViewModel viewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FragmentActivity activity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null){
                    getActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFirebase(view);
        viewModel = new ViewModelProvider(activity).get(MyViewModel.class);
        initView();
    }

    private void initFirebase(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        authStateListener = firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null){
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentMain_to_fragmentLogin);
            }
        };
    }

    private void initView(){
        if (user != null){
            textViewEmail.setText(user.getEmail());
        }

        if (viewModel.isGoogleSignIn()){
            buttonChangeEmail.setEnabled(false);
            buttonChangePassword.setEnabled(false);
            buttonResetPassowrd.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (authStateListener != null){
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @OnClick({R.id.buttonChangeEmail, R.id.buttonChangePassword, R.id.buttonResetPassowrd,
            R.id.buttonRemoveUser, R.id.buttonLogOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonChangeEmail:
                break;
            case R.id.buttonChangePassword:
                break;
            case R.id.buttonResetPassowrd:
                break;
            case R.id.buttonRemoveUser:
                break;
            case R.id.buttonLogOut:
                firebaseAuth.signOut();
                if (viewModel.isGoogleSignIn()){
                    viewModel.signOutGoogleclient();
                    viewModel.setGoogleSignIn(false);
                }
                break;
        }
    }
}
