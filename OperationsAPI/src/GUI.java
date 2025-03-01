import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private final ImageIcon logo = new ImageIcon("src/assets/cblooded1.jpeg");
    private final CalendarPanel calendarPanel;

    public GUI() {
        setTitle("Operations Calendar");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(logo.getImage());
        getContentPane().setBackground(new Color(128, 0, 32));

        setLayout(null);

        calendarPanel = new CalendarPanel(this);
        add(calendarPanel);
    }

    public CalendarPanel getCalendarPanel() {
        return calendarPanel;
    }
}
