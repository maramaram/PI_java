package com.example.pijava;
import Service.DefiService;
import Service.ExerciceService;
import Service.PersonService;
import Entities.Defi;
import Entities.Exercice;
import Entities.Person;
import Utils.MyDatabase;
import java.util.ArrayList;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        //MyDatabase db1 = MyDatabase.getInstance();
        //MyDatabase db2 = MyDatabase.getInstance();




       // System.out.println(db1);
       // System.out.println(db2);

        Person p = new Person(3,"TEST22","TEST22");
        Exercice ee= new Exercice(4,"weza","weza","weza","weza","weza","gg");
        Exercice eee= new Exercice(5,"weza","weza","weza","weza","gg","weza");
        Exercice eeee= new Exercice(6,"weza","weza","weza","weza","weza","gg");
        PersonService ps = new PersonService();
        ExerciceService es = new ExerciceService();
        DefiService ds= new DefiService();
        ArrayList<Exercice> l=new ArrayList<>();
        l.add(ee);
        l.add(eee);
        l.add(eeee);
        Defi d= new Defi("dd","eee","eee",5,l);
        try {
            //ps.modifier(p);
            //ps.add(new Person("test","test"));
            //ps.delete(new Person(3,"test","test"));

           // es.modifier(ee);
           // es.add(ee);
            //es.add(eee);
           // es.add(eeee);
           // ds.add(d);
            //es.delete(new Person(3,"test","test"));

            System.out.println(ds.afficherList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}