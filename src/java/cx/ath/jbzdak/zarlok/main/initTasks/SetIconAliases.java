package cx.ath.jbzdak.zarlok.main.initTasks;

import cx.ath.jbzdak.common.famfamicons.IconManager;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;

public class SetIconAliases extends Task<MainWindowModel> {

	public SetIconAliases() {
		super(0, "SET ICON ALIASES");
	}

	@Override
	public void doTask(MainWindowModel t, Object... o) throws Exception {
      //pierwsze to alias drugie nazwa pliku bez rozszerzenia
		IconManager.setAlias("wyprowadzenie", "cart_add");
      IconManager.setAlias("wydaj", "cart_add");
		IconManager.setAlias("dzien_add", "book_add");
		IconManager.setAlias("dzien_tree_open", "book_open");
		IconManager.setAlias("dzien_tree_closed", "book");
		IconManager.setAlias("dzien_tree_posilek", "basket");
      IconManager.setAlias("no_error_icon", "weather_sun");
      IconManager.setAlias("error_icon", "weather_rain");
      IconManager.setAlias("delete", "cancel");
      IconManager.setAlias("posilek_closed", "basket_go");
      IconManager.setAlias("posilek_leaf", "basket");
      IconManager.setAlias("posilek_open", "basket");
      IconManager.setAlias("dzien_closed", "book");
      IconManager.setAlias("dzien_open", "book_open");
      IconManager.setAlias("dzien_leaf", "book");
      IconManager.setAlias("danie_leaf", "page");
      
	}

}
