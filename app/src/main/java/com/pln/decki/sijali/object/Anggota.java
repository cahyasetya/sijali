package com.pln.decki.sijali.object;

import java.io.Serializable;

/**
 * Created by cahya on 9/1/16.
 */
public class Anggota implements Serializable{
    private String username_anggota=null;
    private String nama_panggilan_anggota=null;
    private String nama_lengkap_anggota=null;
    private String nip_anggota=null;
    private String jabatan_anggota=null;
    private String no_hp_anggota=null;
    private String status_anggota=null;
    private String keterangan_anggota=null;
    private int hak_akses_anggota;

    public Anggota(){

    }

    public void setHak_akses_anggota(int hak_akses_anggota){
        this.hak_akses_anggota=hak_akses_anggota;
    }

    public int getHak_akses_anggota(){
        return this.hak_akses_anggota;
    }

    public void setUsername(String username_anggota){
        this.username_anggota=username_anggota;
    }

    public String getUsername(){
        return this.username_anggota;
    }

    public void setNamaLengkap(String nama_lengkap_anggota){
        this.nama_lengkap_anggota=nama_lengkap_anggota;
    }

    public String getNamaLengkap(){
        return this.nama_lengkap_anggota;
    }

    public void setNip(String nip_anggota){
        this.nip_anggota=nip_anggota;
    }

    public String getNip(){
        return this.nip_anggota;
    }

    public void setJabatan(String jabatan_anggota){
        this.jabatan_anggota=jabatan_anggota;
    }

    public String getJabatan(){
        return this.jabatan_anggota;
    }

    public void setNoHp(String no_hp_anggota){
        this.no_hp_anggota=no_hp_anggota;
    }

    public String getNoHp(){
        return this.no_hp_anggota;
    }

    public void setStatus(String status_anggota){
        this.status_anggota=status_anggota;
    }

    public String getStatus(){
        return this.status_anggota;
    }

    public void setNamaPanggilan(String nama_panggilan_anggota){
        this.nama_panggilan_anggota=nama_panggilan_anggota;
    }

    public String getNamaPanggilan(){
        return this.nama_panggilan_anggota;
    }

    public void setKeterangan(String keterangan_anggota){
        this.keterangan_anggota=keterangan_anggota;
    }

    public String getKeterangan(){
        return this.keterangan_anggota;
    }
}
