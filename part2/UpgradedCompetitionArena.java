
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UpgradedCompetitionArena {
    private JFrame window;
    private LinkedHashMap<String, UpgradedSteed> steeds;
    private JPanel trackPanel;
    private int trackLength = 500;
    private Thread competitionThread;
    private JPanel controlPanel;
    private int numberOfSteeds = 0;
    private LinkedHashMap<Integer, UpgradedGambler> gamblers;
    private JPanel gamblerStatsPanel;
    private boolean isConfigured = false;
    private JButton beginCompetitionButton;
    private JPanel statsPanel;
    private JPanel gamblingPanel;
    private JPanel historyPanel;
    private EnhancedBarGraph victoryGraph;
    private EnhancedBarGraph aptitudeGraph;
    private EnhancedBarGraph recordTimeGraph;
    private EnhancedBarGraph oddsGraph;
    private java.util.List<Point> applePositions;

    /**
     * Constructor for the UpgradedCompetitionArena class.
     *
     * @throws IOException if an I/O error occurs.
     */
    public UpgradedCompetitionArena() throws IOException {
        steeds = new LinkedHashMap<>();
        gamblers = new LinkedHashMap<>();
        applePositions = new ArrayList<>();
        initializeInterface();
    }

    /**
     * Initializes the user interface.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void initializeInterface() throws IOException {
        window = new JFrame("Upgraded Equestrian Competition Arena");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 800);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        Image backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/background.jpeg")));
        backgroundImage = backgroundImage.getScaledInstance(window.getWidth(), window.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        controlPanel = createControlPanel();
        trackPanel = createTrackPanel();
        statsPanel = createStatsPanel();
        gamblingPanel = createGamblingPanel();
        historyPanel = createHistoryPanel();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(controlPanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setOpaque(false);
        topPanel.add(createStyledButton("Place Stakes", this::placeStakes));
        topPanel.add(createStyledButton("View Statistics", this::displayStatistics));
        topPanel.add(createStyledButton("View Gambling History", this::viewGamblingHistory));
        topPanel.add(createStyledButton("View Player Info", this::viewPlayerInfo));

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(trackPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        backgroundLabel.add(mainPanel, BorderLayout.CENTER);
        window.setContentPane(backgroundLabel);
        window.setVisible(true);
    }


    /**
     * Views the player information.
     */
    private void viewPlayerInfo() {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("Player Information:\n\n");

        for (Map.Entry<Integer, UpgradedGambler> entry : gamblers.entrySet()) {
            int gamblerIndex = entry.getKey();
            UpgradedGambler gambler = entry.getValue();

            infoBuilder.append("Gambler ").append(gamblerIndex).append(":\n");
            infoBuilder.append("Balance: $").append(String.format("%.2f", gambler.getFunds())).append("\n\n");
        }

        JTextArea infoTextArea = new JTextArea(infoBuilder.toString());
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JOptionPane.showMessageDialog(window, scrollPane, "Player Information", JOptionPane.PLAIN_MESSAGE);
    }

    private void displayStatistics() {
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new GridLayout(2, 2));
    
        victoryGraph = createVictoryGraph();
        aptitudeGraph = createAptitudeGraph();
        recordTimeGraph = createRecordTimeGraph();
        oddsGraph = createOddsGraph();
    
        // Set the titles for each graph
        victoryGraph.setTitle("Number of Victories");
        aptitudeGraph.setTitle("Aptitude Levels");
        recordTimeGraph.setTitle("Record Times");
        oddsGraph.setTitle("Victory Odds");
    
        statisticsPanel.add(victoryGraph);
        statisticsPanel.add(aptitudeGraph);
        statisticsPanel.add(recordTimeGraph);
        statisticsPanel.add(oddsGraph);
    
        JScrollPane scrollPane = new JScrollPane(statisticsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
    
        JOptionPane.showMessageDialog(window, scrollPane, "Competition Statistics", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates the victory graph.
     *
     * @return The victory graph.
     */
    private EnhancedBarGraph createVictoryGraph() {
        EnhancedBarGraph graph = new EnhancedBarGraph();
        graph.setPreferredSize(new Dimension(400, 300));

        for (UpgradedSteed steed : steeds.values()) {
            graph.addBarData(steed.getStallionColor(), steed.getTriumphCount(), steed.getSteedName());
        }

        return graph;
    }

    /**
     * Creates the aptitude graph.
     *
     * @return The aptitude graph.
     */
    private EnhancedBarGraph createAptitudeGraph() {
        EnhancedBarGraph graph = new EnhancedBarGraph();
        graph.setPreferredSize(new Dimension(400, 300));

        for (UpgradedSteed steed : steeds.values()) {
            graph.addBarData(steed.getStallionColor(), steed.getAptitudeLevel(), steed.getSteedName());
        }

        return graph;
    }

    /**
     * Creates the record time graph.
     *
     * @return The record time graph.
     */
    private EnhancedBarGraph createRecordTimeGraph() {
        EnhancedBarGraph graph = new EnhancedBarGraph();
        graph.setPreferredSize(new Dimension(400, 300));

        for (UpgradedSteed steed : steeds.values()) {
            graph.addBarData(steed.getStallionColor(), steed.getRecordTime(), steed.getSteedName());
        }

        return graph;
    }

    /**
     * Creates the odds graph.
     *
     * @return The odds graph.
     */
    private EnhancedBarGraph createOddsGraph() {
        EnhancedBarGraph graph = new EnhancedBarGraph();
        graph.setPreferredSize(new Dimension(400, 300));

        for (UpgradedSteed steed : steeds.values()) {
            graph.addBarData(steed.getStallionColor(), steed.getVictoryOdds(), steed.getSteedName());
        }

        return graph;
    }
    /**
     * Creates the control panel.
     *
     * @return The control panel.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(Box.createRigidArea(new Dimension(0, 80))); // Space between buttons
        panel.add(createStyledButton("Set Track Length", this::configureTrackLength));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(createStyledButton("Set Number of Steeds", this::getNumberOfSteeds));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(createStyledButton("Set Number of Gamblers", this::getNumberOfGamblers));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        panel.add(createStyledButton("Configure Steeds", this::configureSteeds));
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        beginCompetitionButton = createStyledButton("Begin Competition", this::beginCompetition);
        beginCompetitionButton.setEnabled(false);
        panel.add(beginCompetitionButton);

        return panel;
    }

    /**
     * Creates a styled button.
     *
     * @param text    The text of the button.
     * @param action  The action to perform when the button is clicked.
     * @return The styled button.
     */
    private JButton createStyledButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(213, 91, 91));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMargin(new Insets(10, 10, 10, 10));
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * Creates the track panel.
     *
     * @return The track panel.
     */
    private JPanel createTrackPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isConfigured) {
                    renderTrack(g);
                }
            }
        };
        panel.setOpaque(false);
        return panel;
    }

 /**
 * Renders the track.
 *
 * @param g The graphics context.
 */
