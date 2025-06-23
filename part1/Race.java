package part1;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private Horse [] participants;
    private Horse winner;
    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 

     */
    public Horse addHorse(String name, char horseSymbol,  double confidenceRating)
    {
        return new Horse(name, horseSymbol, confidenceRating);
    }

    public void setHorses(int numHorses){
        Horse [] horses = new Horse[numHorses];
        for(int i=0; i<numHorses; i++){
            String name = inputString("What is the name of Horse #" + (i+1));
            char horseSymbol = inputChar("What is the symbol of Horse #" + (i+1));
            double confidenceRating = inputDouble("What is the confidence rating of Horse #" + (i+1), 1.0, 0.0);
            horses[i]=addHorse(name, horseSymbol, confidenceRating);
        }

        this.participants=horses;
    }
    
    //takes in a string message and prints it to the screen, then takes in a string from the keyboard
    public static String inputString(String message){
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        String inputed="";
 
        inputed= scanner.nextLine();

    
        return inputed;
    }

    //takes in a message and prints it to the screen, then takes in integer from the keyboard
    public static int inputInt(String message, int topLimit, int bottomLimit){
        String response = inputString(message);
        int result=0;
        try{
            result = Integer.parseInt(response);

            if(result>topLimit){
                result=inputInt("Please enter a number less than " + topLimit, topLimit, bottomLimit);
            }
            else if(result<bottomLimit){
                result=inputInt("Please enter a number greater than " + bottomLimit, topLimit, bottomLimit);
            }
        }
        catch(Exception e){
            result=inputInt("Please enter a valid number", topLimit, bottomLimit);
        }

        return result;
    }

    
    //takes in a message and prints it to the screen, then takes in integer from the keyboard
    public static char inputChar(String message){
        String response = inputString(message);
        char result;
        
        if(response.length()>1){
            result=inputChar("Input must be a single character");
        }
        else{
            result=response.charAt(0);
        }
        return result;
    }
    public Horse [] getHorses(){
        return this.participants;
    }
    //takes in a message and prints it to the screen, then takes in integer from the keyboard
    public static double inputDouble(String message, double topLimit, double bottomLimit){
        String response = inputString(message);
        double result=0.0;
        try{
            result = Double.parseDouble(response);

            if(result>topLimit){
                result=inputDouble("Please enter a number less than " + topLimit, topLimit, bottomLimit);
            }
            else if(result<bottomLimit){
                result=inputDouble("Please enter a number greater than " + bottomLimit, topLimit, bottomLimit);
            }
        }
        catch(Exception e){
            result=inputDouble("Please enter a valid decimal", topLimit, bottomLimit);
        }

        return result;
    }
    public static void main(String[] args){
        
        int numHorses = inputInt("Please enter the number of horses in the race", 10, 2);
        int raceDistance = inputInt("Please enter the distance of the race", 100, 0);
        Race race = new Race(raceDistance);
        race.setHorses(numHorses);
        race.startRace();
    }
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        Horse [] horses = this.getHorses();
        for(Horse horse : horses){
            horse.goBackToStart();
        }
                      
        while (!finished)
        {
            //move each horse
            for(Horse horse : horses){
                moveHorse(horse);
            }
                        
            //print the race positions
            printRace();
            
               //move each horse
               for(Horse horse : horses){
                if(raceWonBy(horse)){
                    setWinner(horse);
                    finished = true;
                    System.out.println("The winner is " + getWinner().getName());
                }
            }
            
            boolean allFallen=checkFallen(horses);
            if(allFallen){
                System.out.println("All the horses have fallen, so there is no winner!");
                finished = true;
            }
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
           
        }

       
    }
    

    public Horse getWinner(){
        return this.winner;
    }

    private boolean checkFallen(Horse [] horses){
         //move each horse
         for(Horse horse : horses){
            if(!horse.hasFallen()){
                return false;
            }
        }
            return true;
    }
    public void setWinner(Horse winner){
        this.winner = winner;
    }
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print("\033[2J\033[H\033[3J");
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        Horse [] horses = this.getHorses();
            
            for(Horse horse : horses){
                printLane(horse);
                System.out.println(); 
            }
                  
    
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
            
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('❌');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
        if(theHorse.hasFallen()){
            System.out.print(theHorse.getName() + " has fallen  " + " (New Confidence Rating: " + theHorse.getConfidence() + ")");
        }
        else{
            System.out.print(theHorse.getName() + " (Current Confidence: " + theHorse.getConfidence() + ")");
        }
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
