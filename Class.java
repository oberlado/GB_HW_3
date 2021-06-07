package less;

import java.util.Arrays;

public class Class {
    public static int[] array4(int[] arr) {
        for (int i = arr.length + 1; i >= 0; i--) {
            if (arr[i] == 4) {
                return Arrays.copyOfRange(arr, i + 1, arr.length);
            }
        }
            throw new RuntimeException("Нету 4");
        }

    public static boolean array1and4(int[]arr){
    boolean a = false;
    boolean b = false;
    for (int i =0;i< arr.length;i++){
        if (arr[i] != 1 && arr[i]!=4){return false;}
        if (arr[i]==1){a=true;}
        if (arr[i]==4){b=true;}
    }
        return a && b;
    }
}

