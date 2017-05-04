package com.java8.learn;

import java.util.Arrays;
import java.util.Comparator;

public class Java8Introduction {
    public static void main(String[] args) {
        ///////////////////////////////////////////////////////////////////////////////
    	new Thread(new Runnable() {
			@Override
			public void run() {
               	System.out.println("匿名内部类编程");			
			}
		}).start();
		
		new Thread(() -> System.out.println("函数式编程")).start();
		new Thread(() -> {System.out.println("函数式编程");}).start();
		
		///////////////////////////////////////////////////////////////////////////////
		String[] oldWay = "Improving code with Lambda expressions in Java 8".split(" ");
		Arrays.sort(oldWay, new Comparator<String>(){
			@Override
			public int compare(String str1, String str2) {
				// 忽略大小写排序:
				return str1.toLowerCase().compareTo(str2);
			}
		});
		System.out.println(String.join(",", oldWay));
		
		
		//对于只有一个方法的接口,在java8中,现在可以把他视为一个函数,用lambda表达式简化
		String[] newWay = "Improving code with Lambda expressions in Java 8".split(" ");
		Arrays.sort(newWay,(str1,str2) ->  { 
			return str1.toLowerCase().compareTo(str2);
		});
		
		System.out.println(String.join(",", newWay));
		
		
        ///////////////////////////////////////////////////////////////////////////////
		//old way
    	Runnable oldRunnable = new Runnable() {
			@Override
			public void run() {
                   System.out.println(Thread.currentThread().getName() + ":old Runnable");				
			}
		};
		
		//new way
		Runnable newRunnable = () ->  {
			System.out.println(Thread.currentThread().getName() + ":new Lambda Runnable");
		};
		
		new Thread(oldRunnable).start();	
		new Thread(newRunnable).start();
	}
}
