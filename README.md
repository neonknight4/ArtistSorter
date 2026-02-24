# ArtistSorter 🎵

**ArtistSorter** je JavaFX desktop aplikacija za brzo sortiranje muzičkih izvođača i pesama, sa podrškom za:

* automatsko razdvajanje **bend / ime+prezime / pseudonim**
* ručnu klasifikaciju kandidata
* deduplikaciju unosa
* **Excel import/export** kao trajno čuvanje stanja
* nastavak rada nakon ponovnog pokretanja aplikacije

Cilj projekta je da omogući **brzo i pouzdano sređivanje velikih lista izvođača** (hiljade redova) uz minimalan ručni rad.

---

# ✨ Funkcionalnosti

## 1. Parsiranje inputa

Unos formata:

```
Artist - Song
```

Podržane su različite crtice:

```
-
–
—
```

Automatski se:

* uklanjaju prazni redovi
* ignorišu duplikati **artist + song**
* ignoriše zapis bez pesme ako izvođač već ima pesmu
* sortiraju izvodjaci po slovu, artistu pa songu
* sve kandidate koji počinju sa članom "The" svrstava u bend

---

## 2. Podela na liste

### Sorted

Automatski ide:

* izvođač sa **jednom rečju** → tretira se kao **bend**
* ručno klasifikovani kandidati

Sortiranje:

* **PERSON → po prezimenu**
* **BEND / PSEUDONYM → po prvom slovu**
* **DUET**

---

### Candidates

Sadrži izvođače sa **više reči** koji zahtevaju odluku:

* Bend
* Ime + Prezime
* Pseudonim
* Duet

Klik na opciju:

```
Candidates → Sorted
```

Moguće je i vraćanje:

```
Sorted → Candidates
```

---

## 3. Excel persistencija

### Export

Kreira `.xlsx` sa dva sheet-a:

* **Sorted**
* **Candidates**

Predstavlja kompletan **snapshot aplikacije**.

---

### Import

Učitava isti `.xlsx` i:

* vraća prethodno stanje
* omogućava nastavak rada

---

## 4. Merge logika

Kod dodavanja novog teksta:

* postojeći podaci iz Excel-a **ostaju**
* dodaju se samo **novi zapisi**
* duplikati se ignorišu

---

## 5. Reset aplikacije

Dugme **Clear** briše:

* sorted listu
* candidates listu

i omogućava novi import ili unos.

---

# 🧱 Arhitektura

Projekt je podeljen na slojeve:

```
ui/
 └── ArtistSorterApp
 └── CandidateTypeCell

service/
 └── ArtistParserService

excel/
 └── ExcelExportService
 └── ExcelImportService

model/
 └── Row
 └── ImportResult
```

Principi:

* **Single Responsibility**
* odvajanje UI-a od biznis logike
* laka testabilnost bez JavaFX-a

---

# ⚙️ Tehnologije

* **Java 17**
* **JavaFX**
* **Maven**
* **Apache POI (Excel)**

---

# ▶️ Pokretanje

## Build

```bash
mvn clean install
```

## Run

```bash
mvn javafx:run
```

---

# 📦 Planirani naredni koraci

Moguća unapređenja:

* autosave session
* keyboard shortcuts za brzu klasifikaciju
* AI automatsko prepoznavanje tipa izvođača
* distribucija kao `.jar` / installer

---

# 👤 Autor

**Mihailo Janković**

GitHub:
[https://github.com/neonknight4/ArtistSorter](https://github.com/neonknight4/ArtistSorter)

---

# 📄 Licenca

Slobodno korišćenje za lične i edukativne svrhe.
