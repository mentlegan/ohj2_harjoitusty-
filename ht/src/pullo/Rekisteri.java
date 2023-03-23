package pullo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Harri Keränen
 * @version 7.4.2020
 * tyo5 vaihe: toimii välikätenä pullojen ja valmistajien välillä
 * avustaa controlleria
 * tyo6 vaihe: comtestejä tehty, ja tiedostojen luku ja tallennus lisätty
 * tyo7 vaihe: lisätty haku-ja lajittelukutsut
 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import pullo.SailoException;
 * 
 * private Rekisteri r;
 * private Pullo p1;
 * private Pullo p2;
 * private Pullo p3;
 * private Pullo p4;
 * private Valmistaja v1;
 * private Valmistaja v2;
 * 
 * public void alustaRekisteri() {
 *      r = new Rekisteri();
 *      p1 = new Pullo(); p1.taytaOlviTiedoilla(1); p1.rekisteroi();
 *      p2 = new Pullo(); p2.taytaOlviTiedoilla(1); p2.rekisteroi();
 *      p3 = new Pullo(); p3.taytaOlviTiedoilla(2); p3.rekisteroi();
 *      p4 = new Pullo(); p4.taytaOlviTiedoilla(2); p4.rekisteroi();
 *      v1 = new Valmistaja();
 *      v2 = new Valmistaja();
 *      v1.annaValmistajanTiedot();
 *      v2.annaValmistajanTiedot();
 *      try {
 *      r.lisaa(p1);
 *      r.lisaa(p2);
 *      r.lisaa(p3);
 *      r.lisaa(p4);
 *      r.lisaa(v1);
 *      r.lisaa(v2);
 *      } catch ( Exception e ) {
 *          System.err.println(e.getMessage());
 *      }
 * }
 * </pre>
 */
public class Rekisteri {
    
    private Pullot pullot = new Pullot();
    private Valmistajat valmistajat = new Valmistajat();
    
    
    /**
     * Oletusmuodostaja, ei tee mitään.
     */
    public Rekisteri() {
        //ei tarvitse tehdä mitään
    }
    
    
    /**
     * Lisätään uusi valmistaja rekisteriin.
     * @param uusi Lisättävä pullo
     * @throws SailoException jos ei mahdu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaRekisteri();
     * r.getValmistajia() === 5;
     * r.lisaa(v1);
     * r.getValmistajia() === 6;
     * </pre>
     */
    public void lisaa(Valmistaja uusi) throws SailoException {
        valmistajat.lisaa(uusi);
    }
    
    
    /**
     * Lisätään uusi pullo rekisteriin.
     * @param uusi Lisättävä pullo
     * @throws SailoException jos ei mahdu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaRekisteri();
     * r.getPulloja() === 4;
     * r.lisaa(p1);
     * r.getPulloja() === 5;
     * </pre>
     */
    public void lisaa(Pullo uusi) throws SailoException {
        pullot.lisaa(uusi);
    }
    
    
    /**
     * Poistetaan pullo rekisteristä.
     * @param pullo Poistettava pullo
     * @return pullon poisto
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * r.getPulloja() === 4;
     * r.poistaP(p1);
     * r.getPulloja() === 3;
     * </pre> 
     */  
    public int poistaP(Pullo pullo) {
        if (pullo == null) return 0;
        int ret = pullot.poista(pullo.getTunnus());
        return ret;
    }


    /**
     * Poistetaan valmistaja rekisteristä.
     * Jos valmistaja poistetaan, sen tiedot häviävät pullon tiedoista,
     * uuden valmistajan voi asettaa valitsemalla pullo ja valmistaja listoistaan ja painamalla muokkaa.
     * @param valmistaja poistettava valmistaja
     * @example
     * <pre name="test">
     * alustaRekisteri();
     * r.getValmistajia() === 12;
     * poistaV(v1);
     * r.getValmistajia() === 12;
     * p1.getValId() === 1;
     * </pre>
     */
    public void poistaV(Valmistaja valmistaja) {
        Collection<Pullo> loydetyt = annaPullot(valmistaja);
        for (Pullo pullo : loydetyt) {
            pullo.setValId(0);
        }
        valmistajat.poista(valmistaja);
    }
    
    
    /**
     * Kysyy pullot-luokalta, että paljonko pulloja on.
     * @return Pullojen lukumäärä
     */
    public int getPulloja() {
        return pullot.getLkm();
    } 
    
    
    /**
     * Kysyy valmistajat-luokalta, että paljonko valmistajia on.
     * @return Valmistajien lukumäärä
     */
    public int getValmistajia() {
        return valmistajat.getLkm();
    }
    

    /**
     * Palauttaa pullon id:n perusteella.
     * @param id id-tunnusnumero: monesko palautetaan
     * @return Etsittävä pullo
     * @throws IndexOutOfBoundsException jos id on väärin
     */
    public Pullo annaPullo(int id) throws IndexOutOfBoundsException {
        return pullot.anna(id);
    }
    
    
    /**
     * Palauttaa valmistajan id:n perusteella.
     * @param id id-tunnusnumero: mikä valmistaja palautetaan
     * @return Etsittävä valmistaja
     * @throws IndexOutOfBoundsException jos id on väärin
     */
    public Valmistaja annaValmistaja(int id) throws IndexOutOfBoundsException {
        return valmistajat.anna(id);
    }
    
    
    /**
     * Palauttaa annetun pullon valmistajan.
     * @param pullo Pullo, jonka valmistaja halutaan
     * @return pullon valmistaja
     */
    public Valmistaja annaPullonValmistaja(Pullo pullo) {
        for (Valmistaja valmistaja : valmistajat) {
            if (valmistaja.getId() == pullo.getValId())
                return valmistaja;
        }
        return null;
    }
    
    
    /**
     * Palauttaa listan pulloista, jonka param valmistaja on tehnyt.
     * @param valmistaja Valmistaja, jonka tekemät pullot halutaan
     * @return Lista pulloista
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     * alustaRekisteri();
     * Valmistaja v2 = new Valmistaja();
     * r.lisaa(v2);
     * v2.setId(2);
     * Collection<Pullo> loytyneet;
     * loytyneet = r.annaPullot(v2);
     * loytyneet.size() === 2;
     * loytyneet = r.annaPullot(v1);
     * loytyneet.size() === 2;
     * </pre>
     */
    public Collection<Pullo> annaPullot(Valmistaja valmistaja) {
        return pullot.annaPullot(valmistaja.getId());
    }
    

