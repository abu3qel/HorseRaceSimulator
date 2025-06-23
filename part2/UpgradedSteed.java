import java.awt.Color;
import java.util.Map;
import java.util.Set;

public class UpgradedSteed {
    private String steedName;
    private String symbol;
    private int trackProgress;
    private boolean hasStumbled;
    private double aptitudeLevel;
    private boolean isChampion;
    private Color stallionColor;
    private Set<String> gear;
    private int triumphCount;
    private double totalAptitudeLevel;
    private int totalCompetitions;
    private double recordTime;
    private double totalTrackLength;
    private double victoryOdds;

    private static final Map<String, Double> GEAR_BONUSES = Map.of(
        "Armor", 1.1,
        "Goggles", 1.05,
        "Boots", 1.03
    );

    public String toString() {
        return this.steedName + " (Odds: " +  this.victoryOdds + ")";
    }

    /**
     * Constructor for the UpgradedSteed class.
     *
     * @param name            The name of the steed.
     * @param symbol          The symbol representing the steed.
     * @param aptitudeLevel   The aptitude level of the steed.
     * @param stallionColor   The body color of the steed.
     * @param gear            The set of gear worn by the steed.
     */
    public UpgradedSteed(String name, String symbol, double aptitudeLevel, Color stallionColor, Set<String> gear) {
        this.steedName = name;
        this.symbol = symbol;
        this.aptitudeLevel = aptitudeLevel;
        this.stallionColor = stallionColor;
        this.gear = gear;
        this.trackProgress = 0;
        this.hasStumbled = false;
        this.isChampion = false;
        this.triumphCount = 0;
        this.totalAptitudeLevel = 0;
        this.totalCompetitions = 0;
        this.recordTime = Double.MAX_VALUE;
        this.totalTrackLength = 0;
        this.victoryOdds = 1.0;
    }

    /**
     * Updates the victory odds of the steed based on its triumph count and aptitude level.
     */
    public void computeVictoryOdds() {
        victoryOdds = 1.0 + (triumphCount * 0.1) + (aptitudeLevel * 0.05);
    }

    /**
     * Increments the triumph count of the steed.
     */
    public void incrementTriumphs() {
        triumphCount++;
    }

    /**
     * Updates the aptitude level of the steed based on the new aptitude level.
     *
     * @param newAptitudeLevel   The new aptitude level of the steed.
     */
    public void updateAptitudeLevel(double newAptitudeLevel) {
        totalAptitudeLevel += newAptitudeLevel;
        aptitudeLevel = totalAptitudeLevel / totalCompetitions;
    }

    /**
     * Returns the name of the steed.
     *
     * @return The name of the steed.
     */
    public String getSteedName() {
        return steedName;
    }

    /**
     * Returns the symbol representing the steed.
     *
     * @return The symbol representing the steed.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the track progress of the steed.
     *
     * @return The track progress of the steed.
     */
    public int getTrackProgress() {
        return trackProgress;
    }

    /**
     * Checks if the steed has stumbled.
     *
     * @return true if the steed has stumbled, false otherwise.
     */
    public boolean hasStumbled() {
        return hasStumbled;
    }

    /**
     * Returns the aptitude level of the steed.
     *
     * @return The aptitude level of the steed.
     */
    public double getAptitudeLevel() {
        return aptitudeLevel;
    }

    /**
     * Checks if the steed is the champion.
     *
     * @return true if the steed is the champion, false otherwise.
     */
    public boolean isChampion() {
        return isChampion;
    }

    /**
     * Returns the body color of the steed.
     *
     * @return The body color of the steed.
     */
    public Color getStallionColor() {
        return stallionColor;
    }

    /**
     * Returns the set of gear worn by the steed.
     *
     * @return The set of gear worn by the steed.
     */
    public Set<String> getGear() {
        return gear;
    }

    /**
     * Returns the triumph count of the steed.
     *
     * @return The triumph count of the steed.
     */
    public int getTriumphCount() {
        return triumphCount;
    }

    /**
     * Returns the record time achieved by the steed.
     *
     * @return The record time achieved by the steed.
     */
    public double getRecordTime() {
        return recordTime;
    }

    /**
     * Returns the victory odds of the steed.
     *
     * @return The victory odds of the steed.
     */
    public double getVictoryOdds() {
        return victoryOdds;
    }

    /**
     * Sets the steed as the champion.
     *
     * @param champion   true to set the steed as the champion, false otherwise.
     */
    public void setChampion(boolean champion) {
        isChampion = champion;
    }

    /**
     * Sets the steed as stumbled.
     */
    public void stumble() {
        hasStumbled = true;
    }

    /**
     * Advances the steed in the track.
     */
    public void gallop() {
        if (!hasStumbled && !isChampion) {
            double gearBonus = computeGearBonus();
            trackProgress += 5 + gearBonus;
        }
    }

    /**
     * Calculates the gear bonus for the steed.
     *
     * @return The gear bonus for the steed.
     */
    private double computeGearBonus() {
        double bonus = 0;
        for (String item : gear) {
            bonus += GEAR_BONUSES.getOrDefault(item, 0.0);
        }
        return bonus;
    }

    /**
     * Resets the steed for a new competition.
     */
    public void resetForNewCompetition() {
        trackProgress = 0;
        hasStumbled = false;
        isChampion = false;
    }

    /**
     * Increments the total number of competitions participated by the steed.
     */
    public void incrementTotalCompetitions() {
        totalCompetitions++;
    }

    /**
     * Updates the record time achieved by the steed.
     *
     * @param time   The new time to compare with the record time.
     */
    public void updateRecordTime(double time) {
        if (time < recordTime) {
            recordTime = time;
        }
    }


    /**
     * Updates the total track length participated by the steed.
     *
     * @param trackLength   The length of the track.
     */
    public void updateTotalTrackLength(int trackLength) {
        totalTrackLength += trackLength;
    }

    /**
     * Advances the steed by eating an apple.
     */
    public void eatApple() {
        if (!hasStumbled && !isChampion) {
            trackProgress += 5;
        }
    }
}