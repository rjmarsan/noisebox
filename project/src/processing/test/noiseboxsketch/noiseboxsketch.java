package processing.test.noiseboxsketch;

import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import android.view.MotionEvent; 
import android.view.KeyEvent; 
import android.graphics.Bitmap; 
import java.io.*; 
import java.util.*; 

public class noiseboxsketch extends PApplet {

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



int bg = color(100,100,100);
int bglines = color(17, 211, 193, 100);
int maincircle = color(133, 185, 11, 150);
int secondcircle = color(133, 185, 11, 150);
int selectedarc = color(178, 255, 0, 150);
int circleborder = color(0, 0, 0, 90);
int clocklinebegin = color(178, 255, 0, 0);
int clocklineend = color(178, 255, 0, 100);
int connectlinebegin = color(1, 1, 1, 100);
int connectlineend = color(1, 1, 1, 0);


float circlesize = 90;
float stepswidth = 100;
float linewidth = 1;
float animspeed = 0.2f;


float distance;
float angle;



public void setup() {
 
  orientation(LANDSCAPE);
  smooth(8);
  font = loadFont("SIL-Kai-Reg-Jian-48.vlw");
  textAlign(CENTER);
  ellipseMode(CENTER);
  frameRate(30);
  background(0);

  
  //font = loadFont("STSong-48.vlw");
  
  mainx = 30;
  mainy = 300;
  maindown = true;
  secondx = 320;
  secondy = 102;
  seconddown = true;
  scaleItAll();
}

public void scaleItAll() {
  android.util.DisplayMetrics dm = new android.util.DisplayMetrics();
  getWindowManager().getDefaultDisplay().getMetrics(dm);
  scl = dm.density; 
  print("Scale: "+scl);
  circlesize *= scl;
  stepswidth *= scl;
  linewidth  *= scl;
}



public void draw() {
  drawBackground();
  drawGrid();
  drawNumbers();
  drawCursor();
  
  update();
}

public void drawBackground() { 
  pushStyle();
  //noStroke();
  //fill(bg);
  background(bg);
  //rect(0, 0, width, height);
  popStyle();
}

public void drawGrid() { 
  pushStyle();
  stroke(bglines);
  drawLines(10);
  popStyle();
}

public void drawNumbers() { 
  pushStyle();
  
  popStyle();
}


public void drawLines(int lines) {
  float spacingx = PApplet.parseFloat(width)/PApplet.parseFloat(lines);
  for (int i=1; i<lines; i++) {
    line(spacingx*i, 0, spacingx*i, height);
  }
}




public void drawCursor() {
  pushMatrix();
  
  if (maindown && seconddown) {
    distance = dist(mainx, mainy, secondx, secondy);
    angle = atan2(secondy-mainy, secondx-mainx);
  } else {
    
  }
  
  translate(mainx, mainy);
  rotate(angle);
  
  if (maindown && seconddown) {
    noStroke();
    beginShape();    
      fill(connectlinebegin);
      vertex(circlesize/2,-linewidth);
      vertex(circlesize/2,linewidth);
      
      fill(connectlineend);
      vertex(distance-circlesize/2,linewidth);
      vertex(distance-circlesize/2,-linewidth);
    endShape();
    //line(circlesize/2,0,distance-circlesize/2,0);
  }
  
  
  if (maindown) {    
    noStroke();
    float arcwidth = TWO_PI/numSteps;
    float stepsize = stepswidth/numSteps*stepscale;
    float basesize = circlesize;
    if (stepsize < 0) {
      basesize += stepswidth*abs(stepscale);
    }
    for (int i=0; i<numSteps; i++) {
      float arcsize = basesize + stepsize*i;
      if (i == step) {
        fill(selectedarc);
      } else {
        fill(maincircle);
      }
      arc(0,0,arcsize,arcsize,arcwidth*(i),arcwidth*(i+1));
    }
    noFill();
    stroke(circleborder);
    ellipse(0,0,circlesize,circlesize); 
  }
  
  if (seconddown) {
    stroke(circleborder);
    fill(secondcircle);
    ellipse(distance,0,circlesize,circlesize);
  }
  
  popMatrix();
  
  if (maindown) {
    drawClockLine();
  }
}


public void drawClockLine() {
  noStroke();
  beginShape();
    //vertex(mainx,0);
    float startx = lerp(lastx, mainx, beatprogress-0.3f);
    float endx = lerp(lastx, mainx, beatprogress);
    float starty = lerp(0, mainy-circlesize/2, beatprogress-0.3f);
    float endy = lerp(0, mainy-circlesize/2, beatprogress);
    
    fill(clocklinebegin);
    vertex(startx+linewidth,starty);
    vertex(startx,starty);
    
    fill(clocklineend);
    vertex(endx,endy);
    vertex(endx+linewidth,endy);
  endShape();
}



public void mouseMoved() {
  lastx = mainx;
  lasty = mainy;
  mainx = mouseX;
  mainy = mouseY;
}



public void update() {
  relaxLastCursor();
  mockUpdates();
}

public void relaxLastCursor() {
  lastx = lerp(lastx,mainx,animspeed); 
  lasty = lerp(lasty,mainy,animspeed); 
}

public void mockUpdates() {
  float speed = 0.1f * ((height-mainy) / height);
  beatprogress += speed;
  if (beatprogress > 1) beatprogress = 0;  
  step = PApplet.parseInt(beatprogress * numSteps);
  if (seconddown) stepscale = (mainx-secondx)/(width/2);
}

//#IFDEF ANDROID_MODE
public boolean surfaceTouchEvent(MotionEvent event) {
  if (event.getPointerCount() >= 2) {
    maindown = true;
    seconddown = true;
    mainx = event.getX(0);
    mainy = event.getY(0);
    secondx = event.getX(1);
    secondy = event.getY(1);
  } else if (event.getPointerCount() == 1) {
    maindown = true;
    seconddown = false;
    if (event.getAction() == MotionEvent.ACTION_MOVE) {
      mainx = event.getX(0);
      mainy = event.getY(0);
    }
  } else {
    maindown = false;
    seconddown = false;
  }
  return super.surfaceTouchEvent(event);
}
//#ENDIF ANDROID_MODE

  public int sketchWidth() { return displayWidth; }
  public int sketchHeight() { return displayHeight; }
  public String sketchRenderer() { return OPENGL; }
}
