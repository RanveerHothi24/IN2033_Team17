package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class MusicHallCalendar {

    // LOGIN DETAILS
    private final String username = "";
    private final String password = "";

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
    public final static Color backgroundColor = new Color(18,32,35);
    public final static Color primaryColor = new Color(46,204,65);
    public final static Color secondaryColor = Color.WHITE;

    public final static ImageIcon logo = new ImageIcon("OperationsAPI/src/assets/lmh_logo.png");

    // LOGIN PANEL STUFF
    private JTextField usernameField;
    private JPasswordField passwordField;
    private double animationSpeed;
    private Timer timer;

    public final static int width = 1080;
    private final static int height = 720;

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
        frame.setSize(width,height);
        frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, width, height);

        // MAIN PANEL
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        mainPanel.setBackground(backgroundColor);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);

        JLabel title = new JLabel("Lancaster's Music Hall");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(width/2, 30));
        titlePanel.add(title);

        Color[] legendColors = {Color.CYAN, Color.YELLOW , Color.MAGENTA,Color.GREEN};
        String[] legendText = {"Main Hall Available","Small Hall Available","Rehearsal Available","Fully Booked"};

        for (int i = 0; i < 4; i++){
            JPanel legendPanel = createLegendItem(legendColors[i], legendText[i]);
            legendPanel.setBackground(backgroundColor);
            titlePanel.add(legendPanel);
        }
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // CALENDAR PANEL
        calendarPanel = new CalendarPanel(this);
        calendarPanel.setPreferredSize(new Dimension(width*4/5, height));
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        // SIDE PANEL GONNA SLIDE IT/OUT
        sidePanel = new SidePanel(this);
        sidePanel.setBounds(-300, 0, 300, 720);

        JButton sideButton = new JButton("Side");
        sideButton.setBounds(0,0,30,30);
        sideButton.addActionListener(e -> {
            System.out.println("Button clicked");
            slidePanel(sidePanel, sidePanel.isOpen());
            if (contractsPanel.isOpen()) hideContractsPanel();
            if (sidePanel.isOpen()) calendarPanel.unpause();
            else calendarPanel.pause();
        });
        // CONTRACT SELECTION PANEL
        JPanel selectionPanel = createSelectionPanel();
        selectionPanel.setPreferredSize(new Dimension(width/5, height));
        selectionPanel.setBackground(backgroundColor);
        mainPanel.add(selectionPanel, BorderLayout.EAST);

        // CONTRACTS PANEL
        contractsPanel = new ContractsPanel();
        contractsPanel.setBounds(frame.getWidth()/3,0,frame.getWidth()*2/3,frame.getHeight());

        /* LEGEND PANEL
        legendPanel = new JPanel();
        legendPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/16));
        legendPanel.setBackground(primaryColor);
        mainPanel.add(legendPanel, BorderLayout.SOUTH);*/

        loginPanel = createLoginPanel();

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
        label.setForeground(secondaryColor);

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
    public void slidePanel(JPanel panel, boolean isOpen) {
        System.out.println("Animation started");
        int width = panel.getWidth();
        int targetX = isOpen ? -width : 0;
        int direction = isOpen ? -1 : 1;
        animationSpeed = 20 * direction;
        System.out.println(width + " " + targetX + " " + direction + " " + sidePanel.getX() + " " + sidePanel.isOpen());

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = sidePanel.getX();
                int remainingX = Math.abs(targetX - x);

                animationSpeed = Math.max(2, remainingX * 0.2) * direction;

                if ((direction > 0 && x >= targetX) || (direction < 0 && x <= targetX)) {
                    timer.stop();
                    sidePanel.setLocation(targetX, 0);
                    sidePanel.setOpen(!sidePanel.isOpen());
                } else {
                    sidePanel.setLocation(x + (int)animationSpeed, 0);
                    sidePanel.repaint();
                    layeredPane.repaint();
                }
            }
        });
        timer.start();
    }
    public void showContractsPanel(){
        layeredPane.add(contractsPanel,Integer.valueOf(4));
        layeredPane.repaint();
        contractsPanel.setOpen(true);
    }
    public void hideContractsPanel(){
        layeredPane.remove(contractsPanel);
        layeredPane.repaint();
        contractsPanel.setOpen(false);
    }
    private JPanel createLoginPanel() {
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

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals(username) && passwordField.getText().equals(password)) {
                slideOutLoginPanel();
                calendarPanel.unpause();
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect username or password");
            }
        });
        loginBox.add(loginButton);
        return loginPanel;
    }

    private JPanel createSelectionPanel(){
        JPanel selectionPanel = new JPanel();
        JLabel selectionLabel = new JLabel("Active contracts:");
        selectionLabel.setForeground(secondaryColor);
        java.util.List<JCheckBox> checkBoxList = new ArrayList<>();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(new Color(70,70,70));

        java.util.List<String> contracts = new ArrayList<>(java.util.List.of(ExampleData.companyNames));
        Collections.sort(contracts);
        for (String contract: contracts){
            JCheckBox checkBox = new JCheckBox(contract);
            checkBox.setBackground(new Color(70,70,70));
            checkBox.setForeground(secondaryColor);
            checkBox.setFont(new Font("Arial", Font.PLAIN, 12));

            checkBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    checkBox.setForeground(primaryColor);
                    checkBox.setFont(new Font("Arial", Font.BOLD, 12));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    checkBox.setForeground(secondaryColor);
                    checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
                }
            });

            checkBoxList.add(checkBox);
            selectionPanel.add(checkBox);
        }

        JScrollPane selectionScrollPane = new JScrollPane(selectionPanel);
        selectionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        selectionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        selectionScrollPane.setPreferredSize(new Dimension(150,400));
        selectionScrollPane.setBorder(BorderFactory.createLineBorder(MusicHallCalendar.backgroundColor,4));

        JButton applyBtn = new JButton("Apply");
        // CODE TO APPLY FILTER
        applyBtn.addActionListener(e -> {
            java.util.List<String> selectedFilters = new ArrayList<>();
            for (JCheckBox checkBox: checkBoxList){
                if (checkBox.isSelected()){
                    selectedFilters.add(checkBox.getText());
                }
            }
            System.out.println("applying filters: " + selectedFilters);
            // update calendar
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(selectionLabel, BorderLayout.NORTH);
        panel.add(selectionScrollPane, BorderLayout.CENTER);
        panel.add(applyBtn, BorderLayout.SOUTH);
        return panel;
    }
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
    public CalendarPanel getCalendarPanel() {
        return calendarPanel;
    }
}