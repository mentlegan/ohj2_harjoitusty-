package pullo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Harri Keränen
 * @version 14.4.2020
 * tyo5 vaihe: pullojen hoitaminen
 * oma tietorakenne usealle pullolle
 * muista, että pulloilla ei ole viitteitä, vaan valmistajilla
 * tyo6 vaihe: nyt osataan lukea pullot tiedostoista, ja tallentaa ne tiedostoihin
 *  -comtestejä tehty
 * tyo7 vaihe: tehty etsintä- ja lajittelumetodit
 */
public class Pullot implements Iterable<Pullo>, Comparable<Pullo> {
    
    private int     MAX_PULLOJA        = 10;
    private boolean muutettu           = false;
    private int     lkm                = 0;
    private String  kokoNimi           = "Pullot";
    private String  tiedostonPerusNimi = "tiedostoja\\nimet";
    private Pullo   alkiot[]           = new Pullo[MAX_PULLOJA];
    
    
    /**
     * Oletusmuodostaja.
     */
    public Pullot() {
        //attribuuttien alustus riittää
    }
    
    
    /**
     * Lisää uuden pullon tietorakenteeseen ja ottaa pullon omistukseensa.
     * tyo6 vaihe: taulukko kasvaa nyt dynaamisesti.
     * @param pullo Lisättävä pullo
     * @example
     * <pre name="test">
     * Pullot pullot = new Pullot();
     * Pullo p1 = new Pullo(), p2 = new Pullo();
     * pullot.getLkm() === 0;
     * pullot.lisaa(p1); pullot.getLkm() === 1;
     * pullot.lisaa(p2); pullot.getLkm() === 2;
     * pullot.lisaa(p1); pullot.getLkm() === 3;
     * </pre>
     */
    public void lisaa(Pullo pullo) {    
        if (lkm >= alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, alkiot.length+10);
        }
        alkiot[lkm++]=pullo;
        muutettu = true;
    }
    
    
    /**
     * Etsii pullon id:n perusteella.
     * @param i Monennenko pullon viite halutaan
     * @return Viite pulloon, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Pullo anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laitoin indeksi: " + i);
        return alkiot[i];
    }
    

    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta.
     * @throws SailoException jos ongelmia
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());   
    }
  
    
    /**
     * Lukee tiedot tiedostosta, ja lisää ne tietorakenteeseen.
     * @param tiedostot Luettavat tiedostot
     * @throws SailoException jos ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     * Pullot pullot = new Pullot();
     * Pullo p1 = new Pullo();
     * Pullo p2 = new Pullo();
     * p1.taytaOlviTiedoilla(1);
     * p2.taytaOlviTiedoilla(2);
     * p1.rekisteroi();
     * p2.rekisteroi();
     * String hakemisto = "tiedostot";
     * String nimi = hakemisto+"/nimet";
     * File ftied = new File(nimi+".dat");
     * File dir = new File(hakemisto);
     * dir.mkdir();
     * ftied.delete();
     * pullot.lueTiedostosta(nimi); #THROWS SailoException
     * pullot.lisaa(p1);
     * pullot.lisaa(p2);
     * pullot.tallenna();
     * pullot = new Pullot();
     * pullot.lueTiedostosta(nimi);
     * pullot.lisaa(p2);
     * pullot.tallenna();
     * ftied.delete() === false;
     * File fbak = new File(nimi+".bak");
     * fbak.delete() === false;
     * dir.delete() === false;
     * </pre>
     */
    public void lueTiedostosta(String tiedostot) throws SailoException {
        setTiedostonPerusNimi(tiedostot);
        try {
            @SuppressWarnings("resource")
            BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()));
            kokoNimi = fi.readLine();
            if (kokoNimi == null) throw new SailoException("Rekisterin nimi puuttuu");
            String rivi = fi.readLine();
            if (rivi==null) throw new SailoException("koko puuttuu?");
            
            while ( ( rivi=fi.readLine()) != null) {
                rivi=rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                Pullo pullo = new Pullo();
                pullo.parse(rivi);
                lisaa(pullo);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea!");
        } catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    
    }
    
    
    /**
     * Tallentaa käsillä olevat tiedot tiedostoiksi.
     * @throws SailoException jos ongelmia
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(lkm);
            for (Pullo pul : this) {
                fo.println(pul.toString());
            }
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea!");
        } catch (IOException e) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia!");
        }
        
        muutettu = false;
    }
    
    
    /**
     * @return Rekisterin koko nimi tiedostoon
     */
    public String getKokoNimi() {
        return kokoNimi;
    }


    /**
     * @return Tiedoston perusnimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * @param nimi Tiedoston nimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    
    /**
     * @return Tiedoston perusnimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    
    /**
     * @return Backup-tiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi+".bak";
    }    
    

    /**
     * Iteraattori pulloille, että toimisi paremmin.
     * @author OMISTAJA
     * @version 2.4.2020
     */
    public class PullotIterator implements Iterator<Pullo> {
        
        private int kohdalla = 0;
        
        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        @Override
        public Pullo next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
        }
        
        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei poisteta");
        }
    
    }
    
    /**
     * Palautetaan iteraattori.
     * @return Pullojen iteraattori PullotIterator
     */
    @Override
    public Iterator<Pullo> iterator() {
        return new PullotIterator();
    }
    

    /**
     * Palauttaa pullojen lukumäärän, tätä käytetään mm. pullon hakemisessa.
     * @return lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Tälle annetaan valmistajan id, jonka perusteella etsitään siihen liitetyt pullot,
     * ja lisätään ne listaan.
     * @param id Valmistaja, jonka pullot halutaan
     * @return Löydetyt pullot listana (voitaisiin tarvittaessa muuttaa joksikin muuksi)
     */
    public ArrayList<Pullo> annaPullot(int id) {
        ArrayList<Pullo> loydetyt = new ArrayList<Pullo>();
        for (Pullo pullo : alkiot) {
            if (pullo != null) {                
                if (pullo.getValId() == id) {                
                    loydetyt.add(pullo);         
                }                       
            }
        } 
        return loydetyt;
    }
    

    /**
     * Korvataan tai lisätään tietoja.
     * @param pullo Käsiteltävä pullo
     */
    public void korvaaTaiLisaa(Pullo pullo) {
        int id = pullo.getTunnus();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnus() == id) {
                alkiot[i] = pullo;
                muutettu = true;
                return;
            }
        }
        lisaa(pullo);
    }

    
    /**
     * Poistaa pullon tietorakenteesta.
     * @param id poistettava pullo
     * @return pullon poisto
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++) {
            alkiot[i] = alkiot[i+1];
        }
            alkiot[lkm] = null;
            muutettu = true;
            return 1;
    }
    
    
    /**
     * Vertaa nimet keskenään.
     * @author OMISTAJA
     * @version 13.4.2020
     */
    public class nimiVertaaja implements Comparator<Pullo> {       
        @Override
        public int compare(Pullo p1, Pullo p2) {
            return p1.getNimi().compareTo(p2.getNimi());
        }
    }
    
    
    /**
     * Vertaa alkoholiprosentit keskenään.
     * @author OMISTAJA
     * @version 13.4.2020
     */
    public class alkoholiVertaaja implements Comparator<Pullo> {
        @Override
        public int compare(Pullo p1, Pullo p2) {
            if (p1.getProsentti() < p2.getProsentti()) return -1;
            if (p1.getProsentti() < p2.getProsentti()) return 1;
            return 0;
        }
        
    }
    
    /**
     * Vertaaja juomatyypille.
     * @author OMISTAJA
     * @version 13.4.2020
     */
    public class tyyppiVertaaja implements Comparator<Pullo> {       
        @Override
        public int compare(Pullo p1, Pullo p2) {
            return p1.getTyyppi().compareTo(p2.getTyyppi());
        }
    }
    

    /**
     * Vertaaja valmistusmalle.
     * @author OMISTAJA
     * @version 13.4.2020
     */
    public class maaVertaaja implements Comparator<Pullo> {       
        @Override
        public int compare(Pullo p1, Pullo p2) {
            return p1.getMaa().compareTo(p2.getMaa());
        }
    }
    
    @Override
    public int compareTo(Pullo o) {
        // 
        return 0;
    }
    
    /**
     * Yleinen lajittelumetodi, voi käsitellä kaikkia tietoja pullosta.
     * @param hakuehto lajitteluehto
     * @return pullot lajitteluehdon mukaan
     */
    public ArrayList<Pullo> lajittele(String hakuehto) {
        ArrayList<Pullo> jarjestetyt = new ArrayList<Pullo>();
        for (int i = 0; i < getLkm(); i++) {
            if (anna(i).getTyyppi() != null && anna(i).getMaa() != null)
            jarjestetyt.add(anna(i));
        }
        for (int i = 0; i < getLkm(); i++) {       
            if (hakuehto == "Aakkosjärjestys") {
                Collections.sort(jarjestetyt,new nimiVertaaja());
            }
            
            try {
                if (hakuehto == "Alkoholi Prosentti") {
                    Collections.sort(jarjestetyt,new alkoholiVertaaja());             
                }
            } catch (NumberFormatException e) {
                return jarjestetyt;
            }           
            
            if (hakuehto == "Juomatyyppi") {
                Collections.sort(jarjestetyt,new tyyppiVertaaja());
            }
            
            if (hakuehto == "Valmistusmaa") {
                Collections.sort(jarjestetyt,new maaVertaaja());
            }
            
        }
        return jarjestetyt;
    }
    
    
    /**
     * Tarkastaa, että onko jokin pullon id jo varattu.
     * @param id Tarkastettava id
     * @return true jos varattu, false jos ei
     */
    public boolean tarkastaTunnus(int id) {
        for (int i = 0; i < getLkm(); i++) {
            Pullo pullo = anna(i);
            if (pullo.getTunnus() == id) return true;
        }
        return false;
    }
    
    
    /**
     * Etsii id:n.
     * @param id etsittävä id
     * @return id-tunnus
     */
    private int etsiId(int id) {
        for (int i = 0; i < lkm; i++) {
            if (id == alkiot[i].getTunnus()) return i;
        }
        return -1;
    }
    
    
    /**
     * Etsintä.
     * Osaa käsitellä kaikki pullon tiedot, ja verrata niitä hakusanaan.
     * @param hakusana hakusana, jolla etsitään
     * @param hakuehto hakuehtoa voidaan käyttää apuna
     * @return loytyneet pullot
     */
    public Collection<Pullo> etsi(String hakusana, String hakuehto) {        
        
        ArrayList<Pullo> loytyneet = new ArrayList<Pullo>();
        for (int i = 0; i < getLkm(); i++) {
            
            if (hakuehto == "Aakkosjärjestys") {
                if (anna(i).getNimi().startsWith(hakusana)) {
                    loytyneet.add(anna(i));
                    Collections.sort(loytyneet, new nimiVertaaja());
                }
            }
            
            try {
                if (hakuehto == "Alkoholi Prosentti") {
                    if (String.valueOf(anna(i).getProsentti()).startsWith(hakusana)) {
                        loytyneet.add(anna(i));
                        Collections.sort(loytyneet, new alkoholiVertaaja());
                    }                 
                }
            } catch (NumberFormatException e) {
                return loytyneet;
            }
            
            
            if (hakuehto == "Juomatyyppi") {
                if (anna(i).getTyyppi().startsWith(hakusana)) {
                    loytyneet.add(anna(i));
                    Collections.sort(loytyneet, new tyyppiVertaaja());
                }
            }
            
            if (hakuehto == "Valmistusmaa") {
                if (anna(i).getMaa().startsWith(hakusana)) {
                    loytyneet.add(anna(i));
                    Collections.sort(loytyneet, new maaVertaaja());
                }
            }
            
            if (loytyykoTieto(hakusana, i) && hakuehto == "Valmistaja") {
                loytyneet.add(anna(i));
            }
            
            if (loytyykoTieto(hakusana, i) == true && hakuehto.startsWith("(")) {                
                loytyneet.add(anna(i));         
            }
        }
        return loytyneet;
    }
    
    
    /**
     * Etsi-metodin apu, katsoo, että löytyykö hakusanalla tietoa
     * @param hakusana hakusana
     * @param i silmukan indeksi
     * @return true jos löytyy, false jos ei
     */
    public boolean loytyykoTieto(String hakusana, int i) {
        try {            
            double prosentti = Double.parseDouble(hakusana);
            if (anna(i).getProsentti() == prosentti) {
                return true;
            }
        } catch (NumberFormatException e) { //ettei turhaan nillitä desimaaliluvun muodosta
            if (anna(i).getNimi().startsWith(hakusana)) return true;
            if (anna(i).getTyyppi().startsWith(hakusana)) return true;
            if (anna(i).getMaa().startsWith(hakusana)) return true;                   
            return false;
        }
        return false;
    }


    /**
     * @param args nope
     */
    public static void main(String[] args) {
        Pullot pullot = new Pullot();
        Pullo olvi = new Pullo();
        Pullo olvi2 = new Pullo();
        Pullo olvi3 = new Pullo(2);
        olvi3.tulosta(System.out);
        olvi.taytaOlviTiedoilla(1);
        olvi2.taytaOlviTiedoilla(2);
               
        pullot.lisaa(olvi);
        pullot.lisaa(olvi2);        
        
        for (int i = 0; i < pullot.getLkm(); i++) {
            Pullo pullo = pullot.anna(i);
            pullo.tulosta(System.out);
        }
        
        Collection<Pullo> pullot2 = pullot.annaPullot(2);
        for (Pullo pul : pullot2) {
            System.out.println(pul.getValId() + " ");
            pul.tulosta(System.out);
        }
        
    }
    
}