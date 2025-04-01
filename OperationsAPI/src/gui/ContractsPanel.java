package gui;

import javax.swing.*;
import java.awt.*;

public class ContractsPanel extends JPanel {

    private boolean isOpen = false;

    public ContractsPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(MusicHallCalendar.primaryColor);

        JLabel title = new JLabel("Contracts");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);

    }
    public boolean isOpen() {
        return isOpen;
    }
    public void setOpen(boolean open) {
        this.isOpen = open;
    }
}
