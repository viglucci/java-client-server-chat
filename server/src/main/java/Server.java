import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);

        log.info("server started: {}", serverSocket.getLocalSocketAddress());

        while (!serverSocket.isClosed()) {

            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;

            try {
                Socket socket = serverSocket.accept();

                log.info("client connected: {}", socket.getRemoteSocketAddress());

                bufferedReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));

                while (!socket.isClosed()) {

                    String input = bufferedReader.readLine();
                    if (input != null && !input.trim().equals("")) {

                        log.info("client message: {}", input);

                        bufferedWriter.write("echo: " + input);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        if (input.equalsIgnoreCase("disconnect")) {
                            log.info("client disconnected: {}", socket.getRemoteSocketAddress());
                            break;
                        }
                    }
                }

                socket.close();
            } catch (SocketException e) {
                if (e.getMessage().equalsIgnoreCase("connection reset")) {
                    log.info("client disconnected unexpectedly");
                    log.error(e.getMessage(), e);
                } else {
                    throw e;
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (bufferedWriter != null) bufferedWriter.close();
                if (bufferedReader != null) bufferedReader.close();
            }
        }
    }
}
