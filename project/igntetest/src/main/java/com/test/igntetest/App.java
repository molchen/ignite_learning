package com.test.igntetest;

/**
 * Hello world!
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, SQLException
    {
        System.out.println( "Hello World!" );
        //Register JDBC driver.
        Class.forName("org.apache.ignite.IgniteJdbcThinDriver");

        // Open JDBC connection.
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");
        try {
	      
	
	        // Create database tables.
        
        	Statement stmt = conn.createStatement();
            // Create table based on REPLICATED template.
            stmt.executeUpdate("CREATE TABLE City (" + 
            " id LONG PRIMARY KEY, name VARCHAR) " +
            " WITH \"template=replicated\"");

            // Create table based on PARTITIONED template with one backup.
            stmt.executeUpdate("CREATE TABLE Person (" +
            " id LONG, name VARCHAR, city_id LONG, " +
            " PRIMARY KEY (id, city_id)) " +
            " WITH \"backups=1, affinityKey=city_id\"");
          
            // Create an index on the City table.
            stmt.executeUpdate("CREATE INDEX idx_city_name ON City (name)");

            // Create an index on the Person table.
            stmt.executeUpdate("CREATE INDEX idx_person_name ON Person (name)");
            
        } catch(Exception e) {
        	System.out.print(e);
        }
        
        try {
        	PreparedStatement stmt = conn.prepareStatement("INSERT INTO City (id, name) VALUES (?, ?)");
            stmt.setLong(1, 1L);
            stmt.setString(2, "Forest Hill");
            stmt.executeUpdate();

            stmt.setLong(1, 2L);
            stmt.setString(2, "Denver");
            stmt.executeUpdate();

            stmt.setLong(1, 3L);
            stmt.setString(2, "St. Petersburg");
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement("INSERT INTO Person (id, name, city_id) VALUES (?, ?, ?)");
            stmt.setLong(1, 1L);
            stmt.setString(2, "John Doe");
            stmt.setLong(3, 3L);
            stmt.executeUpdate();

            stmt.setLong(1, 2L);
            stmt.setString(2, "Jane Roe");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();

            stmt.setLong(1, 3L);
            stmt.setString(2, "Mary Major");
            stmt.setLong(3, 1L);
            stmt.executeUpdate();

            stmt.setLong(1, 4L);
            stmt.setString(2, "Richard Miles");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();
        } catch(Exception e) {
        	System.out.print(e);
        }
        
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs =
        		    stmt.executeQuery("SELECT p.name, c.name " +
        		    " FROM Person p, City c " +
        		    " WHERE p.city_id = c.id");
        	while (rs.next())
                System.out.println(rs.getString(1) + ", " + rs.getString(2));
        } catch(Exception e) {
        	System.out.print(e);
        }
    }
}
