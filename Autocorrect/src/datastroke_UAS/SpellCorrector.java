package datastroke_UAS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
   private Trie trie = new Trie();
   private Map<String, Integer> wordFrequency = new HashMap<>();
   private static final List<String> invalidInput = Arrays.asList("lol", "abcdefghijklmnopqrstuvwxyz");

   public SpellCorrector() {
   }

   public void useDictionary(String dictionaryFileName) throws IOException {
      try {
         FileReader fr = new FileReader(dictionaryFileName);
         BufferedReader br = new BufferedReader(fr);
         String currentLine = null;

         while((currentLine = br.readLine()) != null) {
            String cleanWord = currentLine.toLowerCase();
            if (!currentLine.contains(" ")) {
               this.wordFrequency.put(cleanWord, this.wordFrequency.getOrDefault(cleanWord, 0) + 1);
               this.trie.add(cleanWord);
            } else {
               String[] splittedString = currentLine.split("\\s");
               for(String singleString : splittedString) {
                  String cleanStr = singleString.toLowerCase();
                  this.wordFrequency.put(cleanStr, this.wordFrequency.getOrDefault(cleanStr, 0) + 1);
                  this.trie.add(cleanStr);
               }
            }
         }
         
         fr.close();
         br.close();
      } catch (FileNotFoundException ex) {
         System.err.println("File not found: " + ex.getMessage());
         throw ex;
      } catch (IOException ex) {
         System.err.println("IO Exception: " + ex.getMessage());
         throw ex;
      }
   }

   public String suggestSimilarWord(String inputWord) {
      if (inputWord == null || inputWord.length() == 0 || invalidInput.contains(inputWord.toLowerCase())) {
         return null;
      }
      
      String searchWord = inputWord.toLowerCase();
      String suggestionResult = null;

      TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
      INode node = this.trie.find(searchWord);
      
      if (node == null) {
         // Word not found in dictionary, find similar words
         Iterator<String> iterator = this.wordFrequency.keySet().iterator();
         
         while(iterator.hasNext()) {

            String candidateWord = iterator.next();
            int dist = this.editDistance(candidateWord, searchWord);

            TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
            int freq = this.wordFrequency.get(candidateWord);

            TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
            set.add(candidateWord);
            similarWords.put(freq, set);
            map.put(dist, similarWords);
         }
         
         if (!map.isEmpty()) {
            suggestionResult = map.firstEntry().getValue().lastEntry().getValue().first();
         }
      } else {
         // Word found in dictionary
         suggestionResult = searchWord;
      }

      return suggestionResult;
   }

   private int editDistance(String word1, String word2) {
      int n = word1.length();
      int m = word2.length();
      int[][] dp = new int[n + 1][m + 1];

      for(int i = 0; i <= n; ++i) {
         for(int j = 0; j <= m; ++j) {
            if (i == 0) {
               dp[i][j] = j;
            } else if (j == 0) {
               dp[i][j] = i;
            } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
               dp[i][j] = dp[i - 1][j - 1];
            } else if (i > 1 && j > 1 && 
                      word1.charAt(i - 1) == word2.charAt(j - 2) && 
                      word1.charAt(i - 2) == word2.charAt(j - 1)) {
               // Transposition case
               dp[i][j] = 1 + Math.min(Math.min(dp[i - 2][j - 2], dp[i - 1][j]), 
                                      Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
            } else {
               dp[i][j] = 1 + Math.min(dp[i][j - 1], 
                                      Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
            }
         }
      }

      return dp[n][m];
   }
}