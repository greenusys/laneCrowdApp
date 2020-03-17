package com.example.lanecrowd.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class ImageFilePath 
{
 /**
  * Method for return file path of Gallery image 
  * 
  * @param context
  * @param uri
  * @return path of the selected image file from gallery
  */
 public static String getPath(final Context context, final Uri uri)
 {
 
  //check here to KITKAT or new version
  final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
 
  // DocumentProvider
  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
    
   // ExternalStorageProvider
   if (isExternalStorageDocument(uri)) {
    System.out.println("sallu_first");
    final String docId = DocumentsContract.getDocumentId(uri);
    final String[] split = docId.split(":");
    final String type = split[0];
 
    if ("primary".equalsIgnoreCase(type)) {
     return Environment.getExternalStorageDirectory() + "/" + split[1];
    }
   }
   // DownloadsProvider
   else if (isDownloadsDocument(uri)) {

    System.out.println("sallu_second"+uri);


    final String id = DocumentsContract.getDocumentId(uri);
    final Uri contentUri = ContentUris.withAppendedId(
      Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

    System.out.println("contenturi"+contentUri);
    return getDataColumn("first",context, contentUri, null, null);
   }
   // MediaProvider
   else if (isMediaDocument(uri)) {

    System.out.println("sallu_third");

    final String docId = DocumentsContract.getDocumentId(uri);
    final String[] split = docId.split(":");
    final String type = split[0];
 
    Uri contentUri = null;
    if ("image".equals(type)) {
     System.out.println("imagepath_image");
     System.out.println();
     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    } else if ("video".equals(type)) {
     System.out.println("imagepath_vidoe");

     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    } else if ("audio".equals(type)) {
     System.out.println("imagepath_audio");

     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }
 
    final String selection = "_id=?";
    final String[] selectionArgs = new String[] {
      split[1]
    };
 
    return getDataColumn("second",context, contentUri, selection, selectionArgs);
   }
  }
  // MediaStore (and general)
  else if ("content".equalsIgnoreCase(uri.getScheme())) {


   System.out.println("sallu_fourth");


   // Return the remote address
   if (isGooglePhotosUri(uri))
    return uri.getLastPathSegment();
 
   return getDataColumn("third",context, uri, null, null);
  }
  // File
  else if ("file".equalsIgnoreCase(uri.getScheme())) {

   System.out.println("sallu_fifth"+" "+uri.getScheme());

   return uri.getPath();
  }
 
  return null;
 }
 
 /**
  * Get the value of the data column for this Uri. This is useful for
  * MediaStore Uris, and other file-based ContentProviders.
  *
  * @param context The context.
  * @param uri The Uri to query.
  * @param selection (Optional) Filter used in the query.
  * @param selectionArgs (Optional) Selection arguments used in the query.
  * @return The value of the _data column, which is typically a file path.
  */
 public static String getDataColumn(String from,Context context, Uri uri, String selection,
                                    String[] selectionArgs) {

  System.out.println("fromkaif"+from);
  Cursor cursor = null;
  final String column = "_data";
  final String[] projection = {column};
 
  try {
   cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
     null);
   if (cursor != null && cursor.moveToFirst()) {
    final int index = cursor.getColumnIndexOrThrow(column);
    return cursor.getString(index);
   }
  }catch (Exception e)
  {
   e.printStackTrace();
  }


  finally {
   if (cursor != null)
    cursor.close();
  }
  return null;
 }
 
 /**
  * @param uri The Uri to check.
  * @return Whether the Uri authority is ExternalStorageProvider.
  */
 public static boolean isExternalStorageDocument(Uri uri) {
  return "com.android.externalstorage.documents".equals(uri.getAuthority());
 }
 
 /**
  * @param uri The Uri to check.
  * @return Whether the Uri authority is DownloadsProvider.
  */
 public static boolean isDownloadsDocument(Uri uri) {
  return "com.android.providers.downloads.documents".equals(uri.getAuthority());
 }
 
 /**
  * @param uri The Uri to check.
  * @return Whether the Uri authority is MediaProvider.
  */
 public static boolean isMediaDocument(Uri uri) {
  return "com.android.providers.media.documents".equals(uri.getAuthority());
 }
 
 /**
  * @param uri The Uri to check.
  * @return Whether the Uri authority is Google Photos.
  */
 public static boolean isGooglePhotosUri(Uri uri) {
  return "com.google.android.apps.photos.content".equals(uri.getAuthority());
 }
}