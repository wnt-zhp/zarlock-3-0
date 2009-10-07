package cx.ath.jbzdak.zarlok.ui.planowaneWyprowadzenie;

import cx.ath.jbzdak.jpaGui.ReturnableTransaction;
import cx.ath.jbzdak.jpaGui.Transaction;
import cx.ath.jbzdak.jpaGui.autoComplete.AutoCompleteValueHolder;
import cx.ath.jbzdak.jpaGui.autoComplete.FilterAdapter;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.ui.formatted.ParsingException;
import cx.ath.jbzdak.jpaGui.ui.formatted.formatters.BigDecimalFormatter;
import cx.ath.jbzdak.jpaGui.ui.query.AbstractQuery;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.PartieUtils;
import cx.ath.jbzdak.zarlok.entities.PlanowaneWyprowadzenie;
import cx.ath.jbzdak.zarlok.entities.TakieSamePartie;
import cx.ath.jbzdak.zarlok.ui.danie.WyprowadzenieTableModel;
import static org.apache.commons.math.util.MathUtils.round;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-04-20
 */
@SuppressWarnings({"WeakerAccess", "UnusedDeclaration"})
public class PlanowaneTransformModel {

   private final PropertyChangeSupport support = new SwingPropertyChangeSupport(this, true);

   private final DBManager dbManager;

   private final JednoskaAdaptor jednoskaAdaptor = new JednoskaAdaptor();

   private final SpecyfikatorAdaptor specyfikatorAdaptor = new SpecyfikatorAdaptor();

   private final IloscFormatter iloscFormatter = new IloscFormatter();

   private final PatternBeanFormatter maxQFormatter;

   private final WyprowadzenieTableModel model;

   private final PatternBeanFormatter planowanoFormatter = new PatternBeanFormatter(
                   "<html><span style=\"font-size: large;\">Planowano:</span><br/>" +
                           "&nbsp;&nbsp;&nbsp;&nbsp;Wydać <strong> {iloscJednostek} {jednostka} </strong> produktu " +
                           "<strong> {nazwaProduktu} {specyfikator} </strong>" +
                           "</html>");

   private PlanowaneWyprowadzenie wyprowadzenie;

   private List<TakieSamePartie> wszystkiePartie = Collections.emptyList();

   private List<TakieSamePartie> partie = Collections.emptyList();

   private TakieSamePartie wybranePartie;

   private String jednostka, specyfikator;

   private BigDecimal quantity;

   private Number maxQuantity;

   private String planowanoLabelText;

   private String maxLabelText;

   public PlanowaneTransformModel(DBManager dbManager, JTable jTable) {
      this.dbManager = dbManager;
      maxQFormatter = new PatternBeanFormatter("<html><em>(max. {0})</em></html>");
      model = new WyprowadzenieTableModel(dbManager, jTable);
   }

   public PlanowaneWyprowadzenie getWyprowadzenie() {
      return wyprowadzenie;
   }

   public void sendWyprowadzenia(){
      final List<Partia> partie = new ArrayList<Partia>();
      Transaction.execute(dbManager, new Transaction() {
         @Override
         public void doTransaction(EntityManager entityManager) {
            Query q = entityManager.createNamedQuery("getPartieDoWydaniaNaPosilek");
            q.setParameter("nazwa", wybranePartie.getNazwaProduktu());
            q.setParameter("jednostka", wybranePartie.getJednostka());
            q.setParameter("specyfikator", wybranePartie.getSpecyfikator());
            q.setParameter("dzien", wyprowadzenie.getDanie().getPosilek().getDzien().getData());
            partie.addAll(q.getResultList());
         }
      });
      model.getEntities().addAll(PartieUtils.wydajPoKolei(partie, quantity));
   }

