// Source code is decompiled from a .class file using FernFlower decompiler.
import datastroke_UAS.ISpellCorrector;
import datastroke_UAS.SpellCorrector;
import java.io.IOException;
import java.util.Scanner;

public class Main {
   
   public static void main(String[] args) throws IOException {
<<<<<<< HEAD
      String dictionaryFileName = "C:\\Users\\Lyvia Reva Ruganda\\Documents\\Academics\\Code\\Java\\TrieK8_Autocorrect\\Autocorrect\\src\\notsobig.txt";
      String inputWord = "lipayah"; // Replace with the word you want to check
=======

      Scanner scanner = new Scanner(System.in); 

      String dictionaryFileName = "notsobig.txt";

      //String dictionaryFileName = "D:\\KULIAH 2024-2028\\UNTAR\\SEMESTER 2\\Data Structures\\UAS\\Trie_AutocorrectK8\\Autocorrect\\src\\n" + //
                  //"otsobig.txt";

      System.out.print("Type something: ");
      String userInput = scanner.nextLine();  
      
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
      ISpellCorrector corrector = new SpellCorrector();
      corrector.useDictionary(dictionaryFileName);
      String correctedWord = corrector.suggestSimilarWord(userInput);
      
      if (correctedWord == null) {
         correctedWord = "No similar word found";
      }

      System.out.println("Correction: " + correctedWord);

      scanner.close();
   }
}