private void renderTrack(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    int trackWidth = trackPanel.getWidth();
    int trackHeight = trackPanel.getHeight();
    int laneHeight = trackHeight / (steeds.size() + 1);
    int finishLineX = trackWidth - 100;

    for (int i = 0; i < steeds.size(); i++) {
        int laneY = (i + 1) * laneHeight;
        drawLaneBorders(g2d, laneY, trackWidth);
        drawFinishLine(g2d, laneY, laneHeight, finishLineX);
    }

    drawSteeds(g2d, laneHeight, finishLineX);
    drawApples(g2d, laneHeight, finishLineX); // Draw apples after steeds

    g2d.dispose();
}

    /**
     * Draws the lane borders.
     *
     * @param g2d        The Graphics2D object.
     * @param laneY      The y-coordinate of the lane.
     * @param trackWidth The width of the track.
     */
    private void drawLaneBorders(Graphics2D g2d, int laneY, int trackWidth) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2d.drawLine(50, laneY, trackWidth - 50, laneY);
    }

    /**
     * Draws the finish line.
     *
     * @param g2d          The Graphics2D object.
     * @param laneY        The y-coordinate of the lane.
     * @param laneHeight   The height of each lane.
     * @param finishLineX  The x-coordinate of the finish line.
     */
    private void drawFinishLine(Graphics2D g2d, int laneY, int laneHeight, int finishLineX) {
        g2d.setFont(new Font("Arial", Font.PLAIN, laneHeight));
        g2d.drawString("🏁", finishLineX, laneY);
    }

    /**
     * Draws the steeds on the track.
     *
     * @param g2d          The Graphics2D object.
     * @param laneHeight   The height of each lane.
     * @param finishLineX  The x-coordinate of the finish line.
     */
    private void drawSteeds(Graphics2D g2d, int laneHeight, int finishLineX) {
        int steedY = laneHeight / 2;

        for (UpgradedSteed steed : steeds.values()) {
            drawSteed(g2d, steed, steedY, finishLineX);
            steedY += laneHeight;
        }
    }
