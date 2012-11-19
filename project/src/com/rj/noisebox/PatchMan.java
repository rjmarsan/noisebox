package com.rj.noisebox;

import org.puredata.android.processing.PureDataP5Android;

import processing.core.PApplet;
import android.util.Log;
import android.view.MotionEvent;

public class PatchMan {
	public final static String TAG = "PatchManager";
	
	public static String PATCH_PREFIX = "1";
	public static String TOUCH_ON =    PATCH_PREFIX+"-on";
	public static String TOUCH_OFF =   PATCH_PREFIX+"-off";
	public static String NOTE_SET =    PATCH_PREFIX+"-basenote";
	public static String NOTE_VOL =    PATCH_PREFIX+"-vol";
	public static String NOTE_WAVE =   PATCH_PREFIX+"-wave";
	public static String SEQ_WIDTH =   PATCH_PREFIX+"-width";
	public static String SEQ_SPEED =   PATCH_PREFIX+"-speed";
	public static String SEQ_WRAP =    PATCH_PREFIX+"-wrap";
	
	PApplet p;
	SketchLayer layer;
	PureDataP5Android pd;
	
	int mainpointerid = -1;
	int secondpointerid = -1;
	float mainx;
	float mainy;
	float secondx;
	
	float note;
	float speed;
	float stepwidth;
	
	public PatchMan(PApplet p, SketchLayer layer, PureDataP5Android pd) {
		this.layer = layer;
		this.p = p;
		this.pd = pd;
	}

	
	public void setup() {
		initpd();
	}
	
	
	
	private void initpd() {
		send(NOTE_VOL, 1f);
		send(NOTE_WAVE, 2f);
		send(SEQ_WIDTH, 1f);
		send(SEQ_SPEED, 0.6f);
		send(SEQ_WRAP, 4f);
	}
	
	private void mainTouchDown() {
		send(TOUCH_ON, 1);
	}
	private void mainTouchUp() {
		send(TOUCH_OFF, 0);
	}
	private void updatePitch() {
		updateValues();
		send(NOTE_SET, note);
		send(SEQ_WIDTH, stepwidth);
		send(SEQ_SPEED, speed);
	}
	private void updateValues() {
		note = getPitch();
		speed = getSpeed();
		if (layer.seconddown) stepwidth = getNoteWidth();
	}
	
	
	
	private float getNoteWidth() {
		return (secondx-mainx)*8;
	}
	private float getSpeed() {
		return (mainy*mainy)*20;
	}
	private float getPitch() {
		return 40f+mainx*60f;
	}
	
	
	
	private void send(String tag, float value) {
		Log.d(TAG, "Sending: "+tag+ " val:"+value);
		pd.sendFloat(tag, value);
	}
	
	
	
	public void receiveFloat(String source, float x) {
		
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
					mainTouchDown();
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
				mainTouchUp();
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
				mainTouchUp();
			}
		}
		
		for (int i=0; i<event.getPointerCount(); i++) {
			int id = event.getPointerId(i);
			if (id == mainpointerid) {
				layer.mainx = event.getX(i);
				layer.mainy = event.getY(i);
				mainx = layer.mainx / p.width;
				mainy = (p.height-layer.mainy) / p.height;
			} else if (id == secondpointerid) {
				layer.secondx = event.getX(i);
				layer.secondy = event.getY(i);
				secondx = layer.secondx / p.width;
			}
		}
		
		updatePitch();
	}
	
}
