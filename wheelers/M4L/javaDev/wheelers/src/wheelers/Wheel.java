/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelers;

/**
 *
 * @author bloit
 */
public class Wheel {

    private Wheelers maxobj;
    public int fill;
    private Node aNode;
    
    public Wheel(Wheelers _parent) {
        maxobj = _parent;
        fill = 0;
        aNode = new Node(maxobj, 0.1f, 3 );
        
    }

    public void update(){
        // in the High priority thread
        aNode.update();
    }
    
    public void draw() {
        
        aNode.draw();
        
        
//        float r = 0.5f;
//        //int numSegments = getNumCircleSegments(r);
//        int numSegments = 50;
//        Wheelers.post("radius " + r);
//        Wheelers.post("num segments " + numSegments);
//
//        drawCircle(0,0,r,numSegments);
        //testTriangle();
    }
    
    
    // from http://slabode.exofire.net/circle_draw.shtml
    private void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = 2 * 3.1415926f / (float) num_segments;
        float c = (float) Math.cos(theta);//precalculate the sine and cosine
        float s = (float) Math.sin(theta);
        float t;
        float x = r;//we start at angle = 0 
        float y = 0;
        
        maxobj.outlet(0, "reset");
        maxobj.outlet(0, "glcolor 0 1 0 1");
        if (fill == 0) {
            maxobj.outlet(0, "glbegin line_loop");
        }
        else {
            maxobj.outlet(0, "glbegin polygon");
        }
        for (int ii = 0; ii < num_segments; ii++) {
            maxobj.outlet(0, "glvertex " + (x + cx) + " " + (y + cy));//output vertex 
            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        maxobj.outlet(0, "glend");
    }
    
    
    private void testTriangle() {
        maxobj.outlet(0, "reset");
        maxobj.outlet(0, "glcolor 0 1 0 1");
        maxobj.outlet(0, "glbegin line_loop");
        maxobj.outlet(0, "glvertex -0.5 -0.5");
        maxobj.outlet(0, "glvertex 0 0.5");
        maxobj.outlet(0, "glvertex 0.5 -0.5");
        maxobj.outlet(0, "glend");

    }
    
    private int getNumCircleSegments(float r) {
        return 100000 * (int) Math.sqrt(r);//change the 10 to a smaller/bigger number as needed 
    }
    
}
