// tweak from Attraction2D from toxiclibs


import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;

import oscP5.*;
import netP5.*;

OscP5 socket;
NetAddress maxClient;
String maxAdress = "127.0.0.1";
int maxPort = 12000;


int NUM_PARTICLES = 750;

VerletPhysics2D physics;
AttractionBehavior mouseAttractor;

Vec2D mousePos;

void setup() {
  size(680, 382,P3D);
  // setup physics with 10% drag
  physics = new VerletPhysics2D();
  physics.setDrag(0.05f);
  physics.setWorldBounds(new Rect(0, 0, width, height));
  // the NEW way to add gravity to the simulation, using behaviors
  physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.15f)));
  
  // start osc
  socket = new OscP5(this, maxPort);
  maxClient = new NetAddress(maxAdress, maxPort);
  
}

void addParticle() {
  VerletParticle2D p = new VerletParticle2D(Vec2D.randomVector().scale(5).addSelf(width / 2, 0));
  physics.addParticle(p);
  // add a negative attraction force field around the new particle
  physics.addBehavior(new AttractionBehavior(p, 20, -1.2f, 0.01f));
}

void draw() {
  background(255,0,0);
  noStroke();
  fill(255);
  if (physics.particles.size() < NUM_PARTICLES) {
    addParticle();
  }
  physics.update();
  for (VerletParticle2D p : physics.particles) {
    ellipse(p.x, p.y, 5, 5);

    
  }
}

float distanceToMouse(float x, float y){
  PVector mouse = new PVector(mouseX, mouseY);
  PVector particle = new PVector(x, y);
  return mouse.dist(particle);

}

void mousePressed() {
  mousePos = new Vec2D(mouseX, mouseY);
  // create a new positive attraction force field around the mouse position (radius=250px)
  mouseAttractor = new AttractionBehavior(mousePos, 250, 0.9f);
  physics.addBehavior(mouseAttractor);

}

void makeNote(VerletParticle2D p){
       OscMessage myMessage = new OscMessage("/b");
       myMessage.add((int) random(1,10));     // ball index
       
       Vec2D v = p.getVelocity();
       PVector vel = new PVector(v.x, v.y);
       
       myMessage.add(vel.mag());    // ball speed
       myMessage.add( (p.x * 2 / width ) - 1 );     // ball panning 
      /* send the message */
      socket.send(myMessage, maxClient); 
}

void mouseDragged() {
  // update mouse attraction focal point
  mousePos.set(mouseX, mouseY);
    for (VerletParticle2D p : physics.particles) {
      if (distanceToMouse(p.x, p.y) < 20 ) {
        ellipse(p.x, p.y, 10, 10);
        makeNote(p);
    }
  }
  
}

void mouseReleased() {
  // remove the mouse attraction when button has been released
  physics.removeBehavior(mouseAttractor);
}

