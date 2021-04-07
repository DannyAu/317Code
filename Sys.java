import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.io.*;



public class Sys {
    static Database db;
    private static void del(){
        try{
            System.out.println("wait");
            db.deleteTables();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }
    private static void create(){
        try{
            System.out.println("wait");
            db.createTables();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }
    
    private static void insert(){
        Scanner input = new Scanner(System.in);
        try{
            System.out.println("Type in the Source Data Folder Path: ");
            db.loadTables(input.nextLine());
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }
    
    private static void settime(){
        Scanner input = new Scanner(System.in);
        try{
            System.out.println("Please input the date (YYYYMMDD)");
            Project pro = new Project();
            int ans = Integer.parseInt(input.nextLine());
            pro.date = LocalDate.of(ans/10000, ans/100%100, ans%100);
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }

	public static void command() {
        Scanner input = new Scanner(System.in);
		while (true) {
            try{
                System.out.println();
                System.out.println("<This is the system interface.>");
                System.out.println("-------------------------------");
                System.out.println("1. Create Tables.");
                System.out.println("2. Delete Tables.");
                System.out.println("3. Insert Data.");
                System.out.println("4. Set System Date.");
                System.out.println("5. Back to the main menu");
                System.out.println("");
                System.out.print("Enter Your Choice: ");
                int ans = Integer.parseInt(input.nextLine());
                switch (ans) {
                    case 1:
                        create();
                        break;
                    case 2:
                        del();
                        break;
                    case 3:
                        insert();
                        break;
                    case 4:
                        settime();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("[Error]: Invalid input.");
                        break;
                }
            } catch (Exception e) {
                    System.out.println("[Error]: " + e);
            }
        }
    }

}
