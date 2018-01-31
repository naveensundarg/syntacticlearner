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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class BaBIDataset15 implements Dataset{


    private String filePath;
    private List<DataItem> items;

    public BaBIDataset15(String filePath) throws IOException {
        init(filePath, Optional.empty());

    }

    public BaBIDataset15(String filePath, Optional<UnaryOperator<String>> linePreprocessor) throws IOException {
        init(filePath, linePreprocessor);
    }

    private void init(String filePath, Optional<UnaryOperator<String>> linePreprocessor) throws IOException {
        this.filePath = filePath;
        this.items = CollectionUtils.newEmptyList();

        List<String> lines  = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
        ;

        boolean previousLineIsQuestion = false;
        List<Value> currentBackground = CollectionUtils.newEmptyList();

        Predicate<String> isLineQuestion  = line -> line.startsWith("wh");
        for(String line: lines){

            line = line.toLowerCase();
            if(linePreprocessor.isPresent()){
                line = linePreprocessor.get().apply(line);

            }
            String lineWithoutNumber = line.replaceAll("\\d", "").trim();
            if(previousLineIsQuestion & !isLineQuestion.test(lineWithoutNumber)){
                currentBackground.clear();
            }

            if(!isLineQuestion.test(lineWithoutNumber)){

                currentBackground.add(Converter.convertToTermSimple(lineWithoutNumber));
                previousLineIsQuestion = false;

            }
            else {

                String[] questionAndAnwer = lineWithoutNumber.replaceAll("\t", "").split("\\?");
                Value question = Converter.convertToTermSimple(questionAndAnwer[0]);
                Value answer = new Constant(questionAndAnwer[1]);

                items.add(new FOLQAItem(currentBackground, question, answer));
                previousLineIsQuestion = true;
            }

        }
    }

    public List<DataItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return
                "filePath='" + filePath + '\n' +
                        items;

    }


}
