
package cx.ath.jbzdak.zarlok.ui.autocolmpete.test;

import cx.ath.jbzdak.jpaGui.autoComplete.MyComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.util.Arrays;

public class ModelTest extends JComboBox{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	ModelTest(){
		setModel(new MyComboBoxModel(Arrays.asList("A", "B", "C"), false));

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add( new ModelTest(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
