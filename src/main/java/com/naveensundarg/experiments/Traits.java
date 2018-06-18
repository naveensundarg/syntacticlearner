package com.naveensundarg.experiments;

import com.naveensundarg.algorithms.FirstOrderAntiUnifier;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.utils.Reader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Traits {

    public static void main(String[] args) {




/*
            List<Value> inputs = inputs("(trait (Believes! a1 (holds (old x) t1)) (happens (utters a1 (old x)) (+ t1 1)))",
                                        "(trait (Believes! a2 (holds (new y) t2)) (happens (utters a2 (new y)) (+ t2 1)))",
                                        "(trait (Believes! a3 (holds (new y) t3)) (happens (utters a3 (new y)) (+ t3 1)))");
*/

        List<Value> inputs = inputs("(P a a) ",
                "(P u v) ");



        System.out.println(FirstOrderAntiUnifier.antiUnify(inputs));

    }

    private static List<Value> inputs(String... inputs){

        return Arrays.stream(inputs).map(x-> {
            try {
                return Reader.readLogicValueFromString(x);
            } catch (Reader.ParsingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }
}
