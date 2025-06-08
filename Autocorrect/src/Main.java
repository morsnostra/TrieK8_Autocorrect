// Source code is decompiled from a .class file using FernFlower decompiler.
import datastroke_UAS.ISpellCorrector;
import datastroke_UAS.SpellCorrector;
import java.io.IOException;

public class Main {
   
   public static void main(String[] args) throws IOException {
      String dictionaryFileName = "C:\\Users\\Lyvia Reva Ruganda\\Documents\\Academics\\Code\\Java\\TrieK8_Autocorrect\\Autocorrect\\src\\notsobig.txt";
      String inputWord = "lipayah"; // Replace with the word you want to check
      ISpellCorrector corrector = new SpellCorrector();
      corrector.useDictionary(dictionaryFileName);
      String suggestion = corrector.suggestSimilarWord(inputWord);
      if (suggestion == null) {
         suggestion = "No similar word found";
      }

      System.out.println("Suggestion is: " + suggestion);
   }
}
