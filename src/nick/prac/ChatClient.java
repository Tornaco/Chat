package nick.prac;

import com.nick.lib.LogManager;
import nick.prac.common.Component;

/**
 * Created @2017/5/31 12:07
 */
public class ChatClient implements Component {
    @Override
    public void start() {
        // 做做日志记录。
        LogManager.log("Start client");
    }

    @Override
    public void stop() {

    }
}
