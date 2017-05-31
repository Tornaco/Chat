package nick.prac.net;

import com.nick.lib.LogManager;

import java.io.IOException;
import java.net.Socket;

/**
 * Created @2017/5/31 13:42
 */
// 定义一个连接类。保存一个连接。
public class Connection {

    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void close() {
        LogManager.log("Closing connection.");
        try {
            socket.close();
        } catch (IOException e) {
            LogManager.log("Fail close connection %s", e.getLocalizedMessage());
        }
    }
}
