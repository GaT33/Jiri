package ro.tuiasi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class InterfataChatTest {

    private InterfataChat interfataChat;
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        interfataChat = new InterfataChat();
    }

    @Test
    public void testInterfataChatCreation() {
        assertNotNull(interfataChat);
    }
}
