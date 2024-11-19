package org.raindrop.core.db.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by yangtong on 2024/5/14 17:16
 *
 * @Description:
 */
public class ProxyFactory implements InvocationHandler {

    public Object getProxy(Class[] targetInterface) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Proxy.newProxyInstance(classLoader, targetInterface, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法名称得到SQL
        SQLCreator.parseSqlByMethod(method, args);

        return null;
    }
}
