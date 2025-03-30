package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.text.DateFormatSymbols;
// Import Window and Frame for parent determination in SimpleDatePicker
import java.awt.Window;
import java.awt.Frame;
// Import Rectangle and JViewport for scrolling logic
import java.awt.Rectangle;
import javax.swing.JViewport;
// Import Separator
import javax.swing.JSeparator;
// Import Desktop for hyperlink support
import java.awt.Desktop;
import java.net.URI;


public class TheatreContractsGUI { // <<<< START OF MAIN CLASS

    // Explicitly typed ArrayList and synchronized list
    private static final List<Contract> liveShowContracts = Collections.synchronizedList(new ArrayList<Contract>());
    private static final List<Contract> theatreContracts = Collections.synchronizedList(new ArrayList<Contract>());

    // Store references to the main display panels
    private static JPanel liveShowsPanel;
    private static JPanel theatrePanel;

    public static void main(String[] args) {
        // Ensure GUI creation runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(TheatreContractsGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Use Look and Feel that respects system settings for better appearance (optional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        JFrame frame = new JFrame("Contracts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Increased default size slightly to accommodate potentially more fields in dialogs/display
        frame.setSize(950, 750);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("CONTRACTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        // Add some padding to the title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Use BoxLayout for better spacing control
        sidebar.setBackground(new Color(173, 216, 230)); // Light blue
        sidebar.setPreferredSize(new Dimension(160, frame.getHeight())); // Slightly wider sidebar
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5)); // Add padding

        JButton liveShowsButton = new JButton("Live Shows");
        JButton theatreButton = new JButton("Theatre");
        JButton addContractButton = new JButton("+ Add Contract");

        Dimension buttonSize = new Dimension(150, 40); // Uniform button size

