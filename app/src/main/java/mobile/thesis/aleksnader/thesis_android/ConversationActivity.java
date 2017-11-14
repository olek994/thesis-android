package mobile.thesis.aleksnader.thesis_android;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import mobile.thesis.aleksnader.thesis_android.Entity.User;

public class ConversationActivity extends AppCompatActivity {

    private User recipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        recipient = (User) getIntent().getSerializableExtra("recipient");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("Rozmowa: "+recipient.getName() +" "+recipient.getSubname());
    }
}
