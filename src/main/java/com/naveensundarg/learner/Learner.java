package com.naveensundarg.learner;

import com.naveensundarg.dataset.Dataset;
import com.naveensundarg.metrics.Metrics;

public interface Learner {

     void train(Dataset  t);
    Metrics test(Dataset  t);

}
