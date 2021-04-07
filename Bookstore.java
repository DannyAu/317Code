import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;

public class Bookstore{
	static Database db;
	private static void update(){
		try{
			Scanner input = new Scanner(System.in);
			System.out.print("Please input the order ID: ");
			String orderid = input.nextLine();
			if (!db.checkTables()){
                System.out.println("[Error]: There are no record!");
                return;
            }
			db.orderUpdate(orderid);
		} catch (Exception e){
			System.out.println("[Error]: " + e);
		}
	}

	private static void query(){
		try{
			Scanner input = new Scanner(System.in);
			System.out.print("Please input the Month for Order Query (YYYY-MM): ");
			String orderdate = input.nextLine();
			if (!db.checkTables()){
                System.out.println("Total charge of the month is 0");
                return;
            }
			db.orderQuery(orderdate);
		} catch (Exception e){
			System.out.println("[Error]: " + e);
		}
	}

	private static void popular(){
		try{
			Scanner input = new Scanner(System.in);
			System.out.print("Please input the N popular books number: ");
			int num = input.nextInt();
			if (!db.checkTables()){
                System.out.println("[Error]: There are no record!");
                return;
            }
			System.out.println("ISBN:           Title             copies");
			db.nPopularBook(num);
		} catch (Exception e){
			System.out.println("[Error]: " + e);
		}
	}

	public static void request(){
		Scanner input = new Scanner(System.in);
		while(true){
			try{
				System.out.println();
				System.out.println("<This is the bookstore interface.>");
				System.out.println("----------------------------------");
				System.out.println("1. Order Update.");
				System.out.println("2. Order Query.");
				System.out.println("3. N most Popular Book Query.");
				System.out.println("4. Back to main menu.");
				System.out.println("");
				System.out.print("What is your choice??..");
				int ans = Integer.parseInt(input.nextLine());
				switch(ans){
					case 1:
						update();
						break;
					case 2:
						query();
						break;
					case 3:
						popular();
						break;
					case 4:
						return;
					default:
                        System.out.println("error");
                        break;
				}
			} catch (Exception e){
				System.out.println("[Error]: " + e);
			}
		}
	}
}