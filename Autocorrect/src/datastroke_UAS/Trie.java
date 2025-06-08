package datastroke_UAS;

import com.datastruct.MyLinearList;

// implementasi utama trie 
public class Trie implements ITrie {
   
   private TrieNode root = new TrieNode();   // semua kata mulai dari root
   private int trieHashCode = 0;             // hash code trie buat equals comparison, diupdate tiap add kata

   // constructor kosong karena field udah di-initialize di atas (root sama trieHashCode)
   public Trie() {
   }

   // masukin kata baru ke trie
   public void add(String word) {
      TrieNode currentNode = this.root; // mulai dari root node

      // jalan dari root sampe ujung kata, buat node baru kalo belum ada
      for(char character : word.toCharArray()) {
         int indeks = character - 97;

         // kalo path belum ada, buat node baru
         if (currentNode.nodes[indeks] == null) {
            currentNode.nodes[indeks] = new TrieNode();
         }
         // pindah ke node selanjutnya
         currentNode = currentNode.nodes[indeks];
      }

      this.trieHashCode += word.hashCode();  // update hash code trie buat keperluan equals
      currentNode.incrementValue();          // tambah counter kata di node terakhir
      currentNode.isEnd = true;              // tandain ini akhir kata
   }

   // cari kata di trie, return nodenya kalo ketemu
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

   // hitung total node yang ada di trie
   public int getNodeCount() {
      return this.countNodesInTrie(this.root); // panggil helper method rekursif
   }

   // method rekursif untuk hitung node
   private int countNodesInTrie(TrieNode node) {
      if (node == null) {  // base case => node null
         return 0;
      }
      int count = 0;
      // cek semua 26 child node
      for(int i = 0; i < 26; ++i) {
         // kalo child node ada, hitung rekursif
         if (node.nodes[i] != null) {
            count += this.countNodesInTrie(node.nodes[i]);
         }
      }
      return 1 + count; // return total child node + node sekarang
   }

   // hitung berapa kata unik yang tersimpan
   public int getWordCount() {
      return this.wordCount(this.root);  // panggil helper method rekursif
   }

   // method rekursif untuk hitung kata
   private int wordCount(TrieNode root) {
      int result = 0; 
      // kalo di node ini ada kata yang berakhir, count++
      if (root.isEnd) {
         ++result;
      }
      // lanjut ke semua child node
      for(int i = 0; i < 26; ++i) {
         // kalo child node ada, hitung rekursif
         if (root.nodes[i] != null) {
            result += this.wordCount(root.nodes[i]);
         }
      }
      return result; // return total kata
   }

   // convert trie jadi string representation (semua kata dipisah newline)
   public String toString() {
      char[] wordArray = new char[50]; // buffer buat build kata
      StringBuilder sb = new StringBuilder();
      this.printAllWords(this.root, wordArray, 0, sb);
      return sb.toString().length() == 0 ? "" : sb.toString().substring(1); // buang newline pertama kalo ada
   }

   // method rekursif untuk print semua kata
   private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
      if (root != null) {
         // kalo di sini ada kata yang berakhir, print ke stringbuilder
         if (root.isEnd) {
            sb.append("\n");
            for(int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }
         // lanjut ke semua child node dengan nambah karakter
         for(int i = 0; i < 26; ++i) {
            // kalo child node ada
            if (root.nodes[i] != null) {
               wordArray[pos] = (char)(i + 97); // convert index ke karakter
               this.printAllWords(root.nodes[i], wordArray, pos + 1, sb);  // rekursif ke child node dgn posisi +1
            }
         }
      }
   }

   // return hash code trie
   public int hashCode() {
      return this.trieHashCode;
   }

   // bandingin dua trie apakah sama
   public boolean equals(Object o) {
      if (o instanceof Trie) {
         Trie s = (Trie)o;

         // cek basic properties dulu
         if (this.getNodeCount() != s.getNodeCount()) {
            return false;
         }
         if (this.getWordCount() != s.getWordCount()) {
            return false;
         }

         // deep comparison kata per kata
         return this.compareTrie(this, s);
      }
      return false;
   }

   // helper buat deep comparison trie
   private boolean compareTrie(Trie parameter1, Trie parameter2) {
      String string1 = parameter1.toString();
      String string2 = parameter2.toString();

       // edge case kedua trie kosong
      if (string1.equals("") && string2.equals("")) {
         return true;
      }
      
      // split jadi array kata
      String[] words1 = string1.split("\n");
      String[] words2 = string2.split("\n");

      // jumlah kata harus sama
      if (words1.length != words2.length) {
         return false;
      }
      
      // bandingin frekuensi tiap kata
      for(String word : words1) {
         INode node1 = parameter1.find(word);
         INode node2 = parameter2.find(word);
         if (node1.getValue() != node2.getValue()) {
            return false;
         }
      }

      return true;
   }

   // main method buat testing
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

      System.out.println("Find 'cares': " + (studentTrie.find("cares") != null));
      System.out.println("Find 'cart': " + (studentTrie.find("cart") != null));
   }
}