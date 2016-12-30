package com.pln.decki.sijali.object;

import android.util.ArrayMap;
import android.util.Pair;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cahya on 9/1/16.
 */
public class Pekerjaan implements Serializable {
    private String id_pekerjaan=null;
    private String judul_pekerjaan=null;
    private String tanggal_pekerjaan=null;
    private String detail_pekerjaan=null;
    private String lokasi_pekerjaan=null;
    private String username_penanggungjawab_manuver=null;
    private String username_penanggungjawab_k3=null;
    private String username_penanggungjawab_pekerjaan=null;
    private String anggota_pekerjaan=null;

    public Pekerjaan(){
    }

    public void setIdPekerjaan(String id_pekerjaanekerjaan){
        this.id_pekerjaan=id_pekerjaan;
    }

    public String getIdPekerjaan(){
        return this.id_pekerjaan;
    }

    public void setJudulPekerjaan(String judulPekerjaan){
        this.judul_pekerjaan=judulPekerjaan;
    }

    public String getJudulPekerjaan(){
        return this.judul_pekerjaan;
    }

    public void setTanggalPekerjaan(String tanggal_pekerjaan){
        this.tanggal_pekerjaan=tanggal_pekerjaan;
    }

    public String getTanggalPekerjaan(){
        return this.tanggal_pekerjaan;
    }

    public void setDetailPekerjaan(String string){
        this.detail_pekerjaan=detail_pekerjaan;
    }

    public String getDetailPekerjaan(){
        return this.detail_pekerjaan;
    }

    public void setLokasiPekerjaan(String lokasi_pekerjaan){
        this.lokasi_pekerjaan=lokasi_pekerjaan;
    }

    public String getLokasiPekerjaan(){
        return this.lokasi_pekerjaan;
    }

    public void setPenanggungjawabManuver(String  username_penanggungjawab_manuver){
        this.username_penanggungjawab_manuver=username_penanggungjawab_manuver;
    }

    public String getPenanggungjawabManuver(){
        return this.username_penanggungjawab_manuver;
    }

    public void setPenanggungjawabk3(String username_penanggungjawab_k3){
        this.username_penanggungjawab_k3=username_penanggungjawab_k3;
    }

    public String getPenanggungjawabk3(){
        return this.username_penanggungjawab_k3;
    }

    public void setPenanggungjawabPekerjaan(String username_penanggungjawab_pekerjaan){
        this.username_penanggungjawab_pekerjaan=username_penanggungjawab_pekerjaan;
    }

    public String getPenanggungjawabPekerjaan(){
        return this.username_penanggungjawab_pekerjaan;
    }

    public void setAnggota_pekerjaan(String anggota_pekerjaan){
        this.anggota_pekerjaan=anggota_pekerjaan;
    }

    public String getAnggota(){
        return this.anggota_pekerjaan;
    }

    public List<String> getAnggotaList(){
        String[] anggotaList=anggota_pekerjaan.split(",");
        List<String> retVal= Arrays.asList(anggotaList);
        return retVal;
    }
}
