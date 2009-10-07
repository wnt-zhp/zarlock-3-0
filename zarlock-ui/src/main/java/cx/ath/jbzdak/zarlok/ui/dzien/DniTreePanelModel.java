package cx.ath.jbzdak.zarlok.ui.dzien;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.jpaGui.genericListeners.DoStuffMouseListener;
import cx.ath.jbzdak.jpaGui.task.Task;
import cx.ath.jbzdak.jpaGui.ui.form.DAOForm;
import cx.ath.jbzdak.zarlok.db.dao.DzienDao;
import cx.ath.jbzdak.zarlok.entities.Danie;
import cx.ath.jbzdak.zarlok.entities.Dzien;
import cx.ath.jbzdak.zarlok.entities.DzienUtils;
import cx.ath.jbzdak.zarlok.entities.Posilek;
import cx.ath.jbzdak.zarlok.main.MainWindowModel;
import cx.ath.jbzdak.zarlok.raport.RaportFactory;
import cx.ath.jbzdak.zarlok.ui.iloscOsob.IloscOsobDialog;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekPanel;
import cx.ath.jbzdak.zarlok.ui.posilek.PosilekPanelCache;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Apr 27, 2009
 */
class DniTreePanelModel {

   private final SortedSet<Dzien> dni = new TreeSet<Dzien>(new DzienDateComparator());

   private EntityManager entityManager;

