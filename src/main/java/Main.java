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
            boolean isHeld = false;
            long nextReleaseTime = 0;
            long nextClickTime = 0;
            int delay = 390;
            int randomizer = 15;
            int cooldown = 30;

            while(enabled) {
                long currentTime = System.currentTimeMillis();
                if (keyBind.wasPressed) {
                    if (!isHeld && currentTime >= nextClickTime) {
                        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                        isHeld = true;
                        nextReleaseTime = currentTime + delay + random.nextInt(randomizer);
                    }
                    if (isHeld && currentTime >= nextReleaseTime) {
                        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                        isHeld = false;
                        nextClickTime = currentTime + delay + cooldown + random.nextInt(randomizer);
                    }
                }
                else{
                    if(isHeld){
                        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                        isHeld = false;
                    }
                    nextClickTime = currentTime;
                }
                try{
                    Thread.sleep(1);
                }
                catch (InterruptedException e){
                    System.out.println("Failed to set thread to sleep!");
                }
            }
        }).start();
    }
}
