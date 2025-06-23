import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class UpgradedGambler {
    private double funds;
    private int totalTriumphs;
    private double totalEarnings;
    private Map<UpgradedSteed, ArrayList<Stake>> gamblingHistory;
    private Stake currentStake;

    /**
     * Constructor for the UpgradedGambler class.
     *
     * @param initialFunds   The initial funds of the gambler.
     */
    public UpgradedGambler(double initialFunds) {
        this.funds = initialFunds;
        this.totalTriumphs = 0;
        this.totalEarnings = 0;
        this.gamblingHistory = new HashMap<>();
    }

    /**
     * Places a stake on a steed.
     *
     * @param steed         The steed to place the stake on.
     * @param stakeAmount   The amount of the stake.
     * @return true if the stake is placed successfully, false otherwise.
     */
    public boolean placeStake(UpgradedSteed steed, double stakeAmount) {
        if (funds >= stakeAmount) {
            funds -= stakeAmount;
            currentStake = new Stake(steed, stakeAmount);
            gamblingHistory.computeIfAbsent(steed, h -> new ArrayList<>()).add(currentStake);
            return true;
        }
        return false;
    }

    /**
     * Settles the stake based on the champion steed.
     *
     * @param championSteed   The champion steed of the competition.
     */
    public void settleStake(UpgradedSteed championSteed) {
        if (currentStake != null && championSteed != null && championSteed.equals(currentStake.getSteed())) {
            double earnings = currentStake.getAmount() * championSteed.getVictoryOdds();
            funds += earnings + currentStake.getAmount();
            totalTriumphs++;
            totalEarnings += earnings;
            currentStake.setWinning(true);
        }
        currentStake = null;
    }

    /**
     * Returns the current funds of the gambler.
     *
     * @return The current funds of the gambler.
     */
    public double getFunds() {
        return funds;
    }

    /**
     * Returns the total number of triumphs of the gambler.
     *
     * @return The total number of triumphs of the gambler.
     */
    public int getTotalTriumphs() {
        return totalTriumphs;
    }

    /**
     * Returns the total earnings of the gambler.
     *
     * @return The total earnings of the gambler.
     */
    public double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Returns the gambling history of the gambler.
     *
     * @return The gambling history of the gambler.
     */
    public Map<UpgradedSteed, ArrayList<Stake>> getGamblingHistory() {
        return gamblingHistory;
    }

    /**
     * Calculates the win percentage of the gambler.
     *
     * @return The win percentage of the gambler.
     */
    public double getWinPercentage() {
        int totalStakes = gamblingHistory.values().stream().mapToInt(List::size).sum();
        return totalStakes > 0 ? (double) totalTriumphs / totalStakes * 100 : 0;
    }

    /**
     * Inner class representing a stake.
     */
    static class Stake {
        private final UpgradedSteed steed;
        private final double amount;
        private boolean isWinning;

        /**
         * Constructor for the Stake class.
         *
         * @param steed    The steed associated with the stake.
         * @param amount   The amount of the stake.
         */
        public Stake(UpgradedSteed steed, double amount) {
            this.steed = steed;
            this.amount = amount;
            this.isWinning = false;
        }

        /**
         * Returns the steed associated with the stake.
         *
         * @return The steed associated with the stake.
         */
        public UpgradedSteed getSteed() {
            return steed;
        }

        /**
         * Returns the amount of the stake.
         *
         * @return The amount of the stake.
         */
        public double getAmount() {
            return amount;
        }

        /**
         * Checks if the stake is winning.
         *
         * @return true if the stake is winning, false otherwise.
         */
        public boolean isWinning() {
            return isWinning;
        }

        /**
         * Sets the winning status of the stake.
         *
         * @param winning   true to set the stake as winning, false otherwise.
         */
        public void setWinning(boolean winning) {
            isWinning = winning;
        }
    }
}