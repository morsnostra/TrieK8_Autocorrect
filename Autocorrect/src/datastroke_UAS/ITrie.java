// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// interface buat struktur data trie
// trie ini yang simpan semua kata di dictionary

public interface ITrie {
   // masukin kata baru ke dalam trie
   void add(String var1);

   // cari kata di trie, return nodenya kalo ketemu
   INode find(String var1);

   // hitung total kata unik yang ada di trie
   int getWordCount();

   // hitung total node yang ada di trie (termasuk node internal)
   int getNodeCount();

   // convert trie jadi string representation
   String toString();

   // generate hash code buat trie ini
   int hashCode();

   // bandingin apakah dua trie sama atau gak
   boolean equals(Object var1);
}
