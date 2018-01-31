package com.naveensundarg.dataset;

import com.naveensundarg.common.Converter;
import com.naveensundarg.shadow.prover.representations.value.Constant;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NormalizedBaBIDataset15 extends BaBIDataset15{


     public NormalizedBaBIDataset15(String filePath) throws IOException {

        super(filePath, Optional.of(NormalizedBaBIDataset15::normalize));
    }

     private static String normalize(String input){

        input = input.replaceAll("cats", "cat");

        input = input.replaceAll("wolves", "wolf");

        input = input.replaceAll("mice", "mouse");

        return input;
    }
}
