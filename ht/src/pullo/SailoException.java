/**
 * 
 */
package pullo;

/**
 * @author Harri Keränen
 * @version 4.3.2020
 * tyo5 vaihe: Poikkeustapaus, joita ei voida käsitellä.
 */
public class SailoException extends Exception {

    private static final long serialVersionUID = 1L;

/**
 * @param viesti virheilmoituksia
 */
    public SailoException(String viesti) {
        super(viesti);
    }
}
