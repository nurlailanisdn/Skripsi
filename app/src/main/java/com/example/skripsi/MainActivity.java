package com.example.skripsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.skripsi.model.ListOnline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.cardViewListOnline)
    CardView cViewListOnline;

    ListOnlineAdapter myAdapter;
    RecyclerView recyclerView;
    private DatabaseReference userListRef, onlineStatus, connectedRef;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<ListOnline> list;
    FirebaseDatabase db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_online);
        recyclerView = findViewById(R.id.listOnline);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListOnline);
        setSupportActionBar(toolbar);
        toolbar.setTitle("List Online");

        //setup recyclerview
        recyclerView = findViewById(R.id.listOnline);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new ListOnlineAdapter(list);

        //firebase
        db = FirebaseDatabase.getInstance();
        userListRef = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        writeLastSeen(user);
        readDatabase();
        recyclerView.setAdapter(myAdapter);
    }

    private void writeLastSeen(FirebaseUser user){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time = format.format(calendar);
            onlineStatus=db.getReference("listOnline/"+user.getUid()+"/lastSeen");
            connectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(Boolean.class)){
                       onlineStatus.onDisconnect().removeValue();//delete child from listOnline
                        onlineStatus.setValue(time);
                        myAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e ("write LastSeen: ", error.getMessage());
                }
            });
    }

    private void readDatabase(){
        DatabaseReference dbRead= db.getReference().child("Akun");
        dbRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ListOnline listOnline = dataSnapshot.getValue(ListOnline.class);
                    list.add(listOnline);

                    DatabaseReference dbRef =  db.getReference().child("listOnline");
                    dbRef.setValue(listOnline);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("List Online: ", error.getMessage());
            }
        });
    }
}