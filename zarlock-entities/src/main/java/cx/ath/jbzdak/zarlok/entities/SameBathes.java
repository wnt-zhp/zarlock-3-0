package cx.ath.jbzdak.zarlok.entities;

import org.apache.commons.math.util.MathUtils;

import javax.annotation.CheckForNull;


public class SameBathes implements IProductSearchCache {

	private Product product;

	private String specifier;

	private String unit;

	private Number price;

	private Number quantity;

   public SameBathes(Product product, String specifier,
			String unit, @CheckForNull Number price, @CheckForNull Number quantity) {
		super();
		this.product = product;
		this.specifier = specifier;
		this.unit = unit;

		this.price = price ==null?null:MathUtils.round(price.doubleValue(), 2);
		this.quantity = quantity ==null?null:MathUtils.round(quantity.doubleValue(), 2);
	}

   @SuppressWarnings({"WeakerAccess"})
   public Product getProdukt() {
		return product;
	}

	public String getSpecifier() {
		return specifier;
	}

	public String getUnit() {
		return unit;
	}

	public Number getPrice() {
		return price;
	}

	public void setProdukt(Product product) {
		this.product = product;
	}

	public void setSpecifier(String specifier) {
		this.specifier = specifier;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setPrice(Number price) {
		this.price = price;
	}

	public Number getQuantity() {
		return quantity;
	}

	public void setQuantity(Number quantity) {
		this.quantity = quantity;
	}

   @Override
   public String getProductName() {
      return getProdukt().getName();
   }
}
