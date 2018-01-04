package knd.fuzzyInfEngine;

public class LinguisticValue {
    
    private String name;
    private MembershipFunction membershipFunction = null;
    private double aggregateMax = 0;
    
    protected void aggregate(double v) {
        aggregateMax = Math.max(aggregateMax, v) ;
    }
    
    protected double getAggregated(double x) {
        return Math.min(aggregateMax, membershipFunction.calc(x));
    }
    
    public LinguisticValue(String name, MembershipFunction membershipFunction) {
        this.name = name;
        this.membershipFunction = membershipFunction;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }

    public String getName() {
        return name;
    }
    
    protected void reset() {
        aggregateMax = 0;
    }
}
