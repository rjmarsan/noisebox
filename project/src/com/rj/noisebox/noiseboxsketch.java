package com.rj.noisebox;

import org.puredata.android.processing.PureDataP5Android;

import processing.core.PApplet;
import android.view.MotionEvent;

public class noiseboxsketch extends PApplet {
	SketchLayer layer;
	PureDataP5Android pd;
	
	public void setup() {
		layer = new SketchLayer();
		layer.setup(this);
		pd = new PureDataP5Android(this, 44100, 0, 2);
		int zipId = R.raw.patch; // Processing masks R
		pd.unpackAndOpenPatch(zipId, "_metabox1.pd");
//		pd.subscribe("foo");  // Uncomment if you want to receive messages sent to the receive symbol "foo" in Pd.
		pd.start();
	}
	
	public void stop() {
		pd.release();
		super.stop();
	}

	public void draw() {
		layer.draw(this);
	}

	public boolean surfaceTouchEvent(MotionEvent event) {
		layer.surfaceTouchEvent(event);
		return super.surfaceTouchEvent(event);
	}
	
	
	/*
	// Implement methods like the following if you want to receive messages from Pd.
	// You'll also need to subscribe to receive symbols you're interested if you want
	// to receive messages.
	
	public void pdPrint(String s) {
		// Handle string s, printed by Pd
	}
	
	public void receiveBang(String source) {
		// Handle bang sent to symbol source in Pd
	}
	
	public void receiveFloat(String source, float x) {
		// Handle float x sent to symbol source in Pd
	}
	
	public void receiveSymbol(String source, String sym) {
		// Handle symbol sym sent to symbol source in Pd
	}
	*/


	public int sketchWidth() { return displayWidth; }
	public int sketchHeight() {	return displayHeight; }
	public String sketchRenderer() { return OPENGL; }
}
