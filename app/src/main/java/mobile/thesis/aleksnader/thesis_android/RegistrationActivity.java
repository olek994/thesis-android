package mobile.thesis.aleksnader.thesis_android;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RegistrationActivity extends AppCompatActivity {

    private Button submitRegistrationButton;
    private EditText nameEditText;
    private EditText subnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        submitRegistrationButton = (Button) findViewById(R.id.SubmitRegistrationButton);
        nameEditText = (EditText) findViewById(R.id.NameEditText);
        subnameEditText = (EditText) findViewById(R.id.subnameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        repeatPassEditText = (EditText) findViewById(R.id.RepeatPasswordEditText);


        submitRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameEditText.getText().toString().isEmpty() && !subnameEditText.getText().toString().isEmpty() && !emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty() && !repeatPassEditText.getText().toString().isEmpty()){
                    if(passwordsMatch(repeatPassEditText.getText().toString())){
                        User user = new User();
                        user.setName(nameEditText.getText().toString());
                        user.setSubname(subnameEditText.getText().toString());
                        user.setEmail(emailEditText.getText().toString());
                        user.setPassword(passwordEditText.getText().toString());

                        new AsyncTask<User,Void,Void>(){

                            @Override
                            protected Void doInBackground(User... Users) {
                                User user = Users[0];

                                String url = "http://192.168.0.14:8080/user/";
                                RestTemplate restTemplate = new RestTemplate();
                                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                                HttpEntity<User> entity = new HttpEntity<>(user,httpHeaders);
                                ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST,entity,User.class);
                                System.out.println(response);
                                return null;
                            }
                        }.execute(user);

                    }else{
                        Toast.makeText(RegistrationActivity.this, "Hasła są różne", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistrationActivity.this, "Wypełni wszystkie pola", Toast.LENGTH_SHORT).show();
                }
            }
        });

        repeatPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();
                passwordsMatch(value);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private boolean passwordsMatch(String value){
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        if(!passwordEditText.getText().toString().equals(value)){
            repeatPassEditText.setTextColor(RegistrationActivity.this.getResources().getColor(R.color.wrongInputValue));
            repeatPassEditText.setTypeface(repeatPassEditText.getTypeface(), Typeface.BOLD);
            return false;
        }else{
            repeatPassEditText.setTypeface(null, Typeface.NORMAL);
            repeatPassEditText.setTextColor(Color.BLACK);
            return true;
        }
    }

}
