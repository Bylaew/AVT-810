package com.company;


import java.sql.*;
import java.util.Vector;

public class AntDB {
    public static String url = "jdbc:postgresql://localhost:5432/SimulationBase";
    public static String login = "postgres";
    public static String password = "root";
    public static String DB_Driver = "org.postgresql.Driver";

    public static int count = 0;


    AntDB() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, login, password);
            System.out.println("Connection to database: Successful");
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertObj(Vector<Ant> obj) {
        delete("Simulation");
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Simulation (id,Type, X, Y,bornTime,lifeTime) VALUES (?, ?, ?,?, ?, ?)");

            for (int i = 0; i < obj.size(); i++) {
                ps.setInt(1, count++);
                if ((obj.get(i) instanceof Warrior)) {
                    ps.setString(2, "Warrior");
                } else {
                    ps.setString(2, "Worker");
                }
                ps.setInt(3, obj.get(i).getX());
                ps.setInt(4, obj.get(i).getY());
                ps.setInt(5, obj.get(i).getBornTime());
                ps.setInt(6, obj.get(i).getLifeTime());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void insertWarr(Vector<Ant> warr) {
        delete("Warriors");
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Warriors (id, X, Y,bornTime,lifeTime) VALUES (?, ?,?, ?, ?)");

            for (int i = 0; i < warr.size(); i++) {
                ps.setInt(1, count++);
                ps.setInt(2, warr.get(i).getX());
                ps.setInt(3, warr.get(i).getY());
                ps.setInt(4, warr.get(i).getBornTime());
                ps.setInt(5, warr.get(i).getLifeTime());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertWork(Vector<Ant> work) {
        delete("Workers");
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Workers (id, X, Y,bornTime,lifeTime) VALUES (?, ?,?, ?, ?)");

            for (int i = 0; i < work.size(); i++) {
                ps.setInt(1, count++);
                ps.setInt(2, work.get(i).getX());
                ps.setInt(3, work.get(i).getY());
                ps.setInt(4, work.get(i).getBornTime());
                ps.setInt(5, work.get(i).getLifeTime());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public Vector<Ant> loadAllObjects() {
        Vector<Ant> vector = new Vector<>();
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Simulation");
            while (rs.next()) {
                if (rs.getString("Type").equals("Warrior")) {
                    vector.add(new Warrior(rs.getInt("X"), rs.getInt("Y"),
                            rs.getInt("lifeTime"), rs.getInt("bornTime"), rs.getInt("id")));
                } else
                    vector.add(new Worker(rs.getInt("X"), rs.getInt("Y"),
                            rs.getInt("lifeTime"), rs.getInt("bornTime"), rs.getInt("id")));

            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return vector;
    }

    public Vector<Ant> loadWarroirs() {
        Vector<Ant> vector = new Vector<>();
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Warriors");
            while (rs.next()) {
                vector.add(new Warrior(rs.getInt("X"), rs.getInt("Y"),
                        rs.getInt("lifeTime"), rs.getInt("bornTime"), rs.getInt("id")));
            }


        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return vector;
    }


    public Vector<Ant> loadWorkers() {
        Vector<Ant> vector = new Vector<>();
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Workers");
            while (rs.next()) {
                vector.add(new Worker(rs.getInt("X"), rs.getInt("Y"),
                        rs.getInt("lifeTime"), rs.getInt("bornTime"), rs.getInt("id")));
            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return vector;
    }

    public void delete(String str) {
        try {
            Connection con = DriverManager.getConnection(url, login, password);
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM " + str);
            stmt.execute();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}


