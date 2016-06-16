/*
 * The MIT License
 *
 * Copyright (c) 2016 Energy Sistem Technology S.A.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.energysistem.energyautoapkupdater.lib.business.downloader;

import android.os.Environment;

import com.energysistem.energyautoapkupdater.lib.business.downloader.events.OnDownload;
import com.energysistem.energyautoapkupdater.lib.business.exceptions.NullOrEmptyURLException;
import com.energysistem.energyautoapkupdater.lib.business.log.Log;
import com.energysistem.energyautoapkupdater.lib.business.threads.ErrorHandlerThread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HMD on 08/06/2016.
 *
 * Class intended to download files using http protocol
 */
public class Downloader
{
    final String TAG = this.getClass().getName();
    /**
     * URL where file is located
     * */
    private String url;
    /**
     * Input Stream to receive incoming file
     **/
    private InputStream is;
    /**
     * Output Stream to write file into storage
     * */
    private OutputStream os;
    /**
     * Http connection that will get the file form the server
     **/
    private HttpURLConnection connection;
    /**
     * Event object
     * */
    private OnDownload onDownload;
    /**
     *  Public constructor
     *
     *  @param url URL of the information/File to download
     **/
    public Downloader(String url)
    {
        this.url = url;
    }
    /**
     * Return the URL
     *
     * @return URL as a String
     * */
    public String getUrl()
    {
        return url;
    }
    /**
     * Set the URL
     *
     * @param url String URL
     * */
    public void setUrl(String url)
    {
        this.url = url;
    }
    /**
     * Downloads certain information form a web server and stores it in a specified location
     *
     * @param destination_path Path where the information/file will be stored
     **/
    private void download(String destination_path) throws IOException, NullOrEmptyURLException {
        if (getUrl() == null || getUrl().isEmpty())
            throw new NullOrEmptyURLException("URL is not set or is empty.");

        URL _url = new URL(url);
        connection = (HttpURLConnection) _url.openConnection();
        connection.connect();
//      Check for info availability
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
        {
            String message = "Content is unavailable";
            Log.log(TAG, message, Log.Type.ERROR);
//          Unable to get information form server, call event with false argument
            if (onDownload != null)
                onDownload.onDownloadFailed(message);
            return;
        }

        is = connection.getInputStream();
        os = new FileOutputStream(destination_path);

//      Set buffer size to read each time, 4096 bytes should be enough
        byte[] buffer = new byte[4096];
        int bytes_read = -1;
        while ((bytes_read = is.read(buffer)) != -1)
        {
            os.write(buffer, 0, bytes_read);
            os.flush();
        }

//      Once finished we close the streams
        is.close();
        os.close();

//      Call event
        Log.log(TAG, "Download complete", Log.Type.INFO);
        if (onDownload != null)
            onDownload.onDownloadCompleted(destination_path);
    }
    /**
     * Start the download on an asynchronous thread, set OnDownloadComplete to
     * provide a post download action and download result.
     **/
    public void startDownload()
    {
        new ErrorHandlerThread()
        {
            @Override
            public void run()
            {
                try
                {
                    String destination_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk";
                    Downloader.this.download(destination_path);
                }
                catch (IOException | NullOrEmptyURLException e)
                {
                    throw new RuntimeException(e.getMessage());
                }
            }

            @Override
            public void onException(Throwable e)
            {
                super.onException(e);
                if (onDownload != null)
                    onDownload.onDownloadFailed(e.getMessage());
            }
        }.start();
    }

    public void setOnDownload(OnDownload onDownload)
    {
        this.onDownload = onDownload;
    }
}
