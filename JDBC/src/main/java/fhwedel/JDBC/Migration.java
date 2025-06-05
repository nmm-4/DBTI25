package fhwedel.JDBC;

import java.sql.*;
import java.util.HashMap;

public class Migration {

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/firma",
                "root", "password")) {
            var statement = con.createStatement();
            createNew(statement);
            createPersonal(statement);
            renewPersonal_new(statement, con);
            renameTables(statement);
            deleteOldTable(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void renameTables(Statement statement) throws SQLException {
        statement.executeQuery(
                                """
                                    ALTER TABLE personal
                                    RENAME TO personal_old
                                """
                                );
        statement.executeQuery(
                                """
                                    ALTER TABLE personal_new
                                    RENAME TO personal
                                """
                                );
        System.out.println("Tablen erfolgreich unbenannt");
    }

    private static void deleteOldTable(Statement statement) throws SQLException {
         statement.executeQuery(
                                """
                                    DROP TABLE personal_old
                                """
                                );
    }

    private static record Personal(
            int pnr,
            String name,
            String vname,
            String geh_stufe,
            String abt_nr,
            int krankenkasse) {
    }

    public static void renewPersonal_new(Statement statement, Connection con) throws SQLException {
        var kk = statement.executeQuery("select * from krankenversicherung");
        HashMap<String, Integer> kkid = new HashMap<>();
        while (kk.next()) {
            kkid.put(kk.getString(2), kk.getInt(1));
        }

        HashMap<Integer, Personal> p = new HashMap<>();

        var res = statement.executeQuery("select * from personal");

        while (res.next()) {
            p.put(res.getInt(1), new Personal(
                    res.getInt(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    kkid.get(res.getString(6))));
        }

        for (Personal person : p.values()) {
            var stmt = con.prepareStatement(
                    "insert into personal_new(pnr,name,vorname,geh_stufe,abt_nr,kkid) VALUES(?,?,?,?,?,?)");
            stmt.setInt(1, person.pnr);
            stmt.setString(2, person.name);
            stmt.setString(3, person.vname);
            stmt.setString(4, person.geh_stufe);
            stmt.setString(5, person.abt_nr);
            stmt.setInt(6, person.krankenkasse);
            stmt.executeQuery();
        }

        System.out.println("Personal erneuert");
    }

    public static void createNew(Statement statement) throws SQLException {
        statement.executeQuery(
                """
                        CREATE TABLE krankenversicherung (
                            kkid INT,
                            kuerzel char(45),
                            name char(45),
                            constraint krankenversicherung_Abteilung primary key (kkid)
                        );
                        """);
        System.out.println("krankenversicherung erstellt");
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
                        """);
        System.out.println("einträge eingefügt");
    }

    public static void createPersonal(Statement statement) throws SQLException {

        statement.executeUpdate(
                """
                        CREATE TABLE personal_new (
                            pnr INT,
                            name VARCHAR(255),
                            vorname VARCHAR(255),
                            geh_stufe VARCHAR(255),
                            abt_nr VARCHAR(255),
                            kkid INT,
                            CONSTRAINT Schluessel_Personal_new PRIMARY KEY (pnr)
                        );
                        """);
        System.out.println("Tabelle 'personal_new' erfolgreich erstellt.");
    }
}
