package com.example.skripsi;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.model.UploadPhoto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    String judulFoto, deskripsiFoto, imageURL;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference dbRef;
//    private StorageTask mUploadTask;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        ButterKnife.bind(this);

        mStorageRef= FirebaseStorage.getInstance().getReference("fotoKerjaUploads");
        dbRef= FirebaseDatabase.getInstance().getReference("fotoKerjaUploads");
        judulFoto=txt_judulFoto.getText().toString();
        deskripsiFoto=txt_deskripsiFoto.getText().toString();
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
            String pathImage="fotoKerja/"+namaFile;

            UploadTask uploadTask = fileReference.child(pathImage).putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPhotoActivity.this,"Unggah foto berhasil!", Toast.LENGTH_LONG).show();
                    imageURL=taskSnapshot.toString();
                    UploadPhoto upload = new UploadPhoto (judulFoto,deskripsiFoto,imageURL);
                    String uploadId = dbRef.push().getKey();
                    dbRef.child(uploadId).setValue(upload);
                    startActivity(new Intent(UploadPhotoActivity.this,GaleriFoto.class));
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
                }
            });

//            mUploadTask= fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.VISIBLE);
//                            progressBar.setIndeterminate(false);
//                            progressBar.setProgress(0);
//                        }
//                    },500);
//                    imageURL=taskSnapshot.toString();
//                    Toast.makeText(UploadPhotoActivity.this, "Unggah Foto Berhasil!", Toast.LENGTH_LONG).show();
//                    UploadPhoto upload = new UploadPhoto (judulFoto,deskripsiFoto,imageURL);
//                    String uploadId = dbRef.push().getKey();
//                    dbRef.child(uploadId).setValue(upload);
//                    progressBar.setVisibility(View.INVISIBLE);
//                    startActivity(new Intent(UploadPhotoActivity.this,GaleriFoto.class));
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                    double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//                    progressBar.setProgress((int) progress);
//                }
//            });
        } else {
            Toast.makeText(UploadPhotoActivity.this,"Tidak ada foto yang terpilih",Toast.LENGTH_LONG).show();
        }
    }
}