    /**
     * Lisäyksen tai muokkauksen apu.
     * @param pullo käsiteltävä pullo
     * @throws SailoException jos ongelmia
     */
    public void korvaaTaiLisaa(Pullo pullo) throws SailoException {
        pullot.korvaaTaiLisaa(pullo);
    }
    
    
    /**
     * Etsii pullot jollain hakusanalla, käyttää apuna myös hakuehtoa.
     * @param hakusana hakusana
     * @param hakuehto hakuehto
     * @return lista löydetyistä pulloista
     */
    public Collection<Pullo> etsi(String hakusana, String hakuehto) {
        return pullot.etsi(hakusana, hakuehto);
    }
    
    
    /**
     * Lukee tiedot tiedostoista kutsumalla pullot- ja valmistajat-luokkia vuoron perään.
     * @param s Tiedostoja-niminen kansio
     * @throws SailoException jos ongelmia
     */
    public void lueTiedostosta(String s) throws SailoException {
            pullot = new Pullot();
            valmistajat = new Valmistajat();
            
            setTiedosto(s);
            pullot.lueTiedostosta(s+"\\nimet");
            valmistajat.lueTiedostosta(s+"\\valmistajat");             
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet.
     * Pulloille annetaan nimet.dat,
     * Valmistajille valmistajat.dat
     * @param nimi Uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        pullot.setTiedostonPerusNimi(hakemistonNimi + "nimet");
        valmistajat.setTiedostonPerusNimi(hakemistonNimi + "valmistajat");
    }
    
    
    /**
     * Tarkastaa, että onko jokin pullon id jo varattu.
     * @param tunnus etsittävä tunnus
     * @return true jos on, false jos ei
     */
    public boolean tarkastaPtunnus(int tunnus) {
        if (pullot.tarkastaTunnus(tunnus) == true) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Tarkastaa, että onko jokin valmistajan id jo varattu.
     * @param tunnus etsittävä tunnus
     * @return true jos on, false jos ei
     */
    public boolean tarkastaVtunnus(int tunnus) {
        if (valmistajat.tarkastaTunnus(tunnus) == true) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Lajittelee pullot ehdon mukaiseen järjestykseen.
     * @param hakuehto millä lajitellaan
     * @return pullot lajiteltuna ehdon mukaan
     */
    public ArrayList<Pullo> lajittele(String hakuehto) {
        return pullot.lajittele(hakuehto);
    }
    
    
    /**
     * Lajittelee valmistajat aakkosjärjestykseen.
     * @return valmistajat aakkosjärjestyksessä
     */
    public ArrayList<Valmistaja> lajitteleValmistajat() {
        return valmistajat.lajitteleAakkosiin();
    }
    
    
    /**
     * Tallentaa pullot ja valmistajat omiin tiedostoihinsa.
     * @throws SailoException jos tallentamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        setTiedosto("tiedostoja");
        try {
            pullot.tallenna();
        } catch ( SailoException e ) {
            virhe = e.getMessage();
        }
        
        try {
            valmistajat.tallenna();
        } catch ( SailoException e ) {
            virhe += e.getMessage();
        }

        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * @param args nope
     */
    public static void main(String[] args) {
        var rekisteri = new Rekisteri();
        
        Pullo olvi1 = new Pullo(), olvi2 = new Pullo();
        olvi1.taytaOlviTiedoilla(1);
        olvi2.taytaOlviTiedoilla(2);
        
        Valmistaja olviOyj = new Valmistaja();
        Valmistaja olviOyj2 = new Valmistaja();
        Valmistaja olviOyj3 = new Valmistaja();
        olviOyj.annaValmistajanTiedot(); //tämä antaa nimen valmistajalle
        
        try {
            rekisteri.lisaa(olviOyj);
            rekisteri.lisaa(olviOyj2);
            rekisteri.lisaa(olviOyj3);
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
            System.err.flush();
        }
        
        try {
            rekisteri.lisaa(olvi1);
            rekisteri.lisaa(olvi2);
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
            System.err.flush();
        }
        
        for (int i = 0; i < rekisteri.getPulloja(); i++) {
            Pullo pullo = rekisteri.annaPullo(i);
            System.out.println("Pullo paikassa: " + i);
            pullo.tulosta(System.out);
        }
        
        for (int i = 0; i < rekisteri.getValmistajia(); i++) {
            Valmistaja valmistaja = rekisteri.annaValmistaja(i);
            System.out.println("Valmistaja paikassa: " + i);
            valmistaja.tulosta(System.out);
            
            Collection<Pullo> loytyneet = rekisteri.annaPullot(valmistaja);
            for (Pullo pullo : loytyneet) {
                pullo.tulosta(System.out);
            }
            
        }
                
    }

}