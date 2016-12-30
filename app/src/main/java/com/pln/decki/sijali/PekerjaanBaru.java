package com.pln.decki.sijali;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pln.decki.sijali.object.Pekerjaan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;


/**
 * A simple {@link Fragment} subclass.
 */
public class PekerjaanBaru extends Fragment implements View.OnClickListener{

    View v;

    Pekerjaan pekerjaan;

    List<String> nama;
    List<String> username;
    List<String> usernameBaru;
    List<String> namaBaru;

    EditText judulpekerjaan;
    EditText lokasipekerjaan;
    EditText detailpekerjaan;
    EditText tanggalpekerjaan;

    int haripekerjaan;
    int bulanpekerjaan;
    int tahunpekerjaan;

    Spinner spinnerManuver;
    Spinner spinnerK3;
    Spinner spinnerPekerjaan;

    DatePickerDialog datePicker;

    AnggotaBaruAdapter anggotaBaruAdapter;
    ArrayAdapter<String> adapter;

    ProgressDialog pd;

    Spinner anggotaSpinner;

    public PekerjaanBaru() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_pekerjaan_baru, container, false);

        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        //inisiasi elemen pada layout
        prepareElement();

        String url="http://sijali.developer.my.id/listusername.php";//"http://192.168.1.101/sijali/listusername.php";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        nama.add(i,jsonObject.getString("nama_lengkap_anggota"));
                        username.add(i,jsonObject.getString("username_anggota"));
                    }
                    adapter.notifyDataSetChanged();
                    if(pekerjaan!=null){
                        ArrayAdapter<String> adapter=(ArrayAdapter<String>)spinnerManuver.getAdapter();
                        if(username.indexOf(pekerjaan.getPenanggungjawabManuver())!=-1) spinnerManuver.setSelection(username.indexOf(pekerjaan.getPenanggungjawabManuver()));
                        if(username.indexOf(pekerjaan.getPenanggungjawabk3())!=-1)spinnerK3.setSelection(username.indexOf(pekerjaan.getPenanggungjawabk3()));
                        if(username.indexOf(pekerjaan.getPenanggungjawabPekerjaan())!=-1)spinnerPekerjaan.setSelection(username.indexOf(pekerjaan.getPenanggungjawabPekerjaan()));

                        List<String> usernameAnggotaList=pekerjaan.getAnggotaList();
                        for (int i=0; i<usernameAnggotaList.size(); i++){
                            usernameBaru.add(usernameAnggotaList.get(i));
                            if(username.indexOf(usernameAnggotaList.get(i))!=-1) namaBaru.add(nama.get(username.indexOf(usernameAnggotaList.get(i))));
                        }
                        anggotaBaruAdapter.notifyDataSetChanged();
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (JSONException err) {
                    err.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorPekerjaanBaru", "Error server");
                Toast.makeText(getContext(),"Error jaringan", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
        pd.show();

        return v;
    }

    private void prepareElement(){
        nama= new ArrayList<>();
        username=new ArrayList<>();
        usernameBaru=new ArrayList<>();
        namaBaru=new ArrayList<>();

        judulpekerjaan=(EditText)v.findViewById(R.id.judulpekerjaan);
        lokasipekerjaan=(EditText)v.findViewById(R.id.lokasipekerjaan);
        detailpekerjaan=(EditText)v.findViewById(R.id.detailpekerjaan);
        tanggalpekerjaan=(EditText)v.findViewById(R.id.haripekerjaan);
        prepareTanggal();

        tanggalpekerjaan.setOnClickListener(this);

        spinnerManuver=(Spinner)v.findViewById(R.id.penanggungjawab_manuver);
        spinnerK3=(Spinner)v.findViewById(R.id.penanggungjawab_k3);
        spinnerPekerjaan=(Spinner)v.findViewById(R.id.penanggungjawab_pekerjaan);
        anggotaSpinner=(Spinner)v.findViewById(R.id.anggota_spinner);

        Button tambah=(Button)v.findViewById(R.id.tambah);
        Button insert=(Button)v.findViewById(R.id.insert);
        Button update=(Button)v.findViewById(R.id.update);

        insert.setOnClickListener(this);
        tambah.setOnClickListener(this);
        update.setOnClickListener(this);

        prepareSpinner();

        prepareRecyclerView();

        Bundle bundle=getArguments();

        if (bundle==null){
            //state tambah pekerjaan, sembunyikan tombol update
            update.setVisibility(View.GONE);
        }else {
            //state update pekerjaan
            insert.setVisibility(View.GONE);
            pekerjaan=(Pekerjaan)getArguments().getSerializable("pekerjaan");
            judulpekerjaan.setText(pekerjaan.getJudulPekerjaan());
            String[] splittedTanggal=pekerjaan.getTanggalPekerjaan().split("-");
            tanggalpekerjaan.setText(getTanggalInGeneralForm(splittedTanggal[0],splittedTanggal[1],splittedTanggal[2]));
            lokasipekerjaan.setText(pekerjaan.getLokasiPekerjaan());
            detailpekerjaan.setText(pekerjaan.getDetailPekerjaan());

        }
    }

    private void prepareSpinner(){
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nama);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerManuver.setAdapter(adapter);
        spinnerK3.setAdapter(adapter);
        spinnerPekerjaan.setAdapter(adapter);
        anggotaSpinner.setAdapter(adapter);
    }

    private void prepareRecyclerView(){
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.anggota);
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        anggotaBaruAdapter=new AnggotaBaruAdapter(getContext(),usernameBaru,namaBaru);
        recyclerView.setAdapter(anggotaBaruAdapter);
    }

    private void prepareTanggal(){
        Calendar c=Calendar.getInstance();
        haripekerjaan=c.get(Calendar.DAY_OF_MONTH);
        bulanpekerjaan=c.get(Calendar.MONTH);
        tahunpekerjaan=c.get(Calendar.YEAR);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tambah:
                tambahAnggota();
                break;
            case R.id.insert:
                //validasi form pada layout
                FormValidator form=new FormValidator();
                if (!form.required(judulpekerjaan)){
                    break;
                }else if(!form.required(lokasipekerjaan)){
                    break;
                }else if (!form.required(tanggalpekerjaan)){
                    break;
                }else if(!form.required(detailpekerjaan)) {
                    break;
                }
                //request tambah pekerjaan
                String url="http://sijali.developer.my.id/insertpekerjaan.php";//"http://192.168.1.101/sijali/insertpekerjaan.php";
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length()>1||response.equals("1")){
                            //berhasil menambahkan pekerjaan
                            Toast.makeText(getContext(), "Berhasil menambahkan pekerjaan baru",Toast.LENGTH_LONG).show();
                        }else {
                            //gagal menambahkan pekerjaan
                            Toast.makeText(getContext(), "Gagal menambahkan pekerjaan baru",Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                        //pindah ke list jadwal pemeliharaan
                        ((FragmentActivity)getContext()).getSupportFragmentManager().popBackStack();
                        ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new JadwalPemeliharaan()).commit();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handler untuk error jaringan
                        try {
                            Log.d("errorPekerjaanBaru",String.valueOf(error.networkResponse.statusCode));
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                        pd.dismiss();
                        Toast.makeText(getContext(), "Error jaringan", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        //parameter untuk request tambah pekerjaan
                        String anggota= TextUtils.join(",", usernameBaru);
                        String tanggalpekerjaan=String.valueOf(tahunpekerjaan)+"-"+String.valueOf(bulanpekerjaan)+"-"+String.valueOf(haripekerjaan);
                        Map<String, String> params=new HashMap<>();
                        params.put("judulpekerjaan",judulpekerjaan.getText().toString());
                        params.put("lokasipekerjaan",lokasipekerjaan.getText().toString());
                        params.put("detailpekerjaan",detailpekerjaan.getText().toString());
                        params.put("tanggalpekerjaan",tanggalpekerjaan);
                        params.put("usernamemanuver", username.get(spinnerManuver.getSelectedItemPosition()));
                        params.put("usernamek3",username.get(spinnerK3.getSelectedItemPosition()));
                        params.put("usernamepekerjaan",username.get(spinnerPekerjaan.getSelectedItemPosition()));
                        params.put("anggotapekerjaan",anggota);
                        return params;
                    }
                };

                RequestQueue queue=Volley.newRequestQueue(getContext());
                request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*5,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
                pd.show();
                break;
            case R.id.haripekerjaan:
                tampilkanDatePicker();
                break;
            case R.id.update:

                //validasi form pada layout
                form=new FormValidator();
                if (!form.required(judulpekerjaan)){
                    break;
                }else if(!form.required(lokasipekerjaan)){
                    break;
                }else if (!form.required(tanggalpekerjaan)){
                    break;
                }else if(!form.required(detailpekerjaan)) {
                    break;
                }
                updatePekerjaan();
                break;
        }
    }

    private void tambahAnggota(){
        usernameBaru.add(username.get(anggotaSpinner.getSelectedItemPosition()));
        namaBaru.add(nama.get(anggotaSpinner.getSelectedItemPosition()));
        anggotaBaruAdapter.notifyDataSetChanged();
    }

    private String getTanggalInGeneralForm(){
        String tanggal=String.valueOf(haripekerjaan)+"-"+String.valueOf(bulanpekerjaan)+"-"+String.valueOf(tahunpekerjaan);
        return tanggal;
    }

    private String getTanggalInGeneralForm(String tahun, String bulan, String hari){
        String tanggal=String.valueOf(hari)+"-"+String.valueOf(bulan)+"-"+String.valueOf(tahun);
        return tanggal;
    }

    private String getTanggalInReversedOrder(){
        String tanggal=String.valueOf(tahunpekerjaan)+"-"+String.valueOf(bulanpekerjaan)+"-"+String.valueOf(haripekerjaan);
        return tanggal;
    }

    private void tampilkanDatePicker(){
        Calendar newCalendar=Calendar.getInstance();
        datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                haripekerjaan=dayOfMonth;
                bulanpekerjaan=monthOfYear+1;
                tahunpekerjaan=year;

                tanggalpekerjaan.setText(getTanggalInGeneralForm());
            }
        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void updatePekerjaan(){
        //reqest update pekerjaan
        String url="http://sijali.developer.my.id/updatepekerjaan.php";//"http://192.168.1.101/sijali/updatepekerjaan.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>1||response.equals("1")){
                    //berhasil memperbarui pekerjaan
                    Toast.makeText(getContext(), "Berhasil memperbarui pekerjaan baru",Toast.LENGTH_LONG).show();
                }else {
                    //gagal memperbarui pekerjaan
                    Toast.makeText(getContext(), "Gagal memperbarui pekerjaan baru",Toast.LENGTH_LONG).show();
                }
                pd.dismiss();
                //pindah ke layout list jadwal pemeliharaan
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new JadwalPemeliharaan()).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handerl error jaringan
                Toast.makeText(getContext(),"Error jaringan", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                //parameter untuk dikirim bersama request ke server
                String anggota= TextUtils.join(",", usernameBaru);
                String tanggalpekerjaan=getTanggalInReversedOrder();
                Map<String, String> params=new HashMap<>();
                params.put("idpekerjaan",pekerjaan.getIdPekerjaan());
                params.put("judulpekerjaan",judulpekerjaan.getText().toString());
                params.put("lokasipekerjaan",lokasipekerjaan.getText().toString());
                params.put("detailpekerjaan",detailpekerjaan.getText().toString());
                params.put("tanggalpekerjaan",tanggalpekerjaan);
                params.put("usernamemanuver", username.get(spinnerManuver.getSelectedItemPosition()));
                params.put("usernamek3",username.get(spinnerK3.getSelectedItemPosition()));
                params.put("usernamepekerjaan",username.get(spinnerPekerjaan.getSelectedItemPosition()));
                params.put("anggotapekerjaan",anggota);
                return params;
            }
        };

        RequestQueue queue=Volley.newRequestQueue(getContext());
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*5,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        pd.show();
    }
}
