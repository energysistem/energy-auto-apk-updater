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

package com.energysistem.energyautoapkupdater.lib.business.threads;

import com.energysistem.energyautoapkupdater.lib.business.log.Log;

/**
 * Created by HMD on 08/06/2016.
 *
 * Customized thread to handle server services. This thread contains an
 * exception object that points to any possible exception occurred during
 * the execution's process. Every time an exception occurs, this thread will
 * throw a RuntimeException to stop execution and handled exception
 * will be stored. RuntimeExceptions will be automatically caught
 * and executed code may be overridden in the method onException.
 **/
public class ErrorHandlerThread extends Thread
{
    /**
     * Log's TAG
     * */
    private final String TAG = this.getName();
    /**
     * Caught exception will be stored here
     * */
    private Exception exception;
    /**
     * This will handle all occurred exceptions within the thread
     * */
    private ThreadExceptionHandler exceptionHandler;

    public ErrorHandlerThread()
    {
        super();
        exceptionHandler = new ThreadExceptionHandler();
        exception = null;

        super.setUncaughtExceptionHandler(exceptionHandler);
    }
    /**
     * Returns the occurred exception if any.
     *
     * @return Returns null if no exception was thrown
     * */
    public Exception getException()
    {
        return exception;
    }
    /**
     * Code to be executed when an exception is thrown, override this method for customization
     **/
    public void onException(Throwable e)
    {
        Log.log(TAG,"Unexpected error occurred: " + e.getMessage(), Log.Type.ERROR);
    }
    /**
     * UncaughtException handler to catch any runtime exception MPP_Thread could throw
     * */
    private class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler
    {
        @Override
        public void uncaughtException(Thread t, Throwable e)
        {
//          Unhandled exception occurred, so sad! :(
            ErrorHandlerThread.this.onException(e);
        }
    }
}

