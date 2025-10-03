import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyBind implements NativeKeyListener {
    boolean wasPressed = false;
    boolean configMode = false;
    boolean fullMacro = false;
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE){
            try {
                Main.enabled = !Main.enabled;
                GlobalScreen.unregisterNativeHook();
                System.out.println("Successfully stopped the program!");
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_X){
            wasPressed = !wasPressed;
            System.out.println("Toggled the helper to: " + wasPressed);
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_C){
            configMode = !configMode;
        }
    }
}
