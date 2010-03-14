package cx.ath.jbzdak.zarlock.ui.product.edit;

import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.jpaGui.utils.DBUtils;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.Product;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static cx.ath.jbzdak.zarlock.ui.ButtonFactory.createButton;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductEditPanel extends JPanel{

   ProductBasicDataEditPanel productBasicDataEditPanel = new ProductBasicDataEditPanel();

   JButton editButton;

   JButton saveButton;

   JButton cancelButton;

   private boolean editing;

   private Product product;

   private DAO<Product> dao;

   public ProductEditPanel() {
      super(new MigLayout("wrap 1, fillx", "[grow, fill]"));
      editButton = createButton("product.edit");
      saveButton = createButton("product.save");
      cancelButton = createButton("product.cancel");
      JPanel buttonPanel = new JPanel(new MigLayout("hidemode 3"));
      buttonPanel.add(editButton, "tag right");
      buttonPanel.add(saveButton, "tag apply");
      buttonPanel.add(cancelButton, "tag cancel");
      add(productBasicDataEditPanel);
      add(buttonPanel);
      setEditing(false);
      initEvents();
      dao = DBHolder.getDbManager().getDao(Product.class);
      productBasicDataEditPanel.setBeanHolder(dao);
   }

   private void initEvents() {
      editButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            productBasicDataEditPanel.getForm().startEditing();
            setEditing(true);
         }
      });
      saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            productBasicDataEditPanel.getForm().commit();
            setEditing(false);
         }
      });
      cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            productBasicDataEditPanel.getForm().rollback();
            setEditing(false);
         }
      });
   }


   public boolean isEditing() {
      return editing;
   }

   public void setEditing(boolean editing) {
      this.editing = editing;
      editButton.setVisible(!editing);
      saveButton.setVisible(editing);
      cancelButton.setVisible(editing);
   }

   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      if(isEditing()){
         productBasicDataEditPanel.getForm().rollback();
      }
      this.product = product;
      dao.setBean(product);
      setEditing(DBUtils.isIdNull(product));
      if(isEditing()){
         productBasicDataEditPanel.getForm().startEditing();
      }else{
         productBasicDataEditPanel.getForm().startViewing();
      }
   }
}
