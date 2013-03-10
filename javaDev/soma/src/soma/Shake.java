/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soma;

import com.cycling74.max.*;
import java.awt.Color;

/**
 *
 * @author bloit
 */
public class Shake extends MaxObject {

    private MaxQelem qelem;
    private Shaker aShaker;
    public float tempoFactor;
    private double dt;
    private double currTime;
    private double lastTime;
    public int width;
    public int height;
    private String windowName;
    private boolean drawFlag;
    public Shaker[] shakers;
    public Color[] palette;
    public float triggerSensitivity;

    public Shake(Atom[] args) {
        declareInlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        declareOutlets(new int[]{DataTypes.ALL, DataTypes.ALL});
        createInfoOutlet(false);

        windowName = args[0].getString();
        qelem = new MaxQelem(new Callback(this, "draw"));


        currTime = System.currentTimeMillis();
        lastTime = currTime;
        dt = 0;

        drawFlag = true;

        width = 50;
        height = 50;

        triggerSensitivity = 10f;
        
        
//        aShell = new Shell(this);

        palette = new Color[4];
        palette[0] = new Color(255, 116, 0, 200);
        palette[1] = new Color(191, 48, 48, 200);
        palette[2] = new Color(0, 153, 153, 200);
        palette[3] = new Color(0, 204, 0, 200);

        shakers = new Shaker[4];
        shakers[0] = new Shaker(this, palette[0]);
        shakers[1] = new Shaker(this, palette[1]);
        shakers[2] = new Shaker(this, palette[2]);
        shakers[3] = new Shaker(this, palette[3]);

    }

    public void init() {
        updateSize();
    }

    private void updateSize() {
//        // determine size of jit.pwindow
        MaxBox myWin;
        myWin = (MaxBox) this.getParentPatcher().getNamedBox(windowName);
        int[] rect;
        rect = myWin.getRect();
        width = (int) (rect[2] - rect[0]);
        height = (int) (rect[3] - rect[1]);
        post("w " + width + " h " + height);
    }

    public double getDt() {
        return dt;
    }

    private void update() {
        // do high priority stuff here, time sensitive...
        currTime = System.currentTimeMillis();
        dt = (currTime - lastTime);
        lastTime = currTime;

        for (int i = 0; i < 4; i++) {
            shakers[i].update();
        }
    }

    public void setMass(int shakerIndex, float m) {
        shakers[shakerIndex].setMass(m);
    }
    
    public void setStiffness(int shakerIndex, float s) {
        shakers[shakerIndex].setStiffness(s);
    }

    public void setPitch(int shakerIndex, int n) {

        shakers[shakerIndex].setPitch(n);
    }

//    public void setScale(int shellIndex, int interval_0, int interval_1, int interval_2, int interval_3, int interval_4){
    public void setScale(Atom[] args) {

        Atom a;

        Shaker s = shakers[args[0].toInt()];

        for (int i = 1; i < args.length; i++) {
            Particle p = (Particle) s.particles.get(i - 1);
            p.pitch = args[i].toInt();     // relative pitch
        }

    }

    private void draw() {

        for (int i = 0; i < 4; i++) {
            Shaker s = shakers[i];
            StringBuilder toJsui = new StringBuilder(46);
            toJsui.append(i + " " + s.mass + " " + (int) s.x + " " + (int) s.y);
            for (int j = 0; j < s.nbParticles; j++) {
                Particle p = (Particle) s.particles.get(j);
                toJsui.append(" " + (int) p.x + " " + (int) p.y);
            }
            outlet(0, toJsui.toString());
        }
    }

    public void tatum() {
        for (int i = 0; i < 4; i++) {
            shakers[i].newTatum();
        }
    }

    public void notifyDeleted() {
        qelem.release();
        //release the native resources associated
        //with the MaxQuelem object by the Max
        //application. This is very important!!

    }

    public void onDrag(int shellIndex, float mouseX, float mouseY) {
//        float glX = (mouseX / width) * 1.62f  - 0.82f;
//        float glY = -((mouseY / height) * 1.7f - 0.85f) ;
        //post("mouse gl " + glX + " " + glY);

        shakers[shellIndex].setCoord(mouseX, mouseY);
    }

    public void doDraw(int _drawFlag) {
        if (_drawFlag > 0) {
            drawFlag = true;
        } else {
            drawFlag = false;
        }
    }

    public void sensitivity(float ts){
        
        if (ts > 0.1f) this.triggerSensitivity = ts;
        
    }
    
    public void bang() {

        // goal : keep update() in high-priority thread, and draw() in low.
        // given that bang is called by HI (via metro for ex).

        update();
        if (drawFlag) {
            qelem.set();
        }
    }
}
