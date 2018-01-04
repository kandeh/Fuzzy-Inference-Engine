package example;

import knd.fuzzyInfEngine.TwoAndRule;
import knd.fuzzyInfEngine.Engine;
import knd.fuzzyInfEngine.LinguisticValueDescription;
import knd.fuzzyInfEngine.LinguisticVariable;
import knd.fuzzyInfEngine.TrapezoidMembershipFunction;
import knd.fuzzyInfEngine.TriangleMembershipFunction;
import knd.fuzzyInfEngine.TwoOrRule;

public class example {

    public static void main(String[] args) {
        
        try {
            
            Engine engine = new Engine();
            LinguisticValueDescription lvd1 = new LinguisticValueDescription();     
            lvd1.add("Few",      new TrapezoidMembershipFunction(0, 0, 20, 40, 1.5));
            lvd1.add("Medium",   new TriangleMembershipFunction(30, 50, 70, 1.5));
            lvd1.add("Many",     new TrapezoidMembershipFunction(60, 80, 100, 100, 1.5));

            LinguisticValueDescription lvd2 = new LinguisticValueDescription();     
            lvd2.add("Few",      new TrapezoidMembershipFunction(0, 0, 5, 40, 0.7));
            lvd2.add("Medium",   new TriangleMembershipFunction(10, 50, 90, 0.8));
            lvd2.add("Many",     new TrapezoidMembershipFunction(60, 95, 100, 100, 0.7));

            engine.addLinguisticVariable(new LinguisticVariable(engine, false,  "variable1",        0, 100, lvd1));
            engine.addLinguisticVariable(new LinguisticVariable(engine, false,  "variable2",        0, 100, lvd2));
            engine.addLinguisticVariable(new LinguisticVariable(engine, true,   "outputVariable",   0, 100, lvd1));

            engine.addRule(new TwoOrRule("variable1", "Medium", "variable2", "Medium", "outputVariable", "Medium"));
            engine.addRule(new TwoAndRule("variable1", "Few", "variable2", "Few", "outputVariable", "Few"));
            engine.addRule(new TwoAndRule("variable1", "Medium", "variable2", "Medium", "outputVariable", "Many"));
            engine.addRule(new TwoAndRule("variable1", "Many", "variable2", "Many", "outputVariable", "Many"));
            
            
            engine.setValue("variable1", 55);
            engine.setValue("variable2", 86);

            engine.process();

            System.out.println(engine.getValue("outputVariable"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}