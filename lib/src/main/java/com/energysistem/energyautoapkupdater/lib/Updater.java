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

import com.energysistem.energyautoapkupdater.lib.business.downloader.Downloader;
import com.energysistem.energyautoapkupdater.lib.business.downloader.events.OnDownloadCompleted;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateCompleted;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateFailed;
import com.energysistem.energyautoapkupdater.lib.business.exceptions.NullOrEmptyURLException;
import com.energysistem.energyautoapkupdater.lib.business.log.Log;
import com.energysistem.energyautoapkupdater.lib.business.shell.Shell;
import com.energysistem.energyautoapkupdater.lib.business.shell.events.OnExecutionFinished;

import java.io.IOException;

/**
 * Created by HMD on 08/06/2016.
 *
 * This class is the main interface of the Energy Auto Apk Updater tool. Instantiate this class
 * to get a full use of it.
 */
public final class Updater
{
    private final String TAG = this.getClass().getName();
    /**
     * On update failed object
     **/
    private OnUpdateFailed onUpdateFailed;
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
        shell = new Shell();
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
    public void update() throws NullOrEmptyURLException
    {
        if (getUrl() == null || getUrl().isEmpty())
            throw new NullOrEmptyURLException("URL is not set or is empty.");

//      Download the file
        Downloader downloader = new Downloader(url);
        downloader.setOnDownloadCompleted(new OnDownloadCompleted()
        {
            @Override
            public void onDownloadCompleted(boolean success, String file_location)
            {
                if (!success)
                {
                    Log.log(TAG, "Download failed.", Log.Type.ERROR);
                    if (onUpdateFailed != null)
                        onUpdateFailed.onUpdateFailed();
                    return;
                }

                try
                {
                    shell.init();
//                  Set post execution action
                    shell.setOnExecutionFinished(new OnExecutionFinished()
                    {
                        @Override
                        public void onExecutionFinished(int result)
                        {
                            Log.log(TAG, "Command executed successfully!.", Log.Type.INFO);
                            if (result == 0)
                            {
                                if (onUpdateCompleted != null)
                                    onUpdateCompleted.onUpdateCompleted();
                            }
                            else
                            {
                                Log.log(TAG, "Something went wrong with the Package Manager, please try later. Check PM log for more information.", Log.Type.ERROR);
                                if (onUpdateFailed != null)
                                    onUpdateFailed.onUpdateFailed();
                            }
                        }
                    });
//                  Execute installation command
                    String command = "pm install " + file_location;
                    shell.executeCommand(command);
                }
                catch (IOException e)
                {
                    Log.log(TAG, "Unable to use the shell. " + e.getMessage(), Log.Type.ERROR);
                }
                catch (InterruptedException e)
                {
                    Log.log(TAG, "Shell thread stopped. " + e.getMessage(), Log.Type.ERROR);
                }
            }
        });
    }
}
