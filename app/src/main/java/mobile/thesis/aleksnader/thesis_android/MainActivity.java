package mobile.thesis.aleksnader.thesis_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.gson.Gson;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import mobile.thesis.aleksnader.thesis_android.Adapter.UserAdapter;
import mobile.thesis.aleksnader.thesis_android.Entity.Conversation;
import mobile.thesis.aleksnader.thesis_android.Entity.Token;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import mobile.thesis.aleksnader.thesis_android.Utils.HttpRestUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Token accessToken;
    private String userEmail;
    private User loggedUser;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recipients_recycler_view);


    }

    @Override
    protected void onStart() {
        super.onStart();


        int mode = Activity.MODE_PRIVATE;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("accessToken",mode);
        String jsonToken = sharedPreferences.getString("token", "");
        userEmail = sharedPreferences.getString("userEmail","");
        accessToken = gson.fromJson(jsonToken,Token.class);

        if (accessToken == null) {
            Intent intent = new Intent(this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            new AsyncTask<Token, Void, List<User>>() {
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("ładowanie danych..");
                    progressDialog.show();
                }

                @Override
                protected List<User> doInBackground(Token[] tokens) {
                    Token token = tokens[0];
                    List<User> users = new ArrayList<>();
                    try {
                        loggedUser = (User) HttpRestUtils.httpGet(StaticValues.URLIP + "/user/email/" + userEmail, User.class, accessToken);
                        StaticValues.LoggedUserId = loggedUser.getId();

                        for (Long id : loggedUser.getConversationsId()) {
                            String url = StaticValues.URLIP + "/conversation/" + id;
                            Conversation conversation = (Conversation) HttpRestUtils.httpGet(url, Conversation.class, token);
                            User user = null;
                            if(conversation.getFirstInterlocutorId().equals(loggedUser.getId())){
                                StaticValues.firstInConversation = true;
                                user = (User) HttpRestUtils.httpGet(StaticValues.URLIP+"/user/"+conversation.getSecondInterlocutorId(),User.class,token);
                            }else{
                                StaticValues.firstInConversation = false;
                                user = (User) HttpRestUtils.httpGet(StaticValues.URLIP+"/user/"+conversation.getFirstInterlocutorId(),User.class,token);

                            }
                            users.add(user);
                        }

                        return users;
                    }catch (RuntimeException ex){
                        ex.printStackTrace();
                        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                        startActivity(intent);
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
            }.execute(accessToken);

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMainMenuItem:

                int mode = Activity.MODE_PRIVATE;
                SharedPreferences sharedPreferences = getSharedPreferences("accessToken",mode);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token");
                editor.apply();


                Intent intent = new Intent(this,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            default:

        }
        return true;
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
