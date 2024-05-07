package Service;

import Entities.Exercice;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService implements ICRUD<Exercice>{

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;
    public ExerciceService(){
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Exercice exercice) throws SQLException {

        String req = "INSERT INTO `exercice` (`nom`, `des`, `mc`, `nd`, `img`, `gif`) VALUES ('"+exercice.getNom()+"','"+exercice.getDes()+"','"+exercice.getMc()+"','"+exercice.getNd()+"','"+exercice.getImg()+"','"+exercice.getGif()+"')";
        stm = conx.createStatement();
        stm.executeUpdate(req);
        System.out.println("Exercice ajoutée");
    }

    @Override
    public void addMeth2(Exercice exercice) throws SQLException {
        String req = "INSERT INTO `exercice`(`nom`, `des`, `mc`, `nd`, `img`, `gif`) VALUES (?,?,?,?,?,?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, exercice.getNom());
        pstm.setString(2, exercice.getDes());
        pstm.setString(3, exercice.getMc());
        pstm.setString(4, exercice.getNd());
        pstm.setString(5, exercice.getImg());
        pstm.setString(6, exercice.getGif());
        pstm.executeUpdate();
        System.out.println("Exercice ajoutée meth 2");


    }

    @Override
    public void modifier(Exercice exercice) throws SQLException {
        String req = "UPDATE `Exercice` SET `nom`=? , `des`=? , `mc`=? , `nd`=? , `img`=? , `gif`=?  WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, exercice.getNom());
        pstm.setString(2, exercice.getDes());
        pstm.setString(3, exercice.getMc());
        pstm.setString(4, exercice.getNd());
        pstm.setString(5, exercice.getImg());
        pstm.setString(6, exercice.getGif());
        pstm.setInt(7, exercice.getId());
        pstm.executeUpdate();
        System.out.println("Exercice modifier");
    }

    @Override
    public void delete(Exercice exercice) throws SQLException {
        String req = "DELETE FROM `exercice` WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, exercice.getId());
        pstm.executeUpdate();
        System.out.println("Exercice supprimer");
    }

    @Override
    public List<Exercice> afficherList() throws SQLException {
        String req = "SELECT * FROM `exercice`";

        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Exercice> people = new ArrayList<>();

        while (res.next()){
            people.add(new Exercice(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7)));
        }


        return people;
    }


    public List<Exercice> afficherListTri(int a,int b) throws SQLException {
        String req="";
        if (a==0)
        req = "SELECT * FROM `exercice`";
        else if (a==1 && b==1)
            req = "SELECT * FROM `exercice` ORDER BY `nom` ASC";
        else if (a==1 && b==2)
            req = "SELECT * FROM `exercice` ORDER BY `nom` DESC";
            else if (a==2 && b==1)
            req = "SELECT * FROM `exercice` ORDER BY `mc` ASC";
        else if (a==2 && b==2)
            req = "SELECT * FROM `exercice` ORDER BY `mc` DESC";
            else if (a==3 && b==1)
            req = "SELECT * FROM `exercice` ORDER BY `nd` ASC";
        else if (a==3 && b==2)
            req = "SELECT * FROM `exercice` ORDER BY `nd` DESC";
                    stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Exercice> people = new ArrayList<>();

        while (res.next()){
            people.add(new Exercice(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7)));
        }


        return people;
    }




    @Override
    public List<Exercice> afficherListSearch(String s) throws SQLException {
        String req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%'";

        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Exercice> people = new ArrayList<>();

        while (res.next()){
            people.add(new Exercice(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7)));
        }


        return people;
    }


    public List<Exercice> afficherListSearchTri(String s,int a,int b) throws SQLException {


        String req="";
        if (a==0)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%'";
        else if (a==1 && b==1)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `nom` ASC";
        else if (a==1&& b==2)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `nom` DESC";
        else if (a==2&& b==1)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `mc` ASC";
        else if (a==2&& b==2)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `mc` DESC";
        else if (a==3&& b==1)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `nd` ASC";
        else if (a==3&& b==2)
            req = "SELECT * FROM `exercice` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `mc` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%' ORDER BY `nd` DESC";



        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Exercice> people = new ArrayList<>();

        while (res.next()){
            people.add(new Exercice(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7)));
        }


        return people;
    }
}
