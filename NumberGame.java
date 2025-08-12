package CodSoft;
import java.util.Scanner;
import java.util.Random;

public class NumberGame {
    public static void main (String args []) {
       // System.out.println("Welcome to Number Game");
       Scanner sc = new Scanner(System.in);
       Random rand = new Random();

       String playAgain = "yes";
       while (playAgain.equalsIgnoreCase("yes")) {

       int numberToGuess = rand.nextInt(100) + 1;   // 1 to 100
       int userGuess = 0;

       int attempts = 0;
       int maxAttempts = 7;

       while (userGuess != numberToGuess && attempts < maxAttempts) {
        System.out.print("Enter your guess: ");
        userGuess = sc.nextInt();
        attempts++;

        // System.out.println("Your guessed: " + userGuess);
        // System.out.println("Actual number: " + numberToGuess);

        if (userGuess < numberToGuess) {
            System.out.println("Low, try again");
        }
        else if (userGuess > numberToGuess) {
            System.out.println("high, try again");
        }
        else {
            System.out.println("Congratulations! You guessed it right");
            System.out.println("Total attempts: " + attempts);
            break;
        }
       }

       if (userGuess != numberToGuess) {
        System.out.println("Sorry! You have used all attempts.");
        System.out.println("The correct number was: " + numberToGuess);
       }
       
       System.out.println("Do you want to play again? (yes/no): ");
       playAgain = sc.next();
    }
       sc.close();
    }
}