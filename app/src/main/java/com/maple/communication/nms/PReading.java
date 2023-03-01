/*******************************************************************************
 * Copyright (c) 2012 Evelina Vrabie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package com.maple.communication.nms;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.maple.communication.nms.View.GaugeView;


import java.util.Random;

public class PReading extends Activity {

	private GaugeView mGaugeView1;
	private GaugeView mGaugeView2;
	private GaugeView mGaugeView3;private GaugeView mGaugeView4;private GaugeView mGaugeView5;
	private GaugeView mGaugeView6;

	private final Random RAND = new Random();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gaugemain);
		mGaugeView1 = (GaugeView) findViewById(R.id.gauge_view1);
		mGaugeView2 = (GaugeView) findViewById(R.id.gauge_view2);
		mGaugeView3 = (GaugeView) findViewById(R.id.gauge_view1a);
		mGaugeView4 = (GaugeView) findViewById(R.id.gauge_view2a);
		mGaugeView5 = (GaugeView) findViewById(R.id.gauge_view3);
		mGaugeView6 = (GaugeView) findViewById(R.id.gauge_view3a);

		mTimer.start();
	}

	private final CountDownTimer mTimer = new CountDownTimer(30000, 1000) {

		@Override
		public void onTick(final long millisUntilFinished) {
			mGaugeView1.setTargetValue(RAND.nextInt(101));
			mGaugeView2.setTargetValue(RAND.nextInt(101));
			mGaugeView3.setTargetValue(RAND.nextInt(101));
			mGaugeView4.setTargetValue(RAND.nextInt(101));
			mGaugeView5.setTargetValue(RAND.nextInt(101));
			mGaugeView6.setTargetValue(RAND.nextInt(101));

		}

		@Override
		public void onFinish() {}
	};
}
