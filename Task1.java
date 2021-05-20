package leshome;

public class Task1 {
    public static <T> void swap(T[] arr, int in1, int in2){
    T obj = arr[in1];
    arr[in1]=arr[in2];
    arr[in2]= obj;
}
}
