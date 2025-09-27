import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Main {
    public static boolean enabled = true;

    public static void main(String[] args) throws AWTException, NativeHookException {
        KeyBind keyBind = new KeyBind();
        Robot robot = new Robot();
        Random random = new Random();

        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(keyBind);

        new Thread(() -> {
            boolean isClicking = false;
            long nextReleaseTime = 0;
            long nextClickTime = 0;

            while (enabled) {
                long now = System.currentTimeMillis();

                if (keyBind.wasPressed) {
                    // Time to start a new click
                    if (!isClicking && now >= nextClickTime) {
                        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                        isClicking = true;

                        // Random hold time: 450-550 ms
                        nextReleaseTime = now + 400 + random.nextInt(51);
                    }

                    // Time to release the click
                    if (isClicking && now >= nextReleaseTime) {
                        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                        isClicking = false;

                        // Random delay before next click: 480-520 ms
                        nextClickTime = now + 430 + random.nextInt(41);
                    }
                } else {
                    // Release mouse immediately if key is not pressed
                    if (isClicking) {
                        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                        isClicking = false;
                    }
                    nextClickTime = now; // reset next click
                }

                try {
                    Thread.sleep(1); // tiny sleep to reduce CPU usage
                } catch (InterruptedException ignored) {}
                if(keyBind.configMode){

                }
            }
        }).start();
    }
}
