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

package com.energysistem.energyautoapkupdater.lib.business.installer.shell;

import com.energysistem.energyautoapkupdater.lib.business.installer.A_Installer;
import com.energysistem.energyautoapkupdater.lib.business.threads.ErrorHandlerThread;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by HMD on 08/06/2016.
 *
 * This class is intended to handle the communication with the shell
 */
public class Shell extends A_Installer
{
    /**
     * Process that will execute commands
     * */
    private Process process;
    /**
     * Application name that will execute commands
     **/
    private String app;
    /**
     * Stream to write in the shell
     **/
    private DataOutputStream dos;

    public Shell(String app)
    {
        this.app = app;
    }
    /**
     * Initializes the shell in Super user
     **/
    private void init() throws IOException
    {
        process = Runtime.getRuntime().exec(app);
        dos = new DataOutputStream(process.getOutputStream());
    }
    /**
     * Executes a command in the shell in a new thread and waits for it.
     **/
    private void executeCommand(String command) throws IOException, InterruptedException
    {
        if (process == null || dos == null)
            throw new IOException("Shell isn't initialized.");

        final String final_command = command;
        new ErrorHandlerThread()
        {
            @Override
            public void run()
            {
                try
                {
                    dos.writeBytes(final_command);
                    dos.flush();
                    int result = process.waitFor();
//                  Call event
                    if (result != 1)
                        notifyInstallationFailed(new Exception("Unexpected PM result."));
                    else
                        notifyInstallationSuccess();
                }
                catch (IOException | InterruptedException e)
                {
                    notifyInstallationFailed(e);
//                  Once caught throw a RuntimeException to allow the Thread to do further actions
                    throw new RuntimeException(e.getMessage());
                }
            }
        }.start();

    }

    @Override
    public void install(String apk_path)
    {
        try
        {
            init();
            executeCommand("install" + apk_path);
        }
        catch (IOException | InterruptedException e)
        {
            notifyInstallationFailed(e);
        }
    }
}
