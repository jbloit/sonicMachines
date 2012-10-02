/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelers;

import java.util.ArrayList;

/**
 *
 * @author bloit
 */
public class Wheel {

    private Wheelers maxobj;
    public int fill;
    public ArrayList nodes;
    public Node aNode;
    public float x,y;
    public int nbNodes;
    
    public Wheel(Wheelers _parent) {
        x = 0f;
        y = 0f;
        maxobj = _parent;
        fill = 0;
        nbNodes = 4;
        nodes = new ArrayList();
        for (int i = 0; i < nbNodes; i++){
            nodes.add(new Node(maxobj, i));
        }
        //aNode = new Node(maxobj, 0);
        
        
    }
    
    public void setCoord(float _x, float _y){
         x = _x;
         y = _y;
    }

    public void newTatum() {
        Node n;
        for (int i = 0; i < nbNodes; i++) {
            n = (Node) nodes.get(i);
            n.newTatum();
        }
    }

    
    public void update(){
        // in the High priority thread
      
        Node n;
        for (int i = 0; i < nbNodes; i++){
            n = (Node) nodes.get(i);
            n.update(x, y);
        }
        
    }
    
    public void draw() {
        //maxobj.sketch.send("glpushmatrix");
        //maxobj.sketch.send("gltranslate", new float[] {x, y, 0f});
        Node n;
        for (int i = 0; i < nbNodes; i++) {
            n = (Node) nodes.get(i);
            n.draw();
        }
        maxobj.sketch.send("glcolor" , new float[] {1, 1, 1, 1});
        drawCircle(x,y,0.1f, 20, true);
        //maxobj.sketch.send("glpopmatrix");
    }
    
        private void drawCircle(float cx, float cy, float r, int num_segments, boolean fill) {
        float theta = 2 * 3.1415926f / (float) num_segments;
        float cos = (float) Math.cos(theta);//precalculate the sine and cosine
        float sin = (float) Math.sin(theta);
        float t;
        float x = r;//we start at angle = 0 
        float y = 0;
        
        
        if (fill)
            maxobj.sketch.send("glbegin", "polygon");
        else
            maxobj.sketch.send("glbegin", "line_loop");
        for (int ii = 0; ii < num_segments; ii++) {
            maxobj.sketch.send("glvertex",  new float[] {(x + cx), (y + cy)});//output vertex 
            //apply the rotation matrix
            t = x;
            x = cos * x - sin * y;
            y = sin * t + cos * y;
        }
        maxobj.sketch.send("glend");
    }
    
    
}
