
package org.diegoalvarez.controller;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.diegoalvarez.bean.Empresa;
import org.diegoalvarez.bean.Presupuesto;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;
import org.diegoalvarez.report.GenerarReporte;

public class PresupuestoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO}
    private operaciones TipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
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
        fecha= new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/diegoalvarez/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        fecha.setAlignment(Pos.CENTER);
        DesactivarControles();
        cmbCodigoEmpresa.setItems(getEmpresa());
        cargarDatos();
    }

    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
    }
    
    public void seleccinarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }    
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado; 
    }
    
    public ObservableList<Presupuesto> getPresupuesto() {
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next() == true) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next() == true) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo (){
        switch (TipoDeOperacion){
            case NINGUNO:
                ActivarControles();
                LimpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image ("/org/diegoalvarez/image/Guardar.png"));
                imgEliminar.setImage(new Image ("/org/diegoalvarez/image/Cancelar.png"));
                TipoDeOperacion= operaciones.GUARDAR;
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
                TipoDeOperacion= operaciones.NINGUNO;
                break;
        }
    }
    
    public void Eliminar() {
        switch (TipoDeOperacion) {
            case GUARDAR:
                LimpiarControles();
                DesactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/diegoalvarez/image/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/diegoalvarez/image/eliminar.png"));
                TipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
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
        switch (TipoDeOperacion) {
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    ActivarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/diegoalvarez/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/diegoalvarez/image/Cancelar.png"));
                    TipoDeOperacion = operaciones.ACTUALIZAR;
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
                TipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void Guardar(){
        if(fecha.getSelectedDate() == null || txtCantidadPresupuesto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().isEmpty()){
        JOptionPane.showMessageDialog(null,"Los campos estan vacios");
        return;
    }
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
                System.out.println(registro.getCodigoPresupuesto());
                listaPresupuesto.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
     
    public void Actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?,?,?,?)");
            Presupuesto registro = (Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setFechaSolicitud(new java.sql.Date(registro.getFechaSolicitud().getTime()));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, (java.sql.Date) registro.getFechaSolicitud());
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte() {
        switch (TipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                LimpiarControles();
                DesactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/diegoalvarez/image/editar.png"));
                imgReporte.setImage(new Image("/org/diegoalvarez/image/reporte.png"));
                TipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
            GenerarReporte.MostrarReporte("ReportePresupuestos.jasper", "Reporte de Presupuesto", parametros);
    }
    
    public void ActivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(false);
        txtCantidadPresupuesto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void LimpiarControles(){
        txtCodigoPresupuesto.clear();
        fecha.selectedDateProperty().setValue(null);
        cmbCodigoEmpresa.getSelectionModel().select("");
        txtCantidadPresupuesto.clear();
    }
    
    public void DesactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(true);
        txtCantidadPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void PresupuestoMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresas();
    }
}

