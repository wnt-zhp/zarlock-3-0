package cx.ath.jbzdak.zarlok.ui.posilek;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.ui.danie.DaniaPanelCache;
import cx.ath.jbzdak.zarlok.ui.danie.DaniePanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PosilekPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	//Niezależne od posiłku:

	JLabel daniaLabel;

	{
		daniaLabel = new JLabel("<html><font size=\"+1\"><strong>Dania</strong>:</font></html>");
	}

	PatternBeanFormatter nazwaFormatter
		= new PatternBeanFormatter("<html><font size=\"+1\"><strong>{nazwa}</strong> " +
				"(koszt: {#0}{(costStrict)?\"\":\"<i>\"}{koszt}{#0}{(costStrict)?\"\":\"</i>\"}zł</font>) -- id:{id}</html>)");

	JPanel addDaniePanel;

	JButton addDanieButton;

	JTextField danieNazwaTextField;


	//Zależne od posiłku

	JLabel nazwaPosilku;

	private EntityManager entityManager;

	private Posilek posilek;




	PosilekPanel() {
		super(new MigLayout("wrap 2, fillx", "[15px!|fill]"));
		nazwaPosilku = new JLabel();
		addDaniePanel = new JPanel(new FlowLayout());
		danieNazwaTextField = new JTextField();
		danieNazwaTextField.setColumns(10);
		addDaniePanel.add(danieNazwaTextField);
		addDanieButton = new JButton("Dodaj danie", IconManager.getIconSafe("add"));
		addDaniePanel.add(addDanieButton);
		addDanieButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				entityManager.getTransaction().begin();
				try{
					Danie d = new Danie();
					d.setNazwa(danieNazwaTextField.getText());
					d.setPosilek(posilek);
					posilek.getDania().add(d);
					entityManager.getTransaction().commit();
				}catch(RuntimeException ex){
					entityManager.getTransaction().rollback();
					throw ex;
				}
				initGui();
			}
		});
		@SuppressWarnings("unchecked")
		Binding b = Bindings.createAutoBinding(UpdateStrategy.READ,
				danieNazwaTextField, ELProperty.create("#{not empty text}"),
				addDanieButton, BeanProperty.create("enabled"));
		b.bind();
	}

	private void initLabels(){
		nazwaPosilku.setText(nazwaFormatter.format(posilek));
		add(nazwaPosilku, "span 2");
		add(addDaniePanel, "span 2");
		add(daniaLabel, "span 2");
	}

	private void initGui() {
		initLabels();
		for(Danie d: posilek.getDania()){
			DaniePanel dp = DaniaPanelCache.getDanuePanel(d);
			dp.setEntityManager(entityManager);
			dp.setDanie(d);
			add(dp, "skip 1");
		}
		validate();
	}

	public Posilek getPosilek() {
		return posilek;
	}

	public void setPosilek(Posilek posilek) {
		this.posilek = posilek;
		initGui();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


}
