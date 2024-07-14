
package org.diegoalvarez.bean;

public class Usuario {
    private int codigoUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String usuiaroLogin;
    private String contraseña;

    public Usuario() {
    }

    public Usuario(int codigoUsuario, String nombreUsuario, String apellidoUsuario, String usuiaroLogin, String contraseña) {
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.usuiaroLogin = usuiaroLogin;
        this.contraseña = contraseña;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getUsuiaroLogin() {
        return usuiaroLogin;
    }

    public void setUsuiaroLogin(String usuiaroLogin) {
        this.usuiaroLogin = usuiaroLogin;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    
}