/**
 * Draws the apples on the track.
 *
 * @param g2d          The Graphics2D object.
 * @param laneHeight   The height of each lane.
 * @param finishLineX  The x-coordinate of the finish line.
 */
private void drawApples(Graphics2D g2d, int laneHeight, int finishLineX) {

    int appleTextOffset = 5;

    for (Point applePosition : applePositions) {
        int appleX = applePosition.x;
        int appleY = applePosition.y;
        if (appleX < finishLineX) {
       
           
            g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            g2d.drawString("🍎", appleX + appleTextOffset, appleY + appleTextOffset);
        }
    }
}  
    /**
     * Draws a steed on the track.
     *
     * @param g2d          The Graphics2D object.
     * @param steed        The steed to draw.
     * @param steedY       The y-coordinate of the steed.
     * @param finishLineX  The x-coordinate of the finish line.
     */
    private void drawSteed(Graphics2D g2d, UpgradedSteed steed, int steedY, int finishLineX) {
        int steedX = calculateSteedX(steed, finishLineX);
        int steedSize = 30;
        int steedTextOffset = 10;

        g2d.setColor(steed.getStallionColor());
        g2d.setFont(new Font("Arial", Font.BOLD, steedSize));
        g2d.drawString(steed.getSymbol(), steedX, steedY);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(steed.getSteedName(), steedX + steedTextOffset, steedY + steedTextOffset);

        if (steed.hasStumbled()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, steedSize));
            g2d.drawString("X", steedX, steedY);
        }
    }

    /**
     * Calculates the x-coordinate of a steed on the track.
     *
     * @param steed        The steed.
     * @param finishLineX  The x-coordinate of the finish line.
     * @return The x-coordinate of the steed.
     */
    private int calculateSteedX(UpgradedSteed steed, int finishLineX) {
        int steedProgress = steed.getTrackProgress();
        int steedX = 50 + (int) ((double) steedProgress / trackLength * (finishLineX - 50));
        return Math.min(steedX, finishLineX);
    }


    /**
     * Creates the stats panel.
     *
     * @return The stats panel.
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        victoryGraph = new EnhancedBarGraph();
        aptitudeGraph = new EnhancedBarGraph();
        recordTimeGraph = new EnhancedBarGraph();
        oddsGraph = new EnhancedBarGraph();

        panel.add(createGraphPanel("Victories", victoryGraph));
        panel.add(createGraphPanel("Aptitude Levels", aptitudeGraph));
        panel.add(createGraphPanel("Record Times", recordTimeGraph));
        panel.add(createGraphPanel("Win Odds", oddsGraph));

        return panel;
    }

    /**
     * Creates a graph panel.
     *
     * @param title    The title of the graph.
     * @param graph    The graph to be displayed.
     * @return The graph panel.
     */
    private JPanel createGraphPanel(String title, EnhancedBarGraph graph) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        graph.setPreferredSize(new Dimension(300, 200));
        panel.add(graph, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the gambling panel.
     *
     * @return The gambling panel.
     */
    private JPanel createGamblingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Gambling");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createStyledButton("Place Stakes", this::placeStakes));

        return panel;
    }

    /**
     * Creates the history panel.
     *
     * @return The history panel.
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createStyledButton("View Gambling History", this::viewGamblingHistory));

        return panel;
    }


        /**
        
        Configures the track length.
        */
        private void configureTrackLength() {
        String input = JOptionPane.showInputDialog(window, "Enter the track length (300-500):");
        if (input != null && !input.isEmpty()) {
        try {
        trackLength = Integer.parseInt(input);
        if (trackLength < 300 || trackLength > 500) {
        JOptionPane.showMessageDialog(window, "Please enter a number between 300 and 500.");
        configureTrackLength();
        }
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(window, "Invalid input. Please enter a valid number.");
        configureTrackLength();
        }
        }
        }
        
        /**
        
        Gets the number of steeds.
        */
        private void getNumberOfSteeds() {
        String input = JOptionPane.showInputDialog(window, "Enter the number of steeds (2-5):");
        if (input != null && !input.isEmpty()) {
        try {
        numberOfSteeds = Integer.parseInt(input);
        if (numberOfSteeds < 2 || numberOfSteeds > 5) {
        JOptionPane.showMessageDialog(window, "Please enter a number between 2 and 5.");
        getNumberOfSteeds();
        }
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(window, "Invalid input. Please enter a valid number.");
        getNumberOfSteeds();
        }
        }
        }
        
        /**
        
        Gets the number of gamblers.
        */
        private void getNumberOfGamblers() {
        gamblers.clear();
        String input = JOptionPane.showInputDialog(window, "Enter the number of gamblers (2-4):");
        if (input != null && !input.isEmpty()) {
        try {
        int numGamblers = Integer.parseInt(input);
        if (numGamblers < 2 || numGamblers > 4) {
        JOptionPane.showMessageDialog(window, "Please enter a number between 2 and 4.");
        getNumberOfGamblers();
        } else {
        for (int i = 1; i <= numGamblers; i++) {
        gamblers.put(i, new UpgradedGambler(1000.0));
        }
        updateGamblerStatsDisplay();
        }
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(window, "Invalid input. Please enter a valid number.");
        getNumberOfGamblers();
        }
        }
        }
        
        /**
        
        Configures the steeds.
        */
        private void configureSteeds() {
            steeds.clear();
            for (int i = 1; i <= numberOfSteeds; i++) {
            UpgradedSteed steed = getSteedConfiguration(i);
            while (steed == null) {
            steed = getSteedConfiguration(i);
            }
            if (steed != null) {
            steeds.put(steed.getSteedName(), steed);
            }
            }
            isConfigured = true;
            beginCompetitionButton.setEnabled(true);
            trackPanel.repaint();
        }
        
        /**
        
        Gets the configuration of a steed.
        
        @param steedNumber The steed number.
        @return The configured steed, or null if the configuration was canceled.
        */
        private UpgradedSteed getSteedConfiguration(int steedNumber) {
            JPanel configPanel = createSteedConfigPanel(steedNumber);
            int option = JOptionPane.showConfirmDialog(window, configPanel, "Configure Steed " + steedNumber,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
            String name = getTextFieldValue(configPanel, "Name");
            double aptitudeLevel = getDoubleValue(configPanel, "Aptitude Level");
            Color stallionColor = getSelectedColor(configPanel, "Stallion Color");
            String symbol = getSelectedSymbol(configPanel, "Symbol");
            Set<String> gear = getSelectedGear(configPanel);
            return new UpgradedSteed(name, symbol, aptitudeLevel, stallionColor, gear);}
            return null;
        }
        
        /**
        
        Creates the steed configuration panel.
        
        @param steedNumber The steed number.
        @return The steed configuration panel.
        */
        private JPanel createSteedConfigPanel(int steedNumber) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        addLabeledComponent(panel, constraints, "Name:", new JTextField(10));
        addLabeledComponent(panel, constraints, "Aptitude Level (0-100):", new JTextField(10));
        String[] colorOptions = {"Red", "Blue", "Green", "Yellow"};
        addLabeledComponent(panel, constraints, "Stallion Color:", new JComboBox<>(colorOptions));
        String[] symbolOptions = {"🐎", "🦄", "🐴", "🏇"};
        addLabeledComponent(panel, constraints, "Symbol:", new JComboBox<>(symbolOptions));
        addLabeledComponent(panel, constraints, "Gear:", createGearSelectionPanel());
        return panel;
        }
        
        /**
        
        Adds a labeled component to the panel.
        
        @param panel        The panel to add the component to.
        @param constraints  The grid bag constraints.
        @param labelText    The text of the label.
        @param component    The component to add.
        */
        private void addLabeledComponent(JPanel panel, GridBagConstraints constraints, String labelText, JComponent component) {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(component, constraints);
        }
        
        /**
        
        Creates the gear selection panel.
        
        @return The gear selection panel.
        */
        private JPanel createGearSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(new JCheckBox("Armor"));
        panel.add(new JCheckBox("Goggles"));
        panel.add(new JCheckBox("Boots"));
        return panel;
        }
        
        /**
        
        Gets the value of a text field in the panel.
        
        @param panel      The panel.
        @param labelText  The text of the label associated with the text field.
        @return The value of the text field.
        */
        private String getTextFieldValue(JPanel panel, String labelText) {
        for (Component component : panel.getComponents()) {
        if (component instanceof JLabel && ((JLabel) component).getText().equals(labelText + ":")) {
        Component nextComponent = panel.getComponent(panel.getComponentZOrder(component) + 1);
        if (nextComponent instanceof JTextField) {
        return ((JTextField) nextComponent).getText();
        }
        }
        }
        return "";
        }
        
        /**
        
        Gets the value of a double field in the panel.
        
        @param panel      The panel.
        @param labelText  The text of the label associated with the double field.
        @return The value of the double field.
        */
        private double getDoubleValue(JPanel panel, String labelText) {
        String value = getTextFieldValue(panel, labelText);
        try {
        return Double.parseDouble(value);
        } catch (NumberFormatException e) {
        return 0.0;
        }
        }
        
        /**
        
        Gets the selected color from a combo box in the panel.
        
        @param panel      The panel.
        @param labelText  The text of the label associated with the combo box.
        @return The selected color.
        */
        private Color getSelectedColor(JPanel panel, String labelText) {
        for (Component component : panel.getComponents()) {
        if (component instanceof JLabel && ((JLabel) component).getText().equals(labelText + ":")) {
        Component nextComponent = panel.getComponent(panel.getComponentZOrder(component) + 1);
        if (nextComponent instanceof JComboBox) {
        String selectedColor = (String) ((JComboBox<?>) nextComponent).getSelectedItem();
        return getColorByName(selectedColor);
        }
        }
        }
        return Color.BLACK;
        }
        
        /**
        
        Gets the color based on its name.
        
        @param colorName The name of the color.
        @return The color.
        */
        private Color getColorByName(String colorName) {
        return switch (colorName) {
        case "Red" -> Color.RED;
        case "Blue" -> Color.BLUE;
        case "Green" -> Color.GREEN;
        case "Yellow" -> Color.YELLOW;
        default -> Color.BLACK;
        };
        }
        
        /**
        
        Gets the selected symbol from a combo box in the panel.
        
        @param panel      The panel.
        @param labelText  The text of the label associated with the combo box.
        @return The selected symbol.
        */
        private String getSelectedSymbol(JPanel panel, String labelText) {
        for (Component component : panel.getComponents()) {
        if (component instanceof JLabel && ((JLabel) component).getText().equals(labelText + ":")) {
        Component nextComponent = panel.getComponent(panel.getComponentZOrder(component) + 1);
        if (nextComponent instanceof JComboBox) {
        return (String) ((JComboBox<?>) nextComponent).getSelectedItem();
        }
        }
        }
        return "";
        }
        
        /**
        
        Gets the selected gear from checkboxes in the panel.
        
        @param panel The panel.
        @return The set of selected gear.
        */
        private Set<String> getSelectedGear(JPanel panel) {
        Set<String> selectedGear = new HashSet<>();
        for (Component component : panel.getComponents()) {
        if (component instanceof JPanel) {
        JPanel gearPanel = (JPanel) component;
        for (Component gearComponent : gearPanel.getComponents()) {
        if (gearComponent instanceof JCheckBox && ((JCheckBox) gearComponent).isSelected()) {
        selectedGear.add(((JCheckBox) gearComponent).getText());
        }
        }
        }
        }
        return selectedGear;
        }
        
        /**
        
        Begins the competition.
        */
        private void beginCompetition() {
        if (steeds.isEmpty() || gamblers.isEmpty()) {
        return;
        }
        if (competitionThread != null && competitionThread.isAlive()) {
        competitionThread.interrupt();
        }
        prepareSteeds();
        competitionThread = new Thread(this::simulateCompetition);
        competitionThread.start();
        }
        
        /**
        
        Prepares the steeds for the competition.
        */
        private void prepareSteeds() {
        for (UpgradedSteed steed : steeds.values()) {
        steed.resetForNewCompetition();
        steed.computeVictoryOdds();
        }
        }
       /**
 * Simulates the competition.
 */
