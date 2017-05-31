package nick.prac;

/**
 * Created @2017/5/31 12:09
 */
// 该程序的启动器，要写main方法。
public class ServerLauncher {

    public static void main(String[] args) {
        // 启动服务器。
        ChatServer server = new ChatServer();
        server.start();

    }
}
