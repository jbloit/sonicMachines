/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soma;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author bloit
 */
public class Shaker {
    
    private Shake maxobj;
    public int fill;
    public ArrayList particles;
 
    public float x,y;
    public int nbParticles;
    public Color color;
    public float mass;
    public float stiffness;
    public int pitch;                // midi pitch
    
    
    public Shaker(Shake _parent, Color _color) {
        x = 0f;
        y = 0f;
        maxobj = _parent;
        fill = 0;
        nbParticles = 5;

        color = _color;
        
        particles = new ArrayList();
        for (int i = 0; i < nbParticles; i++){
            Particle s = new Particle(maxobj, this, i);
            s.particleColor = color;
            particles.add(s);
            
        }  
    }
    
    
    public void setPitch(int _pitch){
        
        this.pitch  = _pitch;
//        Particle s;
//        for (int i=0; i<nbParticles; i++){
//            s = (Particle) particles.get(i);
//            s.pitch = _pitch ; // dim scale
//        }
        
    }
    
    public void setMass(float _mass){
        Particle s;
        this.mass = _mass;
        for(int i = 0; i<nbParticles; i++){
            s = (Particle) particles.get(i);
            s.setMass( _mass * (1f + i * 0.1f) );
        }
    }
    
    public void setStiffness(float _stiffness){
        Particle s;
        this.stiffness = _stiffness;
        for(int i = 0; i<nbParticles; i++){
            s = (Particle) particles.get(i);
            s.setStiffness( _stiffness );
        }
    }
    
    public void setCoord(float _x, float _y){
         x = _x;
         y = _y;
    }

    public void newTatum() {
        Particle n;
        for (int i = 0; i < nbParticles; i++) {
            n = (Particle) particles.get(i);
            n.newTatum();
        }
    }

    
    public void update(){
        // in the High priority thread
      
        Particle n;
        for (int i = 0; i < nbParticles; i++){
            n = (Particle) particles.get(i);
            n.update(x, y);
        }
        
    }

    
    
}
