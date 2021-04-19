package les1;

import java.util.ArrayList;
import java.util.Arrays;
public class Task2 {
    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<T>(Arrays.asList(arr));
    }
}