private void simulateCompetition() {
    boolean competitionFinished = false;
    generateApples();
    while (!competitionFinished) {
        for (UpgradedSteed steed : steeds.values()) {
            if (!steed.hasStumbled() && !steed.isChampion()) {
                advanceSteed(steed);
                checkSteedProgress(steed);
                checkAppleConsumption(steed);
            }
        }
        trackPanel.repaint();

        if (isCompetitionFinished()) {
            competitionFinished = true;
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            break;
        }
    }
    UpgradedSteed championSteed = getChampionSteed();
    displayCompetitionResult(championSteed);
    updateSteedStatistics(championSteed);
    settleGamblerStakes(championSteed);
    updateCompetitionStatistics();
    updateGamblerStatsDisplay();
    updateStatisticsGraphs();
    applePositions.clear();
}
 
/**
 * Generates apples on the track.
 */
private void generateApples() {
    int numApples = 2;
    int laneHeight = trackPanel.getHeight() / (steeds.size() + 1);

    for (int i = 0; i < numApples; i++) {
        int laneIndex = (int) (Math.random() * steeds.size());
        int laneY = (laneIndex + 1) * laneHeight;
        int appleY = laneY - laneHeight / 2;

        UpgradedSteed steed = new ArrayList<>(steeds.values()).get(laneIndex);
        int steedX = calculateSteedX(steed, trackPanel.getWidth() - 100);
        int appleX = steedX + (int) (Math.random() * (trackLength - steedX - 50));

        applePositions.add(new Point(appleX, appleY));
    }
}
    /**
     
    Advances a steed in the competition.
    
    @param steed The steed to advance.
    */
    private void advanceSteed(UpgradedSteed steed) {
    double randomValue = Math.random();
    if (randomValue < 0.02) {
    steed.stumble();
    } else {
    steed.gallop();
    }
    }
    
    /**
     
    Checks the progress of a steed.
    
    @param steed The steed to check.
    */
    private void checkSteedProgress(UpgradedSteed steed) {
    if (steed.getTrackProgress() >= trackLength) {
    steed.setChampion(true);
    }
    }
 
 /**
 * Checks if a steed consumes an apple.
 *
 * @param steed The steed to check.
 */
