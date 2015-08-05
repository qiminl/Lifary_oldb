package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity implements View.OnClickListener{

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button signupPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.usernameEdittext);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.logInButton);
        loginButton.setOnClickListener(this);
        signupPageButton = (Button) findViewById(R.id.signUpPageButton);
        signupPageButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if (v == signupPageButton){
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }
        else if(v == loginButton){
            MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);
            String username = usernameEditText.getText().toString();
            User user = myDBHandler.findUser(username);
            if(user != null) {
                if (user.getPassword().equals(passwordEditText.getText().toString())) {
                    Toast.makeText(this, "user matched", Toast.LENGTH_LONG).show();
                    // goes to user profile activity
                    Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.putExtra("USER_ID", user.getID());
                    startActivity(i);

                } else {
                    Toast.makeText(this, "password doesn't match", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "username doesn't found", Toast.LENGTH_LONG).show();
            }
        }

    }
}
