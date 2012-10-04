/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zgMaracas;

import com.cycling74.max.*;
import java.util.ArrayList;
import com.cycling74.jitter.*;
import java.awt.Color;

/**
 *
 * @author bloit
 */
public class Maracas extends MaxObject {

    private MaxQelem qelem;
    private Shell aShell;
    public float tempoFactor;
    private double dt;
    private double currTime;
    private double lastTime;
    public final JitterObject render;
    public JitterObject sketch;
    public int width;
    public int height;
    private String windowName;
    
    public Shell[] shells;
    
    public Color[] palette;

    public Maracas(Atom[] args) {

        if (args.length < 1) {
            bail("Wheelers : must supply the name of a pwindow as an argument.");
            render = null;
            return;
        }
        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        createInfoOutlet(false);
        

        windowName =  args[0].getString();
        qelem = new MaxQelem(new Callback(this, "draw"));
        
        // create our render object for our context
        render = new JitterObject("jit.gl.render", new Atom[]{args[0]});
        sketch = new JitterObject("jit.gl.sketch",new Atom[]{args[0]});
        
        currTime = System.currentTimeMillis();
        lastTime = currTime;
        dt = 0;

        
        
        width = 50;
        height = 50;
        
//        aShell = new Shell(this);
        
        palette = new Color[4];
        palette[0] = new Color (255, 116, 00, 200);
        palette[1] = new Color (191, 48, 48, 200);
        palette[2] = new Color (0, 153, 153, 200);
        palette[3] = new Color (0, 204, 0, 200);
        
        shells = new Shell[4];
        shells[0] = new Shell(this, palette[0]);
        shells[1] = new Shell(this, palette[1]);
        shells[2] = new Shell(this, palette[2]);
        shells[3] = new Shell(this, palette[3]);
        

        
    }
    
    public void init(){
        updateSize();
        
        post("new new init");
        render.send("erase_color", new float[]{0.f, 0.f, 0.f, 1.f});
        render.setAttr("ortho", 1);
        sketch.send("glmatrixmode","projection");
        sketch.send("glloadidentity");
        sketch.send("glortho",new int[]{0, width, height, 0, 0, 1});
        sketch.send("glmatrixmode","modelview");
        sketch.send("gldisable","depth_test");
        sketch.send("glloadidentity");
        
        // allow transparency
        sketch.send("glenable","blend");
        sketch.send("glblendfunc",new String[]{"src_alpha", "one_minus_src_alpha"});

        
    }
    
    private void updateSize(){
        // determine size of jit.pwindow
        MaxBox myWin;
        myWin = (MaxBox) this.getParentPatcher().getNamedBox(windowName);
        int[] rect;
        rect = myWin.getRect();
        width = (int) (rect[2] - rect[0]);
        height = (int) (rect[3] - rect[1]);   
    }

    public void setMass(int shellIndex, float m){
            shells[shellIndex].setMass(m);
    }
    
    public void setPitch(int shellIndex, int n){
            shells[shellIndex].setPitch(n);
    }
    
//    public void setStiffness(float s){
//        aShell.aSeed.setStiffness(s);
//    }    
//    public void setDamping(float d){
//        aShell.aSeed.setDamping(d);
//    }
    
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
    
    public void tatum(){
        
        for (int i = 0; i<4; i++){
            shells[i].newTatum();
        }
    }
    

    public double getDt() {
        return dt;
    }

    private void update() {
        // do high priority stuff here, time sensitive...
        currTime = System.currentTimeMillis();
        dt = (currTime - lastTime);
        lastTime = currTime;
        
        for (int i = 0; i<4; i++){
            shells[i].update();
        }
    }

    private void draw() {
        sketch.send("reset");
        for (int i = 0; i<4; i++){
            shells[i].draw();
        }
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

    public void onDrag(int shellIndex, float mouseX, float mouseY){   
        float glX = (mouseX / width) * 1.62f  - 0.82f;
        float glY = -((mouseY / height) * 1.7f - 0.85f) ;
        //post("mouse gl " + glX + " " + glY);
        shells[shellIndex].setCoord(glX, glY);
    }
    
    public void camera(float x, float y, float z){
        render.send("camera",new float[]{x,y,z});
    }
    
    public void printCoords(){
        post("wheel " + aShell.x + " " + aShell.y);
        post("note " + aShell.aSeed.x + " " + aShell.aSeed.y);
    }
    


    /* ----------------
     * OPENGL STUFF
     * ----------------
     */

}
