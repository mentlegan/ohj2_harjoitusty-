package pullo;

import java.io.OutputStream;
import java.io.PrintStream;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Valmistajat toimivat omana pientietokantanaan
 * valmistajat saavat id:n joita lisätyt pullot voivat käyttää asettaessaan itselleen valmistajaa
 * monella eri pullolla voi olla sama valmistaja, joten id:llä viittaaminen niihin vähentää turhaa toistoa
 * @author Harri Keränen
 * @version 16.4.2020
 * tyo5 vaihe: luokkia tehty, ja nyt testausta varten ne tulostavat leikisti tietoja
 * tyo6 vaihe: lisätty toString() ja parse, jotka osaavat muuttaa valmistajan tiedot tiedostomuotoon ja takaisin
 *  -comtestejä tehty
 * tyo7 vaihe: ei isompia muutoksia tähän luokkaan
 */
public class Valmistaja {

    private String nimi = "";
    private int id;
    private static int seuraavaNro = 1;
    
    
    /**
     * Oletusmuodostaja, samalla rekisteröi tunnuksen valmistajalle,
     * koska valmistajien id:t ovat tässä ohjelmassa tärkein.
     */            
    public Valmistaja() {
        rekisteroi();
    }
    
    
    /**
     * @return nimi
     */
    public String getNimi() {
        return nimi;
    }
    

    /**
     * Asetettaa valmistajalle nimen.
     * @param nimi Asetettava nimi
     * @return Asetettu nimi
     */
    public String setNimi(String nimi) {        
        this.nimi = nimi;
        return this.nimi;
    }
    
    
    /**
     * ht5 vaihe: Annetaan leikisti valmistajan tiedot.
     * TODO: joutaisi pois kun kaikki toimii?
     */
    public void annaValmistajanTiedot() {
        nimi = "Olvi Oyj";
    }
    
    
    /**
     * Tulostetaan valmistajan tiedot.
     * Ei varsinaisessa käytössä, tehty testaamista varten.
     * @param out tietovirta, mihin tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Valmistaja: "+nimi);
    }
    
    
    /**
     * Tulostetaan valmistajan tiedot.
     * Ei varsinaisessa käytössä, tehty testaamista varten.
     * @param os tietovirta, mihin tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Valmistajalle annetaan id, jota pullot voivat seurata.
     * @return id-tunnusnumero
     * @example
     * <pre name="test"> 
     * Valmistaja koskenkorva = new Valmistaja();
     * koskenkorva.getId() === 2;
     * Valmistaja cokacola = new Valmistaja();
     * cokacola.getId() === 3;
     * </pre>
     */
    public int rekisteroi() {
        id = seuraavaNro;
        seuraavaNro++;
        return id;        
    }
    
    
    /**
     * @return id-tunnusnumero
     */
    public int getId() {
        return id;
    }
    
    
    /**
     * Lähinnä testausta varten.
     * @param id id-tunnusnumero
     */
    public void setId(int id) {
        this.id=id;
    }
    
    
    /**
     * Ottaa tiedoston tekstin, ja muokkaa sen käsiteltävään muotoon.
     * @param rivi Rivi, josta tiedot otetaan
     * @example
     * <pre name="test">
     * Valmistaja koskenkorva = new Valmistaja();
     * koskenkorva.parse("1|Koskenkorva");
     * koskenkorva.getId() === 1;
     * koskenkorva.toString() === "1|Koskenkorva";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        id = Mjonot.erota(sb, '|', getId());
        nimi = Mjonot.erota(sb, '|', nimi);
    }
    
    
    /**
     * Määrää, että missä muodossa valmistajat tallentuvat tiedostoksi.
     * @return Tiedot tiedostossa esiintyvässä merkkijonossa
     * @example
     * <pre name="test">
     * Valmistaja koskenkorva = new Valmistaja();
     * koskenkorva.parse("1|Koskenkorva");
     * koskenkorva.toString() === "1|Koskenkorva";
     * </pre>
     */
    @Override
    public String toString() {
        String jono = "" + id + "|" + nimi;
        return jono;
    }
    
    
    /**
     * @param args nope
     */
    public static void main(String[] args) {    
    Valmistaja olviOyj = new Valmistaja();
    Valmistaja olviOyj2 = new Valmistaja();
    Valmistaja olviOyj3 = new Valmistaja();
    
    olviOyj.annaValmistajanTiedot();    
    olviOyj.tulosta(System.out);
    System.out.println(olviOyj.toString());
    
    olviOyj2.annaValmistajanTiedot();
    olviOyj2.tulosta(System.out);
    
    olviOyj3.annaValmistajanTiedot();
    olviOyj3.tulosta(System.out);
    }
    
}