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

import android.content.Context;

import com.energysistem.energyautoapkupdater.lib.business.installer.packagemanager.PackageManager;
import com.energysistem.energyautoapkupdater.lib.business.installer.shell.Shell;

/**
 * Created by HMD on 10/06/2016.
 *
 * Installer factory
 */
public class InstallerFactory
{
    /**
     * Builds an installer based on parameter. Installer differences:
     *
     * - Shell: The installer will install automatically the package
     * - Package manager: The installer will pop the PM wizard to install the package
     *
     * @param context Application context
     * @param type Tpe of installer to build
     *
     * @return Returns the installer
     **/
    public static Installer build(Context context, Type type)
    {
        switch (type)
        {
            case SHELL_INSTALLER:
                return new Shell("pm");

            case PACKAGEMANAGER_INSTALLER:
                return new PackageManager(context);

            default: return null;
        }
    }

    public enum Type
    {
        SHELL_INSTALLER,
        PACKAGEMANAGER_INSTALLER
    }
}
