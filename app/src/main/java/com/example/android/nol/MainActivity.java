package com.example.android.nol;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

   private Button joinNowButton, loginButton, googleBtn ;
    private FirebaseAuth mAuth;
    private ProgressBar loadingBar;
    private Intent HomeActivity;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton =  findViewById(R.id.main_join_now_btn);
        loginButton   =  findViewById(R.id.main_login_btn);
        googleBtn     =  findViewById(R.id.main_google_login);
        loadingBar    =  findViewById(R.id.google_progressBar);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this,com.example.android.nol.HomeActivity.class);

        loadingBar.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                loadingBar.setVisibility(View.INVISIBLE);

            }

        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                googleBtn.setVisibility(View.INVISIBLE);
                switch (v.getId()) {
                    case R.id.main_google_login:
                        googleSignIn();
                    // ..
                }
            }
        });

    }
    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

      /* GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        finish();

       FirebaseUser user = mAuth.getCurrentUser();
          if(user != null){
            updateUI();
            finish();
        }*/


    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
           startActivity(HomeActivity);
           loadingBar.setVisibility(View.INVISIBLE);
           googleBtn.setVisibility(View.VISIBLE);
           finish();
        }
    }

    private void googleSignIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


}

