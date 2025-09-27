import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyBind implements NativeKeyListener {
    boolean wasPressed = false;
    boolean configMode = false;
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE){
            try {
                Main.enabled = !Main.enabled;
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
            //System.out.println("Key was wasPressed!");
            //System.out.println(Main.enabled);;
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_X){
            wasPressed = !wasPressed;
        }
        if(e.getKeyCode() == NativeKeyEvent.VC_C){
            configMode = !configMode;
        }
    }
}
