package mobile.thesis.aleksnader.thesis_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import mobile.thesis.aleksnader.thesis_android.Entity.User;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User loggedUser = (User) getIntent().getSerializableExtra("LoggedUser");



    }
}