   public void setWyprowadzenie(PlanowaneWyprowadzenie wyprowadzenie) {
      this.wyprowadzenie = wyprowadzenie;
      setJednostka(wyprowadzenie.getJednostka());
      setSpecyfikator(wyprowadzenie.getSpecyfikator());
      setMaxLabelText(maxQFormatter.format(maxQuantity));
      wszystkiePartie = fetchWszystkiePartie();
      specyfikatorAdaptor.setPartie(wszystkiePartie);
      jednoskaAdaptor.setPartie(wszystkiePartie);
      setPlanowanoLabelText(planowanoFormatter.format(wyprowadzenie));
      setQuantity(wyprowadzenie.getIloscJednostek());
      updateWybranePartie();
   }

   public void setJednostka(String jednostka) {
      String oldJednostka = this.jednostka;
      this.jednostka = jednostka;
      updateWybranePartie();
      support.firePropertyChange("jednostka", oldJednostka, this.jednostka);
   }

   public void setSpecyfikator(String specyfikator) {
      String oldSpecyfikator = this.specyfikator;
      this.specyfikator = specyfikator;
      updateWybranePartie();
      support.firePropertyChange("specyfikator", oldSpecyfikator, this.specyfikator);
   }

   public void setPlanowanoLabelText(String planowanoLabelText) {
      String oldPlanowanoLabelText = this.planowanoLabelText;
      this.planowanoLabelText = planowanoLabelText;
      support.firePropertyChange("planowanoLabelText", oldPlanowanoLabelText, this.planowanoLabelText);
   }

   public void setMaxLabelText(String maxLabelText) {
      String oldMaxLabelText = this.maxLabelText;
      this.maxLabelText = maxLabelText;
      support.firePropertyChange("maxLabelText", oldMaxLabelText, this.maxLabelText);
   }

   public void setSelectedPartia(TakieSamePartie partie){
      this.wybranePartie = partie;
      maxQuantity = partie.getIloscTeraz();
   }

   public void setPartie(List<TakieSamePartie> partie) {
      List<TakieSamePartie> oldPartie = this.partie;
      this.partie = partie;
      support.firePropertyChange("partie", oldPartie, this.partie);
   }

   protected void updateWybranePartie() {
      TakieSamePartie takieSamePartie = null;
      for (TakieSamePartie partie : wszystkiePartie) {
         if (partie.getNazwaProduktu().equals(wyprowadzenie.getNazwaProduktu()) &&
                 partie.getJednostka().equals(jednostka) &&
                 partie.getSpecyfikator().equals(specyfikator)
                 ) {
            takieSamePartie = partie;
            continue;
         }
      }
      setWybranePartie(takieSamePartie);
      assert takieSamePartie != null;
      setMaxQuantity(takieSamePartie.getIloscTeraz());
      setPlanowanoLabelText(maxQFormatter.format(takieSamePartie));
   }

   public JednoskaAdaptor getJednoskaAdaptor() {
      return jednoskaAdaptor;
   }

   public SpecyfikatorAdaptor getSpecyfikatorAdaptor() {
      return specyfikatorAdaptor;
   }

   public String getJednostka() {
      return jednostka;
   }

   public String getSpecyfikator() {
      return specyfikator;
   }

   public Number getMaxQuantity() {
      return maxQuantity;
   }

   public String getPlanowanoLabelText() {
      return planowanoLabelText;
   }

   public String getMaxLabelText() {
      return maxLabelText;
   }

   public List<TakieSamePartie> getPartie() {
      return partie;
   }

   public TakieSamePartie getWybranePartie() {
      return wybranePartie;
   }

   public void setMaxQuantity(Number maxQuantity) {
      Number oldQuantity = this.maxQuantity;
      this.maxQuantity = maxQuantity;
      if(maxQuantity !=null){
         setMaxLabelText(maxQFormatter.format(maxQuantity));
      }else{
         setMaxLabelText("");
      }
      if (maxQuantity != null && quantity!=null) {
         if(round(quantity.doubleValue(),2) > round(maxQuantity.doubleValue(), 2)){
            setQuantity(BigDecimal.valueOf(maxQuantity.doubleValue()));
         }
      }else{
         setQuantity(BigDecimal.ZERO);
      }
      support.firePropertyChange("maxQuantity", oldQuantity, this.maxQuantity);
   }

