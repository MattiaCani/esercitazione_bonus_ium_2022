package com.unica.esbonusium2022mattiacani;

import android.net.Uri;

import java.io.Serializable;
import java.util.Calendar;

public class Utente implements Serializable {
    private String username;
    private String password;
    private String cittaProvenienza;
    private Integer picId;
    private Calendar dataNascita;
    private boolean adminFlag = false;
    private boolean isRootFlag = false;
    private Integer userId;

    //costruttore di default vuoto nel caso non passassimo niente
    public Utente(){
        this.setUsername("");
        this.setPassword("");
        this.setCittaProvenienza("");
        this.setPicId(1);
    }

    public Utente(String username, String password){
        this.setUsername(username);
        this.setPassword(password);
    }

    //costruttore di default
    public Utente(String username, String password, String cittaProvenienza, Calendar dataNascita) {
        this.username = username;
        this.password = password;
        this.cittaProvenienza = cittaProvenienza;
        this.dataNascita = dataNascita;
        this.adminFlag = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCittaProvenienza() {
        return cittaProvenienza;
    }

    public void setCittaProvenienza(String cittaProvenienza) {
        this.cittaProvenienza = cittaProvenienza;
    }

    public Calendar getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Calendar dataNascita) {
        this.dataNascita = dataNascita;
    }

    public boolean getAdmin() {
        return adminFlag;
    }

    public void setAdmin(boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getUserId(){
        return userId;
    }

    public boolean getRootFlag() {
        return isRootFlag;
    }

    public void setRootFlag(boolean rootFlag) {
        isRootFlag = rootFlag;
    }

    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
        this.picId = picId;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Utente){
            Utente u = (Utente) obj;

            return u.getUsername().equals(this.getUsername()) && u.getPassword().equals(this.getPassword());
        }

        return false;
    }

    @Override
    public int hashCode(){
        return this.username.hashCode() + this.password.hashCode();
    }



}

