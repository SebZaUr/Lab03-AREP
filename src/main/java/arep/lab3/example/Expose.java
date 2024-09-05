package arep.lab3.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Expose {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(args[0]);
        Class[] parametersType = {String[].class};
        Method main = c.getDeclaredMethod("main",parametersType);
        String[] params ={args[1],args[2]};
        main.invoke(null,(Object) params);
    }
}