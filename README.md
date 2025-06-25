# CS401 - MOSARCH

## Proje Tanımı
MOSARCH, Java projelerindeki dosyaların import ilişkilerini analiz ederek, çeşitli algoritmalarla bu dosyaları kümelere ayıran bir araçtır. Kullanıcı dostu Swing tabanlı arayüzü sayesinde, klasör seçimi, analiz ve sonuçların kaydedilmesi işlemleri kolayca yapılabilir.

## Kurulum
1. Java 8 veya üzeri bir sürümün yüklü olduğundan emin olun.
2. Proje dizininde `src` klasörü altında tüm Java dosyaları bulunmaktadır.
3. Gerekirse, `matrixAlgorithm.cpp` dosyasını derleyerek `matrixAlgorithm.exe` oluşturabilirsiniz.

## Kullanım
1. Uygulamayı başlatmak için `GuiMain.java` dosyasını çalıştırın.
2. Açılan pencerede analiz etmek istediğiniz proje klasörünün yolunu girin ve "Confirm" butonuna tıklayın.
3. Sonraki ekranda, farklı kümeleme algoritmalarından birini seçerek analiz işlemini başlatabilirsiniz.
4. Sonuçları kaydetmek için ilgili arayüzü kullanabilirsiniz.

## Kümelendirici Algoritmalar
- **Genetic Algorithm**: Evrimsel algoritma ile dosyaları kümeler.
- **KMode Clusterer**: K-Modlar algoritması ile kümeler oluşturur.
- **Import Clusterer**: Import sıklığına göre gruplama yapar.
- **Import Relationship Analyzer**: Dosyalar arası ilişki ve benzerliklere göre kümeler.
- **Matrix Algorithm**: (C++ ile) matris tabanlı kümeleme. 
- **Tamamı kendi hesaplarımız ile yazılmış algoritmalardır.**

## Katkı ve Lisans
Katkıda bulunmak için pull request gönderebilirsiniz. Lisans bilgisi için lütfen proje sahibine danışınız.
