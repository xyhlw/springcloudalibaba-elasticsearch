package com.xy.cloud.search.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yang
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * 上下文对象实例
     */
    private static ApplicationContext APPLICATION_CONTENT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTENT = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTENT;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name name
     * @return Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz 类名称
     * @param <T>   实例化泛型
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  服务别称
     * @param clazz 类名
     * @param <T>   实例化泛型
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
