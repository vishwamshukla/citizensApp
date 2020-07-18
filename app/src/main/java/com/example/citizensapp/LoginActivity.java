package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText InputEmail, InputPassword;
    private TextView forgotPassword;
    private Button LoginButton, LoginwithGoogle;
    private ProgressDialog loadingBar;
    private String parentDbName = "Public";
    //private android.widget.CheckBox chkBoxRememberMe;
    private ImageView circle1;
    TextView tvLogin;
    private ImageButton btRegister;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    private ProgressBar progressBar;

    // private TextView ForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btRegister = findViewById(R.id.btRegister);
        progressBar = findViewById(R.id.progress_bar);

        forgotPassword = (TextView) findViewById(R.id.tvForgot);

        LoginButton = (Button) findViewById(R.id.login_btn_doctor);
        LoginwithGoogle = (Button) findViewById(R.id.login_with_google);
        InputEmail = (EditText) findViewById(R.id.login_email_input_doctor);
        InputPassword = (EditText) findViewById(R.id.login_password_input_doctor);
        loadingBar = new ProgressDialog(this);
        //chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb_reception);
        //Paper.init(this);

        //ForgotPassword = (TextView) findViewById(R.id.forgot_password_link_reception);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//                if (v==btRegister){
//                    Intent a = new Intent(LoginActivity.this, DoctorRegisterActivity.class);
//                    Pair[] pairs = new Pair[1];
//                    pairs[0] = new Pair<View,String> (tvLogin,"login");
//                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
//                    startActivity(a,activityOptions.toBundle());
//                }
              startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        // Configure Google Sign In
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();

       // mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//        mGoogleSignInClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        LoginwithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //GoogleSignInResult result = new Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser !=  null){
            int d = Log.d(TAG, "onStart: "+ currentUser.getDisplayName());
            SendUserToHomeActivity();
        }
    }


    private void LoginUser() {
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Phone Number!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        } else {
//            loadingBar.setTitle("Login in progress");
//            loadingBar.setMessage("Please wait...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToHomeActivity();
                                Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error-" + message, Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }

    private void SendUserToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

//    private void AllowAccessToAccount(final String email, final String password) {
//
////        if (chkBoxRememberMe.isChecked()){
////            Paper.book().write(Prevalent.UserPhoneKey,phone);
////            Paper.book().write(Prevalent.UserPasswordKey,password);
////        }
//
//
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(parentDbName).child(phone).exists()){
//                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
//                    if (usersData.getPhone().equals(phone)){
//                        if (usersData.getPassword().equals(password)){
////                            if (parentDbName.equals("Admins")){
////                                Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
////                                loadingBar.dismiss();
////
////                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
////                                startActivity(intent);
////                            }
//                            if (parentDbName.equals("Users")){
//                                Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//
//                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//                                Prevalent.currentOnlineUser = usersData;
//                                startActivity(intent);
//                            }                        }
//                        else {
//                            loadingBar.dismiss();
//                            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//                else {
//                    Toast.makeText(LoginActivity.this, "Account with this "+phone+" number do not exists", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}