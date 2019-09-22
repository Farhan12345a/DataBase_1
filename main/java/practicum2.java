import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adt.Response;

public class practicum2 {
	public static void main(String[] args) {
//		Set<String> permuations = new HashSet<String>();
//		String word = "apple";
//		char[] lettersInWord = new char[word.length()];
//		for(int  i = 0; i < word.length(); i++) {
//			char letter = word.charAt(i);
//			lettersInWord[i] = letter;
//			System.out.println(lettersInWord[i]);
//		}
//	
		String word = "GIANNIS";
		int wordLength = word.length();
		System.out.println("Length of the word: " + wordLength);
		List<Integer> listOfOccurences = new ArrayList<Integer>();
		char[] letters = {'A','B','C','D','E','F','G','H','I','J','K','L',
				'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		for(int i =0; i < letters.length;i++) {
			int letterCount=0;
			for(int j =0; j <wordLength; j++) {
				if(letters[i] == word.charAt(j)) {
					letterCount++;
				}
			}
			if(letterCount!=0) {
			listOfOccurences.add(letterCount);
			}
		}
		System.out.println(listOfOccurences);
		int factorialProduct = 1;
		for(int i =0; i < listOfOccurences.size();i++) {
			int number = listOfOccurences.get(i);
			factorialProduct *= factorial(number);
		}
		System.out.println(factorialProduct);
		int answer = factorial(wordLength)/factorialProduct;
		System.out.println(answer);
	}
	
	public static int factorial(int n) {
		int factorial = 1;
		for(int i = 2; i <= n; i++) {
			factorial = factorial * i;
		}
		return factorial;
	}

}
