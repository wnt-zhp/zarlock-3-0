package cx.ath.jbzdak.zarlock.scaler;

import cx.ath.jbzdak.jpaGui.db.Query;
import cx.ath.jbzdak.jpaGui.db.dao.DAO;
import cx.ath.jbzdak.zarlok.ConfigHolder;
import cx.ath.jbzdak.zarlok.ConverterRules;
import cx.ath.jbzdak.zarlok.DBHolder;
import cx.ath.jbzdak.zarlok.entities.UnitConverter;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnitConverterImpl implements ConverterRules{

   private final KnowledgeBase consequenceBase;

   private final KnowledgeBase pruneUnneededBase;

   public UnitConverterImpl() {
      consequenceBase = KnowledgeBaseFactory.newKnowledgeBase();
      pruneUnneededBase = KnowledgeBaseFactory.newKnowledgeBase();
      KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      builder.add(ResourceFactory.newClassPathResource("/scalerDrl/pruneUnneeded.drl"), ResourceType.DRL);
      pruneUnneededBase.addKnowledgePackages(builder.getKnowledgePackages());
      builder.add(ResourceFactory.newClassPathResource("/scalerDrl/scalerDrl.drl"),  ResourceType.DRL);
      consequenceBase.addKnowledgePackages(builder.getKnowledgePackages());
   }

   private List<UnitConverter> find(KnowledgeBase knowledgeBase, UnitConverter... converters){
      StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();
      Query query = DBHolder.getDbManager().createQuery("SELECT uc FROM UnitConverter");
      try{
         for(Object o : query.getResultList()){
            session.insert(o);
         }
      }finally {
         query.close();
      }
      List<UnitConverter> unitConverters = new ArrayList<UnitConverter>();
      for(UnitConverter c : converters){
         session.insert(converters);
      }
      session.setGlobal("maxReflexivityDepth", ConfigHolder.getInt("unitConverter.maxReflexivityDepth"));
      session.setGlobal("changed", unitConverters);
      session.fireAllRules();
		return unitConverters;
   }

   @Override
	public List<UnitConverter> findConsequences(UnitConverter... converters) {
      return find(consequenceBase, converters);
	}

	@Override
	public List<UnitConverter> pruneUnneeded(UnitConverter... converters) {
		return find(pruneUnneededBase, converters);
	}

   @Override
   public void performChanges(Collection<UnitConverter> converters) {
      DAO<UnitConverter> dao = DBHolder.getDbManager().getDao(UnitConverter.class);
      for(UnitConverter c : converters){
         dao.setBean(c);
         dao.persistOrUpdate();
      }
   }
}
