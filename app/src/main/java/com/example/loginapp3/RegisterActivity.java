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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class RegisterActivity extends AppCompatActivity {
    TextView btn;
    private EditText inputUsername,inputEmail,inputPassword,inputConformPassword;
    private static final String TAG="RegisterActivity";
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn=findViewById(R.id.AlreadyHaveAccount);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputConformPassword=findViewById(R.id.inputConformPassword);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkcredentials();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
    private void checkcredentials(){
        String username=inputUsername.getText().toString();
        String email=inputEmail.getText().toString();
        String pass=inputPassword.getText().toString();
        String cpass=inputConformPassword.getText().toString();
        if(username.isEmpty() || username.length()<7){
            showError(inputUsername,"Username is not valid");
        }
        else if(email.isEmpty() || !email.contains("@")){
            showError(inputEmail,"Email is not valid");
        }
        else if(pass.isEmpty() || pass.length()<7){
            showError(inputPassword,"Password must be 7 characters");
        }
        else if(cpass.isEmpty() || !cpass.equals(pass)){
            showError(inputConformPassword,"Password not match!!");

        }
        else{
            registerUser( username, email, pass, cpass);

        }

    }
//Register User
    private void registerUser(String username, String email, String pass, String cpass) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            FirebaseUser firebaseuser=auth.getCurrentUser();
//Enter data to database
                            ReadWriteUserDetails writeUserDetails=
                                    new ReadWriteUserDetails(username, email, pass, cpass);


//                            extracting user interface from database for registered users
                            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Registered Users");

                            dr.child(firebaseuser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        firebaseuser.sendEmailVerification();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        Toast.makeText(RegisterActivity.this, "Registration " +
                                                        "Successfully.Please verify your email",
                                                Toast.LENGTH_SHORT).show();
                                        //prevent to go registeractivity after sucessfull registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration " +
                                                        "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });



                        }
                        else{
                            try {
                                throw task.getException();
                            }

                           catch (Exception e){
                                Log.e(TAG, e.getMessage());

                               Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void showError(EditText input,String s){
        input.setError(s);
        input.requestFocus();
    }
}