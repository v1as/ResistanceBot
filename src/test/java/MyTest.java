/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class MyTest {


    public static void main1(String[] args) {
        System.out.println(new A().getValue() != true);
    }

    private static class A {
        private Boolean value;

        public Boolean getValue() {
            return value;
        }
    }
}
