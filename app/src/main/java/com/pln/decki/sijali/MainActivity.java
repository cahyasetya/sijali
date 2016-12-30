package com.pln.decki.sijali;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.pln.decki.sijali.object.Pekerjaan;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String PREF_NAME="sijali_user_session";
    private final static String IS_LOGIN="IsLogedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PekerjaanBaru()).commit();
            }
        });

        Gson gson=new Gson();
        SharedPreferences preferences=getSharedPreferences("sijali", 0);
        String anggotaString=preferences.getString("anggota", null);
        Anggota anggota=gson.fromJson(anggotaString, Anggota.class);
        if(anggota.getHak_akses_anggota()==0){
            fab.setVisibility(View.GONE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String action=getIntent().getStringExtra("fragment");
        if(action!=null){
            PemeliharaanViewer pv=new PemeliharaanViewer();
            Bundle bundle=new Bundle();
            Pekerjaan pekerjaan=(Pekerjaan)gson.fromJson(getIntent().getStringExtra("pekerjaan"),Pekerjaan.class);
            bundle.putSerializable("pemeliharaan",pekerjaan);
            pv.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pv).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JadwalPemeliharaan()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        //event handler saat tombol back di click
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout){
            final ProgressDialog pd=new ProgressDialog(this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);

            //request untuk logout
            String url="http://sijali.developer.my.id/hapustoken.php";
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("1")){
                        //berhasil logout dari server
                        //hapus semua identitas user yang telah login
                        SharedPreferences pref=getSharedPreferences("sijali", 0);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.clear();
                        editor.commit();
                        //beralih ke login page
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }else {
                        //gagal logout dari server
                        Toast.makeText(MainActivity.this, "Gagal logout dari server, coba lagi", Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //handler untuk error jaringan
                    try {
                        error.printStackTrace();
                    }catch (NullPointerException e){
                        Log.d("errorLogOut", "Error tidak diketahui");
                    }
                    Toast.makeText(MainActivity.this, "Error jaringan", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    //parameter untuk logout dari server
                    Gson gson=new Gson();
                    SharedPreferences preferences=getSharedPreferences("sijali", 0);
                    Anggota anggota=gson.fromJson(preferences.getString("anggota", null), Anggota.class);
                    Map<String, String> params=new HashMap<>();
                    params.put("username", anggota.getUsername());
                    return params;
                }
            };

            RequestQueue queue= Volley.newRequestQueue(this);
            queue.add(request);
            pd.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //handler untuk menu saat diklik
        if (id == R.id.jadwalpemeliharaan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JadwalPemeliharaan()).commit();
        } else if (id == R.id.dataanggota) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AnggotaList()).commit();
        } else if (id == R.id.pengaturan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Pengaturan()).commit();
        } else if (id == R.id.tentang){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Tentang()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
