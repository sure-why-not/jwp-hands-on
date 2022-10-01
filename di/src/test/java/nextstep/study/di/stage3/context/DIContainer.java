package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.study.FunctionWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = createBeans(classes);
        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                Class<?> type = field.getType();
                field.setAccessible(true);
                Object o = getBean(type);
                if (o != null) {
                    field.set(bean, o);
                }
            }
        }
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(FunctionWrapper.apply(Class::getDeclaredConstructor))
                .peek(constructor -> constructor.setAccessible(true))
                .map(FunctionWrapper.apply(Constructor::newInstance))
                .collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        for (Object bean : beans) {
            if (bean.getClass().isInstance(aClass)) {
                return (T) bean;
            }
        }
        return null;
    }
}
