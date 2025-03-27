package gui;

import api.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimelinePanel extends JPanel {

    private CalendarPanel.DayPanel dayPanel;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private final String[] roomNames = {"Main","Small","Rehearsal","M1","M2","M3","M4","M5"};
    private final LocalDate date;

    private final List<Event> events;

    // FOR TESTING
    final Random rand = new Random();

    public TimelinePanel(CalendarPanel.DayPanel dayPanel) {
        this.dayPanel = dayPanel;
        date = LocalDate.of(2025,dayPanel.getMonth(),dayPanel.getDay());

        // EXAMPLE DATA
        events = new ArrayList<>();
        for (int i = 0; i < ExampleData.events.length; i++) {
            if (ExampleData.events[i].getEventStartTime().toLocalDate().equals(date)) {
                events.add(ExampleData.events[i]);
            }
        }

        setBounds(0,60,1080,620);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TOP PANEL (VIEWER)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/2));
        topPanel.setBackground(Color.YELLOW);

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.GRAY);

        JLabel titleLabel = new JLabel("Timeline Panel");
        titleLabel.setPreferredSize(new Dimension(100, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topHeader.add(titleLabel, BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(30, 20));
        exitButton.addActionListener(e -> {
            dayPanel.getCalendar().getLayeredPane().remove(this);
            dayPanel.getCalendar().getLayeredPane().repaint();
        });
        topHeader.add(exitButton, BorderLayout.WEST);

        topPanel.add(topHeader, BorderLayout.NORTH);

        // VIEWER PANEL TO VIEW SEAT CONFIG
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.setPreferredSize(new Dimension(300,300));
        viewPanel.setBackground(Color.DARK_GRAY);
        topPanel.add(viewPanel);

        JLabel viewerInfo = new JLabel("Seat config here");
        viewerInfo.setPreferredSize(new Dimension(100, 100));
        viewerInfo.setForeground(Color.WHITE);
        viewerInfo.setHorizontalAlignment(SwingConstants.CENTER);
        viewerInfo.setFont(new Font("Arial", Font.BOLD, 30));
        viewPanel.add(viewerInfo, BorderLayout.CENTER);

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 300));
        leftPanel.setBackground(Color.BLACK);
        topPanel.add(leftPanel, BorderLayout.WEST);

        JLabel leftInfo = new JLabel("Event info here");
        leftInfo.setPreferredSize(new Dimension(100, 100));
        leftInfo.setForeground(Color.WHITE);
        leftInfo.setHorizontalAlignment(SwingConstants.CENTER);
        leftInfo.setFont(new Font("Arial", Font.BOLD, 20));
        leftPanel.add(leftInfo);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 300));
        rightPanel.setBackground(Color.BLACK);
        topPanel.add(rightPanel, BorderLayout.EAST);

        JLabel rightInfo = new JLabel("More info here");
        rightInfo.setPreferredSize(new Dimension(100, 100));
        rightInfo.setForeground(Color.WHITE);
        rightInfo.setHorizontalAlignment(SwingConstants.CENTER);
        rightInfo.setFont(new Font("Arial", Font.BOLD, 20));
        rightPanel.add(rightInfo);

        add(topPanel);

        // BOTTOM PANEL (TIMELINE)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/2));
        bottomPanel.setBackground(Color.BLACK);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setPreferredSize(new Dimension(3000, getHeight()/2-20));
        contentPanel.setBackground(Color.GREEN);

        JPanel timesPanel = new JPanel();
        timesPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        String[] times = {"23:00","22:00","21:00","20:00","19:00","18:00","17:00","16:00","15:00","14:00","13:00","12:00","11:00","10:00"};
        for (int i = 0; i < times.length; i++) {
            JLabel label = new JLabel(times[i]);
            label.setPreferredSize(new Dimension(3000/14,20));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            timesPanel.add(label,FlowLayout.LEFT);
        }
        timesPanel.setPreferredSize(new Dimension(3000,20));
        contentPanel.add(timesPanel);
        for (int i = 0; i < roomNames.length; i++) {
            JPanel roomTimeline = createRoomTimeline(ExampleData.roomIds[i]);
            roomTimeline.setPreferredSize(new Dimension(3000,100));
            contentPanel.add(roomTimeline);
        }

        JPanel roomNamePanel = new JPanel();
        roomNamePanel.setLayout(new BoxLayout(roomNamePanel, BoxLayout.Y_AXIS));
        JLabel test = new JLabel("Room Name");
        test.setPreferredSize(new Dimension(100,20));
        roomNamePanel.add(test);
        for (int i = 0; i < roomNames.length; i++) {
            roomNamePanel.add(createRoomName(roomNames[i] + " "));
        }

        JPanel filler = new JPanel();
        filler.setPreferredSize(new Dimension(100, 17));
        filler.setBackground(Color.WHITE);
        roomNamePanel.add(filler);

        bottomPanel.add(roomNamePanel, BorderLayout.WEST);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(100);
        //scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //addMouseDragScrolling();

        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel);

    }
    private JPanel createRoomTimeline(int roomId) {
        JPanel roomTimeline = new JPanel();
        roomTimeline.setLayout(null);

        roomTimeline.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        int duration;

        for (Event event : events) {
            if (event.getRoomId() == roomId) {
                int multiplier = 3000/14;
                int start = (event.getEventStartTime().getHour() - 10) * multiplier;
                double width = (double)(event.getEventDuration().toMinutes() / 60) * multiplier;
                System.out.println(roomId + " " + event.getEventStartTime() + " " + event.getEventDuration() + " " + date);

                JPanel eventPanel = new JPanel();
                //eventPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                eventPanel.setBounds(start, 5, (int)width, 25);
                eventPanel.setBackground(Color.GREEN);
                roomTimeline.add(eventPanel);

                eventPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Event clicked");
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        eventPanel.setBackground(Color.GREEN.darker());
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        eventPanel.setBackground(Color.GREEN);
                    }
                });
            }
        }
        return roomTimeline;
    }
    private JPanel createRoomName(String roomName) {

        JPanel roomNamePanel = new JPanel();
        roomNamePanel.setLayout(new BorderLayout());
        roomNamePanel.setBackground(Color.WHITE);
        roomNamePanel.setPreferredSize(new Dimension(100,30));
        roomNamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel roomNameLabel = new JLabel(roomName);
        roomNameLabel.setPreferredSize(new Dimension(100,30));
        roomNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        roomNameLabel.setForeground(Color.BLACK);
        roomNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        roomNamePanel.add(roomNameLabel,BorderLayout.CENTER);

        return roomNamePanel;
    }

    public boolean[] getRoomAvailability(){
        boolean[] availability = {true,true,true};
        for (Event event : events) {
            if (event.getRoomId() == 1) availability[0] = false;
            if (event.getRoomId() == 2) availability[1] = false;
            if (event.getRoomId() == 3) availability[2] = false;
        }
        return availability;
    }
}
