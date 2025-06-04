package fhwedel.JDBC;

import java.sql.*;

public class Hello {

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/firma",
        "root", "password")) {
            
            var stmt = con.createStatement();
            var next = stmt.executeQuery("select * from abteilung join personal where personal.abt_nr = abteilung.abt_nr and abteilung.name = \"Verkauf\"");
            
            while (next.next()) {
                System.out.println(
                    next.getString("personal.name") + " " + 
                    next.getString("vorname")
                ); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void update(Connection con) throws SQLException {
       var stmt = con.prepareStatement("update gehalt set betrag = betrag * 1.1 where geh_stufe = ?");
       stmt.setString(1, "it1");
       stmt.executeQuery(); 
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

    static void delete(Connection con) throws SQLException {
        var stmt = con.prepareStatement("DELETE FROM personal WHERE vorname = ? and name = ?");
        stmt.setString(1, "Lutz");
        stmt.setString(2, "Tietze");
        stmt.executeQuery();
    }

}
