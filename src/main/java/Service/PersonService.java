package Service;

import Entities.Person;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonService implements ICRUD<Person>{

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;
    public PersonService(){
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Person person) throws SQLException {

        String req = "INSERT INTO `person`(`name`, `lastname`) VALUES ('"+person.getFirstName()+"','"+person.getLastName()+"')";
        stm = conx.createStatement();
        stm.executeUpdate(req);
        System.out.println("Personne ajoutée");
    }

    @Override
    public void addMeth2(Person person) throws SQLException {
        String req = "INSERT INTO `person`(`name`, `lastname`) VALUES (?,?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, person.getFirstName());
        pstm.setString(2, person.getLastName());

        pstm.executeUpdate();
        System.out.println("Personne ajoutée meth 2");


    }

    @Override
    public void modifier(Person person) throws SQLException {
        String req = "UPDATE `person` SET `name`=? , `lastname`=? WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, person.getFirstName());
        pstm.setString(2, person.getLastName());
        pstm.setInt(3, person.getId());
        pstm.executeUpdate();
        System.out.println("personne modifier");
    }

    @Override
    public void delete(Person person) throws SQLException {
        String req = "DELETE FROM `person` WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, person.getId());
        pstm.executeUpdate();
        System.out.println("personne supprimer");
    }

    @Override
    public List<Person> afficherList() throws SQLException {
        String req = "SELECT * FROM `person`";

        stm  = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Person> people = new ArrayList<>();

        while (res.next()){
            people.add(new Person(res.getInt(1),res.getString(2),res.getString(3)));
        }


        return people;
    }

    @Override
    public List<Person> afficherListSearch(String s) throws SQLException {
        return null;
    }
}
