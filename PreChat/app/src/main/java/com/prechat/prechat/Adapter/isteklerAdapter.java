package com.prechat.prechat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prechat.prechat.Claslar.MesajIstegi;
import com.prechat.prechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class isteklerAdapter extends RecyclerView.Adapter<isteklerAdapter.isteklerHolder> {

    private ArrayList<MesajIstegi> mMesajIstegiLIST;
    private Context mContext;
    private MesajIstegi mesajIstegi,yeniMesajIstegi;
    private View root;
    private int mPos;
    private String mUID, mIsim, mProfil;
    private FirebaseFirestore mFireStore;


    public isteklerAdapter(ArrayList<MesajIstegi> mMesajIstegiLIST, Context mContext,String mUID,String mIsim,String mProfil) {
        this.mMesajIstegiLIST = mMesajIstegiLIST;
        this.mContext = mContext;
        this.mUID =mUID;
        this.mIsim=mIsim;
        this.mProfil=mProfil;
        mFireStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public isteklerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        root = LayoutInflater.from(mContext).inflate(R.layout.gelen_mesaj_istekleri_card_tasarim,parent,false);

        return new isteklerHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull isteklerHolder holder, int position) {
        mesajIstegi = mMesajIstegiLIST.get(position);
        holder.txtMesaj.setText(mesajIstegi.getKullaniciName() + " Kullanıcısı sana mesaj göndermek istiyor");

        if (mesajIstegi.getKullaniciProfilPhoto().equals("default")){
            holder.imgProfil.setImageResource(R.drawable.profile_icons);
        }else{
            Picasso.get().load(mesajIstegi.getKullaniciProfilPhoto()).resize(77,77).into(holder.imgProfil);
        }
        holder.imgOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPos = holder.getAdapterPosition();

                if (mPos != RecyclerView.NO_POSITION){
                    yeniMesajIstegi = new MesajIstegi(mMesajIstegiLIST.get(mPos).getKanalID(),mMesajIstegiLIST.get(mPos).getKullaniciID(),mMesajIstegiLIST.get(mPos)
                            .getKullaniciName(),mMesajIstegiLIST.get(mPos).getKullaniciProfilPhoto());

                    mFireStore.collection("Kullanicilar").document(mUID).collection("Kanal").document(mMesajIstegiLIST.get(mPos).getKullaniciID())
                            .set(yeniMesajIstegi)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        yeniMesajIstegi = new MesajIstegi(mMesajIstegiLIST.get(mPos).getKanalID(),mUID,mIsim,mProfil);

                                        mFireStore.collection("Kullanicilar").document(mMesajIstegiLIST.get(mPos).getKullaniciID()).collection("Kanal")
                                                .document(mUID)
                                                .set(yeniMesajIstegi)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            mesajIstegiSil(mMesajIstegiLIST.get(mPos).getKullaniciID(),"İstek Kabul Edildi");
                                                        }else {
                                                            Toast.makeText(root.getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }

            }
        });
        holder.imgIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPos = holder.getAdapterPosition();

                if (mPos != RecyclerView.NO_POSITION){
                    mesajIstegiSil(mMesajIstegiLIST.get(mPos).getKullaniciID(),"İstek Red Edildi");
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mMesajIstegiLIST.size();
    }

    class isteklerHolder extends RecyclerView.ViewHolder {
    CircleImageView imgProfil;
    TextView txtMesaj;
    ImageView imgIptal,imgOnay;


        public isteklerHolder(@NonNull View itemView) {
            super(itemView);

            imgProfil = itemView.findViewById(R.id.gelen_mesaj_istekleri_card_tasarim_imageView);
            txtMesaj = itemView.findViewById(R.id.gelen_mesaj_istekleri_card_tasarim_TextView);
            imgIptal = itemView.findViewById(R.id.gelen_mesaj_istekleri_card_tasarim_İPTAL_BTN);
            imgOnay = itemView.findViewById(R.id.gelen_mesaj_istekleri_card_tasarim_OK_BTN);
        }
    }

    private void mesajIstegiSil(String hedefUUID,final String toastMesaj){
        mFireStore.collection("Mesajİstekleri").document(mUID).collection("İstekler").document(hedefUUID)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                  //  notifyDataSetChanged();
                    Toast.makeText(root.getContext(),toastMesaj,Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(root.getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}

