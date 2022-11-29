package com.prechat.prechat.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prechat.prechat.Adapter.MesajlarAdapter;
import com.prechat.prechat.Claslar.MesajIstegi;
import com.prechat.prechat.R;

import java.util.ArrayList;

public class SmsFragment extends Fragment {
    private View root;
    private RecyclerView recyclerView;
    private FirebaseFirestore mFireStor;
    private Query mQuery;
    private Query sonMSGmQuery;
    private ArrayList<MesajIstegi> mArryList;
    private ArrayList<String> mSonMesajList;
    private MesajIstegi mesajIstegi;
    private FirebaseUser mUser;
    private MesajlarAdapter mesajlarAdapter;
    private int sonMesajIndex = 0;

    private void init() {
        recyclerView = root.findViewById(R.id.mesajlar_rv);
        mFireStor = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mArryList = new ArrayList<>();
        mSonMesajList = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sms, container, false);
        init();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.VERTICAL,false));

        mQuery = mFireStor.collection("Kullanicilar").document(mUser.getUid()).collection("Kanal");
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(root.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null){
                    mArryList.clear();
                    sonMesajIndex = 0;

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                            mesajIstegi = snapshot.toObject(MesajIstegi.class);

                            if (mesajIstegi != null){
                                mArryList.add(mesajIstegi);

                                sonMSGmQuery = mFireStor.collection("ChatKanalları").document(mesajIstegi.getKanalID()).collection("Mesajlar")
                                        .orderBy("mesajTarihi",Query.Direction.DESCENDING)
                                        .limit(1);
                                sonMSGmQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error) {
                                        if (error == null && value2 !=null){
                                            mSonMesajList.clear();

                                            for (DocumentSnapshot documentSnapshot : value2.getDocuments()){
                                                mSonMesajList.add(documentSnapshot.getData().get("mesajIcerigi").toString());
                                                sonMesajIndex++;
                                                if (sonMesajIndex == value.getDocuments().size()){
                                                    mesajlarAdapter = new MesajlarAdapter(mArryList,root.getContext(),mSonMesajList);
                                                    recyclerView.setAdapter(mesajlarAdapter);
                                                    sonMesajIndex = 0;
                                                }
                                            }
                                        }

                                    }
                                });
                            }
                    }

                }
            }
        });


        return root;
    }


}