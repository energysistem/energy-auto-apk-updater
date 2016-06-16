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
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.energysistem.energyautoapkupdater.lib.Updater;
import com.energysistem.energyautoapkupdater.lib.business.events.OnUpdateCompleted;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Updater updater = new Updater();
        updater.setUrl("http://fake.url");

        updater.setOnUpdateCompleted(new OnUpdateCompleted()
        {
            @Override
            public void onUpdateCompleted()
            {
                Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUpdateFailed(final Exception ex)
            {
                Toast.makeText(MainActivity.this, "Error! " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        updater.update(this);
    }
}
