package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicHallCalendar {
    // FRAMES & PANELS
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JLayeredPane layeredPane;
    private CalendarPanel calendarPanel;
    private SidePanel sidePanel;
    private JPanel legendPanel;
    private TimelinePanel timelinePanel;
    private ContractsPanel contractsPanel;

    // COLOURS
    public final static Color backgroundColor = Color.WHITE;
    public final static Color primaryColor = Color.CYAN;
    public final static Color secondaryColor = Color.GREEN;

    // LOGIN PANEL STUFF
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private double animationSpeed;
    private Timer timer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MusicHallCalendar().createGUI();
        } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void createGUI(){
        // MAIN FRAME
        frame = new JFrame("Music Hall Calendar");
        frame.setSize(1080,720);
        //frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1080, 720);

        // MAIN PANEL
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        mainPanel.setBackground(backgroundColor);


        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel title = new JLabel("Lancaster's Music Hall");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        title.setPreferredSize(new Dimension(500, 30));
        titlePanel.add(title);

        Color[] legendColors = {Color.CYAN, Color.YELLOW , Color.MAGENTA,Color.GREEN};
        String[] legendText = {"Main Hall Available","Small Hall Available","Rehearsal Available","Fully Booked"};

        for (int i = 0; i < 4; i++){
            JPanel legendPanel = createLegendItem(legendColors[i], legendText[i]);
            titlePanel.add(legendPanel);
        }
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // CALENDAR PANEL
        calendarPanel = new CalendarPanel(this);
        calendarPanel.setPreferredSize(new Dimension(1080/2, 720/2));
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        // TIMELINE PANEL STUFF
        //timelinePanel = new TimelinePanel(th);
        //timelinePanel.setBounds(0,50,frame.getWidth(),frame.getHeight()-100);

        // SIDE PANEL GONNA SLIDE IT/OUT
        sidePanel = new SidePanel(this);
        sidePanel.setBounds(-300, 0, 300, 720);

        JButton sideButton = new JButton("Side");
        sideButton.setBounds(0,0,30,30);
        sideButton.addActionListener(e -> {
            System.out.println("Button clicked");
            slidePanel(sidePanel, sidePanel.isInFrame());
            if (contractsPanel.isInFrame()) hideContractsPanel();
            if (sidePanel.isInFrame()) calendarPanel.unpause();
            else calendarPanel.pause();
        });

        // CONTRACTS PANEL
        contractsPanel = new ContractsPanel();
        contractsPanel.setBounds(frame.getWidth()/3,0,frame.getWidth()*2/3,frame.getHeight());

        // LEGEND PANEL
        legendPanel = new JPanel();
        legendPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/16));
        legendPanel.setBackground(primaryColor);
        mainPanel.add(legendPanel, BorderLayout.SOUTH);

        // LOGIN PANEL
        loginPanel = new JPanel(null);
        loginPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        loginPanel.setBackground(primaryColor);

        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBounds(loginPanel.getWidth()/2 - 100, loginPanel.getHeight()/2 - 50, 200, 100);
        loginBox.setBackground(Color.yellow);
        loginPanel.add(loginBox,BorderLayout.CENTER);

        // USERNAME
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setPreferredSize(new Dimension(100, 30));
        loginBox.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(100, 30));
        loginBox.add(usernameField);

        // PASSWORD
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        loginBox.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(100, 30));
        loginBox.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals("") && passwordField.getText().equals("")) {
                slideOutLoginPanel();
                calendarPanel.unpause();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect username or password");
            }
        });
        loginBox.add(loginButton);

        // ADDING AND ORDERING PANELS
        layeredPane.add(mainPanel,Integer.valueOf(0));
        layeredPane.add(sideButton,Integer.valueOf(1));
        //layeredPane.add(timelinePanel,Integer.valueOf(2));
        layeredPane.add(sidePanel,Integer.valueOf(3));
        //layeredPane.add(contractsPanel,Integer.valueOf(4));
        layeredPane.add(loginPanel,Integer.valueOf(5));

        frame.add(layeredPane);

        frame.setVisible(true);
    }
    private JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);

        itemPanel.add(colorBox);
        itemPanel.add(label);
        return itemPanel;
    }
    private void slideOutLoginPanel() {
        animationSpeed = 0.1;
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginPanel.getY() > - loginPanel.getHeight()) {
                    loginPanel.setLocation(loginPanel.getX(), loginPanel.getY() - (int)animationSpeed);
                    animationSpeed = animationSpeed * 1.5;
                    layeredPane.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
    public void slidePanel(JPanel panel, boolean inFrame) {
        System.out.println("Animation started");
        int width = panel.getWidth();
        int targetX = inFrame ? -width : 0;
        int direction = inFrame ? -1 : 1;
        animationSpeed = 20 * direction;
        System.out.println(width + " " + targetX + " " + direction + " " + sidePanel.getX() + " " + sidePanel.isInFrame());

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = sidePanel.getX();
                int remainingX = Math.abs(targetX - x);

                animationSpeed = Math.max(2, remainingX * 0.2) * direction;

                if ((direction > 0 && x >= targetX) || (direction < 0 && x <= targetX)) {
                    timer.stop();
                    sidePanel.setLocation(targetX, 0);
                    sidePanel.setInFrame(!sidePanel.isInFrame());
                } else {
                    sidePanel.setLocation(x + (int)animationSpeed, 0);
                    sidePanel.repaint();
                    layeredPane.repaint();
                }
            }
        });
        timer.start();
    }

    public void showTimelinePanel(){
        layeredPane.add(timelinePanel,Integer.valueOf(2));
        layeredPane.repaint();
    }
    public void hideTimelinePanel(){
        layeredPane.remove(timelinePanel);
        layeredPane.repaint();
    }
    public void showContractsPanel(){
        layeredPane.add(contractsPanel,Integer.valueOf(4));
        layeredPane.repaint();
        contractsPanel.setInFrame(true);
    }
    public void hideContractsPanel(){
        layeredPane.remove(contractsPanel);
        layeredPane.repaint();
        contractsPanel.setInFrame(false);
    }
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
    public CalendarPanel getCalendarPanel() {
        return calendarPanel;
    }
}