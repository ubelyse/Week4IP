package com.example.eventrese.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tnac.Models.Account;
import com.example.tnac.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private Toolbar toolbar;
    private EditText txtFullName, txtAddress, txtPhoneNumber, txtDateOfBirth, txtDescription;
    private RadioGroup radioGroupGender;
    private String uid, fullName, address, dateOfBirth, phoneNumber, description,userName;
    private boolean gender;
    private Account account;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button btnEdit, btnBack;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        txtFullName = findViewById(R.id.editTextFullName);
        txtAddress = findViewById(R.id.editTextAddress);
        txtDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        txtPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        txtDescription = findViewById(R.id.editTextDescription);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        btnEdit = findViewById(R.id.buttonEditProfiles);

        btnBack = findViewById(R.id.btnBackToPersonalFragment);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToFragment();
            }
        });

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = radioGroupGender.getCheckedRadioButtonId();
                View radioButton = radioGroupGender.findViewById(radioButtonID);
                int idx = radioGroupGender.indexOfChild(radioButton);
                RadioButton r = (RadioButton)radioGroupGender.getChildAt(idx);
                String selectedtext = r.getText().toString();
                if(selectedtext.equals("Male")){
                    gender = true;
                }
                else{
                    gender = false;
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                account.setFullName(txtFullName.getText().toString().trim());
                account.setAddress(txtAddress.getText().toString().trim());
                account.setPhoneNumber(txtPhoneNumber.getText().toString().trim());
                account.setGender(gender);
                account.setDescription(txtDescription.getText().toString().trim());
                account.setDateOfBirth(txtDateOfBirth.getText().toString().trim());
                firebaseDatabase = FirebaseDatabase.getInstance();
                //databaseReference = firebaseDatabase.getReference().child("users").child(uid);
                databaseReference=firebaseDatabase.getReference();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("users").child(uid).exists()){
                            databaseReference = firebaseDatabase.getReference().child("users").child(uid);
                            databaseReference.setValue(account);
                            Toast.makeText(EditProfileActivity.this,"Successfully updated!",Toast.LENGTH_SHORT).show();
                            backToFragment();
                        }
                        else if (snapshot.child("doctors").child(uid).exists()){
                            databaseReference = firebaseDatabase.getReference().child("doctors").child(uid);
                            databaseReference.setValue(account);
                            Toast.makeText(EditProfileActivity.this,"Successfully updated!",Toast.LENGTH_SHORT).show();
                            backToFragment();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //databaseReference.setValue(account);

                //Toast.makeText(EditProfileActivity.this,"Successfully updated!",Toast.LENGTH_SHORT).show();
                //backToFragment();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit information");

        Drawable backArrow = getResources().getDrawable(R.drawable.back);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("BUNDLE");

            if (bundle != null) {
                uid = bundle.getString("USERID");
                userName = bundle.getString("USERNAME");
                fullName = bundle.getString("FULLNAME");
                address = bundle.getString("ADDRESS");
                phoneNumber = bundle.getString("PHONENUMBER");
                dateOfBirth = bundle.getString("DATEOFBIRTH");
                description = bundle.getString("DESCRIPTION");
                gender = bundle.getBoolean("GENDER");
                account = new Account(userName, description, fullName, gender, address, phoneNumber, dateOfBirth);
            }
        }

        if (account != null) {
            txtFullName.setText(account.getFullName());
            txtPhoneNumber.setText(account.getPhoneNumber() + "");
            txtDateOfBirth.setText(account.getDateOfBirth());
            if (gender) {
                ((RadioButton)radioGroupGender.getChildAt(1)).setChecked(true);
            } else {
                ((RadioButton)radioGroupGender.getChildAt(2)).setChecked(true);
            }

            txtAddress.setText(account.getAddress());
            txtDescription.setText(account.getDescription());
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ReturnTab", 2);
                bundle.putString("UID",uid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public  void backToFragment(){
        Intent iPersonal = new Intent(EditProfileActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("UID", FirebaseAuth.getInstance().getUid());
        bundle.putInt("ReturnTab",2);
        iPersonal.putExtras(bundle);
        startActivity(iPersonal);
        finish();
    }
}