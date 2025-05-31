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
   // cache untuk menyimpan hash code
   // dihitung secara incremental setiap ada kata baru
   // untuk efisiensi method hashCode()
   private int myHashCode = 0;

   // constructor 
   public Trie() {
   }

   /* constructor kosong
      field sudah diinisialisasi saat deklarasi

      setelah new Trie():
      root = TrieNode kosong
      myHashCode = 0 
    */

   // implementasi add()
   public void add(String word) {

      // buat pointer p yang menunjuk ke root
      // p akan "berjalan" mengikuti path kata
      // mulai dari root node
      TrieNode p = this.root;  
      
      // iterasi setiap karakter dalam kata 
      // word.toCharArray() ubah String jadi char array
      for(char c : word.toCharArray()) {

         // konversi karakter ke index array (a=0, b=1, ..., z=25)
         // ASCII => 'a' = 97, jadi 'a' - 97 = 0, 'b' - 97 = 1, dst
         int index = c - 97;

         // jika child node belum ada, buat node baru
         if (p.nodes[index] == null) {
            p.nodes[index] = new TrieNode();
         }

         // pointer pindah ke child node
         p = p.nodes[index];
      }

      // update hash code dengan menambahkan hash code dari kata baru
      this.myHashCode += word.hashCode();
      
      // increment frekuensi sebuah kata di node terakhir
      // tambah counter kata di node terakhir => utk handle duplicate words
      p.incrementValue();

      // node ini adalah akhir dari sebuah kata
      p.isEnd = true;
   }

   /* contoh add "cat"
    * 1. inisialisasi
         word = "cat"
         p = root
         myHashCode = 0

    * 2. karakter 'c'
         c = 'c' (ASCII 99)
         index = 99 - 97 = 2

         // cek p.nodes[2]
         if (root.nodes[2] == null) {       // true
            root.nodes[2] = new TrieNode(); // buat node baru
         }

         p = root.nodes[2];  // pindah ke node 'c'

         jadinya:
         root
         └── [2] (c) ← p ada di sini

    * 3. karakter 'a'
         c = 'a' (ASCII 97)  
         index = 97 - 97 = 0

         // Cek p.nodes[0] (p sekarang = node 'c')
         if (nodeC.nodes[0] == null) {       // true
            nodeC.nodes[0] = new TrieNode(); // buat node baru
         }
         p = nodeC.nodes[0]; // pindah ke node 'a'

         root
         └── [2] (c)
               └── [0] (a) ← p ada di sini

    * 4. karakter 't'
         c = 't' (ASCII 116)
         index = 116 - 97 = 19

         // Cek p.nodes[19] (p sekarang = node 'a')  
         if (nodeA.nodes[19] == null) {        // true
            nodeA.nodes[19] = new TrieNode(); // buat node baru
         }

         p = nodeA.nodes[19]; // pindah ke node 't'

         root
         └── [2] (c)
               └── [0] (a)
                  └── [19] (t) ← p ada di sini

    * 5. finalisasi
         myHashCode += "cat".hashCode();  // myHashCode = 0 + 98421 = 98421
         p.incrementValue();              // node 't' count = 1
         p.isEnd = true;                  // node 't' isEnd = true

    * contoh menambah kata kedua "car":
      existing tree:
      root
       └── [2] (c)
            └── [0] (a)
                 └── [19] (t) isEnd=true, count=1

    * 1. 'c' - node sudah ada, langsung pindah
      2. 'a' - node sudah ada, langsung pindah
      3. 'r' - node belum ada, buat baru     
      
      final tree
      root
       └── [2] (c)
            └── [0] (a)
                 ├── [17] (r) isEnd=true, count=1 ← "car"
                 └── [19] (t) isEnd=true, count=1 ← "cat"

    */

   // implementasi find() 
   public INode find(String word) {

      // mulai dari root
      TrieNode p = this.root;

      // traverse trie mengikuti path yang dibentuk oleh karakter-karakter kata
      for(char c : word.toCharArray()) {
         int index = c - 97; // konversi ke index array

         // jika path tidak ada, kata tidak ditemukan
         if (p.nodes[index] == null) {
            return null;
         }

         // lanjut ke child node
         p = p.nodes[index];
      }

      // return node hanya jika ini adalah akhir kata yang valid
      // p != null = node ditemukan
      // p.isEnd = node adalah akhir kata
      // p != null selalu true di sini, tapi ditambahkan untuk safety
      return p != null && p.isEnd ? p : null;
   }

   /* contoh find car
      
      existing tree:
      root
      └── [2] (c)
           └── [0] (a)
                ├── [17] (r) isEnd=true, count=1
                └── [19] (t) isEnd=true, count=1

    * 1. 'c'
         c = 'c', index = 2
         p.nodes[2] != null  // true, ada node
         p = root.nodes[2]   // pindah ke node 'c'     

    * 2. 'a'
         c = 'a', index = 0  
         p.nodes[0] != null  // true, ada node
         p = nodeC.nodes[0]  // pindah ke node 'a'

    * 3. 'r'
         c = 'r', index = 17
         p.nodes[17] != null  // true, ada node  
         p = nodeA.nodes[17]  // pindah ke node 'r'

    * 4. cek kondisi     
      p != null = true     // node 'r' ada
      p.isEnd = true       // node 'r' adalah akhir kata
      return p             // return node 'r'  

    * contoh find "ca" (bukan kata lengkap):
      step 1-2: sama seperti di atas
      step 3: cek kondisi

      p != null = true     // node 'a' ada
      p.isEnd = false      // node 'a' BUKAN akhir kata  
      return null          // return null
 

    * contoh Find "xyz" (tidak ada):
    * 1. 'x'
      c = 'x', index = 23
      p.nodes[23] == null  // true, tidak ada node
      return null          // langsung return null

    */

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

   /* contoh dgn tree:
      root (A)
      └── [2] c (B)
            └── [0] a (C)
               ├── [17] r (D) 
               └── [19] t (E)

      call stack:
    * 1. countNodesInTrie(A)
         node = A (root)
         count = 0
         loop i = 0 to 25:
            i = 2: node.nodes[2] != null (ada B)
                  count += countNodesInTrie(B) => step 2
                  count = 0 + 4 = 4
            i = 0,1,3-25: node.nodes[i] == null
         return 1 + 4 = 5

    * 2. countNodesInTrie(B)
         node = B (c)  
         count = 0
         loop i = 0 to 25:
            i = 0: node.nodes[0] != null (ada C)
                   count += countNodesInTrie(C) => step 3
                   count = 0 + 3 = 3
            i = 1-25: node.nodes[i] == null
         return 1 + 3 = 4

    * 3. countNodesInTrie(C)
         node = C (a)
         count = 0  
         loop i = 0 to 25:
         i = 17: node.nodes[17] != null (ada D)
                  count += countNodesInTrie(D)  → Call 4
                  count = 0 + 1 = 1
         i = 19: node.nodes[19] != null (ada E)
                  count += countNodesInTrie(E)  → Call 5  
                  count = 1 + 1 = 2
         i = lainnya: node.nodes[i] == null
         return 1 + 2 = 3

    * 4. countNodesInTrie(D)
         node = D (r)
         count = 0
         loop i = 0 to 25:
         semua node.nodes[i] == null (leaf node)
         return 1 + 0 = 1

    * 5. countNodesInTrie(E)
         node = E (t)
         count = 0
         loop i = 0 to 25:
         semua node.nodes[i] == null (leaf node)  
         return 1 + 0 = 1

    * hasil Akhir: 5 nodes (root, c, a, r, t)

    */

   // implementasi getWordCount() 
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

   /* contoh tree:
      root
       └── c
           └── a
               ├── r (isEnd=true) ← kata "car"
               │   └── d (isEnd=true) ← kata "card"
               └── t (isEnd=true) ← kata "cat"

    * 1. wordCount(root):

         root.isEnd = false   => result = 0
         Ada child 'c'        => result += wordCount(nodeC)

    * 2. wordCount(nodeC):

         nodeC.isEnd = false  => result = 0
         Ada child 'a'        => result += wordCount(nodeA)

    * 3. wordCount(nodeA):

      nodeA.isEnd = false     => result = 0
      Ada child 'r'           => result += wordCount(nodeR) => result = 0 + 2 = 2
      Ada child 't'           => result += wordCount(nodeT) => result = 2 + 1 = 3
      return 3

    * 4. wordCount(nodeR):

      nodeR.isEnd = true      => result = 1 (kata "car")
      Ada child 'd'           => result += wordCount(nodeD) => result = 1 + 1 = 2
      return 2

    * 5. wordCount(nodeD):

      nodeD.isEnd = true      => result = 1 (kata "card")
      Tidak ada child         => return 1

    * 6. wordCount(nodeT):

      nodeT.isEnd = true      => result = 1 (kata "cat")
      Tidak ada child         => return 1

      hasil Akhir: 3 kata (car, card, cat)

    */
 
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