// Source code is decompiled from a .class file using FernFlower decompiler.
package datastroke_UAS;

import java.io.IOException;

// interface utama buat spell checker, ini yang bakal dipake user
public interface ISpellCorrector {

   // load dictionary dari file, bisa throw IOException kalo file bermasalah
   void useDictionary(String var1) throws IOException;

   // kasih saran kata yang mirip (fungsi utamanya spell checker)
   String suggestSimilarWord(String var1);
}
