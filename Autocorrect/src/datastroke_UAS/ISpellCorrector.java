// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

// interface yang berisi "kontrak" untuk spell corrector

import java.io.IOException;

public interface ISpellCorrector {
   // method untuk menggunakan dictionary dari file
   // parameter => nama file dictionary 
   // throws IOException jika terjadi error saat membaca file
   void useDictionary(String dictionaryFileName) throws IOException;

   // method untuk menyarankan kata yang mirip dengan input
   // parameter => kata input yang mungkin salah eja
   // return kata yang paling mirip dari dictionary (atau bisa null)
   String suggestSimilarWord(String inputMissplelledWord);
   
}
