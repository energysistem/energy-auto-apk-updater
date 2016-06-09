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

package com.energysistem.energyautoapkupdater.lib.business.log;

/**
 * Created by HMD on 08/06/2016.
 * <p/>
 * This class is intended to output logs, this class can't be
 * instantiated.
 */
public class Log
{
    private Log(){}

    /**
     * Logs information using android logger
     *
     * @param tag TAG to be user in this log
     * @param message Log's message
     * @param type Type of log to be displayed
     **/
    public static void log(String tag, String message, Type type)
    {
        switch (type)
        {
            case DEBUG:
                android.util.Log.d(tag, message);
                break;

            case ERROR:
                android.util.Log.e(tag, message);
                break;

            case INFO:
                android.util.Log.i(tag, message);
        }
    }
    /**
     * Type of log to be logged by Log class
     **/
    public enum Type
    {
        DEBUG,
        ERROR,
        INFO
    }

}
