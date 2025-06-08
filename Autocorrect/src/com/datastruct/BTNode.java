package com.datastruct;

public class BTNode<K,V> {                                                                       
    private K key;    //key ada bilangan bulat         
    private V data;   //object data dari sebuah class   
    private BTNode<K,V> llink; //left link             
    private BTNode<K,V> rlink; //right link         

    //constructor
    public BTNode(K k, V data) {   
        this.key = k;               
        this.data = data;           
        this.llink = null;          
        this.rlink = null;          
    }                        
      
    public void setKey(K key) {     
        this.key = key;            
    }
    public K getKey() {                        
        return key;                
    }
    public void setData(V data) {   
        this.data = data;           
    }
    public V getData() {           
        return data;               
    }
    public void setLlink(BTNode<K, V> llink) { 
    }
    public BTNode<K, V> getLlink() {            
        return llink;
    }
    public void setRlink(BTNode<K, V> rlink) {
        this.rlink = rlink;
    }
    public BTNode<K, V> getRlink() {
        return rlink;
    }
}
