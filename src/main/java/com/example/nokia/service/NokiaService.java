package com.example.nokia.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class NokiaService {

	public Connection connect() {

		String jdbcURL = "jdbc:h2:mem:test";
        Connection connection = null;

        try {
        	connection = DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			System.out.println("Failed to connect to H2 in-memory database");
			System.exit(0);
		}

        try {
			createDB(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        return connection;
	}

	public void createDB(Connection connection) throws SQLException {

		String sql1 = "Create table company (id int NOT NULL AUTO_INCREMENT, name varchar(50), money double, PRIMARY KEY (id))";
		String sql2 = "Create table manufacturer (id int NOT NULL AUTO_INCREMENT, name varchar(50) NOT NULL, PRIMARY KEY (id))";
		String sql3 = "Create table part (id int NOT NULL AUTO_INCREMENT, manufacturer_id int, name varchar(50) NOT NULL, quantity int, price float, PRIMARY KEY (id))";

        Statement statement = connection.createStatement();

        statement.execute(sql1);
        statement.execute(sql2);
        statement.execute(sql3);

		String sql = "Insert into company (name) values ('NOKIA')";

        statement.executeUpdate(sql);
	}

	public void collectMainMenu(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.println("1: Data");
		System.out.println("2: Manufacturers");
		System.out.println("3: Company");
		System.out.println("4: Exit");
		while (!sc.hasNextInt()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		int choice = sc.nextInt();

		switch (choice) {
			case 1:
				data(connection);
				break;
			case 2:
				manufacturers(connection);
				break;
			case 3:
				company(connection);
				break;
			case 4:
				System.out.println("Application terminated");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid choice.");
				collectMainMenu(connection);
				break;
		}
		
		sc.close();
	}

	public void collectDataMenu(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.println("1: Add part");
		System.out.println("2: Add manufacturer");
		System.out.println("3: Remove Manufacturer");
		System.out.println("4: Back");
		while (!sc.hasNextInt()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		int choice = sc.nextInt();

		switch (choice) {
			case 1:
				addPart(connection);
				break;
			case 2:
				addManufacturer(connection);
				break;
			case 3:
				removeManufacturer(connection);
				break;
			case 4:
				collectMainMenu(connection);
				break;
			default:
				System.out.println("Invalid choice.");
				collectDataMenu(connection);
				break;
		}
		
		sc.close();
	}

	public void collectManufacturersMenu(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.println("1: Add quantity");
		System.out.println("2: List quantity");
		System.out.println("3: Back");
		while (!sc.hasNextInt()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		int choice = sc.nextInt();

		switch (choice) {
			case 1:
				addQuantity(connection);
				break;
			case 2:
				listQuantity(connection);
				break;
			case 3:
				collectMainMenu(connection);
				break;
			default:
				System.out.println("Invalid choice.");
				collectManufacturersMenu(connection);
				break;
		}
		
		sc.close();
	}

	public void collectCompanyMenu(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.println("1: Add money");
		System.out.println("2: Buy parts");
		System.out.println("3: List parts");
		System.out.println("4: Back");
		while (!sc.hasNextInt()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		int choice = sc.nextInt();

		switch (choice) {
			case 1:
				addMoney(connection);
				break;
			case 2:
				buyParts(connection);
				break;
			case 3:
				listParts(connection);
				break;
			case 4:
				collectMainMenu(connection);
				break;
			default:
				System.out.println("Invalid choice.");
				collectCompanyMenu(connection);
				break;
		}
		
		sc.close();
	}

	public void data(Connection connection) throws SQLException {
		
		collectDataMenu(connection);
	}

	public void manufacturers(Connection connection) throws SQLException {
		
		collectManufacturersMenu(connection);
	}

	public void company(Connection connection) throws SQLException {
		
		collectCompanyMenu(connection);
	}

	public void addPart(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert part name: ");
		String partName = sc.next();

		String sql = "Insert into part (name) values ('" + partName + "')";

        Statement statement = connection.createStatement();

        int rows = statement.executeUpdate(sql);

        if (rows > 0) {
            System.out.println("Inserted a new part.");
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void addManufacturer(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert manufacturer name: ");
		String manufacturerName = sc.next();

		String sql = "Insert into manufacturer (name) values ('" + manufacturerName + "')";

        Statement statement = connection.createStatement();

        int rows = statement.executeUpdate(sql);

        if (rows > 0) {
            System.out.println("Inserted a new manufacturer.");
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void removeManufacturer(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert manufacturer name to be removed: ");
		String manufacturerName = sc.next();

		String sql = "Select manufacturer_id from manufacturer where name = '" + manufacturerName + "'";

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
 
        while (resultSet.next()) {

        	int manufacturerID = resultSet.getInt("manufacturer_id");
            sql = "Delete from part where manufacturer_id = '" + manufacturerID + "'";
            int rows = statement.executeUpdate(sql);
            
            if (rows > 0) {
                System.out.println("Deleted manufacturer's parts.");
            }
            else {
            	System.out.println("Manufacturer's parts not found");
            }
        }

        sql = "Delete from manufacturer where name = '" + manufacturerName + "'";

        int rows = statement.executeUpdate(sql);

        if (rows > 0) {
            System.out.println("Deleted manufacturer.");
        }
        else {
        	System.out.println("Manufacturer not found");
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void addQuantity(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert part name: ");
		String partName = sc.next();

		System.out.print("Insert manufacturer's name: ");
		String manufacturerName = sc.next();

		System.out.print("Insert part's price: ");
		String partPrice = sc.next();

		System.out.print("Insert part's quantity: ");
		String partQuantity = sc.next();

		String sql = "Select id from manufacturer where name = '" + manufacturerName + "'";

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
 
        if (resultSet.next()) {

        	int manufacturerID = resultSet.getInt("id");
    		sql = "Insert into part (manufacturer_id, name, quantity, price) "
    				+ "values ('" + manufacturerID + "', '" + partName + "', '" + partQuantity + "', '" + partPrice + "')";

            statement = connection.createStatement();

            int rows = statement.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Inserted a new part's quantity and price");
            }
        }
        else {
        	System.out.println("Manufacturer not found");
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void listQuantity(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert part name: ");
		String partName = sc.next();

		System.out.print("Insert manufacturer's name: ");
		String manufacturerName = sc.next();

		String sql = "Select id from manufacturer where name = '" + manufacturerName + "'";

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
 
        if (resultSet.next()) {

        	int manufacturerID = resultSet.getInt("id");
            sql = "Select * from part where name = '" + partName + "' and manufacturer_id = '" + manufacturerID + "'";
            ResultSet resultSet2 = statement.executeQuery(sql);

            while (resultSet2.next()) {

            	int price = resultSet2.getInt("price");
            	int quantity = resultSet2.getInt("quantity");
                String name = resultSet2.getString("name");
            	System.out.println("Part name: " + name);
            	System.out.println("Manufacturer's name: " + manufacturerName);
            	System.out.println("Price: " + price + ", Quantity: " + quantity);
            }
        }
        else {
        	
            sql = "Select * from part where name = '" + partName + "'";
            ResultSet resultSet2 = statement.executeQuery(sql);

            while (resultSet2.next()) {

            	int price = resultSet2.getInt("price");
            	int quantity = resultSet2.getInt("quantity");
                String name = resultSet2.getString("name");
            	System.out.println("Part name: " + name);
            	System.out.println("Manufacturer's name: " + manufacturerName);
            	System.out.println("Price: " + price + ", Quantity: " + quantity);
            }
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void addMoney(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert money to add: ");
		while (!sc.hasNextDouble()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		double addmoney = sc.nextDouble();

		String sql = "Select money from company";

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
 
        if (resultSet.next()) {

        	double money = resultSet.getDouble("money");
        	double total = money + addmoney;

    		sql = "Update company set money = '" + total + "' where name = 'NOKIA'";

            statement = connection.createStatement();

            int rows = statement.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Added amount of money.");
                System.out.println("Total: " + total);
            }
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void buyParts(Connection connection) throws SQLException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Insert part name: ");
		String partName = sc.next();

		System.out.print("Insert quantity: ");
		while (!sc.hasNextInt()) {
			System.out.println("Invalid choice.");
			sc.next();
		}
		int quantity = sc.nextInt();

		String sql = "Select * from part where name = '" + partName + "'";

        Statement statement1 = connection.createStatement();

        ResultSet resultSet1 = statement1.executeQuery(sql);

        System.out.println("There is: ");

        if (resultSet1.next()) {
        	int totalQuantity = 0;
	        while (resultSet1.next()) {
	
	        	int manufacturerID = resultSet1.getInt("manufacturer_id");
	        	float price = resultSet1.getFloat("price");
	        	int partQuantity = resultSet1.getInt("quantity");
	            sql = "Select name from manufacturer where id = '" + manufacturerID + "'";

	            Statement statement2 = connection.createStatement();

	            ResultSet resultSet2 = statement2.executeQuery(sql);
	
	            if (resultSet2.next()) {
	
	                String manufacturerName = resultSet2.getString("name");
	            	System.out.println("Part name: " + partName + ", Manufacturer's name: " + manufacturerName + 
	            			", Price: " + price + ", Quantity: " + partQuantity);
	            }
	            totalQuantity += partQuantity;
	        }

	        if (quantity <= totalQuantity) {

	        	System.out.println(quantity + " quantity is available.");
	    		System.out.print("Select manufacturer's name: ");
	    		String manufacturerName = sc.next();

	            sql = "Select id from manufacturer where name = '" + manufacturerName + "'";

	            Statement statement = connection.createStatement();

	            ResultSet resultSet = statement.executeQuery(sql);

	            if (resultSet.next()) {
		            int manufacturerID = resultSet.getInt("id");
		            sql = "Select * from part where name = '" + partName + "' and manufacturer_id = '" + manufacturerID + "'";

		            Statement statement3 = connection.createStatement();

		            ResultSet resultSet3 = statement3.executeQuery(sql);

		            if (resultSet3.next()) {
	
			        	float price = resultSet3.getFloat("price");
			        	float totalPrice = price * quantity;
			        	
			            sql = "Select money from company where name = 'NOKIA'";
	
			            Statement statement4 = connection.createStatement();
	
			            ResultSet resultSet4 = statement4.executeQuery(sql);
			
			            if (resultSet4.next()) {
			            	double budgetPrice = resultSet4.getDouble("money");
			            	if (budgetPrice >= totalPrice) {
	
			            		double updatedMoney = budgetPrice - totalPrice;
			            		sql = "Update company set money = '" + updatedMoney + "' where name = 'NOKIA'";
	
			                    Statement statement5 = connection.createStatement();
	
			                    int rows = statement5.executeUpdate(sql);
	
			                    if (rows > 0) {
			                        System.out.println("Budget money still available: " + updatedMoney);
			                    }
			            	}
			            }
		            }
	            }
	        }
        }
        else {
        	System.out.println("No results");
        }

        System.out.println();
        collectMainMenu(connection);
        sc.close();
	}

	public void listParts(Connection connection) throws SQLException {

        collectMainMenu(connection);
	}
}
