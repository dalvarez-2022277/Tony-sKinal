
package org.diegoalvarez.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.diegoalvarez.main.Principal;
import org.diegoalvarez.report.GenerarReporte;

public class ReporteController implements Initializable{
    private Principal escenarioPrincipal;
    @FXML private Button btnReporteEmpresa;
    @FXML private Button btnReportePresupuesto;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public void imprimirReporteEmpresa(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.MostrarReporte("ReportesEmpresas.jasper", "Reporte de Epresas", parametros);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void reportesMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();

}
}
