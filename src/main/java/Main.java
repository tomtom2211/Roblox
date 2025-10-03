import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main {
    public static boolean enabled = true;

    public static void main(String[] args) throws AWTException, NativeHookException {
        KeyBind keyBind = new KeyBind();
        Robot robot = new Robot();
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(keyBind);
        Rectangle detectionRegion = new Rectangle(1536,610,50,30);
        int width = detectionRegion.width;
        int height = detectionRegion.height;
        new Thread(() -> {
            while(enabled) {
                if (keyBind.wasPressed) {
                    BufferedImage screenshot = robot.createScreenCapture(detectionRegion);
                    for(int x = 0; x < width ; x++){
                        for(int y = 0; y < height ; y++){
                            int rgb = screenshot.getRGB(x,y);
                            Color color = new Color(rgb);
                            if(color.getRed() > 250 && color.getGreen() > 250 && color.getBlue() > 250){
                                robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                            }
                        }
                    }
                }
                try{
                    Thread.sleep(1);
                }
                catch(Exception ignored){}
            }
        }).start();
    }
}