   private final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode());

   private final List<TreePath> selectedPaths = new ArrayList<TreePath>();

   final List<Object> selectedItems = new ArrayList<Object>();

   private final DAOForm<Dzien,?> form;

   private final DzienDao dzienDao;

   private final DBManager manager;

   private final JTree tree;

   final RaportFactory raportFactory;

   public DniTreePanelModel(DAOForm<Dzien, ?> form, DzienDao dzienDao, MainWindowModel mainWindowModel, DBManager manager, JTree tree) {
      this.form = form;
      this.dzienDao = dzienDao;
      form.setDao(dzienDao);
      form.setEntity(new Dzien());
      form.startEditing();
      raportFactory = mainWindowModel.getRaportFactory();
      this.manager = manager;
      this.tree = tree;
   }

   private final Timer selectionTimer;
   {
      selectionTimer = new Timer(100, new TimerAction());
      selectionTimer.setRepeats(false);
   }

   private JPanel detailsPanel = new JPanel(new MigLayout("wrap 1, fillx", "[fill]"));


   public Set<Dzien> getDni() {
		return Collections.unmodifiableSet(dni);
	}

   void setContentsFromDb(){
      setDni(entityManager.createQuery("SELECT d FROM Dzien d").getResultList());
   }

   public void removeDzien(final Dzien d){
      Transaction.execute(entityManager, new Transaction() {
         @Override
         public void doTransaction(EntityManager entityManager) throws Exception {
            entityManager.remove(entityManager.find(Dzien.class, d.getId()));
            setContentsFromDb();
            JOptionPane.showMessageDialog(null, "Udało się usunąć");
            selectedItems.clear();
            updateDetailsPanel();
         }
         });

   }

	public void setDni(Collection<Dzien> dni) {
		this.dni.clear();
		this.dni.addAll(dni);
      detailsPanel.removeAll();
      generateTree();
	}

   void generateTree(){
      DefaultMutableTreeNode root = new DefaultMutableTreeNode();

      List<TreePath> selectedNewNodes = new ArrayList<TreePath>();
      List<Object> selectedItemsAndParents = new ArrayList<Object>();
         for(TreePath path :selectedPaths){
            for (int ii = 0; ii < path.getPath().length; ii++) {
               DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getPath()[ii];
               selectedItemsAndParents.add(node.getUserObject());
            }
         }
      model.setRoot(root);
		for(Dzien d: dni){
         //Dzien d2 = entityManager.find(Dzien.class, d.getId());
			DefaultMutableTreeNode dzienNode = new DefaultMutableTreeNode(d, true);
         if(selectedItemsAndParents.contains(d)){
            TreePath path = new TreePath(model.getPathToRoot(dzienNode));
            selectedNewNodes.add(path);
         }
         for(Posilek p : d.getPosilki()){
            DefaultMutableTreeNode posilekNode = new DefaultMutableTreeNode(p, true);
            dzienNode.add(posilekNode);
            if(selectedItemsAndParents.contains(p)){
               TreePath path = new TreePath(new Object[]{root, dzienNode, posilekNode});
               selectedNewNodes.add(path);
            }
            for(Danie danie : p.getDania()){
               DefaultMutableTreeNode danieNode =     new DefaultMutableTreeNode(danie, false);
               posilekNode.add(danieNode);
               if(selectedItemsAndParents.contains(danie)){
                  TreePath path = new TreePath(new Object[]{root, dzienNode, posilekNode, danieNode});
                  selectedNewNodes.add(path);
               }
            }
         }
         root.add(dzienNode);
		}

      model.reload();
      tree.setSelectionPaths(selectedNewNodes.toArray(new TreePath[]{}));
      for(TreePath treePath : selectedNewNodes){
         tree.makeVisible(treePath);
      }
      updateDetailsPanel();
	}


	void updateDetailsPanel(){
		detailsPanel.removeAll();
		for(Object o : selectedItems){
			if (o instanceof Dzien) {
				Dzien d = (Dzien) o;
				DzienPanel dp = DzienPanelCache.getDzienPanel(d);
				dp.setEntityManager(entityManager);
				dp.setDzien(d);
            
				detailsPanel.add(dp);
				continue;
			}
			if(o instanceof Posilek){
				Posilek p = (Posilek) o;
				PosilekPanel pp = PosilekPanelCache.getPosilekPanel(p);
				pp.setEntityManager(entityManager);
				pp.setPosilek(p);
				detailsPanel.add(pp);
				continue;
			}
		}
		detailsPanel.repaint();
		detailsPanel.validate();
	}
	public JPanel getDetailsPanel() {
		return detailsPanel;
	}

   public DefaultTreeModel getModel() {
      return model;
   }

   public DzienDao getDzienDao() {
      return dzienDao;
   }

   public EntityManager getEntityManager() {
      return entityManager;
   }

   public void setEntityManager(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   public void setDetailsPanel(JPanel detailsPanel) {
      this.detailsPanel = detailsPanel;
   }

   class TreeListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
         TreePath[] paths = e.getPaths();
         for (TreePath treePath : paths) {
            if (e.isAddedPath(treePath)) {
               selectedPaths.add(treePath);
               selectedItems.add(((DefaultMutableTreeNode) treePath.getLastPathComponent()).getUserObject());
            } else {
               selectedPaths.remove(treePath);
               selectedItems.remove(((DefaultMutableTreeNode) treePath.getLastPathComponent()).getUserObject());
            }
         }
			if(selectionTimer.isRunning()){
				selectionTimer.restart();
			}else{
				selectionTimer.start();
			}
		}
	}

   class IloscOsobDialogActionListener extends Task<DAOForm>{

      private final IloscOsobDialog dialog;

      IloscOsobDialogActionListener(IloscOsobDialog dialog) {
         this.dialog = dialog;
      }

      @Override
      public void doTask(@Nullable DAOForm daoForm, @Nullable Object... o) throws Exception {
         dialog.hideDialog();
         dzienDao.getEntity().setPosilki(DzienUtils.getDefaultPosilki(dzienDao.getEntity()));
         form.commit();
         dni.add(dzienDao.getEntity());
         generateTree();
      }
   }

   class AddDzienListener implements ActionListener{

      private final IloscOsobDialog dialog;

      AddDzienListener(IloscOsobDialog dialog) {
         this.dialog = dialog;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         Query q = entityManager.createQuery("SELECT COUNT(d) FROM Dzien d WHERE d.data = :data");
         q.setParameter("data",(form.getForms().get(0).getValue()));
         if(((Number)q.getSingleResult()).intValue()!=0){
            JOptionPane.showMessageDialog(null, "Już dodano ten dzień", "Bład", JOptionPane.ERROR_MESSAGE);
            return;
         }
         dialog.showDialog(form.getEntity().getIloscOsob());
         form.commit();
         form.setEntity(new Dzien());
         form.startEditing();
    }
   }

   class ShowPopupMenuListener extends DoStuffMouseListener{
      private final JTree tree;

      private final TreePopupMenu treePopupMenu = new TreePopupMenu(manager, DniTreePanelModel.this);

      ShowPopupMenuListener(JTree tree) {
         this.tree = tree;
      }

      @Override
      protected void doStuff(MouseEvent e) {
         if(e.isPopupTrigger()){
            treePopupMenu.show(tree, e.getX(), e.getY());
         }
      }
   }

   private class TimerAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
         updateDetailsPanel();
		}
   }

}
