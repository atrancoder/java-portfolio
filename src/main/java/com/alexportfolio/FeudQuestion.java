package com.alexportfolio;

import java.util.Map;

public class FeudQuestion {
    public String question;
    public Map<String, Integer> answers;

    public FeudQuestion(String question, Map<String, Integer> answers) {
        this.question = question;
        this.answers = answers;
    }
}