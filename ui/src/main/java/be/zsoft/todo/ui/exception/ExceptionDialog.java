package be.zsoft.todo.ui.exception;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionDialog {

    public static void show(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        JTextArea textArea = new JTextArea(stackTrace);
        textArea.setEditable(false);
        textArea.setRows(10);
        textArea.setColumns(50);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(null, new Object[]{e.getMessage(), scrollPane}, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
