package nick.prac.ui;

import com.nick.lib.LogManager;
import nick.prac.settings.SettingsStorage;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created @2017/5/31 13:07
 */

// 定义一个显示专用类。把相关方法封装进来。
public class FrameContainer implements UI {

    private TextField textField;
    private TextArea textArea;

    private String title;

    private EnterEventListener enterEventListener;

    // 定义设置题目的方法。
    public void setTitle(String title) {
        this.title = title;
    }

    // 设置回车事件监听器。
    public void setEnterEventListener(EnterEventListener enterEventListener) {
        this.enterEventListener = enterEventListener;
    }

    @Override
    public void show() {
        Frame frame = new Frame(this.title);
        frame.setResizable(true); // 是否支持修改窗口大小。
        frame.setSize(SettingsStorage.getWindowSize()[0], SettingsStorage.getWindowSize()[1]); // 窗口大小。写死了不好。写个类去做配置处理。

        // 窗口无法关闭，我们加了监听，先打打log。
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                LogManager.log("windowClosing %s", e);
                // 点击关闭的时候，就会触发这个。所以我们在这里退出。
                System.exit(0);
            }

            // 这个重写目前不知道什么时候会调用。先不管。
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                LogManager.log("windowClosed %s", e);
            }
        });

        // 输入框。
        textField = new TextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                // 猜测是这个方法，监听键盘输入。
                // 加个log看看。
                LogManager.log("keyReleased %s, %d", e.getKeyChar(), e.getKeyCode());
                // 回车打的log是10。
                // 我们判断是不是回车。
                if (10 == e.getKeyCode()) {
                    onEnter();
                }
            }
        });
        frame.add(textField, BorderLayout.SOUTH);

        // 这是文本显示区域。
        textArea = new TextArea();
        frame.add(textArea, BorderLayout.NORTH); // 把这个输入狂加到frame里。


        frame.pack();

        frame.setVisible(true); // 下划线的表是这个方法过时了，点击去看下。

        // 查一下。看了一千的代码，原来忘记 设定这两个组件的未知了。
        // 另外 刚才把输入狂，和显示狂 搞错了。输入狂又错了。
        // 现在运行下看看。 按回车没反应。

    }

    // 定义方法，追加文本区域文字的。
    public void appendTextInTextArea(String text) {
        // 原先的文字。
        String originalText = textArea.getText();
        // 现在的。建议使用format格式花。
        String newText = String.format("%s \n %s", originalText, text);
        textArea.setText(newText);
    }

    private void onEnter() {
        // 把文字显示上去。
        appendTextInTextArea(textField.getText());

        // 发送消息。
        // 其实做为一个窗口来说，不必关心你是要发送还是要干啥，与UI无关的操作，不去处理。
        // 我们使用回调的方式，告知使用FrameContainer者。
        enterEventListener.onEnter(textField.getText());

        // 最后，输入狂的文字也要清除。
        textField.setText(null);
    }

    // 定义一个监听接口。
    public interface EnterEventListener {
        void onEnter(String text);
    }
}
