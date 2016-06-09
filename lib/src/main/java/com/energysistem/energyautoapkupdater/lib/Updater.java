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

import com.energysistem.energyautoapkupdater.lib.business.exceptions.NullOrEmptyURLException;
import com.energysistem.energyautoapkupdater.lib.business.exceptions.ShellException;
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
    /**
     * This is the URL where the apk file is going to be downloaded.
     * */
    private String url;
    /**
     * Object that holds the shell
     * */
    private Shell shell;

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
    public void update() throws NullOrEmptyURLException, ShellException
    {
        if (getUrl() == null || getUrl().isEmpty())
            throw new NullOrEmptyURLException("URL is not set or is empty.");

        try
        {
            shell.init();
        }
        catch (IOException e)
        {
            throw new ShellException("Shell can't be initialized: " + e.getMessage());
        }
//      Set post execution action
        shell.setOnExecutionFinished(new OnExecutionFinished()
        {
            @Override
            public void onExecutionFinished(int result)
            {
                //TODO: add action to what will occur once the command is finished
            }
        });
        //TODO: Download apk file
    }
}
