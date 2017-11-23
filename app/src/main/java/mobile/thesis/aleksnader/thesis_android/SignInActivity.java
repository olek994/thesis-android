package mobile.thesis.aleksnader.thesis_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import mobile.thesis.aleksnader.thesis_android.Entity.Token;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import mobile.thesis.aleksnader.thesis_android.Utils.HttpRestUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SignInActivity extends AppCompatActivity {
    private Button signInButton;
    private Button openRegistrationButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();


        emailEditText = (EditText) findViewById(R.id.emailInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        signInButton = (Button) findViewById(R.id.SignInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) {
                    new AsyncTask<String, Void, Token>() {
                        ProgressDialog progressDialog;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog = new ProgressDialog(SignInActivity.this);
                            progressDialog.setMessage("Autoryzacja");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }

                        @Override
                        protected Token doInBackground(String... strings) {

                            Token token = HttpRestUtils.getUserAccessToken(strings[0], strings[1]);
                            System.out.println(token.getAccess_token()); //Trzeba zapisać ogolnie token (sharedPreference)
                            return token;
                        }

                        @Override
                        protected void onPostExecute(Token token) {
                            super.onPostExecute(token);
                            progressDialog.dismiss();
                        }
                    }.execute(email, password);
                }


            }
        });

        openRegistrationButton = (Button) findViewById(R.id.openRegistrationViewButton);

        openRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
