package Pullot;

import java.net.URL;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pullo.Pullo;
import pullo.Valmistaja;

/**
 * @author Harri Keränen
 * @version 16.4.2020
 * tyo3 vaihe: controller pullojen lisäykselle ja muokkaukselle tehty
 * tähän ikkunaan päästään, kun lisätään tai muokataan pullon tiedot.
 * Poista valmistaja toimii seuraavasti:
 * jos valmistaja poistetaan se poistuu pullon tiedoista, uusi valmistaja voidaan lisätä ja asettaa muokkaa pulloa-toiminnolla uudestaan (kts. controllerin dokumentaatio).
 * tyo7 vaihe: controller päivitetty: voidaan lisätä pullo ja muokata sen tietoja tällä samalla ikkunalla ja controllerilla.
 * NEW PLAN: valmistaja valitaan listasta, ja sitten päästään tähän ikkunaan, valmistaja määräytyy näin helpommin.
 * Ruksin toiminta korjattu: nyt tekee täysin saman kuin "Peruuta"-nappi pienellä boolean-kikkailulla
 * Lisätty parempi oikeellisuustarkistus: nimi on aina pakko kirjoittaa, ja alkoholiprosentin kirjoittaminen on nyt tehty paremmaksi
 */
public class LisaysMuokkausController implements ModalControllerInterface<Pullo>, Initializable {

    @FXML private TextField NimiKentta;
    @FXML private TextField AlkoholiKentta;
    @FXML private TextField MaaKentta;
    @FXML private TextField TyyppiKentta;
    @FXML private TextField ValmistajaKentta;
    @FXML private Button ButtonTallenna;
    @FXML private Button ButtonPeruuta;
    @FXML private Label labelVirhe;
    
    
    @FXML void Peruuta() {
        sulje();        
    }


    @FXML void TallennaPullo() {       
        if (pulloKohdalla != null) {            
            if (tarkastaPullo() == false) {
                return;
            }
        }
        voiSulkea = true;
        ModalController.closeStage(ButtonTallenna);
    }        
    
    
//==========================================================
    private TextField edits[];
    private Pullo pulloKohdalla;
    private static Valmistaja valmistajaKohdalla;
    private static Valmistaja valmistajaNykyinen;
    private boolean muutettu = false;
    private boolean voiSulkea = false;
    
    /**
     * Alustetaan
     */
    protected void alusta() {
        edits = new TextField[] {NimiKentta, AlkoholiKentta, MaaKentta, TyyppiKentta};
        int i = 0;
        labelVirhe.setText("");
        for (TextField edit : edits) {
            final int k = ++i;
            if ( edit != null ) {
                edit.setOnKeyReleased( e -> kasitteleMuutos(k, (TextField)(e.getSource())));
            }    
        }       
    }
    
    
    /**
     * Tarkastaa, että ovatko pullon tiedot oikein.
     * Nimi on aina pakko kirjoittaa dialogiin.
     * Keskittyy pääosin alkoholiprosentin (double-desimaaliluvun) asettamiseen oikein tarkastaProsentti()-funktion avulla
     * @return true, jos pullon tiedot on syötetty oikein, false jos ei
     */
    private boolean tarkastaPullo() {
        if ( pulloKohdalla != null ) {
            if (NimiKentta.getText() == null || pulloKohdalla.getNimi().trim().equals("")) {
                naytaVirhe("Pullolle täytyy antaa nimi");
                return false;
            }                        
            
            if (AlkoholiKentta.getText().isEmpty()) {
                AlkoholiKentta.setText("0.0");
            }                        
                    
            if (!tarkastaProsentti(AlkoholiKentta.getText())) {                
                return false;
            }                        
        }
        return true;
    }
    
    
    /**
     * Tarkastaa, että onko alkoholiprosentti kirjoitettu oikein.
     * @param jono Tekstikentän merkkijono, jota tarkastetaan
     * @return true jos alkoholiprosentti on kirjoitettu oikein, false jos väärin
     */
    public boolean tarkastaProsentti(String jono) {
        
        if (jono.length() >= 5 || pulloKohdalla.getProsentti() > 100.0) {
            naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Alkoholiprosentti ei voi olla enemmän kuin 100 ja voi sisältää vain yhden desimaalin");
            return false;
        }
                
        
        for (int i = 0; i < jono.length(); i++) {
            if (jono.charAt(i) == ',') {
                naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Käytä pistettä desimaalin asetukseen");
                return false;
            }
            
            if (Character.isLetter(jono.charAt(i))) {
                naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Käytä numeroita kirjaimien sijaan");
                return false;
            }                       
            
        }
        
        if (jono.length() > 1) {
            if (jono.charAt(1) == '.' && jono.length() == 4) {
                naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Alkoholiprosentti voi sisältää vain yhden desimaalin");
                return false;
            }
        }
        
        if (!Character.isDigit(jono.charAt(0))) {
            naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Aloita numerolla");
            return false;
        }
        
        if (!Character.isDigit(jono.charAt(jono.length()-1))) {
            naytaVirhe("Virhe alkoholiprosentin asetuksessa:"+"\n"+"Aseta numero viimeiseksi");
            return false;
        }
        
        return true;
    }
    
    
    @Override
    public void setDefault(Pullo oletus) {
            pulloKohdalla = oletus;
            naytaPullo(edits, pulloKohdalla);
            
            if (pulloKohdalla.getValId() == 0 || pulloKohdalla.getValId() == valmistajaKohdalla.getId() ) {
                ValmistajaKentta.setText(valmistajaKohdalla.getNimi());
            } else if (valmistajaNykyinen != null && valmistajaNykyinen.getId() != 0) {
                ValmistajaKentta.setText(valmistajaNykyinen.getNimi());
            }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }    


