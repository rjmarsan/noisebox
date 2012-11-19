package com.rj.noisebox;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import android.graphics.Color;
import android.view.MotionEvent;

public class SketchLayer {

	float scl = 1;
	PFont font;

	float mainx, mainy;
	float lastx, lasty;
	float secondx, secondy;
	boolean maindown, seconddown;
	int numSteps = 8;
	int step = 4;
	float stepscale = -0.9f;
	float beatprogress = 0.3f;

	int bg = Color.rgb(100, 100, 100);
	int bglines = Color.argb(100, 17, 211, 193);
	int maincircle = Color.argb(150, 133, 185, 11);
	int secondcircle = Color.argb(150, 133, 185, 11);
	int selectedarc = Color.argb(150, 178, 255, 0);
	int circleborder = Color.argb(90, 0, 0, 0);
	int clocklinebegin = Color.argb(0, 178, 255, 0);
	int clocklineend = Color.argb(100, 178, 255, 0);
	int connectlinebegin = Color.argb(100, 1, 1, 1);
	int connectlineend = Color.argb(0, 1, 1, 1);

	float circlesize = 90;
	float stepswidth = 100;
	float linewidth = 1;
	float animspeed = 0.2f;

	float distance;
	float angle;

	public void setup(PApplet p) {
		p.smooth(8);
		font = p.loadFont("SIL-Kai-Reg-Jian-48.vlw");
		p.textAlign(PConstants.CENTER);
		p.ellipseMode(PConstants.CENTER);
		p.frameRate(30);
		p.background(0);

		// font = loadFont("STSong-48.vlw");

		mainx = 30;
		mainy = 300;
		maindown = true;
		secondx = 320;
		secondy = 102;
		seconddown = true;
		scaleItAll(p);
	}

	public void scaleItAll(PApplet p) {
		android.util.DisplayMetrics dm = new android.util.DisplayMetrics();
		p.getWindowManager().getDefaultDisplay().getMetrics(dm);
		scl = dm.density;
		PApplet.print("Scale: " + scl);
		circlesize *= scl;
		stepswidth *= scl;
		linewidth *= scl;
	}

	public void draw(PApplet p) {
		drawBackground(p);
		drawGrid(p);
		drawNumbers(p);
		drawCursor(p);

		update(p);
	}

	public void drawBackground(PApplet p) {
		p.pushStyle();
		// noStroke();
		// fill(bg);
		p.background(bg);
		// rect(0, 0, width, height);
		p.popStyle();
	}

	public void drawGrid(PApplet p) {
		p.pushStyle();
		p.stroke(bglines);
		drawLines(p, 10);
		p.popStyle();
	}

	public void drawNumbers(PApplet p) {
		p.pushStyle();

		p.popStyle();
	}

	public void drawLines(PApplet p, int lines) {
		float spacingx = PApplet.parseFloat(p.width) / PApplet.parseFloat(lines);
		for (int i = 1; i < lines; i++) {
			p.line(spacingx * i, 0, spacingx * i, p.height);
		}
	}

	public void drawCursor(PApplet p) {
		p.pushMatrix();

		if (maindown && seconddown) {
			distance = PApplet.dist(mainx, mainy, secondx, secondy);
			angle = PApplet.atan2(secondy - mainy, secondx - mainx);
		} else {

		}

		p.translate(mainx, mainy);
		p.rotate(angle);

		if (maindown && seconddown) {
			p.noStroke();
			p.beginShape();
			p.fill(connectlinebegin);
			p.vertex(circlesize / 2, -linewidth);
			p.vertex(circlesize / 2, linewidth);

			p.fill(connectlineend);
			p.vertex(distance - circlesize / 2, linewidth);
			p.vertex(distance - circlesize / 2, -linewidth);
			p.endShape();
			// line(circlesize/2,0,distance-circlesize/2,0);
		}

		if (maindown) {
			p.noStroke();
			float arcwidth = PConstants.TWO_PI / numSteps;
			float stepsize = stepswidth / numSteps * stepscale;
			float basesize = circlesize;
			if (stepsize < 0) {
				basesize += stepswidth * PApplet.abs(stepscale);
			}
			for (int i = 0; i < numSteps; i++) {
				float arcsize = basesize + stepsize * i;
				if (i == step) {
					p.fill(selectedarc);
				} else {
					p.fill(maincircle);
				}
				p.arc(0, 0, arcsize, arcsize, arcwidth * (i), arcwidth * (i + 1));
			}
			p.noFill();
			p.stroke(circleborder);
			p.ellipse(0, 0, circlesize, circlesize);
		}

		if (seconddown) {
			p.stroke(circleborder);
			p.fill(secondcircle);
			p.ellipse(distance, 0, circlesize, circlesize);
		}

		p.popMatrix();

		if (maindown) {
			drawClockLine(p);
		}
	}

	public void drawClockLine(PApplet p) {
		p.noStroke();
		p.beginShape();
		// vertex(mainx,0);
		float startx = PApplet.lerp(lastx, mainx, beatprogress - 0.3f);
		float endx = PApplet.lerp(lastx, mainx, beatprogress);
		float starty = PApplet.lerp(0, mainy - circlesize / 2, beatprogress - 0.3f);
		float endy = PApplet.lerp(0, mainy - circlesize / 2, beatprogress);

		p.fill(clocklinebegin);
		p.vertex(startx + linewidth, starty);
		p.vertex(startx, starty);

		p.fill(clocklineend);
		p.vertex(endx, endy);
		p.vertex(endx + linewidth, endy);
		p.endShape();
	}

	public void update(PApplet p) {
		relaxLastCursor();
		mockUpdates(p);
	}

	public void relaxLastCursor() {
		lastx = PApplet.lerp(lastx, mainx, animspeed);
		lasty = PApplet.lerp(lasty, mainy, animspeed);
	}

	public void mockUpdates(PApplet p) {
		float speed = 0.1f * ((p.height - mainy) / p.height);
		beatprogress += speed;
		if (beatprogress > 1)
			beatprogress = 0;
		step = PApplet.parseInt(beatprogress * numSteps);
		if (seconddown)
			stepscale = (mainx - secondx) / (p.width / 2);
	}

	public void surfaceTouchEvent(MotionEvent event) {
	}


}
