// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;


public interface ITrie {
   void add(String var1);

   INode find(String var1);

   int getWordCount();

   int getNodeCount();

   String toString();

   int hashCode();

   boolean equals(Object var1);
}
