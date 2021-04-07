import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Customer {
    static Database db;
  private static void searchBook() {
        Scanner input = new Scanner(System.in);
        Scanner inputISBN = new Scanner(System.in);
        Scanner inputTitle = new Scanner(System.in);
        Scanner inputAuthor = new Scanner(System.in);
        System.out.println("What do want to serach??");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice?...");
        try {
            int choice = Integer.parseInt(input.nextLine());
            switch (choice) {
            case 1:
                System.out.print("Input the ISBN: ");
                String ISBN = inputISBN.next();
                db.searchBookByISBN(ISBN);
                break;
            case 2:
                System.out.print("Input the book title: ");
                String title = inputTitle.nextLine();
                db.searchBookByTitle(title);
                break;
            case 3:
                System.out.print("Input the book author: ");
                String author = inputAuthor.next();
                db.searchBookByAuthor(author);
                break;
            case 4:
                break;
            default:
                System.out.println("[Error]: Invalid input.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
  private static void orderCreation(){
      Scanner input = new Scanner(System.in);
      Project pro = new Project();  
      String Odate = pro.date.toString();
      System.out.print("Please enter your customerID??");
      String cid = input.next();
      if(!db.checkCID(cid)){
          System.out.println("This CID does not exist");
          return;
      }
      String oid = null;
      ArrayList<String> ISBN = new ArrayList<>();
      ArrayList<Integer> quantity = new ArrayList<>();
      int listArray = 0;
      System.out.println(">>What books do you want to order??");
      System.out.println(">>Input ISBN and then the quantity.");
      System.out.println(">>You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
      Boolean nextOrder = true;
      while(nextOrder){
          System.out.print("Please enter the book's ISBN:");
          String st = input.next();

          switch (st) {
              case "F":
                  db.printOrder(oid);
                  nextOrder = false;
                  break;
              case "L":
                  System.out.println("ISBN           Number:");
                  if(listArray!=0){ 
                  for(int i=0;i<listArray;i++){
                      System.out.println(ISBN.get(i) + "  " + quantity.get(i));
                  }  
                  }break;
              default:
                  System.out.print("Please enter the quantity of the order: ");
                  int quan = input.nextInt();
                  if(quan<1){
                      System.out.println("Quantity must be larger or equal to 1");
                      break;
                  }
                  if(quan <= db.checkCopies(st)){  
                      if(listArray == 0){          
                      oid = db.getCurrentOID();;
                      db.insertORDERS(oid,Odate,"N",db.getCharge(oid),cid);
                     }
                      db.insertORDERING(oid,st,quan);
                      db.updateBookCopies(st,(-1)*quan);
                      ISBN.add(st);
                      quantity.add(quan);
                      db.updateOrderCharge(oid,db.getCharge(oid));
                      listArray ++;
                  } else{
                      System.out.println("Not enough copy avaliable");
                  }       break;
          }
          
          
          
      }
}

private static void OrderAlter(){
    Scanner input = new Scanner(System.in);
    Project pro = new Project();
     String Odate = pro.date.toString();
    System.out.print("Please enter the OrderID that you want to change: ");
    String oid = input.next();
    if(!db.checkOID(oid)){
        System.out.println("OrderID does not exist");
        return;
    }
    ArrayList<String> list = db.printOrdering(oid);
    System.out.println("Which book you want to alter(input book no.)");
    int bookNo = input.nextInt();
    String ISBN = ((list.get(bookNo-1))).split(",")[0];
    int quan = Integer.parseInt(((list.get(bookNo-1))).split(",")[1]);
    System.out.println("input add or remove");
    String option = input.next();
    if(db.shipStaus(oid).equals("N")){
    switch(option){
        case "add":
            System.out.print("Input the number: ");
            int no1 = input.nextInt();
            if(no1<=db.checkCopies(ISBN)){
                System.out.println("Update is ok");
                db.updateBookCopies(ISBN, (-1)*no1);
                db.updateOrderingQuan(oid, ISBN, quan+no1);
                System.out.println("update done");
                db.updateOrderCharge(oid, db.getCharge(oid));
                System.out.println("updated charge");
                db.updateOrdersDate(oid, Odate);
                db.printOrdering(oid);
            }
            else 
                System.out.println("Not enough copies avaliable");           
            break;
        case "remove":
            System.out.print("Input the number: ");
            int no2 = input.nextInt();
            if(no2<=quan){
                System.out.println("Update is ok");
                db.updateBookCopies(ISBN, no2);
                db.updateOrderingQuan(oid, ISBN, quan-no2);
                System.out.println("update done");
                db.updateOrderCharge(oid, db.getCharge(oid));
                System.out.println("updated charge");
                db.updateOrdersDate(oid, Odate);
                db.printOrdering(oid);
            }
            else 
                System.out.println("You have not order that much copies");
            break;
        default:
            System.out.println("Invalid input");                  
    }
    }
    else 
        System.out.println("The books in the order are shipped");
     
}

private static void orderQuery(){
    Scanner input = new Scanner(System.in);
    System.out.print("Please Input Customer ID:");
    String cid = input.next();
    System.out.print("Please Input the Year:");
    String year = input.next();
    db.printYearOrder(cid, year);
}
 public void command() {
        Scanner input = new Scanner(System.in);
                 while (true) { 
        System.out.println();
        System.out.println("<This is the customer interface.>");
        System.out.println("---------------------------------");
        System.out.println("1. Book Search.");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to the main menu");
        System.out.println();
        System.out.print("What is your choice??.. ");
            try {
                int choice = Integer.parseInt(input.nextLine());
                switch (choice) {
                    case 1:
                        searchBook();
                        break;
                    case 2:
                        orderCreation();
                        break;
                    case 3:
                        OrderAlter();
                        break;
                    case 4:
                        orderQuery();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("[Error]: Invalid input.");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
