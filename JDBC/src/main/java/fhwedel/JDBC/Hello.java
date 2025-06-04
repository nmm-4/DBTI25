package fhwedel.JDBC;

import java.sql.*;

public class Hello {

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/firma",
        "root", "password")) {
            
           // create(con);

           // read(con);
            update(con);
            


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void update(Connection con) throws SQLException {
       var stmt = con.prepareStatement("select * from gehalt where geh_stufe = ?");
       stmt.setString(1, "it1");
       var query = stmt.executeQuery(); 
        // TODO
        if (query.next()) {
            System.out.println(query.getInt(2));
        }
    }
    static void create(Connection con) throws SQLException {
            var stmt = con.prepareStatement(
                "insert into personal(pnr,name,vorname,geh_stufe,abt_nr,krankenkasse) VALUES(?,?,?,?,?,?)"
            );
            stmt.setInt(1, 417);
            stmt.setString(2, "Krause");      
            stmt.setString(3, "Henrik");
            stmt.setString(4, "it1");
            stmt.setString(5, "d13");
            stmt.setString(6, "tkk");
            stmt.executeQuery();
            System.out.println("Henrik drin");
    }

    static void read(Connection con) throws SQLException {
        var stmt1 = con.createStatement();
        stmt1.executeQuery("select * from personal;");
    }


}
