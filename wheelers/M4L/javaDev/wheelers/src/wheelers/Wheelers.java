/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelers;

import com.cycling74.max.*;
import java.util.ArrayList;
import com.cycling74.jitter.*;

/**
 *
 * @author bloit
 */
public class Wheelers extends MaxObject {

    private MaxQelem qelem;
    private Wheel aWheel;
    public float tempoFactor;
    private double dt;
    private double currTime;
    private double lastTime;
    public final JitterObject render;
    public JitterObject sketch;

    public Wheelers(Atom[] args) {

        if (args.length < 1) {
            bail("Wheelers : must supply the name of a pwindow as an argument.");
            render = null;
            return;
        }


        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        createInfoOutlet(false);
        

        qelem = new MaxQelem(new Callback(this, "draw"));

        // create our render object for our context
        render = new JitterObject("jit.gl.render", new Atom[]{args[0]});
        render.send("erase_color", new float[]{0.f, 0.f, 0.f, 1.f});
        sketch = new JitterObject("jit.gl.sketch",new Atom[]{args[0]});
        currTime = System.currentTimeMillis();
        lastTime = currTime;
        dt = 0;
        tempoFactor = 4f;
        aWheel = new Wheel(this);
    }

    public void bang() {

        // goal : keep update() in high-priority thread, and draw() in low.
        // given that bang is called by HI (via metro for ex).

        update();
        qelem.set();
        /*schedule the function draw to
         be executed by the low priority thread.
         if it is already set do not schedule it 
         again. The draw function was specified 
         as the qelem function when we created the 
         MaxQelem qelem in the construtctor
         */
    }
    
    public void setTempoFactor(int tf){
        tempoFactor = tf;
        aWheel.updateTempoFactor();
    }

    public double getDt() {
        return dt;
    }

    private void update() {
        // do high priority stuff here, time sensitive...
        currTime = System.currentTimeMillis();
        dt = (currTime - lastTime);
        lastTime = currTime;
        aWheel.update();
    }



    private void draw() {
        sketch.send("reset");
        
        aWheel.draw();
        
        render.send("erase");
        render.send("drawclients");
        render.send("swap");
        

    }

    public void notifyDeleted() {
        qelem.release();
        //release the native resources associated
        //with the MaxQuelem object by the Max
        //application. This is very important!!
        
        sketch.freePeer();
        render.freePeer();
        
    }

    // --------------------- Controls ---------------------
    public void fill(int f) {
        aWheel.fill = f;
    }

    public void newWheel() {
    }

    public void newNode() {
    }

    public void editNode() {
    }

    public void setTempoFactor(float f) {
    }

    public void setPalette(int i) {
    }
}
