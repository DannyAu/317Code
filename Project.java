//java -cp .:mysql-connector-java-5.1.47.jar Project
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.io.*;


public class Project {
    static Database db;
    static LocalDate date = java.time.LocalDate.now();
	
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        //LocalDate date = LocalDate.of(2021, 4, 3);
        while (true) {

            try {
                System.out.println();  
                System.out.println("<This is the Book Ordering System.>");
                System.out.println("-------------------------------------");
                System.out.println("1. System interface.");
                System.out.println("2. Customer interface.");
                System.out.println("3. Bookstore interface.");
                System.out.println("4. Show System Date.");
                System.out.println("5. Quit the system......");
                System.out.println("");
                System.out.print("Please enter your choice??..");
                int ans = Integer.parseInt(input.nextLine());
                switch (ans) {
                    case 1:
                        Sys sys = new Sys();
                        sys.command();
                        break;
                    case 2:
                        Customer customer = new Customer();
                        customer.command();
                        break;
                    case 3:
                        Bookstore bs = new Bookstore();
                        bs.request();
                        break;
                    case 4:
                        System.out.print("The System Date is now: ");
                        System.out.println(date);  
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("[Error]: Invalid input.");
                        break;
                }
            }  catch (Exception e) {
                System.out.println("[Error]: " + e);
            }
        }
    }
}



