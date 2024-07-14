
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
import org.diegoalvarez.bean.Producto;
import org.diegoalvarez.db.Conexion;
import org.diegoalvarez.main.Principal;

public class ProductosController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCERLAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
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
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("Cantidad"));
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProducto");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
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
           // if (tblProductos.getSelectionModel().getSelectedItem()!=null){
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
           // }else{JOptionPane.showMessageDialog(null,"Debe Ingresar Datos");}
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
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
            if (tblProductos.getSelectionModel().getSelectedItem()!=null){
                int repuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?","Eliminar Producto",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(repuesta == JOptionPane.YES_OPTION){
               try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
               procedimiento.setInt(1,((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
               procedimiento.execute();
               listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPrdocuto(?,?,?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText())); 
            procedimiento.setInt(1,registro.getCodigoProducto());
            procedimiento.setString(2,registro.getNombreProducto());
            procedimiento.setInt(3,registro.getCantidad());
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
        Producto registro = new Producto ();
        if (txtCodigoProducto.getText().isEmpty() || txtCantidad.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }else{
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            if (!registro.getNombreProducto().equals("") && !txtCantidad.getText().equals("")) {
                 try{
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?,?,?)");
                    procedimiento.setInt(1,registro.getCodigoProducto());
                    procedimiento.setString(2,registro.getNombreProducto());
                    procedimiento.setInt(3,registro.getCantidad());
                    procedimiento.execute();
                    listaProducto.add(registro);
                  }catch (Exception e){
                        e.printStackTrace();
                  }
            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");
            }
        }
    }
    

    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);

    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidad.clear();

    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ProductoMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
