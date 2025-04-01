package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;

public class CalendarPanel extends JPanel {

    private final MusicHallCalendar calendar;
    private LocalDate today;
    private final CardLayout cardLayout;
    private int currentMonth;
    private int currentYear;

    private boolean isPaused = true;

    // FOR TESTING
    private Random r = new Random();

    public CalendarPanel(MusicHallCalendar calendar) {
        this.calendar = calendar;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        today = LocalDate.now();
        currentMonth = today.getMonthValue();
        currentYear = today.getYear();

        for (int i = 0; i <= 48; i++){
            int month = currentMonth + i;
            int year = currentYear;

            if (month < 1) {
                month = 12;
                year--;
            } else if (month > 12) {
                month = 1;
                year++;
            }
            add(createMonthPanel(month, year), month + " - " + year);

            cardLayout.show(this, currentMonth + " - " + currentYear);
        }
    }
    private JPanel createMonthPanel(int month, int year) {
        JPanel monthPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());

        String title = YearMonth.of(year, month).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year;
        JLabel monthLabel = new JLabel(title);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 15));
        monthLabel.setForeground(Color.WHITE);

        JPanel dayInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        dayInfoPanel.setBackground(new Color(70,70,70));
        String[] dayNames = {"Sun","Sat","Fri","Thu","Wed","Tue","Mon"};
        for (int i = 0; i < dayNames.length; i++) {
            JLabel dayInfo = new JLabel(dayNames[i]);
            dayInfo.setHorizontalAlignment(SwingConstants.CENTER);
            dayInfo.setPreferredSize(new Dimension((MusicHallCalendar.width*4/5)/7, 20));
            dayInfo.setBorder(BorderFactory.createLineBorder(MusicHallCalendar.backgroundColor,4));
            dayInfo.setForeground(Color.WHITE);
            dayInfoPanel.add(dayInfo, FlowLayout.LEFT);
        }
        headerPanel.add(monthLabel, BorderLayout.NORTH);
        headerPanel.add(dayInfoPanel, BorderLayout.SOUTH);
        headerPanel.setBackground(MusicHallCalendar.backgroundColor);

        monthPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(5, 7));
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();

        int totalCells = 35;
        int day = 1;

        for (int i = 1; i <= totalCells; i++) {
            if (i < firstDayOfWeek || day > daysInMonth) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setBackground(MusicHallCalendar.backgroundColor);
                daysPanel.add(emptyPanel);
            } else {
                daysPanel.add(new DayPanel(day, month));
                day++;
            }
        }

        JButton nextButton = new JButton("Next");
        JButton previousButton = new JButton("Previous");

        nextButton.addActionListener(e -> switchMonth(1));
        previousButton.addActionListener(e -> switchMonth(-1));

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(MusicHallCalendar.backgroundColor);

        if (month == currentMonth) {
            controlPanel.add(nextButton, BorderLayout.SOUTH);
        } else if (month == currentMonth + 6 || (month == 5 && currentMonth == 12)) {
            controlPanel.add(previousButton, BorderLayout.SOUTH);
        } else {
            controlPanel.add(previousButton, BorderLayout.SOUTH);
            controlPanel.add(nextButton, BorderLayout.SOUTH);
        }
        monthPanel.add(controlPanel, BorderLayout.SOUTH);

        monthPanel.add(daysPanel, BorderLayout.CENTER);
        return monthPanel;
    }
    private void switchMonth(int offset){
        currentMonth += offset;

        if (currentMonth < 1){
            currentMonth = 12;
            currentYear--;
        } else if (currentMonth > 12){
            currentMonth = 1;
            currentYear++;
        }
        cardLayout.show(this, currentMonth + " - " + currentYear);
    }

    public class DayPanel extends JPanel {

        private final TimelinePanel timelinePanel;
        private boolean[] roomAvailability;
        private JPanel availablePanel;

        final int day;
        final int month;
        final Color color;

        public DayPanel(int day, int month) {

            this.day = day;
            this.month = month;

            timelinePanel = new TimelinePanel(this);
            roomAvailability = timelinePanel.getRoomAvailability();
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(MusicHallCalendar.backgroundColor,4));

            availablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            color = new Color(70,70,70);

            /*if (!roomAvailability[0] && !roomAvailability[1] && !roomAvailability[2]) availablePanel.add(createColorBox(Color.GREEN));
            else {
                if (roomAvailability[0]) availablePanel.add(createColorBox(Color.CYAN));
                if (roomAvailability[1]) availablePanel.add(createColorBox(Color.YELLOW));
                if (roomAvailability[2]) availablePanel.add(createColorBox(Color.MAGENTA));
            }

            availablePanel.setBackground(color);
            add(availablePanel, BorderLayout.NORTH);*/

            setBackground(color);

            JLabel dayLabel = new JLabel(day + " / " + month);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayLabel.setForeground(Color.WHITE);
            add(dayLabel, BorderLayout.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!isPaused) {
                        calendar.getLayeredPane().add(timelinePanel, Integer.valueOf(2));
                        pause();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isPaused) {
                        availablePanel.setBackground(color.darker());
                        setBackground(color.darker());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isPaused) {
                        availablePanel.setBackground(color);
                        setBackground(color);
                    }
                }
            });
        }
        private JPanel createColorBox(Color color) {
            JPanel colorBox = new JPanel();
            colorBox.setBackground(color);
            colorBox.setBorder(BorderFactory.createLineBorder(Color.black));
            colorBox.setPreferredSize(new Dimension(15, 15));
            return colorBox;
        }
        public void resetColor() {
            availablePanel.setBackground(color);
            setBackground(color);
        }
        public void highlight(String filter, Color color) {
            // if day belongs to contract, highlight it with colour
        }
        public int getDay() {
            return day;
        }
        public int getMonth() {
            return month;
        }
        public MusicHallCalendar getCalendar() {
            return calendar;
        }
    }
    public void pause(){
        isPaused = true;
    }
    public void unpause(){
        isPaused = false;
    }
}
