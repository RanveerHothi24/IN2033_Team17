import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimelinePanel extends JPanel {

    private final GUI gui;

    public TimelinePanel(GUI gui) {
        this.gui = gui;

        setLayout(null);
        setBounds(0,180,1080,360);

        JButton exit = new JButton("Exit");
        exit.setBounds(0,0,40,40);
        add(exit);

        exit.addActionListener(e -> {
            gui.remove(TimelinePanel.this);
            gui.repaint();
            gui.getCalendarPanel().noLongerVisible();
        });
    }
}
