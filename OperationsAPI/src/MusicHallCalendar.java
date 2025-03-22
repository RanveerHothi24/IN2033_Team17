import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicHallCalendar {
    private JFrame frame;
    private JPanel calendarPanel;
    private JComboBox<String> monthDropdown;
    private JComboBox<Integer> yearDropdown;
    private Map<Integer, String> eventDetails;

    public MusicHallCalendar() {
        frame = new JFrame("Lancaster’s Music Hall Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(173, 216, 230));

        JLabel sidebarTitle = new JLabel("Operations App");
        sidebarTitle.setFont(new Font("Arial", Font.BOLD, 16));
        sidebarPanel.add(sidebarTitle);

        JButton calendarButton = new JButton("📅 Calendar");
        calendarButton.setEnabled(false);
        JButton contractsButton = new JButton("🔍 Contracts");
        contractsButton.addActionListener(e -> openContractsPage());

        sidebarPanel.add(calendarButton);
        sidebarPanel.add(contractsButton);

        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("YEAR: "));
        yearDropdown = new JComboBox<>(new Integer[] { 2025 });
        headerPanel.add(yearDropdown);
        headerPanel.add(new JLabel("MONTH: "));
        monthDropdown = new JComboBox<>(new String[] { "January" });
        headerPanel.add(monthDropdown);

        JPanel legendPanel = new JPanel(new GridLayout(4, 1));
        legendPanel.add(createLegendItem("No Bookings", Color.LIGHT_GRAY));
        legendPanel.add(createLegendItem("Main Hall Booked", Color.CYAN));
        legendPanel.add(createLegendItem("Small Hall Booked", Color.YELLOW));
        legendPanel.add(createLegendItem("Fully Booked", Color.GREEN));

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(6, 7, 5, 5));

        eventDetails = new HashMap<>();
        eventDetails.put(5, "Fully Booked");
        eventDetails.put(6, "Small Hall Booked");
        eventDetails.put(11, "Main Hall Booked");
        eventDetails.put(12, "Fully Booked");
        eventDetails.put(17, "Small Hall Booked");
        eventDetails.put(22, "Fully Booked");

        updateCalendar();

        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(calendarPanel, BorderLayout.CENTER);
        frame.add(legendPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        String[] daysOfWeek = { "M", "T", "W", "T", "F", "S", "S" };
        for (String day : daysOfWeek) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            calendarPanel.add(label);
        }

        int firstDayOffset = 2;
        for (int i = 0; i < firstDayOffset; i++) {
            calendarPanel.add(new JLabel());
        }

        for (int i = 1; i <= 31; i++) {
            final int day = i;
            JButton dayButton = new JButton(String.valueOf(day));
            if (eventDetails.containsKey(day)) {
                switch (eventDetails.get(day)) {
                    case "Fully Booked":
                        dayButton.setBackground(Color.GREEN);
                        break;
                    case "Small Hall Booked":
                        dayButton.setBackground(Color.YELLOW);
                        break;
                    case "Main Hall Booked":
                        dayButton.setBackground(Color.CYAN);
                        break;
                }
            } else {
                dayButton.setBackground(Color.LIGHT_GRAY);
            }
            dayButton.addActionListener(e -> openDaySchedule(day));
            calendarPanel.add(dayButton);
        }
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel colorBox = new JLabel();
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        JLabel textLabel = new JLabel(text);
        panel.add(colorBox);
        panel.add(textLabel);
        return panel;
    }

    private void openDaySchedule(int day) {
        JFrame scheduleFrame = new JFrame("Schedule for " + day + " January 2025");
        scheduleFrame.setSize(800, 600);
        scheduleFrame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Schedule - " + day + " January 2025", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        scheduleFrame.add(title, BorderLayout.NORTH);

        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new GridLayout(0, 1));

        schedulePanel.add(createScheduleItem("The Main Hall", "Live Show - Lion King", Color.CYAN));
        schedulePanel.add(createScheduleItem("The Small Hall", "Theatre - Hamlet", Color.YELLOW));
        schedulePanel.add(createScheduleItem("Rehearsal Space", "Hamlet Rehearsal", Color.GREEN));
        schedulePanel.add(createScheduleItem("Rehearsal Space", "Lion King Rehearsal", Color.GREEN));

        scheduleFrame.add(new JScrollPane(schedulePanel), BorderLayout.CENTER);

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> scheduleFrame.dispose());
        scheduleFrame.add(backButton, BorderLayout.SOUTH);

        scheduleFrame.setVisible(true);
    }

    private JPanel createScheduleItem(String location, String event, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setBackground(color);

        JLabel locationLabel = new JLabel(location);
        locationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(locationLabel, BorderLayout.NORTH);

        JLabel eventLabel = new JLabel(event);
        eventLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(eventLabel, BorderLayout.CENTER);

        return panel;
    }

    private void openContractsPage() {
        JFrame contractsFrame = new JFrame("Contracts Page");
        contractsFrame.setSize(800, 600);
        contractsFrame.setLayout(new BorderLayout());
        JLabel title = new JLabel("CONTRACTS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        contractsFrame.add(title, BorderLayout.NORTH);
        contractsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicHallCalendar::new);
    }
}