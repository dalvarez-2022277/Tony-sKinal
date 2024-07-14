/*
Diego Ricardo Alvarez Castañeda 
2022277
PE5AM 
Fecha de creación 12/4/2023 8:13 AM
Fecha de modificación 12/4/2023 5:28 PM
 */
package org.diegoalvarez.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.diegoalvarez.controller.EmpleadosController;
import org.diegoalvarez.controller.EmpresasController;
import org.diegoalvarez.controller.MenuPrincipalController;
import org.diegoalvarez.controller.PresupuestoController;
import org.diegoalvarez.controller.ProductosController;
import org.diegoalvarez.controller.ProgramadorController;
import org.diegoalvarez.controller.ReporteController;
import org.diegoalvarez.controller.TipoEmpleadoController;
import org.diegoalvarez.controller.TipoPlatoController;
import org.diegoalvarez.report.GenerarReporte;


/**
 *
 * @author informatica
 */ 
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/diegoalvarez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception{
        this.escenarioPrincipal  = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        this.escenarioPrincipal.setResizable(false);
        escenarioPrincipal.getIcons().add( new Image("/org/diegoalvarez/image/LogoSecundario.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/diegoalvarez/view/MenuPrincipalView.fxml"));
//       Parent root = FXMLLoader.load(getClass().getResource("/org/diegoalvarez/view/EmpresasView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/diegoalvarez/view/ProgramadorView.fxml"));
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
        menuPrincipal();
        escenarioPrincipal.show();
        
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",640,400);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
           ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",640,400);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaEmpresas(){
        try{
            EmpresasController empresas = (EmpresasController)cambiarEscena("EmpresasView.fxml",640,400);
            empresas.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try {
            ProductosController productos = (ProductosController) cambiarEscena("ProductosView.fxml",640,400);
            productos.setEscenarioPrincipal (this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
     public void ventanaTipoEmpleado(){
         try {
             TipoEmpleadoController tipoempleados = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",640,400);
             tipoempleados.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         } 
     }
     
     public void ventanaTipoPlato(){
         try {
             TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",640,400);
             tipoPlato.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void VentanaPresupuesto(){
         try{
             PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml",640,400);
             presupuesto.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void VentanaEmpleado(){
         try{
             EmpleadosController empleado = (EmpleadosController) cambiarEscena("EmpleadosView.fxml",772,400);
             empleado.setEsenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void ventanaReporte(){
         try{
             ReporteController reporte = (ReporteController) cambiarEscena("ReportesView.fxml",640,400);
             reporte.setEscenarioPrincipal(this);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
    
}
