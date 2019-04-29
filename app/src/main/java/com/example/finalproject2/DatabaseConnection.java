package com.example.finalproject2;


/**
 *
 * @author Nadeem Numbu Issah
 * @author Emmanuel Antwi
 * @author Pamela Quartson
 * @author Daniel Nettey
 */

import android.widget.Toast;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private String userName;
    private String email;
    private String password;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public DatabaseConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/icp_project2","root",",./5mm1nu5l15");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }



    public void credit(String email,String barcode, String binID) throws SQLException{
        try {
            //Add barcode to database
            preparedStatement = connection.prepareStatement("insert into scannedbarcode " +
                    "values ('"+barcode+"');");
            preparedStatement.executeUpdate();
            System.out.println("Inserted into scanned barcode table Successfully");

            //Credit the account
            preparedStatement = connection.prepareStatement("update useraccount set balance = balance + 0.20 where email = '" +email+"';");
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("update useraccount set Scan_count = Scan_count + 1 where email = '" +email+"';");
            preparedStatement.executeUpdate();

            System.out.println("Credited successfully");
            this.checkFullBin(binID);

        } catch (SQLException e) {
            System.out.println("Credit unsuccessful, item has already been used ");
        }
    }


    public void viewCredits(){
        try{
            preparedStatement = connection.prepareStatement("select * from useraccount;");
            ResultSet rs1 = preparedStatement.executeQuery();
            while(rs1.next()){
                String uname = rs1.getString(1);
                String email = rs1.getString(2);
                String balance = rs1.getString(4);
                System.out.println("Username: " + uname +"   Email: " + email + "   Balance: " + balance);
            }
        }
        catch (SQLException e) {
            System.out.println("Credit unsuccessful, item has already been used ");
        }

    }

    public void checkFullBin(String binid){
        //Add to bin
        try{
            preparedStatement = connection.prepareStatement("SELECT capacity FROM Bin WHERE BinId= '" +binid+"';");
            ResultSet rs1 = preparedStatement.executeQuery();
            if (rs1.next()){

                preparedStatement = connection.prepareStatement("update Bin set capacity = capacity - 1 where BinId = '" +binid+"';");
                preparedStatement.executeUpdate();

                if (rs1.getInt(1) <= 0) {
                    System.out.println("Bin is full! Kindly empty the bin");
                    preparedStatement = connection.prepareStatement("update Bin set capacity = 10 where BinId = '" +binid+"';");
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Operation was unsuccessful");
        }

    }

    public void payItem(String user_email, String Item_name, String stakeholder_email){
        try{
            preparedStatement = connection.prepareStatement("SELECT balance FROM useraccount WHERE email= '" +user_email+"';");
            ResultSet bal = preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement("SELECT amount FROM store WHERE name= '" +Item_name+"';");
            ResultSet amnt = preparedStatement.executeQuery();
            if (bal.next() && amnt.next()){
                if (bal.getDouble(1) >= amnt.getDouble(1)) {
                    //Deduct from buyer's account
                    preparedStatement = connection.prepareStatement("update useraccount set Balance = Balance - " + amnt.getDouble(1) + " where Email = '" +user_email+"';");
                    preparedStatement.executeUpdate();

                    //Add to seller's account
                    preparedStatement = connection.prepareStatement("update useraccount set Balance = Balance + " + amnt.getDouble(1) + " where Email = '" +stakeholder_email+"';");
                    preparedStatement.executeUpdate();
                    System.out.println("Transaction Successful");
                }
                else{
                    System.out.println("Your balance is not sufficient for this transaction!");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Please check your inputs");
        }
    }

    public void GuardianFuture() throws SQLException{
        String query = "select * from useraccount where Scan_count = (select max(Scan_count) from useraccount);";
        PreparedStatement stmt1 = connection.prepareStatement(query);
        ResultSet rs1 = stmt1.executeQuery(query);
        String username = "";
        double max = 0;
        while(rs1.next()){
            if (rs1.getDouble(4) > max) {
                max = rs1.getDouble(4);
                username = rs1.getString(1);
            }
        }
        System.out.println("The Guardian of our Future for this week is " + username);
    }







    public String SignUp_Page(String userName, String email, String password) throws SQLException {
        this.userName = userName;
        this.email = email;
        this.password = password;
        preparedStatement =connection.prepareStatement("select * from useraccount;");
        ResultSet rs1 = preparedStatement.executeQuery();
        int i = 0;
        while(rs1.next()){
            String userEmail = rs1.getString(i);
            i++;

            if (userEmail == email){
                return "Email already exist!";
            }

        }
      preparedStatement = connection.prepareStatement("insert into useraccount  values "+"('"+userName
                +"','"+email+"','"+password+"',"+0+","+0+")");
        preparedStatement.executeUpdate();

    return "done!";

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection =new DatabaseConnection();
        databaseConnection.credit("antwi@gmail  ","9876865","LH11");

        //

    }

}


