package datastroke_UAS;

public class Trie implements ITrie {
   private TrieNode root = new TrieNode();
   private int myHashCode = 0;

   public Trie() {
   }

   public void add(String word) {
      TrieNode p = this.root;
      for(char c : word.toCharArray()) {
         if (p.nodes[c - 97] == null) {
            p.nodes[c - 97] = new TrieNode();
         }
         p = p.nodes[c - 97];
      }

      this.myHashCode += word.hashCode();
      p.incrementValue();
      p.isEnd = true;
   }

   public INode find(String word) {
      TrieNode p = this.root;
      for(char c : word.toCharArray()) {
         if (p.nodes[c - 97] == null) {
            return null;
         }
         p = p.nodes[c - 97];
      }

      return p != null && p.isEnd ? p : null;
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
      return this.myHashCode;
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

   private boolean compareTrie(Trie p, Trie q) {
      String s1 = p.toString();
      String s2 = q.toString();
      if (s1.equals("") && s2.equals("")) {
         return true;
      }
      
      String[] strs1 = s1.split("\n");
      String[] strs2 = s2.split("\n");
      if (strs1.length != strs2.length) {
         return false;
      }
      
      for(String s : strs1) {
         INode node1 = p.find(s);
         INode node2 = q.find(s);
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