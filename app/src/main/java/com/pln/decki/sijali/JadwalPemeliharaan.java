package com.pln.decki.sijali;


import android.app.ProgressDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
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
public class JadwalPemeliharaan extends Fragment implements View.OnClickListener{

    RecyclerView rv;
    List<Pekerjaan> pekerjaanList;
    ProgressDialog pd;
    Button cariButton;
    EditText bulan;
    EditText tahun;
    PemeliharaanAdapter pemeliharaanAdapter;
    View v;

    public JadwalPemeliharaan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_jadwal_pemeliharaan, container, false);

        //inisiasi tiap elemen pada layout
        prepareElement();

        //kirim request semua jadwal pemeliharaan
        final String url="http://sijali.developer.my.id/listpekerjaan.php";//"http://192.168.1.101/sijali/listpekerjaan.php";
        final JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Gson gson=new Gson();
                    //apabila mendapat respon, tambahkan semua respons ke list
                    for (int i=0; i<response.length();i++){
                        JSONObject jsonObject=(JSONObject)response.get(i);
                        Pekerjaan pekerjaan=gson.fromJson(jsonObject.toString(), Pekerjaan.class);
                        pekerjaanList.add(pekerjaan);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                pemeliharaanAdapter.notifyDataSetChanged();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handler untuk error jaringan
                try {
                    error.printStackTrace();
                }catch (NullPointerException err){
                    err.printStackTrace();
                }
                pd.dismiss();
                Toast.makeText(getContext(),"Error jaringan",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
        pd.show();

        return v;
    }

    private void prepareElement(){
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        pekerjaanList=new ArrayList<>();

        cariButton=(Button)v.findViewById(R.id.caributton);
        bulan=(EditText)v.findViewById(R.id.bulan);
        tahun=(EditText)v.findViewById(R.id.tahun);

        cariButton.setOnClickListener(this);

        //prepare recyclerview
        rv=(RecyclerView)v.findViewById(R.id.pemeliharaanrecyclerview);
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayout.VERTICAL,false);
        rv.setLayoutManager(llm);
        pemeliharaanAdapter=new PemeliharaanAdapter(getContext(), pekerjaanList);
        rv.setAdapter(pemeliharaanAdapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.caributton:
                pekerjaanList.clear();
                //krim request untuk pencarian jadwal dengan parameter tertentu
                final String url="http://sijali.developer.my.id/filterpekerjaan.php?bulan="+bulan.getText().toString()+"&tahun="+tahun.getText().toString();//"http://192.168.1.101/sijali/filterpekerjaan.php?bulan="+bulan.getText().toString()+"&tahun="+tahun.getText().toString();
                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            //tidak mendapatkan hasil
                            pd.dismiss();
                            Toast.makeText(getContext(),"Tidak ada hasil terkait",Toast.LENGTH_LONG).show();
                        }else {
                            Gson gson=new Gson();
                            //tambahkan semua respons ke list
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                                    Pekerjaan pekerjaan=gson.fromJson(jsonObject.toString(),Pekerjaan.class);
                                    pekerjaanList.add(pekerjaan);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pemeliharaanAdapter.notifyDataSetChanged();
                            pd.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler error jaringan
                        try {
                            error.printStackTrace();
                        }catch (NullPointerException e){
                            Log.d("errorJadwalPemeliharaan", "Error volley");
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(),"Error jaringan",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });

                RequestQueue queue=Volley.newRequestQueue(getContext());
                queue.add(request);
                pd.show();
                break;
        }
    }
}
