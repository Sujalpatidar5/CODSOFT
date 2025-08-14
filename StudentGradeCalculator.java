package CodSoft;
import java.util.Scanner;

public class StudentGradeCalculator {
    public static void main (String args []) {
    System.out.println("Student grade calculator");

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of subjects");
    int n = sc.nextInt();

    while (n <= 0) {            //check validation
        System.out.println("Invalid. Enter valid number of subjects (>=0)");
        n = sc.nextInt();
    }

    double total = 0;          //store total marks
    double [] marks = new double[n];
    for (int i = 0 ; i< n ; i++) {
        System.out.println("Enter marks for subject " + (i+1) + " 70(0-100): ");
        double m = sc.nextDouble();

        while (m < 0 || m > 100) {
            System.out.println("Invalid. Enter valid marks again (0-100) : ");  
            m = sc.nextDouble();     
        }
        marks[i] = m;
        total += m;
    }

    double average = total / n;  //out of 100

    String grade;
    if  (average >= 90)  grade = "A+";
    else if (average >= 80) grade = "A";
    else if (average >= 70) grade = "B";
    else if (average >= 60) grade = "C";
    else if (average >= 50) grade = "D";
    else grade = "F";

    System.out.println("*****RESULT*****");
    System.out.println("Total marks obtained out of " + n*100 + " is : " + total);
    System.out.println("Average marks obtained : " + average);
    System.out.println("Grade : " + grade);

    sc.close();
}
}
//   javac StudentGradeCalculator.java
//   java StudentGradeCalculator.java  