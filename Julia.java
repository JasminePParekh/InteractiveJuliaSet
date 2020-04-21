import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;
 
public class Julia extends JPanel implements AdjustmentListener,ActionListener{
    double zoom = 1;
    double cY, cX;
    JScrollBar [] scroll;
    JLabel [] label;
    JFrame f;
    JButton reset,random;
    double [] parameters = new double [6];
    DecimalFormat decForm=new DecimalFormat("0.00");
 
    public Julia() {
        parameters[0] = 0.0;
        parameters[1] = 0.0;
        parameters[2] = 0.5;
        parameters[3] = 0.5;
        parameters[4] = 100;
        parameters[5] = 0.5;
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Jasmine Set");
        f.setSize(1000,800);
        f.add(this);
        scroll = new JScrollBar[6];
        scroll[0] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
        scroll[1] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
        scroll[2] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
        scroll[3] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000); 
        scroll[4] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
        scroll[5] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
        for(int i=0;i<6;i++){
            scroll[i].addAdjustmentListener(this);
        }
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(6,1));
        for(int x=0; x<6;x++){
            scrollPanel.add(scroll[x]);
        }

        label = new JLabel[6];

        label[0] = new JLabel("A: " + decForm.format(parameters[0]));
        label[1] = new JLabel("B: " + decForm.format(parameters[1]));
        label[2] = new JLabel("Saturation: " + decForm.format(parameters[2]));
        label[3] = new JLabel("Brightness: " + decForm.format(parameters[3]));
        label[4] = new JLabel("MaxIterator: " + decForm.format(parameters[4]));
        label[5] = new JLabel("Hue: " + decForm.format(parameters[5]));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(6,1));
        for(int x=0; x<6;x++){
            labelPanel.add(label[x]);
        }
        labelPanel.setPreferredSize(new Dimension(150,100));
        reset = new JButton("RESET!");
        random = new JButton("RANDOM");
        reset.addActionListener(this);
        random.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.add(reset);
        buttonPanel.add(random);





        JPanel onePanelToRuleThemAll = new JPanel();
        onePanelToRuleThemAll.setLayout(new BorderLayout());
        onePanelToRuleThemAll.add(scrollPanel,BorderLayout.CENTER);
        onePanelToRuleThemAll.add(labelPanel,BorderLayout.WEST);
        onePanelToRuleThemAll.add(buttonPanel,BorderLayout.EAST);
        f.add(onePanelToRuleThemAll,BorderLayout.SOUTH);
        f.setVisible(true);
    }
 
    public void drawJuliaSet(Graphics2D g) {
        int w = getWidth();
        int h = getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
 
        int maxIter = (int)parameters[4];
        System.out.println(maxIter);  
        cX = parameters[0];
        cY = parameters[1];
        double moveX = 0;
        double moveY = 0;
        double zx, zy;
 
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                zx = 1.5 * (x - w / 2) / (0.5 * zoom * w) + moveX;
                zy = (y - h / 2) / (0.5 * zoom * h) + moveY;
                float i = maxIter;
                while (zx * zx + zy * zy < 6 && i > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    i--;
                }
                int c;
                float brightness = (float) parameters[3];
                float saturation = (float) parameters[2];
                float hue = (float) parameters[5];
                if(i>0)
                    c = Color.HSBtoRGB(((maxIter / i) + hue) % 1, saturation, brightness);
                else 
                    c = Color.HSBtoRGB(maxIter / i, 1, 0);

                image.setRGB(x,y,c);
            }
        }
        g.drawImage(image, 0, 0, null);
    }
    public void adjustmentValueChanged(AdjustmentEvent e){

            if(e.getSource()==scroll[0]){
                parameters[0] = scroll[0].getValue()/800.0;
                label[0].setText("A: " + decForm.format(parameters[0]));
            }
            if(e.getSource()==scroll[1]){
                parameters[1] = scroll[1].getValue()/800.0;
                label[1].setText("B: " + decForm.format(parameters[1]));
            }
            if(e.getSource()==scroll[2]){
                double temp = scroll[2].getValue()/1000.0;
                parameters[2] = (0.5*temp)+0.5; 
                label[2].setText("Saturation: " + decForm.format(parameters[2]));  
            }
            if(e.getSource()==scroll[3]){
                double temp = scroll[3].getValue()/1000.0;
                parameters[3] = (0.5*temp)+0.5;
                label[3].setText("Brightness: " + decForm.format(parameters[3]));
            }
            if(e.getSource()==scroll[4]){
                parameters[4] = (scroll[4].getValue() * 0.1) + 100;
                label[4].setText("MaxIterator: " + decForm.format(parameters[4]));
            }
            if(e.getSource()==scroll[5]){
                double temp = scroll[5].getValue()/1000.0;
                parameters[5] = (0.5*temp)+0.5;  
                label[5].setText("Hue: " + decForm.format(parameters[5])); 
            }

        repaint();

    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==reset){
            parameters[0] = 0.0;
            parameters[1] = 0.0;
            parameters[2] = 0.5;
            parameters[3] = 0.5;
            parameters[4] = 100;
            parameters[5] = 0.5;

            scroll[0].setValue(0);
            scroll[1].setValue(0);
            scroll[2].setValue(0);
            scroll[3].setValue(0);
            scroll[4].setValue(0);
            scroll[5].setValue(0);
        }
        if(e.getSource() == random){
            parameters[0] = (Math.random()*2.5)-1.25;
            scroll[0].setValue((int)(parameters[0]*800.0));
            parameters[1] = (Math.random()*2.5)-1.25;
            scroll[1].setValue((int)(parameters[1]*800.0));
            parameters[2] = 1.0;
            scroll[2].setValue(1000);
            parameters[3] = 1.0;
            scroll[3].setValue(1000);
            parameters[4] = (Math.random()*200);
            scroll[4].setValue((int)((parameters[4]-100)/(0.1)));
            parameters[5] = (Math.random());
            scroll[5].setValue((int)((parameters[5]-0.5)/(.0005)));

            label[0].setText("A: " + decForm.format(parameters[0]));
            label[1].setText("B: " + decForm.format(parameters[1]));
            label[2].setText("Saturation: " + decForm.format(parameters[2])); 
            label[3].setText("Brightness: " + decForm.format(parameters[3]));
            label[4].setText("MaxIterator: " + decForm.format(parameters[4]));
            label[5].setText("Hue: " + decForm.format(parameters[5])); 

        }

        repaint();
    }
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        drawJuliaSet(g);
    }
 
    public static void main(String[] args) {
        Julia app = new Julia();
    }
}