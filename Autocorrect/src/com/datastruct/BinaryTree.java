package com.datastruct;
/*
 * Create Simple Binary Search Tree
 * 
 * @author: Lely Hiryanto
 * 
 */

public class BinaryTree<K, V>{

    public void printInOrder(BTNode<K,V> node) {    
        //T sebelumnya adalah child node            
        if(node == null) return;             
        else {
            //ke left node secara rekursif
            printInOrder(node.getLlink());    
            //cetak key dari node
            System.out.print(node.getKey() + ":" + node.getData() + " ");   
            //ke right node secara rekursif
            printInOrder(node.getRlink());     
        }
    }

    //post order traversal
    public void printPostOrder(BTNode<K,V> node) {
        //T sebelumnya adalah child node
        if(node == null) return;
        else {
            //ke left node secara rekursif
            printPostOrder(node.getLlink());
            //ke right node secara rekursif
            printPostOrder(node.getRlink());
            //cetak key dari node 
            System.out.print(node.getKey() + ":" + node.getData() + " ");
        }
    }  

    //pre order traversal
    void printPreOrder(BTNode<K,V> node) {
        //T sebelumnya adalah child node
        if(node == null) return;
        else {
            //cetak key dari node 
            System.out.print(node.getKey() + ":" + node.getData() + " ");
            //ke left node secara rekursif
            printPreOrder(node.getLlink());
            //ke right node secara rekursif
            printPreOrder(node.getRlink());
        }
    }  
    
    private void printLevelOrderRec(MyLinearList<BTNode<K,V>> q) {      
        if(q.isEmpty()) return;                                                 
        BTNode<K,V> node = q.remove();     
        //cetak key dari node 
        System.out.print(node.getKey() + ":" + node.getData() + " ");
        if(node.getLlink() != null) q.pushQ(node.getLlink());                                     
        if(node.getRlink() != null) q.pushQ(node.getRlink());
        printLevelOrderRec(q);
    }

    //level order traversal
    public void printLevelOrder(BTNode<K,V> node) {
        //buat queue untuk menampung node disetiap level
        MyLinearList<BTNode<K,V>> q = new MyLinearList<BTNode<K,V>>();  
        q.pushQ(node);  
        //memanggil fungsi rekursif untuk mencetak key 
        //dari node di setiap leve
        printLevelOrderRec(q);  
    }
}

