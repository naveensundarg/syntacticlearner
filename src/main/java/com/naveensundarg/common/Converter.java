package com.naveensundarg.common;

import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.representations.formula.Predicate;
import com.naveensundarg.shadow.prover.representations.value.Compound;
import com.naveensundarg.shadow.prover.representations.value.Constant;
import com.naveensundarg.shadow.prover.representations.value.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static Value convertToTermSimple(String input) {

        String preprocessedInput = input.toLowerCase().replaceAll("[^0-9a-zA-Z ]", "");

        String[] preprocessedInputTokens = preprocessedInput.split(" ");

        Value[] args =  Arrays.stream(preprocessedInputTokens).map(Constant::new).toArray(Value[]::new);

        return new Compound("SEQ", args);
    }
}
