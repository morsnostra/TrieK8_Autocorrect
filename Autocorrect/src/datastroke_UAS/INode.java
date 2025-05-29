// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// interface yang berisi "kontrak" untuk node yang ada dalam trie

public interface INode {

   // method buat dapat nilai / frekuensi kata pada node
   // return berapa kali sebuah kata muncul
   int getValue();

   // method buat nambah nilai / frekuensi kata pada node
   // digunakan ketika kata yang sama ditambahkan lagi ke dalam trie
   void incrementValue();

   // method buat dapat array yang berisi children dari node 
   // return array INode yang berisi semua child nodes
   // akan berisi a-z
   INode[] getChildren();
}
