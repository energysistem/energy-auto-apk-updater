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

package com.energysistem.energyautoapkupdater.lib.business.installer;

import com.energysistem.energyautoapkupdater.lib.business.installer.events.OnInstallationFailed;
import com.energysistem.energyautoapkupdater.lib.business.installer.events.OnInstallationSuccess;

/**
 * Created by HMD on 10/06/2016.
 *
 * Installer abstract class
 */
public class A_Installer implements Installer
{
    /**
     * Installation failed event
     * */
    private OnInstallationFailed onInstallationFailed;
    /**
     * Installation success event
     * */
    private OnInstallationSuccess onInstallationSuccess;
    /**
     * Empty definition
     **/
    public void install(String apk_path) {}
    /**
     * Notify when installation had success
     * */
    protected void notifyInstallationSuccess()
    {
        if (onInstallationSuccess != null)
            onInstallationSuccess.onInstalationSuccess();
    }
    /**
     * Notify when installation has failed
     * */
    protected void notifyInstallationFailed(Exception e)
    {
        if (onInstallationFailed != null)
            onInstallationFailed.onInstallationFailed(e);
    }
    /**
     * Set the event that occurs when installation fails
     **/
    @Override
    public void setOnInstallationFailed(OnInstallationFailed onInstallationFailed)
    {
        this.onInstallationFailed = onInstallationFailed;
    }
    /**
     * Set the event that occurs when installation succeeds
     **/
    @Override
    public void setOnInstallationSuccess(OnInstallationSuccess onInstallationSuccess)
    {
        this.onInstallationSuccess = onInstallationSuccess;
    }
}
