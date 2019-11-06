package com.example.a0106chatapp;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.a0106chatapp.Adapter.MessageAdapter;
import com.example.a0106chatapp.Model.Chat;
import com.example.a0106chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Messages extends AppCompatActivity {

    TextView username;
    ImageView profile_image;

    FirebaseUser fUser;
    DatabaseReference reference;

    // sending messages
    Button btnSendMsg;
    EditText etMsg;

    Intent intent;

    // getting messages
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmsg);

        recyclerView = (RecyclerView) findViewById(R.id.lvConversation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);



        // chat parametres
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessage);

        username =(TextView) findViewById(R.id.username);
        profile_image = (ImageView) findViewById(R.id.profileimage);

        intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                 username.setText(user.getUsername());
                 profile_image.setImageResource(R.mipmap.ic_launcher_round);

                 readMessage(fUser.getUid(),userId);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

         btnSendMsg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String msg = etMsg.getText().toString();
                 if(!msg.equals("")){
                     sendMessage(fUser.getUid(),userId, msg);

                 }else{
                     Toast.makeText(Messages.this, "You ca not send empty message  ",Toast.LENGTH_SHORT).show();
                 }
                 etMsg.setText("");
             }
         });
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

    }



    private void readMessage(final String myid, final String userid){

        mchat = new ArrayList<>();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);

                        messageAdapter = new MessageAdapter(Messages.this,mchat);
                        recyclerView.setAdapter(messageAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
