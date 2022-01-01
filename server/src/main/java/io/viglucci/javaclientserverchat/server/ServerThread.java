package io.viglucci.javaclientserverchat.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
class ServerThread extends Thread {

    String input = null;
    BufferedReader inputStream = null;
    BufferedWriter outputStream = null;
    Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        final String QUIT_COMMAND = "QUIT";

        log.info("client thread started: {}, {}", this.getName(), socket.getRemoteSocketAddress());

        try {
            inputStream = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            outputStream = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            log.error("failed to establish IO", e);
        }

        try {
            input = inputStream.readLine();
            while (!socket.isClosed() && input.compareTo(QUIT_COMMAND) != 0) {

                log.info("client message: {}", input);

                String response = "echo: " + input;

                log.info("server response: {}", response);

                outputStream.write(response);
                outputStream.newLine();
                outputStream.flush();

                input = inputStream.readLine();
            }
        } catch (IOException e) {
            log.error("{} terminated abruptly", this.getName(), e);
        } catch (NullPointerException e) {
            log.error("client {} closed abruptly", this.getName(), e);
        } finally {
            try {
                log.info("client closing: {}, {}", this.getName(), socket.getRemoteSocketAddress());

                if (inputStream != null) {
                    inputStream.close();
                    log.info("input stream closed");
                }

                if (outputStream != null) {
                    outputStream.close();
                    log.info("output stream closed");
                }

                if (socket != null) {
                    socket.close();
                    log.info("socket closed");
                }
            } catch (IOException e) {
                log.error("socket close error", e);
            }
        }
    }
}
