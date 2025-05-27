// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

public class TrieNode implements INode {
   TrieNode[] nodes = new TrieNode[26];
   int count;
   boolean isEnd;

   public TrieNode() {
   }

   public int getValue() {
      return this.count;
   }

   public void incrementValue() {
      ++this.count;
   }

   public INode[] getChildren() {
      return this.nodes;
   }
}
