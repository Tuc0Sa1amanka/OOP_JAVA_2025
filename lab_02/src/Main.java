import annotation.Repeat;
import services.Example;
import services.ReflectionService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Main
{
    public static void main(String[] args)
    {
        Class<ReflectionService> clazz = ReflectionService.class;
        ReflectionService obj = new ReflectionService();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m: methods)
        {
            Repeat an = m.getAnnotation(Repeat.class);
            int mMod = m.getModifiers();
            if (an != null && (Modifier.isProtected(mMod) || Modifier.isPrivate(mMod)))
            {
                try
                {
                    Class<?>[] parameters = m.getParameterTypes();
                    Object[] parametersToInvoke = new Object[m.getParameterCount()];
                    for (int i = 0; i < parameters.length; i++)
                    {
                        parametersToInvoke[i] = setDefaultValue(parameters[i]);
                    }
                    int value = an.value();
                    for (int i = 0; i < value; i++)
                    {
                        m.setAccessible(true);
                        m.invoke(obj, parametersToInvoke);
                    }
                }
                catch (InvocationTargetException | InstantiationException | IllegalAccessException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    static public Object setDefaultValue(Class<?> parameter) throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        String type = parameter.getTypeName();
        switch (type)
        {
            case "boolean": return true;
            case "byte": return (byte) 127;
            case "char": return '\u0000';
            case "short": return (short) 32767;
            case "int": return 1380;
            case "long": return 22082006;
            case "float": return 1.232;
            case "double": return 22.12;
            default:
                Constructor<?>[] constructors = parameter.getDeclaredConstructors();
                if (constructors.length == 0)
                {
                    throw new IllegalStateException("this Class object represents an interface, an array class, or void" + parameter.getName());
                }
                Constructor<?> defaultConstructor = null;
                Constructor<?> parameterizedConstructor = null;
                for (Constructor<?> constructor : constructors)
                {
                    if (constructor.getParameterCount() == 0)
                    {
                        defaultConstructor = constructor;
                        break;
                    }
                    else if (parameterizedConstructor == null || constructor.getParameterCount() < parameterizedConstructor.getParameterCount())
                    {
                        parameterizedConstructor = constructor;
                    }
                }
                if (defaultConstructor != null)
                {
                    defaultConstructor.setAccessible(true);
                    return defaultConstructor.newInstance();
                }
                parameterizedConstructor.setAccessible(true);
                Class<?>[] paramTypes = parameterizedConstructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++)
                {
                    params[i] = setDefaultValue(paramTypes[i]);
                }
                return parameterizedConstructor.newInstance(params);
        }
    }
}