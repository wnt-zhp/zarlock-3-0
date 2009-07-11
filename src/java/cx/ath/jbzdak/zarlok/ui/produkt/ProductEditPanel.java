package cx.ath.jbzdak.zarlok.ui.produkt;

import cx.ath.jbzdak.jpaGui.Utils;
import static cx.ath.jbzdak.jpaGui.Utils.isIdNull;
import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.zarlok.db.dao.ProduktDAO;
import cx.ath.jbzdak.zarlok.entities.Produkt;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public class ProductEditPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private boolean editable;

	private DBManager manager;

	private ProductAddPanel productAddPanel;

	private KartotekaPanel kartotekaPanel;

	private StanPanel stanPanel;

	private ProduktDAO produktDAO;

	private JPanel filterPanel;

	private ProductSearchBox productSearchBox;

	private JButton setSelectedProductButton;

	private JButton newProductButton;

	private JButton edytujZapiszButton;

   private JButton usunButton;

	public ProductEditPanel(DBManager manager){
		this.manager = manager;
		this.produktDAO = new ProduktDAO(manager);
		setLayout(new MigLayout("wrap 2", "[50%,fill|50%,fill]", "[fill|fill]"));
		initFilterPanel();
		productAddPanel = new ProductAddPanel();
		productAddPanel.setManager(manager);
		productAddPanel.initialize();
		productAddPanel.setBorder(BorderFactory.createTitledBorder("Produkt"));
		edytujZapiszButton = new JButton("Edytuj");
      usunButton = new JButton("Usuń");
		productAddPanel.add(edytujZapiszButton, "w pref!, split 2");
      productAddPanel.add(usunButton, "w pref!");
		kartotekaPanel = new KartotekaPanel(produktDAO, manager);
		kartotekaPanel.setBorder(BorderFactory.createTitledBorder("Kartoteka"));
		stanPanel = new StanPanel(produktDAO);
		stanPanel.setBorder(BorderFactory.createTitledBorder("Stan"));
		add(filterPanel, "span 2 1");
		add(productAddPanel, "");
		add(kartotekaPanel, "span 1 2");
		add(stanPanel);
		initEvents();
      setProdukt(new Produkt());
      setEditable(false);
		start();
	}

	private void initFilterPanel(){
		filterPanel = new JPanel();
		filterPanel.setLayout(new MigLayout("", "[][][]40[]"));
		productSearchBox = new  ProductSearchBox(manager);
		setSelectedProductButton = new JButton("OK");
		newProductButton = new JButton("<html>Dodaj nowy</html>");
		filterPanel.add(new JLabel("Wybierz"));
		filterPanel.add(productSearchBox);
		filterPanel.add(setSelectedProductButton);
		filterPanel.add(newProductButton);

	}


	public void initEvents(){
		setSelectedProductButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setProdukt((Produkt) productSearchBox.getBeanValue());
				setEditable(false);
				start();
			}
		});
		@SuppressWarnings("unchecked")
		Binding b = Bindings.createAutoBinding(UpdateStrategy.READ,
				productSearchBox, ELProperty.create("#{not empty selectedItem}"),
				setSelectedProductButton, BeanProperty.create("enabled"));
		b.bind();
		newProductButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setProdukt(new Produkt());
				setEditable(true);
				start();
			}
		});
		edytujZapiszButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(editable){
					if(productAddPanel.commit()){
                  productAddPanel.stopEditing();
                  setEditable(!editable);
               }
				}else{
					setEditable(!editable);
					start();
				}
			}
		});
      usunButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
           // produktDAO.beginTransaction();
            try{
               if(!produktDAO.getEntity().getPartie().isEmpty()){
                  JOptionPane.showMessageDialog(ProductEditPanel.this, "Nie można usunąć produktu do którego przypisano już partie", "Błąd", JOptionPane.ERROR_MESSAGE);
                  produktDAO.closeTransaction();
                  return;
               }
               produktDAO.remove();
               produktDAO.closeTransaction();
               setProdukt(new Produkt());
               productSearchBox.getEditor().setText("");
               productAddPanel.stopEditing();
               productAddPanel.startViewing();
               setEditable(false);
               //produktDAO.closeTransaction();
            }catch (RuntimeException re){
               //produktDAO.rollbackIfActive();
               throw re;
            }
         }
      });
	}

	@SuppressWarnings("unchecked")
	public void setProdukt(Produkt produkt){
		productAddPanel.setProdukt(produkt);
		if(produkt != null && !isIdNull(produkt)){
			stanPanel.setEntity(produkt);
			kartotekaPanel.setEntity(produkt);
		}else{
			stanPanel.setStanList(Collections.EMPTY_LIST);
			kartotekaPanel.setKartoteka(Collections.EMPTY_LIST);
		}

	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		edytujZapiszButton.setText(editable?"Zapisz":"Edytuj");
      usunButton.setEnabled(editable && produktDAO.getEntity()!=null && !Utils.isIdNull(produktDAO.getEntity()));
	}

	public void start(){
		if(editable){
			productAddPanel.startEditing();
		}else{
			productAddPanel.startViewing();
		}
		productAddPanel.requestFocus();
	}
}
