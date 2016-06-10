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

package com.energysistem.energyautoapkupdater.example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.energysistem.energyautoapkupdater.lib.Updater;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateCompleted;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateFailed;
import com.energysistem.energyautoapkupdater.lib.business.exceptions.NullOrEmptyURLException;
import com.energysistem.energyautoapkupdater.lib.business.log.Log;

public class MainActivity extends AppCompatActivity
{
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      initialize handler
        handler = new Handler(Looper.getMainLooper());

        Updater updater = new Updater();
        updater.setUrl("http://10.42.0.1:80/update.apk");
        updater.setOnUpdateCompleted(new OnUpdateCompleted()
        {
            @Override
            public void onUpdateCompleted()
            {
                Toast.makeText(MainActivity.this, "Actualizado!", Toast.LENGTH_LONG).show();
            }
        });

        updater.setOnUpdateFailed(new OnUpdateFailed()
        {
            @Override
            public void onUpdateFailed(final Exception ex)
            {
                Toast.makeText(MainActivity.this, "Error! " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        try
        {
            updater.update(this);
        }
        catch (NullOrEmptyURLException e)
        {
            Log.log("Example App", "Malformed URL", Log.Type.ERROR);
        }
    }
}