        for (JButton btn : new JButton[]{liveShowsButton, theatreButton, addContractButton}) {
            btn.setBackground(new Color(255, 223, 102)); // Yellowish button color
            btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center buttons horizontally
            btn.setMaximumSize(buttonSize); // Set max size for BoxLayout
            btn.setPreferredSize(buttonSize);
            btn.setMinimumSize(buttonSize);
            btn.setFocusPainted(false); // Remove focus border
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 15))); // Add spacing between buttons
        }

        mainPanel.add(sidebar, BorderLayout.WEST);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding around card panel

        // Initialize the main display panels here
        liveShowsPanel = createListPanel();
        theatrePanel = createListPanel();

        // Add ScrollPanes for the panels
        JScrollPane liveShowsScrollPane = new JScrollPane(liveShowsPanel);
        liveShowsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        liveShowsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Increase scroll speed
        liveShowsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        // Remove border from scroll pane
        liveShowsScrollPane.setBorder(BorderFactory.createEmptyBorder());


        JScrollPane theatreScrollPane = new JScrollPane(theatrePanel);
        theatreScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        theatreScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        theatreScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        theatreScrollPane.setBorder(BorderFactory.createEmptyBorder());

        cardPanel.add(liveShowsScrollPane, "LiveShows");
        cardPanel.add(theatreScrollPane, "Theatre");

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        liveShowsButton.addActionListener(e -> cardLayout.show(cardPanel, "LiveShows"));
        theatreButton.addActionListener(e -> cardLayout.show(cardPanel, "Theatre"));

        // Pass the parent frame and the panels to the dialog method
        addContractButton.addActionListener(e -> showAddContractDialog(frame, liveShowsPanel, theatrePanel));

        // --- Load Sample / Initial Contracts ---
        loadInitialContracts(); // <<<< Calls the NEW method below

        frame.add(mainPanel);
        frame.pack(); // Pack the frame to preferred size AFTER adding components
        frame.setMinimumSize(new Dimension(800, 600)); // Set a reasonable minimum size
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true); // <<<< Make the frame visible *BEFORE* the initial refresh

        // --- Initial Refresh Call ---
        // Needs to happen AFTER frame is visible so getWindowAncestor works
        refreshContractPanels();
    }

    // Helper to create the standard look for list panels
    private static JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        // Add padding inside the list area
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    // Define date format centrally
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
    // private static final SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat("MMM yy", Locale.ENGLISH); // No longer needed

    // *** UPDATED loadInitialContracts Method with Fictional Show Names ***
    private static void loadInitialContracts() {
        // Ensure lists are clear before loading
        liveShowContracts.clear();
        theatreContracts.clear();

        // List of fictional show/production names
        String[] fictionalShowNames = {
                "Starlight Serenade", "Echoes of Time", "The Crimson Masquerade", "Neon Nights Comedy",
                "River City Blues", "Whispers in the Walls", "Quantum Leap", "Sunset Boulevard Revival",
                "Jungle Beats", "The Alchemist's Dream", "CyberCity Chronicles", "Midnight Jazz Hour",
                "Celtic Legends", "A Midsummer Night's Techno Dream", "Urban Canvas", "The Glass Menagerie Reimagined",
                "Galaxy Grooves", "Silk & Steel", "Forgotten Folklore", "The Jester's Gambit",
                "Clockwork Heart", "Emerald City Limits", "Pirate Queen's Ballad", "Arctic Echoes",
                "Desert Mirage Concert", "Ghosts of Parliament", "The Lavender Letter", "Robot Romeo",
                "Velvet Thunder", "Steam Powered Stories", "Sapphire Skies", "The Oracle's Riddle",
                "Comedy Cavalcade", "Dragon's Hoard Musical", "Memories in Monochrome", "Crimson Peak",
                "Symphony of Sparks", "The Wanderer's Tale", "Concrete Jungle Jam", "Lunar Tides",
                "Puppet Master", "Electric Sheep", "Golden Age Gala", "Shadow Puppets",
                "Carnival of Souls", "Iron & Ivy", "Beyond the Horizon", "Whispering Woods",
                "The Time Weaver", "Jazz Hands & Jokes", "Under the Big Top", "Switched" // Adding 'Switched' as requested
                // Add more names if needed to exceed the number of customers
        };


        String customerData = """
            Roberts-Bull Productions	Dr Beth Williams	beth.williams@fakeshow.com	01632 621899	Buckley Group Parkway, West Ellie, Business Park	Port Jeffrey	A9 9AA		
            Robinson and Sons Productions	Kimberley Begum	kimberley.begum@fakeshow.com	020 7946 0588	Tucker, Lewis and Brennan Parkway, Catherinehaven, Industrial Estate	Lake Carolmouth	AA9 9AA		
            Gordon and Sons Productions	Albert Dickinson	albert.dickinson@fakeshow.com	0113 496 2677	Campbell-Dickinson Drive, West Brandonland, Trade Centre	East Laura	A99 9ZZ		
            Howard Ltd Productions	Ashleigh Chapman	ashleigh.chapman@fakeshow.com	020 7946 3367	Mitchell LLC Way, Port Jadeborough, Business Park	Littleborough	AA9 9ZZ	Leah Robson-Turner	leah.robson-turner@fakeshow.com
            Mason, Wright and Green Productions	Francesca Wilson	francesca.wilson@fakeshow.com	07700 361705	Heath Ltd Avenue, East Sean, Industrial Estate	Port Stacey	A99 9ZZ	Dr Alan Brown	alan.brown@fakeshow.com
            Houghton-Wright Productions	Hayley Yates-Thomas	hayley.yates-thomas@fakeshow.com	01632 171989	Allen PLC Road, Sarahside, Corporate Plaza	Lake Dianeport	A9A 9AA	Eric Marshall-Townsend	eric.marshall-townsend@fakeshow.com
            Taylor-Faulkner Productions	Marilyn West-Jones	marilyn.west-jones@fakeshow.com	01632 888134	Phillips, Clark and Booth Drive, East Rosemary, Corporate Plaza	Mahmoodside	AA9 9AA	Charlotte Morgan-Birch	charlotte.morgan-birch@fakeshow.com
            Norton Group Productions	Lynne Hall	lynne.hall@fakeshow.com	0113 496 9437	Smith, Roberts and Wood Avenue, North Gailton, Corporate Plaza	New Danny	AA9A 9AA	Katie Yates-Green	katie.yates-green@fakeshow.com
            Payne-Lowe Productions	Pamela Walker	pamela.walker@fakeshow.com	01632 368469	Doherty-Porter Drive, Williamsshire, Business Park	Smithville	AA9A 9AA		
            Hughes Ltd Productions	Helen Evans	helen.evans@fakeshow.com	01632 515530	West-Riley Avenue, West Marcusshire, Business Park	Shawborough	AA9 9ZZ	Natalie Short	natalie.short@fakeshow.com
            Graham-Smith Productions	Mr Ashley Johnson	ashley.johnson@fakeshow.com	0113 496 4513	Clayton Group Road, Joneshaven, Technology Hub	West Guystad	AA99 9AA	Eleanor Wilkinson	eleanor.wilkinson@fakeshow.com
            Smith Inc Productions	Mr Clifford Patel	clifford.patel@fakeshow.com	07700 185425	Davies Group Lane, Lake Angela, Technology Hub	West Phillip	A99 9AA		
            Rose-Archer Productions	Ashleigh Jones-Sharpe	ashleigh.jones-sharpe@fakeshow.com	0306 415477	Nicholls Ltd Way, Holmesview, Trade Centre	Charlottebury	A99 9ZZ		
            Slater-Marshall Productions	Dr Pamela Clark	pamela.clark@fakeshow.com	0113 496 7865	Harris LLC Drive, Clarkeville, Trade Centre	Evansmouth	AA99 9AA	Mrs Christine Nicholls	christine.nicholls@fakeshow.com
            Palmer-Pearson Productions	Sharon Herbert-Brown	sharon.herbert-brown@fakeshow.com	020 7946 8451	Davis, Smith and Walters Street, Port Marie, Corporate Plaza	East Brianview	AA9 9ZZ		
            Humphries, Heath and Thompson Productions	Robin Kirk	robin.kirk@fakeshow.com	020 7946 7589	Wright-Patel Parkway, New Jade, Technology Hub	Port Rita	A9 9AA	Jake Rhodes-Hall	jake.rhodes-hall@fakeshow.com
            Howe-Harris Productions	Mr Stephen West	stephen.west@fakeshow.com	07700 707076	Murphy-Fisher Street, Shaunshire, Business Park	North Geoffrey	AA9A 9AA	Pauline Bowen	pauline.bowen@fakeshow.com
            Bennett, Hughes and John Productions	Diana Howell-Barker	diana.howell-barker@fakeshow.com	0113 496 0739	Byrne, Hale and Webb Street, West Jasmine, Trade Centre	New Hazelview	A9 9AA	Irene Thomson	irene.thomson@fakeshow.com
            Clarke Inc Productions	Abdul Dixon-Watson	abdul.dixon-watson@fakeshow.com	01632 837427	Wilson-Taylor Street, Lyndaside, Trade Centre	Williamsfurt	AA9A 9AA		
            Turner-Stevens Productions	Lucy Wilkins	lucy.wilkins@fakeshow.com	0306 076341	Barrett LLC Avenue, Knightville, Industrial Estate	New Annafurt	A99 9ZZ		
            Ward Inc Productions	Jason Day	jason.day@fakeshow.com	0113 496 5655	Parry, Green and Ball Road, Port Tracyburgh, Technology Hub	Port Jodiebury	AA9 9ZZ	Dr Louis Dennis	louis.dennis@fakeshow.com
            Bibi Inc Productions	Mr Roy Ward	roy.ward@fakeshow.com	01632 888932	Bell, Black and White Lane, New Dominic, Corporate Plaza	New Brian	A9 9AA		
            Mitchell-Webb Productions	Owen Booth	owen.booth@fakeshow.com	07700 300635	Wood PLC Parkway, Josephineshire, Business Park	West Brucebury	A99 9ZZ		
            Turner, Young and Allan Productions	Dr Stewart Wood	stewart.wood@fakeshow.com	07700 256624	Morris-Williams Road, South Chelsea, Technology Hub	Bradleyfort	A9 9AA	Martin Edwards	martin.edwards@fakeshow.com
            Booth LLC Productions	Louise Roberts	louise.roberts@fakeshow.com	0113 496 1686	Bartlett and Sons Drive, Lake Joeborough, Corporate Plaza	Port Andrew	A99 9ZZ	Frances Fowler	frances.fowler@fakeshow.com
            Bryan Group Productions	Dr Robert Page	robert.page@fakeshow.com	0306 712922	Reed and Sons Street, Deniston, Business Park	Nicholsonstad	A9A 9AA		
            Johnson Inc Productions	Stuart Cooke	stuart.cooke@fakeshow.com	01632 768675	Smith Group Road, South Kimbury, Business Park	Sarachester	AA99 9AA		
            Johnson, Fletcher and Page Productions	Miss Ann Pearson	ann.pearson@fakeshow.com	01632 481546	Smith and Sons Drive, South Tommouth, Technology Hub	Singhburgh	A9 9AA		
            Smith, Barnett and Smith Productions	Anna Duncan	anna.duncan@fakeshow.com	0113 496 8885	Hughes-Osborne Lane, Graceview, Technology Hub	Tobymouth	AA9A 9AA		
            Evans, Richards and Webb Productions	Anne Goodwin-Jones	anne.goodwin-jones@fakeshow.com	07700 937982	Smith-Hudson Lane, Wattsland, Business Park	New Iain	A9A 9AA		
            Chapman, Coles and Howard Productions	Janet Smith	janet.smith@fakeshow.com	01632 947107	Coleman, Roberts and Gray Parkway, Port Rachael, Technology Hub	North Allan	A9A 9AA	Oliver Robinson	oliver.robinson@fakeshow.com
            James LLC Productions	Fiona Taylor	fiona.taylor@fakeshow.com	020 7946 2953	Banks PLC Road, Bethanborough, Corporate Plaza	New Andreaside	AA9 9AA		
            Burton-Matthews Productions	Judith Cook	judith.cook@fakeshow.com	0113 496 8736	Moore, Thomas and Smith Drive, Rogerview, Industrial Estate	Port Janet	AA9 9ZZ		
            White-Clark Productions	Mrs Francesca Fisher	francesca.fisher@fakeshow.com	07700 344633	French-Edwards Lane, Summersside, Trade Centre	South Pamela	AA9A 9AA		
            Smith, Jackson and Richardson Productions	Ms Laura King	laura.king@fakeshow.com	0113 496 3148	Martin Group Parkway, South Yvonneville, Trade Centre	Port Shaneport	A99 9ZZ		
            Gibbs and Sons Productions	Dr Gordon Dixon	gordon.dixon@fakeshow.com	0113 496 7503	Hunt Ltd Avenue, East Denise, Business Park	Ruthmouth	AA9 9ZZ		
            Taylor, Thomas and Hunter Productions	George Wilson	george.wilson@fakeshow.com	020 7946 7148	Johnston-Patel Street, Gailmouth, Corporate Plaza	Charleneburgh	A99 9AA	Mr Mitchell White	mitchell.white@fakeshow.com
            Atkins-Schofield Productions	Katherine Taylor	katherine.taylor@fakeshow.com	0306 559368	Shepherd Ltd Lane, West Sarafurt, Business Park	Moranborough	A99 9ZZ	Sam Hall	sam.hall@fakeshow.com
            George-Whitehead Productions	Anthony Miller	anthony.miller@fakeshow.com	07700 530420	Williams, Carter and Rogers Street, Andrewmouth, Trade Centre	New Oliviachester	AA9A 9AA	Emily Evans	emily.evans@fakeshow.com
            Smith-Bishop Productions	Carole Taylor-Walsh	carole.taylor-walsh@fakeshow.com	07700 695173	Wells, Dixon and Taylor Street, Wadeborough, Corporate Plaza	West Martin	AA99 9AA	Ms Hilary Rogers	hilary.rogers@fakeshow.com
            Marshall and Sons Productions	Scott King	scott.king@fakeshow.com	01632 110455	Fowler, Bennett and McLean Drive, Port Brendaview, Corporate Plaza	South Annafort	AA99 9AA		
            Duncan-Lewis Productions	Dr Ryan Graham	ryan.graham@fakeshow.com	0113 496 2810	Chapman Ltd Lane, Port Kenneth, Technology Hub	Turnerborough	A9 9AA	Barbara White	barbara.white@fakeshow.com
            Lee, James and Russell Productions	Mr Martin Richardson	martin.richardson@fakeshow.com	0113 496 0454	Peacock, Jenkins and Thompson Road, Steeleburgh, Business Park	Allanport	A99 9ZZ	Mohammad Mills-Sullivan	mohammad.mills-sullivan@fakeshow.com
            Barker-Conway Productions	Mathew Roberts	mathew.roberts@fakeshow.com	020 7946 5704	Khan-McCarthy Parkway, Morganside, Business Park	East Dylanmouth	AA9 9ZZ		
            Craig, Gardner and Murphy Productions	Dr Lorraine Reid	lorraine.reid@fakeshow.com	0113 496 7549	Evans-Smith Way, Port Kim, Business Park	South Frances	AA9 9AA	Aaron Dean-Thorpe	aaron.dean-thorpe@fakeshow.com
            Wilson, Higgins and Butler Productions	Leanne Lloyd	leanne.lloyd@fakeshow.com	020 7946 1101	Johnson, Powell and Webb Lane, Shawtown, Technology Hub	Karenside	AA9 9AA	Vincent Potter	vincent.potter@fakeshow.com
            Hamilton Ltd Productions	Joe Ward-Baker	joe.ward-baker@fakeshow.com	020 7946 0365	Green-Bradley Lane, Poolemouth, Business Park	Smithmouth	AA99 9AA		
            Perry, Davis and Lloyd Productions	Toby Baker	toby.baker@fakeshow.com	0306 499990	Cooper, Gordon and Rogers Street, Port Hollie, Industrial Estate	Joelmouth	AA9 9ZZ		
            Chapman-Moss Productions	Ms Yvonne Dunn	yvonne.dunn@fakeshow.com	020 7946 1682	Davey PLC Lane, Lake Bethanburgh, Business Park	Mauricefurt	AA9 9AA		
            Thomson, Holloway and Davies Productions	Abigail Walsh-Evans	abigail.walsh-evans@fakeshow.com	020 7946 3872	James Ltd Lane, East Suzanne, Technology Hub	Lake Ian	AA9 9AA		
            """; // End of customer data block

        String[] lines = customerData.trim().split("\\R"); // Split into lines
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.APRIL, 1); // Start date for generation

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] fields = line.split("\t"); // Split by tab

            if (fields.length < 7) { continue; } // Skip malformed lines

            // Extract Client Info
            String companyName = fields[0]; String contactName = fields[1]; String contactEmail = fields[2];
            String phoneNumber = fields[3]; String streetAddress = fields[4]; String city = fields[5];
            String postcode = fields[6]; String billingName = (fields.length > 7) ? fields[7] : "";
            String billingEmail = (fields.length > 8) ? fields[8] : "";

            // *** Generate Placeholder Core Contract Details using Fictional Names ***
            // Use modulo to cycle through fictional names if needed
            String contractName = fictionalShowNames[i % fictionalShowNames.length];
            String category = (i % 2 == 0) ? "Live Show" : "Theatre"; // Alternate category
            String hall = (i % 3 == 0) ? "Small Hall" : "Main Hall"; // Alternate hall

            // Generate Dates for this contract
            String startDateStr = formatDate(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 20 + (i % 10)); // Add ~3-4 weeks for end date
            String endDateStr = formatDate(cal.getTime());
            List<String> perfDates = new ArrayList<>(); List<String> rehearseDates = new ArrayList<>();
            Calendar tempCal = Calendar.getInstance(); tempCal.setTime(parseDate(startDateStr));
            tempCal.add(Calendar.DAY_OF_MONTH, 3); rehearseDates.add(formatDate(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_MONTH, 4); perfDates.add(formatDate(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_MONTH, 3); rehearseDates.add(formatDate(tempCal.getTime()));
            tempCal.add(Calendar.DAY_OF_MONTH, 4); perfDates.add(formatDate(tempCal.getTime()));

            // Create the Contract object
            Contract newContract = new Contract(
                    contractName, category, hall, startDateStr, endDateStr,
                    perfDates, rehearseDates,
                    companyName, contactName, contactEmail, phoneNumber,
                    streetAddress, city, postcode,
                    billingName, billingEmail
            );

            // Add to the appropriate list
            if ("Live Show".equals(category)) { liveShowContracts.add(newContract); }
            else { theatreContracts.add(newContract); }

            // Increment calendar for the *next* contract's start date
            // Reset day to 1st of next month for cleaner start dates
            cal.setTime(parseDate(endDateStr)); // Start from end date of current contract
            cal.add(Calendar.MONTH, 1); // Go to next month
            cal.set(Calendar.DAY_OF_MONTH, 1); // Set to the 1st
            cal.add(Calendar.DAY_OF_MONTH, (i%7)); // Add a slight offset day (0-6) for variety
        }
    }


    // Refreshes both panels based on the current lists
    private static void refreshContractPanels() {
        refreshPanel(liveShowsPanel, liveShowContracts);
        refreshPanel(theatrePanel, theatreContracts);
    }

    // Refreshes a specific panel with contracts from a list
    private static void refreshPanel(JPanel panel, List<Contract> contractList) {
        if (panel == null) return; // Avoid NPE if called too early

        // Get parent frame reference ONCE for all expandable panels within this refresh
        // This should work now that refresh is called after frame is visible
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
        if (parentFrame == null) {
            Component temp = panel;
            while (temp != null && !(temp instanceof JFrame)) { temp = temp.getParent(); }
            if (temp instanceof JFrame) { parentFrame = (JFrame) temp; }
            else { System.err.println("Refresh Warning: Parent frame not found. Dialogs might not behave modally."); }
        }

        panel.removeAll();
        List<Contract> sortedList = new ArrayList<>(contractList);
        sortedList.sort(Comparator.comparing(Contract::getName, String.CASE_INSENSITIVE_ORDER));

        final JFrame finalParentFrame = parentFrame; // Final variable for use in loop/lambda
        for (Contract contract : sortedList) {
            panel.add(createExpandablePanel(finalParentFrame, contract, liveShowsPanel, theatrePanel));
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing
        }

        SwingUtilities.invokeLater(() -> { panel.revalidate(); panel.repaint(); });
    }

    // --- Add Contract Dialog ---
    private static void showAddContractDialog(JFrame parentFrame, JPanel livePanel, JPanel theatrePanel) {
        JDialog dialog = new JDialog(parentFrame, "Add Contract", true);
        dialog.setSize(550, 700); dialog.setLayout(new BorderLayout()); dialog.setLocationRelativeTo(parentFrame);
        JPanel formPanel = new JPanel(); formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); formPanel.setBackground(Color.WHITE);
        // --- Core Contract Details ---
        JRadioButton liveRadio = new JRadioButton("Live Show"); JRadioButton theatreRadio = new JRadioButton("Theatre");
        ButtonGroup categoryGroup = new ButtonGroup(); categoryGroup.add(liveRadio); categoryGroup.add(theatreRadio);
        liveRadio.setSelected(true); liveRadio.setBackground(Color.WHITE); theatreRadio.setBackground(Color.WHITE);
        formPanel.add(createRadioPanel("Category:", liveRadio, theatreRadio)); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JRadioButton mainHallRadio = new JRadioButton("Main Hall"); JRadioButton smallHallRadio = new JRadioButton("Small Hall");
        ButtonGroup hallGroup = new ButtonGroup(); hallGroup.add(mainHallRadio); hallGroup.add(smallHallRadio);
        mainHallRadio.setSelected(true); mainHallRadio.setBackground(Color.WHITE); smallHallRadio.setBackground(Color.WHITE);
        formPanel.add(createRadioPanel("Hall:", mainHallRadio, smallHallRadio)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextField nameField = new JTextField("", 20); addPlaceholder(nameField, "Enter show/contract name"); // Updated placeholder
        formPanel.add(labeledField("Show Name:", nameField)); formPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Updated label
        final JTextField startDateField = new JTextField("", 15); final JTextField endDateField = new JTextField("", 15);
        setupDateField(startDateField, "Select start date"); setupDateField(endDateField, "Select end date");
        JButton startDateBtn = new JButton("Select"); JButton endDateBtn = new JButton("Select");
        startDateBtn.addActionListener(e -> new SimpleDatePicker(dialog, date -> startDateField.setText(formatDate(date))).showDialog());
        endDateBtn.addActionListener(e -> new SimpleDatePicker(dialog, date -> endDateField.setText(formatDate(date))).showDialog());
        formPanel.add(labeledFieldWithButton("Start Date:", startDateField, startDateBtn)); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(labeledFieldWithButton("End Date:", endDateField, endDateBtn)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        final List<String> perfDates = new ArrayList<>(); JPanel perfDisplayPanel = createDateDisplayPanel();
        JButton addPerfDate = new JButton("Add Performance Date"); addPerfDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPerfDate.addActionListener(e -> addDateWithPicker(dialog, perfDisplayPanel, perfDates));
        formPanel.add(dateListSection("Performance Dates:", perfDisplayPanel, addPerfDate)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        final List<String> rehearseDates = new ArrayList<>(); JPanel rehearseDisplayPanel = createDateDisplayPanel();
        JButton addRehearseDate = new JButton("Add Rehearsal Date"); addRehearseDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        addRehearseDate.addActionListener(e -> addDateWithPicker(dialog, rehearseDisplayPanel, rehearseDates));
        formPanel.add(dateListSection("Rehearsal Dates:", rehearseDisplayPanel, addRehearseDate)); formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        // --- Client Information Section ---
        formPanel.add(new JSeparator()); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel clientLabel = new JLabel("Client Information"); clientLabel.setFont(clientLabel.getFont().deriveFont(Font.BOLD));
        clientLabel.setAlignmentX(Component.LEFT_ALIGNMENT); formPanel.add(clientLabel); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField clientCompanyField = new JTextField("", 25); JTextField clientContactNameField = new JTextField("", 25);
        JTextField clientContactEmailField = new JTextField("", 25); JTextField clientPhoneField = new JTextField("", 25);
        JTextField clientAddressField = new JTextField("", 25); JTextField clientCityField = new JTextField("", 25);
        JTextField clientPostcodeField = new JTextField("", 25); JTextField billingNameField = new JTextField("", 25);
        JTextField billingEmailField = new JTextField("", 25);
        formPanel.add(labeledField("Company Name:", clientCompanyField)); formPanel.add(labeledField("Contact Name:", clientContactNameField));
        formPanel.add(labeledField("Contact Email:", clientContactEmailField)); formPanel.add(labeledField("Phone Number:", clientPhoneField));
        formPanel.add(labeledField("Street Address:", clientAddressField)); formPanel.add(labeledField("City:", clientCityField));
        formPanel.add(labeledField("Postcode:", clientPostcodeField)); formPanel.add(labeledField("Billing Name:", billingNameField));
        formPanel.add(labeledField("Billing Email:", billingEmailField)); formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        // --- Save Button ---
        JButton saveButton = new JButton("Save Contract"); saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty() || name.equals("Enter show/contract name")) { JOptionPane.showMessageDialog(dialog, "Show Name is required.", "Input Error", JOptionPane.WARNING_MESSAGE); return; } // Updated message
            String startDateStr = startDateField.getText().trim(); String endDateStr = endDateField.getText().trim();
            if (startDateStr.isEmpty() || startDateStr.equals("Select start date") || endDateStr.isEmpty() || endDateStr.equals("Select end date")) { JOptionPane.showMessageDialog(dialog, "Start Date and End Date are required.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            Date startDate = parseDate(startDateStr); Date endDate = parseDate(endDateStr);
            if (startDate == null || endDate == null) { JOptionPane.showMessageDialog(dialog, "Invalid date format. Please use the date picker.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            if (startDate.after(endDate)) { JOptionPane.showMessageDialog(dialog, "Start Date cannot be after End Date.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            String category = liveRadio.isSelected() ? "Live Show" : "Theatre"; String hall = mainHallRadio.isSelected() ? "Main Hall" : "Small Hall";
            String clientCompany = clientCompanyField.getText().trim(); String clientContactName = clientContactNameField.getText().trim();
            String clientContactEmail = clientContactEmailField.getText().trim(); String clientPhone = clientPhoneField.getText().trim();
            String clientAddress = clientAddressField.getText().trim(); String clientCity = clientCityField.getText().trim();
            String clientPostcode = clientPostcodeField.getText().trim(); String billingName = billingNameField.getText().trim();
            String billingEmail = billingEmailField.getText().trim();
            Contract newContract = new Contract( name, category, hall, startDateStr, endDateStr, new ArrayList<>(perfDates), new ArrayList<>(rehearseDates),
                    clientCompany, clientContactName, clientContactEmail, clientPhone, clientAddress, clientCity, clientPostcode, billingName, billingEmail );
            if ("Live Show".equals(category)) { liveShowContracts.add(newContract); refreshPanel(livePanel, liveShowContracts); }
            else { theatreContracts.add(newContract); refreshPanel(theatrePanel, theatreContracts); }
            dialog.dispose();
        });
        formPanel.add(saveButton); formPanel.add(Box.createVerticalGlue());
        JScrollPane formScrollPane = new JScrollPane(formPanel); formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(formScrollPane, BorderLayout.CENTER); dialog.setVisible(true);
    }

    // --- Edit Contract Dialog ---
    private static void showEditContractDialog(JFrame parentFrame, Contract contract, JPanel livePanel, JPanel theatrePanel) {
        JDialog editDialog;
        if (parentFrame != null) { editDialog = new JDialog(parentFrame, "Edit Contract: " + contract.getName(), true); }
        else { editDialog = new JDialog((Frame)null, "Edit Contract: " + contract.getName(), true); System.err.println("Edit Contract: Parent frame unknown, dialog might not be modal."); }
        editDialog.setSize(550, 700); editDialog.setLayout(new BorderLayout()); editDialog.setLocationRelativeTo(parentFrame);
        JPanel formPanel = new JPanel(); formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); formPanel.setBackground(Color.WHITE);
        // --- Core Contract Details ---
        JRadioButton liveRadio = new JRadioButton("Live Show"); JRadioButton theatreRadio = new JRadioButton("Theatre");
        ButtonGroup categoryGroup = new ButtonGroup(); categoryGroup.add(liveRadio); categoryGroup.add(theatreRadio);
        liveRadio.setBackground(Color.WHITE); theatreRadio.setBackground(Color.WHITE);
        if ("Live Show".equals(contract.getCategory())) { liveRadio.setSelected(true); } else { theatreRadio.setSelected(true); }
        formPanel.add(createRadioPanel("Category:", liveRadio, theatreRadio)); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JRadioButton mainHallRadio = new JRadioButton("Main Hall"); JRadioButton smallHallRadio = new JRadioButton("Small Hall");
        ButtonGroup hallGroup = new ButtonGroup(); hallGroup.add(mainHallRadio); hallGroup.add(smallHallRadio);
        mainHallRadio.setBackground(Color.WHITE); smallHallRadio.setBackground(Color.WHITE);
        if ("Main Hall".equals(contract.getHall())) { mainHallRadio.setSelected(true); } else { smallHallRadio.setSelected(true); }
        formPanel.add(createRadioPanel("Hall:", mainHallRadio, smallHallRadio)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextField nameField = new JTextField(contract.getName(), 20); // Edit the show name
        formPanel.add(labeledField("Show Name:", nameField)); formPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Updated label
        final JTextField startDateField = new JTextField(contract.getStartDate(), 15); final JTextField endDateField = new JTextField(contract.getEndDate(), 15);
        setupDateField(startDateField, ""); setupDateField(endDateField, "");
        JButton startDateBtn = new JButton("Select"); JButton endDateBtn = new JButton("Select");
        startDateBtn.addActionListener(e -> new SimpleDatePicker(editDialog, date -> startDateField.setText(formatDate(date))).showDialog());
        endDateBtn.addActionListener(e -> new SimpleDatePicker(editDialog, date -> endDateField.setText(formatDate(date))).showDialog());
        formPanel.add(labeledFieldWithButton("Start Date:", startDateField, startDateBtn)); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(labeledFieldWithButton("End Date:", endDateField, endDateBtn)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        final List<String> perfDates = new ArrayList<>(contract.getPerformanceDates()); JPanel perfDisplayPanel = createDateDisplayPanel();
        redrawDatePanel(perfDisplayPanel, perfDates, editDialog); // Initial draw using helper
        JButton addPerfDate = new JButton("Add Performance Date"); addPerfDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPerfDate.addActionListener(e -> addDateWithPicker(editDialog, perfDisplayPanel, perfDates));
        formPanel.add(dateListSection("Performance Dates:", perfDisplayPanel, addPerfDate)); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        final List<String> rehearseDates = new ArrayList<>(contract.getRehearsalDates()); JPanel rehearseDisplayPanel = createDateDisplayPanel();
        redrawDatePanel(rehearseDisplayPanel, rehearseDates, editDialog); // Initial draw using helper
        JButton addRehearseDate = new JButton("Add Rehearsal Date"); addRehearseDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        addRehearseDate.addActionListener(e -> addDateWithPicker(editDialog, rehearseDisplayPanel, rehearseDates));
        formPanel.add(dateListSection("Rehearsal Dates:", rehearseDisplayPanel, addRehearseDate)); formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        // --- Client Information Section ---
        formPanel.add(new JSeparator()); formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel clientLabel = new JLabel("Client Information"); clientLabel.setFont(clientLabel.getFont().deriveFont(Font.BOLD));
        clientLabel.setAlignmentX(Component.LEFT_ALIGNMENT); formPanel.add(clientLabel); formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField clientCompanyField = new JTextField(contract.getClientCompanyName(), 25); JTextField clientContactNameField = new JTextField(contract.getClientContactName(), 25);
        JTextField clientContactEmailField = new JTextField(contract.getClientContactEmail(), 25); JTextField clientPhoneField = new JTextField(contract.getClientPhoneNumber(), 25);
        JTextField clientAddressField = new JTextField(contract.getClientStreetAddress(), 25); JTextField clientCityField = new JTextField(contract.getClientCity(), 25);
        JTextField clientPostcodeField = new JTextField(contract.getClientPostcode(), 25); JTextField billingNameField = new JTextField(contract.getBillingName(), 25);
        JTextField billingEmailField = new JTextField(contract.getBillingEmail(), 25);
        formPanel.add(labeledField("Company Name:", clientCompanyField)); formPanel.add(labeledField("Contact Name:", clientContactNameField));
        formPanel.add(labeledField("Contact Email:", clientContactEmailField)); formPanel.add(labeledField("Phone Number:", clientPhoneField));
        formPanel.add(labeledField("Street Address:", clientAddressField)); formPanel.add(labeledField("City:", clientCityField));
        formPanel.add(labeledField("Postcode:", clientPostcodeField)); formPanel.add(labeledField("Billing Name:", billingNameField));
        formPanel.add(labeledField("Billing Email:", billingEmailField)); formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        // --- Save Button ---
        JButton saveButton = new JButton("Save Changes"); saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim(); if (name.isEmpty()) { JOptionPane.showMessageDialog(editDialog, "Show Name cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE); return; } // Updated message
            String startDateStr = startDateField.getText().trim(); String endDateStr = endDateField.getText().trim();
            if (startDateStr.isEmpty() || endDateStr.isEmpty()) { JOptionPane.showMessageDialog(editDialog, "Start Date and End Date cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            Date startDate = parseDate(startDateStr); Date endDate = parseDate(endDateStr);
            if (startDate == null || endDate == null) { JOptionPane.showMessageDialog(editDialog, "Invalid date format. Please use the date picker.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            if (startDate.after(endDate)) { JOptionPane.showMessageDialog(editDialog, "Start Date cannot be after End Date.", "Input Error", JOptionPane.WARNING_MESSAGE); return; }
            String originalCategory = contract.getCategory(); String newCategory = liveRadio.isSelected() ? "Live Show" : "Theatre";
            String clientCompany = clientCompanyField.getText().trim(); String clientContactName = clientContactNameField.getText().trim();
            String clientContactEmail = clientContactEmailField.getText().trim(); String clientPhone = clientPhoneField.getText().trim();
            String clientAddress = clientAddressField.getText().trim(); String clientCity = clientCityField.getText().trim();
            String clientPostcode = clientPostcodeField.getText().trim(); String billingName = billingNameField.getText().trim();
            String billingEmail = billingEmailField.getText().trim();
            contract.setName(name); contract.setCategory(newCategory); contract.setHall(mainHallRadio.isSelected() ? "Main Hall" : "Small Hall");
            contract.setStartDate(startDateStr); contract.setEndDate(endDateStr); contract.setPerformanceDates(new ArrayList<>(perfDates)); contract.setRehearsalDates(new ArrayList<>(rehearseDates));
            contract.setClientCompanyName(clientCompany); contract.setClientContactName(clientContactName); contract.setClientContactEmail(clientContactEmail);
            contract.setClientPhoneNumber(clientPhone); contract.setClientStreetAddress(clientAddress); contract.setClientCity(clientCity);
            contract.setClientPostcode(clientPostcode); contract.setBillingName(billingName); contract.setBillingEmail(billingEmail);
            if (!originalCategory.equals(newCategory)) {
                if ("Live Show".equals(originalCategory)) { liveShowContracts.remove(contract); theatreContracts.add(contract); }
                else { theatreContracts.remove(contract); liveShowContracts.add(contract); }
                refreshPanel(livePanel, liveShowContracts); refreshPanel(theatrePanel, theatreContracts);
            } else {
                if ("Live Show".equals(newCategory)) { refreshPanel(livePanel, liveShowContracts); } else { refreshPanel(theatrePanel, theatreContracts); } }
            editDialog.dispose();
        });
        formPanel.add(saveButton); formPanel.add(Box.createVerticalGlue());
        JScrollPane formScrollPane = new JScrollPane(formPanel); formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        editDialog.add(formScrollPane, BorderLayout.CENTER); editDialog.setVisible(true);
    }


    // --- Helper Methods for UI Components --- (Minor changes noted, mostly unchanged)

    // Helper to setup common properties for date fields
    private static void setupDateField(JTextField field, String placeholder) {
        field.setEditable(false); field.setBackground(Color.WHITE);
        if (placeholder != null && !placeholder.isEmpty() && field.getText().isEmpty()) { addPlaceholder(field, placeholder); }
        else if (!field.getText().isEmpty()) { field.setForeground(Color.BLACK); } }

    // Helper to create the panel where date entries are displayed
    private static JPanel createDateDisplayPanel() { JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); panel.setBackground(Color.WHITE); panel.setAlignmentX(Component.LEFT_ALIGNMENT); return panel; }

    // Adds placeholder text to a JTextField
    private static void addPlaceholder(JTextField textField, String placeholder) {
        if (textField.getText().isEmpty()) { final boolean[] isPlaceholderActive = {true}; textField.setText(placeholder); textField.setForeground(Color.GRAY);
            textField.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { if (isPlaceholderActive[0] && textField.getText().equals(placeholder)) { textField.setText(""); textField.setForeground(Color.BLACK); isPlaceholderActive[0] = false; } }
                @Override public void focusLost(FocusEvent e) { if (textField.getText().isEmpty()) { textField.setForeground(Color.GRAY); textField.setText(placeholder); isPlaceholderActive[0] = true; } } });
        } else { textField.setForeground(Color.BLACK); } }

    // Creates a panel for radio button groups
    private static JPanel createRadioPanel(String label, JRadioButton radio1, JRadioButton radio2) { JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2)); panel.setBackground(Color.WHITE); JLabel jLabel = new JLabel(label); jLabel.setPreferredSize(new Dimension(110, 20)); panel.add(jLabel); panel.add(radio1); panel.add(radio2); panel.setAlignmentX(Component.LEFT_ALIGNMENT); panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height + 5)); return panel; }

    // Creates a row with a label and a text field
    private static JPanel labeledField(String labelText, JTextField field) { JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2)); panel.setBackground(Color.WHITE); JLabel label = new JLabel(labelText); label.setPreferredSize(new Dimension(110, 20)); panel.add(label); panel.add(field); panel.setAlignmentX(Component.LEFT_ALIGNMENT); panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height + 5)); return panel; }

    // Creates a row with a label, non-editable text field, and a button
    private static JPanel labeledFieldWithButton(String labelText, JTextField field, JButton button) { JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2)); panel.setBackground(Color.WHITE); JLabel label = new JLabel(labelText); label.setPreferredSize(new Dimension(110, 20)); panel.add(label); panel.add(field); panel.add(button); panel.setAlignmentX(Component.LEFT_ALIGNMENT); panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height + 5)); return panel; }

    // Creates a section for listing dates with an add button
    private static JPanel dateListSection(String labelText, JPanel dateDisplayPanel, JButton addButton) { JPanel sectionPanel = new JPanel(); sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS)); sectionPanel.setBackground(Color.WHITE); sectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); JLabel label = new JLabel(labelText); label.setAlignmentX(Component.LEFT_ALIGNMENT); sectionPanel.add(label); sectionPanel.add(Box.createRigidArea(new Dimension(0, 3))); JScrollPane dateScrollPane = new JScrollPane(dateDisplayPanel); dateScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT); dateScrollPane.setPreferredSize(new Dimension(350, 80)); dateScrollPane.setMaximumSize(new Dimension(400, 120)); dateScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); dateScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); dateScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); sectionPanel.add(dateScrollPane); addButton.setAlignmentX(Component.LEFT_ALIGNMENT); sectionPanel.add(Box.createRigidArea(new Dimension(0, 5))); sectionPanel.add(addButton); sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT); sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sectionPanel.getPreferredSize().height + 150)); return sectionPanel; }

    // Opens date picker and adds the selected date to the panel and list
    private static void addDateWithPicker(JDialog parentDialog, JPanel dateDisplayPanel, List<String> dateList) { new SimpleDatePicker(parentDialog, date -> { String formattedDate = formatDate(date); if (!dateList.contains(formattedDate)) { dateList.add(formattedDate); redrawDatePanel(dateDisplayPanel, dateList, parentDialog); SwingUtilities.invokeLater(() -> { Component parent = dateDisplayPanel.getParent(); if (parent instanceof JViewport) { JViewport viewport = (JViewport) parent; Rectangle bounds = dateDisplayPanel.getBounds(); viewport.scrollRectToVisible(new Rectangle(0, Math.max(0, bounds.height - viewport.getHeight() + 20), 1, viewport.getHeight())); } }); } else { JOptionPane.showMessageDialog(parentDialog, "Date already added.", "Duplicate Date", JOptionPane.INFORMATION_MESSAGE); } }).showDialog(); }

    // Adds a date label and delete button to the specified panel
    private static void addDateWithDeleteButton(JPanel dateDisplayPanel, List<String> dates, String date) { JPanel dateEntryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2)); dateEntryPanel.setBackground(Color.WHITE); dateEntryPanel.setAlignmentX(Component.LEFT_ALIGNMENT); JLabel dateLabel = new JLabel(date); JButton deleteButton = new JButton("X"); deleteButton.setMargin(new Insets(1, 4, 1, 4)); deleteButton.setForeground(Color.RED); deleteButton.setFont(deleteButton.getFont().deriveFont(10f)); deleteButton.setToolTipText("Remove date: " + date); final JDialog parentDialog = (JDialog) SwingUtilities.getWindowAncestor(dateDisplayPanel); deleteButton.addActionListener(e -> { dates.remove(date); redrawDatePanel(dateDisplayPanel, dates, parentDialog); }); dateEntryPanel.add(dateLabel); dateEntryPanel.add(deleteButton); dateEntryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateEntryPanel.getPreferredSize().height)); dateDisplayPanel.add(dateEntryPanel); }

    // Helper method to redraw the date panel efficiently
    private static void redrawDatePanel(JPanel dateDisplayPanel, List<String> dates, JDialog parentDialog) { dateDisplayPanel.removeAll(); if (dates != null) { dates.sort(Comparator.comparing( TheatreContractsGUI::parseDate, Comparator.nullsLast(Comparator.naturalOrder()) )); for (String d : dates) { addDateWithDeleteButton(dateDisplayPanel, dates, d); } } SwingUtilities.invokeLater(() -> { dateDisplayPanel.revalidate(); dateDisplayPanel.repaint(); }); }

    // --- Date Formatting and Parsing ---
    private static String formatDate(Date date) { if (date == null) return ""; return DATE_FORMAT.format(date); }
    private static Date parseDate(String dateString) { if (dateString == null || dateString.trim().isEmpty()) { return null; } String cleanDateString = dateString.trim(); if (cleanDateString.equals("Select start date") || cleanDateString.equals("Select end date")) { return null; } try { DATE_FORMAT.setLenient(false); return DATE_FORMAT.parse(cleanDateString); } catch (ParseException e) { System.err.println("Error parsing date string: \"" + cleanDateString + "\" - " + e.getMessage()); return null; } }

    // --- Expandable Panel Creation ---
    private static JPanel createExpandablePanel(JFrame parentFrame, Contract contract, JPanel livePanel, JPanel theatrePanel) { JPanel panel = new JPanel(new BorderLayout(0, 3)); panel.setBackground(new Color(230, 245, 230)); panel.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(Color.GRAY, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10) )); panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55)); JPanel topPanel = new JPanel(new BorderLayout()); topPanel.setOpaque(false); JButton toggleButton = new JButton("▶ " + contract.getName()); toggleButton.setFont(new Font("Arial", Font.BOLD, 16)); toggleButton.setHorizontalAlignment(SwingConstants.LEFT); toggleButton.setBorderPainted(false); toggleButton.setContentAreaFilled(false); toggleButton.setFocusPainted(false); toggleButton.setOpaque(false); toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); JEditorPane detailsArea = new JEditorPane(); detailsArea.setContentType("text/html"); detailsArea.setText(formatContractDetailsHTML(contract)); detailsArea.setEditable(false); detailsArea.setVisible(false); detailsArea.setOpaque(false); detailsArea.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5)); detailsArea.addHyperlinkListener(e -> { if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) { try { if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) { Desktop.getDesktop().browse(e.getURL().toURI()); } else { System.err.println("Desktop Browse not supported."); } } catch (Exception ex) { System.err.println("Error opening link: " + e.getURL()); ex.printStackTrace(); } } }); JScrollPane detailsScrollPane = new JScrollPane(detailsArea); detailsScrollPane.setVisible(false); detailsScrollPane.setOpaque(false); detailsScrollPane.getViewport().setOpaque(false); detailsScrollPane.setBorder(BorderFactory.createEmptyBorder()); toggleButton.addActionListener(e -> { boolean isVisible = detailsScrollPane.isVisible(); detailsScrollPane.setVisible(!isVisible); detailsArea.setVisible(!isVisible); toggleButton.setText((isVisible ? "▶ " : "▼ ") + contract.getName()); SwingUtilities.invokeLater(() -> { if (!isVisible) { try { int availableWidth = panel.getWidth() > 20 ? panel.getWidth() - 40 : 500; detailsArea.setSize(availableWidth, Short.MAX_VALUE); Dimension prefSize = detailsArea.getPreferredSize(); int detailHeight = Math.min(prefSize.height, 400); int prefHeight = topPanel.getPreferredSize().height + detailHeight + 20; panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(55, prefHeight))); } catch (Exception ex) { panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450)); System.err.println("Error calculating expanded size: " + ex.getMessage()); } } else { panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55)); } Component listPanel = panel.getParent(); if (listPanel != null) { listPanel.revalidate(); listPanel.repaint(); } }); }); JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)); buttonPanel.setOpaque(false); JButton editButton = new JButton("Edit"); editButton.setMargin(new Insets(2, 5, 2, 5)); editButton.setToolTipText("Edit contract: " + contract.getName()); editButton.addActionListener(e -> { showEditContractDialog(parentFrame, contract, livePanel, theatrePanel); }); JButton deleteButton = new JButton("Delete"); deleteButton.setMargin(new Insets(2, 5, 2, 5)); deleteButton.setForeground(Color.RED); deleteButton.setToolTipText("Delete contract: " + contract.getName()); deleteButton.addActionListener(e -> { int confirmation = JOptionPane.showConfirmDialog(panel.getParent(), "Are you sure you want to delete the contract '" + contract.getName() + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); if (confirmation == JOptionPane.YES_OPTION) { boolean removed = false; JPanel parentListPanel = null; if ("Live Show".equals(contract.getCategory())) { removed = liveShowContracts.remove(contract); parentListPanel = livePanel; } else if ("Theatre".equals(contract.getCategory())) { removed = theatreContracts.remove(contract); parentListPanel = theatrePanel; } if (removed && parentListPanel != null) { List<Contract> listToRefresh = "Live Show".equals(contract.getCategory()) ? liveShowContracts : theatreContracts; refreshPanel(parentListPanel, listToRefresh); } else if (!removed) { System.err.println("Contract not found in list for deletion: " + contract.getName()); JOptionPane.showMessageDialog(panel.getParent(), "Error: Could not find contract in list to delete.", "Deletion Error", JOptionPane.ERROR_MESSAGE); } } }); buttonPanel.add(editButton); buttonPanel.add(deleteButton); topPanel.add(toggleButton, BorderLayout.CENTER); topPanel.add(buttonPanel, BorderLayout.EAST); panel.add(topPanel, BorderLayout.NORTH); panel.add(detailsScrollPane, BorderLayout.CENTER); return panel; }

    // Formats contract details into basic HTML for JEditorPane
    private static String formatContractDetailsHTML(Contract contract) { StringBuilder html = new StringBuilder("<html><head><style type='text/css'>"); html.append("body { font-family: sans-serif; font-size: 11pt; } b { font-weight: bold; }"); html.append("ul { margin-top: 2px; margin-bottom: 5px; padding-left: 25px; } li { margin-bottom: 2px; }"); html.append("hr { border: 0; border-top: 1px solid #ccc; margin: 8px 0; }"); html.append("</style></head><body>"); html.append("<b>Category:</b> ").append(escapeHTML(contract.getCategory())).append("<br>"); html.append("<b>Hall:</b> ").append(escapeHTML(contract.getHall())).append("<br>"); html.append("<b>Start Date:</b> ").append(escapeHTML(contract.getStartDate())).append("<br>"); html.append("<b>End Date:</b> ").append(escapeHTML(contract.getEndDate())).append("<br><br>"); html.append("<b>Performance Dates:</b>"); appendFormattedDateList(html, contract.getPerformanceDates()); html.append("<b>Rehearsal Dates:</b>"); appendFormattedDateList(html, contract.getRehearsalDates()); html.append("<hr>"); html.append("<b>Client Information:</b><br>"); appendDetailIfNotEmpty(html, "Company:", contract.getClientCompanyName()); appendDetailIfNotEmpty(html, "Contact Name:", contract.getClientContactName()); String email = escapeHTML(contract.getClientContactEmail()); if (email != null && !email.trim().isEmpty()) { html.append("<b>Contact Email:</b> <a href='mailto:").append(email).append("'>").append(email).append("</a><br>"); } appendDetailIfNotEmpty(html, "Phone:", contract.getClientPhoneNumber()); appendDetailIfNotEmpty(html, "Address:", contract.getClientStreetAddress()); appendDetailIfNotEmpty(html, "City:", contract.getClientCity()); appendDetailIfNotEmpty(html, "Postcode:", contract.getClientPostcode()); appendDetailIfNotEmpty(html, "Billing Name:", contract.getBillingName()); String billEmail = escapeHTML(contract.getBillingEmail()); if (billEmail != null && !billEmail.trim().isEmpty()) { html.append("<b>Billing Email:</b> <a href='mailto:").append(billEmail).append("'>").append(billEmail).append("</a><br>"); } html.append("</body></html>"); return html.toString(); }

    // Helper to append a detail line only if the value is not empty
    private static void appendDetailIfNotEmpty(StringBuilder html, String label, String value) { String escapedValue = escapeHTML(value); if (escapedValue != null && !escapedValue.trim().isEmpty()) { html.append("<b>").append(label).append("</b> ").append(escapedValue).append("<br>"); } }

    // Helper to format and append date lists
    private static void appendFormattedDateList(StringBuilder html, List<String> dates) { if (dates == null || dates.isEmpty()) { html.append("<br><i>None</i><br><br>"); } else { List<String> sortedDates = new ArrayList<>(dates); sortedDates.sort(Comparator.comparing( TheatreContractsGUI::parseDate, Comparator.nullsLast(Comparator.naturalOrder()) )); html.append("<ul>"); for(String date : sortedDates) { html.append("<li>").append(escapeHTML(date)).append("</li>"); } html.append("</ul>"); } }

    // Simple HTML escaping
    private static String escapeHTML(String s) { if (s == null) return ""; StringBuilder out = new StringBuilder(Math.max(16, s.length())); for (int i = 0; i < s.length(); i++) { char c = s.charAt(i); if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') { out.append("&#"); out.append((int) c); out.append(';'); } else { out.append(c); } } return out.toString(); }


    // === Reusable SimpleDatePicker class (Nested inside TheatreContractsGUI) ===
    public static class SimpleDatePicker {
        private final JDialog dialog; private final JSpinner monthSpinner; private final JSpinner yearSpinner; private final JPanel calendarPanel;
        private final DatePickerCallback callback; private final Calendar currentCalendar = Calendar.getInstance();
        public interface DatePickerCallback { void onDateSelected(Date date); }
        public SimpleDatePicker(java.awt.Component parent, DatePickerCallback callback) {
            Window parentWindow = SwingUtilities.getWindowAncestor(parent);
            if (parentWindow instanceof JDialog) { dialog = new JDialog((JDialog) parentWindow, "Select Date", true); }
            else if (parentWindow instanceof JFrame) { dialog = new JDialog((JFrame) parentWindow, "Select Date", true); }
            else { dialog = new JDialog((Frame) null, "Select Date", true); System.err.println("SimpleDatePicker parent is not a JFrame or JDialog."); }
            this.callback = callback; dialog.setSize(350, 300); dialog.setLayout(new BorderLayout()); dialog.setLocationRelativeTo(parent); dialog.setResizable(false);
            JPanel topPanel = new JPanel(new FlowLayout()); String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths(); List<String> monthList = Arrays.asList(Arrays.copyOf(months, 12));
            SpinnerListModel monthModel = new SpinnerListModel(monthList); monthSpinner = new JSpinner(monthModel); monthSpinner.setValue(months[currentCalendar.get(Calendar.MONTH)]);
            SpinnerNumberModel yearModel = new SpinnerNumberModel(currentCalendar.get(Calendar.YEAR), 1900, 2100, 1); yearSpinner = new JSpinner(yearModel);
            Dimension spinnerSize = new Dimension(110, monthSpinner.getPreferredSize().height); monthSpinner.setPreferredSize(spinnerSize); yearSpinner.setPreferredSize(new Dimension(70, yearSpinner.getPreferredSize().height));
            topPanel.add(monthSpinner); topPanel.add(yearSpinner); dialog.add(topPanel, BorderLayout.NORTH);
            calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2)); calendarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); dialog.add(calendarPanel, BorderLayout.CENTER);
            monthSpinner.addChangeListener(e -> drawCalendar()); yearSpinner.addChangeListener(e -> drawCalendar()); drawCalendar(); }
        private void drawCalendar() { calendarPanel.removeAll(); String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String day : daysOfWeek) { JLabel dayLabel = new JLabel(day, SwingConstants.CENTER); dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD)); calendarPanel.add(dayLabel); }
            int month = getMonthIndex((String) monthSpinner.getValue()); int year = (Integer) yearSpinner.getValue(); Calendar cal = new GregorianCalendar(year, month, 1);
            int firstDayOfWeekInMonth = cal.get(Calendar.DAY_OF_WEEK); int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = Calendar.SUNDAY; i < firstDayOfWeekInMonth; i++) { calendarPanel.add(new JLabel("")); }
            int today = currentCalendar.get(Calendar.DAY_OF_MONTH); int currentMonth = currentCalendar.get(Calendar.MONTH); int currentYear = currentCalendar.get(Calendar.YEAR);
            for (int day = 1; day <= daysInMonth; day++) { final int d = day; JButton btn = new JButton(String.valueOf(d));
                btn.setMargin(new Insets(2, 2, 2, 2)); btn.setFocusPainted(false); btn.setFont(btn.getFont().deriveFont(10f)); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (day == today && month == currentMonth && year == currentYear) { btn.setBackground(new Color(173, 216, 230)); btn.setFont(btn.getFont().deriveFont(Font.BOLD)); btn.setBorder(BorderFactory.createLineBorder(Color.BLUE)); } else { btn.setBackground(Color.WHITE); }
                btn.addActionListener(e -> { Calendar selectedCal = new GregorianCalendar(year, month, d); callback.onDateSelected(selectedCal.getTime()); dialog.dispose(); }); calendarPanel.add(btn); }
            while (calendarPanel.getComponentCount() % 7 != 0) { calendarPanel.add(new JLabel("")); } calendarPanel.revalidate(); calendarPanel.repaint(); }
        private int getMonthIndex(String monthName) { String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths(); for (int i = 0; i < 12; i++) { if (months[i].equalsIgnoreCase(monthName)) { return i; } } System.err.println("Could not find month index for: " + monthName); return 0; }
        public void showDialog() { if (!dialog.isVisible()) { dialog.setVisible(true); } }
    } // <<<< END OF SimpleDatePicker CLASS


    // === Contract Data Class (Nested inside TheatreContractsGUI) ===
    public static class Contract {
        private String name; private String category; private String hall; private String startDate; private String endDate;
        private List<String> performanceDates; private List<String> rehearsalDates;
        private String clientCompanyName; private String clientContactName; private String clientContactEmail; private String clientPhoneNumber;
        private String clientStreetAddress; private String clientCity; private String clientPostcode; private String billingName; private String billingEmail;
        public Contract(String name, String category, String hall, String startDate, String endDate, List<String> performanceDates, List<String> rehearsalDates,
                        String clientCompanyName, String clientContactName, String clientContactEmail, String clientPhoneNumber, String clientStreetAddress, String clientCity, String clientPostcode, String billingName, String billingEmail) {
            this.name = (name != null) ? name : ""; this.category = (category != null) ? category : ""; this.hall = (hall != null) ? hall : "";
            this.startDate = (startDate != null) ? startDate : ""; this.endDate = (endDate != null) ? endDate : "";
            this.performanceDates = (performanceDates != null) ? new ArrayList<>(performanceDates) : new ArrayList<>();
            this.rehearsalDates = (rehearsalDates != null) ? new ArrayList<>(rehearsalDates) : new ArrayList<>();
            this.clientCompanyName = (clientCompanyName != null) ? clientCompanyName : ""; this.clientContactName = (clientContactName != null) ? clientContactName : "";
            this.clientContactEmail = (clientContactEmail != null) ? clientContactEmail : ""; this.clientPhoneNumber = (clientPhoneNumber != null) ? clientPhoneNumber : "";
            this.clientStreetAddress = (clientStreetAddress != null) ? clientStreetAddress : ""; this.clientCity = (clientCity != null) ? clientCity : "";
            this.clientPostcode = (clientPostcode != null) ? clientPostcode : ""; this.billingName = (billingName != null) ? billingName : ""; this.billingEmail = (billingEmail != null) ? billingEmail : ""; }
        public String getName() { return name; } public String getCategory() { return category; } public String getHall() { return hall; }
        public String getStartDate() { return startDate; } public String getEndDate() { return endDate; }
        public List<String> getPerformanceDates() { return Collections.unmodifiableList(performanceDates); } public List<String> getRehearsalDates() { return Collections.unmodifiableList(rehearsalDates); }
        public String getClientCompanyName() { return clientCompanyName; } public String getClientContactName() { return clientContactName; } public String getClientContactEmail() { return clientContactEmail; }
        public String getClientPhoneNumber() { return clientPhoneNumber; } public String getClientStreetAddress() { return clientStreetAddress; } public String getClientCity() { return clientCity; }
        public String getClientPostcode() { return clientPostcode; } public String getBillingName() { return billingName; } public String getBillingEmail() { return billingEmail; }
        public void setName(String name) { this.name = (name != null) ? name : ""; } public void setCategory(String category) { this.category = (category != null) ? category : ""; }
        public void setHall(String hall) { this.hall = (hall != null) ? hall : ""; } public void setStartDate(String startDate) { this.startDate = (startDate != null) ? startDate : ""; }
        public void setEndDate(String endDate) { this.endDate = (endDate != null) ? endDate : ""; }
        public void setPerformanceDates(List<String> performanceDates) { this.performanceDates = (performanceDates != null) ? new ArrayList<>(performanceDates) : new ArrayList<>(); }
        public void setRehearsalDates(List<String> rehearsalDates) { this.rehearsalDates = (rehearsalDates != null) ? new ArrayList<>(rehearsalDates) : new ArrayList<>(); }
        public void setClientCompanyName(String clientCompanyName) { this.clientCompanyName = (clientCompanyName != null) ? clientCompanyName : ""; } public void setClientContactName(String clientContactName) { this.clientContactName = (clientContactName != null) ? clientContactName : ""; }
        public void setClientContactEmail(String clientContactEmail) { this.clientContactEmail = (clientContactEmail != null) ? clientContactEmail : ""; } public void setClientPhoneNumber(String clientPhoneNumber) { this.clientPhoneNumber = (clientPhoneNumber != null) ? clientPhoneNumber : ""; }
        public void setClientStreetAddress(String clientStreetAddress) { this.clientStreetAddress = (clientStreetAddress != null) ? clientStreetAddress : ""; } public void setClientCity(String clientCity) { this.clientCity = (clientCity != null) ? clientCity : ""; }
        public void setClientPostcode(String clientPostcode) { this.clientPostcode = (clientPostcode != null) ? clientPostcode : ""; } public void setBillingName(String billingName) { this.billingName = (billingName != null) ? billingName : ""; }
        public void setBillingEmail(String billingEmail) { this.billingEmail = (billingEmail != null) ? billingEmail : ""; }
        @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Contract contract = (Contract) o; return Objects.equals(name, contract.name) && Objects.equals(category, contract.category) && Objects.equals(hall, contract.hall) && Objects.equals(startDate, contract.startDate) && Objects.equals(endDate, contract.endDate); }
        @Override public int hashCode() { return Objects.hash(name, category, hall, startDate, endDate); }
        @Override public String toString() { return "Contract{" + "name='" + name + '\'' + ", category='" + category + '\'' + ", startDate='" + startDate + '\'' + '}'; }
    } // <<<< END OF Contract CLASS


} // <<<< END OF MAIN TheatreContractsGUI CLASS