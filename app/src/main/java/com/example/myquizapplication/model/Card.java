package com.example.myquizapplication.model;

public class Card {
    private int id;
    private int setId;
    private String term;
    private String definition;
    private boolean correctAnswerFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isCorrectAnswerFlag() {
        return correctAnswerFlag;
    }

    public void setCorrectAnswerFlag(boolean correctAnswerFlag) {
        this.correctAnswerFlag = correctAnswerFlag;
    }
}
