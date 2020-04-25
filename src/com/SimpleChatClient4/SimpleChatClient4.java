package com.SimpleChatClient4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleChatClient4 {


        JTextArea incoming;
        JTextField outgoing;
        BufferedReader reader;
        Socket sock;
        PrintWriter writer;

        public static void main(String[] args) {
            SimpleChatClient4 client4 = new SimpleChatClient4();
            client4.go();
        }

        public void go() {
            JFrame frame = new JFrame("Ludicrously Simple Chat Client");
            JPanel mainPanle = new JPanel();
            incoming = new JTextArea(15, 50);
            incoming.setLineWrap(true);
            incoming.setWrapStyleWord(true);
            incoming.setEditable(false);
            JScrollPane qSqroller = new JScrollPane(incoming);
            qSqroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            qSqroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


            outgoing = new JTextField(20);
            JButton sendButton = new JButton("send");
            sendButton.addActionListener(new SendButtonListener());
            mainPanle.add(qSqroller);
            mainPanle.add(outgoing);
            mainPanle.add(sendButton);
            setUpNetWorking();


            Thread readerThread = new Thread(new IncomingReader());
            readerThread.setName("reader Thread");
            readerThread.start();
            frame.getContentPane().add(BorderLayout.CENTER, mainPanle);
            frame.setSize(400, 500);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);

        }

        public void setUpNetWorking() {
            try {
                sock = new Socket("127.0.0.1", 5000);
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                System.out.println("networking established");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public class SendButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    writer.println(outgoing.getText());
                    writer.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                outgoing.setText("");
                outgoing.requestFocus();
            }
        }

        public class IncomingReader implements Runnable {
            @Override
            public void run() {
                String message;
                try {
                    while ((message = reader.readLine()) != null
                    ) {
                        System.out.println("reade " + message);
                        incoming.append("client 4 : " +message + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


    }



