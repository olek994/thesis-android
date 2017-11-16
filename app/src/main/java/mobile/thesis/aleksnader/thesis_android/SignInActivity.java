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
                if(!email.isEmpty() && !password.isEmpty()){
                    new AsyncTask<String, Void, User>() {
                        ProgressDialog progressDialog = null;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog = new ProgressDialog(SignInActivity.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Sprawdzanie poprawności informacji..");
                            signInButton.setEnabled(false);
                            progressDialog.show();
                        }

                        @Override
                        protected User doInBackground(String... strings) {
                            String url = strings[0];
                            User user = null;

                            user = (User) HttpRestUtils.httpGet(url, User.class);
                            return user;
                        }

                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            progressDialog.dismiss();
                            signInButton.setEnabled(true);
                            if (user != null) {

                                //Dodanie do sharedPreference zalogowanego usera
                                int mode = Activity.MODE_PRIVATE;
                                SharedPreferences sharedPreferences = getSharedPreferences("loggedUser",mode);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String jsonUser = gson.toJson(user);
                                editor.putString("User",jsonUser);
                                editor.apply();

                                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }else{
                                Toast.makeText(SignInActivity.this, "Operacja zakończona niepowodzeniem", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(StaticValues.URLIP+"/user/signin/"+email+"/"+password);
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
