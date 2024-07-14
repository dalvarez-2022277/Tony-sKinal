package org.diegoalvarez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.diegoalvarez.main.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
   
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void VentanaPresupuesto(){
        escenarioPrincipal.VentanaPresupuesto();
    }
    
    public void VentanaEmpleado(){
        escenarioPrincipal.VentanaEmpleado();
    }
}


