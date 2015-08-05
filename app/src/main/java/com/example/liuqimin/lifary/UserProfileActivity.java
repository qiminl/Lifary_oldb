package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URLEncoder;


public class UserProfileActivity extends Activity {


    TextView usrProfile;
    ImageView usrProtraite;
    ImageView qrCode;
    Button newDiaryButton;
    Button addFriendButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usrProfile = (TextView) findViewById(R.id.usernameProfileText);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("la.droid.qr.scan");    // scan function
                i.putExtra( "la.droid.qr.complete" , true);
                try {
                    //start activity
                    startActivityForResult(i, 1);
                } catch(ActivityNotFoundException ex) {
                    // if QRcodeDroid is not installed, then install it.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=la.droid.qr"));
                    startActivity(intent);
                }
            }
        });
        qrCode = (ImageView) findViewById(R.id.QRcodeImg);
        int userID;
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                userID = extras.getInt("USER_ID");
                MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
                user = myDBHandler.findUserByID(userID);
                usrProfile.setText(user.getUsername());
            }
        }
        newDiaryButton = (Button) findViewById(R.id.newDiaryButton);
        newDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditDiaryActivity.class);
                startActivity(i);
            }
        });

        try {
            String encodeUrl = URLEncoder.encode("" + user.getID());
            Intent i = new Intent("la.droid.qr.encode");
            i.putExtra("la.droid.qr.code", encodeUrl);
            i.putExtra("la.droid.qr.size", 200);
            i.putExtra("la.droid.qr.image", true);
            try {
                startActivityForResult(i, 0);
            }catch (ActivityNotFoundException ex){
                Log.d("Lifary", "UserProfile: startActivity ERROR: " + ex.getLocalizedMessage());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=la.droid.qr"));
                startActivity(intent);
            }
        }catch(Exception e){
            Log.d("Lifary", "UserProfile: encode QRcode ERROR: " + e.getLocalizedMessage());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( 0==requestCode && null!=data && data.getExtras()!=null ) {
            //產生的QRCode Image 路徑，存放在 key 為 la.droid.qr.result 的值中
            String qrcodeImgPath = data.getExtras().getString("la.droid.qr.result");
            Uri imgPath = Uri.fromFile(new File(qrcodeImgPath));
            qrCode.setImageURI(imgPath);
        }

        if( 1==requestCode && null!=data && data.getExtras()!=null ) {
            //掃描結果存放在 key 為 la.droid.qr.result 的值中
            String result = data.getExtras().getString("la.droid.qr.result");
            int id = Integer.parseInt(result.toString());
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            User friend = dbHandler.findUserByID(id);
            TextView frientText = (TextView ) findViewById(R.id.friendText);
            frientText.setText(friend.getUsername());  //將結果顯示在 TextVeiw 中
        }
    }
}
