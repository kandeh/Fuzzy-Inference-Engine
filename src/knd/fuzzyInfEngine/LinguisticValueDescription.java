package knd.fuzzyInfEngine;

import java.util.ArrayList;
import javafx.util.Pair;

public class LinguisticValueDescription {
    
    protected ArrayList<Pair<String, MembershipFunction>> nameFunctions = new ArrayList<Pair<String, MembershipFunction>>();
    
    public void add(String name, MembershipFunction mf) throws Exception {
        for(Pair<String, MembershipFunction> p : nameFunctions) {
            if(p.getKey().equals(name)) {
                throw new Exception("The linguistic value " + name + " is already defined.");
            }
        }
        nameFunctions.add(new Pair<String, MembershipFunction>(name, mf));
    }
    
}
