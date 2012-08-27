/**
 * Bouncy Bubbles. 
 * Based on code from Keith Peters (www.bit-101.com). 
 * 
 * Multiple-object collision.
 *
 * jbloit : added bounce event, sound() method and and OSC message, controling a max/msp sound engine.
 */
 
 
import oscP5.*;
import netP5.*;
import processing.video.*;

MovieMaker mm;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

int numBalls = 5;
float spring = 0.1;
float gravity = 0.19;
float friction = -0.9;
Ball[] balls = new Ball[numBalls];
boolean recording = true;


void setup() 
{
  size(640, 200);
  noStroke();
  smooth();
  
  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this,12000);
  myRemoteLocation = new NetAddress("127.0.0.1",12000);
  
  for (int i = 0; i < numBalls; i++) {
    balls[i] = new Ball(random(width), random(height), random(20, 40), i, balls);
  }
  
//  // Record animation
//  // Save uncompressed, at 15 frames per second
//  mm = new MovieMaker(this, width, height, "drawing.mov",30);
}

void draw() 
{
  background(0);
  for (int i = 0; i < numBalls; i++) {
    balls[i].collide();
    balls[i].move();
    balls[i].display();  
    balls[i].sound(i);  
  }
     

}





class Ball {
  float x, y;
  float diameter;
  float vx = 0;
  float vy = 0;
  int id;
  boolean bounce = false;  // bounce event
  
  Ball[] others;
 
  Ball(float xin, float yin, float din, int idin, Ball[] oin) {
    x = xin;
    y = yin;
    diameter = din;
    id = idin;
    others = oin;
  } 
  
  void collide() {
    for (int i = id + 1; i < numBalls; i++) {
      float dx = others[i].x - x;
      float dy = others[i].y - y;
      float distance = sqrt(dx*dx + dy*dy);
      float minDist = others[i].diameter/2 + diameter/2;
      if (distance < minDist) { 
        //println("COLLISION");
        float angle = atan2(dy, dx);
        float targetX = x + cos(angle) * minDist;
        float targetY = y + sin(angle) * minDist;
        float ax = (targetX - others[i].x) * spring;
        float ay = (targetY - others[i].y) * spring;
        vx -= ax;
        vy -= ay;
        others[i].vx += ax;
        others[i].vy += ay;
      }
    }   
  }
  
  void move() {
    bounce = false;
    vy += gravity;
    x += vx;
    y += vy;
    if (x + diameter/2 > width) {
      x = width - diameter/2;
      vx *= friction; 
    }
    else if (x - diameter/2 < 0) {
      x = diameter/2;
      vx *= friction;
    }
    if (y + diameter/2 > height) {
      y = height - diameter/2;
      vy *= friction; 
      // ground bounce event
      if (abs(vy) > 0.1) {bounce = true;} 
    } 
    else if (y - diameter/2 < 0) {
      
      y = diameter/2;
      vy *= friction;
    }
  } 
  
  void display() {
    fill(255, 204);
    ellipse(x, y, diameter, diameter);
  }
  
  void sound(int i){
    if (this.bounce){
       // println("bounce " + i + " :" + vy);
       OscMessage myMessage = new OscMessage("/b");
       myMessage.add(i);     // ball index
       myMessage.add(vy);    // ball speed
       myMessage.add( (this.x * 2 / width ) - 1 );     // ball panning 
      /* send the message */
      oscP5.send(myMessage, myRemoteLocation); 
    }
  
  }
}
