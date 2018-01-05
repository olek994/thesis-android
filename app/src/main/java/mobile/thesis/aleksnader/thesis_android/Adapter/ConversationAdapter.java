package mobile.thesis.aleksnader.thesis_android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mobile.thesis.aleksnader.thesis_android.Entity.Message;
import mobile.thesis.aleksnader.thesis_android.Enum.MessageTypeEnum;
import mobile.thesis.aleksnader.thesis_android.R;
import mobile.thesis.aleksnader.thesis_android.Static.StaticValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksander on 04.01.2018.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private LayoutInflater layoutInflater;
    List<Message> messages = new ArrayList<>();

    public ConversationAdapter(Context context, List<Message> messages) {
        layoutInflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getAuthorId().equals(StaticValues.LoggedUserId)){
            return MessageTypeEnum.Sended.ordinal();
        }else{
            return MessageTypeEnum.Recived.ordinal();
        }
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ConversationViewHolder holder = null;
        if(viewType == MessageTypeEnum.Sended.ordinal()){
             view = layoutInflater.inflate(R.layout.conversation_my_message,parent,false);
            holder = new ConversationViewHolder(view);
        }else if(viewType == MessageTypeEnum.Recived.ordinal()){
            view = layoutInflater.inflate(R.layout.conversation_other_message,parent,false);
            holder = new ConversationViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        String message = messages.get(position).getContent();
        holder.myMessageContent.setText(message);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public void addItem(Message message){
        messages.add(message);
        notifyItemInserted(messages.size());
        
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder{
            private TextView myMessageContent;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            myMessageContent = (TextView) itemView.findViewById(R.id.myMessage);
        }
    }

}
