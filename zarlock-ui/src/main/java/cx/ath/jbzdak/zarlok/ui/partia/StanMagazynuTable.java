package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.jpaGui.Utils;
import cx.ath.jbzdak.zarlok.entities.TakieSamePartie;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class StanMagazynuTable extends JTable {

	private static final long serialVersionUID = 1L;

	private List<TakieSamePartie> contents;

	@SuppressWarnings("unchecked")
	private JTableBinding binding;

	@SuppressWarnings("unchecked")
	private void initBinding(){
		binding = SwingBindings.createJTableBinding(UpdateStrategy.READ,
				this,
				BeanProperty.<StanMagazynuTable, List<TakieSamePartie>>create("contents"),
				this);
		binding.setEditable(false);
		binding.addColumnBinding(BeanProperty.create("produkt.nazwa")).setColumnName("Nazwa");
		binding.addColumnBinding(BeanProperty.create("specyfikator")).setColumnName("Specyfikator");
		binding.addColumnBinding(BeanProperty.create("jednostka")).setColumnName("Jednostka");
		binding.addColumnBinding(BeanProperty.create("cena")).setColumnName("Cena");
		binding.addColumnBinding(BeanProperty.create("iloscTeraz")).setColumnName("Ilość w magazynie");
		binding.bind();
	}


	public StanMagazynuTable() {
		super();
		initBinding();
	}


	public List<TakieSamePartie> getContents() {
		return contents;
	}

	public void setContents(List<TakieSamePartie> contents) {
		if(!Utils.equals(this.contents, contents)){
			List<TakieSamePartie> oldC = this.contents;
			this.contents = new ArrayList<TakieSamePartie>(contents);
			firePropertyChange("contents", oldC, this.contents);
		}

	}





}
