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

import com.energysistem.energyautoapkupdater.lib.business.downloader.events.OnDownloadCompleted;

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
    private OnDownloadCompleted onDownloadCompleted;

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
     *
     **/
    public void download(String destination_path) throws IOException
    {
        URL _url = new URL(url);
        connection = (HttpURLConnection) _url.openConnection();
        //TODO: finish method

    }

    public void setOnDownloadCompleted(OnDownloadCompleted onDownloadCompleted)
    {
        this.onDownloadCompleted = onDownloadCompleted;
    }
}
