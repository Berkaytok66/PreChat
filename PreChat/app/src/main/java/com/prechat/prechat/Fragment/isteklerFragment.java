package com.prechat.prechat.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prechat.prechat.Adapter.isteklerAdapter;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.Claslar.MesajIstegi;
import com.prechat.prechat.R;

import java.util.ArrayList;

public class isteklerFragment extends Fragment {

    private isteklerAdapter mAdapter;
    private View root;
    private Query mQuery;
    private FirebaseFirestore mFireStor;
    private FirebaseUser mUser;
    private MesajIstegi mesajIstegi;
    private ArrayList<MesajIstegi> MesajIstegiList;
    private RecyclerView mesaj_istekler_RCVIEW;
    private DocumentReference mRef;
    private Kullanicilar mKullanicilar;

    public void init(){
        mFireStor = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mesaj_istekler_RCVIEW = root.findViewById(R.id.mesaj_istekleri_rcView);
        mRef = mFireStor.collection("Kullanicilar").document(mUser.getUid());
        mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mKullanicilar = documentSnapshot.toObject(Kullanicilar.class);
                }
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_istekler, container, false);
        init();
        MesajIstegiList = new ArrayList<>();

        mQuery = mFireStor.collection("Mesajİstekleri").document(mUser.getUid()).collection("İstekler");
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null){
                    MesajIstegiList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){

                        mesajIstegi = snapshot.toObject(MesajIstegi.class);
                        MesajIstegiList.add(mesajIstegi);
                    }

                    mesaj_istekler_RCVIEW.setHasFixedSize(true);
                    mesaj_istekler_RCVIEW.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    if (MesajIstegiList.size() > 0){
                        mAdapter = new isteklerAdapter(MesajIstegiList,getActivity(),mKullanicilar.getKullaniciId(),mKullanicilar.getKullaniciIsmi(),mKullanicilar.getKullaniciProfil());
                        mesaj_istekler_RCVIEW.setAdapter(mAdapter);
                    }
                }
            }
        });

        return root;
    }

}