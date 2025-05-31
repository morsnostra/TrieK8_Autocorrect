package datastroke_UAS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
   
   // trie ini akan menyimpan semua kata dari dictionary untuk pencarian yang efisien
   private Trie trie = new Trie();

   /* contoh add kata "hello" ke trie:
      trie.add("hello");
      trie akan membuat struktur seperti:
      root -> h -> e -> l -> l -> o (dengan flag isEnd = true di node 'o')
   */

   // Map<String, Integer>: Generic type yang menyatakan Map dengan key bertipe String dan value bertipe Integer
   // new HashMap<>(): Membuat instance HashMap baru
   // menyimpan setiap kata dan frekuensi kemunculannya
   private Map<String, Integer> dict = new HashMap<>();
   
   // List<String>: List yang berisi String
   // Arrays.asList(): Method utility untuk membuat List dari array
   // menyimpan kata-kata yang dianggap tidak valid untuk spell correction
   private static final List<String> invalid = Arrays.asList("lol", "abcdefghijklmnopqrstuvwxyz");

   // constructor default yang kosong
   // karena instance variables sudah diinisialisasi saat deklarasi, constructor ini tidak perlu melakukan apa-apa
   // ketika new SpellCorrector() dipanggil, trie dan dict sudah siap digunakan
   public SpellCorrector() {
   }

   public void useDictionary(String dictionaryFileName) throws IOException {
      try { // memulai block untuk exception handling
         
         // FileReader untuk membaca file character by character
         FileReader fr = new FileReader(dictionaryFileName);
         
         // BufferedReader untuk efisiensi => membaca dalam chunks, bukan per karakter
         BufferedReader br = new BufferedReader(fr);
         
         // mendeklarasikan variable untuk menyimpan setiap baris yang dibaca dari file, diinisialisasi dengan null
         String line = null;

         // baca file line by line sampai EOF
         while((line = br.readLine()) != null) {
            // mengonversi semua karakter dalam line menjadi huruf kecil
            String word = line.toLowerCase();

            // jika line tidak mengandung spasi, treat sebagai satu kata
            if (!line.contains(" ")) {
               this.dict.put(word, this.dict.getOrDefault(word, 0) + 1);
               /* this.dict: Mengakses instance variable dict
                * getOrDefault(word, 0)
                  jika word sudah ada di dict, return value-nya
                  jika word belum ada, return 0 (default value)
                * + 1: Increment frekuensi
                * put(word, ...) => menyimpan atau update kata dengan frekuensi baru 
                */

                /* contoh
                 * pertama kali "hello" dibaca:
                 
                   dict.getOrDefault("hello", 0) -> 0 (karena belum ada)
                   dict.put("hello", 0 + 1) -> dict = {"hello": 1}

                 * kedua kali "hello" dibaca:
                 
                   dict.getOrDefault("hello", 0) -> 1 (karena sudah ada)  
                   dict.put("hello", 1 + 1) -> dict = {"hello": 2}

                 */
               
               // menambahkan kata ke trie structure
               // trie akan membuat path dari root ke leaf untuk setiap karakter dalam kata
               this.trie.add(word);

            // jika line ada spasi    
            } else {
               String[] strs = line.split("\\s");
               /* line.split("\\s"):
                * split()   => method untuk memecah string berdasarkan delimiter
                * "\\s"     => regular expression (regex) untuk whitespace characters (spasi, tab, newline)
                * \\        => escape character untuk regex dalam Java string 
                */

               // iterasi semua string dalam array strs
               for(String str : strs) {
                  // mengonversi setiap kata menjadi lowercase untuk konsistensi
                  String cleanStr = str.toLowerCase();
                  // sama seperti penjelasan sebelumnya, tapi dilakukan untuk setiap kata dalam baris
                  this.dict.put(cleanStr, this.dict.getOrDefault(cleanStr, 0) + 1);
                  this.trie.add(cleanStr);
               }
            }
         }
         
         /* contoh 
          * strs = ["Hello", "WORLD", "hello"]
          * iterasi 1 => cleanStr = "hello", dict = {"hello": 1}, trie.add("hello")
          * iterasi 2 => cleanStr = "world", dict = {"hello": 1, "world": 1}, trie.add("world")  
          * iterasi 3 => cleanStr = "hello", dict = {"hello": 2, "world": 1}, trie.add("hello")
          */

         fr.close();
         br.close();

      } catch (FileNotFoundException ex) {
         System.err.println("File not found: " + ex.getMessage());
         throw ex;

         /* menangkap FileNotFoundException yang terjadi jika file tidak ditemukan
          * System.err.println(): Mencetak error message ke error stream (biasanya console)
          * ex.getMessage(): Mendapatkan detail message dari exception
          * throw ex: Melempar kembali exception ke caller
          */
        
        // menangkap IOException untuk error I/O lainnya (permission denied, disk full, dll)
      } catch (IOException ex) {
         System.err.println("IO Exception: " + ex.getMessage());
         throw ex;
      }
   }

   // method utama untuk mendapatkan saran kata yang mirip
   // menerima inputWord dan mengembalikan String (kata yang disarankan atau null)
   public String suggestSimilarWord(String inputWord) {
      
    // validasi input: null, empty, atau kata invalid
    // inputWord == null: mengecek null pointer
    // inputWord.length() == 0: mengecek empty string
    // invalid.contains(inputWord.toLowerCase()): mengecek apakah kata ada dalam daftar invalid
    // jika salah satu kondisi true, return null (tidak ada saran)
      if (inputWord == null || inputWord.length() == 0 || invalid.contains(inputWord.toLowerCase())) {
         return null;
      }

      // inisialisasi variabel

      // input word dalam lowercase
      String s = inputWord.toLowerCase();

      // variable untuk menyimpan hasil akhir
      String res = null;

      // Outer TreeMap  : Key = edit distance (int),    Value = TreeMap
      // Middle TreeMap : Key = frequency (int),        Value = TreeSet
      // Inner TreeSet  : Berisi kata-kata yang terurut alfabetis
      TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
      
      /*contoh struktur map
        map = {
            1: {  // edit distance = 1
                5: {"hello", "world"},     // frequency = 5
                3: {"house", "mouse"}      // frequency = 3
            },
            
            2: {  // edit distance = 2
                10: {"computer"},          // frequency = 10
                2: {"laptop", "tablet"}    // frequency = 2
            }
        }   

       */
      
      // mencari kata s dalam trie
      // jika ditemukan, return TrieNode; jika tidak, return null
      // this merujuk ke instance SpellCorrector saat ini
      // trie adalah field private bertipe Trie
      // find(s) method dari Trie untuk mencari kata dalam dictionary
      INode node = this.trie.find(s);
      
      // find() return null
      if (node == null) {
         // Word not found in dictionary, find similar words
         
         // Iterator untuk Semua Kata di Dictionary

         // keySet() adalah Method dari Map interface, Mengembalikan Set berisi semua key dari Map
         // Dalam kasus ini, semua kata yang ada di dictionary
         // dict.keySet(): Mendapatkan Set berisi semua keys (kata-kata) dari dictionary

         Iterator<String> iterator = this.dict.keySet().iterator();
         
         // iterator(): Membuat iterator untuk traverse semua kata
         // Iterator pattern memungkinkan traversal yang memory-efficient
         // hasNext():  mengecek apakah masih ada element berikutnya
         while(iterator.hasNext()) {
        
         /* contoh
            Map<String, Integer> dict = new HashMap<>();
            dict.put("hello", 5);
            dict.put("world", 3);

            Iterator<String> iter = dict.keySet().iterator();
            while(iter.hasNext()) {
                String word = iter.next();
                System.out.println(word); // print "hello", kemudian "world"
            }          

          */

            // MENGAMBIL KATA BERIKUTNYA
            // iterator: Reference ke Iterator<String> yang dibuat dari dict.keySet().iterator()
            // .next(): Method dari Iterator interface yang Mendapatkan element berikutnya dan memajukan iterator
            // Mengembalikan element berikutnya dalam iteration
            // Memajukan internal pointer iterator ke element selanjutnya
            // Throw NoSuchElementException jika tidak ada element berikutnya
            // String w: Variable untuk menyimpan kata yang sedang diproses
            String w = iterator.next(); 
        
            /* contoh
             * Dictionary keys: ["hello", "world", "help", "held"]
             * Iterasi 1: w = "hello"
             * Iterasi 2: w = "world"  
             * Iterasi 3: w = "help"
             * Iterasi 4: w = "held"
             */
            

            // MENGHITUNG EDIT DISTANCE antara kata dari dictionary (w) dengan input word (s)
            // edit distance = minimum operasi (insert, delete, substitute) untuk mengubah satu kata menjadi kata lain
            // this.editDistance(): Memanggil instance method editDistance pada object yang sama
            // parameter w: Kata dari dictionary yang sedang diproses, parameter s: Input word yang sudah di-lowercase
            // return value: Integer representing minimum edit operations
            int dist = this.editDistance(w, s);

            /* contoh
               editDistance("cat", "bat") = 1 (substitute 'c' dengan 'b')
               editDistance("cat", "cats") = 1 (insert 's')  
               editDistance("cat", "at") = 1 (delete 'c')
             
             * contoh lagi
               editDistance("hello", "helo");  // dist = 1
               editDistance("world", "word");  // dist = 1  
               editDistance("cat", "dog");     // dist = 3

             * lagi
               s = "helo" (input word)
               w = "hello" -> dist = editDistance("hello", "helo") = 1 (insert 'l')
               w = "world" -> dist = editDistance("world", "helo") = 4 (multiple operations)
               w = "help"  -> dist = editDistance("help", "helo") = 2 (substitute 'p'->s', insert 'o')
               w = "held"  -> dist = editDistance("held", "helo") = 2 (substitute 'd'->'o')

             */
    

            // MENDAPATKAN/MEMBUAT TREEMAP UTK EDIT DISTANCE DIST
            // jika belum ada, buat TreeMap baru
            // treeMap ini akan memetakan frequency -> Set of words

            // map.getOrDefault(dist, new TreeMap<>())
            // map: Reference ke TreeMap<Integer, TreeMap<Integer, TreeSet<String>>>
            // .getOrDefault(): Method dari Map interface dengan signature:
            // V getOrDefault(Object key, V defaultValue)
            // jika key exist, return value yang sesuai
            // jika key tidak exist, return defaultValue

            // parameter 1 dist: Key yang dicari (edit distance)
            // parameter 2 new TreeMap<>(): Default value jika key tidak ditemukan
            // jika dist sudah ada di map: Return TreeMap yang sudah ada
            // jika dist belum ada di map: Return TreeMap baru yang kosong
            TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
            
            /* contoh
            Map<String, Integer> scores = new HashMap<>();
            scores.put("Alice", 90);

            int aliceScore = scores.getOrDefault("Alice", 0);  // return 90
            int bobScore = scores.getOrDefault("Bob", 0);      // return 0 (default)
            
            dalam konteks:
            Jika edit distance dist sudah ada di map, ambil TreeMap yang sudah ada
            Jika belum ada, buat TreeMap baru

             */

            /* contoh lagi
             * initial state: map = {}

             * iterasi 1: w="hello", dist=1
            
            map.getOrDefault(1, new TreeMap<>()) 
            -> Key 1 belum ada, return new TreeMap<>()
            similarWords = {} (empty TreeMap)


             * iterasi 2: w="help", dist=2  
             
            map.getOrDefault(2, new TreeMap<>())
            -> Key 2 belum ada, return new TreeMap<>()
            similarWords = {} (empty TreeMap)


             * iterasi 3: w="held", dist=2
             
            map.getOrDefault(2, new TreeMap<>())
            -> Key 2 sudah ada dari iterasi sebelumnya
            similarWords = {existing TreeMap for distance 2}

            */

            // MENDAPATKAN FREKUENSI KATA W DARI DICTIONARY
            // dict adalah Map<String, Integer> yang menyimpan kata dan frekuensi
            // this.dict: reference ke HashMap<String, Integer> instance variable
            //.get(w)   : method dari Map interface untuk mendapatkan value berdasarkan key
            // get(w) mengambil frekuensi dari kata w

            // asumsi: kata w pasti ada di dict karena iterator dibuat dari dict.keySet()
            // return: integer representing frekuensi kemunculan kata dalam dictionary file
            // frekuensi ini dihitung saat membaca dictionary file
            int freq = this.dict.get(w);
            
            /* contoh
             * Assumsi dict = {"hello": 3, "world": 1, "help": 2, "held": 1}
             * w = "hello" -> freq = 3
             * w = "world" -> freq = 1  
             * w = "help"  -> freq = 2
             * w = "held"  -> freq = 1
             */
           
            // MEMBANGUN TREESET UNTUK KATA DENGAN FREQUENCY YANG SAMA
            // Mendapatkan TreeSet untuk frequency freq
            // TreeSet secara otomatis mengurutkan string secara alfabetis
            
            // similarWords.getOrDefault(freq, new TreeSet<>())
            // similarWords     : TreeMap<Integer, TreeSet<String>> dari baris sebelumnya
            // .getOrDefault()  : Method yang sama seperti sebelumnya
            // Parameter 1 freq : Key yang dicari (frequency value)
            // Parameter 2 new TreeSet<>()  : Default TreeSet kosong jika key tidak ditemukan
            TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
            
            // TreeSet:
            // Implementasi Set yang otomatis mengurutkan elemen
            // Untuk String, diurutkan secara alfabetis
            // Tidak allow duplikat

            /* contoh
            similarWords untuk dist=2: {2: ["help"], 1: ["held"]}

            Ketika w="help", freq=2:
            similarWords.getOrDefault(2, new TreeSet<>())
            -> Key 2 sudah ada, return ["help"]
            set = ["help"]

            Ketika w="held", freq=1:  
            similarWords.getOrDefault(1, new TreeSet<>())
            -> Key 1 sudah ada, return ["held"]
            set = ["held"]

            Ketika w="hello", freq=3 (misalnya pada dist=1):
            similarWords.getOrDefault(3, new TreeSet<>())
            -> Key 3 belum ada, return new TreeSet<>()
            set = {} (empty TreeSet)

             */

            // MENAMBAHKAN KATA W KE SET
            // set: TreeSet<String> reference dari baris sebelumnya
            // .add(w): Method dari Set interface untuk menambahkan element
            // Jika w belum ada, tambahkan dan maintain sorted order
            // Jika w sudah ada, tidak ada perubahan (Set property)
            set.add(w);

            /*  TreeSet<String> words = new TreeSet<>();
                words.add("zebra");
                words.add("apple");  
                words.add("banana");
                System.out.println(words); // [apple, banana, zebra] - otomatis terurut

             * 1. Cari TreeSet untuk frequency tertentu, atau buat baru jika belum ada
             * 2. Tambahkan kata w ke TreeSet
             * 3. Simpan kembali TreeSet ke similarWords

             */

             /* cth lagi
              * set = ["help"] (existing)
                set.add("help") -> set tetap ["help"], return false
                
                set = [] (empty)  
                set.add("hello") -> set menjadi ["hello"], return true

              * set = ["apple"]
                set.add("banana") -> set menjadi ["apple", "banana"], return true
                set.add("ant") -> set menjadi ["ant", "apple", "banana"], return true
              */

            // MENYIMPAN KE MAP UTAMA
            // Update map dengan struktur yang sudah dimodifikasi
            // Simpan TreeMap similarWords ke map utama dengan key dist
            // Jika dist sudah ada, akan overwrite (tapi isinya sudah di-update sebelumnya)
            
            // similarWords: TreeMap<Integer, TreeSet<String>> reference
            // .put(freq, set): Method dari Map interface untuk insert/update entry
            // Parameter 1 freq: Key (frequency value)
            // Parameter 2 set: Value (TreeSet yang sudah dimodifikasi)

            // Jika key sudah ada: replace old value dengan new value
            // Jika key belum ada: insert new entry
            // Karena set adalah reference ke TreeSet yang sama, 
            // operasi ini sebenarnya "menyimpan" perubahan yang sudah dilakukan pada set.add(w).
            similarWords.put(freq, set);

            // map: TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> main structure
            // .put(dist, similarWords): Update main map dengan TreeMap yang sudah dimodifikasi
            // Parameter 1 dist: Key (edit distance value)
            // Parameter 2 similarWords: Value (TreeMap yang sudah dimodifikasi)
            map.put(dist, similarWords);
            
         }
         
        // Input: "ct", Dictionary: {"cat":2, "bat":1, "rat":3, "cut":1}
    
        // iterasi 1: w="cat", dist=1, freq=2
        // map = {1: {2: ["cat"]}}
    
        // Iterasi 2: w="bat", dist=2, freq=1  
        // map = {1: {2: ["cat"]}, 2: {1: ["bat"]}}
        
        // Iterasi 3: w="rat", dist=2, freq=3
        // map = {1: {2: ["cat"]}, 2: {1: ["bat"], 3: ["rat"]}}
    
        // Iterasi 4: w="cut", dist=1, freq=1
        // map = {1: {2: ["cat"], 1: ["cut"]}, 2: {1: ["bat"], 3: ["rat"]}}

// contoh 2

        // Input: s = "helo"
        // Dictionary: {"hello": 3, "world": 1, "help": 2, "held": 1, "helm": 1}

        // 1. w = "hello"
        // 2. dist = editDistance("hello", "helo") = 1 (insert 'l')
        // 3. similarWords = map.getOrDefault(1, new TreeMap<>()) = {} (new)
        // 4. freq = dict.get("hello") = 3
        // 5. set = similarWords.getOrDefault(3, new TreeSet<>()) = {} (new)
        // 6. set.add("hello") -> set = ["hello"]
        // 7. similarWords.put(3, ["hello"]) -> similarWords = {3: ["hello"]}
        // 8. map.put(1, {3: ["hello"]}) -> map = {1: {3: ["hello"]}}

/* State setelah iterasi 1:

map = {
  1: {                    // edit distance = 1
    3: ["hello"]         // frequency = 3, words = ["hello"]
  }
}

 */

        // Iterasi 2: w = "world"
        // 1. w = "world"  
        // 2. dist = editDistance("world", "helo") = 4
        // 3. similarWords = map.getOrDefault(4, new TreeMap<>()) = {} (new)
        // 4. freq = dict.get("world") = 1
        // 5. set = similarWords.getOrDefault(1, new TreeSet<>()) = {} (new)
        // 6. set.add("world") -> set = ["world"]
        // 7. similarWords.put(1, ["world"]) -> similarWords = {1: ["world"]}
        // 8. map.put(4, {1: ["world"]})

/* State setelah iterasi 2:

map = {
  1: {                    // edit distance = 1
    3: ["hello"]         
  },
  4: {                    // edit distance = 4
    1: ["world"]         // frequency = 1, words = ["world"]
  }
}

 */

        // Iterasi 3: w = "help"
        // 1. w = "help"
        // 2. dist = editDistance("help", "helo") = 2 (substitute 'p'->'o')  
        // 3. similarWords = map.getOrDefault(2, new TreeMap<>()) = {} (new)
        // 4. freq = dict.get("help") = 2
        // 5. set = similarWords.getOrDefault(2, new TreeSet<>()) = {} (new)
        // 6. set.add("help") -> set = ["help"]
        // 7. similarWords.put(2, ["help"]) -> similarWords = {2: ["help"]}
        // 8. map.put(2, {2: ["help"]})

/* State setelah iterasi 3:

map = {
  1: {                    // edit distance = 1  
    3: ["hello"]         
  },
  2: {                    // edit distance = 2
    2: ["help"]          // frequency = 2, words = ["help"]
  },
  4: {                    // edit distance = 4
    1: ["world"]         
  }
}

 */
        
        // Iterasi 4: w = "held"
        // 1. w = "held"
        // 2. dist = editDistance("held", "helo") = 2 (substitute 'd'->'o')
        // 3. similarWords = map.getOrDefault(2, existing TreeMap) = {2: ["help"]}
        // 4. freq = dict.get("held") = 1  
        // 5. set = similarWords.getOrDefault(1, new TreeSet<>()) = {} (new)
        // 6. set.add("held") -> set = ["held"]
        // 7. similarWords.put(1, ["held"]) -> similarWords = {2: ["help"], 1: ["held"]}
        // 8. map.put(2, {2: ["help"], 1: ["held"]}) -> update existing

/* State setelah iterasi 4:

map = {
  1: {                    // edit distance = 1
    3: ["hello"]         
  },
  2: {                    // edit distance = 2
    2: ["help"],         // frequency = 2, words = ["help"]  
    1: ["held"]          // frequency = 1, words = ["held"]
  },
  4: {                    // edit distance = 4
    1: ["world"]         
  }
}

 */

        // Iterasi 5: w = "helm"
        // 1. w = "helm"
        // 2. dist = editDistance("helm", "helo") = 2 (substitute 'm'->'o')
        // 3. similarWords = map.getOrDefault(2, existing TreeMap) = {2: ["help"], 1: ["held"]}
        // 4. freq = dict.get("helm") = 1
        // 5. set = similarWords.getOrDefault(1, existing TreeSet) = ["held"]
        // 6. set.add("helm") -> set = ["held", "helm"] (sorted alphabetically)
        // 7. similarWords.put(1, ["held", "helm"]) -> update
        // 8. map.put(2, updated similarWords)

/* final state
 
map = {
  1: {                    // edit distance = 1 (BEST)
    3: ["hello"]         // frequency = 3 (HIGHEST for dist=1)
  },
  2: {                    // edit distance = 2  
    2: ["help"],         // frequency = 2
    1: ["held", "helm"]  // frequency = 1, sorted: ["held", "helm"]
  },
  4: {                    // edit distance = 4 (WORST)
    1: ["world"]         
  }
}

 */

         // mencari kata terbaik

         // map.firstEntry()
         // Method dari TreeMap yang mengembalikan entry dengan key terkecil
         // Return type: Map.Entry<Integer, TreeMap<Integer, TreeSet<String>>>
         // Key terkecil = edit distance terkecil = kata paling mirip

         // .getValue()
         // Method dari Map.Entry untuk mendapatkan value
         // Dalam kasus kita: TreeMap<Integer, TreeSet<String>>
         // Ini adalah TreeMap yang berisi frequency dan kata-kata

         // .lastEntry()
         // Method dari TreeMap untuk mendapatkan entry dengan key terbesar
         // Key terbesar = frequency tertinggi = kata paling sering muncul
         // Return type: Map.Entry<Integer, TreeSet<String>>

         // .getValue()
         // Mendapatkan TreeSet<String> dari entry frequency tertinggi

         // .first()
         // Method dari TreeSet untuk mendapatkan elemen pertama (terkecil)
         // Karena TreeSet<String> diurutkan alfabetis, ini adalah kata yang alfabetis pertama

         if (!map.isEmpty()) {
            res = map.firstEntry().getValue().lastEntry().getValue().first();
         }

         /* contoh first.entry
         
            TreeMap<Integer, String> distances = new TreeMap<>();
            distances.put(3, "far");
            distances.put(1, "close");  
            distances.put(2, "medium");

            distances.firstEntry(); // return entry dengan key=1, value="close"

          */

          /* contoh lengkap
            Misalkan input "helo", ada kata-kata:
            hello" (freq=10, dist=1), "help" (freq=8, dist=2), "held" (freq=5, dist=2)

            map = {
                1: {10: {"hello"}},           // distance 1, freq=10
                2: {8: {"help"}, 5: {"held"}} // distance 2, freq=8 dan 5
            }

            Proses:
            map.firstEntry()              // entry dengan key=1 (distance terkecil)
            .getValue()                   // {10: {"hello"}}
            .lastEntry()                  // entry dengan key=10 (freq tertinggi)
            .getValue()                   // {"hello"}
            .first()                      // "hello"

           */

           // Algoritma prioritas:
           // 1. pilih edit distance terkecil (kata paling mirip)
           // 2. jika ada tie, pilih frequency tertinggi (kata paling populer)
           // 3. jika masih tie, pilih yang alfabetis pertama (konsistensi)

      } else {
         // Word found in dictionary, Kata sudah benar, tidak perlu koreksi
         res = s;
      }

      return res; // Return kata itu sendiri (dalam lowercase)
   }

   // Possible return values:
   // null: Jika input tidak valid atau tidak ada kata mirip
   // String yang sama (lowercase): Jika kata sudah benar
   // String kata lain: Jika ditemukan koreksi yang lebih baik
   
   private int editDistance(String word1, String word2) {
   
      int n = word1.length();
      int m = word2.length();

      // int[][] = array 2 dimensi (matriks) dengan elemen bertipe integer
      // new int[n + 1][m + 1] = membuat matriks berukuran (n+1) x (m+1)
      // Ukuran n+1 dan m+1 karena kita perlu menyimpan kasus string kosong
      // Secara default, semua elemen diinisialisasi dengan nilai 0
      int[][] dp = new int[n + 1][m + 1];

      // outer loop: i dari 0 sampai n (inclusive), mewakili posisi di word1
      // inner loop: j dari 0 sampai m (inclusive), mewakili posisi di word2
      // ++i = pre-increment, sama dengan i = i + 1
      // Loop berjalan total (n+1) × (m+1) kali
      for(int i = 0; i <= n; ++i) {
         for(int j = 0; j <= m; ++j) {

            // BASE CASE - BARIS PERTAMA (i=0)
            // ketika i = 0, artinya kita membandingkan string kosong dengan j karakter pertama dari word2
            // untuk mengubah string kosong menjadi j karakter, kita perlu j operasi INSERT
            // contoh: "" → "do" memerlukan 2 operasi insert
            if (i == 0) {
               dp[i][j] = j;

            /* contoh
             * dp[0][0] = 0  // "" → "" = 0 operasi
             * dp[0][1] = 1  // "" → "d" = 1 insert
             * dp[0][2] = 2  // "" → "do" = 2 insert
             * dp[0][3] = 3  // "" → "dog" = 3 insert
             */

            // BASE CASE - KOLOM PERTAMA (j=0)
            // ketika j = 0, artinya kita membandingkan i karakter pertama dari word1 dengan string kosong
            // untuk mengubah i karakter menjadi string kosong, kita perlu i operasi DELETE
            // contoh: "cat" → "" memerlukan 3 operasi delete
            } else if (j == 0) {
               dp[i][j] = i;

            /* dp[1][0] = 1  // "c" → "" = 1 delete
             * dp[2][0] = 2  // "ca" → "" = 2 delete
             * dp[3][0] = 3  // "cat" → "" = 3 delete
             */

            // KARAKTER MATCH
            // charAt(index) adalah method bawaan String di Java
            // mngembalikan karakter pada posisi index (dimulai dari 0)
            // i - 1 dan j - 1 karena dp menggunakan indeks 1-based, sedangkan string 0-based
            // jika karakter di posisi (i-1) dari word1 sama dengan karakter di posisi (j-1) dari word2
            // maka tidak perlu operasi tambahan, ambil nilai dari dp[i-1][j-1] (diagonal)
            } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
               dp[i][j] = dp[i - 1][j - 1];
            } 

            /* contoh
             * word1 = "cat", word2 = "car"
             * pada i=2, j=2:
             * word1.charAt(1) = 'a'  // karakter ke-2 dari "cat"
             * word2.charAt(1) = 'a'  // karakter ke-2 dari "car"  
             * karena sama, dp[2][2] = dp[1][1]
             */
            
            // TRANSPOSITION (PERTUKARAN KARAKTER)
            // i > 1 && j > 1: Pastikan ada minimal 2 karakter di masing-masing string
            // word1.charAt(i - 2) == word2.charAt(j - 1): Karakter sebelumnya word1 = karakter current word2
            // ini mendeteksi pattern transposition (swap):
            // word1: ...XY...  (posisi i-2=X, i-1=Y)
            // word2: ...YX...  (posisi j-2=X, j-1=Y)
            else if (i > 1 && j > 1 && 
                      word1.charAt(i - 1) == word2.charAt(j - 2) && 
                      word1.charAt(i - 2) == word2.charAt(j - 1)) {
               
               // Math.min(a, b) mengembalikan nilai terkecil antara a dan b
               // Nested Math.min() untuk mencari minimum dari 4 nilai
               // Transposition case
               dp[i][j] = 1 + Math.min(Math.min(dp[i - 2][j - 2], dp[i - 1][j]), 
                                      Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
            } 
            
            /* contoh
             * word1 = "form", word2 = "from"
             * "rm" vs "om" - karakter r dan o bertukar posisi
             * bisa diperbaiki dengan 1 operasi swap: form → from
             */
            
            // DEFAULT CASE
            else {
               dp[i][j] = 1 + Math.min(dp[i][j - 1], 
                                      Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
            }

            // ditambah 1 karena setiap operasi (insert/delete/substitute) menambah cost sebesar 1
            // jadi kita ambil minimum cost dari 3 kemungkinan operasi, lalu tambah 1
            
            // dp[i][j - 1]: INSERT - tambah karakter dari word2
            // dp[i - 1][j]: DELETE - hapus karakter dari word1
            // dp[i - 1][j - 1]: SUBSTITUTE - ganti karakter word1 dengan word2

            /* contoh setiap operasi
             * 1. INSERT: "cat" → "cart" → "car" (insert r, delete t)
             * 2. DELETE: "cat" → "ca" → "car" (delete t, insert r)
             * 3. SUBSTITUTE: "cat" → "car" (substitute t dengan r)
             */
         }
      }

      // dp[n][m] berisi minimum edit distance untuk mengubah seluruh word1 menjadi word2
      // posisi [n][m] = posisi terakhir di matriks, mewakili perbandingan string lengkap
      return dp[n][m];
   }
}

