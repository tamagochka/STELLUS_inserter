package my.tamagochka.Main;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String questionText = null;
    private List<Answer> answers = new ArrayList<>();

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Answer getAnswer(int i) {
        return answers.get(i);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public int getCountAnswers() {
        return answers.size();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Answer getRightAnswer() {
        for(Answer answer : answers)
            if(answer.isCorrect()) return answer;
        return null;
    }

    public int getNumberAnswer(Answer answer) {
        return answers.indexOf(answer);
    }

    @Override
    public String toString() {
        String result = "";
        for(Answer answer : answers)
            result = result + answer + "; ";
        result = result.substring(0, result.length() - 2);
        return "[" + questionText + " ] {" + result  + "}";
    }

}
