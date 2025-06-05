package com.datastruct;
/*
 * Generic Array List: Creating Our own version of Java's ArrayList
 * Generic Array List ini unchecked (weak typing) generic data type
 */

// Membuat class generik bernama MyArrayList, <T> artinya bisa simpan data tipe apa saja (seperti Integer, String, dll)
public class MyArrayList <T> {
    /*
     * Prinsip Encapsulation dari OOP:
     * 1. semua variabel dari class HARUS private
     *    (hanya dapat diakses oleh classnya sendiri)
     * 2. akses (set dan get) variabel melalui public method
     *    yang dibuat di class ini.
     */
    private Object[] thelist; // Menyimpan elemen-elemen list dalam bentuk array Object
    private int n; // Menyimpan jumlah elemen yang sedang ada di list
    private int max_size;  // Menyimpan kapasitas maksimal array
    
    // Constructor, fungsi yang dipanggil saat membuat objek MyArrayList
    public MyArrayList(int max_size) {
        thelist = new Object[max_size]; // Buat array dengan ukuran maksimal sesuai parameter
        n = 0; // Saat awal, list masih kosong
        this.max_size = max_size; //Simpan ukuran maksimum ke variabel class
    }

    // Getter untuk mengambil seluruh isi array (bentuk mentah)
    //get reference to thelist
    public Object[] getThelist() {
        return thelist;
    }
    //mengembalikan ukuran maksimum array
    public int maxSize() {
        return max_size;
    }
    //mengembalikan jumlah elemen array saat ini
    public int size() {
        return n;
    }
    //mengembalikan true jika array list sudah penuh
    private boolean isFull() {
        if(n == max_size) return true;  
        else return false; 
    }
    //mengembalikam true jika array list masih kosong
    public boolean isEmpty() {
        if(n == 0) return true;
        else return false;
    }
    //menambahkan data dengan ke posisi akhir di array list
    public void add(T value) {
        if(!isFull()) { // Tambah hanya kalau belum penuh
            thelist[n] = value; // Simpan data ke indeks ke-n
            n = n + 1; // Tambah jumlah elemen
        }
        else {
            System.out.println("List sudah penuh!");
        }
    }
    //menyisipkan data ke posisi index tertentu di array list
    public void add(int index, T value) {
        if(index >= 0 && !isFull()) { // Pastikan indeks valid dan list belum penuh
            n = n + 1; // Tambah jumlah elemen
            int i = n;
            do { // Geser elemen dari belakang ke kanan satu per satu
                thelist[i] = thelist[i-1]; 
                i = i - 1;
            }while(i > index);  // Ulang sampai sampai di posisi index yang dituju
            thelist[index] = value; // Simpan data ke posisi index
        }
        else {
            System.out.println("List sudah penuh!");
        }
    }

    public void remove(int index) {
        if(index >= 0 && !isEmpty()) {
            for(int i = index; i < n-1; i++) 
                thelist[i] = thelist[i+1];
            thelist[n-1] = null;
            n = n - 1;
        }
    }

    public T get(int i) {
        @SuppressWarnings("unchecked")
        final T e = (T) thelist[i];
        return e;
    }

    public void set(int index, T value) {
        thelist[index] = value;
    }

    public void clear() {
        if(!isEmpty()) {
            for(int i = 0; i < n; i++) 
                thelist[i] = null;    
            n = 0;
        }
    }

    public void cetakList() {
        //jika list kosong, tampilkan pesan list kosong
		if(isEmpty()) System.out.println("List kosong!");
        // jika list tidak kosong, maka cetak elemen pada list
		else {
            System.out.print("[ ");
            for(int i = 0; i < n; i++) {
				System.out.print(thelist[i].toString() + " ");
			}
            System.out.println("]");
		}
    }
}
