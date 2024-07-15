package com.example.group3_sker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.R;
import com.example.group3_sker.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;
    private OnUserClickListener listener;
    private DatabaseReference mDatabase;

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }

    public UserAdapter(Context context, List<User> users, OnUserClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.userName.setText(user.getName());
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user.getId()));

        // Set avatar image (replace with actual image loading logic)
        holder.avatarImage.setImageResource(R.drawable.sker_logo);

        // Fetch and display the recent message
        fetchRecentMessage(user.getId(), holder.recentMessage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void fetchRecentMessage(String userId, TextView recentMessageTextView) {
        String currentUserId = "2b2a5cad-fb48-497c-973d-08dc9f167606";

        mDatabase.child("messages")
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                String messageText = messageSnapshot.child("text").getValue(String.class);
                                String senderId = messageSnapshot.child("senderId").getValue(String.class);
                                String receiverId = messageSnapshot.child("receiverId").getValue(String.class);

                                if ((senderId.equals(currentUserId) && receiverId.equals(userId)) ||
                                        (senderId.equals(userId) && receiverId.equals(currentUserId))) {
                                    recentMessageTextView.setText(messageText);
                                    break;
                                }
                            }
                        } else {
                            recentMessageTextView.setText("No messages yet");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView userName;
        TextView recentMessage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            userName = itemView.findViewById(R.id.user_name);
            recentMessage = itemView.findViewById(R.id.recent_message);
        }
    }
}
