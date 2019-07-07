package com.yzg.toutiao;

import org.junit.Test;

/**
 * @author yzg
 * @create 2019/6/28
 */
public class paixuTest {

    @Test
    public void test() {


        int[] arr = {156, 23, 32, 22, 14, 35, 123, 22, 53, 95, 11, 8, 45};
        int[] arr2 = {};
        int[] arr3 = {3, 2, 1};
        //maopao(arr);
        //chose(arr);
        quick(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i : arr2) {
            System.out.print(i + " ");

        }
        System.out.println();
        for (int i : arr3) {
            System.out.print(i + " ");
        }
        System.out.println();

    }

    /**
     * 快速排序
     *
     * @param arr
     * @return
     */
    private void quick(int[] arr, int low, int high) {
        int i = low;
        int j = high;
        if (i >= j) {
            return;
        }
        //基准数
        int temp = arr[low];
        while (i < j) {
            //从后往前找到一个比基准数小的数的索引
            while (i < j && arr[j] >= temp) {
                j--;
            }
            //从前往后找到一个比基准数大的数的索引
            while (i < j && arr[j] <= temp) {
                i++;
            }
            //交换两数
            if (i < j) {
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
            }
        }
        //交换基数
        arr[low] = arr[i];
        arr[i] = temp;
        //递归排序前半部分
        quick(arr, low, j - 1);
        //递归排序后半部分
        quick(arr, j + 1, high);

    }

    /**
     * 插入排序
     * 将无序区的指定索引下的数拆入到有序区中
     *
     * @param arr
     * @return
     */
    public int[] insert(int[] arr) {
        int length = arr.length;

        return arr;
    }


    /**
     * 选择排序
     * 从未排序的序列中选择最小值添加到已排序序列末尾
     *
     * @param arr
     * @return
     */
    private int[] chose(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            //最小值的索引
            int k = i;
            for (int j = i + 1; j < length; j++) {
                if (arr[j] < arr[k]) {
                    k = j;
                }
            }
            if (k != i) {
                int temp = arr[i];
                arr[i] = arr[k];
                arr[k] = temp;
            }

        }
        return arr;
    }

    /**
     * 冒泡排序
     * 大的数往后移，直至最后一位为最大值
     *
     * @param arr
     * @return
     */
    private int[] maopao(int[] arr) {

        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }
}
