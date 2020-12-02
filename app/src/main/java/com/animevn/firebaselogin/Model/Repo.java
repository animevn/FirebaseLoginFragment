package com.animevn.firebaselogin.Model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.animevn.firebaselogin.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Repo {

    public interface OnProcessEmailAndPassword{
        void processEmailAndPassword(String email, String password);
    }

    public static void signIn(
            TextInputEditText editTextPassword,
            TextInputEditText editTextEmail, Context context, OnProcessEmailAndPassword listener){
        if (editTextPassword.getText() != null && editTextEmail.getText() != null){
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (TextUtils.isEmpty(email)){
                editTextEmail.setError(context.getString(R.string.email_empty));
                return;
            }

            if (TextUtils.isEmpty(password)){
                editTextPassword.setError(context.getString(R.string.password_empty));
                return;
            }

            if (password.length() < 6){
                editTextPassword.setError(context.getString(R.string.minimum_password));
                return;
            }

            listener.processEmailAndPassword(email, password);
        }
    }

    public interface OnGoogleSignInOk{
        void onGoogleSignInOk(View view);
    }

    public static void getFirebaseAuthFromGoogleSignIn(GoogleSignInAccount account,
                                                 FirebaseAuth firebaseAuth,
                                                 View view,
                                                 OnGoogleSignInOk listener){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful() && view != null){
                listener.onGoogleSignInOk(view);
            }else {
                Log.e("SignIn with Firebase: ", "error");
            }
        });
    }


}
