package Service;

import Entities.product;

import java.sql.SQLException;
import java.util.List;

public interface ICRUD<T> {
    public void ajouterproduit(product pr) throws SQLException;



    void updateProduit(int id, product product) throws SQLException;

    public void deleteproduit(int id) throws SQLException;
    List<T> afficherList() throws SQLException;
     List<T> afficherListFront() throws SQLException;
}
