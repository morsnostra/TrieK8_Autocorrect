// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;


public interface INode {
    //buat dapat nilai / frekuensi kata pada node
   int getValue();

   //nambah nilai / frekuensi kata pada node
   void incrementValue();

   INode[] getChildren();
}
