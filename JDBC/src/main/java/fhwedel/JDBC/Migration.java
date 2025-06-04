package fhwedel.JDBC;

import java.sql.*;

public class Migration {

    public static void main(String[] args) {
         try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/firma",
        "root", "password")) {
            createNew(con);
      

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createNew(Connection con) throws SQLException {
        var statement = con.createStatement();
        statement.executeQuery(
            """
            CREATE TABLE krankenversicherung (
                kkid int,
                kuerzel char(45),
                name char(45),
                constraint krankenversicherung_Abteilung primary key (kkid)
            );    
            """); 
        statement.executeQuery(
            """
            INSERT INTO krankenversicherung
            (kkid, kuerzel, name)
            VALUES 
                (1, 'aok', 'Allgemeine Ortskrankenkasse'),
                (2, 'bak', 'Betriebskrankenkasse B. Braun Aesculap'),
                (3, 'bek', 'Barmer Ersatzkasse'),
                (4, 'dak', 'Deutsche Angestelltenkrankenkasse'),
                (5, 'tkk', 'Techniker Krankenkasse'),
                (6, 'kkh', 'Kaufmaennische Krankenkasse')
            """
            );

        // statement.executeQuery(
        //     """
        //     CREATE TABLE personal_new (
        //         pnr int,
        //         name varchar(255),
        //         vorname varchar(255),
        //         geh_stufe varchar(255),
        //         abt_nr varchar(255)
        //         kkid int,
        //         constraint krankenversicherung_Abteilung primary key (kkid)
        //     );    
        //     """);    
    }

}
