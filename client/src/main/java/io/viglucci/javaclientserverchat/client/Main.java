package io.viglucci.javaclientserverchat.client;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class Main {

    public static void main(String[] args) {

        final String QUIT_COMMAND = "QUIT";

        Socket socket = null;

        InputStreamReader inputStreamReader = null;

        OutputStreamWriter outputStreamWriter = null;

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        System.out.println("Session started.");
        System.out.println("Enter commands below.");
        System.out.println("");
        try {
            // instanciate socket
            socket = new Socket("localhost", 1234);

            inputStreamReader = new InputStreamReader(socket.getInputStream());

            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("> ");

                String messageToSend = scanner.nextLine();

                bufferedWriter.write(messageToSend);

                bufferedWriter.newLine();

                bufferedWriter.flush();

                System.out.println(bufferedReader.readLine());

                if (messageToSend.equalsIgnoreCase(QUIT_COMMAND)) {

                    System.out.println(" ");
                    System.out.println("Session ended.");
                    System.out.println(" ");

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
