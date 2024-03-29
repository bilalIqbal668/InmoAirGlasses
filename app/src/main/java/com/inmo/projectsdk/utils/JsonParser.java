package com.inmo.projectsdk.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class JsonParser {


    public static String multipartFormRequestForFindFriends(String requestURL, String charset,
                                                            JSONObject requestHeadersData, JSONObject fileRequestData) {


        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);


            if (requestHeadersData != null) {
                for (Iterator<String> keys = requestHeadersData.keys(); keys.hasNext(); ) {
                    String key = keys.next();
                    multipart.addFormField(key, requestHeadersData.getString(key));
                }
            }

            if (fileRequestData != null) {
                for (Iterator<String> keys = fileRequestData.keys(); keys.hasNext(); ) {
                    String key = keys.next();
                    Log.d("key", key);

                    multipart.addFilePart(key, new File(fileRequestData.getString(key)));
                }
            }


            List<String> response = multipart.finish();

            Log.i("ISSUE", response.toString());

            StringBuilder stringBuilder = new StringBuilder();

            for (String line : response) {
                stringBuilder.append(line);
            }

            Log.e("Server Response: ", stringBuilder.toString());
            JSONObject ab = new JSONObject(stringBuilder.toString());
            Log.d(" respons", ab.toString());

            return stringBuilder.toString();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.d(" error", e.toString());
            e.printStackTrace();
            return null;
        }

    }

}
