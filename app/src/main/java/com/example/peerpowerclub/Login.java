package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextemail, editTextPassword;
    private TextView  register,editTextForgotpassword;

    private Button  signin;
    private FirebaseAuth mAuth;
    static String AreaofInterest2 = Registration.AreaofInterest,daynight2 = Registration.daynight;

    String checkbox;


    CheckBox remember,c1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextForgotpassword = (TextView) findViewById(R.id.forgotPassword2);
        editTextForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, forgotpassword.class));
            }
        });
        signin = (Button) findViewById(R.id.btnLogin2);
        signin.setOnClickListener(this);
        c1 = findViewById(R.id.c);

        editTextemail = (EditText) findViewById(R.id.inputEmail2);
        editTextPassword = (EditText) findViewById(R.id.inputPassword2);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        checkbox = preferences.getString("remember","");

        if(checkbox.equals("true"))
        {
            startActivity(new Intent(Login.this,myHome.class));
        }
        else if (checkbox.equals("false"))
        {
            Toast.makeText(this,"please login",Toast.LENGTH_LONG).show();
        }

        remember = findViewById(R.id.remember2);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(Login.this,"checked",Toast.LENGTH_LONG).show();
                }
                else if(!compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(Login.this,"Unchecked",Toast.LENGTH_LONG).show();
                }
            }
        });
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextPassword.setTransformationMethod(null);
                } else {
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:
                startActivity(new Intent(this, Registration.class));

                break;

            case R.id.btnLogin2:
                userLogin();
                break;

            case R.id.forgotPassword2:
                startActivity(new Intent(this,forgotpassword.class));
                break;

        }

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

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
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
    }
}