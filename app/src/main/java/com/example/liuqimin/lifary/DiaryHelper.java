package com.example.liuqimin.lifary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Created by liuqimin on 15-07-07.
 */
public class DiaryHelper {

    private int id;
    private String date;
    private String text;
    private float latitude, longitude;
    private int share;
    //private byte[]img;
    private String image;
    //private byte[] sound;
    private String sound;

    public  DiaryHelper(){}

    public  DiaryHelper(int a){
        Calendar c= Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = month + "-" + day + "-" + year + "  " +
                hour + ":" + minute + ":" + seconds;

        //Log.d("Lifary", "date ===== " + date);

        id  = a;
        //img = null;
        image = "image";
        //sound = null;
        sound = "sound";
        text = "text";
        latitude = 0;
        longitude = 0;
        share = 0;
    }
    public void addImage(Bitmap bmp){
        byte [] img = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        img = os.toByteArray();
        //Log.d("fb", "copy byte b to img , img.size = " + img.length);
        image = Base64.encodeToString(img, Base64.DEFAULT);
        if (image == null)
            image = "";
        //Log.d("fb", "image: " + image);
    }

    public void addSound(byte[] audioByte){
        try {
            byte[] sound_temp = null;
            sound_temp = audioByte;
            //Log.d("fb", "sound.size = " + sound_temp.length);
            sound = Base64.encodeToString(sound_temp, Base64.DEFAULT);
        }
        catch (Exception e){
            Log.d("fb", "sound wrong");
        }
        //Log.d("fb", "sound: " + sound);
    }

    public void addLocation(float lat, float lon){
        latitude = lat;
        longitude = lon;
        //Log.d("fb", "lat: " + latitude +" long:" + longitude);
    }
    public void setShare(int s){
        share = s;
    }
    public void setDate(String d){date = d;}
    public void setId(int i){
        id = i;
        //Log.d("fb", "lat: " + id);
    }

    public void convert(Diary d){
        id = d.getId();
        date = d.getDate();
        sound =d.getSound();
        text = d.getText();
        image = d.getImage();
        latitude = d.getLatitude();
        longitude = d.getLongitude();
        share = d.getShare();
    }

    public void convert(DiaryHelper d){
        id = d.getId();
        date = d.getDate();
        sound =d.getSound();
        text = d.getText();
        image = d.getImage();
        latitude = d.getLatitude();
        longitude = d.getLongitude();
        share = d.getShare();
    }

    public void past(DiaryHelper d){
        d.setId(id);
        d.setDate(date);
        d.setShare(share);
        d.setSound(sound);
        d.setLocation(latitude, longitude);
        d.setText (text);
        d.setImage(image);
        //id = d.getId();
        //date = d.getDate();
        //sound =d.getSound();
        //text = d.getText();
        //image = d.getImage();
        //latitude = d.getLatitude();
        //longitude = d.getLongitude();
        //share = d.getShare();
        Log.d("fb", "Done convert");
    }

    private void setText(String text) {
        this.text = text;
    }

    private void setLocation(float v, float v1) {
        longitude = v; latitude = v1;
    }

    private void setSound(String s) {
        sound = s;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public int getId(){ return id;}
    public String getDate(){return date;}
    public String getSound(){return sound;}
    public String getText(){return text;}
    public String getImage(){return image;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}
    public int getShare(){return share;}
    public void print(){
        Log.d("fb","pringting diar in diary helper:");
        Log.d("fb", "image: " + image);
        Log.d("fb", "id" +id);
        Log.d("fb", "date" +date);
        Log.d("fb", "text" +text);
        Log.d("fb", "lat" +latitude +" long:" +longitude);
        Log.d("fb", "share" +share);
        //private byte[]img;
        //private byte[] sound;
        Log.d("fb", "sound" +sound);
    }


}
