package pullo;

import java.io.OutputStream;
import java.io.PrintStream;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Käsittelee pullon tiedot/kentät: kuten nimi, valmistusmaa, alkoholi% jne.
 * @author Harri Keränen
 * @version 22.4.2020
 * tyo6 vaihe: lisätty toString() ja parse, jotka osaavat muuttaa pullon tiedot tiedostomuotoon ja takaisin
 *  -Comtestejä tehty
 * tyo7 vaihe: lisätty gettereitä ja settereitä tehokkaampaa tietojen käsittelyä varten
 * tyoX vaihe: testauksen tulostukset poistettu
 */
public class Pullo implements Cloneable {
    
    private String nimi;
    private double alkoholiprosentti;
    private String juomatyyppi;
    private String valmistusmaa;
    private int pTunnus; //pullon oma tunnus
    private int valId; //haetaan tieto Valmistaja-tai tai Valmistajat luokasta                                            
    private static int seuraavaNro = 1;
    
    
    /**
     * Oletusmuodostaja, rekisteröi pullon.
     */
    public Pullo() {
        rekisteroi();
    }
    
     
    @Override
    public Pullo clone() throws CloneNotSupportedException {
        Pullo uusi;
        uusi = (Pullo) super.clone();
        return uusi;
    }
    
    
    /**
     * Toinen muodostaja.
     * Jos annetaan id.
     * @param Id tunnusnro
     */
    public Pullo(int Id) {
        this.pTunnus = Id;
    }
    
    
    /**
     * Asetetaan pullolle tietoa, testausta varten.
     * TODO: joutaisi pois, kun kaikki toimii
     * @param nro id-viite valmistajaan, jonka pullosta on kyse
     */
    public void taytaOlviTiedoilla(int nro) {
        valId = nro;
        nimi = "Olvi III";
        alkoholiprosentti = 4.5;
        juomatyyppi = "III-Olut";
        valmistusmaa = "Suomi";
    }
    
    
    /**
     * Tulostetaan pullon tiedot.
     * @param out tietovirta, mihin tulostetaan
     */
    public void tulosta(PrintStream out) {
        
        if (nimi == null) {
            out.println("Nimi: (anna nimi)");                       
        } else out.println("Nimi: " + nimi);
        
        out.println();
        
        out.println("Alkoholiprosentti: " + alkoholiprosentti + "%");
        
        out.println();
        
        if (juomatyyppi == null || this.getTyyppi().trim().equals("")) {
            out.println("Juomatyyppi: (anna juomatyyppi)");
        } else out.println("Juomatyyppi: " + juomatyyppi);
        
        out.println();
        
        if (valmistusmaa == null || this.getMaa().trim().equals("")) {
            out.println("Valmistusmaa: (anna valmistusmaa)");
        } else out.println("Valmistusmaa: " + valmistusmaa);
        
        out.println();
    }
    
    
    /**
     * Tulostetaan pullon tiedot.
     * @param os tietovirta, mihin tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
    /**
     * Rekisteröi pullolle oman id:n, jota käytetään haussa.
     * @return Pullon id
     * @example
     * <pre name="test">
     * Pullo olvi = new Pullo();
     * olvi.taytaOlviTiedoilla(1);
     * olvi.rekisteroi();
     * olvi.getTunnus() === 7;
     * Pullo olvi2= new Pullo();
     * olvi2.taytaOlviTiedoilla(1);
     * olvi2.rekisteroi();
     * olvi2.getTunnus() === 9;
     * </pre>
     */
    public int rekisteroi() {
        pTunnus = seuraavaNro;
        seuraavaNro++;
        return pTunnus;
    }
    
    
    /**
     * @return Valmistajan id
     */
    public int getValId() {
        return valId;
    }

    
    /**
     * @param valId Valmistajan id
     */
    public void setValId(int valId) {
        this.valId=valId;
    }
    
    
    /**
     * @return Pullon nimi
     * @example
     * <pre name="test">
     * Pullo olvi = new Pullo();
     * olvi.taytaOlviTiedoilla(1);
     * olvi.getNimi() === "Olvi III";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Asetetaan nimi pullolle.
     * @param nimi Asetettava nimi
     * @return Asetettu nimi
     */
    public String setNimi(String nimi) {     
        this.nimi=nimi;
        return this.nimi;
    }                                     
    
    
    /**
     * @return Pullon id-tunnusnumero
     */
    public int getTunnus() {
        return pTunnus;
    }
    
