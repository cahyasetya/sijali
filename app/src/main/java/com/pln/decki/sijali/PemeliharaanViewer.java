package com.pln.decki.sijali;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pln.decki.sijali.object.Anggota;
import com.pln.decki.sijali.object.Pekerjaan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PemeliharaanViewer extends Fragment implements View.OnClickListener{

    TextView judulpekerjaan;
    TextView lokasipekerjaan;
    TextView detailpekerjaan;
    TextView tanggalpekerjaan;
    TextView penanggungjawabmanuver;
    TextView penanggungjawabk3;
    TextView penanggungjawabpekerjaan;
    List<String> anggotaList;
    List<String> anggotaListTemp;
    List<String> username;
    List<String> nama;
    Button hapus;
    Button update;
    View v;
    ProgressDialog pd;
    Pekerjaan pekerjaan;
    AnggotaBaruAdapter adapter;

    public PemeliharaanViewer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_pemeliharaan_viewer, container, false);
        //terima paramter dari layout sebelumnya
        Bundle bundle=getArguments();
        if(bundle.containsKey("pemeliharaan"))
        pekerjaan=(Pekerjaan)bundle.getSerializable("pemeliharaan");

        username=new ArrayList<>();
        nama=new ArrayList<>();

        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        anggotaList=new ArrayList<>();
        anggotaListTemp=new ArrayList<>();
        anggotaListTemp=pekerjaan.getAnggotaList();

        //siapkan paramter untuk dropdown
        String url="http://sijali.developer.my.id/listnama.php";//"http://192.168.1.101/sijali/listnama.php";
        JSONArray array=new JSONArray();
        array.put(pekerjaan.getPenanggungjawabManuver());
        array.put(pekerjaan.getPenanggungjawabk3());
        array.put(pekerjaan.getPenanggungjawabPekerjaan());
        for (int i=0; i<anggotaListTemp.size(); i++){
            array.put(anggotaListTemp.get(i));
        }

        //kirimkan request dan terapkan hasil pada elemen
        final JsonArrayRequest request=new JsonArrayRequest(Request.Method.POST, url, array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject object=response.getJSONObject(i);
                        username.add(object.getString("username_anggota"));
                        nama.add(object.getString("nama_lengkap_anggota"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //set nilai penanggungjawab berdasarkan yang telah diklik pada layout sebelumnya
                if(username.indexOf(pekerjaan.getPenanggungjawabManuver())!=-1) {
                    penanggungjawabmanuver.setText(nama.get(username.indexOf(pekerjaan.getPenanggungjawabManuver())));
                }else {
                    penanggungjawabmanuver.setText("Penanggungjawab Manuver telah dihapus");
                }
                if(username.indexOf(pekerjaan.getPenanggungjawabk3())!=-1){
                    penanggungjawabk3.setText(nama.get(username.indexOf(pekerjaan.getPenanggungjawabk3())));
                }else {
                    penanggungjawabk3.setText("Penanggungjawab K3 telah dihapus");
                }
                if(username.indexOf(pekerjaan.getPenanggungjawabPekerjaan())!=-1){
                    penanggungjawabpekerjaan.setText(nama.get(username.indexOf(pekerjaan.getPenanggungjawabPekerjaan())));
                }else {
                    penanggungjawabpekerjaan.setText("Penanggungjawab Pekerjaan telah dihapus");
                }

                    for (int i=0; i<anggotaListTemp.size(); i++){
                        if(anggotaListTemp.get(i)!=null || !anggotaListTemp.get(i).equals("")){
                            if(username.indexOf(anggotaListTemp.get(i))!=-1)anggotaList.add(nama.get(username.indexOf(anggotaListTemp.get(i))));
                        }
                    }
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handler error jaringan
                try {
                    error.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d("errorPemeliharaanView","Error volley");
                }
                Toast.makeText(getContext(),"Error jaringan",Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });

        RequestQueue queue=Volley.newRequestQueue(getContext());
        queue.add(request);
        pd.show();

        judulpekerjaan=(TextView)v.findViewById(R.id.judulpekerjaan);
        detailpekerjaan=(TextView)v.findViewById(R.id.detailpekerjaan);
        detailpekerjaan.setMovementMethod(new ScrollingMovementMethod());
        lokasipekerjaan=(TextView)v.findViewById(R.id.lokasipekerjaan);
        tanggalpekerjaan=(TextView)v.findViewById(R.id.tanggalpekerjaan);
        penanggungjawabmanuver=(TextView)v.findViewById(R.id.penanggungjawab_manuver);
        penanggungjawabk3=(TextView)v.findViewById(R.id.penanggungjawab_k3);
        penanggungjawabpekerjaan=(TextView)v.findViewById(R.id.penanggungjawab_pekerjaan);

        //inisiasi elemen berdasarkan nilai dari layout sebelumnya
        judulpekerjaan.setText(pekerjaan.getJudulPekerjaan());
        tanggalpekerjaan.setText(pekerjaan.getTanggalPekerjaan());
        detailpekerjaan.setText(pekerjaan.getDetailPekerjaan());
        lokasipekerjaan.setText(pekerjaan.getLokasiPekerjaan());
        hapus=(Button)v.findViewById(R.id.hapus);
        hapus.setOnClickListener(this);
        update=(Button)v.findViewById(R.id.update);
        update.setOnClickListener(this);

        //tampilkan atau sembunyikan item yang hanya boleh diakses oleh admin
        Gson gson=new Gson();
        SharedPreferences preferences=getContext().getSharedPreferences("sijali",0);
        Anggota anggota=gson.fromJson(preferences.getString("anggota",null),Anggota.class);
        if (anggota.getHak_akses_anggota()==0){
            update.setVisibility(View.GONE);
            hapus.setVisibility(View.GONE);
        }

        RecyclerView rv=(RecyclerView)v.findViewById(R.id.anggotarecyclerview);
        LinearLayoutManager lm=new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        adapter=new AnggotaBaruAdapter(getContext(), anggotaList);
        rv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.hapus:
                //request untuk menghapus jadwal pemeliharaan
                String url="http://sijali.developer.my.id/hapuspekerjaan.php";//"http://192.168.1.101/sijali/hapuspekerjaan.php";

                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //berhasil menghapus, pindah ke list jadwal pemeliharaan
                        Toast.makeText(getContext(), response.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new JadwalPemeliharaan()).commit();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler error jaringan
                        pd.dismiss();
                        Toast.makeText(getContext(),"Error jaringan",Toast.LENGTH_LONG).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        //parameter untuk dikirimkan ke server
                        Map<String, String> params=new HashMap<>();
                        params.put("idpekerjaan", pekerjaan.getIdPekerjaan().toString());
                        return  params;
                    }
                };

                RequestQueue queue= Volley.newRequestQueue(getContext());
                queue.add(request);
                pd.show();
                break;
            case R.id.update:
                //update jadwal pemeliharaan, pindah ke layout lain
                PekerjaanBaru pb=new PekerjaanBaru();
                Bundle bundle=new Bundle();
                bundle.putSerializable("pekerjaan",pekerjaan);
                pb.setArguments(bundle);
                ((FragmentActivity)getContext()).getSupportFragmentManager().popBackStack();
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().addToBackStack("updatepekerjaan").replace(R.id.fragment_container,pb).commit();
                break;
        }
    }
}
