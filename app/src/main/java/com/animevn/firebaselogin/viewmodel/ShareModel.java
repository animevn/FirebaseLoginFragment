package com.animevn.firebaselogin.viewmodel;

import android.app.Application;
import android.content.Context;
import com.animevn.firebaselogin.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ShareModel extends AndroidViewModel {

    private GoogleSignInClient googleClient;

    public ShareModel(@NonNull Application application) {
        super(application);
        googleClient = (initGoogleSignIn(application.getApplicationContext()));
    }

    private GoogleSignInClient initGoogleSignIn(Context context){
        GoogleSignInOptions options = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, options);
    }

    public GoogleSignInClient getGoogleClient() {
        return googleClient;
    }

    public void signOutGoogleclient(){
        googleClient.signOut();
    }

}