   public void setWybranePartie(TakieSamePartie wybranePartie) {
      TakieSamePartie oldWybranePartie = this.wybranePartie;
      this.wybranePartie = wybranePartie;
      support.firePropertyChange("wybranePartie", oldWybranePartie, this.wybranePartie);
   }

   protected List<TakieSamePartie> fetchWszystkiePartie(){
      return Transaction.execute(dbManager, new ReturnableTransaction<List<TakieSamePartie>>() {
         @Override
         @SuppressWarnings({"JpaQueryApiInspection"})
         public List<TakieSamePartie> doTransaction(EntityManager entityManager) {
            Query q = entityManager.createQuery(
                    "SELECT new cx.ath.jbzdak.zarlok.entities.TakieSamePartie(" +
                            "   p.produkt, p.specyfikator, p.jednostka, AVG(p.cena*p.iloscTeraz)/SUM(p.iloscTeraz), " +
                            "   SUM(p.iloscTeraz)" +
                            ")  " +
                            "FROM Partia p " +
                            "WHERE " +
                            "   (:nazwa IS NULL OR p.produkt.nazwa = :nazwa) AND " +
                            "   (:specyfikator IS NULL OR p.specyfikator = :specyfikator ) AND " +
                            "   (:jednostka IS NULL OR p.jednostka = :jednostka) AND " +
                            "   p.dataWaznosci > :dzien AND " +
                            "   p.dataKsiegowania < :dzien AND " +
                            "   p.iloscTeraz > 0 " +
                            " GROUP BY p.produkt, p.specyfikator, p.jednostka");
            q.setParameter("nazwa", wyprowadzenie.getNazwaProduktu());
//            q.setParameter("jednostka", jednostka);
//            q.setParameter("specyfikator", specyfikator);
            q.setParameter("dzien", wyprowadzenie.getDanie().getPosilek().getDzien().getData());
            return q.getResultList();
         }
      });
   }

   public BigDecimal getQuantity() {
      return quantity;
   }

   public void setQuantity(BigDecimal quantity) {
      BigDecimal oldQuantity = this.quantity;
      this.quantity = quantity;
      support.firePropertyChange("quantity", oldQuantity, this.quantity);
   }

   public IloscFormatter getIloscFormatter() {
      return iloscFormatter;
   }

   class SpecyfikatorAdaptor extends FilterAdapter<String>{
      SpecyfikatorAdaptor() {
         super(new AbstractQuery<String, String>(){
            @Override
            public boolean matches(String str) {
               return str.contains(query);
            }
         });
      }

      public void setPartie(List<TakieSamePartie> partie){
         Set<String> specyfikatory = new HashSet<String>();
         for (TakieSamePartie takieSamePartie : partie) {
            specyfikatory.add(takieSamePartie.getSpecyfikator());
         }
         setContents(specyfikatory);
      }

      @Override
      public AutoCompleteValueHolder getValueHolderFromFilter() {
         return null;
      }
   }

   class JednoskaAdaptor extends FilterAdapter<String>{

      JednoskaAdaptor() {
         super(new AbstractQuery<String, String>() {
            public boolean matches(String str) {
               return str.contains(query);
            }
         });
      }

      public void setPartie(List<TakieSamePartie> partie){
         Set<String> jednostki = new HashSet<String>();
         for (TakieSamePartie takieSamePartie : partie) {
            jednostki.add(takieSamePartie.getJednostka());
         }
         setContents(jednostki);
      }

      @Override
      public AutoCompleteValueHolder getValueHolderFromFilter() {
         return null;
      }
   }

   class IloscFormatter extends BigDecimalFormatter{
      @Override
      public BigDecimal parseValue(String text) throws Exception {
         BigDecimal result = super.parseValue(text);
         if(round(result.doubleValue(),2) > round(maxQuantity.doubleValue(), 2)){
            throw new ParsingException("Chcesz wydać więcej niż masz. . . ");
         }
         return result;
      }
   }

}
