/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelers;

import com.cycling74.max.*;
import java.util.ArrayList;

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

    public Wheelers() {

        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        createInfoOutlet(false);

        qelem = new MaxQelem(new Callback(this, "draw"));

        
        
        
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

    public double getDt() {
        return dt;
    }

    private void update() {
        // do high priority stuff here, time sensitive...
        currTime = System.currentTimeMillis();
        dt = (currTime - lastTime);
        lastTime = currTime;
        //outletHigh(1, "dt " + dt);

        aWheel.update();
    }



    private void draw() {
        aWheel.draw();

    }

    public void notifyDeleted() {
        qelem.release();
        //release the native resources associated
        //with the MaxQuelem object by the Max
        //application. This is very important!!
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
