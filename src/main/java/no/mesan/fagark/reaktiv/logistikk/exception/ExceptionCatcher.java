package no.mesan.fagark.reaktiv.logistikk.exception;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ExceptionCatcher implements MethodInterceptor {

    public ExceptionCatcher() {
        super();
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> target) {
        return (T) Enhancer.create(target, new ExceptionCatcher());
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> target, final Object constructorParam) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new ExceptionCatcher());
        enhancer.setSuperclass(target);
        return (T) enhancer.create(new Class[] { constructorParam.getClass() }, new Object[] { constructorParam });
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> target, final Object... constructorParam) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new ExceptionCatcher());
        enhancer.setSuperclass(target);
        final Class<?>[] clazzes = new Class[constructorParam.length];
        final Object[] objects = new Object[constructorParam.length];

        for (int i = 0; i < constructorParam.length; i++) {
            clazzes[i] = constructorParam[i].getClass();
            objects[i] = constructorParam[i];
        }

        try {
            return (T) enhancer.create(clazzes, objects);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] objects, final MethodProxy mehodProxy)
            throws Throwable {
        final ExceptionCatchable annotation = method.getAnnotation(ExceptionCatchable.class);

        if (annotation == null) {
            return mehodProxy.invokeSuper(obj, objects);
        }

        try {
            return mehodProxy.invokeSuper(obj, objects);
        } catch (final Exception e) {
            final Catchable newInstance = annotation.catcher().newInstance();
            newInstance.handle(e);
            throw e;
        }
    }

}
