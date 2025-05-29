// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// interface yang berisi "kontrak" untuk trienya sendiri

public interface ITrie {
   // method buat tambahin kata ke dalam trie
   // parameter => string kata yang akan ditambahkan
   void add(String word);
   
   // method buat cari kata dalam trie
   // parameter => string kata yang dicari
   // return INode jika kata ditemukan, null jika tidak
   INode find(String word);

   // method buat dapet jumlah kata yang unik dalam trie
   // return jumlah kata yang berbeda
   int getWordCount();

   // method buat dapet jumlah total node dalam trie
   // return jumlah semua node 
   int getNodeCount();

   // method buat konversi trie menjadi string 
   // return string yang berisi semua kata dalam trie
   String toString();

   // method buat dapet hash code dari trie
   // return hash code
   int hashCode();

   // method untuk membandingkan kesamaan dua trie
   // parameter => object yang akan dibandingkan
   // return true(sama)/false(beda)
   boolean equals(Object obj);

}
