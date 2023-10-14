package com.example.loginapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    TextView btn;
    private EditText inputEmail,inputPassword;
    private TextView textViewSignUp,ForgetPassword;
    private FirebaseAuth authProfile;
    private static final String TAG="LoginActivity";
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         btn=findViewById(R.id.textViewSignUp);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        btnLogin=findViewById(R.id.btnLogin);
        textViewSignUp=findViewById(R.id.textViewSignUp);
        ForgetPassword=findViewById(R.id.ForgetPassword);
        authProfile=FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkcredentials();

            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
    }
    private void checkcredentials(){
        String email=inputEmail.getText().toString();
        String pass=inputPassword.getText().toString();
      if(email.isEmpty() || !email.contains("@")){
            showError(inputEmail,"Email is not valid");
        }
        else if(pass.isEmpty() || pass.length()<7){
            showError(inputPassword,"Password must be 7 characters");
        }

        else{
            LoginUser(email,pass);

        }

    }
    private void LoginUser(String email,String pass){
        authProfile.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, Welcome.class);
                    intent.putExtra("email", email); // Pass the email as an extra
                    startActivity(intent);
                }else{
                    try {
                        throw task.getException();
                    }
                    catch(FirebaseAuthInvalidUserException e){
//                        showError(inputEmail,"User not valid please register again");
                        Toast.makeText(LoginActivity.this, "User not valid please register again", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthInvalidCredentialsException e){
//                        showError(inputEmail,"Invalid credentials. Please re-enter ");
                        Toast.makeText(LoginActivity.this,"Invalid credentials. Please re-enter", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());


                    }
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void showError(EditText input,String s){
        input.setError(s);
        input.requestFocus();
    }
}