package mobile.thesis.aleksnader.thesis_android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import mobile.thesis.aleksnader.thesis_android.Utils.HttpRestUtils;
import org.springframework.http.ResponseEntity;

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

                        new AsyncTask<User,Void,Boolean>(){
                            ProgressDialog progressDialog = null;
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(RegistrationActivity.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage("Rejestrowanie użytkownika..");
                                submitRegistrationButton.setEnabled(false);
                                progressDialog.show();
                            }

                            @Override
                            protected Boolean doInBackground(User... Users) {
                                User user = Users[0];

                                String url = StaticValues.URLIP+"/user";
                                ResponseEntity<User> response = (ResponseEntity<User>) HttpRestUtils.httpPost(url,user,User.class);

                                return response.getStatusCode().toString().equals("200");
                            }

                            @Override
                            protected void onPostExecute(Boolean succese) {
                                super.onPostExecute(succese);
                                submitRegistrationButton.setEnabled(true);
                                progressDialog.dismiss();
                                if(succese){
                                    Toast.makeText(RegistrationActivity.this, "Operacja zakończona powodzeniem", Toast.LENGTH_SHORT).show();
                                    finish();

                                }else{
                                    Toast.makeText(RegistrationActivity.this, "Wystąpił błąd. Spróbuj ponownie...", Toast.LENGTH_SHORT).show();
                                }
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


    }

    private boolean passwordsMatch(String value){
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        return passwordEditText.getText().toString().equals(value);
    }

}
