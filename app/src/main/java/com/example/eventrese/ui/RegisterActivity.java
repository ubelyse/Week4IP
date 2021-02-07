package com.example.eventrese.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventrese.R;
import com.example.eventrese.fragments.DatePickerFragment;
import com.example.eventrese.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    @BindView(R.id.editTextUsername) EditText mun;
    @BindView(R.id.editTextPassword) EditText mpsd;
    @BindView(R.id.editTextRetypePassword) EditText mretypepsd;
    @BindView(R.id.editTextFullName) EditText mfn;
    @BindView(R.id.editTextAddress) EditText madd;
    @BindView(R.id.editTextPhoneNumber) EditText mphone;
    @BindView(R.id.editTextDateOfBirth) EditText mdob;

    @BindView(R.id.buttonRegister) Button mregister;
    @BindView(R.id.buttonCancelRegister) Button mcancel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Date date;
    private String dateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(iLogin);
                finish();
            }
        });

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mun.getText().toString().trim();
                final String password = mpsd.getText().toString().trim();
                final String repassword = mretypepsd.getText().toString().trim();
                final String fullname = mfn.getText().toString().trim();
                final String address = madd.getText().toString().trim();
                final String phonenumber = mphone.getText().toString().trim();

                date = new Date();

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    dateOfBirth = mdob.getText().toString().trim();
                    date = formatter.parse(dateOfBirth);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password must be longer than 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(getApplicationContext(), "password incorrect!", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registration error, please check again. Each email only registered 1 time only!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                   String userid=firebaseUser.getUid();
                                    Toast.makeText(RegisterActivity.this, "Registration is complete!",
                                            Toast.LENGTH_SHORT).show();

                                    User account = new User(userid,email, fullname,address,"","", phonenumber, dateOfBirth,"offline",fullname.toLowerCase());
                                    String uid = firebaseAuth.getCurrentUser().getUid();
                                    databaseReference.child("users").child(uid).setValue(account);
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }

                        });

            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.editTextDateOfBirth:
                if (hasFocus) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(), "Date of birth");
                }
                break;
        }
    }
}