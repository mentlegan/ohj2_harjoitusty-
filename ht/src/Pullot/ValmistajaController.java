package Pullot;

import java.net.URL;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pullo.Valmistaja;
import javafx.fxml.Initializable;

/**
 * @author Harri Keränen
 * @version 14.4.2020
 * tyo3 vaihe: lisätty controller valmistajan lisäystä varten pientietokantaan
 * tyo7 vaihe: valmistaja voidaan nyt lisätä antamalla tähän tietoja
 * Ruksin toiminta korjattu: nyt tekee täysin saman kuin "Peruuta"-nappi pienellä boolean-kikkailulla
 */
public class ValmistajaController implements ModalControllerInterface<Valmistaja>, Initializable {
    
    @FXML private TextField ValmistajaText;
    @FXML private Button ButtonTallenna;
    @FXML private Button ButtonPeruuta;

    
    @FXML
    void Peruuta() {
        sulje();        
    }

    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    
    @FXML
    void TallennaValmistaja() {
        if ( valmistajaKohdalla != null && valmistajaKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Valmistajalle pitää antaa nimi");
            return;
        }
        voiSulkea = true;
        ModalController.closeStage(ButtonTallenna);
    }
    

    @Override
    public void handleShown() {
        ValmistajaText.requestFocus();
    }

    
//======================================================================
    
    private Valmistaja valmistajaKohdalla;
    private TextField edits[];
    private boolean muutettu = false;
    private boolean voiSulkea = false;
    
    
    /**
     * Alustaa ikkunan.
     */
    protected void alusta() {
        edits = new TextField[] {ValmistajaText};
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            if ( edit != null ) {
                edit.setOnKeyReleased( e -> kasitteleMuutos(k, (TextField)(e.getSource())));                
            }    
        }           
    }
    
    
    /**
     * @return haetaan nimi
     */
    public String haeNimi() {
        return ValmistajaText.getText();
    }
    
    /**
     * Näyttää virheen, jos ongelmia.
     * @param string
     */
    private void naytaVirhe(String string) {
        Dialogs.showMessageDialog(string);
    }
    
    /**
     * Käsittelee muutokset.
     * @param k kenttä
     * @param edit muutokset
     */
    private void kasitteleMuutos(int k, TextField edit) {
        if (valmistajaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
        case 1: virhe = valmistajaKohdalla.setNimi(s); break;                 
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
    }

    
    @Override
    public Valmistaja getResult() {
        if (muutettu == false ) {            
            return null;
        }
        
        if (voiSulkea == true && muutettu == true) {            
            return valmistajaKohdalla;
        }
        return null;
    }
    
    /**
     * Sulkeminen.
     */
    private void sulje() {
        if ( muutettu == false) {
            ModalController.closeStage(ButtonPeruuta);
        }
        
        if (muutettu == true) {
            muutettu = false;
            ModalController.closeStage(ButtonPeruuta);
        }    
    }

    
    @Override
    public void setDefault(Valmistaja oletus) {
        valmistajaKohdalla = oletus;
        naytaValmistaja(edits, valmistajaKohdalla);     
    }

    
    /**
     * @param edits muutokset
     * @param valmistaja valmistaja, jota lisätään
     */
    public static void naytaValmistaja(TextField[] edits, Valmistaja valmistaja) {
        if (valmistaja == null) return;
        edits[0].setText(valmistaja.getNimi());
    }
    
    
    /**
     * @param modalityStage tämä stage
     * @param oletus oletus
     * @return tämä ikkuna
     */
    public static Valmistaja kysyValmistaja(Stage modalityStage, Valmistaja oletus) {
        return ModalController.<Valmistaja, ValmistajaController>showModal(
                LisaysMuokkausController.class.getResource("PullotGUIValmistaja.fxml"),
                "Lisää Valmistaja",modalityStage,oletus,null);
    }
    
}