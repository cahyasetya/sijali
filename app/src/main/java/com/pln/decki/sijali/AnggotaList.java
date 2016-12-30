package com.pln.decki.sijali;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pln.decki.sijali.object.Anggota;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnggotaList extends Fragment implements View.OnClickListener{

    private List<Anggota> anggotaList;

    private Button siapButton;
    private Button tidakSiapButton;
    private RecyclerView rv;

    private AnggotaAdapter anggotaAdapter;
    private LinearLayoutManager llm;
    private View v;

    private ProgressDialog pd;


    public AnggotaList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_anggota_list, container, false);

        //inisiasi tiap elemen pada layout
        init(v);


        anggotaList=new ArrayList<>();
        anggotaList.clear();

        showProgressDialog();

        //setting recycler view untuk list
        rv=(RecyclerView)v.findViewById(R.id.anggotarecyclerview);
        rv.setHasFixedSize(true);
        llm=new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        anggotaAdapter=new AnggotaAdapter(getContext(), anggotaList);
        rv.setAdapter(anggotaAdapter);


        //siapkan data awal
        String url="http://sijali.developer.my.id/listanggota.php";//"http://192.168.1.101/sijali/listanggota.php";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson=new Gson();
                try {
                    for (int i=0; i<response.length(); i++){
                        JSONObject anggotaJson=(JSONObject)response.get(i);
                        Anggota anggota=gson.fromJson(String.valueOf(anggotaJson), Anggota.class);
                        anggotaList.add(anggota);
                    }
                    anggotaAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    error.printStackTrace();
                }catch (NullPointerException e){
                    Log.d("errorAnggotaList", "error volley");
                    e.printStackTrace();
                }
                Toast.makeText(getContext(),"Error jaringan", Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        return v;
    }

    private void init(View v){
        //inisiasi tiap elemen
        siapButton=(Button)v.findViewById(R.id.siapbutton);
        tidakSiapButton=(Button)v.findViewById(R.id.tidaksiapbutton);

        siapButton.setOnClickListener(this);
        tidakSiapButton.setOnClickListener(this);

        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }

    private void ambilDataDariServer(){

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.siapbutton:
                //hapus list anggota
                anggotaList.clear();
                //kirim request untuk data anggota yang siap
                String url="http://sijali.developer.my.id/listanggotasiap.php";
                JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            try {
                                Gson gson=new Gson();
                                for (int i = 0; i < response.length(); i++) {
                                    //tambahkan tiap item ke list
                                    JSONObject jsonObject = (JSONObject) response.get(i);
                                    Anggota anggota = gson.fromJson(String.valueOf(jsonObject), Anggota.class);
                                    anggotaList.add(anggota);
                                }
                                anggotaAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else Toast.makeText(getContext(), "Semua anggota tidak siap", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler apabila terdapat error pada transaksi jaringan
                        try {
                            error.printStackTrace();
                        }catch (NullPointerException e){
                            Log.d("errorAnggotaList", "Error volley");
                            e.printStackTrace();
                        }
                        Log.d("errorAnggotaList", String.valueOf(error.networkResponse.statusCode));
                        hideProgressDialog();
                    }
                });

                RequestQueue queue=Volley.newRequestQueue(getContext());
                queue.add(request);

                showProgressDialog();
                break;
            case R.id.tidaksiapbutton:
                anggotaList.clear();
                url="http://sijali.developer.my.id/listanggotatidaksiap.php";
                //kirim request untuk anggota yang tidak siap
                request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            try {
                                Gson gson = new Gson();
                                for (int i = 0; i < response.length(); i++) {
                                    //tambahkan tiap item ke list
                                    JSONObject jsonObject = (JSONObject) response.get(i);
                                    Anggota anggota = gson.fromJson(String.valueOf(jsonObject), Anggota.class);
                                    anggotaList.add(anggota);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            anggotaAdapter.notifyDataSetChanged();
                        } else {
                            //semua anggota siap
                            Toast.makeText(getContext(), "Semua anggota siap", Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler untuk error jaringan
                        try {
                            error.printStackTrace();
                        }catch (NullPointerException e){
                            Log.d("errorAnggotaList", "Error volley");
                        }
                        Toast.makeText(getContext(), "Error jaringan", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                });
                queue=Volley.newRequestQueue(getContext());
                queue.add(request);

                showProgressDialog();

                break;
        }
    }

    private void showProgressDialog(){
        pd.show();
    }

    private void hideProgressDialog(){
        pd.dismiss();
    }
}
