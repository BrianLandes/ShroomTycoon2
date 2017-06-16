package com.bytesbrothers.shroomtycoon.pools;

import com.bytesbrothers.shroomtycoon.elements.Alert;

import easypool.ObjectPool;

public class AlertPool extends ObjectPool<Alert> {

	public AlertPool() {
		super( 10 );
	}

	@Override
	protected Alert createObject() {
		return new Alert();
	}

}
