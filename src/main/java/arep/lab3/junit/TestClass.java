package arep.lab3.junit;

public class TestClass {
    @Test
    public static void test1(){
        System.out.println("Test 1");
    }

    public static void test2() throws Exception {
        throw new Exception("Exception 1");
    }
    @Test
    public static void test3(){
        System.out.println("Test 3");
    }

    public static void test4() throws Exception {
        throw new Exception("Exception 1");
    }

    public static void test5(){
        System.out.println("Test 5");
    }
    @Test
    public static void test6() throws Exception {
        throw new Exception("Exception 1");
    }
    public static void test7(){
        System.out.println("Test 7");
    }
}
