package com.hoiyen.iats.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hoiyen.iats.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

public final class GalleryHelper {

    public static List<ImageModel> getImages(Activity activity, int offset) {
        return getImages(activity, 30, offset);
    }

    public static List<ImageModel> getImages(Activity activity, int limit, int offset) {

        final List<ImageModel> images = new ArrayList<>();

        // requested data
        final String[] projection = {
                MediaStore.MediaColumns.DATA};

        // requesting image gallery
        final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // setup query
        final StringBuilder query = new StringBuilder();
        query.append(MediaStore.Images.ImageColumns.DATE_TAKEN);
        query.append(" limit ");
        query.append(limit);
        query.append(" offset ");
        query.append(offset * limit);

        // start query images
        final Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, query.toString());
        //final Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(activity.getContentResolver(), uri, MediaStore.Images.Thumbnails.MINI_KIND, null);

        if (cursor != null) {

            int field = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (cursor.moveToNext()) {
                images.add(new ImageModel(cursor.getString(field)));
            }

            cursor.close();
        }

        return images;
    }

}
