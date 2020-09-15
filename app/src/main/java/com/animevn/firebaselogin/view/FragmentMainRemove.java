package com.animevn.firebaselogin.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.animevn.firebaselogin.R;
import com.animevn.firebaselogin.viewmodel.ShareModel;
import com.google.android.material.textfield.TextInputEditText;
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

public class FragmentMainRemove extends Fragment {

    @BindView(R.id.editTextEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.buttonRemoveUserFragment)
    Button buttonRemoveUserFragment;
    @BindView(R.id.textViewReturn)
    TextView textViewReturn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
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
        View view = inflater.inflate(R.layout.fragment_main_remove_user, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ShareModel viewModel = new ViewModelProvider(activity).get(ShareModel.class);
        if (viewModel.getEmail() != null) {
            editTextEmail.setText(viewModel.getEmail());
        }
        return view;
    }

    @OnClick({R.id.buttonRemoveUserFragment, R.id.textViewReturn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonRemoveUserFragment:
                deleteUser();
                break;
            case R.id.textViewReturn:
                Navigation.findNavController(view)
                        .navigate(R.id.action_fragmentMainRemove_to_fragmentMain);
                break;
        }
    }

    private void deleteUser(){
        if (user != null){
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful() && getView() != null){
                    firebaseAuth.signOut();
                    Navigation.findNavController(getView())
                            .navigate(R.id.action_fragmentMainRemove_to_fragmentLogin);
                }else {
                    Log.e("error: ", "delete user");
                }
            });
        }
    }
}
