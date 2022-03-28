package com.example.skripsi;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.ListGaleriFoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadPhotoActivity extends AppCompatActivity {
    @BindView(R.id.txt_judulFoto)
    EditText txt_judulFoto;
    @BindView(R.id.txt_deskripsiFoto)
    EditText txt_deskripsiFoto;
    @BindView(R.id.fotoKerja)
    ImageView fotoKerja;
    @BindView (R.id.progress_bar_uploadFotoKerja)
    ProgressBar progressBar;
    private static final int PICK_IMAGE_REQ =1;
    private FirebaseUser mUser;

    String judulFoto, deskripsiFoto, imageURL, namaUploader, namaAtasan;
    private SharedPreferences preferences;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference dbRef, mDb;
//    private StorageTask mUploadTask;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        ButterKnife.bind(this);

        mStorageRef= FirebaseStorage.getInstance().getReference("fotoKerja");
        dbRef= FirebaseDatabase.getInstance().getReference("fotoKerja");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @OnClick(R.id.btn_pilihFotoKerja)
    public void pilihFotoKerja(){
        pilihFoto();
    }

    @OnClick(R.id.btn_uploadFotoKerja)
    public void uploadFotoKerja(){
        uploadFoto();
    }

    public void pilihFoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==PICK_IMAGE_REQ && resultCode==RESULT_OK&& data!=null && data.getData()!=null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(fotoKerja);
        }
    }

    private  String getFileExtentionUri(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFoto(){
        if (mImageUri!=null){
            String uId =mUser.getUid();

            fotoKerja.setDrawingCacheEnabled(true);
            fotoKerja.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) fotoKerja.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes = stream.toByteArray();

            StorageReference fileReference = FirebaseStorage.getInstance().getReference();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);

            String namaFile= UUID.randomUUID()+".jpg";
            String pathImage="fotoKerja/"+uId+"/"+namaFile;

            UploadTask uploadTask = fileReference.child(pathImage).putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPhotoActivity.this,"Unggah foto berhasil!", Toast.LENGTH_LONG).show();
                    mDb = FirebaseDatabase.getInstance().getReference().child("Akun").child(uId);

                    mDb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ListGaleriFoto getNamaUploader = snapshot.getValue(ListGaleriFoto.class);
                            ListGaleriFoto getNamaAtasan = snapshot.getValue(ListGaleriFoto.class);
                            namaUploader = getNamaUploader.getNamaPanjang();
                            namaAtasan = getNamaAtasan.getNamaAtasan();
                            Log.d("nama uploader",namaUploader);
                            Log.d("nama atasan", namaAtasan);
                            Date currentTime= Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = dateFormat.format(currentTime);

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                            StorageReference dataRef = storageReference.child("fotoKerja"+"/"+uId);
//                            StorageReference storageReference=fileReference.child(pathImage);
                            storageReference.child(pathImage).getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                          public void onSuccess(Uri uri) {
                                            String key = dbRef.push().getKey();

                                            imageURL = uri.toString();
                                            judulFoto=txt_judulFoto.getText().toString();
                                            deskripsiFoto=txt_deskripsiFoto.getText().toString();
                                            ListGaleriFoto upload = new ListGaleriFoto (strDate,namaUploader,judulFoto,imageURL,deskripsiFoto,namaAtasan,key,uId);
                                            dbRef.child(key).setValue(upload);
                                            startActivity(new Intent(UploadPhotoActivity.this,GaleriFotoTeknisi.class));
                                        }
                                    }
                            );
//
//                            dataRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    imageURL = uri.toString();
//                                    }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(UploadPhotoActivity.this, "Gagal mengambil URL", Toast.LENGTH_LONG).show();
//                                }
//                            });

//                            imageURL=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UploadPhotoActivity.this,"Gagal mengambil data nama uploader", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPhotoActivity.this,"Unggah foto gagal!", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                    Toast.makeText(UploadPhotoActivity.this,"Foto sedang terunggah",Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(UploadPhotoActivity.this,"Tidak ada foto yang terpilih",Toast.LENGTH_LONG).show();
        }
    }
}
