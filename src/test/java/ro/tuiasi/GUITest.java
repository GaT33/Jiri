package ro.tuiasi;

import com.formdev.flatlaf.FlatLightLaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class GUITest {

    private GUI gui;
    private JFrame frame;

    @Test
    public void testGUICreation() {
        gui = new GUI();
        assertNotNull(gui);
    }

    @Test
    public void testEmailValidation() throws Exception {
        Method method = GUI.class.getDeclaredMethod("isEmailValid", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(gui, "test@gmail.com"));
        assertTrue((Boolean) method.invoke(gui, "test@yahoo.com"));
        assertTrue((Boolean) method.invoke(gui, "test@hotmail.com"));
        assertFalse((Boolean) method.invoke(gui, "test@unknown.com"));
        assertFalse((Boolean) method.invoke(gui, "invalidemail"));
    }

    @Test
    public void testPasswordValidation() throws Exception {
        Method method = GUI.class.getDeclaredMethod("isPasswordValid", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(gui, "Password1!"));
        assertFalse((Boolean) method.invoke(gui, "pass"));
        assertFalse((Boolean) method.invoke(gui, "password1"));
        assertFalse((Boolean) method.invoke(gui, "Password"));
    }

    @Test
    public void testKeyValidation() throws Exception {
        Method method = GUI.class.getDeclaredMethod("isKeyValid", String.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(gui, "sk-test-1234567890abcdefghijklmnopqrstuv"));
        assertFalse((Boolean) method.invoke(gui, "sk-test-123"));
        assertFalse((Boolean) method.invoke(gui, "1234567890abcdefghijklmnopqrstuv"));
    }
}
