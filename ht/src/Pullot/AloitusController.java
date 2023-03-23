package Pullot;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Harri Keränen
 * @version 10.2.2020
 * tyo3 vaihe: lisätty aloituksen modaali controller.
 * Tekee kaksi asiaa:
 * - siirtymisen pääikkunaan (PullotGUIView.fxml)
 * - ohjelmasta poistuminen
 * Onko aloitusikkuna turha?
 * - mielestäni ei, koska se kertoo, että mistä tässä ohjelmassa on kyse.
 * tyo7 vaihe: dokumentaatio päivitetty.
 */
public class AloitusController implements ModalControllerInterface<String> {
           
    @FXML private Button AloitusPainike;
    @FXML private Button LopetusPainike;
    
    /**
     * Siirrytään pääsivulle.
     */
    @FXML void avaaOhjelma() {
        ModalController.closeStage(AloitusPainike);
    }
    

    /**
     * Metodipari sulkee ohjelman.
     * Poistu-metodi kutsuu Lopeta-metodia.
     * Lopeta-metodi kutsuu PullotGUIControllerin PoistuOhjelmasta-metodia, joka lopettaa ohjelman.
     * Lopeta-metodi sulkee myös Stagen samalla käyttäen lopeta-painiketta.
     */
    private void Lopeta() {
        PullotGUIController.PoistuOhjelmasta();
        ModalController.closeStage(LopetusPainike);
    }
    
    
    /**
     * Rasti ei toimi.
     * @return false
     */
    public boolean poistaRasti() {
        return false;
    }
    
    
    @FXML void Poistu() {
        Lopeta();
    } 
    
    
    //Nämähän ovat turhia, mutta niitä tarvitsee, että toimii oikein?
    @Override
    public String getResult() {
        return null;
    }
    

    @Override
    public void handleShown() {
        //
    }
    

    @Override
    public void setDefault(String oletus) {
        //
    }
    
}