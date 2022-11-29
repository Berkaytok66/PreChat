package com.prechat.prechat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prechat.prechat.Activite.ChatActivity;
import com.prechat.prechat.Claslar.MesajIstegi;
import com.prechat.prechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesajlarAdapter extends RecyclerView.Adapter<MesajlarAdapter.MesajlarHolder> {
    private ArrayList<MesajIstegi> mArrayList;
    private ArrayList<String> mSonMSJlist;
    private Context mContext;
    private MesajIstegi mesajIstegi;
    private View root;

    private int kPos;

    public MesajlarAdapter(ArrayList<MesajIstegi> mArrayList, Context mContext,ArrayList<String> mSonMSJlist) {
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.mSonMSJlist = mSonMSJlist;
    }

    @NonNull
    @Override
    public MesajlarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        root = LayoutInflater.from(mContext).inflate(R.layout.mesajlar_item,parent,false);
        return new MesajlarHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MesajlarHolder holder, int position) {
    mesajIstegi = mArrayList.get(position);
    holder.kullaniciIsim.setText(mesajIstegi.getKullaniciName());
    holder.sonMesaj.setText("Son: "+mSonMSJlist.get(position));
        if (mesajIstegi.getKullaniciProfilPhoto().equals("default")){
            holder.mProfilPhoto.setImageResource(R.drawable.profile_icons);
        }else{
            Picasso.get().load(mesajIstegi.getKullaniciProfilPhoto()).resize(120,120).into(holder.mProfilPhoto);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kPos = holder.getAdapterPosition();
                if (kPos != RecyclerView.NO_POSITION){
                   Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("kanalId",mArrayList.get(kPos).getKanalID());
                    intent.putExtra("hedefId",mArrayList.get(kPos).getKullaniciID());
                    intent.putExtra("hedefProfil",mArrayList.get(kPos).getKullaniciProfilPhoto());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class MesajlarHolder extends RecyclerView.ViewHolder{
        TextView kullaniciIsim,sonMesaj;
        CircleImageView mProfilPhoto;
        public MesajlarHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciIsim = itemView.findViewById(R.id.mesajlar_kullanici_name);
            sonMesaj = itemView.findViewById(R.id.mesajlar_son_mesaj);
            mProfilPhoto = itemView.findViewById(R.id.mesajlar_item_profil_photo);
        }
    }
}
