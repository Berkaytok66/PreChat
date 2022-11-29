package com.prechat.prechat.Fragment;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;
import com.prechat.prechat.Activite.MainActivity;
import com.prechat.prechat.Claslar.GamerProfile;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.R;


public class GamerProfilFragment extends Fragment {

    private View root;
    private LabelToggle labelToggle, labelToggle2,cinsiyetLabelToggle1,cinsiyetLabelToggle2,
            rankedLabelToggle1,rankedLabelToggle2,rankedLabelToggle3,rankedLabelToggle4,
            rankedLabelToggle5,rankedLabelToggle6,rankedLabelToggle7,rankedLabelToggle8;

    private ProgressBar progressBar;
    private TextView loginBTNtext, profil_durum_text,profil_durum_text2,profil_durum_text3,oyuncuTanimlamaText;
    private CardView cardView , cardViewButton;


    String sohbet,ranked,cinsiyet,tanim,kAdi,kmUID;


    private DocumentReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GamerProfile gamerProfile,mProfil;
    private Kullanicilar mKullanici;
    private FirebaseFirestore mFireStor;

    private LinearLayout GameProfileLinearlayout,GameProfileLinearlayout_2;
    private TextInputLayout textinputlayout;
    private TextInputEditText textInputEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gamer_profil, container, false);

        mFireStor = FirebaseFirestore.getInstance();
        mUser  = FirebaseAuth.getInstance().getCurrentUser();
        init();
        return root;
    }

    private void init() {

        labelToggle = root.findViewById(R.id.lb_sun);
        labelToggle2 = root.findViewById(R.id.lb_mon);
        progressBar = root.findViewById(R.id.progresBar);
        loginBTNtext = root.findViewById(R.id.loginBTN);
        cardView =root.findViewById(R.id.cardView);
        cardViewButton  =root.findViewById(R.id.cardView1);
        rankedLabelToggle1 = root.findViewById(R.id.ranked_1);
        rankedLabelToggle2 = root.findViewById(R.id.ranked_2);
        rankedLabelToggle3 = root.findViewById(R.id.ranked_3);
        rankedLabelToggle4 = root.findViewById(R.id.ranked_4);
        rankedLabelToggle5 = root.findViewById(R.id.ranked_5);
        rankedLabelToggle6 = root.findViewById(R.id.ranked_6);
        rankedLabelToggle7 = root.findViewById(R.id.ranked_7);
        rankedLabelToggle8 = root.findViewById(R.id.ranked_8);

        cinsiyetLabelToggle1 = root.findViewById(R.id.kiz);
        cinsiyetLabelToggle2= root.findViewById(R.id.erkek);

        GameProfileLinearlayout = root.findViewById(R.id.GameProfileLinearlayout);
        GameProfileLinearlayout_2 = root.findViewById(R.id.GameProfileLinearlayout_2);
        profil_durum_text = root.findViewById(R.id.profil_durum_text);
        profil_durum_text2= root.findViewById(R.id.profil_durum_text2);
        profil_durum_text3= root.findViewById(R.id.profil_durum_text3);
        oyuncuTanimlamaText = root.findViewById(R.id.oyuncuTanimlamaText);



        ProfilUpdate();
        ProfilBilgileriGörüntüleme();


    }
    public void ProfilUpdate(){
        mRef = mFireStor.collection("Kullanicilar").document(mUser.getUid());
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                if (value != null && value.exists()) {
                    mKullanici = value.toObject(Kullanicilar.class);

                    if (mUser != null) {

                        kAdi = mKullanici.getKullaniciIsmi();
                        kmUID = mKullanici.getKullaniciId();

                    }
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBTNtext.setText("Lütfen Bekleyin");
                progressBar.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        loginBTNtext.setTextColor(Color.parseColor("#3DDC84"));
                        SohbetUpdate();
                        RankedUpdate();
                        CinsiyetUpdate();
                        TanimUpdate();
                        if (sohbet != null){
                            if (ranked != null){
                                if (cinsiyet != null){
                                    if (tanim != null){
                                        gamerProfile = new GamerProfile(sohbet,ranked,cinsiyet,tanim,kAdi,"default",kmUID);
                                        mFireStor.collection("GameProfil").document(mUser.getUid()).set(gamerProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loginBTNtext.setText("BAŞARILI");
                                                progressBar.setVisibility(View.INVISIBLE);
                                                GameProfileLinearlayout.setVisibility(View.VISIBLE);
                                                GameProfileLinearlayout_2.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }else {
                                        loginBTNtext.setText("Lütfen Kendinizi Tanımlayın");
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }else {
                                    loginBTNtext.setText("Lütfen Cinsiyet Belirtin");
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }else {
                                loginBTNtext.setText("Lütfen Rank Tercihinizi Secin");
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }else{
                            loginBTNtext.setText("Lütfen Sohbet Tercihinizi Secin");
                            progressBar.setVisibility(View.INVISIBLE);
                        }



                    }
                },3000);
            }
        });

    }
    public void SohbetUpdate(){
        if (labelToggle.isChecked()){
            sohbet = labelToggle.getText().toString();

        }else if (labelToggle2.isChecked()){
            sohbet = labelToggle2.getText().toString();

        }
    }
    public void RankedUpdate(){
        if (rankedLabelToggle1.isChecked()){
            ranked=rankedLabelToggle1.getText().toString();
        }else if (rankedLabelToggle2.isChecked()){
            ranked=rankedLabelToggle2.getText().toString();
        }else if (rankedLabelToggle3.isChecked()){
            ranked=rankedLabelToggle3.getText().toString();
        }else if (rankedLabelToggle4.isChecked()){
            ranked=rankedLabelToggle4.getText().toString();
        }else if (rankedLabelToggle5.isChecked()){
            ranked=rankedLabelToggle5.getText().toString();
        }else if (rankedLabelToggle6.isChecked()){
            ranked=rankedLabelToggle6.getText().toString();
        }else if (rankedLabelToggle7.isChecked()){
            ranked=rankedLabelToggle7.getText().toString();
        }else if (rankedLabelToggle8.isChecked()){
            ranked=rankedLabelToggle8.getText().toString();
        }
    }
    public void CinsiyetUpdate(){
        if (cinsiyetLabelToggle1.isChecked()){
            cinsiyet = cinsiyetLabelToggle1.getText().toString();
        }else if (cinsiyetLabelToggle2.isChecked()){
            cinsiyet = cinsiyetLabelToggle2.getText().toString();
        }
    }
    public void TanimUpdate(){
        textinputlayout = root.findViewById(R.id.textinputlayout);
        textInputEditText = root.findViewById(R.id.textInputEditText);
        tanim = textInputEditText.getText().toString();
    }
    public void ProfilBilgileriGörüntüleme(){

        mRef = mFireStor.collection("GameProfil").document(mUser.getUid());
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                if (value != null && value.exists()) {
                    mProfil = value.toObject(GamerProfile.class);

                    if (mUser != null) {
                             profil_durum_text.setText(mProfil.getSohbet());
                             profil_durum_text2.setText(mProfil.getRank());
                             profil_durum_text3.setText(mProfil.getCinsiyet());
                             oyuncuTanimlamaText.setText(mProfil.getTanim());


                    }
                }
            }
        });



        cardViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameProfileLinearlayout.setVisibility(View.INVISIBLE);
                GameProfileLinearlayout_2.setVisibility(View.VISIBLE);
            }
        });
    }
}