package com.rj.noisebox;

import processing.core.PApplet;
import processing.core.PFont;
import android.view.MotionEvent;

public class noiseboxsketch extends PApplet {
	SketchLayer layer;

	public void setup() {
		layer = new SketchLayer();
		layer.setup(this);
	}

	public void draw() {
		layer.draw(this);
	}

	public boolean surfaceTouchEvent(MotionEvent event) {
		layer.surfaceTouchEvent(event);
		return super.surfaceTouchEvent(event);
	}


	public int sketchWidth() {
		return displayWidth;
	}

	public int sketchHeight() {
		return displayHeight;
	}

	public String sketchRenderer() {
		return OPENGL;
	}
}
