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
        aNode = new Node(maxobj, 0.5f);
        
    }
    
    public void updateTempoFactor() {
        if (aNode != null) {
            aNode.updateTempoFactor();
        }
    }
    
    public void update(){
        // in the High priority thread
        aNode.update();
    }
    
    public void draw() {
        
        aNode.draw();
        
    }
    
}
