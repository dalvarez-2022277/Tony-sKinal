
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
import org.diegoalvarez.bean.TipoEmpleado;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;

public class TipoEmpleadoController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCERLAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaEmpresa;

    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcionTipoEmpleado;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcionTipoEmpleado;
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
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("descripcion"));
     
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
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
            // if (tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
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
                //}else{
               // JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
            //}
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcionTipoEmpleado.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());

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
            if (tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
                int repuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?","Eliminar TipoEmpleado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(repuesta == JOptionPane.YES_OPTION){
               try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
               procedimiento.setInt(1,((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
               procedimiento.execute();
               listaEmpresa.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
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
                if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1,registro.getCodigoTipoEmpleado());
            procedimiento.setString(2,registro.getDescripcion());
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
        TipoEmpleado registro = new TipoEmpleado ();
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
        if(!registro.getDescripcion().equals("")){        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1,registro.getDescripcion());

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
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);

    }
    
    public void limpiarControles (){
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();

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
