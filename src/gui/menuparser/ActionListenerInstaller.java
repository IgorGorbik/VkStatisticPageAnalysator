package gui.menuparser;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.swing.JMenuItem;

/**
 *
 * @author Игорь
 */
public class ActionListenerInstaller {

    public static void proccessAnnotations(Object object, XMLMenuLoader loader) throws Exception {
        Class<?> c = object.getClass();
        for (Method m : c.getDeclaredMethods()) {
            ActionListenerFor a = m.getAnnotation(ActionListenerFor.class);
            if (a != null) {
                String str = a.source();
                JMenuItem item = loader.getMenuItem(str);
                addListener(item, object, m);
            }
        }
    }

    private static void addListener(JMenuItem item, final Object o, final Method method) throws Exception {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method1, Object[] args) throws Throwable {
                return method.invoke(o);
            }
        };

        Object listener = Proxy.newProxyInstance(null, new Class[]{java.awt.event.ActionListener.class}, handler);
        Method adder = item.getClass().getMethod("addActionListener", ActionListener.class);
        adder.invoke(item, listener);
    }

}
