package com.pln.decki.sijali;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pln.decki.sijali.object.Anggota;

import java.util.List;

/**
 * Created by cahya on 9/6/16.
 */
public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder> {
    private final Context context;
    private final List<Anggota> anggotaList;
    public class AnggotaViewHolder extends RecyclerView.ViewHolder{
        public TextView namalengkap;
        public TextView jabatan;
        public LinearLayout anggotacontainer;
        private AnggotaViewHolder(View v){
            super(v);
            anggotacontainer=(LinearLayout)v.findViewById(R.id.anggota_container);
            namalengkap=(TextView)v.findViewById(R.id.namalengkap);
            jabatan=(TextView)v.findViewById(R.id.jabatan);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=getAdapterPosition();

                    //parameter untuk diteruskan ke profil viewer
                    Bundle arg=createParameter(getAdapterPosition());

                    //menambahkan parameter ke layout selanjutnya
                    AnggotaViewer pv=new AnggotaViewer();
                    pv.setArguments(arg);

                    //beralih ke layout selanjutnya
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pv).commit();
                }
            });
        }

        private Bundle createParameter(int i){
            //pembuatan parameter
            Anggota anggota=new Anggota();
            anggota.setUsername(anggotaList.get(i).getUsername());
            anggota.setNamaLengkap(anggotaList.get(i).getNamaLengkap());
            anggota.setNamaPanggilan(anggotaList.get(i).getNamaPanggilan());
            anggota.setNip(anggotaList.get(i).getNip());
            anggota.setJabatan(anggotaList.get(i).getJabatan());
            anggota.setNoHp(anggotaList.get(i).getNoHp());
            anggota.setStatus(anggotaList.get(i).getStatus());
            anggota.setKeterangan(anggotaList.get(i).getKeterangan());

            Bundle arg=new Bundle();
            arg.putSerializable("anggota", anggota);
            return arg;
        }
    }

    public AnggotaAdapter(Context context, List<Anggota> anggotaList){
        this.context=context;
        this.anggotaList=anggotaList;
    }

    @Override
    public int getItemCount(){
        return anggotaList.size();
    }

    @Override
    public AnggotaViewHolder onCreateViewHolder(ViewGroup parent, int i){
        //pembuatan item pada list
        View v= LayoutInflater.from(context).inflate(R.layout.anggota_item, parent, false);
        return new AnggotaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnggotaViewHolder anggotaViewHolder, int i){
        //inisiasi pada tiap item di list
        Anggota anggota=anggotaList.get(i);
        if(anggota!=null){
            anggotaViewHolder.namalengkap.setText(anggota.getNamaLengkap());
            anggotaViewHolder.jabatan.setText(anggota.getJabatan());
            if(anggota.getStatus().equals("Siap")){
                anggotaViewHolder.anggotacontainer.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }else{
                anggotaViewHolder.anggotacontainer.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
