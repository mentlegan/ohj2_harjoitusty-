package pullo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @author Harri Keränen
 * @version 7.4.2020
 * tyo5 vaihe: valmistajien hoitaminen
 * muista että pulloilla ei ole viitteitä, vaan valmistajilla
 * tyo6 vaihe: nyt osataan lukea valmistajat tiedostoista, ja tallentaa ne tiedostoihin
 *  -comtestejä tehty
 * tyo7 vaihe: lisätty lajittelumetodi aakkosjärjestykselle
 */
public class Valmistajat implements Iterable<Valmistaja>, Comparable<Valmistaja> {

    private static ArrayList<Valmistaja> alkiot = new ArrayList<Valmistaja>();
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "tiedostoja\\valmistajat";
    
    /**
     * Oletusmuodostaja
     */
    public Valmistajat () {
        //attribuuttien alustus riittää
        //alkiot = new Valmistaja[MAX_VALMISTAJIA];
    }
    
    
    /**
     * Lisää uuden valmistajan tietorakenteeseen ja ottaa valmistajan omistukseensa.
     * @param valmistaja Lisättävä valmistaja
     */
    public void lisaa(Valmistaja valmistaja) {    
        alkiot.add(valmistaja);
        muutettu = true;
    }
    

    /**
     * Poistaa valmistajan tietorakenteesta.
     * @param valmistaja valmistajan
     * @return valmistajan poisto
     */
    public boolean poista(Valmistaja valmistaja) {
        boolean ret = alkiot.remove(valmistaja);
        if (ret) muutettu = true;
        return ret;
    }
    
    
    /**
     * Tarkastaa, että onko jokin valmistajan id jo varattu.
     * @param id tarkasteltava id
     * @return true jos on, false jos ei
     */
    public boolean tarkastaTunnus(int id) {
        for (Valmistaja valmistaja : alkiot) {            
            if (valmistaja.getId() == id) return true;
        }        
        return false;
    }
    
    
    /**
     * Etsii valmistajien tiedot hakemistosta.
     * @param tiedostot Hakemisto, josta tiedot etsitään
     * @throws SailoException jos ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Valmistajat valmistajat = new Valmistajat();
     *  Valmistaja k1 = new Valmistaja(); k1.annaValmistajanTiedot();
     *  Valmistaja k2 = new Valmistaja(); k2.annaValmistajanTiedot();
     *  Valmistaja k3 = new Valmistaja(); k3.annaValmistajanTiedot(); 
     *  Valmistaja k4 = new Valmistaja(); k4.annaValmistajanTiedot();
     *  Valmistaja k5 = new Valmistaja(); k5.annaValmistajanTiedot(); 
     *  String hakemisto = "tiedostot";
     *  File ftied = new File(hakemisto+".dat");
     *  ftied.delete();
     *  valmistajat.lueTiedostosta(hakemisto); #THROWS SailoException
     *  valmistajat.lisaa(k1);
     *  valmistajat.lisaa(k2);
     *  valmistajat.lisaa(k3);
     *  valmistajat.lisaa(k4);
     *  valmistajat.lisaa(k5);
     *  valmistajat.tallenna();
     *  valmistajat = new Valmistajat();
     *  valmistajat.lueTiedostosta(hakemisto);
     *  valmistajat.lisaa(k1);
     *  valmistajat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(hakemisto+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tiedostot) throws SailoException {
        setTiedostonPerusNimi(tiedostot);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Valmistaja val = new Valmistaja();
                val.parse(rivi); // voisi olla virhekäsittely
                lisaa(val);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    
    /**
     * @return Tiedoston perusnimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Tallentaa valmistajat ht\tiedostoja\valmistajat.dat -tiedostoon.
     * @throws SailoException jos ongelmia
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Valmistaja val : this) {
                fo.println(val.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea!");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    
    /**
     * @return alkiot-tietorakenteen koko
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * @return suurin id mitä löytyy
     */
    public int haeSuurinId() {
        int suurin = Integer.MIN_VALUE;
        
        for (Valmistaja valmistaja : alkiot) {
            if (valmistaja.getId() > suurin) {          
                suurin = valmistaja.getId();
            }
        }
        return suurin+1;
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta.
     * @param tied Tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
     * @return Tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Palauttaa backup-tiedoston nimen.
     * @return Backup-tiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Antaa tietyn valmistajan tietorakenteesta.
     * @param id id-tunnusnumero
     * @return Otetaan indeksi
     * @throws IndexOutOfBoundsException jos laiton indeksi
     */
    public Valmistaja anna(int id) throws IndexOutOfBoundsException {       
        return alkiot.get(id);
    }
    
    /**
     * @return valmistajat aakkosjärjestyksessä
     */
    public ArrayList<Valmistaja> lajitteleAakkosiin() {
        ArrayList<Valmistaja> jarjestetyt = new ArrayList<Valmistaja>();
        for (Valmistaja valmistaja : alkiot) {
            jarjestetyt.add(valmistaja);
        }
        Collections.sort(jarjestetyt, new Vertaaja());
        return jarjestetyt;
    }
    
    
    /**
     * Vertaa nimet keskenään.
     * @author OMISTAJA
     * @version 13.4.2020
     */
    public class Vertaaja implements Comparator<Valmistaja> {       
        @Override
        public int compare(Valmistaja v1, Valmistaja v2) {
            return v1.getNimi().compareTo(v2.getNimi());
        }
    }
        
    
    @Override
    public int compareTo(Valmistaja o) {        
        return o.getNimi().compareTo(o.getNimi());
    }
    
    
    /**
     * Palautetaan iteraattori.
     */
    @Override
    public Iterator<Valmistaja> iterator() {
        return alkiot.iterator();
    }
    
    
    /**
     * @param args nope
     */
    public static void main(String[] args) {
        
        Valmistajat valm = new Valmistajat();
        
        Valmistaja olviOyj = new Valmistaja();
        Valmistaja olviOyj2 = new Valmistaja();
        Valmistaja olviOyj3 = new Valmistaja();
        
        olviOyj. annaValmistajanTiedot();
        olviOyj2.annaValmistajanTiedot();
        olviOyj3.annaValmistajanTiedot();
        
        valm.lisaa(olviOyj);
        valm.lisaa(olviOyj2);
        valm.lisaa(olviOyj3);
        
        olviOyj2.setId(100);
        olviOyj3.setId(2000);
        
        for (Valmistaja val : alkiot)
        val.tulosta(System.out);
        
    }

}