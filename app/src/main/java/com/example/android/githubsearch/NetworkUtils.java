package com.example.android.githubsearch;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by hessro on 1/30/18.
 */

public class NetworkUtils {
    public static String doHTTPGet(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        InputStream inputStream;
        try {
            inputStream = urlConnection.getInputStream();
        } finally {
            urlConnection.disconnect();
        }

        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        if (scanner.hasNext()) {
            return scanner.next();
        } else {
            return null;
        }
    }
}
