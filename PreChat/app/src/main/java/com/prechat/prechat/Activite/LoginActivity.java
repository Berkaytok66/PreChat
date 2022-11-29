package com.prechat.prechat.Activite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prechat.prechat.R;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout linearLayout;
    private AnimationDrawable animationDrawable;
    private ProgressBar progressBar;
    private TextView loginBTNtext;
    private CardView cardView;
    private Button hesapOlusturBTN;
    public TextInputLayout GmailLayout,SifreLayout;
    public TextInputEditText GmailEditText, SifreEditText;
    public String GmailTutucu, SifreTutucu;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean aBoolean;
    private static final String SHARED_PREF_NAME = "MyPref";
    private static final String KEY_CHECKBOX = "CheckBox";
    private Handler handler = new Handler(Looper.getMainLooper());


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;

    public void init(){
        linearLayout = findViewById(R.id.relative_layout);
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        progressBar = findViewById(R.id.progresBar);
        loginBTNtext = findViewById(R.id.loginBTN);
        cardView = findViewById(R.id.cardView);
        hesapOlusturBTN = findViewById(R.id.hesap_olustur_btn);

        GmailLayout = findViewById(R.id.textInputPhoneNumberLayout);
        SifreLayout = findViewById(R.id.textInputLayout);
        GmailEditText = findViewById(R.id.textInputPhoneNumberEditText);
        SifreEditText = findViewById(R.id.textInputEditText);

        checkBox = findViewById(R.id.checkBox);
        mAuth= FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        // Bacgrount un rengini değiştirme
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                aBoolean=b;
                editor.putBoolean(KEY_CHECKBOX,b);
                editor.apply();
            }
        });
        if (aBoolean){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
        }else {
        }
        boolean isChacked = sharedPreferences.getBoolean(KEY_CHECKBOX,false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isChacked){
                    if (mUser != null){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                    }else
                        mAuth.signOut();
                }else {
                    mAuth.signOut();
                }
            }
        },1000);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBTNtext.setText("Lütfen Bekleyin");
                progressBar.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GmailTutucu = GmailEditText.getText().toString();
                        SifreTutucu = SifreEditText.getText().toString();

                        progressBar.setVisibility(View.INVISIBLE);
                        loginBTNtext.setTextColor(Color.parseColor("#3DDC84"));

                        if (!TextUtils.isEmpty(GmailTutucu)){
                            if (!TextUtils.isEmpty(SifreTutucu)){

                                mAuth.signInWithEmailAndPassword(GmailTutucu,SifreTutucu).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            loginBTNtext.setText("Başarılı");
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                                        }else{
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            loginBTNtext.setText("İşlem Sırasında Bir Hata İle Karşılaştık");
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }


                                    }
                                });
                            }else{
                                SifreLayout.setError("Sifre Boş bırakılamaz");
                                loginBTNtext.setText("Sifre Boş bırakılamaz");
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }else{
                            GmailLayout.setError("Gmail Boş Olamaz");
                            loginBTNtext.setText("Gmail Boş Olamaz");
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        //Butondan sonra yapılacak işlemler
                        Toast.makeText(getApplicationContext(),"giriş başarılı",Toast.LENGTH_SHORT).show();
                    }
                },4000);
            }
        });
        hesapOlusturBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HesapOlusturActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
            }
        });



    }

}