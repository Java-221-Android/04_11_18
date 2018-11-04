package com.sheygam.java_221_04_11_18;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout progressFrame;
    private LinearLayout inputWrapper;
    private EditText inputEmail, inputPassword;
    private Button regBtn, loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressFrame = findViewById(R.id.progressFrame);
        inputWrapper = findViewById(R.id.inputWrapper);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        regBtn = findViewById(R.id.regBtn);
        loginBtn = findViewById(R.id.loginBtn);

        regBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    private void showError(String error){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Ok",null)
                .setCancelable(false)
                .create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){

        }else if(v.getId() == R.id.regBtn){
            new RegTask(inputEmail.getText().toString(),
                    inputPassword.getText().toString()).execute();
        }
    }

    class RegTask extends AsyncTask<Void,Void,String>{
        private String email;
        private String password;
        boolean isSuccess;

        public RegTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            inputWrapper.setVisibility(View.GONE);
            progressFrame.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String status = "Registration OK!";

            try {
                TokenModel token = HttpProvider.getInstance().registration(email,password);
                isSuccess = true;
                Log.d("MY_TAG", "doInBackground token: " + token.getToken());
            }catch(IOException e){
                status = "Connection error! Check your internet!";
                isSuccess = false;
            }catch (Exception e) {
                status = e.getMessage();
                isSuccess = false;
            }

            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            inputWrapper.setVisibility(View.VISIBLE);
            progressFrame.setVisibility(View.GONE);
            if(isSuccess){
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }else{
                showError(s);
            }
        }
    }


}
