package mobile.thesis.aleksnader.thesis_android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import mobile.thesis.aleksnader.thesis_android.Adapter.UserAdapter;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import mobile.thesis.aleksnader.thesis_android.Utils.HttpRestUtils;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private User loggedUser;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loggedUser = (User) getIntent().getSerializableExtra("LoggedUser");
        recyclerView = (RecyclerView) findViewById(R.id.recipients_recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loggedUser == null) {
            Intent intent = new Intent(this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            new AsyncTask<List<Long>, Void, List<User>>() {
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("ładowanie użytkowników..");
                    progressDialog.show();
                }

                @Override
                protected List<User> doInBackground(List<Long>[] lists) {
                    List<Long> ids = lists[0];
                    List<User> users = new ArrayList<>();
                    for (Long id : ids) {
                        String url = StaticValues.URLIP + "/user/" + id;
                        User user = (User) HttpRestUtils.httpGet(url, User.class);
                        users.add(user);
                    }
                    return users;
                }

                @Override
                protected void onPostExecute(List<User> users) {
                    super.onPostExecute(users);
                    userAdapter = new UserAdapter(MainActivity.this, users);
                    recyclerView = setConfigurationofRecyclerView(recyclerView,userAdapter);
                    progressDialog.dismiss();
                }
            }.execute(loggedUser.getRecipientId());
        }
    }
    private  RecyclerView setConfigurationofRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new FadeInDownAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        return recyclerView;
    }
}