private void checkAppleConsumption(UpgradedSteed steed) {
    int steedX = calculateSteedX(steed, trackPanel.getWidth() - 100);
    int steedY = getSteedY(steed);
    Iterator<Point> iterator = applePositions.iterator();
    while (iterator.hasNext()) {
        Point applePosition = iterator.next();
        if (isAppleConsumed(steedX, steedY, applePosition)) {
          
            steed.eatApple();
            iterator.remove();
            for(int i=0; i<3; i++){
                steed.gallop();
            }
            trackPanel.repaint(); // Repaint the track to reflect the changes
            break; // Only consume one apple per iteration
        }
    }
}
    
private boolean isAppleConsumed(int steedX, int steedY, Point applePosition) {
    
    int appleX = applePosition.x;
    int appleY = applePosition.y+120;
    int tolerance = 25; // Adjust the tolerance value as needed

    return Math.abs(steedX - appleX) <= tolerance && Math.abs(steedY - appleY) <= tolerance;
}
 /**
 
 Gets the y-coordinate of a steed on the track.
 
 @param steed The steed.

@return The y-coordinate of the steed.
*/
private int getSteedY(UpgradedSteed steed) {
    int numSteeds = steeds.size();
    int steedIndex = new ArrayList<>(steeds.values()).indexOf(steed);
    int trackHeight = trackPanel.getHeight() - 100;
    int steedSpacing = trackHeight / (numSteeds + 1);
    return 50 + (steedIndex + 1) * steedSpacing;
    }
    
    /**
    
    Checks if the competition is finished.
    
    @return true if the competition is finished, false otherwise.
    */
    private boolean isCompetitionFinished() {
        return steeds.values().stream().anyMatch(UpgradedSteed::isChampion)
        || steeds.values().stream().allMatch(UpgradedSteed::hasStumbled);
    }
    
    /**
    
    Gets the champion steed of the competition.
    
    @return The champion steed, or null if there is no champion.
    */
    private UpgradedSteed getChampionSteed() {
        return steeds.values().stream()
        .filter(UpgradedSteed::isChampion)
        .findFirst()
        .orElse(null);
    }
    
    /**
    
    Displays the result of the competition.
    
    @param championSteed The champion steed.
    */
    private void displayCompetitionResult(UpgradedSteed championSteed) {
    if (championSteed != null) {
    JOptionPane.showMessageDialog(window, "The champion is: " + championSteed.getSteedName());
    } else {
    JOptionPane.showMessageDialog(window, "No champion. All steeds have stumbled.");
    }
    }
    
    /**
    
    Updates the statistics of the steeds.
    
    @param championSteed The champion steed.
    */
    private void updateSteedStatistics(UpgradedSteed championSteed) {
        if (championSteed != null) {
        championSteed.incrementTriumphs();
        championSteed.updateAptitudeLevel(10);
        }
        for (UpgradedSteed steed : steeds.values()) {
        steed.resetForNewCompetition();
        steed.computeVictoryOdds();
        }
    }
    
    /**
    
    Settles the stakes of the gamblers.
    
    @param championSteed The champion steed.
    */
    private void settleGamblerStakes(UpgradedSteed championSteed) {
        for (UpgradedGambler gambler : gamblers.values()) {
        gambler.settleStake(championSteed);
        }
    }
    
    /**
    
    Updates the competition statistics.
    */
    private void updateCompetitionStatistics() {
        for (UpgradedSteed steed : steeds.values()) {
        steed.incrementTotalCompetitions();
        steed.updateRecordTime(steed.getTrackProgress() / 100.0);
        steed.updateTotalTrackLength(trackLength);
        }
    }
    
    /**
    
    Places a stake for a gambler.
    
    @param gambler      The gambler.
    @param gamblerIndex The index of the gambler.
    */
    private void placeStakeForGambler(UpgradedGambler gambler, int gamblerIndex) {
    JPanel stakePanel = createStakePanel();
    int option = JOptionPane.showConfirmDialog(window, stakePanel, "Place Stake - Gambler " + gamblerIndex,
    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (option == JOptionPane.OK_OPTION) {
    String selectedSteedName = getSelectedSteedName(stakePanel);
    double stakeAmount = getStakeAmount(stakePanel);
    UpgradedSteed selectedSteed = steeds.get(selectedSteedName);
    if (selectedSteed != null) {
        boolean stakeSuccessful = gambler.placeStake(selectedSteed, stakeAmount);
        if (!stakeSuccessful) {
            JOptionPane.showMessageDialog(window, "Insufficient funds to place the stake.");
        }
 }
}
}

    /**

    Creates the stake panel.

    @return The stake panel.
    */
    private JPanel createStakePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Select Steed:"));
        panel.add(new JComboBox<>(steeds.values().toArray(new UpgradedSteed[0])));
        panel.add(new JLabel("Stake Amount:"));
        panel.add(new JTextField());
        return panel;
    }

