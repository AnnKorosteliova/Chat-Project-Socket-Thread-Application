package lib;

import java.util.Scanner;

public class MessageIO {

	public static String scan(String what) {
		System.out.println(what + ": ");
		return new Scanner(System.in).nextLine();
	}
	
	public static void print(String what) {
		System.out.println(what);
	}
	
}
