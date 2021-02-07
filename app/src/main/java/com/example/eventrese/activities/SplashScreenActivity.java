package com.example.eventrese.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventrese.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.aescrypt.AESCrypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView iconShakeHands,iconSlogan;
    Animation fromTop, fromLeft, fromRight;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iconShakeHands = (ImageView)findViewById(R.id.iconShakeHands);
        iconSlogan = (ImageView)findViewById(R.id.iconslogan);

        fromTop = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.animator.anim_from_top_to_bottom);
        fromLeft = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.animator.anim_from_left_to_right);
        fromRight = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.animator.anim_from_right_to_left);

        iconShakeHands.setAnimation(fromTop);
       // iconLogo.setAnimation(fromLeft);
        iconSlogan.setAnimation(fromRight);

        // Set thread set to 3 seconds for the welcome screen
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                    if(readFile().equals("")) {
                        Intent iLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                        overridePendingTransition(R.animator.animation_in,R.animator.animation_out);
                        finish();
                    }
                    else
                    {
                        String messageAfterDecrypt = "";
                        try {
                            messageAfterDecrypt = AESCrypt.decrypt("123", readFile());
                        }catch (GeneralSecurityException e){
                            e.printStackTrace();
                        }
                        if(!messageAfterDecrypt.isEmpty()) {
                            String[] fulluser = messageAfterDecrypt.split("[ ]");
                            String email = fulluser[0].trim();
                            String password = fulluser[1].trim();
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SplashScreenActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Intent iLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                                startActivity(iLogin);
                                                overridePendingTransition(R.animator.animation_in,R.animator.animation_out);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(SplashScreenActivity.this,
                                                        MainActivity.class);
                                                String uid = firebaseAuth.getCurrentUser().getUid();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("UID", uid);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }

                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private String readFile() {
        try {
            //Opens a file reading stream.
            FileInputStream in = this.openFileInput("session.txt");
            if (in == null)
                return "";
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            return "";
        }
    }
}
