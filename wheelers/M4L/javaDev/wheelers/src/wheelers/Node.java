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

    private int index;
    private float rv;                   // rotation velocity
    public float x, y;
    private float vx, vy;               // The x- and y-axis velocities
    private float gravity;
    private float mass;
    private float stiffness;
    private float damping;

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
    
    public Node(Wheelers _maxobj, int _index) {
        
        maxobj = _maxobj;
        rand = new Random();
       

        tone = (int) Math.floor(rand.nextFloat() * 4.f);
        //nodePalette = (int[]) palettes.get(curPaletteIndex);
        //nodeColor = nodePalette[tone];
        nodeColor = new Color(0.6f, 0.2f, 0.1f, 0.7f);
        
        mass = rand.nextFloat();
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
        maxobj.outletHigh(1, "tick " + " " + index + " " + playbackRate);
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
        
//        x = targetX;
//        y = targetY;
        
        // distance between node and target node (or wheel center)
        stretch = (float) Math.sqrt(xdist*xdist + ydist*ydist);
        triggerPeriod = (int) Math.abs (Math.exp(-stretch + 4) + 1);
        //triggerPeriod = (int) (-4*stretch)+20;
        
        
        maxobj.outlet(0, "triggerPeriod " + triggerPeriod);

        //maxobj.post("x " + x + " y " + y + " dt " + maxobj.getDt() );
    }
    
    // with a new tatum event (smallest beat grid), decide whether to trigger a note, or not.
    public void newTatum(){
        tatum++;
        if (stretch > 0.1f) {
            if ((tatum % triggerPeriod) == 0) {
                tick();
                tatum = 0;
            }
        }
        //maxobj.post("strecth " + stretch);
    }
    
    void updateTempoFactor(){
  
    }
    
    void draw(){

        // draw node
        
        drawCircle(x, y, mass/5f, 20, true);
    }
    
    private void drawCircle(float cx, float cy, float r, int num_segments, boolean fill) {
        float theta = 2 * 3.1415926f / (float) num_segments;
        float cos = (float) Math.cos(theta);//precalculate the sine and cosine
        float sin = (float) Math.sin(theta);
        float t;
        float anx = r;//we start at angle = 0 
        float any = 0;
        maxobj.sketch.send("glcolor" , new float[] {(float)nodeColor.getRed()/255, nodeColor.getGreen()/255, nodeColor.getBlue()/255, nodeColor.getAlpha()/255});
        
        if (fill) {
            maxobj.sketch.send("glbegin", "polygon");
        }
        else {
            maxobj.sketch.send("glbegin", "line_loop");
        }
        for (int ii = 0; ii < num_segments; ii++) {
            maxobj.sketch.send("glvertex",  new float[] {(anx + cx), (any + cy)});//output vertex 
            //apply the rotation matrix
            t = anx;
            anx = cos * anx - sin * any;
            any = sin * t + cos * any;
        }
        maxobj.sketch.send("glend");
    }
}
