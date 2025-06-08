package datastroke_UAS;

// implementasi utama trie 
public class Trie implements ITrie {
   
   private TrieNode root = new TrieNode();   // semua kata mulai dari root
   private int trieHashCode = 0;             // hash code trie buat equals comparison, diupdate tiap add kata

<<<<<<< HEAD
   public Trie() {}
=======
   // constructor kosong karena field udah di-initialize di atas (root sama trieHashCode)
   public Trie() {
   }
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7

   // masukin kata baru ke trie
   public void add(String word) {
<<<<<<< HEAD
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
=======
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
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
   }

   // cari kata di trie, return nodenya kalo ketemu
   public INode find(String word) {
<<<<<<< HEAD
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
=======
      TrieNode currentNode = this.root; // mulai dari root node

      // dari root ikutin path kata
      for(char character : word.toCharArray()) {
         int indeks = character - 97;
         // kalo path ga ada, kata ga ketemu
         if (currentNode.nodes[indeks] == null) {
            return null;
         }
         // lanjut ke node selanjutnya
         currentNode = currentNode.nodes[indeks];
      }
      return currentNode != null && currentNode.isEnd ? currentNode : null;  // pastiin ini beneran akhir kata, bukan cuma prefix
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
   }

   // hitung total node yang ada di trie
   public int getNodeCount() {
<<<<<<< HEAD
      return countNodesInTrie(this.root);
=======
      return this.countNodesInTrie(this.root); // panggil helper method rekursif
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
   }

   // method rekursif untuk hitung node
   private int countNodesInTrie(TrieNode node) {
<<<<<<< HEAD
      if (node == null) return 0;

      int count = 0;
      for (int i = 0; i < 26; ++i) {
=======
      if (node == null) {  // base case => node null
         return 0;
      }
      int count = 0;
      // cek semua 26 child node
      for(int i = 0; i < 26; ++i) {
         // kalo child node ada, hitung rekursif
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
         if (node.nodes[i] != null) {
            count += countNodesInTrie(node.nodes[i]);
         }
      }
      return 1 + count; // return total child node + node sekarang
   }

   // hitung berapa kata unik yang tersimpan
   public int getWordCount() {
<<<<<<< HEAD
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
=======
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
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
         }
      }
      return result; // return total kata
   }

   // convert trie jadi string representation (semua kata dipisah newline)
   public String toString() {
      char[] wordArray = new char[50]; // buffer buat build kata
      StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
      printAllWords(this.root, wordArray, 0, sb);
      return sb.length() == 0 ? "" : sb.substring(1);
   }

   private void printAllWords(TrieNode node, char[] wordArray, int pos, StringBuilder sb) {
      if (node != null) {
         if (node.isEnd) {
=======
      this.printAllWords(this.root, wordArray, 0, sb);
      return sb.toString().length() == 0 ? "" : sb.toString().substring(1); // buang newline pertama kalo ada
   }

   // method rekursif untuk print semua kata
   private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
      if (root != null) {
         // kalo di sini ada kata yang berakhir, print ke stringbuilder
         if (root.isEnd) {
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
            sb.append("\n");
            for (int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }
<<<<<<< HEAD

         for (int i = 0; i < 26; ++i) {
            if (node.nodes[i] != null) {
               wordArray[pos] = (char) (i + 'a');
               printAllWords(node.nodes[i], wordArray, pos + 1, sb);
=======
         // lanjut ke semua child node dengan nambah karakter
         for(int i = 0; i < 26; ++i) {
            // kalo child node ada
            if (root.nodes[i] != null) {
               wordArray[pos] = (char)(i + 97); // convert index ke karakter
               this.printAllWords(root.nodes[i], wordArray, pos + 1, sb);  // rekursif ke child node dgn posisi +1
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
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
<<<<<<< HEAD
         Trie s = (Trie) o;
         if (this.getNodeCount() != s.getNodeCount()) return false;
         if (this.getWordCount() != s.getWordCount()) return false;
         return compareTrie(this, s);
=======
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
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
      }
      return false;
   }

<<<<<<< HEAD
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
=======
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
>>>>>>> aaec699fa5c246804e664dd8c5e6f6d85e3e7ed7
            return false;
         }
      }

      return true;
   }

   // main method buat testing
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
