package mobile.thesis.aleksnader.thesis_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
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
                            progressDialog.setTitle("Loading");
                            progressDialog.setMessage("Searching for Person..");
                            signInButton.setEnabled(false);
                            progressDialog.show();
                        }

                        @Override
                        protected User doInBackground(String... strings) {
                            String url = strings[0];
                            RestTemplate restTemplate = new RestTemplate();
                            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                            User user = restTemplate.getForObject(url,User.class);
                            return user;
                        }

                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            progressDialog.dismiss();
                            emailEditText.setText("");
                            passwordEditText.setText("");
                            signInButton.setEnabled(true);
                            Toast.makeText(SignInActivity.this, user.getName()+", "+user.getSubname(), Toast.LENGTH_SHORT).show();

                        }
                    }.execute("http://192.168.0.14:8080/user/signin/"+email+"/"+password);
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
