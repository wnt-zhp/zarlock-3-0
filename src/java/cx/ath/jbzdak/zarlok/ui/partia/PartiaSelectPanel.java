package cx.ath.jbzdak.zarlok.ui.partia;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.zarlok.entities.Partia;
import cx.ath.jbzdak.zarlok.entities.PartieUtils;
import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PartiaSelectPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	@Nonnull
	private JList partieList;

	private List<Partia> modelList = Collections.emptyList();

	private JButton okButton;

	private JButton cancelButton;

	private JPanel buttonPanel = new JPanel();

	private MouseListener listener = new  Listener();

	private JPopupMenu popupMenu;

	@SuppressWarnings("unchecked")
	private JListBinding binding;

	@SuppressWarnings("unchecked")
	public PartiaSelectPanel(){
		super(new BorderLayout());
		partieList = new JList(){

			private static final long serialVersionUID = 1L;

			@Override
			public String getToolTipText(MouseEvent event) {
				int idx = locationToIndex(new Point(event.getX(), event.getY()));
				return PartieUtils.getHtmlFormattedPartiaDesc(getModelList().get(idx));
			}

		};
		partieList.setToolTipText(""); //Żeby getToolTipText(MouseEvent event)  się wołąło!
		partieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		binding = SwingBindings.createJListBinding(UpdateStrategy.READ,
				this, BeanProperty.<PartiaSelectPanel, List<Partia>>create("modelList"), partieList);
		binding.setDetailBinding(BeanProperty.create("basicData"));
		binding.bind();
		add(partieList);
		add(buttonPanel, BorderLayout.SOUTH);
		setOkButton(new JButton("Wybierz", IconManager.getIconSafe("accept")));
		setCancelButton(new JButton("Anuluj", IconManager.getIconSafe("cancel")));
		partieList.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				okButton.setEnabled(partieList.getSelectedIndex()!=-1);
			}
		});
		add(new JScrollPane(partieList), BorderLayout.CENTER);
		setMaximumSize(new Dimension(640, 480));
	}

	public List<Partia> getSelectFrom() {
		return getModelList();
	}

	public List<Partia> getModelList() {
		return modelList;
	}


	public void setModelList(@Nonnull List<Partia> modelList) {
		if(this.modelList == null || !this.modelList.equals(modelList)){
			List<Partia> oldList = this.modelList;
			this.modelList = modelList;
			firePropertyChange("modelList", oldList, this.modelList);
		}

	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(JPopupMenu popupMenu) {
		if(this.popupMenu!=null && popupMenu ==null){
			removeMouseListener(listener);
		}else if (this.popupMenu == null && popupMenu!=null){
			addMouseListener(listener);
		}
		this.popupMenu = popupMenu;
	}

	private void switchButtons(JButton oldButton, JButton newButton, String constraints){
		if(oldButton!=newButton){
			if(oldButton!=null){
				buttonPanel.remove(oldButton);
			}
			if(newButton != null){
				buttonPanel.add(newButton, constraints);
			}
			validate();
		}
	}

	public JButton getOkButton() {
		return okButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public void setOkButton(JButton okButton) {
		switchButtons(this.okButton, okButton, "tag ok");
		this.okButton = okButton;
	}

	public void setCancelButton(JButton cancelButton) {
		switchButtons(this.cancelButton, cancelButton, "tag cancel");
		this.cancelButton = cancelButton;
	}

	private void maybeShowPopup(MouseEvent e){
		if(popupMenu!=null && e.isPopupTrigger()){
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public Partia getSelectedPartia(){
		return modelList.get(partieList.getSelectedIndex());
	}

	private class Listener extends MouseAdapter implements Serializable{

		private static final long serialVersionUID = 1L;

		@Override
		public void mouseClicked(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
	}
}
