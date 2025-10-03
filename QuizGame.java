import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizGame extends JFrame implements ActionListener {
    
    private JLabel titleLabel;
    private JLabel questionLabel;
    private JLabel questionNumberLabel;
    private JLabel scoreLabel;
    private JButton[] optionButtons;
    private JButton nextButton;
    private JButton restartButton;
    private JPanel mainPanel;
    
    private Question[] questions;
    private int currentQuestionIndex;
    private int score;
    private int selectedOption;
    private boolean questionAnswered;
    
    public QuizGame() {
        initializeQuestions();
        setupGUI();
        startNewQuiz();
    }
    
    private void initializeQuestions() {
        questions = new Question[] {
            new Question(
                "What is the time complexity of binary search?",
                new String[]{"O(n)", "O(log n)", "O(n²)", "O(1)"},
                1
            ),
            new Question(
                "Which data structure uses LIFO principle?",
                new String[]{"Queue", "Stack", "Array", "Tree"},
                1
            ),
            new Question(
                "What does OOP stand for?",
                new String[]{"Object Oriented Programming", "Object Operation Protocol", 
                           "Organized Object Processing", "Optional Object Parameters"},
                0
            ),
            new Question(
                "Which of these is NOT a Java keyword?",
                new String[]{"static", "final", "main", "class"},
                2
            ),
            new Question(
                "What is the default value of a boolean variable in Java?",
                new String[]{"true", "false", "null", "0"},
                1
            ),
            new Question(
                "Which collection class allows duplicate elements?",
                new String[]{"Set", "HashSet", "ArrayList", "TreeSet"},
                2
            ),
            new Question(
                "What is encapsulation in OOP?",
                new String[]{"Inheriting properties", "Hiding implementation details", 
                           "Creating multiple objects", "Overriding methods"},
                1
            ),
            new Question(
                "Which sorting algorithm has O(n log n) average time complexity?",
                new String[]{"Bubble Sort", "Selection Sort", "Merge Sort", "Insertion Sort"},
                2
            )
        };
    }
    
    private void setupGUI() {
        setTitle("Computer Science Quiz Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(245, 245, 250));
        titleLabel = new JLabel("Computer Science Quiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 102, 153));
        titlePanel.add(titleLabel);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(questionNumberLabel, BorderLayout.WEST);
        headerPanel.add(scoreLabel, BorderLayout.EAST);
        
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        questionPanel.add(questionLabel);
        
        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            optionButtons[i].setMargin(new Insets(10, 15, 10, 15));
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setBackground(new Color(248, 248, 255));
            optionButtons[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            optionButtons[i].addActionListener(this);
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            questionPanel.add(optionButtons[i]);
            questionPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBackground(new Color(51, 102, 153));
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(140, 35));
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        
        restartButton = new JButton("Restart Quiz");
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setBackground(new Color(204, 102, 102));
        restartButton.setForeground(Color.WHITE);
        restartButton.setPreferredSize(new Dimension(140, 35));
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(this);
        restartButton.setVisible(false);
        
        buttonPanel.add(nextButton);
        buttonPanel.add(restartButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(headerPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void startNewQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        questionAnswered = false;
        selectedOption = -1;
        
        nextButton.setText("Next Question");
        nextButton.setVisible(true);
        restartButton.setVisible(false);
        
        for (JButton button : optionButtons) {
            button.setVisible(true);
            button.setEnabled(true);
            button.setBackground(new Color(248, 248, 255));
        }
        
        displayCurrentQuestion();
    }
    
    private void displayCurrentQuestion() {
        if (currentQuestionIndex < questions.length) {
            Question current = questions[currentQuestionIndex];
            
            questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.length);
            scoreLabel.setText("Score: " + score + "/" + questions.length);
            questionLabel.setText("<html><div style='width: 580px;'>" + current.getQuestionText() + "</div></html>");
            
            String[] options = current.getOptions();
            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setText((char)('A' + i) + ") " + options[i]);
                optionButtons[i].setEnabled(true);
                optionButtons[i].setBackground(new Color(248, 248, 255));
            }
            
            questionAnswered = false;
            nextButton.setEnabled(false);
            
            if (currentQuestionIndex == questions.length - 1) {
                nextButton.setText("Show Results");
            }
        }
    }
    
    private void selectOption(int option) {
        if (!questionAnswered) {
            selectedOption = option;
            questionAnswered = true;
            
            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setEnabled(false);
                if (i == option) {
                    optionButtons[i].setBackground(new Color(173, 216, 230));
                }
            }
            
            nextButton.setEnabled(true);
        }
    }
    
    private void showResults() {
        for (JButton button : optionButtons) {
            button.setVisible(false);
        }
        
        double percentage = ((double) score / questions.length) * 100;
        
        JLabel resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        String resultText = "<html><div style='text-align: center; padding: 40px;'>";
        resultText += "<h2>Quiz Complete!</h2>";
        resultText += "<p style='font-size: 16px;'>Your Final Score: <b>" + score + " out of " + questions.length + "</b></p>";
        resultText += "<p style='font-size: 16px;'>Percentage: <b>" + String.format("%.1f", percentage) + "%</b></p>";
        
        if (percentage >= 80) {
            resultText += "<p style='color: green; font-size: 16px;'>Excellent! Great job! 🌟</p>";
        } else if (percentage >= 60) {
            resultText += "<p style='color: blue; font-size: 16px;'>Good work! Keep it up! 👍</p>";
        } else {
            resultText += "<p style='color: orange; font-size: 16px;'>Keep studying and try again! 📚</p>";
        }
        
        resultText += "</div></html>";
        resultLabel.setText(resultText);
        
        questionLabel.setText(resultText);
        
        nextButton.setVisible(false);
        restartButton.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < optionButtons.length; i++) {
            if (e.getSource() == optionButtons[i]) {
                selectOption(i);
                return;
            }
        }
        
        if (e.getSource() == nextButton) {
            if (questionAnswered) {
                if (selectedOption == questions[currentQuestionIndex].getCorrectAnswer()) {
                    score++;
                }
                
                currentQuestionIndex++;
                
                if (currentQuestionIndex < questions.length) {
                    displayCurrentQuestion();
                } else {
                    showResults();
                }
            }
        }
        
        if (e.getSource() == restartButton) {
            startNewQuiz();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.out.println("Could not set system look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            new QuizGame().setVisible(true);
        });
    }
}

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswer;
    
    public Question(String questionText, String[] options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public String[] getOptions() {
        return options;
    }
    
    public int getCorrectAnswer() {
        return correctAnswer;
    }
}