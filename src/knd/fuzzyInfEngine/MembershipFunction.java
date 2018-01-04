package knd.fuzzyInfEngine;

public abstract class MembershipFunction {
    
    abstract public double calc(double x);
    
    public static boolean isEqual(double a, double b) {
        if(Math.abs(a - b) <= 1e-8) {
            return true;
        }
        return false;
    }
    
}
