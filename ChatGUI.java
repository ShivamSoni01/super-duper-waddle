import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatGUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private ChatClient client;

    public ChatGUI(String serverAddress, int port) {
        try {
            client = new ChatClient(serverAddress, port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server");
            System.exit(0);
        }

        frame = new JFrame("Java LAN Chat");
        chatArea = new JTextArea(20, 50);
        inputField = new JTextField(40);

        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JButton sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Action to send message
        ActionListener sendAction = e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                client.sendMessage(msg);
                inputField.setText("");
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        // Receive messages in background
        new Thread(() -> {
            try {
                String message;
                BufferedReader in = client.getInput();
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                chatArea.append("Connection closed.\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI("localhost", 1234));
    }
}
