package com.amazing.android.autopompomme.write.presenter;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WritePresenter implements WriteContract.Presenter {
    private WriteContract.View view;

    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private StorageReference reference;

    public WritePresenter(WriteContract.View view) {
        this.view = view;
    }

    @Override
    public void write(String profileName, Uri profileUri, String date, String title, String detail, List<Uri> imgUris) {

        List<Task<Uri>> uploadTasks = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        for(Uri imgUri : imgUris) {
            StorageReference imgRef = storage.getReference().child("images/" + imgUri.getLastPathSegment());
            UploadTask uploadTask = imgRef.putFile(imgUri);
            Log.d("TEST","task"+uploadTask);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imgRef.getDownloadUrl();
                }
            });

            uploadTasks.add(uriTask);

        }

        Tasks.whenAllSuccess(uploadTasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> list) {
                List<Uri> imgUriList = new ArrayList<>();
                for(Object object : list) {
                    Uri downloadUrl = (Uri) object;
                    imgUriList.add(downloadUrl);
                }

                Log.d("TEST","downloadUrl"+imgUriList);
                Map<String, Object> post = new HashMap<>();
                post.put("profileName", profileName);
                post.put("profileUri", profileUri);
                post.put("date", date);
                post.put("title", title);
                post.put("content", detail);
                post.put("postUri", imgUriList);

                fireStore.collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TEST", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TEST", "Error adding document", e);
                            }
                        });

            }
        });

        //--------------------------------------------------------------------------------------

        //api 코드
        //이미지 api

    }
}
