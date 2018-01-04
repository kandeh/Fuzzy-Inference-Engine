package knd.fuzzyInfEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.swing.JFrame;

public class LinguisticVariable extends JFrame {
    
    private String name = null;
    private double range_a = 0;
    private double range_b = 100;
    private ArrayList<LinguisticValue> linguisticValues = new ArrayList<LinguisticValue>();
    private Engine engine;
    private double crispValue;
    
    private final int w = 500;
    private final int h = 150;
    private final int x0 = 48;
    private final int y0 = h + x0 * 1;
    
    private boolean isOutputVariable = false;
    
    public final static Color colors[] = {
        Color.RED,
        Color.orange,
        Color.yellow,
        Color.GREEN,
        Color.blue,
        Color.pink,
        Color.MAGENTA
    };
    
    public Engine getEngine() {
        return engine;
    }
    
    public boolean isOutputVariable(){
        return isOutputVariable;
    }
    
    protected double getValue() {
        synchronized(engine.__lock) {
            return crispValue;
        }
    }
    
    public LinguisticVariable(Engine engine, boolean isOutputVariable, String name, double range_a, double range_b, LinguisticValueDescription lvd) {
        this.engine = engine;
        this.isOutputVariable = isOutputVariable;
        this.name = name;
        this.range_a = range_a;
        this.range_b = range_b;
        for(Pair<String, MembershipFunction> p : lvd.nameFunctions) {
            linguisticValues.add(new LinguisticValue(p.getKey(), p.getValue()));
        }
        this.setTitle(name + ": " + crispValue);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        //this.setAlwaysOnTop(true);
        this.setSize(w + x0 * 2, y0 + x0 - 10);
        this.setVisible(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                if(x < x0) {
                    x = x0;
                } else if(e.getX() > x0 + w) {
                    x = x0 + w;
                }
                try {
                    engine.setValue(name, range_a + (range_b - range_a) * ((double) (x - x0) / w));
                    engine.process();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    public String getVariableName() {
        return name;
    }
    
    protected void setValue(double crispValue) {
        synchronized(engine.__lock) {
            this.crispValue = crispValue;
        }
    }
    
    protected double getMembership(String valueName) throws Exception {
        synchronized(engine.__lock) {
            for(LinguisticValue lv : linguisticValues) {
                if(lv.getName().equals(valueName)) {
                    return lv.getMembershipFunction().calc(crispValue);
                }
            }
            throw new Exception("The linguistic value " + valueName + " is not defined.");
        }
    }
    
    protected void reset() {
        synchronized(engine.__lock) {
            for(LinguisticValue lv : linguisticValues) {
                lv.reset();
            }
        }
    }
    
    protected void aggregate(String linguisticValueName, double mu) throws Exception {
        synchronized(engine.__lock) {
            for(LinguisticValue lv : linguisticValues) {
                if(lv.getName().equals(linguisticValueName)) {
                    lv.aggregate(mu);
                    return;
                }
            }
            throw new Exception("The linguistic value " + linguisticValueName + " is not defined.");
        }
    }
    
    protected void defuzzify() {
        synchronized(engine.__lock) {
            double step = 1;
            double sum = 0;
            double sumX = 0;
            for(int i = 0; i <= w; i += step) {
                double maxY = 0;
                double x = ((i  * (range_b - range_a)) / w) + range_a;
                for(LinguisticValue lv : linguisticValues) {
                    maxY = Math.max(maxY, lv.getAggregated(x));
                }
                sum += maxY;
                sumX += maxY * x;
                maxY = maxY * h;
            }
            if(sum != 0) {
                crispValue = sumX / sum;
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        synchronized(engine.__lock) {
            this.setTitle(name + ": " + crispValue);
            Graphics2D g2 = (Graphics2D) g;

            int step = 50;

            for(int i = 0; i <= w; i += step) {
                g2.setColor(Color.lightGray);
                g2.drawLine(i + x0, y0, i + x0, y0 - h);
                g2.setColor(Color.black);
                g2.drawString(((i  * (range_b - range_a)) / w) + range_a + "", i + x0, y0 + 20);
            }

            step = 1;

            Polygon polygon = new Polygon();
            polygon.addPoint(x0, y0);
            for(int i = 0; i <= w; i += step) {
                double maxY = 0;
                double x = ((i  * (range_b - range_a)) / w) + range_a;;
                for(LinguisticValue lv : linguisticValues) {
                    maxY = Math.max(maxY, lv.getAggregated(x));
                }
                maxY = maxY * h;
                polygon.addPoint((int) i + x0, (int) (y0 - maxY));
            }
            polygon.addPoint(x0 + w, y0);
            polygon.addPoint(x0, y0);
            g2.setColor(Color.cyan);
            g2.setColor(new Color(0, 200, 200, 150));
            g2.fillPolygon(polygon);

            int c = 0;
            g2.setStroke(new BasicStroke(3));

            for(LinguisticValue lv : linguisticValues) {
                g2.setColor(colors[c++]);
                MembershipFunction f = lv.getMembershipFunction();
                double lastX = 0;
                double t = f.calc(lastX);

                double lastY = t * h;
                double ip;
                for(int i = step; i <= w; i += step) {
                    ip = ((i  * (range_b - range_a)) / w) + range_a;
                    t = f.calc(ip);
                    t = (t * h);
                    g2.drawLine((int) lastX + x0, (int) (y0 - lastY), i + x0, (int) (y0 - t));
                    lastX = i;
                    lastY = t;
                }
            }

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2));
            double d = crispValue;
            d = (((d - range_a)  * w) / (range_b - range_a));
            g2.drawLine((int) (x0 + d), 0, (int) (x0 + d), 400);
            g2.setColor(Color.black);
            g2.drawRect(x0, y0 - h, w, h);
        }

    }
    
}
