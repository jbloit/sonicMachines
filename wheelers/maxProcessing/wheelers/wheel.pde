class Wheel{

  float x,y = 0;
  ArrayList nodes = new ArrayList();
  
  int colorPaletteIndex;
  
  Wheel(float _x, float _y){
    x = _x;
    y = _y;
    Node n = new Node((int) random(20, 150), (int) random(2, 5));
    nodes.add(n);
    colorPaletteIndex = floor(random(0, palettes.size()));
  }
  
  void addNode(){
    Node n = new Node((int) random(20, 150), (int) random(2, 5));
    nodes.add(n);
  }
  
 void setTempoFactor(float _tempoFactor){
    for (int i = 0; i < nodes.size(); i++){
      Node n = (Node) nodes.get(i);
      n.setTempoFactor(_tempoFactor);
    }
  }
  
  void setPalette(int paletteIndex){
    for (int i = 0; i < nodes.size(); i++){
      Node n = (Node) nodes.get(i);
      n.setNodePalette(paletteIndex);
    }
  }
 
  void update(){
    for (int i = 0; i < nodes.size(); i++){
      Node n = (Node) nodes.get(i);
      n.update();
    }
  }

 
  void draw(){
    
    pushMatrix();
    translate(x , y);
    float x1 = 0;
    float y1 = 0;
    for (int i = 0; i < nodes.size(); i++){
      Node n = (Node) nodes.get(i);
      n.draw();
      alpha(256);
      strokeWeight(3);
      //line(x1, y1, n.x, n.y);
      line(0, 0, n.x, n.y);
      x1 = n.x;
      y1 = n.y;
    }
    
    popMatrix();
    
  }

}
