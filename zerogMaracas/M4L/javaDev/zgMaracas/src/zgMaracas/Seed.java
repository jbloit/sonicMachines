/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zgMaracas;
import java.awt.Color;
import java.util.Random;
/**
 *
 * @author bloit
 */
public class Seed {

    private Maracas maxobj;

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
    public Color seedColor;
    private int[] seedPalette;
    private int tone;
    public boolean inEditMode = false;
    
    private int tatum;
    private int triggerPeriod;          // how many tatum ticks between notes
    private float stretch;              // distance to shell center 
    
    
    
    private Random rand;
    
    public Seed(Maracas _maxobj, int _index) {
        
        maxobj = _maxobj;
        rand = new Random();
       

        tone = (int) Math.floor(rand.nextFloat() * 4.f);
        //nodePalette = (int[]) palettes.get(curPaletteIndex);
        //nodeColor = seedPalette[tone];
        seedColor = new Color(255, 255, 255, 100); // default
        
        mass = rand.nextFloat();
        stiffness = 1f;
        damping = 0.1f;
        gravity = 0.f;
        
        tatum = -1;
        triggerPeriod = 1;             
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
    
    // send a control event
    public   void tick(){
        // get audio repitching factor from color value
        float playbackRate = (seedColor.getRed() + seedColor.getGreen() + seedColor.getBlue()) * 2 / 765;
        maxobj.outletHigh(1, "tick " + " " + index + " " + playbackRate);
  }
  
  
    // UDATE PHYSICS
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
        
        // distance to shell center
        stretch = (float) Math.sqrt(xdist*xdist + ydist*ydist);
        
        // trigger more for higher strecth value 
        triggerPeriod = (int) Math.abs (Math.exp(-stretch + 4) + 1);
        //triggerPeriod = (int) (-4*stretch)+20;

        maxobj.outlet(0, "triggerPeriod " + triggerPeriod);

    }
    
    // with a new tatum event (smallest beat grid), decide whether to trigger a note or not.
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
        
        drawCircle(x, y, mass/5f, 20, true);
    }
    
    
    
    /* ----------------------------
     *  LOW LEVEL DRAWING 
     * ----------------------------
     */
    
    private void drawCircle(float cx, float cy, float r, int num_segments, boolean fill) {
        float theta = 2 * 3.1415926f / (float) num_segments;
        float cos = (float) Math.cos(theta);//precalculate the sine and cosine
        float sin = (float) Math.sin(theta);
        float t;
        float anx = r;//we start at angle = 0 
        float any = 0;
      //  maxobj.sketch.send("glcolor" , new float[] {(float)seedColor.getRed(), seedColor.getGreen(), seedColor.getBlue(), seedColor.getAlpha()});
        
        maxobj.sketch.send("glcolor" , new float[] {seedColor.getRed()/255f,seedColor.getGreen()/255f,seedColor.getBlue()/255f, seedColor.getAlpha()/255f});
       
        
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
