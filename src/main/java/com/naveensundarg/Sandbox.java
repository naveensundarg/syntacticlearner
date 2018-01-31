package com.naveensundarg;

import com.naveensundarg.algorithms.FirstOrderAntiUnifier;
import com.naveensundarg.common.Utils;
import com.naveensundarg.dataset.BaBIDataset15;
import com.naveensundarg.dataset.Dataset;
import com.naveensundarg.dataset.NormalizedBaBIDataset15;
import com.naveensundarg.learner.Learner;
import com.naveensundarg.learner.RandomLearner;
import com.naveensundarg.learner.UnificationBasedLearner;
import com.naveensundarg.metrics.Accuracy;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Sandbox {

    public static void main(String[] args) throws IOException {


        Dataset baBIDataset15Train = new BaBIDataset15("./src/main/resources/com/naveensundarg/folqaitem/babi/tasks_1-20_v1-2 3/en-10k/qa15_basic-deduction_train.txt");
        Dataset baBIDataset15Test = new BaBIDataset15("./src/main/resources/com/naveensundarg/folqaitem/babi/tasks_1-20_v1-2 3/en-10k/qa15_basic-deduction_test.txt");


        Learner randomLeaner = new RandomLearner();
        randomLeaner.train(baBIDataset15Train);

        Accuracy accuracy1 = (Accuracy) randomLeaner.test(baBIDataset15Test);

        System.out.println(accuracy1.getAccuracy());


        Learner unificationLearner = new UnificationBasedLearner();
        unificationLearner.train(baBIDataset15Train);

        Accuracy accuracy2= (Accuracy) unificationLearner.test(baBIDataset15Test);

        System.out.println(accuracy2.getAccuracy());

    }
}
