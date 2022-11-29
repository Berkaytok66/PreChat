package com.prechat.prechat.Activite;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prechat.prechat.Claslar.GamerProfile;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.Fragment.AyarlarFragment;
import com.prechat.prechat.Fragment.GamerProfilFragment;
import com.prechat.prechat.Fragment.HomeFragment;
import com.prechat.prechat.Fragment.OtoEslesmeFragment;
import com.prechat.prechat.Fragment.OyuncularFragment;
import com.prechat.prechat.Fragment.SmsFragment;
import com.prechat.prechat.Fragment.isteklerFragment;
import com.prechat.prechat.R;
import com.squareup.picasso.Picasso;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DuoDrawerLayout drawerLayout;
    private TextView userName,userMail,bildirimText;
    private ImageView baslikImageView;
    private LinearLayout anaLayoutBacgroun;
    private RelativeLayout mesajIstekbildirimleriRealitiveLayout;
    private FirebaseFirestore mFireStor;
    private FirebaseUser mUser,firebaseUser;
    private FirebaseAuth mAuth;
    private Query mQuery;


    private Kullanicilar mKullanici;
    private GamerProfile mGameProfile;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "MyPref";
    private static final String KEY_CHECKBOX = "CheckBox";
    private Handler handler = new Handler(Looper.getMainLooper());

    private Kullanicilar hedefKullanici;
    private DocumentReference hedefRef;

    private ActivityResultLauncher<String> permissonLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireStor = FirebaseFirestore.getInstance();
        mUser  = FirebaseAuth.getInstance().getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        baslikImageView = findViewById(R.id.baslıkImageView);
        anaLayoutBacgroun = findViewById(R.id.anaLayoutBacgrountID);
        init();

    }
    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        View contenView = drawerLayout.getContentView();
        View menuViewView = drawerLayout.getMenuView();

        LinearLayout home_id = menuViewView.findViewById(R.id.home_id);
        LinearLayout sms_id = menuViewView.findViewById(R.id.sms_id);
        LinearLayout istekler_id = menuViewView.findViewById(R.id.istekler_id);
        LinearLayout eslesme_id = menuViewView.findViewById(R.id.eslesme_id);
        LinearLayout ayarlar_id = menuViewView.findViewById(R.id.ayarlar_id);
        LinearLayout logout_id = menuViewView.findViewById(R.id.logout_id);
        LinearLayout oyuncular_id = menuViewView.findViewById(R.id.oyuncular_id);
        LinearLayout gamerProfil_if = menuViewView.findViewById(R.id.gamer_profil_id);

        userName = menuViewView.findViewById(R.id.userName222);
        userMail = menuViewView.findViewById(R.id.userMail333);
        bildirimText = menuViewView.findViewById(R.id.bildirim_text);
        mesajIstekbildirimleriRealitiveLayout = menuViewView.findViewById(R.id.mesajIstekbildirimleriRealitiveLayout);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        gamerProfil_if.setOnClickListener(this);
        home_id.setOnClickListener(this);
        oyuncular_id.setOnClickListener(this);
        sms_id.setOnClickListener(this);
        istekler_id.setOnClickListener(this);
        eslesme_id.setOnClickListener(this);
        ayarlar_id.setOnClickListener(this);
        logout_id.setOnClickListener(this);

        replace(new HomeFragment());
        userNameAndMailUpdate();
        mesajIstegiKontrol();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gamer_profil_id:
                replace(new GamerProfilFragment(),"Gamer Profil");
                baslikImageView.setImageResource(R.drawable.game_profil_png);
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg_game_profil);
                break;
                case R.id.home_id:
                replace(new HomeFragment(),"Home");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg);

                break;
            case R.id.oyuncular_id:
                replace(new OyuncularFragment(),"Kullanicilar");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg_game_profil);
                break;
            case R.id.sms_id:
                replace(new SmsFragment(),"SMS");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg);
                break;

            case R.id.istekler_id:
                replace(new isteklerFragment(),"İstekler");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg);
                break;

            case R.id.eslesme_id:
                replace(new OtoEslesmeFragment(),"OTO ESLESME");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.main_bg);
                break;

            case R.id.ayarlar_id:
                replace(new AyarlarFragment(),"AYARLAR");
                anaLayoutBacgroun.setBackgroundResource(R.drawable.ayarlar_layout_bacgrount);
                ProfilPhotoUpdate();
                baslikImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;

            case R.id.logout_id:
                logOut();
                break;
        }
        drawerLayout.closeDrawer();
    }
    private void replace(Fragment fragment, String s) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.addToBackStack(s);
        transaction.commit();
    }
    private void replace(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }

    public void userNameAndMailUpdate(){


        DocumentReference mRef = mFireStor.collection("Kullanicilar").document(mUser.getUid());
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                if (value != null && value.exists()){
                    mKullanici = value.toObject(Kullanicilar.class);

                    if(mKullanici != null){
                        userName.setText(mKullanici.getKullaniciIsmi());

                    }
                }
            }
        });

        DocumentReference mRef2 = mFireStor.collection("GameProfil").document(mUser.getUid());
        mRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }

                if (value != null && value.exists()){
                    mGameProfile = value.toObject(GamerProfile.class);

                    if(mGameProfile != null){
                        userMail.setText("Sohbet: "+mGameProfile.getSohbet()+"\n"+"Rankınız: "+mGameProfile.getRank()+"\n"+"Cinsiyet: "+ mGameProfile.getCinsiyet());

                    }
                }
            }
        });

    }
    public void mesajIstegiKontrol(){
        mQuery = mFireStor.collection("Mesajİstekleri").document(mUser.getUid()).collection("İstekler");
        mQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null){
                    if (value.getDocuments().size() >0){
                        mesajIstekbildirimleriRealitiveLayout.setVisibility(View.VISIBLE);
                        bildirimText.setText("+" + String.valueOf(value.getDocuments().size()));
                    }

                }
            }
        });
    }
    public void logOut(){
        boolean isChacked = sharedPreferences.getBoolean(KEY_CHECKBOX,true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isChacked){
                    mAuth.signOut();
                    firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser == null){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }else{

                }
            }
        },100);
    }
    public void ProfilPhotoUpdate(){
        hedefRef = mFireStor.collection("Kullanicilar").document(mUser.getUid());
        hedefRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    hedefKullanici = documentSnapshot.toObject(Kullanicilar.class);
                    if (hedefKullanici != null){
                        if (hedefKullanici.getKullaniciProfil().equals("default")){
                            baslikImageView.setImageResource(R.drawable.profile_icons);
                        }else{
                            Picasso.get().load(hedefKullanici.getKullaniciProfil()).resize(250,200).into(baslikImageView);
                        }
                    }
                }


            }
        });
    }


}