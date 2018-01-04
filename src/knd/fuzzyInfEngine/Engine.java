package knd.fuzzyInfEngine;

import java.util.ArrayList;

public class Engine {
    
    private ArrayList<LinguisticVariable> linguisticVariables = new ArrayList<LinguisticVariable>();
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    protected final Object __lock = new Object();
    
    public void removeAllLinguisticVariables() {
        synchronized(__lock) {
            linguisticVariables.clear();
        }
    }
    
    public void addRule(Rule rule) {
        synchronized(__lock) {
            this.rules.add(rule);
        }
    }

    public void setValue(String variableName, double value) throws Exception {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
            if(lv.getVariableName().equals(variableName)) {
                lv.setValue(value);
                    return;
                }
            }
            throw new Exception("The variable " + variableName + " is not defined."); 
        }
    }
    
    public double getValue(String variableName) throws Exception {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
                if(lv.getVariableName().equals(variableName)) {
                    return lv.getValue();
                }
            }
            throw new Exception("The variable " + variableName + " is not defined.");
        }
    }
    
    public void addLinguisticVariable(LinguisticVariable lv) throws Exception {
        synchronized(__lock) {
            if(lv.getEngine() != this) {
                throw new Exception("Error.");
            }
            for(LinguisticVariable lv_ : linguisticVariables) {
                if(lv_.getVariableName().equals(lv.getVariableName())) {
                    throw new Exception("The variable " + lv.getVariableName() + " is already defined.");
                }
            }
            linguisticVariables.add(lv);
        }
    }
    
    protected double getMembership(String variableName, String valueName) throws Exception {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
                if(lv.getVariableName().equals(variableName)) {
                    if(lv.isOutputVariable()) {
                        lv.defuzzify();
                    }
                    return lv.getMembership(valueName);
                }
            }
            throw new Exception("The variable " + variableName + " is not defined.");
        }
    }
    
    protected void aggregate(String variableName, String valueName, double value) throws Exception {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
                if(lv.getVariableName().equals(variableName)) {
                    lv.aggregate(valueName, value);
                    return;
                }
            }
            throw new Exception("The variable " + variableName + " is not defined.");
        }
    }
    
    public double calcAnd(double a, double b) {
        return Math.min(a, b);
    }
    
    public double calcOr(double a, double b) {
        return Math.max(a, b);
    }
    
    public void process() throws Exception {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
                lv.reset();
            }
            for(Rule rule : rules) {
                rule.process(this);
            }
            for(LinguisticVariable lv : linguisticVariables) {
                if(lv.isOpaque()) {
                    lv.defuzzify();
                }
                lv.repaint();
            }
        }
    }
    
    public void repiant() {
        synchronized(__lock) {
            for(LinguisticVariable lv : linguisticVariables) {
                lv.repaint();
            }
        }
    }
    
}