    /**
     * @param tunnus asetettava tunnus
     * @return uusi ptunnus
     */
    public int setTunnus(int tunnus) {
        this.pTunnus=tunnus;
        return this.pTunnus;
    }        
    
    
    /**
     * @return Pullon aikoinaan sisältämän juoman alkoholiprosentti
     */
    public double getProsentti() {
        return alkoholiprosentti;
    }
    

    /**
     * @return Pullon aikoinaan sisältämän juoman tyyppi (esim. rommi tai olut)
     */
    public String getTyyppi() {
        return juomatyyppi;
    }
    

    /**
     * @return Pullon valmistusmaa
     */
    public String getMaa() {
        return valmistusmaa;
    }
    
    
    /**
     * @param prosentti Alustettava prosentti
     * @return Asetettu prosentti
     */
    public double setProsentti(double prosentti) {
        this.alkoholiprosentti=prosentti;
        return this.alkoholiprosentti;
    }
    
    
    /**
     * @param juomatyyppi Alustettava juomatyyppi
     * @return Asetettu juomatyyppi
     */
    public String setTyyppi(String juomatyyppi) {     
        this.juomatyyppi = juomatyyppi;
        return this.juomatyyppi;
    }
    
    
    /**
     * @param maa Alustettava valmistusmaa
     * @return Asetettu valmistusmaa
     */
    public String setMaa(String maa) {     
        this.valmistusmaa = maa;
        return this.valmistusmaa;
    }
    

    /**
     * Ottaa tiedoston tekstin, ja muokkaa sen käsiteltävään muotoon
     * @param rivi Rivi, josta tiedot otetaan
     * @example
     * <pre name="test">
     * Pullo pullo = new Pullo();
     * pullo.taytaOlviTiedoilla(1);
     * pullo.rekisteroi();
     * pullo.getTunnus() === 5;
     * pullo.toString() === "5|Olvi III|4.5|III-Olut|Suomi|1";
     * pullo.parse("1|Olvi III|4.5%|III-Olut|Suomi|1");
     * pullo.getNimi() === "Olvi III";
     * pullo.getValId() === 1;
     * pullo.getTunnus() === 1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        pTunnus = Mjonot.erota(sb, '|', pTunnus);
        nimi = Mjonot.erota(sb, '|', nimi);
        alkoholiprosentti = Mjonot.erota(sb, '|', alkoholiprosentti);
        juomatyyppi = Mjonot.erota(sb, '|', juomatyyppi);
        valmistusmaa = Mjonot.erota(sb, '|', valmistusmaa);
        valId = Mjonot.erota(sb, '|', valId);
    }
    
    
    /**
     * Ottaa pullon tiedot, ja antaa ne muotoon, joka on sopiva tiedostoon tallennukseen
     * @return Tiedot tiedostomuodossa
     * @example
     * <pre name="test">
     * Pullo pullo = new Pullo();
     * pullo.taytaOlviTiedoilla(1);
     * pullo.rekisteroi();
     * pullo.toString() === "3|Olvi III|4.5|III-Olut|Suomi|1";
     * </pre>
     */
    @Override
    public String toString() {
        String jono = "" + pTunnus + "|" +
                    nimi + "|" +
                    alkoholiprosentti + "|" +
                    juomatyyppi  + "|" +
                    valmistusmaa  + "|"+
                    this.getValId();
    
        return jono;
    }
    

    /**
     * @param args nope
     */
    public static void main(String[] args) {
        Pullo olvi = new Pullo();
        Pullo olvi2 = new Pullo();
        Pullo olvi3 = new Pullo();


        Pullo olvi4 = new Pullo(2);
        
        olvi.getValId(); //tällä annetaan leikisti jokin valmistaja
        olvi.taytaOlviTiedoilla(1); //tämä metodi on testausta varten
        olvi.tulosta(System.out);
        
        olvi2.getValId();
        olvi2.taytaOlviTiedoilla(2);
        olvi2.tulosta(System.out);
        
        //olvi3.taytaOlviTiedoilla();
        olvi3.getValId();
        olvi3.tulosta(System.out);
                
        olvi4.tulosta(System.out);
    }
    
}