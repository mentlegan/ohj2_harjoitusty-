package Pullot;

import javafx.application.Application;
import javafx.stage.Stage;
import pullo.Rekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author Harri Keränen
 * @version 22.4.2020
 * Koko ht:n rivimäärä: 3104 hui
 * tyo3 vaihe: ohjelma avaa aloitussivun(PullotGUIAloitus.fxml) ja pääikkunan (PullotGUIView).
 * tyo5 vaihe: main ei ole muuttunut, muutokset tietorakenteissa ja controllerissa.
 * tyo6 vaihe: ruksin toiminnat poistettu/yhdistetty tallennukseen (varmistaa sulkiessa, että halutaanko tiedot tallentaa ennen sulkemista).
 * tyo7 vaihe: turhia controllereita ja fxml-tiedostoja poistettu (mm. pullojen poiston vastaavat tiedostot).
 * Olemassa olevia fxml-tiedostoja (varsinkin pääikkunan PullotGUIView) muokattu paremmiksi.
 * tyoX vaihe: ohjelma otettu käyttöön
 */
public class PullotMain extends Application {
    /**
     * @param primaryStage Pääsivu
     */
    @Override
    public void start(Stage primaryStage) {
        try {     
            
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("PullotGUIView.fxml"));
            final Pane root = ldr.load();
            final PullotGUIController pullotCtrl = (PullotGUIController) ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("pullot.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Pullot");
            
            primaryStage.setOnCloseRequest((event) -> {
                if (!pullotCtrl.Poistu() ) event.consume();
            });
                      
            Rekisteri rekisteri = new Rekisteri();
            pullotCtrl.setRekisteri(rekisteri);
                        
            primaryStage.show();
            
            Application.Parameters params = getParameters();
            if (params.getRaw().size() > 0)
                pullotCtrl.lueTiedostot(params.getRaw().get(0));
            
            //tässä tehdään aloitusikkuna
            Stage alku = new Stage();
            FXMLLoader ldr2 = new FXMLLoader(getClass().getResource("PullotGUIAloitus.fxml"));
            Pane root2 = ldr2.load();
            AloitusController aloitusCtrl = (AloitusController) ldr2.getController();
            Scene aloitus = new Scene(root2);
            alku.setScene(aloitus);
            alku.setOnCloseRequest((event) -> {
                if (!aloitusCtrl.poistaRasti() ) event.consume();
            });
            Startti(alku); //avaa aloitusikkunan
            
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Avaa aloitusikkunan käynnistäessä ohjelman.
     * Miksi? Käyttäjä tietää, että minkä ohjelman hän on avannut.
     * @param aloitus aloitusikkuna
     */
    public void Startti(Stage aloitus) {       
              aloitus.show();
    }
    
    
    /**
     * @param args nope
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}