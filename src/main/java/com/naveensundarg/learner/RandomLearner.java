package com.naveensundarg.learner;

 import com.naveensundarg.dataset.DataItem;
import com.naveensundarg.dataset.Dataset;
 import com.naveensundarg.metrics.Accuracy;
 import com.naveensundarg.metrics.Metrics;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;

import java.util.Set;
 import java.util.concurrent.ThreadLocalRandom;

public class RandomLearner implements Learner{

    private final Set<Object> possibleOutputsSet;
    private  Object[] possibleOutputs;

    public RandomLearner() {
        possibleOutputsSet = CollectionUtils.newEmptySet();
        possibleOutputs = new Object[0];
    }

    public void train(Dataset dataset) {

        for(DataItem dataItem: dataset.getItems()){

            possibleOutputsSet.add(dataItem.getOutput());
        }

        possibleOutputs = new Object[possibleOutputsSet.size()];

        possibleOutputs = possibleOutputsSet.toArray();

    }

    private Object randomPrediction(){

        return possibleOutputs[ThreadLocalRandom.current().nextInt(0, possibleOutputs.length)];

    }
     public Metrics test(Dataset testDataset) {


        int total = testDataset.getItems().size();


        int correctCount = testDataset.getItems().stream().mapToInt(dataItem -> dataItem.getOutput().equals(randomPrediction())? 1: 0).sum();


        return new Accuracy(1.0* correctCount/total);

    }
}
