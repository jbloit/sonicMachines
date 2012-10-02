class Node{

  float a; // angle
  float r; // radius
  float s; //size 
  float rv; // rotation velocity
  float x,y;
  int nbTicks;

  float tickAngle;       // delta angle 
  float tickWindow;      // tick detection
  boolean isTicking;
  boolean wasTicking;
  color nodeColor;
  color[] nodePalette;
  int tone;
  
  boolean inEditMode = false;
 
   Node(int _radius, int _size){
     a = random(0, 2*PI); // initial phase
     a = 0;
     r = _radius;
     s = _size;
     nbTicks = ceil(r * tempoFactor);
    // nbTicks = 12;
     tickAngle = 2*PI / nbTicks; 
     rv = 0.0001;
     tickWindow = 2*PI / 100.0;
     isTicking = false;
     wasTicking = false;
     tone = floor(random(0,4));
     nodePalette = (color[]) palettes.get(curPaletteIndex);
     nodeColor = nodePalette[tone];
     
   } 
   
  void setTempoFactor(float _tempoFactor){
    nbTicks = ceil(r * _tempoFactor);
    // nbTicks = 12;
     tickAngle = 2*PI / nbTicks; 
  }
  
  void setRadius(float _radius){
    println("calling set radius");
    r = _radius;
    nbTicks = ceil(r * tempoFactor);
    tickAngle = 2*PI / nbTicks; 
  }

  void setNodePalette(int _paletteIndex){
    nodePalette = (color[]) palettes.get(_paletteIndex);
    nodeColor = nodePalette[tone];
    println("set node color to " + nodeColor);
  }
    
  void tick(){
    //println("tick");
    OscMessage aMessage = new OscMessage("/nodetick");
    aMessage.add(a % (2*PI));

    // get audio repitching factor from color value
    float playbackRate = (red(nodeColor) + green(nodeColor) + blue(nodeColor)) * 2 / 765; 
    aMessage.add(playbackRate);
    
    osc_s.send(aMessage, destAddress);
  }
  
  
  void update(){
    
    a +=  rv * dt;
    x = r * cos(a);
    y = r * sin(a);
    
    if ((!wasTicking) && (isTicking))
      this.tick();  
  }
  
  
  // try to avoid too straight line styles
   void draw(){
    
    // draw orbit with ticks
     noFill();
     
     if (inEditMode)
     strokeWeight(4);
     else
     strokeWeight(1);
     
     stroke(nodeColor, 200);
     ellipse(0, 0, 2*r, 2*r);
     
     fill(255, 100);
     noStroke();
     float curTickAngle = 0;
     
     wasTicking = isTicking;
     
     isTicking = false;
     for (int i = 0; i < nbTicks; i++){
       ellipse(r * cos(curTickAngle), r * sin(curTickAngle), 5, 5);
       if ( (a % (2*PI)  <  (curTickAngle + tickWindow)) && ( a % (2*PI) >  curTickAngle )) {
         isTicking = true;
       }
       curTickAngle += tickAngle;
     }
    
   
    // draw node
    if (isTicking){
      fill(nodeColor);
      stroke(nodeColor, 255); 
    }
    else {
      noFill();
      stroke(nodeColor, 100);
    }
    ellipse(x, y, 10, 10);
   
   }
}
