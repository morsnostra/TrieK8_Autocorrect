// Source code is decompiled from a .class file using FernFlower decompiler.
import datastroke_UAS.ISpellCorrector;
import datastroke_UAS.SpellCorrector;
import java.io.IOException;
import java.util.Scanner;

public class Main {
   
   public static void main(String[] args) throws IOException {

      Scanner scanner = new Scanner(System.in); 

      String dictionaryFileName = "notsobig.txt";

      //String dictionaryFileName = "D:\\KULIAH 2024-2028\\UNTAR\\SEMESTER 2\\Data Structures\\UAS\\Trie_AutocorrectK8\\Autocorrect\\src\\n" + //
                  //"otsobig.txt";

      System.out.print("Type something: ");
      String userInput = scanner.nextLine();  
      
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
