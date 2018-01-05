package mobile.thesis.aleksnader.thesis_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import mobile.thesis.aleksnader.thesis_android.Adapter.ConversationAdapter;
import mobile.thesis.aleksnader.thesis_android.Entity.Conversation;
import mobile.thesis.aleksnader.thesis_android.Entity.Message;
import mobile.thesis.aleksnader.thesis_android.Entity.Token;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;
import mobile.thesis.aleksnader.thesis_android.Utils.HttpRestUtils;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    private User recipient;
    private EditText messageEditText;
    private List<Message> messages;
    private Button sendButton;
    private Token accessToken;
    private User loggedUser;
    private RecyclerView conversationRecyclerView;
    private ConversationAdapter conversationAdapter;
    private Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        messages = new ArrayList<>();

        //ustawienie RecyclerView
        conversationRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        conversationAdapter = new ConversationAdapter(ConversationActivity.this,messages);
        conversationRecyclerView.setItemAnimator(new FadeInDownAnimator());
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(ConversationActivity.this));

        //Pobranie tokenu i danych uzytkownika
        int mode = Activity.MODE_PRIVATE;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("accessToken",mode);
        String jsonToken = sharedPreferences.getString("token", "");
        accessToken = gson.fromJson(jsonToken,Token.class);
        String userEmail = sharedPreferences.getString("userEmail","");

        //Poberanie usera z kim jest prowadzona rozmowa
        new AsyncTask<String, Void, User>(){

            @Override
            protected User doInBackground(String... strings) {
                String email = strings[0];
                User user = null;
                try {
                    user = (User) HttpRestUtils.httpGet(StaticValues.URLIP + "/user/email/" + email, User.class, accessToken);
                    return user;
                }catch(RuntimeException ex){
                    Intent intent = new Intent(ConversationActivity.this,SignInActivity.class);
                    startActivity(intent);
                }
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                if (user == null) {
                    Intent intent = new Intent(ConversationActivity.this,SignInActivity.class);
                    startActivity(intent);
                }else{
                    loggedUser = user;
                    new AsyncTask<Void, Void, List<Message>>(){

                        @Override
                        protected List<Message> doInBackground(Void... voids) {

                            //Ustawienie id w zaleznosci od kolejnosci rozmowcy w konwersacji
                            String urlConversation = StaticValues.firstInConversation ?  urlConversation  = StaticValues.URLIP + "/conversation/first/" + loggedUser.getId() + "/second/"+recipient.getId() :StaticValues.URLIP + "/conversation/first/" + recipient.getId() + "/second/"+loggedUser.getId();
                            conversation = (Conversation) HttpRestUtils.httpGet(urlConversation, Conversation.class, accessToken);
                            messages = checkForMessages(accessToken,conversation);

                            return messages;
                        }

                        @Override
                        protected void onPostExecute(List<Message> messages) {
                            super.onPostExecute(messages);
                            insertMessagesToAdapter(conversationRecyclerView,messages);
                        }
                    }.execute();

                }
            }
        }.execute(userEmail);

        recipient = (User) getIntent().getSerializableExtra("recipient");
        messageEditText = (EditText) findViewById(R.id.messageToSend);
        sendButton = (Button) findViewById(R.id.sendMessageButton);

        //Wyslanie wiadomosci po kliknieciu przycisku wyslij
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickSendMessageButton();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("Rozmowa: "+recipient.getName() +" "+recipient.getSubname());
    }


    private void onClickSendMessageButton(){

        new AsyncTask<String, Void, List<Message>>(){
            ResponseEntity<Message> response;
            @Override
            protected List<Message> doInBackground(String... strings) {
                try{

                    Message message = new Message();
                    message.setContent(strings[0]);
                    message.setSendDate(new Date());
                    message.setAuthorId(loggedUser.getId());
                    message.setConversationId(conversation.getId());

                    response = (ResponseEntity<Message>) HttpRestUtils.httpPost(StaticValues.URLIP+"/message/",message, Message.class,accessToken);

                    return checkForMessages(accessToken,conversation);
                }catch (RuntimeException ex){
                    ex.printStackTrace();
                    Intent intent = new Intent(ConversationActivity.this,SignInActivity.class);
                    startActivity(intent);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Message> messages) {
                messageEditText.setText("");
                insertMessagesToAdapter(conversationRecyclerView,messages);
            }
        }.execute(messageEditText.getText().toString());


    }

    //Sprawdzenie czy sa nowe wiadomosci
    private List<Message> checkForMessages(Token token,Conversation conversation){
        Message[] messages;
        List<Message> messageList = new ArrayList<>();
        try{

            String url = StaticValues.URLIP+"/message/conversation/"+conversation.getId();
            messages = (Message[]) HttpRestUtils.httpGet(url,Message[].class,token);

            messageList = Arrays.asList(messages);
        }catch (RuntimeException ex){
           ex.printStackTrace();
        }
        return messageList;
    }

    private void insertMessagesToAdapter(RecyclerView recyclerView,List<Message> messages){
        if(recyclerView.getAdapter().getItemCount() < messages.size()){
            for(int i = recyclerView.getAdapter().getItemCount();i <messages.size();i++){
                conversationAdapter.addItem(messages.get(i));
            }
            conversationRecyclerView.scrollToPosition(conversationRecyclerView.getAdapter().getItemCount()-1);
        }
    }
}
