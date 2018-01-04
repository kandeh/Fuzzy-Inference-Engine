package knd.fuzzyInfEngine;

public class TriangleMembershipFunction extends MembershipFunction {
    
    private double a = 0;
    private double b = 0;
    private double c = 0;
    private double hedge = 1;
    
    public TriangleMembershipFunction(double a, double b, double c, double hedge) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.hedge = hedge;
    }
    
    public TriangleMembershipFunction(double a, double b, double c) {
        this(a, b, c, 1);
    }
    
    private double calc_(double x) {
        if(x >= a && x <= b) {
            if(isEqual(a, b)) {
                return 1;
            }
            return ((x - a) / (b - a));
        }
        if(x >= b && x <= c) {
            if(isEqual(b, c)) {
                return 1;
            }
            return 1 - ((x - b) / (c - b));
        }
        return 0;
    }
    
    public double calc(double x) {
        return Math.pow(calc_(x), hedge);
    }
    
}