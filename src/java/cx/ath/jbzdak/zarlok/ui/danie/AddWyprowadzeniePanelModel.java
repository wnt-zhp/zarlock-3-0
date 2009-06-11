package cx.ath.jbzdak.zarlok.ui.danie;

import cx.ath.jbzdak.jpaGui.Transaction;
import static cx.ath.jbzdak.jpaGui.Utils.makeLogger;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.zarlok.entities.*;
import cx.ath.jbzdak.zarlok.ui.autocolmpete.adaptor.PartiaAdaptor;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.slf4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jacek Bzdak jbzdak@gmail.com
 * Date: 2009-04-15
 * Time: 02:36:19
 */

@SuppressWarnings({"WeakerAccess"})
public class AddWyprowadzeniePanelModel {

    private static final Logger LOGGER = makeLogger();

    private final DBManager dbManager;

    private final WyprowadzeniaTable wyprowadzeniaTable;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Dzien dzien;

    private ProductSearchCache insertedProduct;

    private List<Partia> partie;

    private List<Wyprowadzenie> selectedWyprowadzenia;

    private BigDecimal quantity;

    private BigDecimal maxQuantity = BigDecimal.ZERO;

    AddWyprowadzeniePanelModel(DBManager dbManager, WyprowadzeniaTable wyprowadzeniaTable) {
        this.dbManager = dbManager;
        this.wyprowadzeniaTable = wyprowadzeniaTable;
    }

   public void setInsertedProduct(final ProductSearchCache insertedProduct) {
        ProductSearchCache oldInsertedCach = this.insertedProduct;
        this.insertedProduct = insertedProduct;
        support.firePropertyChange("insertedProduct", oldInsertedCach, this.insertedProduct);
        if (this.insertedProduct != null) {
            Transaction.execute(dbManager, new Transaction() {
                @Override
                public void doTransaction(EntityManager entityManager) {
                    Query q = entityManager.createNamedQuery("getPartieDoWydaniaNaPosilek");
                    q.setParameter("nazwa", insertedProduct.getNazwaProduktu());
                    q.setParameter("jednostka", insertedProduct.getJednostka());
                    q.setParameter("specyfikator", insertedProduct.getSpecyfikator());
                    q.setParameter("dzien", dzien.getData());
                    setPartie(q.getResultList());
                }
            });
        }
    }

    public List<Partia> getPartie() {
        return partie;
    }

    public List<Wyprowadzenie> getSelectedWyprowadzenia() {
        return selectedWyprowadzenia;
    }

    public void setSelectedWyprowadzenia(List<Wyprowadzenie> selectedWyprowadzenia) {
        List<Wyprowadzenie> oldSelectedWyprowadzenia = this.selectedWyprowadzenia;
        this.selectedWyprowadzenia = selectedWyprowadzenia;
        support.firePropertyChange("selectedWyprowadzenia", oldSelectedWyprowadzenia, this.selectedWyprowadzenia);
    }

    public void setPartie(List<Partia> partie) {
        List<Partia> oldWyprowadzenia = this.partie;
        this.partie = partie;
        BigDecimal maxQuantity = BigDecimal.ZERO;
        for (Partia partia : partie) {
            maxQuantity = maxQuantity.add(partia.getIloscTeraz());
        }
        setMaxQuantity(maxQuantity);
        support.firePropertyChange("partie", oldWyprowadzenia, this.partie);
    }

    @SuppressWarnings({"WeakerAccess"})
    public void setQuantity(BigDecimal quantity) {
        BigDecimal oldQuantity = this.quantity;
        this.quantity = quantity;
        support.firePropertyChange("quantity", oldQuantity, this.quantity);
    }

    @SuppressWarnings({"WeakerAccess"})
    public void setMaxQuantity(BigDecimal maxQuantity) {
        BigDecimal oldMaxQuantity = this.maxQuantity;
        this.maxQuantity = maxQuantity;
        support.firePropertyChange("maxQuantity", oldMaxQuantity, this.maxQuantity);
        if(quantity!=null && quantity.compareTo(maxQuantity)>0){
            setQuantity(maxQuantity);
        }
    }

    public ProductSearchCache getInsertedProduct() {
        return insertedProduct;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getMaxQuantity() {
        return maxQuantity;
    }

    public Dzien getDzien() {
        return dzien;
    }

    public void setDzien(Dzien dzien) {
        this.dzien = dzien;
        setPartie(Collections.<Partia>emptyList());
    }



    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return support.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return support.getPropertyChangeListeners(propertyName);
    }

    public boolean hasListeners(String propertyName) {
        return support.hasListeners(propertyName);
    }

    public void sendWyprowadzenia(){
        List<Wyprowadzenie> wyprowadzenia = PartieUtils.wydajPoKolei(partie, quantity);
        wyprowadzeniaTable.wyprowadzeniaModel.addAll(wyprowadzenia);
    }


    public class Adapter extends PartiaAdaptor{
        public Adapter(DBManager manager) {
            super(manager);
        }

        @Override
        protected List<ProductSearchCache> getResultsList(EntityManager entityManager, ProductSearchCache filter) {
            Query q = entityManager.createQuery(
                    "SELECT " +
                            "new cx.ath.jbzdak.zarlok.entities.ProductSearchCache(p.produkt.nazwa, p.specyfikator, p.jednostka, p.produkt.id)" +
                            "FROM Partia p WHERE " +
                            "p.dataKsiegowania < :dzien AND " +
                            "(p.dataWaznosci IS NULL OR p.dataWaznosci > :dzien) AND " +
                            "p.iloscTeraz > 0 AND " +
                            "((:nazwa IS NULL) OR  LOWER(p.produkt.nazwa) LIKE LOWER('%' || :nazwa || '%')) AND" +
                            "((:specyfikator IS NULL) OR LOWER(p.specyfikator) LIKE LOWER('%' || :specyfikator || '%')) AND " +
                            "((:jednostka IS NULL) OR LOWER(p.jednostka) LIKE LOWER('%' || :jednostka || '%'))" +
                            "GROUP BY p.produkt.nazwa, p.specyfikator, p.jednostka, p.produkt.id");
            q.setParameter("dzien", dzien.getData());
            q.setParameter("nazwa", filter.getNazwaProduktu());
            q.setParameter("specyfikator", filter.getSpecyfikator());
            q.setParameter("jednostka", filter.getJednostka());
            return (List<ProductSearchCache>) q.getResultList();
        }
    }

    public class IloscJednostekFormatter extends BigDecimalFormatter{
        @Override
        public BigDecimal parseValue(String text) throws Exception {
            BigDecimal value = super.parseValue(text);
            if(value.compareTo(maxQuantity)>0){
                throw new ParsingException("Chcesz wydać więcej elementów niż jest do wydania");
            }
            return value;
        }
    }
}
