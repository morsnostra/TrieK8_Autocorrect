package datastroke_UAS;

public class Trie implements ITrie {
   private TrieNode root = new TrieNode();
   private int trieHashCode = 0;

   public Trie() {
   }

   public void add(String word) {
      TrieNode currentNode = this.root;
      for(char character : word.toCharArray()) {
         if (currentNode.nodes[character - 97] == null) {
            currentNode.nodes[character - 97] = new TrieNode();
         }
         currentNode = currentNode.nodes[character - 97];
      }

      this.trieHashCode += word.hashCode();
      currentNode.incrementValue();
      currentNode.isEnd = true;
   }

   public INode find(String word) {
      TrieNode currentNode = this.root;
      for(char character : word.toCharArray()) {
         if (currentNode.nodes[character - 97] == null) {
            return null;
         }
         currentNode = currentNode.nodes[character - 97];
      }

      return currentNode != null && currentNode.isEnd ? currentNode : null;
   }

   public int getNodeCount() {
      return this.countNodesInTrie(this.root);
   }

   private int countNodesInTrie(TrieNode node) {
      if (node == null) {
         return 0;
      }
      
      int count = 0;
      for(int i = 0; i < 26; ++i) {
         if (node.nodes[i] != null) {
            count += this.countNodesInTrie(node.nodes[i]);
         }
      }

      return 1 + count;
   }

   public int getWordCount() {
      return this.wordCount(this.root);
   }

   private int wordCount(TrieNode root) {
      int result = 0;
      if (root.isEnd) {
         ++result;
      }

      for(int i = 0; i < 26; ++i) {
         if (root.nodes[i] != null) {
            result += this.wordCount(root.nodes[i]);
         }
      }

      return result;
   }

   public String toString() {
      char[] wordArray = new char[50];
      StringBuilder sb = new StringBuilder();

      this.printAllWords(this.root, wordArray, 0, sb);
      return sb.toString().length() == 0 ? "" : sb.toString().substring(1);
   }

   private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
      if (root != null) {
         if (root.isEnd) {
            sb.append("\n");
            for(int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }

         for(int i = 0; i < 26; ++i) {
            if (root.nodes[i] != null) {
               wordArray[pos] = (char)(i + 97);
               this.printAllWords(root.nodes[i], wordArray, pos + 1, sb);
            }
         }
      }
   }

   public int hashCode() {
      return this.trieHashCode;
   }

   public boolean equals(Object o) {
      if (o instanceof Trie) {
         Trie s = (Trie)o;
         if (this.getNodeCount() != s.getNodeCount()) {
            return false;
         }
         if (this.getWordCount() != s.getWordCount()) {
            return false;
         }
         return this.compareTrie(this, s);
      }
      return false;
   }

   private boolean compareTrie(Trie parameter1, Trie parameter2) {
      String string1 = parameter1.toString();
      String string2 = parameter2.toString();
      if (string1.equals("") && string2.equals("")) {
         return true;
      }
      
      String[] words1 = string1.split("\n");
      String[] words2 = string2.split("\n");
      if (words1.length != words2.length) {
         return false;
      }
      
      for(String word : words1) {
         INode node1 = parameter1.find(word);
         INode node2 = parameter2.find(word);
         if (node1.getValue() != node2.getValue()) {
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      ITrie studentTrie = new Trie();
      ITrie studentTrie2 = new Trie();
      studentTrie.add("cares");
      studentTrie.add("caress");
      studentTrie.add("baboon");
      studentTrie.add("car");
      System.out.println("1:" + studentTrie);
      studentTrie2.add("cares");
      studentTrie2.add("caress");
      studentTrie2.add("baboon");
      System.out.println("2:" + studentTrie2);
      System.out.println();
      System.out.println(studentTrie.equals(studentTrie));
      System.out.println(studentTrie != null);
      // Fixed the problematic line - comparing ITrie objects, not String
      System.out.println(studentTrie.equals(null));
      System.out.println(studentTrie.equals(studentTrie2));
      studentTrie2.add("car");
      System.out.println(studentTrie.equals(studentTrie2));
      System.out.println(studentTrie.equals(studentTrie2) == studentTrie2.equals(studentTrie));
      studentTrie2.add("car");
      System.out.println(studentTrie.equals(studentTrie2));
   }
}