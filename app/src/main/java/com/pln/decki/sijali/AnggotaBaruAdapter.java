package com.pln.decki.sijali;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cahya on 9/12/16.
 */
public class AnggotaBaruAdapter extends RecyclerView.Adapter<AnggotaBaruAdapter.AnggotaBaruViewHolder> {
    List<String> usernameList;
    List<String> namalengkapList;
    Context context;
    public class AnggotaBaruViewHolder extends RecyclerView.ViewHolder{
        public TextView hapus;
        public TextView namalengkap;
        public AnggotaBaruViewHolder(View v){
            super(v);
            namalengkap=(TextView)v.findViewById(R.id.namalengkap);
            hapus=(TextView)v.findViewById(R.id.hapus);
            if(usernameList==null){
                hapus.setVisibility(View.GONE);
            }
            //event handler saat item pada list jadwal pemeliharaan diclick
            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usernameList.remove(getAdapterPosition());
                    namalengkapList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    //notifyItemRangeRemoved(getAdapterPosition(),usernameList.size());
                    //notifyItemRangeRemoved(getAdapterPosition(),namalengkapList.size());
                }
            });
        }
    }

    public AnggotaBaruAdapter(Context context,List<String> namalengkap){
        this.context=context;
        this.namalengkapList=namalengkap;
    }

    public AnggotaBaruAdapter(Context context, List<String> username, List<String> namalengkap){
        this.context=context;
        this.usernameList=username;
        this.namalengkapList=namalengkap;
    }
    @Override
    public int getItemCount(){
        return this.namalengkapList.size();
    }
    @Override
    public AnggotaBaruViewHolder onCreateViewHolder(ViewGroup parent, int i){
        //pembuatan tiap item pada list
        View v= LayoutInflater.from(context).inflate(R.layout.anggota_baru_item,parent,false);
        return new AnggotaBaruViewHolder(v);
    }
    @Override
    public void onBindViewHolder(AnggotaBaruViewHolder anggotaBaruViewHolder, int i){
        //inisiasi tiap item pada list
        if (this.namalengkapList!=null){
            String namalengkap=this.namalengkapList.get(i);
            anggotaBaruViewHolder.namalengkap.setText(namalengkap);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
