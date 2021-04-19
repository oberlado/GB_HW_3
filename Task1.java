package les1;
import java.util.Arrays;

public class Task1 {
    public static void swap(Object[] arr, int index1, int index2) {
        Object tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
}
}
