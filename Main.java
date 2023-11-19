import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Main {
    private final static SecureRandom random = new SecureRandom();
    private static BigInteger p; // Prime number p
    private static BigInteger q; // Prime number q
    private static BigInteger n; // Modulus
    private static BigInteger e; // Public exponent
    private static BigInteger d; // Private exponent

    // Declaration of JTextAreas as class variables
    private static JTextArea outputP;
    private static JTextArea outputQ;
    private static JTextArea outputN;
    private static JTextArea outputE;
    private static JTextArea outputD;
    private static JTextArea outputEncrypted;
    private static JTextArea outputDecrypted;

    public static void main(String[] args) {
        // User Interface Setup
        JFrame frame = new JFrame("RSA Encryption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1350, 700);
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Set the background color

        // Styling Interface Elements
        JTextField inputField = new JTextField(20);
        inputField.setBackground(Color.LIGHT_GRAY);
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputField.setForeground(Color.BLACK);
        inputField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Initializing JTextAreas
        outputP = createStyledOutputArea("Prime p");
        outputQ = createStyledOutputArea("Prime q");
        outputN = createStyledOutputArea("Modulus n");
        outputE = createStyledOutputArea("Public Exponent e");
        outputD = createStyledOutputArea("Private Exponent d");
        outputEncrypted = createStyledOutputArea("Encrypted Message");
        outputDecrypted = createStyledOutputArea("Decrypted Message");

        // Generate RSA Key Pair after initializing JTextAreas
        generateKeyPair();

        JButton encryptButton = styleButton("Encrypt");
        JButton decryptButton = styleButton("Decrypt");
        JButton clearButton = styleButton("Clear");

        // Title above the inputField
        JLabel titleLabel = new JLabel("Please insert your message");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        // Grouping JTextAreas in a single JPanel
        JPanel keyInfoPanel = new JPanel();
        keyInfoPanel.setLayout(new BoxLayout(keyInfoPanel, BoxLayout.Y_AXIS));
        keyInfoPanel.setBackground(new Color(30, 30, 30));
        keyInfoPanel.add(new JScrollPane(outputP));
        keyInfoPanel.add(new JScrollPane(outputQ));
        keyInfoPanel.add(new JScrollPane(outputN));
        keyInfoPanel.add(new JScrollPane(outputE));
        keyInfoPanel.add(new JScrollPane(outputD));

        // Grouping JTextAreas for encrypted and decrypted messages
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(30, 30, 30));
        messagePanel.add(new JScrollPane(outputEncrypted));
        messagePanel.add(new JScrollPane(outputDecrypted));

        // Encrypt Button Action
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String inputText = inputField.getText();
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Input field is empty.");
                    return;
                }

                try {
                    BigInteger message = new BigInteger(inputText.getBytes(StandardCharsets.UTF_8));
                    BigInteger encrypted = message.modPow(e, n);
                    outputEncrypted.setText(encrypted.toString(16)); // Convert to hexadecimal string
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error in encryption process.");
                }
            }
        });

        // Decrypt Button Action
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String encryptedText = outputEncrypted.getText();
                if (encryptedText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No encrypted message found.");
                    return;
                }

                try {
                    BigInteger encrypted = new BigInteger(encryptedText, 16); // Interpret as a hexadecimal string
                    BigInteger decrypted = encrypted.modPow(d, n);
                    outputDecrypted.setText(new String(decrypted.toByteArray(), StandardCharsets.UTF_8));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error during decryption.");
                }
            }
        });

        
        // Clear Button Action
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSpecificOutputs(inputField, outputEncrypted, outputDecrypted);
            }
        });

        // Adding Elements to the Window
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.add(titleLabel, BorderLayout.PAGE_START);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(keyInfoPanel, BorderLayout.LINE_START);  // Adding the panel with keys
        panel.add(messagePanel, BorderLayout.LINE_END);  // Adding the panel for encrypted and decrypted messages

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        frame.getContentPane().add(panel);

        //frame.pack();  // Adjust the window size based on the components
        frame.setVisible(true);
    }

    // Helper method to create styled output areas
    private static JTextArea createStyledOutputArea(String title) {
        JTextArea outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        outputArea.setBackground(Color.LIGHT_GRAY);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(000, 100, 100)), title);
        border.setTitleColor(new Color(250, 000, 000)); // Set border title color
        outputArea.setBorder(border);
        return outputArea;
    }

    // Helper method to style buttons
    private static JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 70, 70)); // Set button background color
        button.setForeground(new Color(255, 255, 255)); // Set text color
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120))); // Set border color
        return button;
    }


    // Helper method to clear specific output areas for JTextArea and JTextField
    private static void clearSpecificOutputs(JTextField textField, JTextArea... textAreas) {
        textField.setText("");
        for (JTextArea textArea : textAreas) {
            textArea.setText("");
        }
    }


    // Method to Generate a Pair of RSA Keys
    private static void generateKeyPair() {
        p = BigInteger.probablePrime(1024, random);
        q = BigInteger.probablePrime(1024, random);
        n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.valueOf(65537); // Common value for E
        d = e.modInverse(phi);

        // Display RSA parameters
        outputP.setText(p.toString());
        outputQ.setText(q.toString());
        outputN.setText(n.toString());
        outputE.setText(e.toString());
        outputD.setText(d.toString());
    }
}
