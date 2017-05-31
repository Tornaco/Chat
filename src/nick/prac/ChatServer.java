package nick.prac;

import com.nick.lib.LogManager;
import nick.prac.common.Component;

/**
 * Created @2017/5/31 12:07
 */
public class ChatServer implements Component {
    @Override
    public void start() {
        LogManager.log("Start server");
    }

    @Override
    public void stop() {

    }
}
