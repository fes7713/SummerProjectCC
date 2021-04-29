package ArrayListSpeedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int SIZE = 10000000;

    public static void main(String[] args) {
        long start, end;
        System.out.println("ListSize : " + SIZE);

        Random random = new Random();
        String[] sample = new String[SIZE];
        for(int i = 0; i < sample.length; i++){
            sample[i] = Integer.toString(random.nextInt());
        }

        for(int i = 0; i < 5; i++){
            start = System.currentTimeMillis();
            arrayTest(sample);
            end = System.currentTimeMillis();
            output("配列", start, end);

            System.gc();

            start = System.currentTimeMillis();
            arrayListTest(sample);
            end = System.currentTimeMillis();
            output("リスト", start, end);

            System.gc();
        }
    }

    private static void arrayTest(String[] sample){
        String[] array = new String[SIZE];
        for(int i = 0; i < sample.length; i++){
            array[i] = sample[i];
        }
    }

    private static void arrayListTest(String[] sample){
        List<String> arrayList = new ArrayList<String>(SIZE);	// ここでArrayListの初期容量を指定
        for(int i = 0; i < sample.length; i++){
            arrayList.add(sample[i]);
        }
    }

    private static void output(String message, long start, long end){
        long time = end - start;
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.print(message + " -> ");
        System.out.println("時間：" + time + ", " + "メモリ使用量:" + usedMemory);
    }
}