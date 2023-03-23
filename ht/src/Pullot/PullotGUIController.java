package Pullot;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import pullo.Pullo;
import pullo.Rekisteri;
import pullo.SailoException;
import pullo.Valmistaja;

/**
 * @author Harri Keränen
 * @version 22.4.2020
 * Omia muistiinpanoja:
 * NEW PLAN: Tehty niin, että valmistaja lisätään ensin
 * sitten valmistaja valitaan, ja painetaan lisää pullo
 * valmistaja määräytyy näin ilman kummempaa kikkailua.
 * Jos valmistaja poistetaan, se poistuu pullon tiedoista, uusi valmistaja voidaan lisätä ja asettaa muokkaa pulloa-toiminnolla uudestaan
 * tämä toteutuu niin, että valitaan valmistaja, ja painetaan muokkaa pulloa painiketta, tämä valmistaja sitten ilmestyy muokkaa pulloa-ikkunaan valmistajan kohdalle.
 
 * tyo6 vaihe: Lisätyt pullot ja valmistajat voi nyt tallentaa tiedostoihin
 * tyo7 vaihe: pullojen ja valmistajien tiedot saadaan nyt dialogeista
 * Valmistajien asetus on päivitetty paremmaksi (kts. NEW PLAN)
 * Ohjelman sulkeminen päivitetty: tiedosto --> poistu poistettu ja ruksin toiminta korjattu: poistucontroller poistettu ja nyt kysytään yksinkertainen questiondialog, joka toimii oikein
 * Lisätty avustustila: käyttäjä voi aktivoida tämän tilan päälle tai pois
 *  - Kun tämä tila on päällä, käyttäjä voi kokeilla ohjelman toimintoja. Tilan ollessa päällä ohjelma ei tee varsinaista toimintoa, vaan selittää käyttäjälle, miten toiminto toimii
 * tyoX vaihe: testaus tulostukset ja havainnollistamiset poistettu
 *  - tiedostot latautuvat käynnistäessä
 */
public class PullotGUIController implements Initializable {
    
    //Ylänurkan menu
    @FXML private MenuItem MenuTallenna;
    @FXML private MenuItem MenuLisaa;
    @FXML private MenuItem MenuMuokkaa;
    @FXML private MenuItem MenuPoista;
    @FXML private MenuItem MenuApua;
    @FXML private MenuItem MenuLisaaV;
    @FXML private MenuItem MenuPoistaV;
    @FXML private MenuItem avustusTilaOnOff;

    
    //Pullojen ja valmistajien esittäminen
    @FXML private ListChooser<Pullo> PulloLista;
    @FXML private ListChooser<Valmistaja> ValmistajaLista;
    @FXML private ScrollPane panelPullo;
    @FXML private ScrollPane panelValmistaja;
    
    //Hakupalkin menu, määrää hakusanan ja lajitteluehdon
    @FXML private TextField hakuKentta;  
    @FXML private MenuButton hakuValitse;
    @FXML private MenuItem nimiLajittelu;
    @FXML private MenuItem alkoholiLajittelu;
    @FXML private MenuItem maaLajittelu;
    @FXML private MenuItem valmistajaLajittelu;
    @FXML private MenuItem tyyppiLajittelu;
    @FXML private MenuItem oletusLajittelu;
    
