package nick.prac.common;

/**
 * Created @2017/5/31 12:07
 */

// 定义一个组件接口，可以启动和停止。
// Server和Client都会实现这个接口。
public interface Component {
    void start();

    void stop();
}
