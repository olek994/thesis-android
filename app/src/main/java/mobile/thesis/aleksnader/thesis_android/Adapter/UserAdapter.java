package mobile.thesis.aleksnader.thesis_android.Adapter;

import mobile.thesis.aleksnader.thesis_android.ConversationActivity;
import mobile.thesis.aleksnader.thesis_android.Entity.User;
import mobile.thesis.aleksnader.thesis_android.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksander on 14.11.2017.
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2017r.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private LayoutInflater layoutInflater;
    List<User> recipients = Collections.emptyList();
    public UserAdapter(Context context, List<User> recipients) {
        layoutInflater = LayoutInflater.from(context);
        this.recipients = recipients;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipient_list_row,parent,false);
        UserViewHolder holder = new UserViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User currentUser = recipients.get(position);
        holder.nameAndSubnameOfRecipient.setText(currentUser.getName()+" "+currentUser.getSubname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ConversationActivity.class);
                intent.putExtra("recipient",currentUser);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipients.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView nameAndSubnameOfRecipient;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameAndSubnameOfRecipient = (TextView) itemView.findViewById(R.id.nameAndSubnameOfRecipient);
        }
    }

}
