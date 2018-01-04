package knd.fuzzyInfEngine;

public class TrapezoidMembershipFunction extends MembershipFunction {
    
    private double a = 0;
    private double b = 0;
    private double c = 0;
    private double d = 0;
    private double hedge = 1;
    
    public TrapezoidMembershipFunction(double a, double b, double c, double d, double hedge) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.hedge = hedge;
    }
    
    public TrapezoidMembershipFunction(double a, double b, double c, double d) {
        this(a, b, c, d, 1);
    }
    
    private double calc_(double x) {
        if(x >= a && x <= b) {
            if(isEqual(a, b)) {
                return 1;
            }
            return ((x - a) / (b - a));
        }
        if(x >= b && x <= c) {
            return 1;
        }
        if(x >= c && x <= d) {
            if(isEqual(c, d)) {
                return 1;
            }
            return 1 - ((x - c) / (d - c));
        }
        return 0;
    }
    
    public double calc(double x) {
        return Math.pow(calc_(x), hedge);
    }
    
}