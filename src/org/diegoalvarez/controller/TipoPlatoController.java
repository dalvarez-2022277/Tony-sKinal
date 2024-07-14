
package org.diegoalvarez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegoalvarez.bean.TipoPlato;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;

public class TipoPlatoController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCERLAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaEmpresa;

    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("descripcionTipo"));
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoplato");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo (){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Guardar.png"));
                imgEliminar.setImage(new Image ("/org/diegoalvarez/image/Cancelar.png"));
                tipoDeOperacion= operaciones.GUARDAR;
                break;
            case GUARDAR:
                // if (tblTipoPlato.getSelectionModel().getSelectedItem()!=null){
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
                tipoDeOperacion= operaciones.NINGUNO;
                cargarDatos();
                break;
                // }else{
                // JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");}
        // }
    }
    }
    public void seleccionarElemento(){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipoPlato.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());

    }   
    
    
    public void eiliminar(){
    switch(tipoDeOperacion){
        case GUARDAR:
            limpiarControles();
            desactivarControles();
            btnNuevo.setText("Nuevo");
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Nuevo.png"));
            imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
            tipoDeOperacion= operaciones.NINGUNO;
            break;
        default:
            if (tblTipoPlato.getSelectionModel().getSelectedItem()!=null){
                int repuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?","Eliminar TipoPlato",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(repuesta == JOptionPane.YES_OPTION){
               try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
               procedimiento.setInt(1,((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
               procedimiento.execute();
               listaEmpresa.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
               limpiarControles();
               }catch(Exception e){
                   e.printStackTrace();
                   
               } 
                }
            }else{
            JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
            }
        }
    }
    
    public void editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() !=null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image ("/org/diegoalvarez/image/Actualizar.png"));
                    imgReporte.setImage(new Image ("/org/diegoalvarez/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion= operaciones.ACTUALIZAR;               
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image ("/org/diegoalvarez/image/editar.png"));
                imgReporte.setImage(new Image ("/org/diegoalvarez/image/reporte.png"));
                cargarDatos();
                tipoDeOperacion= operaciones.NINGUNO;
        }   
        
        
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
            procedimiento.setInt(1,registro.getCodigoTipoPlato());
            procedimiento.setString(2,registro.getDescripcionTipo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
            
    public void reporte(){
        switch (tipoDeOperacion){
            case ACTUALIZAR:
                 actualizar();
                 limpiarControles();
                 desactivarControles();
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 imgEditar.setImage(new Image("/org/diegoalvarez/image/editar.png"));
                 imgReporte.setImage(new Image("/org/diegoalvarez/image/reporte.png"));
                 cargarDatos();
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
        }
    }
    
    public void guardar(){
        TipoPlato registro = new TipoPlato ();
        registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
        if(!registro.getDescripcionTipo().equals("")){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
            procedimiento.setString(1,registro.getDescripcionTipo());
            procedimiento.execute();
            listaEmpresa.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        }else{
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);

    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);

    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();

    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void tipoEmpleadoMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}

