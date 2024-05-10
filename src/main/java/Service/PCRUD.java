package Service;
import Entities.panier;
import Entities.product;

import java.sql.SQLException;
import java.util.List;

public interface PCRUD<T> {
    public void ajouterpanier (panier panier) throws SQLException;
    public void updatepanier(int id, panier panier) throws SQLException ;
    public void deletepanier(int id) throws SQLException ;

    public List<panier> afficherp() throws SQLException ;
    public int creerPanier() throws SQLException;
    public void ajouterProduitAuPanier(int panierId, int produitId) throws SQLException;
    void effacerPanier() throws SQLException;

    }
