package com.naveensundarg.learner;

import clojure.lang.Var;
import com.naveensundarg.algorithms.FirstOrderAntiUnifier;
import com.naveensundarg.common.Utils;
import com.naveensundarg.dataset.DataItem;
import com.naveensundarg.dataset.Dataset;
import com.naveensundarg.metrics.Accuracy;
import com.naveensundarg.metrics.Metrics;
import com.naveensundarg.shadow.prover.core.proof.Unifier;
import com.naveensundarg.shadow.prover.representations.value.Compound;
import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.representations.value.Variable;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;
import com.naveensundarg.shadow.prover.utils.Reader;
import com.naveensundarg.shadow.prover.utils.Sets;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UnificationBasedLearner implements Learner{


    private final Set<Value> generalizedBackGround;
    private Value generalizedInput;

    private Variable answerVariable;
    public UnificationBasedLearner() {
        this.generalizedBackGround = Sets.newSet();
        answerVariable = (Variable) Utils.readValueFromString("?ans");

    }


    @Override
    public void train(Dataset dataset) {


        List<Value> inputs = dataset.getItems().stream().map(x-> (Value) x.getInput()).collect(Collectors.toList());




        AtomicInteger variableNumber = new AtomicInteger(1);
        this.generalizedInput = FirstOrderAntiUnifier.antiUnify(inputs, variableNumber);

        AtomicInteger MIN_SIZE_RELEVANT_BACKGROUND = new AtomicInteger(Integer.MAX_VALUE);
        List<List<Value>> relevantBackgrounds = CollectionUtils.newEmptyList();

        for(DataItem dataItem: dataset.getItems()){

            Map<Variable, Value> specializationInput = Unifier.unify(this.generalizedInput, (Value) dataItem.getInput());
            if(specializationInput==null){
                continue;
            } else {

                // First Relevant terms are those that contain the input.
                // Second Relevant terms are those that contain first relevant terms and the output.

                Value answer = (Value) dataItem.getOutput();
                Set<Value> firstRelevantTerms = Sets.newSet();
                firstRelevantTerms.addAll(specializationInput.values());

                Set<Value> secondRelevantTerms = Sets.newSet();
                 secondRelevantTerms.add(answer);

                List<Value> backgroundTerms = ((List<Value>) dataItem.getBackground());
                List<Value> firstRelevantBackground = backgroundTerms.stream().filter(x-> firstRelevantTerms.stream().anyMatch(y->Utils.contains(x.getArguments(), y))).collect(Collectors.toList());

                List<Value>  termsinFirstRelevantBackground  = firstRelevantBackground.stream().flatMap(x-> Arrays.stream(x.getArguments())).collect(Collectors.toList());
                List<Value> secondRelevantBackground = backgroundTerms.stream().
                        filter(x-> secondRelevantTerms.stream().anyMatch(y->Utils.contains(x.getArguments(), y))).
                        filter(x-> termsinFirstRelevantBackground.stream().anyMatch(y->Utils.contains(x.getArguments(), y))).
                         collect(Collectors.toList());


                Set<Value> thisRelevantBackground = CollectionUtils.newEmptySet();
                thisRelevantBackground.addAll(firstRelevantBackground);
                thisRelevantBackground.addAll(secondRelevantBackground);

                specializationInput.put(answerVariable, answer);
                Map<Value, Variable> generalizationMap = Utils.reverseMap(specializationInput).get();

                thisRelevantBackground = thisRelevantBackground.stream().map(x-> x.generalize(generalizationMap)).collect(Collectors.toSet());

                if(thisRelevantBackground.size()<= MIN_SIZE_RELEVANT_BACKGROUND.get()){

                     MIN_SIZE_RELEVANT_BACKGROUND.set(thisRelevantBackground.size());
                   relevantBackgrounds.add(thisRelevantBackground.stream().sorted(Comparator.comparingInt(x->x.getArguments().length)).collect(Collectors.toList()));

                }




            }


        }


        relevantBackgrounds = relevantBackgrounds.stream().filter(x-> x.size()<= MIN_SIZE_RELEVANT_BACKGROUND.get()).collect(Collectors.toList());

        List<Value> relevantBackgroundsAsTerms = relevantBackgrounds.stream().map(x-> new Compound("SEQ", x.stream().toArray(Value[]::new))).collect(Collectors.toList());


        Value generalizedBackgroundAsaTerm = (FirstOrderAntiUnifier.antiUnify(relevantBackgroundsAsTerms, variableNumber));

        generalizedBackGround.addAll(Arrays.stream(generalizedBackgroundAsaTerm.getArguments()).collect(Collectors.toList()));

        
    }


    public void construct() throws Reader.ParsingException {


    }

    @Override
    public Metrics test(Dataset dataset) {


        int totalCorrect = dataset.getItems().stream().mapToInt(this::predict).sum();

        return new Accuracy(1.0*totalCorrect/dataset.getItems().size());

    }

    private static <T> Set<List<T>> nProd (Set<T> things, int n){

        List<Set<T>> listofSetOfThings = CollectionUtils.newEmptyList();


        IntStream.range(0, n).forEach(x->{

            listofSetOfThings.add(things);
        });

        return Sets.cartesianProduct(listofSetOfThings);

    }
    public int predict(DataItem dataItem)  {



        Value input =  (Value) dataItem.getInput();
        Value output =  (Value) dataItem.getOutput();
        List<Value> background = (List<Value>) dataItem.getBackground();

        Map<Variable, Value> specifier = Unifier.unify(input, generalizedInput);



        List<Value> instTerms = generalizedBackGround.stream().map(x->x.apply(specifier)).collect(Collectors.toList());
        Set<List<Value>> prods  = nProd(new HashSet<>(background), 2);

        for(List<Value> prod: prods){

            Value left = new Compound("ALL", instTerms);
            Value right = new Compound("ALL", prod);

            Map<Variable, Value> map =  Unifier.unify(left, right);

             if(map!=null && map.containsKey(answerVariable)){

                if(output.equals(map.get(answerVariable))){

                    return 1;
                }
                else {

                    return 0;
                }
            }
         }


        return 0;



    }

}
