package test.ui;

import cx.ath.jbzdak.jpaGui.beanFormatter.PatternBeanFormatter;
import junit.framework.Assert;
import org.junit.Test;


public class PatternBeanFormatterTest {

	public static class BarBean {
		String baz;

		public BarBean(String baz) {
			super();
			this.baz = baz;
		}

		@Override
		public String toString() {
			return "BarBean[baz='"+baz+"']";
		}

		public String getBaz() {
			return baz;
		}

		public void setBaz(String baz) {
			this.baz = baz;
		}
	}

	public static class FooBean{

		String foo;

		BarBean bar;

		Boolean floo = true;

		@Override
		public String toString() {
			return "FooBean[foo='"+foo+"', baz='" + bar + "']";
		}


		public FooBean(String foo, BarBean bar) {
			super();
			this.foo = foo;
			this.bar = bar;
		}


		public String getFoo() {
			return foo;
		}


		public BarBean getBar() {
			return bar;
		}


		public void setFoo(String foo) {
			this.foo = foo;
		}


		public void setBar(BarBean bar) {
			this.bar = bar;
		}


		public Boolean getFloo() {
			return floo;
		}


		public void setFloo(Boolean floo) {
			this.floo = floo;
		}
	}


	private FooBean foo1 = new FooBean("1foo", new BarBean("1bar"));

	private FooBean foo2 = new FooBean("2foo", new BarBean("2bar"));

	String testPattern1 = "<html>{}</html>";

	private void test(String pattern, String expected){
		Assert.assertEquals(new PatternBeanFormatter(pattern).format(foo1,foo2), expected);
	}

	@Test
	public void test1(){
		test(testPattern1, "<html>FooBean[foo='1foo', baz='BarBean[baz='1bar']']</html>");
	}

	@Test
	public void test2(){
		test("{}", foo1.toString());
	}

	@Test
	public void test3(){
		test("{1}", foo2.toString());
	}

	@Test
	public void test4(){
		test("{bar}", foo1.getBar().toString());
	}

	@Test
	public void test5(){
		test("{1.bar}", foo2.getBar().toString());
	}

	@Test
	public void test6(){
		test("{1.bar.baz}", "2bar");
	}

	@Test
	public void test7(){
		System.out.println(new PatternBeanFormatter("{#1}{floo?\"xxx\":\"yyyy\"}").format(foo1,foo2));
		test("{#1}{floo?\"xxx\":\"yyyy\"}", "xxx");
	}

	@Test
	public void test8(){
		System.out.println(new PatternBeanFormatter("Weekday: {#weekday,firstUppercase}{}").format(new java.util.Date()));
		//Assert.assertEquals(new PatternBeanFormatter("{#weekday}{date}").format(new Date()), expected);
	}
}
