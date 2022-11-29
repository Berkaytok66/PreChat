package com.prechat.prechat.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.R;

public class AyarlarFragment extends Fragment {

    private View root;
    private TextInputEditText name,gmail,pasword;
    private Kullanicilar mKullanici;
    private FirebaseFirestore mFireStor;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference mRef;



    public void init(){
        name=root.findViewById(R.id.name);
        gmail=root.findViewById(R.id.gmail);
        pasword=root.findViewById(R.id.pasword);
        mFireStor = FirebaseFirestore.getInstance();
        mUser  = FirebaseAuth.getInstance().getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ayarlar, container, false);
        init();
        ProfilNamePaswordGmailDowland();

        return root;
    }
    public void ProfilNamePaswordGmailDowland(){

        mRef = mFireStor.collection("Kullanicilar").document(mUser.getUid());
        mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mKullanici = documentSnapshot.toObject(Kullanicilar.class);
                    if (mKullanici != null){
                        name.setText(mKullanici.getKullaniciIsmi());
                        gmail.setText(mKullanici.getKullaniciEmail());
                        pasword.setText(mKullanici.getKullaniciPassword());
                    }
                }
            }
        });
    }

}