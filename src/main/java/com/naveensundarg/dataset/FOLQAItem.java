package com.naveensundarg.dataset;

import com.naveensundarg.shadow.prover.representations.value.Value;
import com.naveensundarg.shadow.prover.utils.CollectionUtils;

import java.util.List;

public class FOLQAItem implements DataItem {


    private final List<Value> background;
    private final Value question;
    private final Value answer, output;
    private final List<Value> input;

    public FOLQAItem(List<Value> background, Value question, Value answer) {
        this.background = CollectionUtils.newEmptyList();
        this.background.addAll(background);
        this.question = question;
        this.answer = answer;
        this.input = CollectionUtils.newEmptyList();
        this.input.addAll(this.background);
        this.input.add(question);
        this.output = answer;
    }


    public Object getBackground() {
        return background;
    }

    public Value getQuestion() {
        return question;
    }

    public Value getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "FOLQAItem{" +
                "\n background=" + background +
                ",\n question=" + question +
                ",\n answer=" + answer +
                '}';
    }

    @Override
    public Value getInput() {
        return question;
    }

    @Override
    public Value getOutput() {
        return output;
    }
}
