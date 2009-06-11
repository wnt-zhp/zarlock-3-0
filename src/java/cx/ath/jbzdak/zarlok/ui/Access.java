package cx.ath.jbzdak.zarlok.ui;

import java.lang.reflect.Method;

public class Access {
    public static void main(String[] args) throws Exception {
        Class<?> imageFetcher = Class.forName("sun.awt.image.FetcherInfo");
        for (Method method : imageFetcher.getDeclaredMethods()) {
            ;
        }
        Method method = imageFetcher.getDeclaredMethod("getFetcherInfo");
        method.setAccessible(true);
        Object fetcher = method.invoke(null);
        System.err.println(fetcher);
    }
}
