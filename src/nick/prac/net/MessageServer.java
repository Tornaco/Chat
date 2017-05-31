package nick.prac.net;

/**
 * Created @2017/5/31 13:15
 */

// 接口起手。
public interface MessageServer {

    // 发送消息的接口。
    void sendMessage(String message);

    void readMessage(Connection connection);
}