    @Override
    public Pullo getResult() {
        if (muutettu == false) {
            return null;
        }
        
        if (voiSulkea == true && muutettu == true) {            
            return pulloKohdalla;
        }
        return null;
    }
    
    
    private void sulje() {
        if ( muutettu == false) {
            pulloKohdalla = null;
            ModalController.closeStage(ButtonPeruuta);
        }
        
        if (muutettu == true) {
            muutettu = false;
            ModalController.closeStage(ButtonPeruuta);
            }
    }


    @Override
    public void handleShown() {
        NimiKentta.requestFocus();      
    }
    
    
    private void naytaVirhe(String virhe) {
        labelVirhe.setText(virhe);
    }
    
    
    /**
     * Käsitellään tapahtuneet muutokset.
     * @param k Kenttä
     * @param edit Muutokset
     */
    private void kasitteleMuutos(int k, TextField edit) {
        try {
            if (pulloKohdalla == null) return;
            String s = edit.getText();
            String virhe = null;
            switch (k) {
            case 1: virhe = pulloKohdalla.setNimi(s); break;                 
            case 2: virhe = String.valueOf(pulloKohdalla.setProsentti(Double.valueOf(s))); break;
            case 3: virhe = pulloKohdalla.setTyyppi(s); break;
            case 4: virhe = pulloKohdalla.setMaa(s); break;
            default:
            }
            
            if (virhe != null) {
                Dialogs.setToolTipText(edit, "");
                edit.getStyleClass().removeAll("virhe");            
            } else {
                Dialogs.setToolTipText(edit, "virhe");
                edit.getStylesheets().add("virhe");
                
            }
            muutettu = true;
        } catch (NumberFormatException e) {
            //ei tehdä mitään, ettei desimaalipisteen laitosta nillitetä turhaan
        }
    }
    
    
    /**
     * Asettaa tekstin kenttään.
     * @param edits Muutokset
     * @param pullo Pullo jota on muutettu
     */
    public static void naytaPullo(TextField[] edits, Pullo pullo) {
        if (pullo == null) return;
        edits[0].setText(pullo.getNimi());
        edits[1].setText(String.valueOf(pullo.getProsentti()));
        edits[2].setText(pullo.getTyyppi());
        edits[3].setText(pullo.getMaa());
    }
    
    
    /**
     * Toimii ikään kuin mainina.
     * @param modalityStage Tämä stage
     * @param oletus Oletuspullo
     * @param valmistajaK Valittu valmistaja
     * @param valmistajaN Jo asetettu valmistaja
     * @return Lisättävä tai muokattava pullo
     */
    public static Pullo kysyPullo(Stage modalityStage, Pullo oletus, Valmistaja valmistajaK, Valmistaja valmistajaN) {
        valmistajaKohdalla = valmistajaK;
        valmistajaNykyinen = valmistajaN;
        return ModalController.<Pullo, LisaysMuokkausController>showModal(
                LisaysMuokkausController.class.getResource("PullotGUIMuokkaa.fxml"),
                "Rekisteri",modalityStage,oletus,null);
    }

}