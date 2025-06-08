package datastroke_UAS;

public class Trie implements ITrie {
   private TrieNode root = new TrieNode();
   private int myHashCode = 0;

   public Trie() {}

   public void add(String word) {
      TrieNode p = this.root;
      for (char c : word.toCharArray()) {
         if (p.nodes[c - 'a'] == null) {
            p.nodes[c - 'a'] = new TrieNode();
         }
         p = p.nodes[c - 'a'];
      }
      this.myHashCode += word.hashCode();
      p.incrementValue();
      p.isEnd = true;
   }

   public INode find(String word) {
      class StackFrame {
         TrieNode node;
         int index;

         StackFrame(TrieNode node, int index) {
            this.node = node;
            this.index = index;
         }
      }

      MyLinearList<StackFrame> stack = new MyLinearList<>();
      stack.pushS(new StackFrame(this.root, 0));

      while (!stack.isEmpty()) {
         StackFrame frame = stack.remove();
         TrieNode current = frame.node;
         int index = frame.index;

         if (index == word.length()) {
            if (current != null && current.isEnd) {
               return current;
            }
            continue;
         }

         char c = word.charAt(index);
         TrieNode next = current.nodes[c - 'a'];
         if (next != null) {
            stack.pushS(new StackFrame(next, index + 1));
         }
      }

      return null;
   }

   public int getNodeCount() {
      return countNodesInTrie(this.root);
   }

   private int countNodesInTrie(TrieNode node) {
      if (node == null) return 0;

      int count = 0;
      for (int i = 0; i < 26; ++i) {
         if (node.nodes[i] != null) {
            count += countNodesInTrie(node.nodes[i]);
         }
      }

      return 1 + count;
   }

   public int getWordCount() {
      return wordCount(this.root);
   }

   private int wordCount(TrieNode node) {
      int result = 0;
      if (node.isEnd) {
         ++result;
      }

      for (int i = 0; i < 26; ++i) {
         if (node.nodes[i] != null) {
            result += wordCount(node.nodes[i]);
         }
      }

      return result;
   }

   public String toString() {
      char[] wordArray = new char[50];
      StringBuilder sb = new StringBuilder();
      printAllWords(this.root, wordArray, 0, sb);
      return sb.length() == 0 ? "" : sb.substring(1);
   }

   private void printAllWords(TrieNode node, char[] wordArray, int pos, StringBuilder sb) {
      if (node != null) {
         if (node.isEnd) {
            sb.append("\n");
            for (int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }

         for (int i = 0; i < 26; ++i) {
            if (node.nodes[i] != null) {
               wordArray[pos] = (char) (i + 'a');
               printAllWords(node.nodes[i], wordArray, pos + 1, sb);
            }
         }
      }
   }

   public int hashCode() {
      return this.myHashCode;
   }

   public boolean equals(Object o) {
      if (o instanceof Trie) {
         Trie s = (Trie) o;
         if (this.getNodeCount() != s.getNodeCount()) return false;
         if (this.getWordCount() != s.getWordCount()) return false;
         return compareTrie(this, s);
      }
      return false;
   }

   private boolean compareTrie(Trie p, Trie q) {
      String s1 = p.toString();
      String s2 = q.toString();
      if (s1.equals("") && s2.equals("")) return true;

      String[] strs1 = s1.split("\n");
      String[] strs2 = s2.split("\n");
      if (strs1.length != strs2.length) return false;

      for (String s : strs1) {
         INode node1 = p.find(s);
         INode node2 = q.find(s);
         if (node1 == null || node2 == null || node1.getValue() != node2.getValue()) {
            return false;
         }
      }

      return true;
   }

   public static void main(String[] args) {
      ITrie trie = new Trie();
      trie.add("car");
      trie.add("care");
      trie.add("cares");
      trie.add("cat");

      System.out.println("Trie Content:");
      System.out.println(trie);

      System.out.println("Find 'care': " + (trie.find("care") != null));
      System.out.println("Find 'cart': " + (trie.find("cart") != null));
   }
}
