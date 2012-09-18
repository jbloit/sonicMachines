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
    private float a;                    // angle
    private float orbitRadius;          // radius
    private float nodeRadius;
    private float rv;                   // rotation velocity
    private float x, y;
    private float vx, vy;               // The x- and y-axis velocities
    private float gravity;
    private float mass;
    private float stiffness;
    private float damping;
    
    int nbTicks;
    private float tickAngle;            // delta angle 
    private float tickWindow;           // tick detection
    private boolean isTicking;
    private boolean wasTicking;
    private Color nodeColor;
    private int[] nodePalette;
    private int tone;
    public boolean inEditMode = false;
    
    private int tatum;
    private int triggerPeriod;          // how many tatum ticks between notes
    private float stretch;
    
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
        
        mass = 1f;
        stiffness = 1f;
        damping = 0.1f;
        gravity = 0.f;
        
        tatum = -1;
        triggerPeriod = 7;             // sylvain style
        stretch = 0;
        

    }
    
    public void setMass(float _mass){
        mass = _mass;
    }
    public void setStiffness(float _stiff) {
        stiffness = _stiff;
    }
    public void setDamping(float _damping) {
        damping = _damping;
    }
    
    public   void tick(){
        // get audio repitching factor from color value
        float playbackRate = (nodeColor.getRed() + nodeColor.getGreen() + nodeColor.getBlue()) * 2 / 765;
        maxobj.outletHigh(1, "tick " + (a % (2 * Math.PI)) + " " + playbackRate);
  }
  
  
    void update(float targetX, float targetY) {
        // do high priority stuff here, time sensitive...
        
        float xdist = targetX - x;
        float ydist = targetY - y;
        // spring motion
        float forceX = xdist * stiffness;
        float ax = forceX / mass;
        vx = (1 - damping )* (vx + ax);
        x += vx * (maxobj.getDt() / 1000) ;
        float forceY = ydist * stiffness;
        forceY += gravity;
        float ay = forceY / mass;
        vy = (1 - damping) * (vy + ay);
        y += vy * (maxobj.getDt() / 1000);
        
        // distance between node and target node (or wheel center)
        stretch = (float) Math.sqrt(xdist*xdist + ydist*ydist);
        triggerPeriod = (int) Math.abs (Math.exp(-stretch + 4) + 1);
        
        
        maxobj.outlet(0, "triggerPeriod " + triggerPeriod);

        //maxobj.post("x " + x + " y " + y + " dt " + maxobj.getDt() );
    }
    
    // with a new tatum event (smallest beat grid), decide whether to trigger a note, or not.
    public void newTatum(){
        tatum++;
        if (stretch > 0.01f) {
            if ((tatum % triggerPeriod) == 0) {
                tick();
                tatum = 0;
            }
        }
        //maxobj.post("strecth " + stretch);
    }
    
    void updateTempoFactor(){
        nbTicks = (int) Math.ceil(orbitRadius * maxobj.tempoFactor);
        tickAngle =  2 * (float)Math.PI / nbTicks;   
    }
    
    void draw(){

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
