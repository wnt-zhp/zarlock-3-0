package test.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

public class FormPanelTest extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;

	/**
	 * This is the default constructor
	 */
	public FormPanelTest() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			//FormFactory fact = new FormFactory();
			//jContentPane.add(fact.decotrateComboBox("Nazwa", "nazwa",  new NoopFormatter()), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}
