package JDBC;

import Environment.Collect;
import Environment.Habitat;
import LabObjects.DownloadImage;
import LabObjects.House;
import LabObjects.Kap_House;
import LabObjects.Wooden_house;


import java.sql.*;
import java.util.Vector;

public class HouseBD {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/lab7";
    public static final String DB_Driver = "org.postgresql.Driver";
    private static String login = "postgres";
    private static String password = "postgres";
    private String createtab;
    private Statement st;

    private HouseBD(){
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL, login, password);//соединениесБД
            connection.close();
        }catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException er){er.printStackTrace();}
    }



    public void insert(Vector vec){
        try {
            Connection con = DriverManager.getConnection(DB_URL, login, password);
            PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO Houses (TYPE, X, Y) VALUES (?, ?, ?)");

            for (int i = 0; i <vec.size(); i++){
                House temp = (House)vec.get(i);
                stmt.setString(1, temp.getType());
                stmt.setInt(2, temp.getX());
                stmt.setInt(3, temp.getY());
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
            con.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    public void delete_all(){
        try{
            Connection con = DriverManager.getConnection(DB_URL, login, password);
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM HOUSES");
            stmt.execute();
            stmt.close();
            con.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    public void load_all(Habitat frame){
        try{
        Connection con = DriverManager.getConnection(DB_URL, login, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM HOUSES");
        String str = new String();
        while(rs.next()){
            House temp;
            str = rs.getString("type");
            if (str.equals("Wood")){
            temp = new Wooden_house();
            temp.setX(rs.getInt("X"));
            temp.setY(rs.getInt("Y"));
            Collect.getInstance().add_obj(temp, -1,frame);
            }
            if (str.equals("Kap")){
                temp = new Kap_House();
                temp.setX(rs.getInt("X"));
                temp.setY(rs.getInt("Y"));
                Collect.getInstance().add_obj(temp, -1,frame);
            }
        }
        rs.close();
        stmt.close();
        con.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    public void load_woodobj(Habitat frame){
try{
    Connection con = DriverManager.getConnection(DB_URL, login, password);
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM HOUSES WHERE TYPE='Wood'");
    while(rs.next()){
        House temp;
        temp = new Wooden_house();
        temp.setX(rs.getInt("X"));
        temp.setY(rs.getInt("Y"));
        Collect.getInstance().add_obj(temp, -1,frame);
    }
    rs.close();
    stmt.close();
    con.close();

}catch (SQLException e){e.printStackTrace();}
    }

    public void load_kapobj(Habitat frame){
        try{
            Connection con = DriverManager.getConnection(DB_URL, login, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM HOUSES WHERE TYPE='Kap'");
            while(rs.next()){
                House temp;
                temp = new Kap_House();
                temp.setX(rs.getInt("X"));
                temp.setY(rs.getInt("Y"));
                Collect.getInstance().add_obj(temp, -1,frame);
            }
            rs.close();
            stmt.close();
            con.close();

        }catch (SQLException e){e.printStackTrace();}
    }


    public static class BDHolder{
        public static final HouseBD HOLDER_INSTANCE = new HouseBD();
    }

    public static HouseBD getInstance(){
        return HouseBD.BDHolder.HOLDER_INSTANCE;
    }
}
