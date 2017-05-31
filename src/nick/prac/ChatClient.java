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
import java.net.Socket;

/**
 * Created @2017/5/31 12:07
 */
public class ChatClient implements Component, UI, MessageServer {

    private FrameContainer frameContainer;
    private Connection connection;

    public ChatClient() {
        frameContainer = new FrameContainer();
        frameContainer.setTitle("Client");
        frameContainer.setEnterEventListener(new FrameContainer.EnterEventListener() {
            @Override
            public void onEnter(String text) {
                // 调用发送即可。
                sendMessage(text);
            }
        });
    }

    @Override
    public void start() {
        // 做做日志记录。
        LogManager.log("Start client");
        show();

        // 开始启动Socket client.
        startSocket();
    }

    private void startSocket() {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);// 之前我们用错方法了，再试试这个。

            // 加个log看看是否恋上了。
            LogManager.log("Connected to server.");

            // 连接OK。
            connection = new Connection();
            connection.setSocket(socket);
            readMessage(connection);

        } catch (IOException e) {
            LogManager.log("Fail start client %s", e.getLocalizedMessage());
        }
    }

    @Override
    public void stop() {
        connection.close();
    }


    @Override
    public void show() {
        frameContainer.show();
    }

    // 另外，chat client发送消息，就是往服务器发送。服务器负责转发。
    @Override
    public void sendMessage(String message) {

        // 现在需要实现。
        // 或者在实现之前，先把UI和发送逻辑条通顺。这里加个log先。
        LogManager.log("sendMessage: %s", message);

        // 使用流发的送。
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(connection.getSocket().getOutputStream());
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            LogManager.log("Fail send message to server %s", e.getLocalizedMessage());
        }
    }

    // start的时候调用了read，因此不能阻塞，不然其他的客户端无法启动。
    @Override
    public void readMessage(Connection connection) {
        // 客户端读取，只需要不停读取服务端的内容就可以了。
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataInputStream dis = new DataInputStream(connection.getSocket().getInputStream());
                        String line = dis.readUTF();
                        // 显示到文本区域上。
                        LogManager.log("Read message %s", line);
                        frameContainer.appendTextInTextArea(line);
                    } catch (IOException e) {
                        LogManager.log("Fail read message from server: %s", e.getLocalizedMessage());

                        System.exit(-1); // 致命错误，退出系统。
                    }
                }
            }
        }).start();
    }
}
