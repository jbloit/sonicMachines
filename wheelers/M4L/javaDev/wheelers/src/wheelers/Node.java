/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelers;
import java.awt.Color;
import java.util.Random;
/**
 *
 * @author bloit
 */
public class Node {

    private Wheelers maxobj;
    private float a; // angle
    private float orbitRadius; // radius
    private float nodeRadius;
    private float rv; // rotation velocity
    private float x, y;
    int nbTicks;
    private float tickAngle;       // delta angle 
    private float tickWindow;      // tick detection
    private boolean isTicking;
    private boolean wasTicking;
    private Color nodeColor;
    private int[] nodePalette;
    private int tone;
    public boolean inEditMode = false;
  
    private Random rand;
    
    public Node(Wheelers _maxobj, float _radius) {
        
        maxobj = _maxobj;
        rand = new Random();
        
        nodeRadius = 0.1f;
        a =   rand.nextFloat() * 2 * (float)Math.PI; // initial phase
        a = 0;
        orbitRadius = _radius;
        nbTicks = (int) Math.ceil(orbitRadius * maxobj.tempoFactor);
        // nbTicks = 12;
        tickAngle =  2 * (float)Math.PI / nbTicks;
        rv = 0.0005f;
        tickWindow = 2 * (float)Math.PI / 100.0f;
        isTicking = false;
        wasTicking = false;
        tone = (int) Math.floor(rand.nextFloat() * 4.f);
        //nodePalette = (int[]) palettes.get(curPaletteIndex);
        //nodeColor = nodePalette[tone];
        nodeColor = new Color(0.3f, 0.6f, 0.1f, 0.8f);
        maxobj.post("node const, radius = " + orbitRadius );
        maxobj.post("node const, tmpfactor = " + maxobj.tempoFactor );
        maxobj.post("node const, nbTicks = " + nbTicks );
    }
    
    
    public   void tick(){
        // get audio repitching factor from color value
        float playbackRate = (nodeColor.getRed() + nodeColor.getGreen() + nodeColor.getBlue()) * 2 / 765;
        maxobj.outletHigh(1, "tick " + (a % (2 * Math.PI)) + " " + playbackRate);
  }
  
  
    void update() {
        // do high priority stuff here, time sensitive...
        a += rv * maxobj.getDt();
        x = orbitRadius * (float) Math.cos(a);
        y = orbitRadius * (float) Math.sin(a);
        if ((!wasTicking) && (isTicking)) {
            this.tick();
        }
        wasTicking = isTicking;
        isTicking = false;
        float curTickAngle = 0;
        for (int i = 0; i < nbTicks; i++) {
            if ((a % (2 * Math.PI) < (curTickAngle + tickWindow)) && (a % (2 * Math.PI) > curTickAngle)) {
                isTicking = true;
            }
            curTickAngle += tickAngle;
        }
    }
    
    void updateTempoFactor(){
        nbTicks = (int) Math.ceil(orbitRadius * maxobj.tempoFactor);
        tickAngle =  2 * (float)Math.PI / nbTicks;   
    }
    
    void draw(){
        
        // draw orbit
        drawCircle(0, 0, orbitRadius, 40, false);
        
        
        // draw ticks
        float curTickAngle = 0;
        for (int i = 0; i < nbTicks; i++) {
            drawCircle(orbitRadius * (float) Math.cos(curTickAngle), orbitRadius * (float) Math.sin(curTickAngle), nodeRadius*0.5f, 30, true);
            curTickAngle += tickAngle;
        }
        // draw node
        
        drawCircle(x, y, nodeRadius, 20, isTicking);
    }
    
    private void drawCircle(float cx, float cy, float r, int num_segments, boolean fill) {
        float theta = 2 * 3.1415926f / (float) num_segments;
        float cos = (float) Math.cos(theta);//precalculate the sine and cosine
        float sin = (float) Math.sin(theta);
        float t;
        float x = r;//we start at angle = 0 
        float y = 0;
        maxobj.sketch.send("glcolor" , new float[] {0, 1, 0, 1});
        
        if (fill)
            maxobj.sketch.send("glbegin", "polygon");
        else
            maxobj.sketch.send("glbegin", "line_loop");
        for (int ii = 0; ii < num_segments; ii++) {
            maxobj.sketch.send("glvertex",  new float[] {(x + cx), (y + cy), 0});//output vertex 
            //apply the rotation matrix
            t = x;
            x = cos * x - sin * y;
            y = sin * t + cos * y;
        }
        maxobj.sketch.send("glend");
    }
}
