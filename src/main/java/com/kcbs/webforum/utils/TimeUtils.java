package com.kcbs.webforum.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {

    //时间戳转时间
    public static String stampToDate(long time){
        if (time==0){
            return null;
        }
        String s = String.valueOf(time).substring(0,13);
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    //往后多少S的日期
    public static Date getEndDate(Integer s){
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND,+s);
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        return date;
    }


    public static <E extends Comparable<E>> E[] sort(E[] data){
        for (int i=0;i<data.length;i++){
            int min = i;
            for (int j=i+1;j<data.length;j++){
                if (data[min].compareTo(data[j])>0){
                   min = j;
                }
            }
            swap(data, i, min);
        }
        return data;
    }

    private static <E> void swap(E[] data, int i, int min) {
        E t = data[min];
        data[min] = data[i];
        data[i] = t;
    }

    public static <E extends Comparable<E>> E[] sort2(E[] data){
        for (int i=0;i<data.length;i++){
            E temp;
            for (int j=i;j-1>=0;j--){
                if (data[j].compareTo(data[j-1])<0){
                    temp = data[j];
                    data[j] = data[j-1];
                    data[j-1] = temp;
                }else {
                    break;
                }
            }

        }
        return data;
    }

    public interface Stack<E>{
        void push(E e);
        E pop();
        E top();
        int size();
    }

    public static class ArrStack<E> implements Stack<E>{
        List<E> arr;

        public ArrStack(){
            arr = new ArrayList<>();
        }

        @Override
        public void push(E e) {
            arr.add(e);
        }

        @Override
        public E pop() {
            E e = arr.get(arr.size()-1);
            arr.remove(arr.size()-1);
            return e;
        }

        @Override
        public E top() {
            return arr.get(arr.size()-1);
        }

        @Override
        public int size() {
            return arr.size();
        }
    }

    public static int sum(int n){
        return n>0?n+sum(n-1):n;
    }

    public static int getbill(int billAmount){
        if(billAmount<0||billAmount>1000000000){
            throw new IllegalArgumentException("参数异常");
        }
        String s = String .valueOf(billAmount);
        int sum=0;
        for(int i=0;i<s.length();i++){
            if (Integer.parseInt(s.substring(i,i+1))%2==1){
                sum += Integer.parseInt(s.substring(i,i+1));
            }
        }
        return sum;
    }

    public static int getNum(int n,String s){
        String[] s1 = s.split(" ");
        int num = 0;
        for (String a:s1){
            if (Integer.parseInt(a)<n){
                num++;
            }
        }
        return num;
    }


    public static class LinkedList<E>{
         public class Node{
             private E val;
             private Node next;

             public Node(E e,Node next){
                 this.val = e;
                 this.next = next;
             }

             public Node(E e){
                 this(e,null);
             }

             public Node(){
                 this(null,null);
             }
         }
         public  Node dummyHead;
         private int size;

         public LinkedList(){
             dummyHead = new Node();
             size = 0;
         }

         private void add(int index,E e){
             if (index<0 || index>size){
                 throw new IllegalArgumentException("Add Failed");
             }
             Node prev = dummyHead;
             for (int i=0;i<index;i++){
                 prev = prev.next;
             }
             prev.next = new Node(e,prev.next);
             size++;
         }

        public void addFront(E e){
            add(0,e);
        }

        public void addLast(E e){
             add(size,e);
        }

        public int getSize(){
             return size;
        }

        public boolean isEmpty(){
             return size==0;
        }

        public E get(int index){
             if (index<0||index>size){
                 throw new IllegalArgumentException("get Failed");
             }
             Node cur = dummyHead.next;
             for (int i=0;i<index;i++){
                 cur = cur.next;
             }
             return cur.val;
        }

        @Override
        public String toString(){
             StringBuffer sb = new StringBuffer();
             for (Node cur=dummyHead.next;cur!=null;cur=cur.next){
                 sb.append(cur.val+"->");
             }
             sb.append("NULL");
             return sb.toString();
         }

         public void reverseList(){
             Node pre = null;
             Node cur = dummyHead.next;
             Node next = null;
             while (cur!=null){
                 next = cur.next;
                 cur.next = pre;
                 pre = cur;
                 cur = next;
             }
             dummyHead.next = pre;
         }


    }

    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i=0;i<5;i++)
            linkedList.addFront(i);
        System.out.println(linkedList);
        linkedList.reverseList();
        System.out.println(linkedList);
    }
}
