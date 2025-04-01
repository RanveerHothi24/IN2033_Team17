package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientGUI {

    private static List<Client> clients = new ArrayList<>();
    private static List<Client> displayedClients = new ArrayList<>(); // For search results
    private static JPanel clientListPanel;
    private static JFrame frame;
    private static JTextField searchField;
    private static JComboBox<String> sortComboBox;

    private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Alice Blue
    private static final Color ACCENT_COLOR = new Color(255, 140, 0); // Dark Orange

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::createAndShowGUI);
    }

    private static class Client {
        String companyName;
        String contactName;
        String contactEmail;
        String phoneNumber;
        String streetAddress;
        String city;
        String postcode;
        String billingName;
        String billingEmail;

        Client(String companyName, String contactName, String contactEmail, String phoneNumber, String streetAddress, String city, String postcode, String billingName, String billingEmail) {
            this.companyName = companyName;
            this.contactName = contactName;
            this.contactEmail = contactEmail;
            this.phoneNumber = phoneNumber;
            this.streetAddress = streetAddress;
            this.city = city;
            this.postcode = postcode;
            this.billingName = billingName;
            this.billingEmail = billingEmail;
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Client Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null); // Center the frame

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SECONDARY_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Clients", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Search and Sort Panel
        JPanel searchSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchSortPanel.setBackground(SECONDARY_COLOR);

        searchField = new JTextField(20);
        searchField.setFont(DEFAULT_FONT);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterClients();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterClients();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterClients();
            }
        });

        sortComboBox = new JComboBox<>(new String[]{"Company Name", "Contact Name", "City"});
        sortComboBox.setFont(DEFAULT_FONT);
        sortComboBox.addActionListener(e -> sortClients());

        searchSortPanel.add(new JLabel("Search:"));
        searchSortPanel.add(searchField);
        searchSortPanel.add(new JLabel("Sort by:"));
        searchSortPanel.add(sortComboBox);

        mainPanel.add(searchSortPanel, BorderLayout.PAGE_START);

        // Client List Panel
        clientListPanel = new JPanel();
        clientListPanel.setLayout(new BoxLayout(clientListPanel, BoxLayout.Y_AXIS));
        clientListPanel.setBackground(SECONDARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(clientListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Clients", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 30), Color.BLACK));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(SECONDARY_COLOR);
        JButton addClientButton = new JButton("Add Client");
        addClientButton.setFont(DEFAULT_FONT);
        addClientButton.setBackground(ACCENT_COLOR);
        addClientButton.setForeground(Color.WHITE);
        addClientButton.addActionListener(e -> showAddClientDialog());
        buttonPanel.add(addClientButton);

        JButton exportButton = new JButton("Export to CSV");
        exportButton.setFont(DEFAULT_FONT);
        exportButton.setBackground(ACCENT_COLOR);
        exportButton.setForeground(Color.WHITE);
        exportButton.addActionListener(e -> exportToCSV());
        buttonPanel.add(exportButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load and Display
        loadSampleClients();
        displayedClients.addAll(clients); // Initially show all
        updateClientListPanel();
        sortClients(); // Initial sort
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void loadSampleClients() {
        clients.add(new Client("Roberts-Bull Productions", "Dr Beth Williams", "beth.williams@fakeshow.com", "01632 621899", "Buckley Group Parkway, West Ellie, Business Park", "Port Jeffrey", "A9 9AA", "", ""));
        clients.add(new Client("Robinson and Sons Productions", "Kimberley Begum", "kimberley.begum@fakeshow.com", "020 7946 0588", "Tucker, Lewis and Brennan Parkway, Catherinehaven, Industrial Estate", "Lake Carolmouth", "AA9 9AA", "", ""));
        clients.add(new Client("Gordon and Sons Productions", "Albert Dickinson", "albert.dickinson@fakeshow.com", "0113 496 2677", "Campbell-Dickinson Drive, West Brandonland, Trade Centre", "East Laura", "A99 9ZZ", "", ""));
        clients.add(new Client("Howard Ltd Productions", "Ashleigh Chapman", "ashleigh.chapman@fakeshow.com", "020 7946 3367", "Mitchell LLC Way, Port Jadeborough, Business Park", "Littleborough", "AA9 9ZZ", "Leah Robson-Turner", "leah.robson-turner@fakeshow.com"));
        clients.add(new Client("Mason, Wright and Green Productions", "Francesca Wilson", "francesca.wilson@fakeshow.com", "07700 361705", "Heath Ltd Avenue, East Sean, Industrial Estate", "Port Stacey", "A99 9ZZ", "Dr Alan Brown", "alan.brown@fakeshow.com"));
        clients.add(new Client("Houghton-Wright Productions", "Hayley Yates-Thomas", "hayley.yates-thomas@fakeshow.com", "01632 171989", "Allen PLC Road, Sarahside, Corporate Plaza", "Lake Dianeport", "A9A 9AA", "Eric Marshall-Townsend", "eric.marshall-townsend@fakeshow.com"));
        clients.add(new Client("Taylor-Faulkner Productions", "Marilyn West-Jones", "marilyn.west-jones@fakeshow.com", "01632 888134", "Phillips, Clark and Booth Drive, East Rosemary, Corporate Plaza", "Mahmoodside", "AA9 9AA", "Charlotte Morgan-Birch", "charlotte.morgan-birch@fakeshow.com"));
        clients.add(new Client("Norton Group Productions", "Lynne Hall", "lynne.hall@fakeshow.com", "0113 496 9437", "Smith, Roberts and Wood Avenue, North Gailton, Corporate Plaza", "New Danny", "AA9A 9AA", "Katie Yates-Green", "katie.yates-green@fakeshow.com"));
        clients.add(new Client("Payne-Lowe Productions", "Pamela Walker", "pamela.walker@fakeshow.com", "01632 368469", "Doherty-Porter Drive, Williamsshire, Business Park", "Smithville", "AA9A 9AA", "", ""));
        clients.add(new Client("Hughes Ltd Productions", "Helen Evans", "helen.evans@fakeshow.com", "01632 515530", "West-Riley Avenue, West Marcusshire, Business Park", "Shawborough", "AA9 9ZZ", "Natalie Short", "natalie.short@fakeshow.com"));
        clients.add(new Client("Graham-Smith Productions", "Mr Ashley Johnson", "ashley.johnson@fakeshow.com", "0113 496 4513", "Clayton Group Road, Joneshaven, Technology Hub", "West Guystad", "AA99 9AA", "Eleanor Wilkinson", "eleanor.wilkinson@fakeshow.com"));
        clients.add(new Client("Smith Inc Productions", "Mr Clifford Patel", "clifford.patel@fakeshow.com", "07700 185425", "Davies Group Lane, Lake Angela, Technology Hub", "West Phillip", "A99 9AA", "", ""));
        clients.add(new Client("Rose-Archer Productions", "Ashleigh Jones-Sharpe", "ashleigh.jones-sharpe@fakeshow.com", "0306 415477", "Nicholls Ltd Way, Holmesview, Trade Centre", "Charlottebury", "A99 9ZZ", "", ""));
        clients.add(new Client("Slater-Marshall Productions", "Dr Pamela Clark", "pamela.clark@fakeshow.com", "0113 496 7865", "Harris LLC Drive, Clarkeville, Trade Centre", "Evansmouth", "AA99 9AA", "Mrs Christine Nicholls", "christine.nicholls@fakeshow.com"));
        clients.add(new Client("Palmer-Pearson Productions", "Sharon Herbert-Brown", "sharon.herbert-brown@fakeshow.com", "020 7946 8451", "Davis, Smith and Walters Street, Port Marie, Corporate Plaza", "East Brianview", "AA9 9ZZ", "", ""));
        clients.add(new Client("Humphries, Heath and Thompson Productions", "Robin Kirk", "robin.kirk@fakeshow.com", "020 7946 7589", "Wright-Patel Parkway, New Jade, Technology Hub", "Port Rita", "A9 9AA", "Jake Rhodes-Hall", "jake.rhodes-hall@fakeshow.com"));
        clients.add(new Client("Howe-Harris Productions", "Mr Stephen West", "stephen.west@fakeshow.com", "07700 707076", "Murphy-Fisher Street, Shaunshire, Business Park", "North Geoffrey", "AA9A 9AA", "Pauline Bowen", "pauline.bowen@fakeshow.com"));
        clients.add(new Client("Bennett, Hughes and John Productions", "Diana Howell-Barker", "diana.howell-barker@fakeshow.com", "0113 496 0739", "Byrne, Hale and Webb Street, West Jasmine, Trade Centre", "New Hazelview", "A9 9AA", "Irene Thomson", "irene.thomson@fakeshow.com"));
        clients.add(new Client("Clarke Inc Productions", "Abdul Dixon-Watson", "abdul.dixon-watson@fakeshow.com", "01632 837427", "Wilson-Taylor Street, Lyndaside, Trade Centre", "Williamsfurt", "AA9A 9AA", "", ""));
        clients.add(new Client("Turner-Stevens Productions", "Lucy Wilkins", "lucy.wilkins@fakeshow.com", "0306 076341", "Barrett LLC Avenue, Knightville, Industrial Estate", "New Annafurt", "A99 9ZZ", "", ""));
        clients.add(new Client("Ward Inc Productions", "Jason Day", "jason.day@fakeshow.com", "0113 496 5655", "Parry, Green and Ball Road, Port Tracyburgh, Technology Hub", "Port Jodiebury", "AA9 9ZZ", "Dr Louis Dennis", "louis.dennis@fakeshow.com"));
        clients.add(new Client("Bibi Inc Productions", "Mr Roy Ward", "roy.ward@fakeshow.com", "01632 888932", "Bell, Black and White Lane, New Dominic, Corporate Plaza", "New Brian", "A9 9AA", "", ""));
        clients.add(new Client("Mitchell-Webb Productions", "Owen Booth", "owen.booth@fakeshow.com", "07700 300635", "Wood PLC Parkway, Josephineshire, Business Park", "West Brucebury", "A99 9ZZ", "", ""));
        clients.add(new Client("Turner, Young and Allan Productions", "Dr Stewart Wood", "stewart.wood@fakeshow.com", "07700 256624", "Morris-Williams Road, South Chelsea, Technology Hub", "Bradleyfort", "A9 9AA", "Martin Edwards", "martin.edwards@fakeshow.com"));
        clients.add(new Client("Booth LLC Productions", "Louise Roberts", "louise.roberts@fakeshow.com", "0113 496 1686", "Bartlett and Sons Drive, Lake Joeborough, Corporate Plaza", "Port Andrew", "A99 9ZZ", "Frances Fowler", "frances.fowler@fakeshow.com"));
        clients.add(new Client("Bryan Group Productions", "Dr Robert Page", "robert.page@fakeshow.com", "0306 712922", "Reed and Sons Street, Deniston, Business Park", "Nicholsonstad", "A9A 9AA", "", ""));
        clients.add(new Client("Johnson Inc Productions", "Stuart Cooke", "stuart.cooke@fakeshow.com", "01632 768675", "Smith Group Road, South Kimbury, Business Park", "Sarachester", "AA99 9AA", "", ""));
        clients.add(new Client("Johnson, Fletcher and Page Productions", "Miss Ann Pearson", "ann.pearson@fakeshow.com", "01632 481546", "Smith and Sons Drive, South Tommouth, Technology Hub", "Singhburgh", "A9 9AA", "", ""));
        clients.add(new Client("Smith, Barnett and Smith Productions", "Anna Duncan", "anna.duncan@fakeshow.com", "0113 496 8885", "Hughes-Osborne Lane, Graceview, Technology Hub", "Tobymouth", "AA9A 9AA", "", ""));
        clients.add(new Client("Evans, Richards and Webb Productions", "Anne Goodwin-Jones", "anne.goodwin-jones@fakeshow.com", "07700 937982", "Smith-Hudson Lane, Wattsland, Business Park", "New Iain", "AA9A 9AA", "", ""));
        clients.add(new Client("Chapman, Coles and Howard Productions", "Janet Smith", "janet.smith@fakeshow.com", "01632 947107", "Coleman, Roberts and Gray Parkway, Port Rachael, Technology Hub", "North Allan", "A9A 9AA", "Oliver Robinson", "oliver.robinson@fakeshow.com"));
        clients.add(new Client("James LLC Productions", "Fiona Taylor", "fiona.taylor@fakeshow.com", "020 7946 2953", "Banks PLC Road, Bethanborough, Corporate Plaza", "New Andreaside", "AA9 9AA", "", ""));
        clients.add(new Client("Burton-Matthews Productions", "Judith Cook", "judith.cook@fakeshow.com", "0113 496 8736", "Moore, Thomas and Smith Drive, Rogerview, Industrial Estate", "Port Janet", "AA9 9ZZ", "", ""));
        clients.add(new Client("White-Clark Productions", "Mrs Francesca Fisher", "francesca.fisher@fakeshow.com", "07700 344633", "French-Edwards Lane, Summersside, Trade Centre", "South Pamela", "AA9A 9AA", "", ""));
        clients.add(new Client("Smith, Jackson and Richardson Productions", "Ms Laura King", "laura.king@fakeshow.com", "0113 496 3148", "Martin Group Parkway, South Yvonneville, Trade Centre", "Port Shaneport", "A99 9ZZ", "", ""));
        clients.add(new Client("Gibbs and Sons Productions", "Dr Gordon Dixon", "gordon.dixon@fakeshow.com", "0113 496 7503", "Hunt Ltd Avenue, East Denise, Business Park", "Ruthmouth", "AA9 9ZZ", "", ""));
        clients.add(new Client("Taylor, Thomas and Hunter Productions", "George Wilson", "george.wilson@fakeshow.com", "020 7946 7148", "Johnston-Patel Street, Gailmouth, Corporate Plaza", "Charleneburgh", "A99 9AA", "Mr Mitchell White", "mitchell.white@fakeshow.com"));
        clients.add(new Client("Atkins-Schofield Productions", "Katherine Taylor", "katherine.taylor@fakeshow.com", "0306 559368", "Shepherd Ltd Lane, West Sarafurt, Business Park", "Moranborough", "A99 9ZZ", "Sam Hall", "sam.hall@fakeshow.com"));
        clients.add(new Client("George-Whitehead Productions", "Anthony Miller", "anthony.miller@fakeshow.com", "07700 530420", "Williams, Carter and Rogers Street, Andrewmouth, Trade Centre", "New Oliviachester", "AA9A 9AA", "Emily Evans", "emily.evans@fakecustomer.com"));
        clients.add(new Client("Smith-Bishop Productions", "Carole Taylor-Walsh", "carole.taylor-walsh@fakeshow.com", "07700 695173", "Wells, Dixon and Taylor Street, Wadeborough, Corporate Plaza", "West Martin", "AA99 9AA", "Ms Hilary Rogers", "hilary.rogers@fakecustomer.com"));
        clients.add(new Client("Marshall and Sons Productions", "Scott King", "scott.king@fakeshow.com", "01632 110455", "Fowler, Bennett and McLean Drive, Port Brendaview, Corporate Plaza", "South Annafort", "AA99 9AA", "", ""));
        clients.add(new Client("Duncan-Lewis Productions", "Dr Ryan Graham", "ryan.graham@fakeshow.com", "0113 496 2810", "Chapman Ltd Lane, Port Kenneth, Technology Hub", "Turnerborough", "A9 9AA", "Barbara White", "barbara.white@fakeshow.com"));
        clients.add(new Client("Lee, James and Russell Productions", "Mr Martin Richardson", "martin.richardson@fakeshow.com", "0113 496 0454", "Peacock, Jenkins and Thompson Road, Steeleburgh, Business Park", "Allanport", "A99 9ZZ", "Mohammad Mills-Sullivan", "mohammad.mills-sullivan@fakecustomer.com"));
        clients.add(new Client("Barker-Conway Productions", "Mathew Roberts", "mathew.roberts@fakeshow.com", "020 7946 5704", "Khan-McCarthy Parkway, Morganside, Business Park", "East Dylanmouth", "AA9 9ZZ", "", ""));
        clients.add(new Client("Craig, Gardner and Murphy Productions", "Dr Lorraine Reid", "lorraine.reid@fakeshow.com", "0113 496 7549", "Evans-Smith Way, Port Kim, Business Park", "South Frances", "AA9 9AA", "Aaron Dean-Thorpe", "aaron.dean-thorpe@fakeshow.com"));
        clients.add(new Client("Wilson, Higgins and Butler Productions", "Leanne Lloyd", "leanne.lloyd@fakeshow.com", "020 7946 1101", "Johnson, Powell and Webb Lane, Shawtown, Technology Hub", "Karenside", "AA9 9AA", "Vincent Potter", "vincent.potter@fakeshow.com"));
        clients.add(new Client("Hamilton Ltd Productions", "Joe Ward-Baker", "joe.ward-baker@fakeshow.com", "020 7946 0365", "Green-Bradley Lane, Poolemouth, Business Park", "Smithmouth", "AA99 9AA", "", ""));
        clients.add(new Client("Perry, Davis and Lloyd Productions", "Toby Baker", "toby.baker@fakeshow.com", "0306 499990", "Cooper, Gordon and Rogers Street, Port Hollie, Industrial Estate", "Joelmouth", "AA9 9ZZ", "", ""));
        clients.add(new Client("Chapman-Moss Productions", "Ms Yvonne Dunn", "yvonne.dunn@fakeshow.com", "020 7946 1682", "Davey PLC Lane, Lake Bethanburgh, Business Park", "Mauricefurt", "AA9 9AA", "", ""));
        clients.add(new Client("Thomson, Holloway and Davies Productions", "Abigail Walsh-Evans", "abigail.walsh-evans@fakeshow.com", "020 7946 3872", "James Ltd Lane, East Suzanne, Technology Hub", "Lake Ian", "AA9 9AA", "", ""));
    }

    private static void updateClientListPanel() {
        clientListPanel.removeAll();
        for (Client client : displayedClients) {
            clientListPanel.add(createClientPanel(client));
        }
        clientListPanel.revalidate();
        clientListPanel.repaint();
    }

    private static JPanel createClientPanel(Client client) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton toggleButton = new JButton("▶ " + client.companyName);
        toggleButton.setFont(DEFAULT_FONT.deriveFont(Font.BOLD));
        toggleButton.setForeground(PRIMARY_COLOR);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        toggleButton.addActionListener(e -> toggleClientDetails(panel, client));
        panel.add(toggleButton, BorderLayout.NORTH);

        return panel;
    }

    private static void toggleClientDetails(JPanel panel, Client client) {
        Component[] components = panel.getComponents();
        boolean detailsVisible = false;
        for (Component component : components) {
            if (component instanceof JPanel && "detailsPanel".equals(component.getName())) {
                detailsVisible = component.isVisible();
                component.setVisible(!detailsVisible);
                break;
            }
        }

        if (!detailsVisible) {
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBackground(panel.getBackground());
            detailsPanel.setName("detailsPanel");
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel companyNameLabel = createDetailLabel("Company Name:", client.companyName);
            JLabel contactNameLabel = createDetailLabel("Contact Name:", client.contactName);
            JLabel contactEmailLabel = createDetailLabel("Contact Email:", client.contactEmail);
            JLabel phoneNumberLabel = createDetailLabel("Phone Number:", client.phoneNumber);
            JLabel streetAddressLabel = createDetailLabel("Street Address:", client.streetAddress);
            JLabel cityLabel = createDetailLabel("City:", client.city);
            JLabel postcodeLabel = createDetailLabel("Postcode:", client.postcode);
            JLabel billingNameLabel = createDetailLabel("Billing Name:", client.billingName);
            JLabel billingEmailLabel = createDetailLabel("Billing Email:", client.billingEmail);

            detailsPanel.add(companyNameLabel);
            detailsPanel.add(contactNameLabel);
            detailsPanel.add(contactEmailLabel);
            detailsPanel.add(phoneNumberLabel);
            detailsPanel.add(streetAddressLabel);
            detailsPanel.add(cityLabel);
            detailsPanel.add(postcodeLabel);
            detailsPanel.add(billingNameLabel);
            detailsPanel.add(billingEmailLabel);

            panel.add(detailsPanel, BorderLayout.CENTER);

            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actionPanel.setBackground(panel.getBackground());
            JButton editButton = createActionButton("Edit Client", e -> showEditClientDialog(client));
            JButton deleteButton = createActionButton("Delete Client", e -> deleteClient(client));
            actionPanel.add(editButton);
            actionPanel.add(deleteButton);
            panel.add(actionPanel, BorderLayout.SOUTH);

        } else {
            // Remove detailsPanel and actionPanel
            for (int i = components.length - 1; i >= 0; i--) {
                if (components[i] instanceof JPanel) {
                    JPanel tempPanel = (JPanel) components[i]; // Cast to JPanel
                    if ("detailsPanel".equals(components[i].getName()) ||
                            tempPanel.getLayout() instanceof FlowLayout) { // Use tempPanel.getLayout()
                        panel.remove(components[i]);
                    }
                }
            }
        }

        panel.revalidate();
        panel.repaint();
        frame.revalidate();
        frame.repaint();
    }

    private static JLabel createDetailLabel(String label, String value) {
        JLabel detailLabel = new JLabel("<html><b>" + label + "</b>: " + (value != null ? value : "") + "</html>");
        detailLabel.setFont(DEFAULT_FONT);
        return detailLabel;
    }



    private static JButton createActionButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(DEFAULT_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.addActionListener(listener);
        button.setFocusPainted(false);
        return button;
    }

    private static void showAddClientDialog() {
        JDialog dialog = new JDialog(frame, "Add New Client", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(SECONDARY_COLOR);

        // Store the JPanels, but access the JTextFields within them
        JPanel companyNamePanel = createLabeledInputField("Company Name:");
        JPanel contactNamePanel = createLabeledInputField("Contact Name:");
        JPanel contactEmailPanel = createLabeledInputField("Contact Email:");
        JPanel phoneNumberPanel = createLabeledInputField("Phone Number:");
        JPanel streetAddressPanel = createLabeledInputField("Street Address:");
        JPanel cityPanel = createLabeledInputField("City:");
        JPanel postcodePanel = createLabeledInputField("Postcode:");
        JPanel billingNamePanel = createLabeledInputField("Billing Name:");
        JPanel billingEmailPanel = createLabeledInputField("Billing Email:");

        formPanel.add(companyNamePanel);
        formPanel.add(contactNamePanel);
        formPanel.add(contactEmailPanel);
        formPanel.add(phoneNumberPanel);
        formPanel.add(streetAddressPanel);
        formPanel.add(cityPanel);
        formPanel.add(postcodePanel);
        formPanel.add(billingNamePanel);
        formPanel.add(billingEmailPanel);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(DEFAULT_FONT.deriveFont(Font.BOLD));
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            // Access the JTextFields within the JPanels
            String companyName = ((JTextField) companyNamePanel.getComponent(1)).getText().trim();
            if (companyName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Company Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String contactName = ((JTextField) contactNamePanel.getComponent(1)).getText().trim();
            if (contactName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Contact Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String contactEmail = ((JTextField) contactEmailPanel.getComponent(1)).getText().trim();
            if (!contactEmail.isEmpty() && !isValidEmail(contactEmail)) {
                JOptionPane.showMessageDialog(dialog, "Invalid Email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String phoneNumber = ((JTextField) phoneNumberPanel.getComponent(1)).getText().trim();
            if (phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Phone Number is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String streetAddress = ((JTextField) streetAddressPanel.getComponent(1)).getText().trim();
            if (streetAddress.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Street Address is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String city = ((JTextField) cityPanel.getComponent(1)).getText().trim();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "City is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String postcode = ((JTextField) postcodePanel.getComponent(1)).getText().trim();
            if (postcode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Postcode is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String billingName = ((JTextField) billingNamePanel.getComponent(1)).getText().trim();
            String billingEmail = ((JTextField) billingEmailPanel.getComponent(1)).getText().trim();
            if (!billingEmail.isEmpty() && !isValidEmail(billingEmail)) {
                JOptionPane.showMessageDialog(dialog, "Invalid Billing Email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            clients.add(new Client(companyName, contactName, contactEmail, phoneNumber, streetAddress, city, postcode, billingName, billingEmail));
            displayedClients.clear();
            displayedClients.addAll(clients); // Reset displayed list
            updateClientListPanel();
            sortClients();
            dialog.dispose();
        });

        formPanel.add(saveButton);

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static JPanel createLabeledInputField(String labelText) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        label.setFont(DEFAULT_FONT);
        JTextField textField = new JTextField(20);
        textField.setFont(DEFAULT_FONT);

        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.setMaximumSize(new Dimension(400, 30));
        // Return the JTextField instead of the JPanel
        return inputPanel;
    }



    private static JTextField createInputField(String labelText) {
        JTextField textField = new JTextField(20);
        textField.setFont(DEFAULT_FONT);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(textField);
        panel.setMaximumSize(new Dimension(400, 30));
        return textField;
    }

    private static void showEditClientDialog(Client client) {
        JDialog dialog = new JDialog(frame, "Edit Client", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(SECONDARY_COLOR);

        // Create labeled input fields and set their initial values
        JPanel companyNamePanel = createLabeledInputField("Company Name:");
        ((JTextField) companyNamePanel.getComponent(1)).setText(client.companyName);

        JPanel contactNamePanel = createLabeledInputField("Contact Name:");
        ((JTextField) contactNamePanel.getComponent(1)).setText(client.contactName);

        JPanel contactEmailPanel = createLabeledInputField("Contact Email:");
        ((JTextField) contactEmailPanel.getComponent(1)).setText(client.contactEmail);

        JPanel phoneNumberPanel = createLabeledInputField("Phone Number:");
        ((JTextField) phoneNumberPanel.getComponent(1)).setText(client.phoneNumber);

        JPanel streetAddressPanel = createLabeledInputField("Street Address:");
        ((JTextField) streetAddressPanel.getComponent(1)).setText(client.streetAddress);

        JPanel cityPanel = createLabeledInputField("City:");
        ((JTextField) cityPanel.getComponent(1)).setText(client.city);

        JPanel postcodePanel = createLabeledInputField("Postcode:");
        ((JTextField) postcodePanel.getComponent(1)).setText(client.postcode);

        JPanel billingNamePanel = createLabeledInputField("Billing Name:");
        ((JTextField) billingNamePanel.getComponent(1)).setText(client.billingName);

        JPanel billingEmailPanel = createLabeledInputField("Billing Email:");
        ((JTextField) billingEmailPanel.getComponent(1)).setText(client.billingEmail);

        formPanel.add(companyNamePanel);
        formPanel.add(contactNamePanel);
        formPanel.add(contactEmailPanel);
        formPanel.add(phoneNumberPanel);
        formPanel.add(streetAddressPanel);
        formPanel.add(cityPanel);
        formPanel.add(postcodePanel);
        formPanel.add(billingNamePanel);
        formPanel.add(billingEmailPanel);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(DEFAULT_FONT.deriveFont(Font.BOLD));
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            String companyName = ((JTextField) companyNamePanel.getComponent(1)).getText().trim();
            if (companyName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Company Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String contactName = ((JTextField) contactNamePanel.getComponent(1)).getText().trim();
            if (contactName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Contact Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String contactEmail = ((JTextField) contactEmailPanel.getComponent(1)).getText().trim();
            if (!contactEmail.isEmpty() && !isValidEmail(contactEmail)) {
                JOptionPane.showMessageDialog(dialog, "Invalid Email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String phoneNumber = ((JTextField) phoneNumberPanel.getComponent(1)).getText().trim();
            if (phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Phone Number is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String streetAddress = ((JTextField) streetAddressPanel.getComponent(1)).getText().trim();
            if (streetAddress.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Street Address is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String city = ((JTextField) cityPanel.getComponent(1)).getText().trim();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "City is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String postcode = ((JTextField) postcodePanel.getComponent(1)).getText().trim();
            if (postcode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Postcode is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String billingName = ((JTextField) billingNamePanel.getComponent(1)).getText().trim();
            String billingEmail = ((JTextField) billingEmailPanel.getComponent(1)).getText().trim();
            if (!billingEmail.isEmpty() && !isValidEmail(billingEmail)) {
                JOptionPane.showMessageDialog(dialog, "Invalid Billing Email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            client.companyName = companyName;
            client.contactName = contactName;
            client.contactEmail = contactEmail;
            client.phoneNumber = phoneNumber;
            client.streetAddress = streetAddress;
            client.city = city;
            client.postcode = postcode;
            client.billingName = billingName;
            client.billingEmail = billingEmail;

            updateClientListPanel();
            sortClients();
            dialog.dispose();
        });

        formPanel.add(saveButton); // ADDED THE SAVE BUTTON BACK!

        dialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static void filterClients() {
        String searchText = searchField.getText().trim().toLowerCase();
        displayedClients.clear();
        if (searchText.isEmpty()) {
            displayedClients.addAll(clients); // Show all clients
        } else {
            for (Client client : clients) {
                if (client.companyName.toLowerCase().contains(searchText) ||
                        client.contactName.toLowerCase().contains(searchText) ||
                        client.city.toLowerCase().contains(searchText)) {
                    displayedClients.add(client);
                }
            }
        }
        updateClientListPanel();
        sortClients(); // Keep the sorting consistent after filtering
    }

    private static void sortClients() {
        String sortBy = (String) sortComboBox.getSelectedItem();
        switch (sortBy) {
            case "Company Name":
                displayedClients.sort(Comparator.comparing(client -> client.companyName.toLowerCase()));
                break;
            case "Contact Name":
                displayedClients.sort(Comparator.comparing(client -> client.contactName.toLowerCase()));
                break;
            case "City":
                displayedClients.sort(Comparator.comparing(client -> client.city.toLowerCase()));
                break;
        }
        updateClientListPanel();
    }

    private static void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave + ".csv")) {
                // Write header
                writer.write("Company Name,Contact Name,Contact Email,Phone Number,Street Address,City,Postcode,Billing Name,Billing Email\n");
                // Write data
                for (Client client : clients) {
                    writer.write(client.companyName + "," +
                            client.contactName + "," +
                            client.contactEmail + "," +
                            client.phoneNumber + "," +
                            client.streetAddress + "," +
                            client.city + "," +
                            client.postcode + "," +
                            client.billingName + "," +
                            client.billingEmail + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Data exported successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }


    private static void deleteClient(Client client) {
        int choice = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete " + client.companyName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            clients.remove(client);
            displayedClients.remove(client);
            updateClientListPanel();
        }
    }

}