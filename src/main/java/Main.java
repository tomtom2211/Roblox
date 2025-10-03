import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main {
    public static boolean enabled = true;
    public static int xLoc = 521;
    public static int yLoc = 635;
    public static Rectangle detectionRegion = new Rectangle(xLoc,yLoc,5,5);
    public static void main(String[] args) throws AWTException, NativeHookException {
        KeyBind keyBind = new KeyBind();
        Robot robot = new Robot();
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(keyBind);
        new Thread(() -> {
            int width = detectionRegion.width;
            int height = detectionRegion.height;
            while(enabled) {
                if (keyBind.wasPressed) {
                    BufferedImage screenshot = robot.createScreenCapture(detectionRegion);
                    //System.out.println(xLoc + " " + yLoc);
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
                if (keyBind.configMode){
                    try{
                        xLoc = MouseInfo.getPointerInfo().getLocation().x;
                        yLoc = MouseInfo.getPointerInfo().getLocation().y;
                        detectionRegion = new Rectangle(xLoc,yLoc,5,5);
                        System.out.println(xLoc + " " + yLoc);
                        Thread.sleep(1000);
                    }
                    catch(Exception ignored){}
                }
                try{
                    Thread.sleep(1);
                }
                catch(Exception ignored){}
            }
        }).start();
    }
}
