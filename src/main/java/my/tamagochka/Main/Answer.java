package my.tamagochka.Main;

public class Answer {

    private String answerText = null;
    private boolean correct = false;

    public Answer(String answerText, boolean correct) {
        this.answerText = answerText;
        this.correct = correct;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return "[" + answerText + ", " + correct + "]";
    }

}
