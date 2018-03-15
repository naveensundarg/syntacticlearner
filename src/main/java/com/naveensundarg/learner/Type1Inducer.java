package com.naveensundarg.learner;

import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.representations.formula.Predicate;

import java.util.Set;

public interface Type1Inducer {

    Set<Formula> induce(Set<Formula> background, Set<Predicate> positives, Set<Predicate> negatives);

}
