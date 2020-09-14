package com.animevn.firebaselogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                break;
        }
    }
}
