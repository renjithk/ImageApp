package com.image.tech.network;

import android.util.Log;

import com.image.tech.exception.AppException;

import org.apache.http.params.BasicHttpParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * This class handles all network operations.
 * It helps you connect to required URL, open a valid input stream
 * and fetch JSON data.
 */
public class NetworkHandler {
    private BasicHttpParams httpParams;

    /**
     * Connects to server and opens a valid input stream
     * @return valid JSON string
     * @throws AppException if an error occurs during data fetch.
     */
    public String readFromServer() throws AppException {
        HttpURLConnection urlConnection = null;
        BufferedInputStream bufferedInputStream = null;
        URL serverUrl = null;
        String result = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = Long.toHexString(System.currentTimeMillis());

        try {
            serverUrl = new URL("http://i.img.co/data/data.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new AppException(AppException.MALFORMED_URL);
        }

        try {
            urlConnection = (HttpURLConnection) serverUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(AppException.CONNECTION_OPEN_FAIL);
        }

        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(5000); //set connection timeout of 5s
        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new AppException(AppException.ERROR_HTTP_PROTOCOL);
        }
        urlConnection.setRequestProperty("Content-Type", "text/plain");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try {
            bufferedInputStream  = new BufferedInputStream(urlConnection.getInputStream());
            Log.i("Network", "bufferedInputStream ? " + bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(AppException.INPUT_STREAM_OPEN_FAILURE);
        }
        try {
            result = getReadableStream(bufferedInputStream);
            Log.i("NetworkHandler", "result ? " + result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(AppException.READABLE_STREAM_ERROR);
        } finally {
            if(null != bufferedInputStream) try {
                bufferedInputStream.close();
            } catch (IOException e) {
                throw new AppException(AppException.STREAM_CLOSE_FAIL);
            }

            if(null != urlConnection) urlConnection.disconnect();
        }

        return result;
    }

    /**
     * Reads data from the input stream and converts to a readable format
     * @param is valid input stream
     * @return a valid JSON string
     * @throws IOException
     */
    private String getReadableStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();

        String streamAsString = null;
        try {
            while ((streamAsString = reader.readLine()) != null) {
                buffer.append(streamAsString + "\n");
            }
        } finally {
            if(null != reader) reader.close();
            if(null != is) is.close();
        }

        return buffer.toString();
    }
}
