package com.lek.sororas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lek.sororas.Models.Message;
import com.lek.sororas.R;

import java.util.ArrayList;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by evera on 06/02/2018.
 */

public class AdapterChatRecycleView extends RecyclerView.Adapter{

    private ArrayList<Message> messages;
    private Context context;
    String userId;

    public AdapterChatRecycleView(ArrayList<Message> messages, Context context, String userId) {
        this.messages = messages;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chatmenssage, parent, false);

        materialViewHolder holder = new materialViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        materialViewHolder materialHolder = (materialViewHolder) holder;

        Message message = messages.get(position);
        materialHolder.message.setText(message.getText());

        if(message.getSenderId().equals(userId)){

            materialHolder.chatMessageView.setGravity(Gravity.RIGHT);
            materialHolder.chatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.RIGHT);
            materialHolder.chatMessageView.setShowArrow(true);
            materialHolder.chatMessageView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent),context.getResources().getColor(R.color.branco));

            materialHolder.message.setTextColor(context.getResources().getColor(R.color.white));
        }

        else{
            materialHolder.chatMessageView.setGravity(Gravity.LEFT);
            materialHolder.chatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.LEFT);

            materialHolder.chatMessageView.setShowArrow(true);
            materialHolder.chatMessageView.setBackgroundColor(context.getResources().getColor(R.color.cinzachat),context.getResources().getColor(R.color.preto));

            materialHolder.message.setTextColor(context.getResources().getColor(R.color.preto));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class materialViewHolder extends RecyclerView.ViewHolder {

        final TextView message;
        final ChatMessageView chatMessageView;

        public materialViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.messageTv);
            chatMessageView = view.findViewById(R.id.chatview);
            // restante das buscas
        }

    }
}
