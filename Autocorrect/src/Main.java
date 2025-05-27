// Source code is decompiled from a .class file using FernFlower decompiler.
import datastroke_UAS.ISpellCorrector;
import datastroke_UAS.SpellCorrector;
import java.io.IOException;

public class Main {
   
   public static void main(String[] args) throws IOException {
      String dictionaryFileName = "D:\\KULIAH 2024-2028\\UNTAR\\SEMESTER 2\\Data Structures\\UAS\\Trie_AutocorrectK8\\Autocorrect\\src\\n" + //
                  "otsobig.txt";
      String inputWord = "example"; // Replace with the word you want to check
      ISpellCorrector corrector = new SpellCorrector();
      corrector.useDictionary(dictionaryFileName);
      String suggestion = corrector.suggestSimilarWord(inputWord);
      if (suggestion == null) {
         suggestion = "No similar word found";
      }

      System.out.println("Suggestion is: " + suggestion);
   }
}
