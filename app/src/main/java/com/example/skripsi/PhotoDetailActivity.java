package com.example.skripsi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.model.ListGaleriFoto;
import com.example.skripsi.model.ListKomentar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDetailActivity extends AppCompatActivity {
    @BindView(R.id.photoDetail)
    ImageView photoDetail;
    @BindView(R.id.judulDetailFoto)
    TextView judulFotoDetail;
    @BindView (R.id.deskripsiFotoDetail)
    TextView deskripsiFotoDetail;
    @BindView (R.id.editText_komentar)
    EditText komentarFoto;
    @BindView(R.id.recyclerview_komentar)
    RecyclerView recyclerView;
    private FirebaseUser mUser;

    private KomentarAdapter myAdapter;
    ArrayList<ListKomentar> list=new ArrayList<>();
    FirebaseDatabase db;
    DatabaseReference refPhoto, dbRef, komentarPhoto;
    String komentarKey, namaKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_activity);
        ButterKnife.bind(this);

        recyclerView=findViewById(R.id.recyclerview_komentar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new KomentarAdapter(list);
        recyclerView.setAdapter(myAdapter);

        db=FirebaseDatabase.getInstance();
        refPhoto= db.getReference("fotoKerja");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef= FirebaseDatabase.getInstance().getReference("ListKomentar");
        loadIntent();

    }
        ListGaleriFoto listGaleriFoto;

        private void loadIntent(){
        if (getIntent().getExtras()!=null){
            listGaleriFoto = (ListGaleriFoto) getIntent().getSerializableExtra("photoData");
            Picasso.with(PhotoDetailActivity.this)
                    .load(listGaleriFoto.getImageURL())
                    .into(photoDetail);
            judulFotoDetail.setText(listGaleriFoto.getJudulFotoKerja());
            deskripsiFotoDetail.setText(listGaleriFoto.getDeskripsiFotoKerja());
            loadComment();
            String uid=mUser.getUid();
            db.getReference().child("Akun").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ListKomentar listKomentar = snapshot.getValue(ListKomentar.class);
                    namaKomentar = listKomentar.getNamaPanjang();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } loadComment();
        }

        private void loadComment(){
           db.getReference().child("ListKomentar").child(listGaleriFoto.getKey())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ListKomentar komentar = dataSnapshot.getValue(ListKomentar.class);
                                list.add(komentar);
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("","Gagal mengambil data");
                        }
                    });
        }

        @OnClick(R.id.btn_komentarFoto)
    public void kirim(){
            if (komentarFoto.getText().toString().isEmpty()){
                Toast.makeText(this, "Harap isi komentar", Toast.LENGTH_SHORT).show();
                return;
            }
            //isi komentar
            String uid = mUser.getUid();
            String komentar = komentarFoto.getText().toString();
            ListKomentar uploadKomentar = new ListKomentar(namaKomentar,komentar,komentarKey,uid);
            db.getReference().child("ListKomentar").child(listGaleriFoto.getKey()).child(komentarKey).setValue(uploadKomentar);
            recreate();
        }


}
