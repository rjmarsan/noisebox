package com.rj.noisebox;

import org.puredata.android.processing.PureDataP5Android;

import processing.core.PApplet;
import android.util.Log;
import android.view.MotionEvent;

public class PatchMan {
	public final static String TAG = "PatchManager";
	PApplet p;
	SketchLayer layer;
	PureDataP5Android pd;
	
	int mainpointerid = -1;
	int secondpointerid = -1;
	
	public PatchMan(PApplet p, SketchLayer layer, PureDataP5Android pd) {
		this.layer = layer;
		this.p = p;
		this.pd = pd;
	}
	
	
	
	
	
	private void mainTouchDown() {
		pd.sendFloat("1-on", 1);
	}
	private void mainTouchUp() {
		pd.sendFloat("1-off", 1);
	}
	
	private void updatePitch() {
		
	}
	
	
	
	
	
	
	
	

	public void surfaceTouchEvent(MotionEvent event) {
		
		if ( (event.getActionMasked()) == MotionEvent.ACTION_DOWN || (event.getActionMasked()) == MotionEvent.ACTION_POINTER_DOWN) {
			for (int i=0; i<event.getPointerCount(); i++) {
				int id = event.getPointerId(i);
				Log.d(TAG, String.format("Looking at %d with %d %d", id, mainpointerid, secondpointerid));
				if (mainpointerid == -1 && id != secondpointerid) {
					mainpointerid = id;
					layer.maindown = true;
					Log.d(TAG, "Main pointer down "+id+", "+i);
				} else if (secondpointerid == -1 && id != mainpointerid) {
					secondpointerid = id;
					layer.seconddown = true;
					Log.d(TAG, "Second pointer down "+id+", "+i);
				}
			}
		}
		
		
		if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
			boolean maindown = false;
			boolean seconddown = false;
			for (int i=0; i<event.getPointerCount(); i++) {
				int id = event.getPointerId(i);
				if (id == mainpointerid) {
					maindown = true;
				}
				if (id == secondpointerid) {
					seconddown = true;
				}
			}
			if (maindown == false) {
				mainpointerid = -1;
				layer.maindown = false;
			}
			if (seconddown == false){
				secondpointerid = -1;
				layer.seconddown = false;
			}
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			if (event.getPointerCount() == 1) {
				mainpointerid = -1;
				layer.maindown = false;
				secondpointerid = -1;
				layer.seconddown = false;
			}
		}
		
		for (int i=0; i<event.getPointerCount(); i++) {
			int id = event.getPointerId(i);
			if (id == mainpointerid) {
				layer.mainx = event.getX(i);
				layer.mainy = event.getY(i);
			} else if (id == secondpointerid) {
				layer.secondx = event.getX(i);
				layer.secondy = event.getY(i);
			}
		}
		
	}
	
	public void receiveFloat(String source, float x) {
	}
}
