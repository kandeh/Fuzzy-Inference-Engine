package knd.fuzzyInfEngine;

public class TwoAndRule extends Rule {
    
    private String name1;
    private String is1;
    private String name2;
    private String is2;
    private String name3;
    private String is3;
        
    public TwoAndRule(String name1, String is1, String name2, String is2, String name3, String is3) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.is1 = is1;
        this.is2 = is2;
        this.is3 = is3;
    }
    
    protected void process(Engine engine) throws Exception {
        double a = engine.getMembership(name1, is1);
        double b = engine.getMembership(name2, is2);
        engine.aggregate(name3, is3, engine.calcAnd(a, b));
    }
    
}
