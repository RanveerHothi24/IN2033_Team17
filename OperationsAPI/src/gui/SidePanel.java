package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidePanel extends JPanel {

    private boolean isOpen = false;
    private final MusicHallCalendar calendar;

    public SidePanel(MusicHallCalendar calendar) {
        this.calendar = calendar;

        setBackground(MusicHallCalendar.backgroundColor);
        setBorder(BorderFactory.createLineBorder(MusicHallCalendar.primaryColor));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Image logo = MusicHallCalendar.logo.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoLabel);

        JLabel title = new JLabel("Operations");
        title.setPreferredSize(new Dimension(getWidth(),80));
        title.setFont(new Font("Arial",Font.BOLD,40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        JLabel calendarLbl = new JLabel("Calendar");
        calendarLbl.setFont(new Font("Arial",Font.BOLD,40));
        calendarLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarLbl.setPreferredSize(new Dimension(getWidth(),80));
        add(calendarLbl);

        JLabel contractsLbl = new JLabel("Contracts");
        contractsLbl.setFont(new Font("Arial",Font.BOLD,40));
        contractsLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        contractsLbl.setPreferredSize(new Dimension(getWidth(),80));

        contractsLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                calendar.showContractsPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                contractsLbl.setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contractsLbl.setForeground(Color.BLACK);
            }
        });

        add(contractsLbl);

    }
    public boolean isOpen() {
        return isOpen;
    }
    public void setOpen(boolean open) {
        this.isOpen = open;
    }
}
