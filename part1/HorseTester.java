package part1;
public class HorseTester {

    public static void main(String[] args) {
        HorseTester tester = new HorseTester();
        tester.testHorse();
    }

    public void testHorse() {
        // Create a Horse instance
        Horse horse = new Horse("Thunder", 'T', 0.8);

        // Test getName
        assert "Thunder".equals(horse.getName()) : "Name does not match";
        
        // Test getSymbol
        assert horse.getSymbol() == 'T' : "Symbol does not match";
        
        // Test getConfidence
        assert horse.getConfidence() == 0.8 : "Initial confidence does not match";
        
        // Test hasFallen (should be false initially)
        assert !horse.hasFallen() : "Horse should not have fallen yet";
        
        // Test getDistanceTravelled (should be 0 initially)
        assert horse.getDistanceTravelled() == 0 : "Initial distance travelled should be 0";
        
        // Test moveForward method
        horse.moveForward();
        assert horse.getDistanceTravelled() == 1 : "Distance travelled after moving forward should be 1";
        
        // Test fall method and boundary conditions for confidence
        horse.fall();
        assert horse.hasFallen() : "Horse should have fallen";
        assert horse.getConfidence() == 0.7 : "Confidence after falling should decrease by 0.1";
        
        // Test falling again to see if confidence goes below 0
        horse.fall(); // Should not allow confidence to go below 0
        assert horse.getConfidence() >= 0 : "Confidence should not go below 0";
        
        // Test setConfidence with valid and invalid values
        horse.setConfidence(0.5); // Valid
        assert horse.getConfidence() == 0.5 : "Confidence should be set to 0.5";
        horse.setConfidence(-0.1); // Invalid, should set confidence to 0.0
        assert horse.getConfidence() == 0.5 : "Confidence should not change when setting to -0.1";
        horse.setConfidence(1.1); // Invalid, should cap at 1.0
        assert horse.getConfidence() == 1.0 : "Confidence should cap at 1.0 when setting to 1.1";
        
        // Test goBackToStart method
        horse.goBackToStart();
        assert horse.getDistanceTravelled() == 0 : "Distance travelled should reset to 0";
        
        // Test setSymbol method
        horse.setSymbol('S');
        assert horse.getSymbol() == 'S' : "Symbol should update to 'S'";
        
        System.out.println("All tests passed.");
    }
}