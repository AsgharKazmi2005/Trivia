import java.util.List;

public class Question {
    // Use Constructor to define private fields
    private String category;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public Question(String question, List<String> incorrect, String correct, String category) {
        this.question=question;
        this.incorrectAnswers=incorrect;
        this.correctAnswer=correct;
        this.category=category;
    }

    // Getters and setters for the fields
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    // Method to validate whether the user's answer is correct
    public boolean isCorrectAnswer(String userAnswer) {
        // Convert both the correct answer and user's answer to lowercase for case-insensitive comparison
        String correctAnswerLower = correctAnswer.toLowerCase();
        String userAnswerLower = userAnswer.toLowerCase();

        // Check if the user's answer matches the correct answer
        return correctAnswerLower.equals(userAnswerLower);
    }
}