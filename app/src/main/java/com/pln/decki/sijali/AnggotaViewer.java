package com.pln.decki.sijali;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pln.decki.sijali.object.Anggota;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnggotaViewer extends Fragment implements View.OnClickListener{

    private TextView namalengkapjudul;
    private TextView namalengkap;
    private TextView namapanggilan;
    private TextView nip;
    private TextView jabatan;
    private TextView nohp;
    private Button siap;
    private Button tidaksiap;
    private TextView keterangan;
    private Button hapus;
    private Anggota anggota;

    public AnggotaViewer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profil_viewer, container, false);

        //terima objek untuk ditampilkan
        anggota=(Anggota)getArguments().getSerializable("anggota");

        //inisiasi komponen
        inisiasiKomponen(v,anggota);

        //assign nilai pada layout
        layoutSetup(anggota);
        return v;
    }

    private void inisiasiKomponen(View v, Anggota anggota){
        namalengkapjudul=(TextView)v.findViewById(R.id.namalengkapjudul);
        namalengkap=(TextView)v.findViewById(R.id.namalengkap);
        namapanggilan=(TextView)v.findViewById(R.id.namapanggilan);
        nip=(TextView)v.findViewById(R.id.nip);
        jabatan=(TextView)v.findViewById(R.id.jabatan);
        nohp=(TextView)v.findViewById(R.id.nohp);
        keterangan=(TextView)v.findViewById(R.id.keterangan);
        siap=(Button)v.findViewById(R.id.statussiap);
        tidaksiap=(Button)v.findViewById(R.id.statustidaksiap);
        hapus=(Button)v.findViewById(R.id.hapus);
        hapus.setOnClickListener(this);

        Gson gson=new Gson();
        SharedPreferences preferences=getContext().getSharedPreferences("sijali", 0);
        Anggota user=gson.fromJson(preferences.getString("anggota",null).toString(), Anggota.class);
        if (user.getHak_akses_anggota()==1){
            hapus.setVisibility(View.VISIBLE);
        }else {
            hapus.setVisibility(View.GONE);
        }
    }

    private void layoutSetup(Anggota anggota){
        namalengkapjudul.setText(anggota.getNamaLengkap());
        namalengkap.setText(anggota.getNamaLengkap());
        namapanggilan.setText(anggota.getNamaPanggilan());
        nip.setText(anggota.getNip());
        jabatan.setText(anggota.getJabatan());
        nohp.setText(anggota.getNoHp());
        if(anggota.getStatus().equals("Siap")){
            siap.setBackgroundResource(R.drawable.kiri_biru);
            tidaksiap.setBackgroundResource(R.drawable.kanan_putih);
        }else if(anggota.getStatus().equals("Tidak Siap")){
            tidaksiap.setBackgroundResource(R.drawable.kanan_merah);
            siap.setBackgroundResource(R.drawable.kiri_putih);
        }
        if(anggota.getKeterangan()==null||anggota.getKeterangan().equals("")){
            keterangan.setText("Keterangan belum diatur");
        }else {
            keterangan.setText(anggota.getKeterangan());
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.hapus:
                final ProgressDialog pd=new ProgressDialog(getContext());
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                String url="http://sijali.developer.my.id/hapusanggota.php";
                //kirim request untuk menghapus anggota
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            //berhasil menghapus
                            Toast.makeText(getContext(), "Berhasil menghapus anggota", Toast.LENGTH_SHORT).show();
                        }else {
                            //gagal menghapus
                            Toast.makeText(getContext(), "Gagal menghapus anggota", Toast.LENGTH_SHORT).show();
                        }
                        pd.dismiss();
                        //setelah menghapus pindahkan layout ke list anggota
                        ((FragmentActivity)getContext()).getSupportFragmentManager().popBackStack();
                        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AnggotaList()).commit();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler untuk error jaringan
                        try {
                            error.printStackTrace();
                        }catch (NullPointerException e){
                            Log.d("errorAnggotaViewer", "Error volley");
                        }
                        Toast.makeText(getContext(), "Error jaringan", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        //apabila error alihkan layout ke list anggota
                        ((FragmentActivity)getContext()).getSupportFragmentManager().popBackStack();
                        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AnggotaList()).commit();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        //parameter untuk dikirim ke server dalam rangka menghapus anggota tertentu
                        Map<String, String> params=new HashMap<>();
                        params.put("username", anggota.getUsername());
                        return params;
                    }
                };

                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(request);
                pd.show();
                break;
        }
    }
}
