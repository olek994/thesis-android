package mobile.thesis.aleksnader.thesis_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
    }
}
