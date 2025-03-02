import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalendarPanel extends JPanel {
    private final GUI gui;

    private boolean timelineVisible = false;

    public CalendarPanel(GUI gui) {
        this.gui = gui;
        //setSize(800, 600);
        setBounds(40,40, gui.getWidth()-80, gui.getHeight()-120);
        setLayout(new GridLayout(4,7));

        for (int day = 1; day <= 28; day++) {
            add(new DayPanel(1,day));
        }
    }
    private class DayPanel extends JPanel {

        private final int month;
        private final int day;
        private final TimelinePanel timelinePanel;

        public DayPanel(int day, int month){

            this.day = day;
            this.month = month;
            timelinePanel = new TimelinePanel(gui);

            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.black));
            setLayout(new BorderLayout());

            JLabel dayLabel = new JLabel(day + "/" + month);
            add(dayLabel, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!timelineVisible) {
                        gui.add(timelinePanel);
                        timelinePanel.repaint();
                        timelineVisible = true;
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!timelineVisible) {
                        setBackground(Color.WHITE);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!timelineVisible) {
                        setBackground(Color.LIGHT_GRAY);
                    }
                }
            });
        }
    }
    public void noLongerVisible() {
        timelineVisible = false;
    }
}