    //Pääikkunan napit
    @FXML private Button ButtonLisaa;
    @FXML private Button ButtonMuokkaa;
    @FXML private Button ButtonPoista;
    @FXML private Button ButtonLisaaValmistaja;
    @FXML private Button ButtonPoistaValmistaja;
    @FXML private Button valmistajaOnOff;
   
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }

    
    @FXML void lajitteleOletus() {
        if (avustusTila != true) {            
            hakuValitse.setText("(lajittele)");
            haeP(0);
        } else tulosta("Lajittelee pullot\nlisäysjärjestykseen");
    }
    
    
    @FXML void lajitteleNimet() {
        if (avustusTila != true) { 
            hakuValitse.setText("Aakkosjärjestys");
            lajittelePullot();
        } else tulosta("Lajittelee pullot\naakkosjärjestykseen");
    }
    
    @FXML void lajitteleAlkoholi() {
        if (avustusTila != true) { 
            hakuValitse.setText("Alkoholi Prosentti");
            lajittelePullot();
        } else tulosta("Lajittelee pullot alkoholiprosentin\nmukaan miedoimmasta väkevimpään");
    }
    
    @FXML void lajitteleTyyppi() {
        if (avustusTila != true) { 
            hakuValitse.setText("Juomatyyppi");
            lajittelePullot();
        } else tulosta("Lajittelee pullot juomatyypin\nmukaan aakkosjärjestykseen");
    }
    
    @FXML void lajitteleMaa() {
        if (avustusTila != true) { 
            hakuValitse.setText("Valmistusmaa");        
            lajittelePullot();
        } else tulosta("Lajittelee pullot valmistusmaan\nmukaan aakkosjärjestykseen");
    }
    
    @FXML void lajitteleValmistajalla() {  
        if (avustusTila != true) {
            lajitteleValmistajilla();
        } else tulosta("Valitse ensin valmistaja listasta,\nja sitten valitse tämä, niin\npullot järjestyvät valitun\nvalmistajan mukaan");
    }
    
    
    @FXML
    void etsiPullo() {
        if (avustusTila != true) { 
            String hakusana = hakuKentta.getText();
            String hakuehto = hakuValitse.getText();
            etsi(hakusana, hakuehto);
        } else tulosta("Tässä on hakukenttä pullojen etsimiselle");
    }
    
    
    @FXML void aakkostaValmistajat() {
        if (avustusTila != true) { 
            if (valmistajatAakkosissa == false) {           
                valmistajatAakkosissa = true;
                if (valmistajaKohdalla != null) {                
                    haeV(valmistajaKohdalla.getId());
                } 
            } else if (valmistajatAakkosissa == true) {           
                valmistajatAakkosissa = false;
                if (valmistajaKohdalla != null) {                
                    haeV(valmistajaKohdalla.getId());
                } 
            } else Dialogs.showMessageDialog("Listassa ei ole valmistajia lajittelua varten");
        } else tulosta("Järjestää valmistajat\naakkosjärjestykseen,\nja takaisin oletusjärjestykseen");
    }
    
    
    //Menun valikon metodit
    @FXML
    void naytaApu() { //toimii myös tarvittaessa ns. testi nappina uusille metodeille       
        Dialogs.showMessageDialog(
         "Hei, ja tervetuloa pullorekisteriin!\n"
       + "Aloita lisäämällä valmistaja, tai lataa tallennetut valitsemalla Tiedosto-->Lataa\n"
       + "Voit asettaa avustustilan päälle menusta: Muokkaa --> Avustustila\n"
       + "Avustustilassa voit kokeilla ohjelman toimintoja, ja ohjelma selittää, että mitä kyseinen toiminto tekee\n"
       + "Author: Harri Keränen 2020\n"
       );        
    }
    
    
    @FXML
    void avustusTila() {
        if (avustusTila == false) {            
            avustusTila = true;
            tulosta("");
        }
        else {
            avustusTila = false;
            areaPullo.setText("");
        }

    }
        
    
    @FXML
    void Tallenna() {
        if (avustusTila != true) { 
            TallennaTiedot();
        } else tulosta("Tallentaa syötetyt tiedot");
    }
    
      
    /**
     * tyo6 vaihe: tehty boolean tallennettu, joka muuttuu trueksi, kun painetaan tallenna
     */
    @FXML
    boolean Poistu() {
        if (tallennettu == false) {
            if ( !Dialogs.showQuestionDialog("Poistu", "Et ole tallentanut tietoja", "Poistu tallentamatta", "Peruuta"))
                return false;
        } else PoistuOhjelmasta();
        return true;
    }
    
    
    /**
     * Ohjelmasta poistuminen
     * AloitusController, ja Poistu-metodit kutsuvat poistuOhjelmasta-metodia
     * FXML metodeja ei voi kutsua, joten on mentävä vaikeamman kautta:
     * tässä controllerissa on siis oma PoistuOhjelmasta, joka sulkee ohjelman
     *  -tätä metodia voidaan kutsua tarvittaessa muissa controllereissa
     */
    static void PoistuOhjelmasta() {
        Platform.exit();
    }
    
    
    /**
     * Pullojen ja valmistajien käsittelymetodit
     */
    @FXML
    void lisaaPullo() {
        if (avustusTila != true) { 
            uusiPullo();
        } else tulosta("Lisää pullon rekisteriin,\nja asettaa tälle pullolle\nvalmistajaksi listasta valitun");
    }
    
    
    @FXML
    void muokkaaPullo() {  
        if (avustusTila != true) { 
            muokkaaPulloa();
        } else tulosta("Muokkaa valitun pullon tietoja,\nja asettaa tälle pullolle\nvalmistajaksi listasta valitun\nmikäli haluat,\nvalmistaja vaihtuu automaattisesti\njos pullolle ei ole asetettu valmistajaa");
    }

    
    @FXML
    void poistaPullo() {
        if (avustusTila != true) { 
            poisPullo();
        } else tulosta("Poistaa valitun pullon rekisteristä");
    }
    
    
    @FXML
    void lisaaValmistaja() {
        if (avustusTila != true) { 
            uusiValmistaja();
        } else tulosta("Lisää valmistajan rekisteriin,\naloita ohjelman käyttö tästä");
    }
    
    
    @FXML
    void poistaValmistaja() {  
        if (avustusTila != true) { 
            poisValmistaja();
        } else tulosta("Poistaa valmistajan rekisteristä,\nja nollaa tälle valmistajalle\nasetetuttujen pullojen viitteet");
    }
    
    
    //=================viiva======================
    
    private Rekisteri rekisteri;
    private TextArea areaPullo = new TextArea(); //ohjelman outputstream
    private Pullo pulloKohdalla; // valittu pullo
    private Valmistaja valmistajaKohdalla; // valittu valmistaja
    private boolean tallennettu = true; //toimii kuin on/off nappulana
    private boolean valmistajatAakkosissa = false; //tämä myös
    private boolean avustusTila = false; //ja tämä
    
    
    /**
     * Alustaa käyttöliittymän ohjelman käynnistyessä.
     * Jostain syystä tiedostojen luku ei toimi tässä.
     */
    private void alusta() {
        areaPullo.setEditable(false); //tekstikenttää ei voi muokata
        panelPullo.setContent(areaPullo);
        areaPullo.setFont(new Font("Arial Black",12));
        panelPullo.setFitToHeight(true);
        
        PulloLista.clear();
        PulloLista.addSelectionListener(e -> naytaPullo());
        
        ValmistajaLista.clear();
        ValmistajaLista.addSelectionListener(e -> valitseValmistaja());
        
        hakuValitse.setText("(lajittele)");
    }
    
    
    /**
     * Tallentaa pullot ja valmistajat tiedostoon.
     * Luo/muokkaa kansiota "tiedostoja" joka tehdään minun tapauksessani ht-hakemistoon(eli sen voi pushata gittiin myös esimerkin vuoksi).
     * Pullot tallentuvat nimet.dat-nimiseen tiedostoon ja valmistajat valmistajat.dat-tiedostoon.
     * Luo myös backup (.bak-loppuiset)-tiedostot.
     * @return Jos toimii --> null, jos ei --> virheviesti
     */
    public String TallennaTiedot() {
        try {
            rekisteri.tallenna(); //täällä lisätietoa
            tallennettu = true;
            return null;
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tallennuksessa ongelma! " + e.getMessage());
            return e.getMessage();
        }
    }
    
    
    /**
     * Asetetaan controlleriin rekisterin viite.
     * @param rekisteri Rekisteri, johon viitataan
     */
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaPullo();
        valitseValmistaja();
        lueTiedostot("tiedostoja"); //tässä on startin tiedostojen luku, mutta nyt testaamista varten voimme ladata sen erikseen
    }
    
    
    /**
     * Poistaa pullon.
     * Pullo poistuu rekisteristä ja listasta.
     * Pullon id vapautuu, tulevaisuudessa lisätyt pullot ottavat tämän id:n hallintaansa.
     */
    private void poisPullo() {
        if (valmistajaKohdalla == null) {
            Dialogs.showMessageDialog("Valitse pullo listasta ensin");
            return;
        }
        Pullo pullo = pulloKohdalla;
        if ( pullo == null ) return;
        if ( !Dialogs.showQuestionDialog("Poista pullo", "Poistetaanko pullo: " + pullo.getNimi(), "Kyllä", "Ei"))
            return;
        rekisteri.poistaP(pullo);
        int index = PulloLista.getSelectedIndex();              
        haeP(0);
        PulloLista.setSelectedIndex(index);
        tallennettu = false;
    }
    
        
    /**
     * Poistaa valmistajan.
     * Valmistaja poistuu listasta ja rekisteristä.
     * Kaikkien valmistettujen pullojen (eli id-täsmäävät) id:t muuttuvat apunumero nollaksi, joka tarkoittaa, että pullolle ei ole asetettu valmistajaa.
     */
    private void poisValmistaja() {
        if (valmistajaKohdalla == null) {
            Dialogs.showMessageDialog("Valitse valmistaja listasta ensin");
            return;
        }
        Valmistaja valmistaja = valmistajaKohdalla;
        if ( valmistaja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poista valmistaja", "Poistetaanko valmistaja: " + valmistaja.getNimi(), "Kyllä", "Ei"))
            return;        
        rekisteri.poistaV(valmistaja);
        valmistaja.setId(0);
        int index = ValmistajaLista.getSelectedIndex();
        haeV(0);
        ValmistajaLista.setSelectedIndex(index);
        tallennettu = false;
    }
    

    //Missä muokkaaValmistaja()?
    //olisi yksinkertainen ja helposti hoidettavissa, mutta
    // a) valmistajat lisätään ensin, joten niiden kirjoitusvirheen kyllä huomaa ajoissa.
    // b) valmistajille tulee tiedoksi vain nimi (tunnusnumero hoitaa itsensä), joten melkein olisi helpompaa vain poistaa valmistaja,
    // koska ne lisätään ennen pulloja
    
    
    /**
     * Muokataan pullon tietoja.
     * Valmistajan muuttaminen: jos pullon id on apuluku 0 (eli ei valmistajaa), ohjelma muuttaa pullon valmistajan automaattisesti listasta valittuun.
     * Valmistaja muokataan valitusta valmistajasta kysymyksen kera.
     */
    private void muokkaaPulloa() {
        if ( pulloKohdalla == null ) {
            Dialogs.showMessageDialog("Valitse pullo listasta."+"\n"+"Jos pulloja ei ole, lisää sellainen.");            
            return;
        }        
        try {
            Pullo pullo;
            if (pulloKohdalla.getValId() != valmistajaKohdalla.getId()) {
                if (pulloKohdalla.getValId() == 0 && valmistajaKohdalla.getId() != 0) {                
                    pulloKohdalla.setValId(valmistajaKohdalla.getId());
                    Dialogs.showMessageDialog("Valmistaja vaihdettu");
                    haeP(pulloKohdalla.getTunnus());
                } else
                if (Dialogs.showQuestionDialog("Vaihda valmistaja?", "Haluatko vaihtaa pullon valmistajan valittuun?", "Kyllä", "Ei")) {
                    pulloKohdalla.setValId(valmistajaKohdalla.getId());
                    haeP(pulloKohdalla.getTunnus());
                }
            }
            pullo = LisaysMuokkausController.kysyPullo(null, pulloKohdalla.clone(), valmistajaKohdalla, rekisteri.annaPullonValmistaja(pulloKohdalla));             
            if ( pullo == null ) return; // jos ei tehtykään muokkauksia
            rekisteri.korvaaTaiLisaa(pullo);                                    
            haeP(pullo.getTunnus());       
        } catch (CloneNotSupportedException e) {
            Dialogs.showMessageDialog("Muokkaamisessa ongelma");
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Muokkaamisessa ongelma");
        }
        tallennettu = false;
    }
    
    
    /**
     * Etsintäaliohjelma.
     * Tekstikenttään syötetty tieto antaa pulloja suodatettuna.
     * Toimii myös desimaaliluvuille. 
     * Voi käyttää hakuehtoa mukana.
     * Tai sitten etsii kaikkiin tietoihin täsmäävät pullot.
     * @param hakusana sana jolla etsitään
     * @param hakuehto ts. lajitteluehto
     */
    private void etsi(String sana, String ehto) {        
        if (rekisteri.getPulloja() != 0) {
            if (sana != "" && sana != null) {
                PulloLista.clear();
                Collection<Pullo> loytyneet = rekisteri.etsi(sana, ehto);                
                int index = 0;
                for (Pullo pullo : loytyneet) {
                    PulloLista.add(pullo.getNimi(), pullo); 
                }
                PulloLista.setSelectedIndex(index);
                
                if (loytyneet.isEmpty()) {
                    try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPullo)) {
                        areaPullo.setText("");
                        tulosta("Hakusanalla ei löytynyt pulloja");
                    }
                }
                
                if (lajiteltu() == 5) hakuValitse.setText("(lajittele)");
            } else if (lajiteltu() == 0) { // hakee oletuksen, jos pullot ovat lajittelematta
                haeP(0);
            } else lajittelePullot(); // esittää pullon lajiteltuna, jos näin on asetettu
        }
    }
    
    
    /**
     * Pullojen lajittelu hakuehdon mukaan.
     * Pullot järjestyvät yleensä aakkosjärjestykseen valitun ehdon mukaan.
     * Toimii haun kanssa yhteistyössä.
     */
    private void lajittelePullot() {
        if (rekisteri.getPulloja() != 0) { //pulloja pitää olla olemassa lajittelua varten
            String hakuehto = hakuValitse.getText();
            PulloLista.clear();
            ArrayList<Pullo> jarjestys = rekisteri.lajittele(hakuehto);
            for (Pullo pullo : jarjestys) {                
                PulloLista.add(pullo.getNimi(), pullo);
            }
        }
    }
    
    
    /**
     * Palauttaa numeroarvon lajitteluehdon perusteella:
     * @example 1 = aakkosjärjestys, 2 = alkoholi%, 3 = juomatyyppi, 4 = valmistusmaa, 5 = valmistaja, 0 = ei mikään, ts. (lajittele)
     * @return numeroarvo
     */
    public int lajiteltu() {
        if (hakuValitse.getText() == "Aakkosjärjestys") {
            return 1;
        }
        
        if (hakuValitse.getText() == "Alkoholi Prosentti") {
            return 2;
        }
        
        if (hakuValitse.getText() == "Juomatyyppi") {
            return 3;
        }
        
        if (hakuValitse.getText() == "Valmistusmaa") {
            return 4;   
        }
        
        if (hakuValitse.getText() == "Valmistaja") {
            return 5;
        }
                        
        return 0; //apunumero, jos pullot ovat oletusjärjestyksessä
    }
    
    
    /**
     * Ottaa valitun valmistajan ja esittää sen pullot.
     * Ei toimi etsimisen kanssa yhteistyössä.
     */
    private void lajitteleValmistajilla() {
        if (rekisteri.getPulloja() != 0 && ValmistajaLista != null) {
            hakuValitse.setText("Valmistaja");
            PulloLista.clear();
            Collection<Pullo> jarjestys = rekisteri.annaPullot(valmistajaKohdalla);
            int index = 0;
            for (Pullo pullo : jarjestys) {
                PulloLista.add(pullo.getNimi(), pullo);
            }
            PulloLista.setSelectedIndex(index);           
        }
    }
    
    
    /**
     * Hakee pullon pullolistasta (listchooser) oman id:n perusteella.
     * @param id Pullon rekisteröity tunnus, jolla haetaan
     */
    private void haeP(int id) {                  
        PulloLista.clear();
        int index = 0;
        for (int i = 0; i < rekisteri.getPulloja(); i++) {
            Pullo pullo = rekisteri.annaPullo(i);
            if (pullo.getTunnus() == id) index = i;
            PulloLista.add(pullo.getNimi(), pullo);
        }
        PulloLista.setSelectedIndex(index); // valitsee pullon
        if (lajiteltu() > 0) lajittelePullot(); //lajittelee, jos tämä toiminto on valittu
        if (lajiteltu() == 5) lajitteleValmistajilla();
    }
    
    
    /**
     * Hakee valmistajan valmistajalistasta (listchooser) oman id:n perusteella.
     * Testauksen ja havainnollistamisen vuoksi tulostaa listaan nimen perään myös tunnusnumeron.
     * Osaa myös lajitella valmistajat aakkosjärjsetykseen ja oletusasetukseen, mikäli käyttäjä näin haluaa.
     * @param id Valmistajan rekisteröity tunnus, jolla haetaan
     */
    private void haeV(int id) {
        if ( valmistajatAakkosissa == true) {
            if (rekisteri.getValmistajia() >= 2) { //ei lajittele, jos valmistajia on liian vähän
                ValmistajaLista.clear();
                ArrayList<Valmistaja> jarjestys = rekisteri.lajitteleValmistajat(); //lajittelee aakkosjärjestykseen
                int index = 0;                                    
                for (Valmistaja valmistaja : jarjestys) {            
                    ValmistajaLista.add(valmistaja.getNimi(), valmistaja);       
                }            
                ValmistajaLista.setSelectedIndex(index);
        }
        
        } if ( valmistajatAakkosissa == false) { //valmistajien oletusjärjestys           
            ValmistajaLista.clear();
            int index = 0;
            for (int i = 0; i < rekisteri.getValmistajia(); i++) {
                Valmistaja val = rekisteri.annaValmistaja(i);
                if (val.getId() == id) index = i;
                ValmistajaLista.add(val.getNimi(), val);
            }           
            ValmistajaLista.setSelectedIndex(index);
        }
    }
    
    
    /**
     * Yhdistetty id-tunnusnumero haku.
     * Jos pitää hakea molemmat.
     * @param v valmistajan id
     * @param p pullon id
     */
    private void hae(int p, int v) {
        haeP(p);
        haeV(v);
    }
    

    /**
     * Lisätään uusi pullo.
     * Ottaa valmistajan automaattisesti listasta valitun valmistajan perusteella.
     * Tietojen lisäysikkunassa (LisaysMuokkausController) näkyy, että mikä valmistaja on asetettu.
     */
    private void uusiPullo() {
        if (valmistajaKohdalla == null) {
            Dialogs.showMessageDialog("Valitse valmistaja listasta"); 
            return;
        }
        try {
        Pullo uusi = new Pullo();
        uusi.setValId(valmistajaKohdalla.getId()); 
        uusi = LisaysMuokkausController.kysyPullo(null, uusi, valmistajaKohdalla, null); //siirtyy uuteen ikkunaan
        if ( uusi == null ) return; //jos ei ole tehty muutoksia
        for (int i = 1; i < rekisteri.getPulloja(); i++) { //tarkistin samoille tunnusnumeroille
            if (rekisteri.tarkastaPtunnus(uusi.getTunnus()) == false) break;
            if (rekisteri.tarkastaPtunnus(uusi.getTunnus()) == true) {                
                uusi.setTunnus(i);
            }
        }
        rekisteri.lisaa(uusi);
        haeP(uusi.getTunnus());        
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + ex.getMessage());
            return;
        }        
        tallennettu = false;
    }
    
    
    /**
     * Näyttää pullon tiedot tekstikentässä.
     * Kutsuu tulostaPullo-aliohjelmaa, jossa tarkempia tietoja.
     */
    private void naytaPullo() {     
        pulloKohdalla = PulloLista.getSelectedObject();      
        if (pulloKohdalla == null) return;      
        areaPullo.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPullo)) {
            //pulloKohdalla.tulosta(os); tämä kutsuu pullo-luokan tulosta-metodia, jossa valmistajaa ei ole tekstimuodossa
            tulostaPullo(os, pulloKohdalla); // ^siksi teemme tämmöisen
        }    
    }
 
    
    /**
     * Pullojen tietojen tulostus tekstikenttään.
     * Käyttää lähinnä Pullo-luokan tulosta-metodia, mutta lisää myös valmistajan, joka löytyy rekisteristä.
     * ^koska Pullo-luokka ei tiedä valmistajiaan, on hoidettava valmistaja täällä käyttäen rekisteri-luokkaa.
     * @param os mihin tulostetaan
     * @param pullo pullo, joka tulostetaan
     */
    private void tulostaPullo(PrintStream os, Pullo pullo) {
        os.println("---");
        pullo.tulosta(os);        
        for (int i = 0; i < rekisteri.getValmistajia(); i++) {
            Valmistaja valmistaja = rekisteri.annaValmistaja(i);
            if(valmistaja != null) {
                if (valmistaja.getId() == pullo.getValId() ) { //jos täsmää          
                    os.println("Valmistaja: "+ valmistaja.getNimi());
                    break;
                }
                if (pullo.getValId() == 0) { //nollaa käytetään apulukuna: jos pullolle ei ole asetettu valmistajaa (ts. pullon valmistaja on poistettu)
                    os.println("Valmistajaa ei ole asetettu."+"\n"+"Aseta valitsemalla valmistaja,"+"\n"+"ja painamalla muokkaa-nappia");
                    break;
                }
            }
        }
        os.println("---");
    }
    
    /**
     * Yleinen tulostusmetodi.
     * Voidaan käyttää pienemmille viesteille, pop-upit ovat hieman ärsyttäviä.
     * Avustustila käyttää tätä kommunikointiin.
     * @param os mihin tulostetaan
     * @param viesti mitä tulostetaan
     */
    private void tulosta(String viesti) {
        if (avustusTila == true) {
            areaPullo.setText("---\n"+"Avustustila päällä\n");
        }
        areaPullo.appendText("---\n"+viesti+"\n---\n");
    }


    /**
     * Tällä metodilla lisätään valmistaja, sen tiedot esiintyvät listchooserissa.
     */
    private void uusiValmistaja() {
        try {
        Valmistaja uusi = new Valmistaja();                
        uusi = ValmistajaController.kysyValmistaja(null, uusi);
        if ( uusi == null ) return;
        String nimi = uusi.getNimi();
        for (int i = 1; i < rekisteri.getPulloja(); i++) { // tarkastaa tunnukset, ettei satu samaa
            if (rekisteri.tarkastaVtunnus(uusi.getId()) == false) break;
            if (rekisteri.tarkastaVtunnus(uusi.getId()) == true) {                
                uusi.setId(i);
            }
        }
        rekisteri.lisaa(uusi);
        ValmistajaLista.add(nimi, uusi); //tässä lisäys listaan
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + ex.getMessage());
            return;
        }        
        tallennettu = false;
    }
    
    
    /**
     * Tällä metodilla valitaan valmistaja listasta (listchooser).
     * Käytetään mm. poistaessa valittu valmistaja, tai pullon lisäyksessä ja muokkauksessa:
     * lisätessä pulloa sen valmistaja asetetaan tämän kautta,
     * muokattaessa pulloa sen valmistajan voi asettaa tämän kautta myös.
     */
    private void valitseValmistaja() {
       valmistajaKohdalla = ValmistajaLista.getSelectedObject();
    }
    

    /**
     * Lukee pullojen ja valmistajien tietoja tiedostoista.
     * Luetaan tallennuksessa tehty ht-kansioon tehdyn kansion "tiedostoja" tiedostot nimet.dat(pullot tekstitiedostona) ja valmistajat.dat(valmistajat vastaavana).
     * @param s Kansio nimeltä tiedostoja.
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedostot(String s) {
        
        try {
            rekisteri.lueTiedostosta(s);
            hae(0,0); // hakee kaikki tiedot listoihin
            return null;
        } catch (SailoException e) {
            hae(0,0);
            hae(0,0);
            String virhe = e.getMessage();
            if (virhe != null) Dialogs.showMessageDialog(virhe);
            return virhe;
        } 
        
    }

}