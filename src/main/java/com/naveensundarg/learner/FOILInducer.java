package com.naveensundarg.learner;

import com.naveensundarg.shadow.prover.core.Prover;
import com.naveensundarg.shadow.prover.core.SnarkWrapper;
import com.naveensundarg.shadow.prover.representations.cnf.Clause;
import com.naveensundarg.shadow.prover.representations.formula.Formula;
import com.naveensundarg.shadow.prover.representations.formula.Predicate;
import com.naveensundarg.shadow.prover.utils.Sets;

import java.util.Set;
import java.util.stream.Collectors;

public class FOILInducer implements Type1Inducer{
    public Set<Formula> induce(Set<Formula> background, Set<Predicate> positives, Set<Predicate> negatives) {


        Prover prover = SnarkWrapper.getInstance();
        Set<Predicate> positiveExamples = Sets.copy(positives);
        Set<Predicate> negativeExamples = Sets.copy(negatives);


        Set<Clause> clauses = Sets.newSet();

        while(!positiveExamples.isEmpty()){

            Clause clause = newClause(positiveExamples, negativeExamples);

            Set<Predicate> covered = Sets.newSet();
            for(Predicate p: positiveExamples){

                if(prover.prove(Sets.add(background, clause.toFormula()), p).isPresent()){

                    covered.add(p);
                }
            }
            positiveExamples.removeAll(covered);

            clauses.add(clause);
        }

        return clauses.stream().map(Clause::toFormula).collect(Collectors.toSet());
    }

    private Clause newClause(Set<Predicate> positiveExamples, Set<Predicate> negativeExamples) {
        return null;
    }
}
