package com.example.liuqimin.lifary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class DiaryView extends Activity implements View.OnClickListener, Communication{

    private Diary diary;
    TextView date, share, content, location;
    ImageView img;
    ImageButton audioPlay;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;

    private Firebase rootRef = new Firebase("https://kimmyblog.firebaseio.com/");
    private double diaryCounter = 0;
    private DiaryHelper targetPost = null;
    private Diary t_taget = null;
    //private Diary targetDiary;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_view);
        date = (TextView) findViewById(R.id.timeTextView);
        share = (TextView) findViewById(R.id.shareTextView);
        content = (TextView) findViewById(R.id.diaryTextView);
        location = (TextView) findViewById(R.id.locationTextView);
        img = (ImageView) findViewById(R.id.diaryImageView);
        audioPlay = (ImageButton) findViewById(R.id.playAudioButton);




        targetPost = new DiaryHelper(3);
        t_taget = new Diary(3);
        Firebase refA = rootRef.child("2").child("list");
        refA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot2) {
                diaryCounter = snapshot2.getChildrenCount();
                Log.d("fb", "@diaryView there are " + diaryCounter + " diaries");
                for (DataSnapshot postSnapshot2 : snapshot2.getChildren()) {
                    try {
                        DiaryHelper popo = postSnapshot2.getValue(DiaryHelper.class);
                        //popo.print();
                        if (popo.getId() == 2) {
                            Log.d("fb", "Things that matter");
                            //popo.print();
                            targetPost.convert(popo);
                            //targetPost.print();
                            //Log.d("fb", "diary convert damn it");
                            t_taget.convert(popo);
                            t_taget.print();
                            //t.print();
                            Log.d("fb", "omfg");

                            diary = t_taget;
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
                            diary.addImage(b);
                            //diary.setImageByByte();
                            Log.d("fb", "diary print");
                            diary.print();
                            Log.d("fb", "diary print done");
                            if(diary != null){

                                date.setText(diary.getDate());      // set date
                                content.setText(diary.getContent());    // set diary text

                                // set share
                                if(diary.getShare() == 0){share.setText("privately");}
                                else{share.setText("publicly");}

                                // get location
                                float longitude = diary.getLongitude();
                                float latitude = diary.getLatitude();

                                if(longitude == 0 || latitude == 0){
                                    location.setText("not available");
                                }
                                else {
                              /*      Communication cc = (Communication) this;
                                    ReadLocation readLocation = new ReadLocation(this, cc);
                                    readLocation.getLocation("" + latitude, "" + longitude);
                                    */
                                }

                                try{
                                    Display display = getWindowManager().getDefaultDisplay();
                                    Point size = new Point();
                                    display.getSize(size);
                                    int width = size.x;

                                    Bitmap bitmap = diary.getImgBitmap();
                                    Log.d("Lifary", "DiaryView: bitmap.getHeight = " + bitmap.getHeight() +
                                            "\tbitmap.getWidth = " + bitmap.getWidth() +
                                            "\t imageView width = " + width);
                                    int nh = (int) ( bitmap.getHeight() * (width / bitmap.getWidth()) );
                                    Log.d("Lifary", "DiaryView: nh = " + nh +
                                            " result = " + (int)( bitmap.getHeight() * (512 / bitmap.getWidth()))  +
                                            " result 2 = " +( bitmap.getHeight()*(512/bitmap.getWidth())));
                                    bitmap = Bitmap.createScaledBitmap(bitmap, width, nh, true);
                                    img.setImageBitmap(bitmap);
                                    img.setVisibility(View.VISIBLE);
                                    Log.d("Lifary", "DiaryView: bitmap is loaded ");
                                }catch (Exception e){
                                    Log.d("Lifary", "failed to load image ERROR: " + e.getLocalizedMessage());
                                    img.setVisibility(View.GONE);
                                }

                            }
                        }
                    } catch (FirebaseException e) {
                        Log.d("fb", "exception :" + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("fb", "The read failed: " + firebaseError.getMessage());
            }
        });


        audioPlay.setOnClickListener(this);

        //diary = t_taget;
        //diary = null;
        //diary = targetDiary;
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int id = extra.getInt("DIARY_ID");
            Log.d("Lifary", "diary get the date: id = " + id);
            DiaryDBHandler myDiaryDBHandler = new DiaryDBHandler(this, null, null, 1);
            try {
                //diary = myDiaryDBHandler.findDiaryByID(id);
                Log.d("Lifary", "found diary");
            }catch (Exception e){
                Log.d("Lifary", "diary found ERROR: " + e.getLocalizedMessage());
            }
        }

        if(diary != null){

            date.setText(diary.getDate());      // set date
            content.setText(diary.getContent());    // set diary text

            // set share
            if(diary.getShare() == 0){share.setText("privately");}
            else{share.setText("publicly");}

            // get location
            float longitude = diary.getLongitude();
            float latitude = diary.getLatitude();

            if(longitude == 0 || latitude == 0){
                location.setText("not available");
            }
            else {
                Communication cc = (Communication) this;
                ReadLocation readLocation = new ReadLocation(this, cc);
                readLocation.getLocation("" + latitude, "" + longitude);
            }

            try{
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;

                Bitmap bitmap = diary.getImgBitmap();
                Log.d("Lifary", "DiaryView: bitmap.getHeight = " + bitmap.getHeight() +
                        "\tbitmap.getWidth = " + bitmap.getWidth() +
                        "\t imageView width = " + width);
                int nh = (int) ( bitmap.getHeight() * (width / bitmap.getWidth()) );
                Log.d("Lifary", "DiaryView: nh = " + nh +
                        " result = " + (int)( bitmap.getHeight() * (512 / bitmap.getWidth()))  +
                        " result 2 = " +( bitmap.getHeight()*(512/bitmap.getWidth())));
                bitmap = Bitmap.createScaledBitmap(bitmap, width, nh, true);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                Log.d("Lifary", "DiaryView: bitmap is loaded ");
            }catch (Exception e){
                Log.d("Lifary", "failed to load image ERROR: " + e.getLocalizedMessage());
                img.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onStart(){
        super .onStart();
        //Log.d("fb", "on start t print");
        //t_taget.print();
        //Log.d("fb", "oon start t print done");
    }

    @Override
    protected void onResume(){
        super .onResume();
        //Log.d("fb", "on Resume t print");
        //t_taget.print();
        //Log.d("fb", "oon Resume t print done");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == audioPlay && !isPlaying && diary.getAudio() != null) {
                playMp3(diary.getAudio());
            } else if (v == audioPlay && isPlaying) {
                stopPlaying();
            }else if (v == audioPlay && !isPlaying && diary.getAudio() == null){
                Toast.makeText(this, "Sorry, No Audio can be played", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.d("Lifary", "DiaryView: play audio ERROR: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void com(String contents) {
        location.setText(contents);
    }

    private void playMp3(byte[] mp3SoundByteArray)
    {
        try
        {

            File path=new File(getCacheDir()+"/musicfile.3gp");

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer = new MediaPlayer();

            FileInputStream fis = new FileInputStream(path);
            mediaPlayer.setDataSource(getCacheDir()+"/musicfile.3gp");

            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            audioPlay.setImageResource(R.drawable.player_stop);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
        }
        catch (IOException ex)
        {
            String s = ex.toString();
            ex.printStackTrace();
            Log.d("Lifary", "DiaryView: play Mp3 ERROR: " + ex.getLocalizedMessage() );
        }
    }

    // -------------------------- Stop Playing Media File --------------------------------
    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
        audioPlay.setImageResource(R.drawable.play_sound);

    }

}


