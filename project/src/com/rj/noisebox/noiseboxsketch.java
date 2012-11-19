package com.rj.noisebox;

import org.puredata.android.processing.PureDataP5Android;

import processing.core.PApplet;
import android.view.MotionEvent;

public class noiseboxsketch extends PApplet {
	SketchLayer layer;
	PureDataP5Android pd;
	PatchMan patchman;
	
	public void setup() {
		layer = new SketchLayer();
		layer.setup(this);
		pd = new PureDataP5Android(this, 44100, 0, 2);
		patchman = new PatchMan(this, layer, pd);
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
		patchman.surfaceTouchEvent(event);
		layer.surfaceTouchEvent(event);
		return super.surfaceTouchEvent(event);
	}
	
	public void receiveFloat(String source, float x) {
		patchman.receiveFloat(source, x);
	}

	public int sketchWidth() { return displayWidth; }
	public int sketchHeight() {	return displayHeight; }
	public String sketchRenderer() { return OPENGL; }
}
