package com.prechat.prechat.Adapter;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prechat.prechat.Activite.ChatActivity;
import com.prechat.prechat.Claslar.GamerProfile;
import com.prechat.prechat.Claslar.MesajIstegi;
import com.prechat.prechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class OyuncularAdapter extends RecyclerView.Adapter<OyuncularAdapter.OyuncularHolder> {
    private ArrayList<GamerProfile> mKullaniciList;
    private Context mContext;
    private View root;
    private GamerProfile mGameProfil;
    private int kPos;

    private Dialog mesajDialog;
    private ImageButton iptalBTN,gonderBTN;
    private CircleImageView imgProfil;
    private EditText editMesaj;
    private String mesajIcerik;
    private Window mesajWindow;
    private TextView dialog_kullanici_name_txt;

    private FirebaseFirestore mFiresStor;
    private DocumentReference mRef;
    private String mUID,kanalID,mesajDocID,misim,mProfilURL;
    private MesajIstegi mesajIstegi;
    private HashMap<String,Object> mData;
    private Intent intent;

    public OyuncularAdapter(ArrayList<GamerProfile> mKullaniciList, Context mContext,String mUID,String misim,String mProfilURL) {
        this.mKullaniciList = mKullaniciList;
        this.mContext = mContext;
        mFiresStor = FirebaseFirestore.getInstance();
        this.mUID = mUID;
        this.misim = misim;
        this.mProfilURL = mProfilURL;
    }

    @NonNull
    @Override
    public OyuncularHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        root = LayoutInflater.from(mContext).inflate(R.layout.my_gamer_profile_cart_tasarim,parent,false);
        return new OyuncularHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull OyuncularHolder holder, int position) {
    mGameProfil = mKullaniciList.get(position);
    holder.kullaniciName.setText(mGameProfil.getKullaniciAd());
    if (mGameProfil.getTanim() == null){
        holder.kullaniciDurum.setText("Belirsiz Durum");
    }else{
        holder.kullaniciDurum.setText(mGameProfil.getTanim());
    }
        if (mGameProfil.getKullaniciProfil().equals("default")){
             holder.oyuncaProfil.setImageResource(R.drawable.profile_icons);
        }else{
            Picasso.get().load(mGameProfil.getKullaniciProfil()).resize(66,66).into(holder.oyuncaProfil);
        }

        holder.satir_cart_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kPos = holder.getAdapterPosition();
                if (kPos != RecyclerView.NO_POSITION){

                    mRef = mFiresStor.collection("Kullanicilar").document(mUID).collection("Kanal").document(mKullaniciList.get(kPos).getKullaniciId());
                    mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                //Mesajlaşma activite
                                 intent = new Intent(mContext, ChatActivity.class);
                                 intent.putExtra("kanalId",documentSnapshot.getData().get("kanalID").toString());
                                 intent.putExtra("hedefId",mKullaniciList.get(kPos).getKullaniciId());
                                 intent.putExtra("hedefProfil",mKullaniciList.get(kPos).getKullaniciProfil());
                                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                 mContext.startActivity(intent);
                            }else {
                                mesajgonderDialog(mKullaniciList.get(kPos));
                            }
                        }
                    });

                }
            }
        });
    }

    private void mesajgonderDialog(GamerProfile gamerProfile) {
        mesajDialog = new Dialog(mContext);
        mesajDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mesajWindow = mesajDialog.getWindow();
        mesajWindow.setGravity(Gravity.CENTER);
        mesajDialog.setContentView(R.layout.custom_dialog_kullanici_bilgileri_goruntuleme);

        iptalBTN = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_img_iptal);
        gonderBTN = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_img_gonder);
        imgProfil = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_img_profil);
        editMesaj = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_editText);
        dialog_kullanici_name_txt = mesajDialog.findViewById(R.id.custom_dialog_mesaj_gonder_kullanici_isim);


        dialog_kullanici_name_txt.setText(gamerProfile.getKullaniciAd());
        if (gamerProfile.getKullaniciProfil().equals("default")){
            imgProfil.setImageResource(R.drawable.profile_icons);
        }else{
            Picasso.get().load(mGameProfil.getKullaniciProfil()).resize(120,120).into(imgProfil);
        }
        iptalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesajDialog.dismiss();
            }
        });

        gonderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesajIcerik = editMesaj.getText().toString();

                if (!TextUtils.isEmpty(mesajIcerik)){
                    kanalID = UUID.randomUUID().toString();

                    mesajIstegi = new MesajIstegi(kanalID, mUID,misim,mProfilURL);
                    mFiresStor.collection("Mesajİstekleri").document(gamerProfile.getKullaniciId()).collection("İstekler").document(mUID)
                            .set(mesajIstegi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         //chat bölümü
                                         mesajDocID = UUID.randomUUID().toString();
                                         mData = new HashMap<>();
                                         mData.put("mesajIcerigi",mesajIcerik);
                                         mData.put("Gonderen",mUID);
                                         mData.put("Alici",gamerProfile.getKullaniciAd());
                                         mData.put("mesajTipi","text");
                                         mData.put("mesajTarihi", FieldValue.serverTimestamp());
                                         mData.put("docID",mesajDocID);
                                         mFiresStor.collection("ChatKanalları").document(kanalID).collection("Mesajlar").document(mesajDocID)
                                                 .set(mData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if (task.isSuccessful()){
                                                             Toast.makeText(mContext,"başarılı",Toast.LENGTH_SHORT).show();
                                                             if (mesajDialog.isShowing()){
                                                                 mesajDialog.dismiss();
                                                             }
                                                         }
                                                     }
                                                 });
                                     }else {
                                         Toast.makeText(mContext,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                     }
                                }
                            });

                }else {
                    editMesaj.setHint("Boş Mesaj Gönderilemez !!!");
                }
            }
        });
        mesajWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        mesajDialog.show();
    }

    @Override
    public int getItemCount() {
        return mKullaniciList.size();
    }

    class OyuncularHolder extends RecyclerView.ViewHolder{

        CircleImageView oyuncaProfil;
        TextView kullaniciName, kullaniciDurum;
        CardView satir_cart_view;
        public OyuncularHolder(@NonNull View itemView) {
            super(itemView);

            oyuncaProfil = itemView.findViewById(R.id.kullaniciProfilIMAGE);
            kullaniciName = itemView.findViewById(R.id.kullaniciNAME);
            kullaniciDurum = itemView.findViewById(R.id.textViewValorant170VPkacPuan);
            satir_cart_view = itemView.findViewById(R.id.satir_cart_view);
        }
    }
}
