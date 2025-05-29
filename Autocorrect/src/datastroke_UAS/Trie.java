package datastroke_UAS;

public class Trie implements ITrie {

   // root node => semua kata akan dimulai dari sini
   private TrieNode root = new TrieNode();
   
   // Cache hash code untuk efisiensi - dihitung secara incremental
   private int myHashCode = 0;

   // constructor 
   public Trie() {
   }

   // implementasi add()
   public void add(String word) {
      TrieNode p = this.root;  // mulai dari root node
      
      // iterasi setiap karakter dalam kata
      for(char c : word.toCharArray()) {

         // konversi karakter ke index array (a=0, b=1, ..., z=25)
         // ASCII => 'a' = 97, jadi 'a' - 97 = 0, 'b' - 97 = 1, dst
         int index = c - 97;

         // jika child node belum ada, buat node baru
         if (p.nodes[index] == null) {
            p.nodes[index] = new TrieNode();
         }

         // pindah ke child node
         p = p.nodes[index];
      }

      // update hash code dengan menambahkan hash code dari kata baru
      // ini memungkinkan perhitungan hash code yang efisien tanpa rekalkulasi
      this.myHashCode += word.hashCode();
      
      // Increment frekuensi kata di node terakhir
      p.incrementValue();

      // Tandai bahwa node ini adalah akhir dari sebuah kata
      p.isEnd = true;
   }

   // Implementasi find() - mencari kata dalam trie
   public INode find(String word) {
      TrieNode p = this.root;

       // Traverse trie mengikuti path yang dibentuk oleh karakter-karakter kata
      for(char c : word.toCharArray()) {
         int index = c - 97; // Konversi ke index array

         // Jika path tidak ada, kata tidak ditemukan
         if (p.nodes[index] == null) {
            return null;
         }

         // Lanjut ke child node
         p = p.nodes[index];
      }

      // return node hanya jika ini adalah akhir kata yang valid
      // p != null selalu true di sini, tapi ditambahkan untuk safety
      return p != null && p.isEnd ? p : null;
   }

   // implementasi getNodeCount()
   public int getNodeCount() {
      return this.countNodesInTrie(this.root);
   }

   // method rekursif untuk menghitung node
   private int countNodesInTrie(TrieNode node) {
      if (node == null) {
         return 0; // Base case: node null tidak dihitung
      }
      
      int count = 0;

      // Rekursif hitung semua child nodes
      for(int i = 0; i < 26; ++i) {
         if (node.nodes[i] != null) {
            count += this.countNodesInTrie(node.nodes[i]);
         }
      }

      // Return 1 (node saat ini) + jumlah semua child nodes
      return 1 + count;
   }

   // Implementasi getWordCount() - menghitung jumlah kata unik
   public int getWordCount() {
      return this.wordCount(this.root);
   }

   // Helper method rekursif untuk menghitung kata
   private int wordCount(TrieNode root) {
      int result = 0;

      // Jika node ini adalah akhir kata, hitung sebagai 1 kata
      if (root.isEnd) {
         ++result;
      }

      // Rekursif hitung kata di semua child nodes
      for(int i = 0; i < 26; ++i) {
         if (root.nodes[i] != null) {
            result += this.wordCount(root.nodes[i]);
         }
      }

      return result;
   }

   // Implementasi toString() - mengkonversi trie menjadi string semua kata
   public String toString() {
      // Array untuk membangun kata saat traversal (max 50 karakter)
      char[] wordArray = new char[50];
      StringBuilder sb = new StringBuilder();

      // Panggil helper method untuk print semua kata
      this.printAllWords(this.root, wordArray, 0, sb);
      
       // Hapus newline pertama jika ada, atau return empty string
      return sb.toString().length() == 0 ? "" : sb.toString().substring(1);
   }

   // Helper method rekursif untuk print semua kata
   private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
      if (root != null) {
          // Jika ini akhir kata, tambahkan ke StringBuilder
         if (root.isEnd) {
            // Newline sebagai separator
            sb.append("\n");

            // Tambahkan karakter-karakter yang membentuk kata
            for(int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }

         // Rekursif traverse semua child nodes
         for(int i = 0; i < 26; ++i) {
            if (root.nodes[i] != null) {
               // Tambahkan karakter saat ini ke wordArray
               wordArray[pos] = (char)(i + 97);

               // Rekursif dengan posisi berikutnya
               this.printAllWords(root.nodes[i], wordArray, pos + 1, sb);
            }
         }
      }
   }

    // Implementasi hashCode() - return cached hash code
   public int hashCode() {
      return this.myHashCode;
   }

   // Implementasi equals() - membandingkan dua trie
   public boolean equals(Object o) {
       // Type checking dengan instanceof
      if (o instanceof Trie) {
         // Safe cast karena sudah dicek
         Trie s = (Trie)o;

         // Quick check: jika jumlah node berbeda, pasti tidak sama
         if (this.getNodeCount() != s.getNodeCount()) {
            return false;
         }

         // Quick check: jika jumlah kata berbeda, pasti tidak sama
         if (this.getWordCount() != s.getWordCount()) {
            return false;
         }

         // Deep comparison menggunakan helper method
         return this.compareTrie(this, s);
      }
      return false; // Bukan instance dari Trie
   }

    // Helper method untuk membandingkan dua trie secara detail
   private boolean compareTrie(Trie p, Trie q) {
      String s1 = p.toString();
      String s2 = q.toString();

      // Jika kedua trie kosong, mereka sama
      if (s1.equals("") && s2.equals("")) {
         return true;
      }
      
      // Split string menjadi array kata
      String[] strs1 = s1.split("\n");
      String[] strs2 = s2.split("\n");

      // Jika jumlah kata berbeda, tidak sama
      if (strs1.length != strs2.length) {
         return false;
      }
      
      // Periksa setiap kata dan frekuensinya
      for(String s : strs1) {
         INode node1 = p.find(s);
         INode node2 = q.find(s);

         // Jika frekuensi kata berbeda, tidak sama
         if (node1.getValue() != node2.getValue()) {
            return false;
         }
      }

      return true;
   }

   //main method untuk testing
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