import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class GraphingData extends JPanel {
    public int[] data1;
    //public int[] data2={4,47,102,1090};
    public int arraySize = 4;
    public final int PAD = 20;
    
    public GraphingData(int[] iData){
    	data1=iData;
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        double xInc = (double)(w - 2*PAD)/(arraySize);
        double scale1 = (double)(h - 2*PAD)/getMax(data1);
        //double scale2 = (double)(h - 2*PAD)/getMax(data2);
        
        // Mark data points.-- Data1
        g2.setPaint(Color.red);
        for(int i = 0; i < arraySize; i++) {
        	
        	
        	double x = PAD + (i+1)*xInc;
            
            double y = h - PAD - scale1*data1[i];
            g2.fill(new Ellipse2D.Double(x-5, y-5, 10, 10));
        }
        
        /**
        
     // Mark data points.-- Data2
        g2.setPaint(Color.blue);
        for(int i = 0; i < arraySize; i++) {
            double x = PAD + (i+1)*xInc;
            double y = h - PAD - scale2*data2[i];
            g2.fill(new Ellipse2D.Double(x-5, y-5, 10, 10));
        }
        
        **/
        
        
        
        
    }
 
    
    private int getMax(int[] data) {
        int max = -Integer.MAX_VALUE;
        for(int i = 0; i < arraySize; i++) {
            if(data[i] > max)
                max = data[i];
        }
        return max;
    }
    
 
    public static void main(String[] args) {
    	/*
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GraphingData());
        f.setSize(600,600);
        f.setLocation(200,200);
        f.setVisible(true);
        */
    }
}