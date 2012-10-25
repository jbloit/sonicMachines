/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soma;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author bloit
 */
public class Particle {
    
    private Shake maxobj;
    private Shaker parent;
    private int index;
    private float rv;                   // rotation velocity
    public float x, y;
    private float vx, vy;               // The x- and y-axis velocities
    private float gravity;
    private float mass;
    private float stiffness;
    private float damping;

    private float tickWindow;           // tick detection
    private boolean isTicking;
    private boolean wasTicking;
    public Color particleColor;
    private int[] seedPalette;
    public boolean inEditMode = false;
    
    private int tatum;
    private int triggerPeriod;          // how many tatum ticks between notes
    private float stretch;              // distance to shell center 
    
    public int pitch;                   // midi pitch
    
    
    private Random rand;
    
    public Particle(Shake _maxobj, Shaker _parent, int _index) {
        
        maxobj = _maxobj;
        parent = _parent;
        rand = new Random();
        index = _index;

        particleColor = new Color(255, 255, 255, 100); // default
        
        mass = rand.nextFloat();
        stiffness = 1f;
        damping = 0.1f;
        gravity = 0.f;
        
        tatum = -1;
        triggerPeriod = 1;             
        stretch = 0;
        pitch = 0; // relative to the pitch of parent shell
        
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
        //float playbackRate = (particleColor.getRed() + particleColor.getGreen() + particleColor.getBlue()) * 2 / 765;
        maxobj.outletHigh(1, "tick " + " " + (parent.pitch + this.pitch) + " " + (stretch*mass));
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
        stretch = stretch * 8 / maxobj.width;       // scale to fit mapping function below
        triggerPeriod = (int) Math.abs (Math.exp(-stretch + 4) + 1);
        //triggerPeriod = (int) (-4*stretch)+20;

        // high clip (watch mapping curve before making changes to this thresh)
        if (triggerPeriod > 50) triggerPeriod = -1; // flag value
        
        // to comment out after tuning :
        //maxobj.outletHigh(1, "triggerPeriod " + index + " " + triggerPeriod);

    }
    
    // with a new tatum event (smallest beat grid), decide whether to trigger a note or not.
    public void newTatum(){
        tatum++;
        if (triggerPeriod > 0) {
            if ((tatum % triggerPeriod) == 0) {
                tick();
                tatum = 0;
            }
        }
        //maxobj.post("strecth " + stretch);
    }
    
    void updateTempoFactor(){
  
    }
    

    
    
    

}
