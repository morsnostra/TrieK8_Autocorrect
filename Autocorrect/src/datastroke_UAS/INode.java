// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// interface buat representasi node di dalam trie
// setiap node bisa nyimpen berapa kali kata muncul (frekuensi) 
// dan bisa punya child node

public interface INode {
    // buat dapetin berapa kali kata ini muncul (frekuensi) di dictionary  
    int getValue();

    // buat nambah counter kata ini (dipanggil tiap ada kata yang sama)
    void incrementValue();

    // buat dapat array child node (untuk jalan ke karakter berikutnya)
    INode[] getChildren();
}
