
package org.diegoalvarez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.diegoalvarez.bean.Empresa;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;
import org.diegoalvarez.report.GenerarReporte;

public class EmpresasController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCERLAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;

    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
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
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("telefono"));
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
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
            //if (tblEmpresas.getSelectionModel().getSelectedItem()!=null){
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
            //}else{JOptionPane.showMessageDialog(null,"Debe Ingresar Datos");
           // }
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
    }   
    
    
    public void eiliminar(){
    switch(tipoDeOperacion){
        case GUARDAR:
            limpiarControles();
            desactivarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Nuevo.png"));
            imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
            tipoDeOperacion= operaciones.NINGUNO;
            break;
        default:
            if (tblEmpresas.getSelectionModel().getSelectedItem()!=null){
                JOptionPane.showMessageDialog(null,"Esto puede que elimine datos de otra entidad");
                int repuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(repuesta == JOptionPane.YES_OPTION){
               try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
               procedimiento.setInt(1,((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
               procedimiento.execute();
               listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
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
                if(tblEmpresas.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            procedimiento.setInt(1,registro.getCodigoEmpresa());
            procedimiento.setString(2,registro.getNombreEmpresa());
            procedimiento.setString(3,registro.getDireccion());
            procedimiento.setString(4,registro.getTelefono());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
            
    public void reporte(){
        switch (tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
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
        Empresa registro = new Empresa ();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        if (!registro.getDireccion().equals("") && !registro.getNombreEmpresa().equals("")
                && !registro.getTelefono().equals("")) {
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?,?,?)");
            procedimiento.setString(1,registro.getNombreEmpresa());
            procedimiento.setString(2,registro.getDireccion());
            procedimiento.setString(3,registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        }catch (Exception e){   
            e.printStackTrace();
        }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    } 
    
    public void imprimirReporte(){
        URL NUEVA_IMAGEN1 = this.getClass().getResource("/org/diegoalvarez/image/LogoPrincipal.png");
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("NUEVA_IMAGEN1", NUEVA_IMAGEN1);
        GenerarReporte.MostrarReporte("ReporteEmpresaImg.jasper", "Reporte de Epresas", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void empresasMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.VentanaPresupuesto();
    }
}
