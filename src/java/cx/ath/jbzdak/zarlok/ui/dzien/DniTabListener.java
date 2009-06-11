package cx.ath.jbzdak.zarlok.ui.dzien;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DniTabListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("SELECTED")){
			((DniTab)e.getSource()).tabVisible();
		}else if(e.getActionCommand().equals("HIDDEN")){
			((DniTab)e.getSource()).tabHidden();
		}
		
	}

}
