package nick.prac;

import com.nick.lib.LogManager;
import nick.prac.common.Component;
import nick.prac.net.Connection;
import nick.prac.net.MessageServer;
import nick.prac.ui.FrameContainer;
import nick.prac.ui.UI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created @2017/5/31 12:07
 */
public class ChatServer implements Component, UI, MessageServer {

    private FrameContainer frameContainer;
    private boolean isRunning;

    // 保存所有连接。
    private List<Connection> connections;

    // 写个构造器，在构造里初始化FrameContainer。
    public ChatServer() {
        frameContainer = new FrameContainer();
        frameContainer.setTitle("Server");
        frameContainer.setEnterEventListener(new FrameContainer.EnterEventListener() {
            @Override
            public void onEnter(String text) {
                // 调用发送即可。
                sendMessage(text);
            }
        });
        connections = new ArrayList<>();
    }

    @Override
    public void start() {
        LogManager.log("Start server");

        isRunning = true;

        show();

        // 启动SocketServer。
        new Thread(new Runnable() {
            @Override
            public void run() {
                startServerSocket();
            }
        }).start();

        // 写在这里是错误的。
        // 因此服务启动后，就去循环读取消息，此时很有可能没有任何连接。因此需要改一下。
    }

    private void startServerSocket() {
        try {
            ServerSocket ss = new ServerSocket(8888);

            // 不停accept。
            // 建议不要写true，写个变量，只要服务在运行就循环.
            while (isRunning) {

                // log里面。只看到Accept了一次，因次我们需要一直accept才能接收多个客户端连接。
                // 多个客户端需要写一个类去保存相关信息，例如Socket。
                Socket socket = ss.accept();

                // 加个log。
                LogManager.log("Accepted!!!");

                Connection connection = new Connection();
                connection.setSocket(socket);

                // 在这里去读取。
                readMessage(connection);

                // 加入列表。
                connections.add(connection);
            }

        } catch (IOException e) {
            LogManager.log("Fail start server %s", e.getLocalizedMessage());
        }
    }

    @Override
    public void stop() {
        isRunning = false;

        // 遍历所有Connection。关闭。
        for (Connection c : connections) {
            c.close();
        }
    }

    @Override
    public void show() {
        frameContainer.show();
    }

    // 当按下回车时，我们除了显示在文本显示区域，还需要发送出去。因此找到回车处理的部分。
    // 这里发送消息，就是群发，发给所有客户端的连接。
    @Override
    public void sendMessage(String message) {

        // 现在需要实现。
        // 或者在实现之前，先把UI和发送逻辑条通顺。这里加个log先。
        LogManager.log("sendMessage: %s", message);

        // 遍历发。
        for (Connection c : connections) {
            Socket socket = c.getSocket();
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(message);
            } catch (IOException e) {
                LogManager.log("Fail send message to client %s", e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void readMessage(Connection connection) {
        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket s = connection.getSocket();
                try {
                    InputStream is = s.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    // 之前忘记写循环了。。
                    // 这个log没打。
                    LogManager.log("Start reading from %s", connection);
                    while (true) {
                        String line = dis.readUTF();
                        // 但是我们需要一直不停的读取该连接。
                        // 所以需要循环。
                        // 考虑到很多连接，因此需要新的线程去读取，不然会阻塞。

                        // 当服务器收到消息的时候，我们需要转发给所有客户端。
                        LogManager.log("Read message from client: %s", line);
                        sendMessage(line);
                        // Fail read message from server socket closed 这个错误。
                        // 可能这里导致的。
                        // is.close();
                    }
                } catch (IOException e) {
                    LogManager.log("Fail read message %s", e.getLocalizedMessage());
                }
            }
        });

        readThread.start();
    }
}
