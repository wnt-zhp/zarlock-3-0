package cx.ath.jbzdak.zarlock.ui.product.edit;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import cx.ath.jbzdak.zarlock.ui.ButtonFactory;
import static cx.ath.jbzdak.zarlock.ui.ButtonFactory.*;
import cx.ath.jbzdak.zarlok.entities.Product;
import cx.ath.jbzdak.zarlok.DBHolder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-11-11
 */
public class ProductEditPanel extends JPanel{

   ProductBasicDataEditPanel productBasicDataEditPanel;

   JButton editButton;

   JButton saveButton;

   JButton cancelButton;

   private boolean editing;

   private Product product;

   public ProductEditPanel() {
      super(new MigLayout("wrap 1, fillx", "[grow]"));
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
      productBasicDataEditPanel.setBeanHolder(DBHolder.getDbManager().getDao(Product.class));
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
          setEditing(false);
      }
      this.product = product;
   }
}
