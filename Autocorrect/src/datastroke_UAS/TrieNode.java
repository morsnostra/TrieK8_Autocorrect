// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

public class TrieNode implements INode {
   // array untuk menyimpan 26 child nodes (a-z)
   // Index 0 = 'a', index 1 = 'b', dst
   TrieNode[] nodes = new TrieNode[26];
   
   /* contoh
    * TrieNode node = new TrieNode();
    * Buat child untuk huruf 'c' (index 2)
    * node.nodes[2] = new TrieNode();
    * node.nodes[2] sekarang berisi TrieNode baru
    * node.nodes[0,1,3-25] masih null
    */

   // menyimpan frekuensi berapa kali kata yang berakhir di node ini
   // default value = 0
   int count;

   /* contoh
    * kata "cat" dimasukkan 3 kali
    * add("cat");  // count di node 't' = 1
    * add("cat");  // count di node 't' = 2  
    * add("cat");  // count di node 't' = 3 
    */

   // menandai apakah node ini adalah akhir dari sebuah kata
   // default = false
   boolean isEnd;

   /* contoh 
    * Root
      └── c
          └── a
              └── r (isEnd = true, count = 1) ← kata "car" berakhir di sini
                  └── d (isEnd = true, count = 1) ← kata "card" berakhir di sini
    */

   // constructor 
   public TrieNode() {
   }

   /* constructor kosong (default constructor)
      semua field diinisialisasi dengan nilai default:
      nodes = array 26 slot berisi null
      count = 0
      isEnd = false 

      contoh
      TrieNode node = new TrieNode();
      node.nodes = [null, null, null, ..., null] (26 nulls)
      node.count = 0
      node.isEnd = false
    */

   // implementasi getValue() => method getter untuk mengambil nilai count
   public int getValue() {
      return this.count; // count dari object ini
   }

   /* contoh
      TrieNode node = new TrieNode();
      node.count = 5;
      int frequency = node.getValue();  // frequency = 5
      System.out.println(frequency);    // Output: 5
    */
    
   // implementasi incrementValue() => method untuk menambah count sebesar 1
   public void incrementValue() {
      ++this.count; // pre-increment => tambah dulu, baru pakai
   }

   /* contoh
      TrieNode node = new TrieNode();
      System.out.println(node.count);  // Output: 0

      node.incrementValue();
      System.out.println(node.count);  // Output: 1

      node.incrementValue();  
      System.out.println(node.count);  // Output: 2

      node.incrementValue();
      System.out.println(node.count);  // Output: 3
    */

   // implementasi getChildren() => method getter untuk mengambil array children
   // return type INode[] (bukan TrieNode[])
   // casting otomatis karena TrieNode implements INode
   public INode[] getChildren() {
      return this.nodes;
   }

   /* contoh
      TrieNode parent = new TrieNode();
      parent.nodes[2] = new TrieNode();  // child untuk 'c'
      parent.nodes[0] = new TrieNode();  // child untuk 'a'

      INode[] children = parent.getChildren();
      // children[0] = node untuk 'a'
      // children[1] = null
      // children[2] = node untuk 'c'
      // children[3-25] = null
    */
}
