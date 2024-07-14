
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.diegoalvarez.bean.Empleado;
import org.diegoalvarez.bean.TipoEmpleado;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;

public class EmpleadosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoEmpleado;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
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
        DesactivarControles();
        cmbCodigoTipoEmpleado.setItems(getTipoeEmpleado());
        cargarDatos();
    }
   
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("numeroEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,String>("apellidosEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("direccionEmpleado"));
        colTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado,Integer>("codigoTipoEmpleado"));
    }

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next() == true) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    
    public void seleccinarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado()));
        txtNombreEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
        txtDireccionEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
        txtTelefonoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        txtGradoCocinero.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero()));
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    }   
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                        registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado; 
    }
    
    public ObservableList<TipoEmpleado> getTipoeEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next() == true) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
        public void Nuevo (){
        switch (tipoDeOperacion){
            case NINGUNO:
                ActivarControles();
                LimpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Guardar.png"));
                imgEliminar.setImage(new Image ("/org/diegoalvarez/image/Cancelar.png"));
                tipoDeOperacion= operaciones.GUARDAR;
                break;
            case GUARDAR:
                Guardar();
                cargarDatos();
                LimpiarControles();
                DesactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
                tipoDeOperacion= operaciones.NINGUNO;
                break;
        }
    }
        
        public void Guardar() {
            if (txtNumeroEmpleado.getText().isEmpty() || txtApellidoEmpleado.getText().isEmpty() ||
                    txtNombreEmpleado.getText().isEmpty() || txtDireccionEmpleado.getText().isEmpty() ||
                    txtTelefonoEmpleado.getText().isEmpty() || txtGradoCocinero.getText().isEmpty() ||
                    cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Error: Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Empleado registro = new Empleado();
                registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
                registro.setNombresEmpleado(txtNombreEmpleado.getText());
                registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
                registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
                registro.setGradoCocinero(txtGradoCocinero.getText());
                registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());

                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
                    procedimiento.setInt(1, registro.getNumeroEmpleado());
                    procedimiento.setString(2, registro.getApellidosEmpleado());
                    procedimiento.setString(3, registro.getNombresEmpleado());
                    procedimiento.setString(4, registro.getDireccionEmpleado());
                    procedimiento.setString(5, registro.getTelefonoContacto());
                    procedimiento.setString(6, registro.getGradoCocinero());
                    procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
                    procedimiento.execute();
                    listaEmpleado.add(registro);
                    JOptionPane.showMessageDialog(null, "Empleado guardado correctamente.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    
    public void Eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                LimpiarControles();
                DesactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/diegoalvarez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Empleado?", "Eliminar EliminarEmpleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            LimpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
        }
    }   
              
    public void Editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    ActivarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/diegoalvarez/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/diegoalvarez/image/Cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                }
                break;

            case ACTUALIZAR:
                Actualizar();
                LimpiarControles();
                DesactivarControles();
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

    public void Actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?,?)");
            Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, Integer.parseInt(txtNumeroEmpleado.getText()));
            procedimiento.setString(3, txtApellidoEmpleado.getText());
            procedimiento.setString(4, txtNombreEmpleado.getText());
            procedimiento.setString(5, txtDireccionEmpleado.getText());
            procedimiento.setString(6, txtTelefonoEmpleado.getText());
            procedimiento.setString(7, txtGradoCocinero.getText());
            procedimiento.setInt(8, ((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
     public void Reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                LimpiarControles();
                DesactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/diegoalvarez/image/editar.png"));
                imgReporte.setImage(new Image("/org/diegoalvarez/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }   
        
    public void ActivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
        public void LimpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoEmpleado.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.getSelectionModel().select("");
    }
        
     public void DesactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoEmpleado.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEsenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void EmpleadoMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
