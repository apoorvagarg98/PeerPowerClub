package com.example.peerpowerclub.registrationAndLogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.myHome;
import com.example.peerpowerclub.preRegistrtion;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextemail, editTextPassword;
    private TextView  editTextForgotpassword,donthaveacc;
   Button gsignButton ;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN =123;

    private Button  signin;
    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            Intent intent = new Intent(getApplicationContext(),myHome.class);

            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
donthaveacc = findViewById(R.id.dontHaveacc);
        donthaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, preRegistrtion.class));
                finish();
            }
        });
        mLoadingBar = new ProgressDialog(this);

        editTextForgotpassword = (TextView) findViewById(R.id.forgotPassword2);
        editTextForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, forgotpassword.class));
            }
        });
        signin = findViewById(R.id.btnLogin2);
        signin.setOnClickListener(this);

        editTextemail = (EditText) findViewById(R.id.inputEmail2);
        editTextPassword = (EditText) findViewById(R.id.inputPassword2);

        mAuth = FirebaseAuth.getInstance();
        gsignButton = findViewById(R.id.signInButton1);
        gsignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_i)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.register:
//                startActivity(new Intent(this, Registration.class));

//                break;

            case R.id.btnLogin2:
                userLogin();
                break;

            case R.id.forgotPassword2:
                startActivity(new Intent(this,forgotpassword.class));
                break;


        }

    }
    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(Login.this,"what the hell" +e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }


        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.toString(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("yo", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            editTextemail.setText(signInAccount.getEmail());





                    }
                });

    }

    private void userLogin() {

        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextemail.setError("email required");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("please provide valid email");
            editTextemail.requestFocus();
            return;

        }
        if(password.isEmpty())
        {
            editTextPassword.setError("password required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editTextPassword.setError("min pass require length should be 6");
            editTextPassword.requestFocus();
            return;
        }
        else {
            mLoadingBar.setTitle("logging user");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {mLoadingBar.dismiss();
                        Intent intent = new Intent(Login.this,myHome.class);

                        startActivity(intent);
                        finish();
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"check you mail to confirm it",Toast.LENGTH_LONG).show();
                    }

                }else
                {
                    Toast.makeText(Login.this,"failed to login",Toast.LENGTH_LONG).show();
                }
            }





        });
    }}
}