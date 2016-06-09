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

package com.energysistem.energyautoapkupdater.lib.business.shell;

import com.energysistem.energyautoapkupdater.lib.business.shell.events.OnExecutionFailed;
import com.energysistem.energyautoapkupdater.lib.business.shell.events.OnExecutionFinished;
import com.energysistem.energyautoapkupdater.lib.business.threads.ErrorHandlerThread;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by HMD on 08/06/2016.
 *
 * This class is intended to handle the communication with the shell
 */
public class Shell
{
    /**
     * Super user process that will execute commands
     * */
    private Process su;
    /**
     * Stream to write in the shell
     **/
    private DataOutputStream dos;
    /**
     * Event of command execution finished
     **/
    private OnExecutionFinished onExecutionFinished;
    /**
     * Event of command execution failed
     **/
    private OnExecutionFailed onExecutionFailed;

    public Shell() {}

    /**
     * Initializes the shell in Super user
     **/
    public void init() throws IOException
    {
        su = Runtime.getRuntime().exec("su");
        dos = new DataOutputStream(su.getOutputStream());
    }
    /**
     * Executes a command in the shell in  anew thread and waits for it.
     **/
    public void executeCommand(String command) throws IOException, InterruptedException
    {
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
                    int result = su.waitFor();
//                  Call event
                    if (onExecutionFinished != null)
                        onExecutionFinished.onExecutionFinished(result);
                }
                catch (IOException | InterruptedException e)
                {
                    if (onExecutionFailed != null)
                        onExecutionFailed.onCommandExecutionFailed(e);
//                  Once caught throw a RuntimeException to allow the Thread to do further actions
                    throw new RuntimeException(e.getMessage());
                }
            }
        }.start();

    }
    /**
     * Implement execution finished event.
     * */
    public void setOnExecutionFinished(OnExecutionFinished onExecutionFinished)
    {
        this.onExecutionFinished = onExecutionFinished;
    }
}
