package com.example.quiztutorial;

public class answerclass {
    private int questionid;
    private int optionA;
    private int optionB;
    private int optionC;
    private int optionD;
    private int answerid;

    public answerclass(int questionid, int optionA, int optionB, int optionC, int optionD, int answerid) {
        this.questionid = questionid;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answerid = answerid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public int getOptionA() {
        return optionA;
    }

    public int getOptionB() {
        return optionB;
    }

    public int getOptionC() {
        return optionC;
    }

    public int getOptionD() {
        return optionD;
    }

    public int getAnswerid() {
        return answerid;
    }
}
