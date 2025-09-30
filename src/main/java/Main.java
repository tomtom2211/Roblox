import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Main {
    public static boolean enabled = true;

    public static void main(String[] args) throws AWTException, NativeHookException {
        KeyBind keyBind = new KeyBind();
        Robot robot = new Robot();

        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(keyBind);

        new Thread(() -> {
            long fillDelay = 350; // Change
            int panDelay = 5000; // Change
            int moveTime = 1500; // Change
            int panTimes = 4; // Change
            int pannedCounter = 0;
            int cycleMacroMode = -1;
            int cycleHelperMode = -1;
            int cycleConfigMode = -1;
            boolean isHeld = false;
            boolean isMoving = false;
            long nextClick = 0;
            long nextReleaseClick = 0;
            long nextStop = 0;
            long now;
            while(Main.enabled){
                try {
                    Thread.sleep(1);
                    now = System.currentTimeMillis();

                    if(keyBind.wasPressed && !keyBind.fullMacro && !keyBind.configMode){
                        switch (cycleHelperMode){
                            case -1:
                                pannedCounter = 0;
                                nextClick = 0;
                                nextReleaseClick = 0;
                                nextStop = 0;
                                cycleHelperMode = 0;
                                break;
                            case 0:
                                if (now > nextClick && !isHeld) {
                                    robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                                    nextReleaseClick = now + fillDelay;
                                    isHeld = true;
                                } else if (now > nextReleaseClick && isHeld) {
                                    robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                                    pannedCounter++;
                                    isHeld = false;
                                    nextClick = now + 1000;}
                                break;
                            default:
                                System.out.println("Error with switch() at helper!");
                                break;
                        }
                    }
                    else if(!keyBind.wasPressed && !keyBind.fullMacro && keyBind.configMode){
                        while(keyBind.configMode) {
                            isHeld = true;
                            robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                            fillDelay = System.currentTimeMillis() - now;
                            try{
                                Thread.sleep(1);
                            }
                            catch (InterruptedException e){
                                System.out.println("Failed to set thread to sleep in config mode!");
                            }
                        }
                    }
                    else if (keyBind.fullMacro && !keyBind.wasPressed && !keyBind.configMode) {
                        switch (cycleMacroMode) {
                            case -1:
                                pannedCounter = 0;
                                nextClick = 0;
                                nextReleaseClick = 0;
                                nextStop = 0;
                                cycleMacroMode = 0;
                            case 0: // Digging phase
                                if (now > nextClick && !isHeld) {
                                    robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                                    nextReleaseClick = now + fillDelay;
                                    isHeld = true;
                                } else if (now > nextReleaseClick && isHeld) {
                                    robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                                    pannedCounter++;
                                    isHeld = false;
                                    nextClick = now + 1000;
                                    if (pannedCounter == panTimes) {
                                        cycleMacroMode = 1;
                                        pannedCounter = 0;
                                    }
                                }
                                break;
                            case 1: // Move towards the lava, stop
                                if (!isMoving) {
                                    robot.keyPress(KeyEvent.VK_W);
                                    isMoving = true;
                                    nextStop = now + moveTime + 15;
                                } else if (now > nextStop) {
                                    robot.keyRelease(KeyEvent.VK_W);
                                    isMoving = false;
                                    nextClick = now + 500;
                                    cycleMacroMode = 2;
                                }
                                break;
                            case 2:
                                if (now > nextClick && !isHeld) {
                                    robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                                    try{
                                        Thread.sleep(150);
                                    }
                                    catch (Exception ignored){}
                                    robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                                    robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                                    isHeld = true;
                                    nextReleaseClick = now + panDelay;
                                }
                                else if (isHeld && now > nextReleaseClick) {
                                    robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                                    isHeld = false;
                                    cycleMacroMode = 3;
                                }
                                break;
                            case 3:
                                if (!isMoving) {
                                    nextStop = now + moveTime;
                                    isMoving = true;
                                    robot.keyPress(KeyEvent.VK_S);
                                } else if (now > nextStop) {
                                    robot.keyRelease(KeyEvent.VK_S);
                                    isMoving = false;
                                    cycleMacroMode = 0;
                                }
                                break;
                            default:
                                System.out.println("Error with switch() at macro!");
                        }
                    }
                    else{
                        if(isHeld) {
                            robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                            isHeld=false;
                        }
                        if(isMoving){
                            robot.keyRelease(KeyEvent.VK_W);
                            robot.keyRelease(KeyEvent.VK_S);
                            isMoving = false;
                        }
                    }
                }
                catch (Exception ignored){}
            }
        }).start();
    }
}
