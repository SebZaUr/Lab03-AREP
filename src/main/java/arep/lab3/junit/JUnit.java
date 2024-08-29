package arep.lab3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JUnit {
    public static void main (String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(args[0]);
        Method[] listMethods = c.getDeclaredMethods();
        int complete = 0;
        int fail = 0;
        for(Method method: listMethods){
            if(method.isAnnotationPresent(Test.class)){
                try {
                    method.invoke(null);
                    complete++;
                }catch(Exception e){
                    fail++;
                }
            }
        }
        float percentage = (float) complete /listMethods.length;
        System.out.println("Test Finished");
        System.out.println("Complete: " + complete);
        System.out.println("Fail: " + fail);


    }

}
