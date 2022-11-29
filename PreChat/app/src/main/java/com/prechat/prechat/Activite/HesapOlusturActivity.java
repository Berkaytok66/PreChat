package com.prechat.prechat.Activite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prechat.prechat.Claslar.GamerProfile;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.R;

public class HesapOlusturActivity extends AppCompatActivity {
    private int atlamaDeger;

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout1,linearLayout2,linearLayout3;
    private AnimationDrawable animationDrawable;
    private Button hesabimVarBTN,next_btn,back_btn;
    private ProgressBar progressBar;
    private TextView loginBTNtext;
    private CardView cardView;


    private TextInputLayout KAdiLayout, GmailLayout, SifreLayout;
    private TextInputEditText KAdiEditText, GmailEditText, SifreEditText;
    private String KAtutucu, GmailTutucu, SifreTutucu;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore, mFirestore2;
    private FirebaseUser mUser;
    private Kullanicilar mKullanici;
    private GamerProfile gamerProfile;
    private FirebaseUser mUser2;

    private Animation animation;
    private Dialog dialog;
    private Window window;

    public void init(){
        relativeLayout = findViewById(R.id.linearlayout2);
        linearLayout1 = findViewById(R.id.linear_layout_1);
        linearLayout2 = findViewById(R.id.linear_layout_2);
        linearLayout3 = findViewById(R.id.linear_layout_3);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        hesabimVarBTN = findViewById(R.id.zaten_hesabim_var);
        progressBar = findViewById(R.id.progresBar);
        loginBTNtext = findViewById(R.id.loginBTN);
        cardView = findViewById(R.id.cardView);

        KAdiLayout = findViewById(R.id.textInputKullaniciAdiLayout);
        GmailLayout = findViewById(R.id.textInputGmailLayout);
        SifreLayout = findViewById(R.id.textInputSifreLayout);

        KAdiEditText = findViewById(R.id.textInputKullaniciAdiEditText);
        GmailEditText = findViewById(R.id.textInputGmailEditText);
        SifreEditText = findViewById(R.id.textInputSifreEditText);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore2 = FirebaseFirestore.getInstance();
        mUser2  = FirebaseAuth.getInstance().getCurrentUser();
        back_btn = findViewById(R.id.back_buton);
        next_btn = findViewById(R.id.next_button);

        //animasyon
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.login_alpha_animation);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap_olustur);
        atlamaDeger=0;
        init();

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();



       hesabimVarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HesapOlusturActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
            }
        });
       next_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               KAtutucu = KAdiEditText.getText().toString();
               GmailTutucu = GmailEditText.getText().toString();
               SifreTutucu = SifreEditText.getText().toString();

                sorguBtn();
                buttonNext();

           }
       });
       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               atlamaDeger =atlamaDeger-1;
               buttonNext();
           }
       });
    }
    public void buttonNext(){

        if (atlamaDeger == 0 ){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout1.startAnimation(animation);
            linearLayout2.setVisibility(View.INVISIBLE);
            back_btn.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
        }else if (atlamaDeger == 1){
            linearLayout1.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            linearLayout2.startAnimation(animation);
            linearLayout3.setVisibility(View.INVISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            back_btn.startAnimation(animation);
        }else if (atlamaDeger == 2){
            linearLayout1.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.VISIBLE);
            linearLayout3.startAnimation(animation);
            back_btn.setVisibility(View.VISIBLE);
            next_btn.setText("oluştur");
        }


    }
    public void sorguBtn(){
        if (atlamaDeger == 0){
            if (!TextUtils.isEmpty(KAtutucu)){
                atlamaDeger = 1;
            }else
                KAdiLayout.setError("Kullanici Adi Boş Olamaz");
        }else if (atlamaDeger == 1){
            if (!TextUtils.isEmpty(GmailTutucu)){
                atlamaDeger = 2;
            }else
                GmailLayout.setError("Gmail boş olamaz");
        }else if (atlamaDeger == 2){
            if (!TextUtils.isEmpty(SifreTutucu)){
                mAuth.createUserWithEmailAndPassword(GmailTutucu,SifreTutucu).addOnCompleteListener(HesapOlusturActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mUser = mAuth.getCurrentUser();
                            if (mUser != null){
                                mKullanici = new Kullanicilar(KAtutucu,GmailTutucu,mUser.getUid(),SifreTutucu,"default");
                                mFirestore.collection("Kullanicilar").document(mUser.getUid()).set(mKullanici)
                                        .addOnCompleteListener(HesapOlusturActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    gamerProfile = new GamerProfile(null,null,null,null,KAtutucu,"default",mUser.getUid());
                                                    mFirestore.collection("GameProfil").document(mUser.getUid()).set(gamerProfile).addOnCompleteListener(HesapOlusturActivity.this, new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){

                                                                dialog = new Dialog(HesapOlusturActivity.this);
                                                                window = dialog.getWindow();
                                                                window.setGravity(Gravity.CENTER);
                                                                dialog.setContentView(R.layout.hesap_olustur_succses_dialog);
                                                                dialog.show();

                                                                Button btn = dialog.findViewById(R.id.succses_btn);
                                                                btn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(HesapOlusturActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });


                                                }else{
                                                    Snackbar.make(relativeLayout,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                            }
                        }else {
                            Snackbar.make(relativeLayout,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();

                        }
                    }
                });
            }else
                SifreLayout.setError("Gmail boş olamaz");
        }
    }

}