package com.example.group3_sker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group3_sker.Model.Message;
import com.example.group3_sker.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> messages;
    private String currentUserId;

    public MessageAdapter(Context context, List<Message> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        // Set user name
        holder.userName.setText(message.getUsername());

        // Set message text
        holder.messageText.setText(message.getText());

        // Set avatar image (replace with actual image loading logic)
        holder.avatarImage.setImageResource(R.drawable.sker_logo);

        // Set background color based on sender
        if (message.getSenderId().equals(currentUserId)) {
            holder.messageText.setBackgroundResource(R.drawable.rounded_corner_green);
        } else {
            holder.messageText.setBackgroundResource(R.drawable.rounded_corner_blue);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView userName;
        TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            userName = itemView.findViewById(R.id.user_name);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}

