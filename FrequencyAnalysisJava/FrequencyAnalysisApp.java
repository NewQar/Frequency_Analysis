import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FrequencyAnalysisApp extends JFrame {
    private JTextField inputCiphertext;
    private JTextField inputSubstitution;
    private JTextArea textOutput;

    public FrequencyAnalysisApp() {
        setTitle("Frequency Analysis and Decryption");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        inputCiphertext = new JTextField(30);
        inputSubstitution = new JTextField(30);
        textOutput = new JTextArea(20, 30);
        textOutput.setEditable(false);

        JButton buttonReset = new JButton("Reset");
        JButton buttonRemoveSpaces = new JButton("Remove Spaces");
        JButton buttonCountSingle = new JButton("Count Single Characters");
        JButton buttonCountDiagrams = new JButton("Count Diagrams");
        JButton buttonCountTrigrams = new JButton("Count Trigrams");
        JButton buttonDecryptConvert = new JButton("Decrypt/Convert");

        add(new JLabel("Enter Ciphertext:"));
        add(inputCiphertext);
        add(buttonReset);
        add(buttonRemoveSpaces);
        add(buttonCountSingle);
        add(buttonCountDiagrams);
        add(buttonCountTrigrams);
        add(new JLabel("Enter Substitution Table:"));
        add(inputSubstitution);
        add(buttonDecryptConvert);
        add(new JScrollPane(textOutput));

        buttonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputCiphertext.setText("");
                textOutput.setText("");
            }
        });

        buttonRemoveSpaces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputCiphertext.getText();
                inputCiphertext.setText(ciphertext.replace(" ", ""));
            }
        });

        buttonCountSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputCiphertext.getText();
                String frequencyAnalysis = analyzeFrequency(ciphertext);
                textOutput.setText(frequencyAnalysis);
            }
        });

        buttonCountDiagrams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputCiphertext.getText();
                String frequencyAnalysis = analyzeNgrams(ciphertext, 2);
                textOutput.setText(frequencyAnalysis);
            }
        });

        buttonCountTrigrams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputCiphertext.getText();
                String frequencyAnalysis = analyzeNgrams(ciphertext, 3);
                textOutput.setText(frequencyAnalysis);
            }
        });

        buttonDecryptConvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputCiphertext.getText();
                String substitutionTable = inputSubstitution.getText();
                String decryptedText = decryptCiphertext(ciphertext, substitutionTable);
                textOutput.setText(decryptedText);
            }
        });
    }

    private String analyzeFrequency(String ciphertext) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()) {
            frequencyMap.put(c, 0);
        }

        for (char c : ciphertext.toUpperCase().toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return result.toString();
    }

    private String analyzeNgrams(String ciphertext, int n) {
        Map<String, Integer> ngramMap = new HashMap<>();
        for (int i = 0; i <= ciphertext.length() - n; i++) {
            String ngram = ciphertext.substring(i, i + n).toUpperCase();
            ngramMap.put(ngram, ngramMap.getOrDefault(ngram, 0) + 1);
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : ngramMap.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return result.toString();
    }

    private String decryptCiphertext(String ciphertext, String substitutionTable) {
        Map<Character, Character> substitutionMap = new HashMap<>();

        String[] lines = substitutionTable.toUpperCase().split("\n");
        for (String line : lines) {
            String[] parts = line.split("->");
            if (parts.length == 2) {
                char originalChar = parts[0].trim().charAt(0);
                char substituteChar = parts[1].trim().charAt(0);
                substitutionMap.put(originalChar, substituteChar);
            }
        }

        StringBuilder decryptedText = new StringBuilder();
        for (char c : ciphertext.toUpperCase().toCharArray()) {
            decryptedText.append(substitutionMap.getOrDefault(c, c));
        }

        return decryptedText.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrequencyAnalysisApp().setVisible(true);
            }
        });
    }
}
