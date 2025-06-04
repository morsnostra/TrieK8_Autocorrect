// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// implementasi dari INode, ini node actual yang dipake di trie
public class TrieNode implements INode {

   // array buat simpan 26 anak node (a-z)
   TrieNode[] nodes = new TrieNode[26];

   // counter berapa kali kata ini muncul di dictionary
   int count;

   // flag buat nandain apakah di node ini ada kata yang berakhir
   boolean isEnd;

   // constructor kosong, semua field udah auto-initialized
   public TrieNode() {
   }

   // return berapa kali kata ini muncul
   public int getValue() {
      return this.count;
   }

   // nambah counter kata (dipanggil tiap add kata yang sama)
   public void incrementValue() {
      ++this.count;
   }

   // return array anak-anak node buat navigasi trie
   public INode[] getChildren() {
      return this.nodes;
   }
}