/**

Gets the selected steed name from the stake panel.

@param stakePanel The stake panel.
@return The selected steed name.
*/
private String getSelectedSteedName(JPanel stakePanel) {
    for (Component component : stakePanel.getComponents()) {
        if (component instanceof JComboBox) {
            UpgradedSteed selectedSteed = (UpgradedSteed) ((JComboBox<?>) component).getSelectedItem();
            if (selectedSteed != null) {
                return selectedSteed.getSteedName();
            }
        }
    }
    return "";
}
    /**

    Gets the stake amount from the stake panel.

    @param stakePanel The stake panel.
    @return The stake amount.
    */
    private double getStakeAmount(JPanel stakePanel) {
        for (Component component : stakePanel.getComponents()) {
            if (component instanceof JTextField) {
            String amountText = ((JTextField) component).getText();
            try {
            return Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
            return 0.0;
            }
            }
        }
    return 0.0;
    }

/**

    Places stakes for all gamblers.
    */
    private void placeStakes() {
        for (int gamblerIndex : gamblers.keySet()) {
            UpgradedGambler gambler = gamblers.get(gamblerIndex);
            placeStakeForGambler(gambler, gamblerIndex);
        }
    }

/**

Views the gambling history.
*/
    private void viewGamblingHistory() {
    StringBuilder historyBuilder = new StringBuilder();
    historyBuilder.append("Gambling History:\n\n");
    for (Map.Entry<Integer, UpgradedGambler> entry : gamblers.entrySet()) {
        int gamblerIndex = entry.getKey();
        UpgradedGambler gambler = entry.getValue();
        historyBuilder.append("Gambler ").append(gamblerIndex).append(":\n");

        for (Map.Entry<UpgradedSteed, ArrayList<UpgradedGambler.Stake>> stakeEntry : gambler.getGamblingHistory().entrySet()) {
            UpgradedSteed steed = stakeEntry.getKey();
            List<UpgradedGambler.Stake> stakes = stakeEntry.getValue();

            for (UpgradedGambler.Stake stake : stakes) {
                historyBuilder.append("- Steed: ").append(steed.getSteedName());
                historyBuilder.append(", Amount: $").append(String.format("%.2f", stake.getAmount()));
                historyBuilder.append(", Result: ").append(stake.isWinning() ? "Win" : "Loss");
                historyBuilder.append("\n");
            }
        }

    historyBuilder.append("\n");
    }
        JTextArea historyTextArea = new JTextArea(historyBuilder.toString());
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(window, scrollPane, "Gambling History", JOptionPane.PLAIN_MESSAGE);
    }

