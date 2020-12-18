package com.example.android.customcamera;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private static final String TAG = "DriveServiceHelper";
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveService;
    Date date = new Date();


    public DriveServiceHelper(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }

    public Task<String> uploadYourImage(String filepath) {
        Log.i(TAG, "uploadYourImage: " + filepath);
        if (filepath.isEmpty()) {
            Log.i(TAG, "uploadYourImage: got empty file" );
        }
        return Tasks.call(mExecutor,() -> {

            String folderId = "1FDuSY7y9v3oJlBT3t1LN2bYECtFHQB6e";
            File filemetaData = new File();
            filemetaData.setName(String.valueOf(date.getTime()));
            filemetaData.setParents(Collections.singletonList(folderId));
            java.io.File file = new java.io.File(filepath);
            FileContent mediaContent = new FileContent("image/jpeg",file);
            File myFile = null;
            try {
                myFile = mDriveService.files().create(filemetaData, mediaContent)
                        .setFields("id")
                        .execute();
                Log.i(TAG, "uploadYourImage: File id: " + myFile.getId());
            } catch (Exception e){
                e.printStackTrace();
            }

            if(myFile == null) {
                throw  new IOException("Nothing in file ");
            }
            return myFile.getId();
        });
    }
}
