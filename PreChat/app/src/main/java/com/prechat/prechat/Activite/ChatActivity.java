package com.prechat.prechat.Activite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prechat.prechat.Adapter.ChatAdapter;
import com.prechat.prechat.Claslar.Chat;
import com.prechat.prechat.Claslar.Kullanicilar;
import com.prechat.prechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseUser mUser;
    private HashMap<String,Object> mData;

    private RecyclerView mRecView;
    private CircleImageView mProfilPhoto;
    private EditText editText;
    private ImageView chatCloseBTN;
    private ImageButton gonderBTN;
    private TextView mProfilName;

    private Intent gelenIntent;
    private String hedefId,kanalId,hedefProfil,txtMesaj,mesajDocID;
    private Kullanicilar hedefKullanici;
    private DocumentReference hedefRef;
    private FirebaseFirestore mFireStor;

    private Query chatQuery;
    private ArrayList<Chat> mChatList;
    private Chat mChat;
    private ChatAdapter chatAdapter;

    private void init(){
        mRecView = findViewById(R.id.activiteRecaclerview);
        gonderBTN = findViewById(R.id.chat_mesaj_gönder_btn);
        chatCloseBTN = findViewById(R.id.chat_close);
        mProfilName = findViewById(R.id.chat_txt_name);
        mProfilPhoto = findViewById(R.id.chetActivite_image_profil_view);
        editText = findViewById(R.id.chat_mesaj_gir_editTect);
        mFireStor = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        gelenIntent = getIntent();
        hedefId = gelenIntent.getStringExtra("hedefId");
        kanalId = gelenIntent.getStringExtra("kanalId");
        hedefProfil=gelenIntent.getStringExtra("hedefProfil");
        mChatList = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        Butonlar();
        hedefRef =mFireStor.collection("Kullanicilar").document(hedefId);
        hedefRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(ChatActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null && value.exists()){
                    hedefKullanici = value.toObject(Kullanicilar.class);

                    if (hedefKullanici != null){

                        if (hedefKullanici.getKullaniciProfil().equals("default")){
                            mProfilPhoto.setImageResource(R.drawable.profile_icons);
                        }else{
                            Picasso.get().load(hedefKullanici.getKullaniciProfil()).resize(66,66).into(mProfilPhoto);
                        }
                    }
                }
            }
        });
        mRecView.setHasFixedSize(true);
        mRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        chatQuery = mFireStor.collection("ChatKanalları").document(kanalId).collection("Mesajlar")
                .orderBy("mesajTarihi", Query.Direction.ASCENDING);//gelen verinin tarihe göre sıralanması
        chatQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(ChatActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null){
                    mChatList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        mChat=snapshot.toObject(Chat.class);

                        if (mChat != null){
                            mChatList.add(mChat);
                        }
                    }
                    chatAdapter = new ChatAdapter(mChatList,ChatActivity.this,mUser.getUid(),hedefProfil);
                    mRecView.setAdapter(chatAdapter);
                }
            }
        });

    }
    private void Butonlar(){
        chatCloseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gonderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMesaj = editText.getText().toString();

                if (!TextUtils.isEmpty(txtMesaj)){
                    mesajDocID = UUID.randomUUID().toString();

                    mData = new HashMap<>();
                    mData.put("mesajIcerigi",txtMesaj);
                    mData.put("Gonderen",mUser.getUid());
                    mData.put("Alici",hedefId);
                    mData.put("mesajTipi","text");
                    mData.put("mesajTarihi", FieldValue.serverTimestamp());
                    mData.put("docID",mesajDocID);

                    mFireStor.collection("ChatKanalları").document(kanalId).collection("Mesajlar").document(mesajDocID)
                            .set(mData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        editText.setText("");
                                    }else {
                                        Toast.makeText(ChatActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    editText.setHint("Mesajınızı Girin");
                }
            }
        });
    }
}