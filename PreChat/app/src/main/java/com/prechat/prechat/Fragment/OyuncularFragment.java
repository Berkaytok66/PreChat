package com.prechat.prechat.Fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.prechat.prechat.Adapter.OyuncularAdapter;
import com.prechat.prechat.Claslar.GamerProfile;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.R;

import java.util.ArrayList;


public class OyuncularFragment extends Fragment {
    private View root;
    private RecyclerView mRecyclerView;
    private OyuncularAdapter mAdapter;
    private ArrayList<GamerProfile> mKullaniciList;
    private Kullanicilar mKullanic;
    private GamerProfile mGameProfil;


    private FirebaseUser mUser;
    private FirebaseFirestore mFirestor;
    private Query mQuery;
    private DocumentReference mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_oyuncular, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestor = FirebaseFirestore.getInstance();

        mRef = mFirestor.collection("Kullanicilar").document(mUser.getUid());
        mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mKullanic = documentSnapshot.toObject(Kullanicilar.class);
                }


            }
        });

        mKullaniciList = new ArrayList<>();
        rcycviewAyari();


        mQuery = mFirestor.collection("GameProfil");
        mQuery.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(root.getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                if (value != null ){
                        mKullaniciList.clear();

                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            mGameProfil = snapshot.toObject(GamerProfile.class);

                            assert mGameProfil != null;
                            if (!mGameProfil.getKullaniciId().equals(mUser.getUid())){
                                mKullaniciList.add(mGameProfil);
                            }

                        }

                        mAdapter = new OyuncularAdapter(mKullaniciList,root.getContext(),mKullanic.getKullaniciId(),mKullanic.getKullaniciIsmi(),mKullanic.getKullaniciProfil());
                        mRecyclerView.setAdapter(mAdapter);

                }
            }
        });

        return root;
    }

    public void rcycviewAyari(){
        mRecyclerView = root.findViewById(R.id.rcView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }


}