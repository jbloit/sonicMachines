void newWheel(){
  println("called newWheel");
  //newWheel = new Wheel( random(0,width), random(0,height));
  newWheel = new Wheel(width/2,height/2);
  wheels.add(newWheel);
}

void newNode(){
  println("called newNode");
  if (newWheel != null){
    newWheel.addNode();
  }
}

// remove last created node
void killNode(){
  println("remove last node");
  if (newWheel != null) {
    println("newWheel size : " + newWheel.nodes.size());
    if (newWheel.nodes.size() > 0){
      newWheel.nodes.remove(newWheel.nodes.size() - 1) ;
    }else 
      println("newWheel has no nodes to remove");
  }
}

// toggle edit mode for last created node
void editNode(){
  if (newWheel != null) {
    if (newWheel.nodes.size() > 0){
      nodeToEdit = (Node) newWheel.nodes.get(newWheel.nodes.size() - 1);
      if (nodeToEdit.inEditMode) {
         nodeToEdit.inEditMode = false;
        }
        else {
          nodeToEdit.inEditMode = true;
      }
    }   
  }
}

void setTempoFactor(float f){
  println("called setTempo" + f);
  tempoFactor = f;
  if (newWheel != null){
    newWheel.setTempoFactor(f);
  }
}

void setPalette(int i){
  println("called Palette" + i);
  if (newWheel != null){
      newWheel.setPalette(i);
  }
}

//-------------------- GUI controls
void guiSetup(){
  gui = new ControlP5(this);
  Group gui1 = gui.addGroup("new wheel")
                  .setBackgroundColor(color(0, 64))
                  .setBackgroundHeight(50)
                  ;
                  
  gui.addSlider("setTempoFactor")
     .setPosition(10, 20)
     .setSize(100, 20)
     .setRange(0.01, 0.2)
     .setValue(0.01)
     .moveTo(gui1)
     ;
  gui.addSlider("setPalette")
     .setPosition(10, 50)
     .setSize(100, 20)
     .setRange(0, palettes.size()-1)
     .setValue(0)
     .moveTo(gui1)
     ;       
  accordionUI = gui.addAccordion("accordionUI")
                   .setPosition(0, 0)
                   .setWidth(200)
                   .addItem(gui1)
                   ;
}


