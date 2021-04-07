import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.ArrayList;

public class Database {
    public static Connection connectSQL() {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db6";
        String dbUsername = "Group6";
        String dbPassword = "yunwatjai3170";

        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
            System.exit(0);
        }
        return con;
    }

    public static boolean checkTables(){
        int counter = 0;
        counter += checkBOOK();
        counter += checkCUSTOMER();
        counter += checkORDERS();
        counter += checkORDERING();
        counter += checkAUTHOR();
        if(counter >= 0)
            return true;
        return false;
    }

    public static int checkBOOK(){
        Statement stmt = null;
        int res = -1;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM BOOK;");
            rs.next();
            res=rs.getInt(1);
            stmt.close();
            con.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public static int checkCUSTOMER(){
        Statement stmt = null;
        int res = -1;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM CUSTOMER;");
            rs.next();
            res=rs.getInt(1);
            stmt.close();
            con.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public static int checkORDERS(){
        Statement stmt = null;
        int res = -1;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM ORDERS;");
            rs.next();
            res=rs.getInt(1);
            stmt.close();
            con.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public static int checkORDERING(){
        Statement stmt = null;
        int res = -1;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM ORDERING;");
            rs.next();
            res=rs.getInt(1);
            stmt.close();
            con.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public static int checkAUTHOR(){
        Statement stmt = null;
        int res = -1;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM AUTHOR;");
            rs.next();
            res=rs.getInt(1);
            stmt.close();
            con.close();
            return res;
        } catch (SQLException e) {
            return res;
        }
    }
    
    public static void createTables(){
        if (checkTables()){
           System.out.println("[Error]: You have already created tables!");
           return;
        }
        Statement stmt = null;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            System.out.print("Processing...");
            stmt.execute("create table BOOK( ISBN varchar(13) primary key, TITLE varchar(30) not null, UNIT_PRICE integer not null, COPIES integer not null);");
            stmt.execute("create table CUSTOMER(CID varchar(10) not null primary key, CNAME varchar(50) not null, ADDRESS varchar(200) not null, CARD_NO varchar(19) not null);");
            stmt.execute("create table ORDERS(O_ID varchar(8) not null primary key, O_DATE date not null, STATUS varchar(1) not null, CHARGE integer not null, CID varchar(10) not null, foreign key(CID) references CUSTOMER(CID));");
            stmt.execute("create table ORDERING(O_ID varchar(8) not null, ISBN varchar(13) not null, quantity integer not null, primary key (O_ID, ISBN), foreign key(O_ID) references ORDERS(O_ID), foreign key(ISBN) references BOOK(ISBN));");
            stmt.execute("create table AUTHOR(ISBN varchar(13) not null, ANAME varchar(50) not null, primary key (ISBN, ANAME), foreign key(ISBN) references BOOK(ISBN));");
            System.out.println("Done! Database is initialized!");
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("[Error]: " + e);
        }
    }


    public static void deleteTables(){
        if (!checkTables()){
           System.out.println("[Error]: Tables are already empty!");
           return;
        }
        Statement stmt = null;
        try {
            Connection con= connectSQL();
            stmt = con.createStatement();
            stmt.execute("DROP TABLE IF EXISTS ORDERING;");
            stmt.execute("DROP TABLE IF EXISTS AUTHOR;");
            stmt.execute("DROP TABLE IF EXISTS BOOK;");
            stmt.execute("DROP TABLE IF EXISTS ORDERS;");
            stmt.execute("DROP TABLE IF EXISTS CUSTOMER;");
            stmt.close();
            System.out.println("Done! Database is removed!");
            con.close();
        } catch (SQLException e) {
            System.out.println("[Error]: " + e);
        }
    }

        
    public static void insertBOOK(String isbn, String title, int price, int copies){
        try {
            Connection con= connectSQL();
            PreparedStatement stmnt = con.prepareStatement("INSERT INTO BOOK VALUES (?,?,?,?);");
            stmnt.setString(1,isbn);
            stmnt.setString(2,title);
            stmnt.setInt(3,price);
            stmnt.setInt(4,copies);
            stmnt.executeUpdate();
            stmnt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    } 

    public static void insertCUSTOMER(String cid, String cname, String address, String cardnum){
        try {
            Connection con= connectSQL();
            PreparedStatement stmnt = con.prepareStatement("INSERT INTO CUSTOMER VALUES (?,?,?,?);");
            stmnt.setString(1,cid);
            stmnt.setString(2,cname);
            stmnt.setString(3,address);
            stmnt.setString(4,cardnum);
            stmnt.executeUpdate();
            stmnt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }

    public static void insertORDERS(String oid, String odate, String ostatus, int charge, String cid){
        try {
            Connection con= connectSQL();
            PreparedStatement stmnt = con.prepareStatement("INSERT INTO ORDERS VALUES (?,STR_TO_DATE(?,'%Y-%m-%d'),?,?,?);");
            stmnt.setString(1,oid);
            stmnt.setString(2,odate);
            stmnt.setString(3,ostatus);
            stmnt.setInt(4,charge);
            stmnt.setString(5,cid);
            stmnt.executeUpdate();
            stmnt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    }

    public static void insertORDERING(String oid, String isbn, int quantity){
        try {
            Connection con= connectSQL();
            PreparedStatement stmnt = con.prepareStatement("INSERT INTO ORDERING VALUES (?,?,?);");
            stmnt.setString(1,oid);
            stmnt.setString(2,isbn);
            stmnt.setInt(3,quantity);
            stmnt.executeUpdate();
            stmnt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    } 

    public static void insertAUTHOR(String isbn, String aname){
        try {
            Connection con= connectSQL();
            PreparedStatement stmnt = con.prepareStatement("INSERT INTO AUTHOR VALUES (?,?);");
            stmnt.setString(1,isbn);
            stmnt.setString(2,aname);
            stmnt.executeUpdate();
            stmnt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
    } 

    public static void loadTables(String path){
        if (!checkTables()){
           System.out.println("[Error]: Please create table first!");
           return;
        }

        if (!(new File(path)).isDirectory()){
           System.out.println("[Error]: Incorrect path!");
           return;
        }


        
        try {
            Connection con= connectSQL();
            String line = null;
            //load BOOK
            BufferedReader reader = new BufferedReader(new FileReader(path + "/book.txt"));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                insertBOOK(data[0],data[1],Integer.parseInt(data[2]),Integer.parseInt(data[3]));
            }
            
             //load CUSTOMER
            line = null;
            reader = new BufferedReader(new FileReader(path + "/customer.txt"));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                insertCUSTOMER(data[0],data[1],data[2],data[3]);
            }

            //load ORDERS
            line = null;
            reader = new BufferedReader(new FileReader(path + "/orders.txt"));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                insertORDERS(data[0],data[1],data[2],Integer.parseInt(data[3]),data[4]);
            }

            //load ORDERING
            line = null;
            reader = new BufferedReader(new FileReader(path + "/ordering.txt"));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                insertORDERING(data[0],data[1],Integer.parseInt(data[2]));
            }


            //load AUTHOR

            line = null;
            reader = new BufferedReader(new FileReader(path + "/book_author.txt"));
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                insertAUTHOR(data[0],data[1]);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("[Error]: " + e);
        }
        System.out.println("Data are successfully loaded!");
    }
    
    public static void printAuthor(String ISBN){
        PreparedStatement prestmt = null;
        int i =1;
        try{
            Connection con = connectSQL();
            String sql = "Select ANAME FROM AUTHOR WHERE ISBN = ? ;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,ISBN);
            ResultSet rs = prestmt.executeQuery();
            while(rs.next()){
                System.out.println(i + ": " +rs.getString(1));
                i++;
            }
            System.out.println();
            prestmt.close();
            con.close();
        } catch(SQLException ex){
            System.out.println(ex);
        }  
    }

    public static void searchBookByISBN(String ISBN){
        PreparedStatement prestmt = null;
        int i = 1;
        try{
            Connection con = connectSQL();
            String sql = "Select ISBN,TITLE,UNIT_PRICE,COPIES FROM BOOK WHERE ISBN = ? ;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,ISBN);
            ResultSet rs = prestmt.executeQuery();
            while(rs.next()){
                if(rs.getString(1)==null){
                    return;
                }
                System.out.println("Record " + i );
                System.out.println("ISBN: " + rs.getString(1));
                System.out.println("Book Title: " + rs.getString(2));
                System.out.println("Unit Price: " + rs.getInt(3));
                System.out.println("Copies: " + rs.getInt(4));
                System.out.println("Author");
                printAuthor(rs.getString(1));
                i++;
            }        
            prestmt.close();
            con.close();
            System.out.println("Operation not allowed after ResultsSet closed");
            System.out.println("cannot query the book");
        } catch(SQLException ex){
            System.out.println(ex);
        }  
    }

    public static String exactSearch(String st){
        while(st.startsWith("_")|st.startsWith("%")){
            st = st.substring(1);
        }
        while(st.endsWith("_")|st.endsWith("%")){
            st = st.substring(0,st.length()-1);
        }
        if(st.contains("_")|st.contains("%")){
            return (String)null;
        }
        return st;
    }

    public static void searchBookByTitle(String title){
        String exactTitle = exactSearch(title);
        PreparedStatement prestmt1 = null;
        PreparedStatement prestmt2 = null;
        ArrayList<String> ISBN = new ArrayList<>();
        try{
            Connection con = connectSQL();
            if(exactTitle!=null){
                if(!title.equals(exactTitle)){
                    String sql ="SELECT ISBN,TITLE,UNIT_PRICE,COPIES FROM BOOK WHERE TITLE = ? ORDER BY ISBN,TITLE;";       
                    prestmt1 = con.prepareStatement(sql);
                    prestmt1.setString(1,exactTitle);
                    ResultSet rs = prestmt1.executeQuery();
                    while(rs.next()){
                        if(rs.getString(1)==null){
                            return;
                        }
                    ISBN.add(rs.getString(1)); 
                    System.out.println("Record " + ISBN.size());
                    System.out.println("ISBN: " + rs.getString(1));
                    System.out.println("Book Title: " + rs.getString(2));
                    System.out.println("Unit Price: " + rs.getInt(3));
                    System.out.println("Copies: " + rs.getInt(4));
                    System.out.println("Author");
                    printAuthor(rs.getString(1));                    
                    }
                prestmt1.close();
                con.close();
                }
            }
        }catch(SQLException ex){
            System.out.println(ex);
        } 
        try{
            Connection con = connectSQL();
            String sql2 = "SELECT ISBN,TITLE,UNIT_PRICE,COPIES FROM BOOK WHERE TITLE LIKE ? ORDER BY ISBN,TITLE;";      
            prestmt2 = con.prepareStatement(sql2);
            prestmt2.setString(1,title);
            ResultSet rs2 = prestmt2.executeQuery();
            while(rs2.next()){
                if(rs2.getString(1)==null){
                    return;
                }
                if(!ISBN.contains(rs2.getString(1))){
                ISBN.add(rs2.getString(1));
                System.out.println("Record " + ISBN.size());
                System.out.println("ISBN: " + rs2.getString(1));
                System.out.println("Book Title: " + rs2.getString(2));
                System.out.println("Unit Price: " + rs2.getInt(3));
                System.out.println("Copies: " + rs2.getInt(4));
                System.out.println("Author");
                printAuthor(rs2.getString(1));  
                }
            };           
            prestmt2.close();
            con.close();
            System.out.println("Operation not allowed after ResultsSet closed");
            System.out.println("cannot query the book");
        }catch(SQLException ex){
            System.out.println(ex);
        } 
    }
    public static void searchBookByAuthor(String author){
        String exactAuthor = exactSearch(author);
        PreparedStatement prestmt2 = null;
        PreparedStatement prestmt1 = null;
        ArrayList<String> ISBN = new ArrayList<>();
        int i =1;
        try{
            if(exactAuthor!=null){
                if(!author.equals(exactAuthor)){  
                    Connection con = connectSQL(); 
                    String sql ="SELECT B.ISBN,B.TITLE,B.UNIT_PRICE,B.COPIES FROM BOOK B,AUTHOR A WHERE A.ANAME = ? AND B.ISBN = A.ISBN GROUP BY ISBN ORDER BY B.ISBN,B.TITLE;";       
                    prestmt1 = con.prepareStatement(sql);
                    prestmt1.setString(1,exactAuthor);
                    ResultSet rs = prestmt1.executeQuery();
                    while(rs.next()){
                        if(rs.getString(1)==null){
                            return;
                        }
                        ISBN.add(rs.getString(1)); 
                        System.out.println("Record " + i);
                        System.out.println("ISBN: " + rs.getString(1));
                        System.out.println("Book Title: " + rs.getString(2));
                        System.out.println("Unit Price: " + rs.getInt(3));
                        System.out.println("Copies: " + rs.getInt(4));
                        System.out.println("Author");
                        printAuthor(rs.getString(1));     
                    }     
                    prestmt1.close();
                    con.close();
                }
            }
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        try{
            Connection con = connectSQL();
            String sql2 = "SELECT B.ISBN,B.TITLE,B.UNIT_PRICE,B.COPIES FROM BOOK B,AUTHOR A WHERE A.ANAME LIKE ? AND B.ISBN = A.ISBN GROUP BY ISBN ORDER BY B.ISBN,B.TITLE;"; 
            prestmt2 = con.prepareStatement(sql2);
            prestmt2.setString(1,author);
            ResultSet rs2 = prestmt2.executeQuery();
            while(rs2.next()){
                if(rs2.getString(1)==null){
                    return;
                }
                if(!ISBN.contains(rs2.getString(1))){
                    ISBN.add(rs2.getString(1));
                    System.out.println("Record " + ISBN.size());
                    System.out.println("ISBN: " + rs2.getString(1));
                    System.out.println("Book Title: " + rs2.getString(2));
                    System.out.println("Unit Price: " + rs2.getInt(3));
                    System.out.println("Copies: " + rs2.getInt(4));
                    System.out.println("Author");
                    printAuthor(rs2.getString(1));  
                }
            }             
            prestmt2.close();
            con.close();
            System.out.println("Operation not allowed after ResultsSet closed");
            System.out.println("cannot query the book");
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }

    public static int checkCopies(String ISBN){
        PreparedStatement prestmt = null;
        int copies = 0;
        try{
            Connection con = connectSQL();
            String sql = "SELECT COPIES FROM BOOK WHERE ISBN = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,ISBN);
            ResultSet rs = prestmt.executeQuery();
            rs.next();
            copies =rs.getInt(1);
            prestmt.close();
            con.close();
        }catch(SQLException e){
            return 0;
        }
        return copies ;
    }

    public static Boolean checkCID(String cid){
        PreparedStatement prestmt = null; 
        Boolean exist = false;
        try{
            Connection con = connectSQL();
            String sql ="SELECT CID FROM CUSTOMER WHERE CID = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,cid);
            ResultSet rs = prestmt.executeQuery();
            rs.next();
            if(rs.getString(1)!=null){
                exist = true;
            }
            prestmt.close();
            con.close();
        }catch(SQLException ex){
            System.out.println(ex);
            return false;
        }
        return exist; 
    }

    public static Boolean checkOID(String oid){
        PreparedStatement prestmt = null; 
        Boolean exist = false;
        try{
            Connection con = connectSQL();
            String sql ="SELECT O_ID FROM ORDERS WHERE O_ID = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,oid);
            ResultSet rs = prestmt.executeQuery();
            rs.next();
            if(rs.getString(1)!=null){
                exist = true;
            }
            prestmt.close();
            con.close();
        }catch(SQLException ex){
            System.out.println(ex);
            return false;
        }
        return exist; 
    }

    public static String getCurrentOID(){
        Statement stmt =null;
        int current = 0;
        try{
            Connection con =connectSQL();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(O_ID) FROM ORDERS");
            rs.next();
            current =Integer.parseInt(rs.getString(1))+1;        
            stmt.close();
            con.close();
        }catch(SQLException e){
        }
        String st = String.format("%08d",current);
        return st;
    }

    public static int getCharge(String oid){
        PreparedStatement prestmt = null;       
        int charge =0;
        try{
            Connection con = connectSQL();
            String sql ="SELECT B.UNIT_PRICE,O.quantity FROM BOOK B,ORDERING O WHERE O.O_ID = ? AND O.ISBN = B.ISBN;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,oid);
            ResultSet rs = prestmt.executeQuery();
            while(rs.next()){
                 charge += (rs.getInt(1)+10)*rs.getInt(2) ;
            }
            if(charge!=0)
                charge += 10;
            prestmt.close();
            con.close();
        }catch(SQLException ex){
            System.out.println(ex);
            return 0;
        }
        return charge; 
    }

    public static void printOrder(String oid){
        PreparedStatement prestmt =null;
            try{
                Connection con = connectSQL();
                String sql1 = "SELECT O_ID,STATUS,CHARGE,CID FROM ORDERS WHERE O_ID = ?;";
                prestmt = con.prepareStatement(sql1);
                prestmt.setString(1,oid);
                ResultSet rs = prestmt.executeQuery();
                rs.next();
                System.out.println("order_id: " + rs.getString(1)+" shipping:"+rs.getString(2)+ " charge=" + rs.getInt(3) + " customerId=" + rs.getString(4));
                prestmt.close();
                con.close();
            }catch(SQLException e){
                System.out.print(e);
            } 
    }
    
    public static ArrayList<String> printOrdering(String oid){
        PreparedStatement prestmt1 =null;
        ArrayList<String> list = new ArrayList<>();
        int total =1;
        printOrder(oid);
        try{
            Connection con = connectSQL();
            String sql2 = "SELECT ISBN,quantity FROM ORDERING WHERE O_ID = ?;";
            prestmt1 = con.prepareStatement(sql2);
            prestmt1.setString(1,oid);
            ResultSet rs = prestmt1.executeQuery();
            while(rs.next()){
                System.out.println("book no " + total +" ISBN= " + rs.getString(1)+" quantity= " + rs.getInt(2));
                list.add(String.format("%s,%d",rs.getString(1),rs.getInt(2)));
                total++;              
            }
            prestmt1.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
        return list;
    }

    public static String shipStaus(String oid){
        PreparedStatement prestmt =null; 
        String status =null;
        try{
            Connection con = connectSQL();
            String sql = "SELECT STATUS FROM ORDERS WHERE O_ID = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,oid);
            ResultSet rs = prestmt.executeQuery();
            rs.next();
            status = rs.getString(1);
            prestmt.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
            return null;
        } 
        return status;
    }
    

    
    public static void updateBookCopies(String ISBN,int quantity){
        PreparedStatement prestmt =null;        
        try{
        Connection con = connectSQL();
        String sql = "UPDATE BOOK SET COPIES = (COPIES +?) WHERE ISBN = ?;";
        prestmt = con.prepareStatement(sql);
        prestmt.setInt(1,quantity);
        prestmt.setString(2,ISBN);
        prestmt.executeUpdate();
        prestmt.close();
        con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
    }

    public static void updateOrderingQuan(String oid,String ISBN,int quan){
        PreparedStatement prestmt =null;        
        try{
            Connection con = connectSQL();
            String sql = "UPDATE ORDERING SET quantity = ? WHERE O_ID = ? AND ISBN =?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setInt(1,quan);
            prestmt.setString(2,oid);
            prestmt.setString(3,ISBN);
            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
    }

    public static void updateOrderCharge(String oid,int charge){
        PreparedStatement prestmt =null;        
        try{
            Connection con = connectSQL();
            String sql = "UPDATE ORDERS SET CHARGE = ? WHERE O_ID = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setInt(1,charge);
            prestmt.setString(2,oid);
            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
    }

    public static void updateOrdersDate(String oid,String date){
        PreparedStatement prestmt =null; 
        try{
            Connection con = connectSQL();
            String sql = "UPDATE ORDERS SET O_DATE = STR_TO_DATE(?,'%Y-%m-%d') WHERE O_ID = ?;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,date);
            prestmt.setString(2,oid);
            prestmt.executeUpdate();
            prestmt.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
    }
    
    public static void printYearOrder(String cid,String year){
        System.out.println();
        PreparedStatement prestmt =null;   
        int i =1;
        try{
            Connection con = connectSQL();
            String sql = "SELECT O_ID,O_DATE,CHARGE,STATUS FROM ORDERS WHERE CID = ? AND O_DATE >= ? AND O_DATE <= ? ORDER BY O_ID;";
            prestmt = con.prepareStatement(sql);
            prestmt.setString(1,cid);
            prestmt.setString(2,String.format("%s-01-01",year));
            prestmt.setString(3,String.format("%s-12-31",year));
            ResultSet rs = prestmt.executeQuery();
            while(rs.next()){
                System.out.println("Record: " + i);
                System.out.println("OrderID: " + rs.getString(1));
                System.out.println("OderDate: " + rs.getString(2));
                System.out.println("charge: " + rs.getInt(3));   
                System.out.println("shipping status: " + rs.getString(4));
                System.out.println();
                i++;
            }            
            prestmt.close();
            con.close();
        }catch(SQLException e){
            System.out.println(e);
        } 
    }

    public static void orderUpdate(String orderid){
        try{
            Connection con= connectSQL();
            Scanner input = new Scanner(System.in);
            PreparedStatement stmt1 = con.prepareStatement("SELECT SUM(quantity) FROM ORDERING WHERE O_ID = ? ;");
            stmt1.setString(1, orderid);
            ResultSet rsquan = stmt1.executeQuery();
            rsquan.next();
            if (rsquan.getInt(1) == 0){
                System.out.println("No order with order id " + orderid);
                System.out.println("End of Query");
                return;
            }
            else {
                PreparedStatement stmt2 = con.prepareStatement("SELECT OS.STATUS FROM ORDERS OS WHERE OS.O_ID = ? ;");
                stmt2.setString(1, orderid);
                ResultSet rs1 = stmt2.executeQuery();
                rs1.next();
                if (rs1.getString(1).equals("Y")){
                    System.out.println("The Shipping status of " + orderid + "is Y and " + rsquan.getInt(1) + " books ordered.");
                    System.out.println("End of Query");
                    return;
                }
                if (rs1.getString(1).equals("N")){
                    System.out.println("The Shipping status of " + orderid + " is N and " + rsquan.getInt(1) + " books ordered.");
                    System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
                    String x = input.nextLine();
                    if (x.equals("Y")){
                        PreparedStatement stmt3 = con.prepareStatement("UPDATE ORDERS SET STATUS = 'Y' WHERE O_ID = ? ;");
                        stmt3.setString(1, orderid);
                        stmt3.executeUpdate();
                        System.out.println("Updated shipping status");
                        return;
                    }
                    else{
                        System.out.println("No need update shipping status");
                        System.out.println("End of query");
                        return;
                    }
                }
            }
        } catch (Exception e){
            System.out.println("[Error]: " + e);
        }
    }

    public static void orderQuery(String orderdate){
        try{
            Connection con= connectSQL();
            PreparedStatement stmt = con.prepareStatement("SELECT OS.O_ID, OS.CID, OS.O_DATE, OS.CHARGE FROM ORDERS OS WHERE OS.O_DATE >= ? AND OS.O_DATE <= ?;");
            stmt.setString(1, String.format("%s-01",orderdate));
            stmt.setString(2, String.format("%s-31",orderdate));
            ResultSet rs = stmt.executeQuery();
            int num = 1;
            while (rs.next()){
                if(rs.getString(1) == null){
                    System.out.println("There is no order in this month!");
                    return;
                }
                System.out.println("");
                System.out.println("Record : " + num);
                System.out.println("order_id : " + rs.getString(1));
                System.out.println("customer_id : " + rs.getString(2));
                System.out.println("date : " + rs.getString(3));
                System.out.println("chage : " + rs.getInt(4));
                System.out.println("");
                num++;
            }
            stmt = con.prepareStatement("SELECT SUM(OS.CHARGE) FROM ORDERS OS WHERE OS.O_DATE >= ? AND OS.O_DATE <= ?;");
            stmt.setString(1, String.format("%s-01",orderdate));
            stmt.setString(2, String.format("%s-31",orderdate));
            rs = stmt.executeQuery();
            rs.next();
            System.out.println("Total charge of the month is " + rs.getInt(1));
        } catch (Exception e){
            System.out.println("[Error]: " + e);
        }
    }

    public static void nPopularBook(int num){
        try{
            Connection con= connectSQL();
            PreparedStatement stmt = con.prepareStatement("SELECT B.ISBN, B.TITLE, SUM(OI.quantity) AS sumquan FROM BOOK B, ORDERING OI WHERE B.ISBN = OI.ISBN AND OI.quantity > 0 GROUP BY B.ISBN ORDER BY sumquan DESC LIMIT ?;");
            stmt.setInt(1, num);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1) + "   " + rs.getString(2) + "   " + rs.getInt(3));
            }
            PreparedStatement stmt1 = con.prepareStatement("SELECT COUNT(*) FROM BOOK B");
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            int y = rs1.getInt(1);
            if( y > num ){
                int n = num-1;
                stmt = con.prepareStatement("SELECT B.ISBN, B.TITLE, SUM(OI.quantity) AS sumquan FROM BOOK B, ORDERING OI WHERE B.ISBN = OI.ISBN WHERE OI.quantity > 0 GROUP BY B.ISBN ORDER BY sumquan DESC LIMIT 1 OFFSET ?;");
                stmt.setInt(1, n);
                rs = stmt.executeQuery();
                rs.next();
                int x = rs.getInt(3);
                PreparedStatement stmt2 = con.prepareStatement("SELECT B.ISBN, B.TITLE, SUM(OI.quantity) AS sumquan FROM BOOK B, ORDERING OI WHERE B.ISBN = OI.ISBN WHERE OI.quantity > 0 GROUP BY B.ISBN ORDER BY sumquan DESC LIMIT ? OFFSET ?;");
                stmt2.setInt(1, y);
                stmt2.setInt(2, num);
                ResultSet rs2 = stmt2.executeQuery();
                while(rs2.next()){
                    if(x == rs2.getInt(3)){
                        System.out.println(rs2.getString(1) + "   " + rs2.getString(2) + "   " + rs2.getInt(3));
                    }
                }
            }
        } catch (Exception e){
            System.out.println("[Error]: " + e);
        }
    }


}



