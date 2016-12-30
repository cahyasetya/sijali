package com.pln.decki.sijali;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pln.decki.sijali.object.Pekerjaan;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by cahya on 9/10/16.
 */
public class PemeliharaanAdapter extends RecyclerView.Adapter<PemeliharaanAdapter.PemeliharaanViewHolder> {
    List<Pekerjaan> pekerjaanList;
    Context context;

    public class PemeliharaanViewHolder extends RecyclerView.ViewHolder{
        public TextView tanggal,judul,lokasi;
        public PemeliharaanViewHolder(View v){
            super(v);
            tanggal=(TextView)v.findViewById(R.id.tanggalpemeliharaan);
            judul=(TextView)v.findViewById(R.id.judulpemeliharaan);
            lokasi=(TextView)v.findViewById(R.id.lokasipemeliharaan);

            //event handler saat item pada jadwal pemeliharaan diklik
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //buat parameter dan sertakan saat berpindah ke layout selanjutnya
                    Bundle arg=new Bundle();
                    arg.putSerializable("pemeliharaan", pekerjaanList.get(getAdapterPosition()));
                    PemeliharaanViewer pv=new PemeliharaanViewer();
                    pv.setArguments(arg);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().addToBackStack("pemeliharaanViewer").replace(R.id.fragment_container,pv).commit();
                }
            });
        }
    }

    public PemeliharaanAdapter(Context context, List<Pekerjaan> pekerjaanList){
        this.pekerjaanList=pekerjaanList;
        this.context=context;
    }

    @Override
    public int getItemCount(){
        return pekerjaanList.size();
    }

    @Override
    public PemeliharaanViewHolder onCreateViewHolder(ViewGroup parent, int i){
        //buat item pada list
        View v= LayoutInflater.from(context).inflate(R.layout.pemeliharaan_item,parent,false);
        return new PemeliharaanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PemeliharaanViewHolder pemeliharaanViewHolder, int i){
        //inisiasi setiap item
        Pekerjaan pekerjaan=pekerjaanList.get(i);
        String[] tanggal=pekerjaan.getTanggalPekerjaan().split("-");
        String tanggalBaru=tanggal[2]+"-"+tanggal[1]+"-"+tanggal[0];
        if(pekerjaan!=null){
            pemeliharaanViewHolder.judul.setText(pekerjaan.getJudulPekerjaan());
            pemeliharaanViewHolder.tanggal.setText(tanggalBaru);
            pemeliharaanViewHolder.lokasi.setText(pekerjaan.getLokasiPekerjaan());
        }
    }
}