/* contoh untuk word1="kitten", word2="sitting":

inisialisasi
n = 6, m = 7
dp = matriks 7x8 (semua nilai 0)

     "" s  i  t  t  i  n  g
""   0  1  2  3  4  5  6  7
k    1  
i    2  
t    3  
t    4  
e    5  
n    6

proses fill matriks
    ""  s  i  t  t  i  n  g
""   0  1  2  3  4  5  6  7
k    1  1  2  3  4  5  6  7
i    2  2  1  2  3  4  5  6
t    3  3  2  1  2  3  4  5
t    4  4  3  2  1  2  3  4
e    5  5  4  3  2  2  3  4
n    6  6  5  4  3  3  2  3

Hasil akhir: dp[6][7] = 3

Operasi yang dibutuhkan:
kitten → sitten (substitute k→s)
sitten → sittn (substitute e→nothing atau delete e)
sittn → sitting (insert g)

Method ini digunakan untuk:
Menghitung seberapa "mirip" kata input dengan kata-kata di dictionary
Memilih kata dengan edit distance terkecil sebagai saran koreksi
Jika ada beberapa kata dengan distance sama, pilih yang paling sering muncul (frequency tertinggi)

cth penggunaan 
editDistance("teh", "the");      // return 1 (swap h dan e)
editDistance("colour", "color"); // return 1 (delete u)
editDistance("hello", "helo");   // return 1 (delete l)

 */