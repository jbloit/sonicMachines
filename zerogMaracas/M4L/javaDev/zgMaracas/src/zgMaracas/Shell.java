/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zgMaracas;

import java.util.ArrayList;
import java.awt.Color;

/**
 *
 * @author bloit
 */
public class Shell {

    private Maracas maxobj;
    public int fill;
    public ArrayList seeds;
    public Seed aSeed;
    public float x,y;
    public int nbSeeds;
    public Color[] palette;
    
    
    public Shell(Maracas _parent) {
        x = 0f;
        y = 0f;
        maxobj = _parent;
        fill = 0;
        nbSeeds = 4;
        
        palette = new Color[4];
        palette[0] = new Color (255, 116, 00, 200);
        palette[1] = new Color (191, 48, 48, 200);
        palette[2] = new Color (0, 153, 153, 200);
        palette[3] = new Color (0, 204, 0, 200);
        
        seeds = new ArrayList();
        for (int i = 0; i < nbSeeds; i++){
            
            Seed s = new Seed(maxobj, i);
            s.seedColor = palette[i];
            seeds.add(s);
            
            
        }  
    }
    
    public void setCoord(float _x, float _y){
         x = _x;
         y = _y;
    }

    public void newTatum() {
        Seed n;
        for (int i = 0; i < nbSeeds; i++) {
            n = (Seed) seeds.get(i);
            n.newTatum();
        }
    }

    
    public void update(){
        // in the High priority thread
      
        Seed n;
        for (int i = 0; i < nbSeeds; i++){
            n = (Seed) seeds.get(i);
            n.update(x, y);
        }
        
    }
    
    public void draw() {
        //maxobj.sketch.send("glpushmatrix");
        //maxobj.sketch.send("gltranslate", new float[] {x, y, 0f});
        Seed n;
        for (int i = 0; i < nbSeeds; i++) {
            n = (Seed) seeds.get(i);
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
