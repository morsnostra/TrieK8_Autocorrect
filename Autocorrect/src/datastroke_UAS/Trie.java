package datastroke_UAS;

public class Trie implements ITrie {

   // root node => semua kata akan dimulai dari sini
   private TrieNode root = new TrieNode();
   /* contoh
      root (TrieNode)
      ├── nodes[0] = null (untuk 'a')
      ├── nodes[1] = null (untuk 'b')  
      ├── nodes[2] = null (untuk 'c')
      ├── ...
      └── nodes[25] = null (untuk 'z')
    */
    
   // insialisasi
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
      this.myHashCode += word.hashCode();
      
      // increment frekuensi sebuah kata di node terakhir
      p.incrementValue();

      // node ini adalah akhir dari sebuah kata
      p.isEnd = true;
   }

   // implementasi find() 
   public INode find(String word) {
      TrieNode p = this.root;

       // traverse trie mengikuti path yang dibentuk oleh karakter-karakter kata
      for(char c : word.toCharArray()) {
         int index = c - 97; // lonversi ke index array

         // jika path tidak ada, kata tidak ditemukan
         if (p.nodes[index] == null) {
            return null;
         }

         // lanjut ke child node
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
         return 0; // base case: node null tidak dihitung
      }
      
      int count = 0;

      // rekursif hitung semua child nodes
      for(int i = 0; i < 26; ++i) {
         if (node.nodes[i] != null) {
            count += this.countNodesInTrie(node.nodes[i]);
         }
      }

      // return 1 (node saat ini) + jumlah semua child nodes
      return 1 + count;
   }

   // implementasi getWordCount() - menghitung jumlah kata unik
   public int getWordCount() {
      return this.wordCount(this.root);
   }

   // helper method rekursif untuk menghitung kata
   private int wordCount(TrieNode root) {
      int result = 0;

      // jika node ini adalah akhir kata, hitung sebagai 1 kata
      if (root.isEnd) {
         ++result;
      }

      // rekursif hitung kata di semua child nodes
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

   // helper method rekursif untuk print semua kata
   private void printAllWords(TrieNode root, char[] wordArray, int pos, StringBuilder sb) {
      if (root != null) {
          // jika ini akhir kata, tambahkan ke StringBuilder
         if (root.isEnd) {
            // nwwline sebagai separator
            sb.append("\n");

            // tambahkan karakter-karakter yang membentuk kata
            for(int i = 0; i < pos; ++i) {
               sb.append(wordArray[i]);
            }
         }

         // rekursif traverse semua child nodes
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

    // implementasi hashCode() - return cached hash code
   public int hashCode() {
      return this.myHashCode;
   }

   // implementasi equals() - membandingkan dua trie
   public boolean equals(Object o) {
       // type checking dengan instanceof
      if (o instanceof Trie) {
         // safe cast karena sudah dicek
         Trie s = (Trie)o;

         // quick check: jika jumlah node berbeda, pasti tidak sama
         if (this.getNodeCount() != s.getNodeCount()) {
            return false;
         }

         // quick check: jika jumlah kata berbeda, pasti tidak sama
         if (this.getWordCount() != s.getWordCount()) {
            return false;
         }

         // deep comparison menggunakan helper method
         return this.compareTrie(this, s);
      }
      return false; // bukan instance dari Trie
   }

    // helper method untuk membandingkan dua trie secara detail
   private boolean compareTrie(Trie p, Trie q) {
      String s1 = p.toString();
      String s2 = q.toString();

      // jika kedua trie kosong, mereka sama
      if (s1.equals("") && s2.equals("")) {
         return true;
      }
      
      // split string menjadi array kata
      String[] strs1 = s1.split("\n");
      String[] strs2 = s2.split("\n");

      // jika jumlah kata berbeda, tidak sama
      if (strs1.length != strs2.length) {
         return false;
      }
      
      // periksa setiap kata dan frekuensinya
      for(String s : strs1) {
         INode node1 = p.find(s);
         INode node2 = q.find(s);

         // jika frekuensi kata berbeda, tidak sama
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