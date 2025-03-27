package gui;

import javax.swing.*;
import java.awt.*;

public class ContractsPanel extends JPanel {

    private boolean inFrame = false;

    public ContractsPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(MusicHallCalendar.primaryColor);

        JLabel title = new JLabel("Contracts");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);

    }
    public boolean isInFrame() {
        return inFrame;
    }
    public void setInFrame(boolean inFrame) {
        this.inFrame = inFrame;
    }
}
