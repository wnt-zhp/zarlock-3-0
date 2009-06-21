package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekPanel;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekPanelCache;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityManager;
import javax.swing.*;

public class DzienPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	//Niezlaeżne od dnia

	private final JLabel posilkiLabel;

	private final PatternBeanFormatter naTerenieFormatter = new PatternBeanFormatter(
			"<html><strong>Na terenie:</strong><br/>" +
			"Ilość uczestników: {iloscOsob.iloscUczestnikow}<br/>" +
			"Ilość kadry: {iloscOsob.iloscKadry}<br/>" +
			"Ilość pozostałych: {iloscOsob.iloscInnych}<br/>" +
			"<strong>Razem: {iloscOsob.suma}</strong></html>"
	);

	private final PatternBeanFormatter dataFormatter = new PatternBeanFormatter(
			"<html><span style=\"font-size: xx-large;\"><strong>{#weekday,firstUppercase}{data}</strong><br/>" +
			"{data}r.</span></html>"
	);

	//Zależne od dnia

	private final JLabel dateLabel;

   private final JLabel naTerenieLabel;

	private Dzien dzien;

	private EntityManager entityManager;


   public DzienPanel(){
		super(new MigLayout("wrap 3, fillx", "[15px|fill, grow|fill, grow]"));
		posilkiLabel = new JLabel("<html><font size=\"+1\"><strong>Posiłki</strong>:</font></html>");
		dateLabel = new JLabel("");
		naTerenieLabel = new JLabel("");
      
	}

	private void initLabels(){
		add(dateLabel, "span 2");
		add(naTerenieLabel);
		add(posilkiLabel, "span 3");
	}

	private void initGui(){
		removeAll();
		initLabels();
		dateLabel.setText(dataFormatter.format(dzien));
		naTerenieLabel.setText(naTerenieFormatter.format(dzien));
		for(Posilek p : dzien.getPosilki()){
			PosilekPanel panel = PosilekPanelCache.getPosilekPanel(p);
			panel.setEntityManager(entityManager);
			panel.setPosilek(p);
			add(panel, "skip 1, span 2");
		}
	}

   public void setDzien(Dzien dzien) {
		this.dzien = dzien;
		initGui();
	}

   public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

   //LOWPRIO Kiedyś dodać jakiś skprytnu kod który dodaje tylko ten posiłek. Whartever
	@SuppressWarnings({"UnusedParameters"})
   public void posilekAdded(Posilek p){
		initGui();
	}



}
