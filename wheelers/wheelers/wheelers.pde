import controlP5.*;
import oscP5.*;
import netP5.*;

ArrayList wheels = new ArrayList();
Wheel newWheel;

OscP5 osc_s;
NetAddress destAddress; 

  float tempoFactor = 0.1;

float dt = 0; // time since last draw() 

ControlP5 gui;
Accordion accordionUI;

ArrayList palettes = new ArrayList();
int curPaletteIndex = 0;

void setup(){
  size(320,480);
  smooth();
  frameRate(100);
  
  String destIP = "127.0.0.1";
  int destPort = 7777;
  destAddress = new NetAddress(destIP, destPort);
  osc_s = new OscP5(this, destPort);
  
  makePalettes(dataPath("palettes"));
  
  guiSetup();
}

void draw(){
  dt = millis() - dt;
  dt = floor(dt/100); 
  // I must be doing something wrong here...
  // so forget it and set it manually
  
  dt = 100;
  //println(dt);
  background(0);
  fill(255);
  noStroke();
  ellipse(width/2, height/2, 13, 13);
  for (int i = wheels.size() - 1; i >= 0; i-- ){
    Wheel w = (Wheel) wheels.get(i);
    w.update();
    w.draw();
  }
}

void keyPressed(){
  // create new wheel at random position
  if (key == 'w'){
    newWheel();
  }
  if (key == 'n'){
    newNode();
  }

}

