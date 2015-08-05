package com.example.liuqimin.lifary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class SignUp extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button signUpButton;
    Button loginPageButton;
    EditText passwordConfirmEdit;
    Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);
        loginPageButton = (Button) findViewById(R.id.loginPageButton);
        loginPageButton.setOnClickListener(this);
        passwordConfirmEdit = (EditText) findViewById(R.id.passwordConfirmedEditText);
        Firebase.setAndroidContext(this);
        rootRef = new Firebase("https://liuqimintest.firebaseio.com/");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
        if(v == signUpButton){
            MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
            String username = usernameEditText.getText().toString();
            if(myDBHandler.findUser(username) != null){
                Toast.makeText(this, "username already exist", Toast.LENGTH_LONG).show();
            }
            else{
                if(passwordEditText.getText().toString().equals(passwordConfirmEdit.getText().toString())) {
                    User user = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    myDBHandler.addUser(user);
                    user = myDBHandler.findUser(username);
                    Toast.makeText(this, "new user id: " + user.getID() + " added", Toast.LENGTH_LONG).show();

                    try {
                       Firebase ref = rootRef.push();
                        ref.setValue(user);

                    }catch (Exception e){
                        Log.d("Lifary", "Signup: failed to add user to online database ERROR: " + e.getLocalizedMessage());
                    }
                    // goes to user profile activity
            /*        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.putExtra("USER_ID", user.getID());
                    startActivity(i);

                    */
                }
                else{
                    Toast.makeText(this, "please confirm your password", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(v == loginPageButton){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }
}
