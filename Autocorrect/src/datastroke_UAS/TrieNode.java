// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

public class TrieNode implements INode {
   // Array untuk menyimpan 26 child nodes (a-z)
   // Index 0 = 'a', index 1 = 'b', dst
   TrieNode[] nodes = new TrieNode[26];
   
   // Counter untuk menyimpan frekuensi kata yang berakhir di node ini
   int count;

    // Flag boolean untuk menandai apakah node ini adalah akhir dari sebuah kata
   boolean isEnd;

   // Constructor default - tidak perlu inisialisasi khusus
   public TrieNode() {
   }

   // Implementasi getValue() - mengembalikan frekuensi kata
   public int getValue() {
      return this.count;
   }

   // Implementasi incrementValue() - menambah frekuensi kata sebanyak 1
   public void incrementValue() {
      ++this.count; // Prefix increment lebih efisien untuk primitive types
   }

   // Implementasi getChildren() - mengembalikan array child nodes
   // Cast dari TrieNode[] ke INode[] karena TrieNode implements INode
   public INode[] getChildren() {
      return this.nodes;
   }
}
