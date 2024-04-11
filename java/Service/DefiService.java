package Service;

import Entities.Defi;
import Entities.Exercice;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefiService implements ICRUD<Defi>{

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;
    public DefiService(){
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Defi defi) throws SQLException {

        String req = "INSERT INTO `defi` (`nom`, `des`, `nd`, `nbj`) VALUES ('"+defi.getNom()+"','"+defi.getDes()+"','"+defi.getNd()+"','"+defi.getNbj()+"')";
        stm = conx.createStatement();
        stm.executeUpdate(req);

        req = "SELECT id FROM `defi` ORDER BY id DESC LIMIT 1 ";
        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);


        if (res.next()) {
            defi.setId(res.getInt("id")); // Récupération de l'ID à partir du résultat de la requête
        }

        for (int i = 0; i < defi.getLex().size(); i++) {
            Exercice element = defi.getLex().get(i);
            req = "INSERT INTO `defi_exercice` VALUES ('"+defi.getId()+"','"+element.getId()+"')";
            stm = conx.createStatement();
            stm.executeUpdate(req);
        }

        System.out.println("Defi ajoutée");
    }

    @Override
    public void addMeth2(Defi defi) throws SQLException {
        String req = "INSERT INTO `defi`(`nom`, `des`, `nd`, `nbj`) VALUES (?,?,?,?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, defi.getNom());
        pstm.setString(2, defi.getDes());
        pstm.setString(3, defi.getNd());
        pstm.setInt(4, defi.getNbj());

        pstm.executeUpdate();
        req = "SELECT id FROM `defi` ORDER BY id DESC LIMIT 1 ";
        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);


        if (res.next()) {
            defi.setId(res.getInt("id")); // Récupération de l'ID à partir du résultat de la requête
            System.out.println("ddddddddddddddddddd  "+defi.getId());
        }

        for (int i = 0; i < defi.getLex().size(); i++) {
            Exercice element = defi.getLex().get(i);
            req = "INSERT INTO `defi_exercice` VALUES ('"+defi.getId()+"','"+element.getId()+"')";
            stm = conx.createStatement();
            stm.executeUpdate(req);
        }
        System.out.println("Defi ajoutée meth 2");


    }

   @Override
    public void modifier(Defi defi) throws SQLException {
        String req = "UPDATE `Defi` SET `nom`=? , `des`=? , `nd`=? , `nbj`=?  WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, defi.getNom());
        pstm.setString(2, defi.getDes());
        pstm.setString(3, defi.getNd());
        pstm.setInt(4, defi.getNbj());
        pstm.setInt(5, defi.getId());
        pstm.executeUpdate();


         req = "DELETE FROM `defi_exercice` WHERE `defi_id`=?";
       pstm = conx.prepareStatement(req);
       pstm.setInt(1, defi.getId());
       pstm.executeUpdate();

       for (int i = 0; i < defi.getLex().size(); i++) {
           Exercice element = defi.getLex().get(i);
           req = "INSERT INTO `defi_exercice` VALUES ('"+defi.getId()+"','"+element.getId()+"')";
           stm = conx.createStatement();
           stm.executeUpdate(req);
       }
        System.out.println("Defi modifier");
    }

    @Override
    public void delete(Defi defi) throws SQLException {
        String req = "DELETE FROM `defi` WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, defi.getId());
        pstm.executeUpdate();


        req = "DELETE FROM `defi_exercice` WHERE `defi_id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, defi.getId());
        pstm.executeUpdate();
        System.out.println("Defi supprimer");
    }

    @Override
    public List<Defi> afficherList() throws SQLException {
        String req = "SELECT * FROM `defi`";

        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Defi> people = new ArrayList<>();

        while (res.next()){


          Defi d = new Defi(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getInt(5),new ArrayList<Exercice>());
          req = "SELECT `exercice_id` FROM `defi_exercice` where `defi_id`='"+d.getId()+"'";
          ArrayList<Exercice> exos = new ArrayList<>();
          stm  = conx.createStatement();
          ResultSet rres = stm.executeQuery(req);

            while (rres.next()) {

                req = "SELECT * FROM `exercice`where `id`='"+rres.getInt(1)+"'";
                stm  = conx.createStatement();
                ResultSet rrres = stm.executeQuery(req);


                while (rrres.next()) {

                    exos.add(new Exercice(rrres.getInt(1),rrres.getString(2),rrres.getString(3),rrres.getString(4),rrres.getString(5),rrres.getString(6),rrres.getString(7)));
                }
                }

            d.setLex(exos);

            people.add(d);
        }


        return people;
    }

    @Override
    public List<Defi> afficherListSearch(String s) throws SQLException {
        String req = "SELECT * FROM `defi` WHERE `nom` LIKE '"+"%"+s+"%"+"' OR `nd` LIKE '"+"%"+s+"%'";

        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Defi> people = new ArrayList<>();

        while (res.next()){


            Defi d = new Defi(res.getInt(1),res.getString(2),res.getString(3),res.getString(4),res.getInt(5),new ArrayList<Exercice>());
            req = "SELECT `exercice_id` FROM `defi_exercice` where `defi_id`='"+d.getId()+"'";
            ArrayList<Exercice> exos = new ArrayList<>();
            stm  = conx.createStatement();
            ResultSet rres = stm.executeQuery(req);

            while (rres.next()) {

                req = "SELECT * FROM `exercice`where `id`='"+rres.getInt(1)+"'";
                stm  = conx.createStatement();
                ResultSet rrres = stm.executeQuery(req);


                while (rrres.next()) {

                    exos.add(new Exercice(rrres.getInt(1),rrres.getString(2),rrres.getString(3),rrres.getString(4),rrres.getString(5),rrres.getString(6),rrres.getString(7)));
                }
            }

            d.setLex(exos);

            people.add(d);
        }


        return people;
    }
}
