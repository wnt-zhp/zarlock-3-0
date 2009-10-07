package cx.ath.jbzdak.zarlok.importer;

import cx.ath.jbzdak.jpaGui.db.DBManager;
import cx.ath.jbzdak.jpaGui.db.Transaction;
import cx.ath.jbzdak.zarlok.db.tasks.CleanImportTables;
import cx.ath.jbzdak.zarlok.entities.*;
import zzDataBase.Day;
import zzDataBase.MealExpenditure;
import zzDataBase.Mealv3;
import zzDataBase.StaticContent;
import zzEx.ZZIllegalEx;
import zzMyDate.MyDate;
import zzProduct.Product;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class Importer {


	public static final void doImport(DBManager manager){
		try {
			feedProductsIntoDb(manager);
			feedMealsIntoDb(manager);
			generateProducts(manager);
			geenrateMeals(manager);
			generateExpenditures(manager);
			try {
				new CleanImportTables().doTask(manager);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}


	private static void generateExpenditures(DBManager manager) {
		final List<ZZMeal> meals = new ArrayList<ZZMeal>();
		Transaction.execute(manager.createEntityManager(), new Transaction(){
			{closeEntityManager=true;}
			@SuppressWarnings("unchecked")
			@Override
			public void doTransaction(EntityManager entityManager) {
				meals.addAll(entityManager.createQuery("SELECT p from ZZMeal p").getResultList());

			}
		});
		for(final ZZMeal m : meals){
			Transaction.execute(manager.createEntityManager(), new Transaction(){
				{closeEntityManager=true;}
				@SuppressWarnings("unchecked")
				@Override
				public void doTransaction(EntityManager entityManager) {
					ZZMeal meal = entityManager.merge(m);
					Posilek pos = meal.getPosilek();
					Danie danie = new Danie();
					danie.setNazwa("Nieokreślone");
					danie.setPosilek(pos);
					entityManager.persist(danie);
					pos.getDania().add(danie);
					for(ZZExpenditure expenditure : meal.getExpenditures()){
						Partia partia = expenditure.getProduct().getPartia();
						Wyprowadzenie wyp = new Wyprowadzenie();
						wyp.setDataWyprowadzenia(expenditure.getData());
						wyp.setPartia(partia);
						wyp.setTytulem(expenditure.getTytulem());
						wyp.setIloscJednostek(expenditure.getIlosc());
                        wyp.setDanie(danie);
						entityManager.persist(wyp);
						danie.getWyprowadzenia().add(wyp);
					}
				}
			});
		}

	}


	private static void geenrateMeals(DBManager manager) {
		final List<Date> dni = new ArrayList<Date>();
		Transaction.execute(manager.createEntityManager(), new Transaction(){
			{closeEntityManager=true;}
			@SuppressWarnings("unchecked")
			@Override
			public void doTransaction(EntityManager entityManager) {
				dni.addAll(entityManager.createQuery("SELECT DISTINCT(p.data) from ZZMeal p").getResultList());
			}
		});
		for(final Date dzienData : dni){
			Transaction.execute(manager.createEntityManager(), new Transaction(){
				{closeEntityManager=true;}
				@SuppressWarnings("unchecked")
				@Override
				public void doTransaction(EntityManager entityManager) {
					Dzien dzien = new Dzien();
					dzien.setData(dzienData);
					dzien.setIloscOsob(new IloscOsob(100,10,0));
					Query q = entityManager.createQuery("SELECT m FROM ZZMeal m WHERE m.data = :data");
					q.setParameter("data", dzienData);
					entityManager.persist(dzien);
					List<ZZMeal> meals = q.getResultList();
					for(ZZMeal meal : meals){
						Posilek p = new Posilek(meal.getNazwa(), dzien);
						entityManager.persist(p);
						meal.setPosilek(p);
					}
				}
			});
		}
	}

	private static void generateProducts(DBManager manager) {
		final List<String> nazwy = new ArrayList<String>();
		Transaction.execute(manager.createEntityManager(), new Transaction(){
			{closeEntityManager=true;}
			@SuppressWarnings("unchecked")
			@Override
			public void doTransaction(EntityManager entityManager) {
				nazwy.addAll(entityManager.createQuery("SELECT DISTINCT(p.name) from ZZProduct p").getResultList());
			}
		});
		for(final String s : nazwy){
			Transaction.execute(manager.createEntityManager(), new Transaction(){
				{closeEntityManager=true;}
				@Override
				public void doTransaction(EntityManager entityManager) {
					Produkt p = new Produkt();
					p.setDataWaznosci(-1);
					p.setJednostka("Nieistotna");
					p.setNazwa(s);
					entityManager.persist(p);
					Query q = entityManager.createQuery("SELECT p FROM ZZProduct p WHERE p.name = :nazwa");
					q.setParameter("nazwa", s);
					for(ZZProduct prod :  (List<ZZProduct>)q.getResultList()){
						Partia partia = new Partia();
						partia.setCena(prod.getPrice());
						partia.setDataKsiegowania(prod.getBookingDate());
						partia.setDataWaznosci(prod.getExpiryDate());
						partia.setIloscPocz(prod.getSQuantity());
						partia.setJednostka(prod.getPackaging());
						partia.setNumerFaktury(prod.getFacturaNo());
						partia.setOpis(prod.getDescription());
						partia.setSpecyfikator(prod.getUnit());
						partia.setProdukt(p);
						prod.setPartia(partia);
						entityManager.persist(partia);
					}
					entityManager.persist(p);
				}

			});
		}
	}

	private static final void feedProductsIntoDb(DBManager manager) {
		final Collection<Product> products = StaticContent.sc.getDataBase()
				.getDatabase().values();

		for (final Product product : products) {

			Transaction.execute(manager.createEntityManager(),
					new Transaction() {
						{
							closeEntityManager = true;
						}

						@Override
						public void doTransaction(EntityManager entityManager) {
							try {
								entityManager.persist(product(product));
							} catch (ZZIllegalEx e) {
								e.printStackTrace();
							}

						}
					});

		}

	}

	private static final void feedMealsIntoDb(DBManager manager){
		final Collection<Day> days = StaticContent.sc.getDays();
		for(Day d : days){
			for(Mealv3 mealv3 : d.getMeals()){
				feedOneMeal(manager, mealv3);
			}
		}

	}

	private static final void feedOneMeal(DBManager manager, final Mealv3 mealv3){
		Transaction.execute(manager.createEntityManager(), new Transaction(){
			{closeEntityManager = true;}
			@Override
			public void doTransaction(EntityManager entityManager) {
				ZZMeal meal = new ZZMeal();
				meal.setData(date(mealv3.getDate()));
				meal.setNazwa(mealv3.getName());
				for(MealExpenditure me : mealv3.getContents().values()){
					ZZExpenditure expenditure = expenditure(me, entityManager);
					entityManager.persist(expenditure);
					meal.getExpenditures().add(expenditure);
				}
				entityManager.persist(meal);
			}
		});
	}

	private static final ZZExpenditure expenditure(MealExpenditure expenditure, EntityManager entityManager){
		ZZExpenditure result = new ZZExpenditure();
		result.setData(date(expenditure.date));
		result.setTytulem(expenditure.tytulem);
		result.setIlosc(BigDecimal.valueOf(expenditure.ile));
		result.setProduct(entityManager.find(ZZProduct.class, (long)expenditure.getPId().getVal()));
		return result;
	}

	private static final Date date(MyDate date){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, date.getDay());
		c.set(Calendar.MONTH, date.getMonth()-1);
		c.set(Calendar.YEAR, date.getYear());
		return c.getTime();
	}

	private static final ZZProduct product(Product product) throws ZZIllegalEx{
		ZZProduct result = new ZZProduct();
		result.setId((long)(product.getId().getVal()));
		result.setName(product.getName());
		result.setFacturaNo(product.getFacturaNo());
		result.setPrice(BigDecimal.valueOf(product.getPrice()));
		result.setDescription(product.getDescription());
		result.setExpiryDate(date(product.getExpiryDate()));
		result.setBookingDate(date(product.getDateOfBooking()));
		result.setPackaging(product.getUnit());
		result.setUnit(product.getUnit());
		result.setSQuantity(BigDecimal.valueOf(product.getSQuantity()));
		return result;
	}
}
