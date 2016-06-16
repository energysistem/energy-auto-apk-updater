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

package com.energysistem.energyautoapkupdater.lib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.energysistem.energyautoapkupdater.lib.business.downloader.Downloader;
import com.energysistem.energyautoapkupdater.lib.business.downloader.events.OnDownload;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateCompleted;
import com.energysistem.energyautoapkupdater.lib.business.exceptions.NullOrEmptyURLException;
import com.energysistem.energyautoapkupdater.lib.business.installer.Installer;
import com.energysistem.energyautoapkupdater.lib.business.installer.InstallerFactory;
import com.energysistem.energyautoapkupdater.lib.business.installer.events.OnInstallationFailed;
import com.energysistem.energyautoapkupdater.lib.business.installer.shell.Shell;
import com.energysistem.energyautoapkupdater.lib.business.log.Log;

import java.io.IOException;

/**
 * Created by HMD on 08/06/2016.
 *
 * This class is the main interface of the Energy Auto Apk Updater tool. Instantiate this class
 * to get a full use of it.
 */
public final class Updater
{
    /**
     * Handler
     **/
    private Handler handler;

    private final String TAG = this.getClass().getName();
    /**
     * On update completed object
     **/
    private OnUpdateCompleted onUpdateCompleted;
    /**
     * This is the URL where the apk file is going to be downloaded.
     * */
    private String url;
    /**
     * Object that holds the shell
     * */
    private Shell shell;
    /**
     * Updater constructor
     **/
    public Updater()
    {
        shell = new Shell("pm");
        handler = new Handler(Looper.myLooper());
    }
    /**
     * Retrieves the URl where is the apk file located.
     *
     * @return A string containing the URL.
     * */
    public String getUrl()
    {
        return url;
    }
    /**
     * Sets the URL where the apk is located.
     * */
    public void setUrl(String url)
    {
        this.url = url;
    }
    /**
     * Updates the apk.
     *
     * @throws NullOrEmptyURLException This exception occurs if the URL is not set or is empty.
     * */
    public void update(final Context context)
    {
//      Download the file
        Downloader downloader = new Downloader(url);
        downloader.setOnDownload(new OnDownload()
        {
            @Override
            public void onDownloadCompleted(String file_location)
            {
                Installer installer = InstallerFactory.build(context, InstallerFactory.Type.PACKAGEMANAGER_INSTALLER);
                if (installer != null)
                {
                    installer.setOnInstallationFailed(new OnInstallationFailed()
                    {
                        @Override
                        public void onInstallationFailed(final Exception ex)
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (onUpdateCompleted != null)
                                        onUpdateCompleted.onUpdateFailed(ex);
                                }
                            });
                        }
                    });
                    installer.install(file_location);
                }
                else
                {
                    Log.log(TAG, "Null installer", Log.Type.ERROR);
                }

            }

            @Override
            public void onDownloadFailed(final String error)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.log(TAG, "Download failed.", Log.Type.ERROR);
                        if (onUpdateCompleted != null)
                            onUpdateCompleted.onUpdateFailed(new IOException("File couldn't be downloaded, " + error));
                    }
                });
            }
        });
        downloader.startDownload();
    }

    public void setOnUpdateCompleted(OnUpdateCompleted onUpdateCompleted)
    {
        this.onUpdateCompleted = onUpdateCompleted;
    }
}
