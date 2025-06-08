package com.datastruct;

public class DoubleHashing<K, V> extends Hashing<K, V> {
    public DoubleHashing(int capacity) {
        super(capacity);
    }
    // Secondary hash function
    private int hash2(K key) {
        return 1 + (convertToNumber(key) % 7); // h2(key) = 1 + (h(key) mod 7)
    }
    @Override
    public void put (K key, V value) {
        HashNode<K,V> newNode= new HashNode<K,V> (key,value);
        int hash = convertToNumber(key) % table.maxSize();
        int stepSize = hash2(key); 
        int i = 0;
        int newIndex = hash;

        while (i < table.maxSize()) {
            if (table.get(newIndex) == null) {
                table.set(newIndex, newNode);
                incSize();
                return;
            }
            //Kondisi bila key sudah ada, maka update valuenya
            HashNode<K,V> existingNode = table.get(newIndex);
            if(existingNode.key.equals(key)){
                table.set(newIndex, newNode);
                return;
            }

            i++;
            newIndex= (hash + i * stepSize) % table.maxSize();
        }

        System.out.println("Penuh, gabisa diisi lagi" + key);
        }

        public V get(K key) {
            int hash = convertToNumber(key) % table.maxSize();
            int stepSize =hash2(key); //hitung step
            int i =0;
            int newIndex =hash;

            while(table.get(newIndex) != null && i < table.maxSize()) {
                HashNode <K,V> node = table.get(newIndex);
                if(node != null && node.key.equals(key)){
                    return node.data;
                }
                i++;
                newIndex = (hash + i * stepSize) % table.maxSize();
            }
            return null;
            
        }
        
    }

   