/**

Updates the gambler stats display.
*/
private void updateGamblerStatsDisplay() {
        if (gamblerStatsPanel != null) {
            gamblerStatsPanel.removeAll();
        } else {
            gamblerStatsPanel = new JPanel();
            gamblerStatsPanel.setLayout(new BoxLayout(gamblerStatsPanel, BoxLayout.Y_AXIS));
            gamblerStatsPanel.setOpaque(false);
        }
    for (Map.Entry<Integer, UpgradedGambler> entry : gamblers.entrySet()) {
        int gamblerIndex = entry.getKey();
        UpgradedGambler gambler = entry.getValue();
        JPanel gamblerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel gamblerLabel = new JLabel("Gambler " + gamblerIndex + ":");
        gamblerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gamblerPanel.add(gamblerLabel);

        JLabel fundsLabel = new JLabel("Funds: $" + String.format("%.2f", gambler.getFunds()));
        fundsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gamblerPanel.add(fundsLabel);

        JLabel triumphsLabel = new JLabel("Total Triumphs: " + gambler.getTotalTriumphs());
        triumphsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gamblerPanel.add(triumphsLabel);

        JLabel winPercentageLabel = new JLabel("Win Percentage: " + String.format("%.2f", gambler.getWinPercentage()) + "%");
        winPercentageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gamblerPanel.add(winPercentageLabel);

        gamblerStatsPanel.add(gamblerPanel);
    }
    if (gamblerStatsPanel != null) {
        gamblerStatsPanel.revalidate();
        gamblerStatsPanel.repaint();
    }
}

/**

    Updates the statistics graphs.
    */
    private void updateStatisticsGraphs() {
        victoryGraph.removeAll();
        aptitudeGraph.removeAll();
        recordTimeGraph.removeAll();
        oddsGraph.removeAll();
            for (UpgradedSteed steed : steeds.values()) {
                victoryGraph.addBarData(steed.getStallionColor(), steed.getTriumphCount(), steed.getSteedName());
                aptitudeGraph.addBarData(steed.getStallionColor(), steed.getAptitudeLevel(), steed.getSteedName());
                recordTimeGraph.addBarData(steed.getStallionColor(), steed.getRecordTime(), steed.getSteedName());
                oddsGraph.addBarData(steed.getStallionColor(), steed.getVictoryOdds(), steed.getSteedName());
            }
        victoryGraph.repaint();
        aptitudeGraph.repaint();
        recordTimeGraph.repaint();
        oddsGraph.repaint();
    }

/**

The main method to run the Upgraded Competition Arena.

@param args The command line arguments.
*/
public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
try {
new UpgradedCompetitionArena();
} catch (IOException e) {
e.printStackTrace();
}
});
}
}