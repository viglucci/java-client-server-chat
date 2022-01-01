import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);

        log.info("server started: {}", serverSocket.getLocalSocketAddress());

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();

            log.info("client connected: {}", socket.getRemoteSocketAddress());

            (new ServerThread(socket)).start();
        }
    }
}
