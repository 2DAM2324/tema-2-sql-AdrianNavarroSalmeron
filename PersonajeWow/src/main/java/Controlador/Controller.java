/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.*;
import Vista.Ventana1;
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.tools.DocumentationTool;

//***************Imports para escribir y leer xml *******************************

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

/******************************************

/**
 *
 * @author AdrianNS
 */
public class Controller {
    private Inventario inventario;
    private Objeto objeto;
    private Personaje personaje;
    private Hermandad hermandad;
    private Conector conector;
    private Ventana1 ventana;
    public ArrayList <Personaje> ArrayDePersonajesSistema;
    private ArrayList <Hermandad> ArrayDeHermandadesSistema;
    private ArrayList <Objeto> ArrayDeObjetosSistema;
    private ArrayList<Inventario> ArrayDeInventariosSistema;
    private Ventana1 vista;
    
   
    public Controller(Ventana1 vistaSet){
        ArrayList<Personaje> nuevoArray = new ArrayList<>();
        ArrayList<Objeto> arrayObjeto = new ArrayList<>();
        ArrayList<Inventario> arrayInventario = new ArrayList<>();
        ArrayList<Hermandad> arrayHermandad = new ArrayList<>();
        this.vista = vistaSet;
        this.ArrayDePersonajesSistema = nuevoArray;
        this.ArrayDeObjetosSistema = arrayObjeto;
        this.ArrayDeHermandadesSistema = arrayHermandad;
        this.ArrayDeInventariosSistema = arrayInventario;
        Conector conector = new Conector();
        this.conector = conector;
        conector.crearBaseDatos();
        
        
       // leerXMLObjetos();
       conector.leerObjetosDeBd(ArrayDeObjetosSistema);
        leerInventarioSistema();   
        leerXMLPersonajes();
        leerXMLHermandades();
        cargarObjetoEnTabla(ArrayDeObjetosSistema);
        cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
        cargarPersonajesEnTabla(ArrayDePersonajesSistema);
        cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
    }
    
    /**
    public Controller(){
        ArrayList<Personaje> nuevoArray = new ArrayList<>();
        ArrayList<Objeto> arrayObjeto = new ArrayList<>();
        ArrayList<Inventario> arrayInventario = new ArrayList<>();
        ArrayList<Hermandad> arrayHermandad = new ArrayList<>();
        this.ArrayDePersonajesSistema = nuevoArray;
        this.ArrayDeObjetosSistema = arrayObjeto;
        this.ArrayDeHermandadesSistema = arrayHermandad;
        this.ArrayDeInventariosSistema = arrayInventario;
    }
    * */
    
    public void setArrayDePersonajesDeSistema(ArrayList<Personaje> array){
        ArrayDePersonajesSistema = array;
    }
    
    public ArrayList<Personaje> getArrayDePersonajesDeSistema(){
        return ArrayDePersonajesSistema;
    }
    
    public void setArrayDeHermandadesSistema( ArrayList<Hermandad> array){
        ArrayDeHermandadesSistema = array;
    }
    
    public ArrayList<Hermandad> getArrayDeHermandadesSistema(){
        return ArrayDeHermandadesSistema;
    }
    
    public void setArrayDeObjetosSistema(ArrayList<Objeto> array){
        ArrayDeObjetosSistema = array;
    }
    
    public ArrayList<Objeto> getArrayDeObjetosSistema(){
        return ArrayDeObjetosSistema;
    }
    
    public void setArrayDeInventariosSistema(ArrayList<Inventario> array){
        ArrayDeInventariosSistema = array;
    }
    
    public ArrayList<Inventario> getArrayDeInventariosSistema(){
        return ArrayDeInventariosSistema;
    }
    //Funciones de los objetos******************************************************************************************
    public void aniadirObjeto(String nombreObjeto, String rareza, String precio, String descipcion, String IdObjeto){
        Objeto objeto = new Objeto();
        try{
            if(nombreObjeto != null && rareza != null && precio != null && descipcion != null){
                    //Eliminamos los espacios en blanco
                    nombreObjeto = nombreObjeto.trim();
                    rareza = rareza.trim();
                    descipcion= descipcion.trim();
                    //Filtro para saber si el precio es mayor que 0
                    double precioParseado = Double.parseDouble(precio);
                    if(precioParseado >= 1){
                        objeto.setNombreObjeto(nombreObjeto);
                        objeto.setRareza(rareza);
                        objeto.setDescripcion(descipcion);
                        objeto.setPrecio(precioParseado);
                        ArrayDeObjetosSistema.add(objeto);
                        conector.insertarObjetoEnBd(objeto);
                        escribirXMLObjetos(ArrayDeObjetosSistema);
                        cargarObjetoEnTabla(ArrayDeObjetosSistema);
                        JOptionPane.showMessageDialog(vista, "Objeto añadido con exito", "OK", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(vista, "El precio debe ser mayor que 0", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }     
            }
            else{
                JOptionPane.showMessageDialog(vista, "No puede haber campos vacios!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
         catch(NumberFormatException formatoIncorrecto){
            JOptionPane.showMessageDialog(ventana, "El precio debe de ser un numero", "Error", JOptionPane.ERROR_MESSAGE);;
        }
        catch(NullPointerException campoVacio){
            JOptionPane.showMessageDialog(ventana, "No puede haber campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //TODO los datos no se leen del xml despues de actualizarlos al borrar algo.
    public void borrarObjeto(String idObjetoaBorrar){
        Objeto objetoaBorrar = new Objeto();
         for(Objeto objeto : ArrayDeObjetosSistema){
             if(objeto.getIdObjeto().equals(idObjetoaBorrar)){
                 objetoaBorrar = objeto;
             }
         }
        for (Inventario inventario : getArrayDeInventariosSistema()) {
            if (inventario.getObjetosInventario().contains(objetoaBorrar)) {
                inventario.getObjetosInventario().remove(objetoaBorrar);
                inventario.setEspaciosOcupados(inventario.getObjetosInventario().size());
            }
        }
        conector.borrarObjetoDeBd(objetoaBorrar);
        ArrayDeObjetosSistema.remove(objetoaBorrar);
        escribirXMLObjetos(ArrayDeObjetosSistema);
        escribirXMLInventarios(ArrayDeInventariosSistema);
        escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
        cargarObjetoEnTabla(ArrayDeObjetosSistema);
        cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
    }

    public void borrarObjetoInventario(String idObjeto, String idInventario){
        int posicionObjeto = getPosicionObjetoById(idObjeto);
        int posicionInventario = buscarInventarioPorId(idInventario);
        if(posicionObjeto != -1 && posicionInventario != -1){
            if(getArrayDeInventariosSistema().get(posicionInventario).comprobarSiObjetoEnInventario(getObjetoById(idObjeto))){
                getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().remove(getObjetoById(idObjeto));
                getArrayDeInventariosSistema().get(posicionInventario).setEspaciosOcupados(getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().size());
                escribirXMLInventarios(ArrayDeInventariosSistema);
                escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                cargarPersonajesEnTabla(ArrayDePersonajesSistema);
                cargarInventarioPersonajeEnTabla(idInventario);
                JOptionPane.showMessageDialog(vista, "Objeto borrado correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(vista, "El objeto no existe en el inventario", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            System.out.println("No se ha encontrado el objeto o el inventario");
        }
    }
    
    public Objeto getObjetoById(String id){
        for(Objeto objeto : ArrayDeObjetosSistema){
            if(objeto.getIdObjeto().equals(id)){
                return objeto;
            }
        }
        return null;
    }
    
     public int getPosicionObjetoById(String id){
         int posicion = -1;
        for(Objeto objeto : ArrayDeObjetosSistema){
            if(objeto.getIdObjeto().equals(id)){
                posicion = ArrayDeObjetosSistema.indexOf(objeto);
                
            }
        }
        return posicion;
     }
    
    public void modificarObjeto(String id, String nombre, String rareza, String precio, String descripcion){
        if(getPosicionObjetoById(id) !=-1){
            try{
                double precioParseado = Double.parseDouble(precio);
                if(getArrayDeObjetosSistema().get(getPosicionObjetoById(id)) != null && precioParseado >= 1){
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setNombreObjeto(nombre);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setRareza(rareza);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setPrecio(precioParseado);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setDescripcion(descripcion);
                    conector.modificarObjetoEnBd(getArrayDeObjetosSistema().get(getPosicionObjetoById(id)));
                    escribirXMLObjetos(ArrayDeObjetosSistema);
                    cargarObjetoEnTabla(ArrayDeObjetosSistema);
                    JOptionPane.showMessageDialog(vista, "Objeto modificado correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
                }
                 else{
                JOptionPane.showMessageDialog(vista, "El precio debe ser mayor que 0", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NumberFormatException formatoIncorrecto){
                JOptionPane.showMessageDialog(ventana, "El precio debe de ser un numero", "Error", JOptionPane.ERROR_MESSAGE);;
            }
            catch(NullPointerException campoVacio){
                JOptionPane.showMessageDialog(ventana, "No puede haber campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void cargarObjetoEnTabla(ArrayList<Objeto> ArrayDeObjetos) {
        DefaultTableModel model = (DefaultTableModel) vista.jTable_objeto_objeto.getModel();

        // Limpia la tabla 
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        // Llena el modelo con los datos de tu ArrayList
        for (Objeto objeto : ArrayDeObjetos) {
            Object[] rowData = {objeto.getIdObjeto(), objeto.getNombreObjeto(), objeto.getRareza(), objeto.getPrecio()};
            model.addRow(rowData);
        }

        // Asigna el modelo a la JTable
        vista.jTable_objeto_objeto.setModel(model);
    }
    //******************************************************************************************************************************************
    
    public void cargarInventariosSistmemaEnTabla(ArrayList<Inventario> arrayDeInventarios){
        DefaultTableModel model = (DefaultTableModel) vista.jTable_inventario_objetos.getModel();
        
        while(model.getRowCount()  > 0){
            model.removeRow(0);
        }
        
        for(Inventario inventario : arrayDeInventarios){
            Object[] rowData = {inventario.getIdInventario(), inventario.getIdPersonaje(), inventario.getEspaciosOcupados()};
            model.addRow(rowData);
        }
         vista.jTable_inventario_objetos.setModel(model);
    }
    
    
      public void cargarInventarioDetallesPersonajes(String nombrePersonaje, String servidorPersonaje){
          DefaultTableModel model = (DefaultTableModel) vista.jTable_detalles_inventario_personaje_personaje.getModel();
          int posicionPersonaje = buscarPersonajeEnSistema(nombrePersonaje, servidorPersonaje);
          while (model.getRowCount() > 0) {
              model.removeRow(0);
          }
         Inventario inventario = getArrayDePersonajesDeSistema().get(posicionPersonaje).getInventario();
        for (Objeto objeto : inventario.getObjetosInventario()) {
            Object[] rowData = {objeto.getIdObjeto(), objeto.getNombreObjeto(), objeto.getRareza(), objeto.getPrecio(), objeto.getDescripcion()};
            model.addRow(rowData);
        }
          vista.jTable_detalles_inventario_personaje_personaje.setModel(model);
    }
    
    public void setVista(Ventana1 vistaSet){
        vista = vistaSet;
    }
    
    public Ventana1 getVista(){
        return vista;
    }

    public void cargarInventarioPersonajeEnTabla(String idPersonaje){
        DefaultTableModel model = (DefaultTableModel) vista.jTable_inventario_personaje.getModel();
        while(model.getRowCount()  > 0){
            model.removeRow(0);
        }
        
        for(Inventario inventario : getArrayDeInventariosSistema()){
            if(inventario.getIdPersonaje().equals(idPersonaje)){
                for(Objeto objeto : inventario.getObjetosInventario()){
                    Object[] rowData = {objeto.getIdObjeto(), objeto.getNombreObjeto(), objeto.getRareza(), objeto.getPrecio()};
                    model.addRow(rowData);
                }
            }
        }
         vista.jTable_inventario_personaje.setModel(model);
    }
    
    //Agrega un personaje a una hermandad si hay espacio
    public void agregarUnPersonajeaHermandad(Personaje personaje, Hermandad hermandad){
            if(hermandad.getListaMiembros().size() <= 100){
                hermandad.getListaMiembros().add(personaje);
                hermandad.setNumeroMiembros(hermandad.getNumeroMiembros()+1);
            }
            else{
                System.out.println("La hermandad esta llena!");
            }
        }
    
    
    //*************************************FUNCIONES INVENTARIO****************************************************
    
    public void aniadirInventario( String nombrePersonaje, String servidorPersonaje ){
            int posicionPersonaje = buscarPersonajeEnSistema(nombrePersonaje, servidorPersonaje);
            
            if(posicionPersonaje != -1){
                Inventario inventario = new Inventario();
                ArrayList<Objeto> vectorObjetos = new ArrayList<>();
                inventario.setEspaciosOcupados(0);
                inventario.setObjetosInventario(vectorObjetos);
                inventario.setIdPersonaje(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje());
                getArrayDeInventariosSistema().add(inventario);
                escribirXMLInventarios(ArrayDeInventariosSistema);
                escribirXMLPersonajes(ArrayDePersonajesSistema, vectorObjetos, ArrayDeInventariosSistema);
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
               
            }
            else{
                System.out.println("Personaje no encontrado");
            } 
    }
    
   
    //Se realizan comprobaciones para ver si el objeto existe, si el inventario existe y si el objeto existe dentro del inventario
    public void aniadirObjetoaInventario(String idObjeto, String idInventario){
        int posicionObjeto =  getPosicionObjetoById(idObjeto);
        int posicionInventario = buscarInventarioPorId(idInventario);
        if(posicionObjeto != -1 && posicionInventario != -1){
            if(!getArrayDeInventariosSistema().get(posicionInventario).comprobarSiObjetoEnInventario(getObjetoById(idObjeto))){
                getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().add(getArrayDeObjetosSistema().get(posicionObjeto));
                getArrayDeInventariosSistema().get(posicionInventario).setEspaciosOcupados(getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().size());
                escribirXMLInventarios(ArrayDeInventariosSistema);
                escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                cargarPersonajesEnTabla(ArrayDePersonajesSistema);
                //Mensaje informativo de que se ha añadido correctamente
                JOptionPane.showMessageDialog(vista, "Objeto añadido correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                //Mensaje de error de que el objeto ya existe en el inventario
                JOptionPane.showMessageDialog(vista, "El objeto ya existe en el inventario", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
            System.out.println("No se ha encontrado el objeto o el inventario");
        }
    }
    
    public void vaciarInventario(String idInventario){
        
        for(Inventario inventario : getArrayDeInventariosSistema()){
            if(inventario.getIdInventario().equals(idInventario)){
                inventario.getObjetosInventario().removeAll(inventario.getObjetosInventario());
                inventario.setEspaciosOcupados(0);
                escribirXMLInventarios(ArrayDeInventariosSistema);
                escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            }
        }
        
    }
    
    //Busca el inventario por el id en el vector de inventarios del sistema.
    public int buscarInventarioPorId(String idInventario){
        int posicion = -1;
        for(Inventario inventario : getArrayDeInventariosSistema()){
            if(inventario.getIdInventario().equals(idInventario)){
                posicion = getArrayDeInventariosSistema().indexOf(inventario);
            }
        }
        return posicion;
    }
   
    //****************************Funciones del PERSONAJE*****************************************************************


    public boolean comprobarSiNivelCorrecto(int nivel){
        boolean correcto = false;
        if(nivel < 1 || nivel > 60){
            JOptionPane.showMessageDialog(vista, "El nivel debe estar entre 1 y 60", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else{
            correcto = true;
        }
        return correcto;
    }

    //Esto se ejecuta en el boton de guardar.
    public void aniadirPersonaje(String nombre, String servidor, String Raza, String nivel, String faccion  ){
        try{
            Inventario inventarioNuevo = new Inventario();
            int nivelParseado = Integer.parseInt(nivel);
            Personaje personajeAniadir = new Personaje();
            personajeAniadir.setNombre(nombre);
            personajeAniadir.setServidor(servidor);
            personajeAniadir.setRaza(Raza);
            personajeAniadir.setNivel(nivelParseado);
            personajeAniadir.setFaccion(faccion);
            personajeAniadir.setAniadirInventarioaPersonaje(inventarioNuevo);
            
            ArrayDeInventariosSistema.add(inventarioNuevo);
            ArrayDePersonajesSistema.add(personajeAniadir);
            conector.insertarPersonajeYInventarioEnBD(personajeAniadir, inventarioNuevo);
            
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
            escribirXMLInventarios(ArrayDeInventariosSistema);
            escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
            JOptionPane.showMessageDialog(vista, "Personaje añadido correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(NumberFormatException formatoIncorrecto){
            System.out.println(formatoIncorrecto.getMessage());
        }
    }
    
    public int buscarPersonajeEnSistema(String nombre, String servidor){
        for (int i = 0; i < this.getArrayDePersonajesDeSistema().size(); i++) {
            if (nombre.equals(this.getArrayDePersonajesDeSistema().get(i).getNombre()) && servidor.equals(this.getArrayDePersonajesDeSistema().get(i).getServidor())) {
                return i; // Si se encuentra el personaje, devuelve la posición
            }
        }
        return -1; // Si no se encuentra el personaje, devuelve -1
    }
    
    //Devuelve la posicion en la cual se encuentra ese personaje en el array del sistema
    public int buscarPersonajeEnSistemaPorId(String idPersonaje){
        int posicion = -1;
        for (Personaje personaje : getArrayDePersonajesDeSistema()) {
            if (personaje.getIdPersonaje().equals(idPersonaje)) {
                posicion = getArrayDePersonajesDeSistema().indexOf(personaje);
            }
        }
        return posicion;
    }
    
   
    
    
    
    public void modificarPersonaje(String idPersonaje, String nombre, String servidor, String raza, int nivel, String faccion){
  
        int posicionPersonaje = buscarPersonajeEnSistemaPorId(idPersonaje);
         if(posicionPersonaje != -1){
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNombre(nombre);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setServidor(servidor);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setRaza(raza);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNivel(nivel);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setFaccion(faccion);
            escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
         }
    }
    
    public void borrarPersonaje(String nombre, String servidor){
        
        int posicionPersonaje = buscarPersonajeEnSistema(nombre, servidor);
        if(posicionPersonaje != -1){
            //Primero borra todos los objetos que contiene el inventario
            vaciarInventario(getArrayDePersonajesDeSistema().get(posicionPersonaje).getInventario().getIdInventario());
            //Borra el inventario del sistema
            getArrayDeInventariosSistema().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje).getInventario());
             System.out.println("Llega aqui");
            //Busca en el array de hermandades de sistema si el personaje pertenece a alguna hermandad y lo borra de la lista de miembros
            for(Hermandad hermandad : getArrayDeHermandadesSistema()){
               for(int i = 0; i < hermandad.getListaMiembros().size(); i++){
                    if(hermandad.getListaMiembros().get(i).getIdPersonaje().equals(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje())){
                        hermandad.getListaMiembros().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                        hermandad.setNumeroMiembros(hermandad.getListaMiembros().size());
                    }
                }
            }
           
            //Borra el personaje
            getArrayDePersonajesDeSistema().remove(posicionPersonaje);
            escribirXMLInventarios(ArrayDeInventariosSistema);
            escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
            escribirXMLHermandades(ArrayDeHermandadesSistema);
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
        }
        else{
            System.out.println("No se ha encontrado el personaje");
        }
    }
    
     public void cargarPersonajesEnTabla(ArrayList<Personaje> ArrayDePersonajes) {
        DefaultTableModel model = (DefaultTableModel) vista.jTable_personaje.getModel();

        // Limpia la tabla
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        // Llena el modelo con los datos de tu ArrayList
        for (Personaje personaje : ArrayDePersonajes) {
            Object[] rowData = {personaje.getNombre(), personaje.getServidor(), personaje.getRaza(), personaje.getNivel()};
            model.addRow(rowData);
        }

        // Asigna el modelo a la JTable
        vista.jTable_personaje.setModel(model);
    }
     
     //***************************************************************************************************************************************************************
        
     //**********************************************************FUNCIONES de Hermandad**************************************************************************
     public void aniadirHermandad(String nombre, String servidor){
        if(buscarHermandadPorNombre(nombre, servidor) == -1){
            Hermandad hermandad = new Hermandad();
            hermandad.setNombreHermandad(nombre);
            hermandad.setServidorHermandad(servidor);
            ArrayDeHermandadesSistema.add(hermandad);
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
            escribirXMLHermandades(ArrayDeHermandadesSistema);
        }
        else{
            System.out.println("La hermandad ya existe");
        }
     }

     //Nos devuelve la posicion en la que se encuentra la hermandad en el array de hermandaddes de sistema
     public int buscarHermandadPorNombre(String nombre, String servidor){
         for(Hermandad hermandad : getArrayDeHermandadesSistema()){
             if(hermandad.getNombreHermandad().equals(nombre) && hermandad.getServidorHermandad().equals(servidor)){
                 return ArrayDeHermandadesSistema.indexOf(hermandad);
             }
         }
         return -1;
     }

     //Nos devuelve el objeto 
     public Hermandad buscarHermandadPorId(String id){
        for(Hermandad hermandad : getArrayDeHermandadesSistema()){
            if(hermandad.getIdHermandad().equals(id)){
                return hermandad;
            }
        }
        return null;
     }

     public boolean comprobarSiPersonajeEnHermandad(String idPersonaje, String idHermandad){
        for(Hermandad hermandad : getArrayDeHermandadesSistema()){
            if(hermandad.getIdHermandad().equals(idHermandad)){
                for(Personaje personaje : hermandad.getListaMiembros()){
                    if(personaje.getIdPersonaje().equals(idPersonaje)){
                        return true;
                    }
                }
            }
        }
        return false;
     }

     //Añade un personaje a una hermandad
     public void aniadirPersonajeaHermandad(String nombrePersonaje, String servidorPersonaje, String nombreHermandad, String servidorHermandad){
            int posicionPersonaje = buscarPersonajeEnSistema(nombrePersonaje, servidorPersonaje);
            int posicionHermandad = buscarHermandadPorNombre(nombreHermandad, servidorHermandad);
            if(posicionPersonaje != -1 && posicionHermandad != -1){
                //Comprueba si la hermandad esta llena, o si este personaje ya esta en la hermandad
                //hacer sin contains
                if(getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().size() <= 100 && !comprobarSiPersonajeEnHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje(), getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad())){
                    getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().add(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                    getArrayDeHermandadesSistema().get(posicionHermandad).setNumeroMiembros(getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().size());
                    escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                    escribirXMLHermandades(ArrayDeHermandadesSistema);
                    cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
                    cargarPersonajesHermandadEnTabla(getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad());
                    JOptionPane.showMessageDialog(vista, "Personaje añadido correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);

                }
                else{
                    JOptionPane.showMessageDialog(vista, "El personaje ya esta en la hermandad", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                
                 System.out.println("No se ha encontrado la hermandad o el personaje");
            }                
        }
     
     //Recibe el nombre y el servidor del personaje y lo borra de la lista de miembros de la hermandad, y de la lista de hermandades del personaje borra esa hermnadad
        public void borrarPersonajeDeHermandad(String nombrePersonaje, String servidorPersonaje, String nombreHermandad, String servidorHermandad){
                int posicionPersonaje = buscarPersonajeEnSistema(nombrePersonaje, servidorPersonaje);
                int posicionHermandad = buscarHermandadPorNombre(nombreHermandad, servidorHermandad);
                if(posicionPersonaje != -1 && posicionHermandad != -1){
                    //Comprueba si el personaje esta en la hermandad
                    if(comprobarSiPersonajeEnHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje(), getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad())){
                        getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                        getArrayDeHermandadesSistema().get(posicionHermandad).setNumeroMiembros(getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().size());
                        getArrayDePersonajesDeSistema().get(posicionPersonaje).getListaHermandadades().remove(getArrayDeHermandadesSistema().get(posicionHermandad));
                        escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                        escribirXMLHermandades(ArrayDeHermandadesSistema);
                        cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
                        cargarPersonajesHermandadEnTabla(getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad());
                        JOptionPane.showMessageDialog(vista, "Personaje borrado correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(vista, "El personaje no esta en la hermandad", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    System.out.println("No se ha encontrado la hermandad o el personaje");
                }
        }

     //Modifica una hermandad
     public void modificarHermandad(String nombreOriginal, String servidorOriginal, String nombreCambiar, String servidorCambiar){
            //Te busca la posicion de la hermandad que quieres modificar
            int posicionHermandad = buscarHermandadPorNombre(nombreOriginal, servidorOriginal);
            int posicionHermandadCambiar = buscarHermandadPorNombre(nombreCambiar, servidorCambiar);
            if(posicionHermandad != -1){
                //Comprueba si ya existe una hermandad con el mismo nombre y el mismo servidor
                if(posicionHermandadCambiar == -1){
                    getArrayDeHermandadesSistema().get(posicionHermandad).setNombreHermandad(nombreCambiar);
                    getArrayDeHermandadesSistema().get(posicionHermandad).setServidorHermandad(servidorCambiar);
                    escribirXMLHermandades(ArrayDeHermandadesSistema);
                    cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
                }
                else{
                    System.out.println("La hermandad ya existe");
                }
            }
            else{
                System.out.println("No se ha encontrado la hermandad");
            }
     }

    //Busca la hermandad, si esta existe busca en listaHermandades de cada personaje para borrar esta hermandad de sus listas, luego borra la hermandad del sistema
     public void borrarHermandad(String nombreHermandad, String servidorHermandad){
            int posicionHermandad = buscarHermandadPorNombre(nombreHermandad, servidorHermandad);
            if(posicionHermandad != -1){
                for(Personaje personaje : getArrayDePersonajesDeSistema()){
                    for(Hermandad hermandad : personaje.getListaHermandadades()){
                        if(hermandad.getIdHermandad().equals(getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad())){
                            personaje.getListaHermandadades().remove(hermandad);
                        }
                    }
                }
                getArrayDeHermandadesSistema().remove(posicionHermandad);
                escribirXMLPersonajes(ArrayDePersonajesSistema, ArrayDeObjetosSistema, ArrayDeInventariosSistema);
                escribirXMLHermandades(ArrayDeHermandadesSistema);
                cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
            }
            else{
                System.out.println("No se ha encontrado la hermandad");
            }
        }

     public void escribirXMLHermandades(ArrayList ArrayDeHermandadesSistema){
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document documento = builder.newDocument();

                Element nodoHermandades = documento.createElement("hermandades");
                documento.appendChild(nodoHermandades);
                for (Hermandad hermandad : getArrayDeHermandadesSistema()) {
                    Element nodoHermandad = documento.createElement("hermandad");
                    nodoHermandades.appendChild(nodoHermandad);
                    agregarElementoConTexto(documento, nodoHermandad, "id_hermandad", hermandad.getIdHermandad());
                    agregarElementoConTexto(documento, nodoHermandad, "nombre", hermandad.getNombreHermandad());
                    agregarElementoConTexto(documento, nodoHermandad, "servidor", hermandad.getServidorHermandad());
                    agregarElementoConTexto(documento, nodoHermandad, "numero_miembros", Integer.toString(hermandad.getNumeroMiembros()));
                    Element nodoMiembros = documento.createElement("miembros");
                    nodoHermandad.appendChild(nodoMiembros);
                    for(Personaje personaje : hermandad.getListaMiembros()){
                        Element nodoMiembro = documento.createElement("miembro");
                        nodoMiembros.appendChild(nodoMiembro);
                        agregarElementoConTexto(documento, nodoMiembro, "id_personaje", personaje.getIdPersonaje());
                    }
                }
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                DOMSource source = new DOMSource(documento);
                StreamResult result = new StreamResult(new File("hermandadesSistema.xml"));

                transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leerXMLHermandades(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File("hermandadesSistema.xml"));

            NodeList hermandades = documento.getElementsByTagName("hermandad");

            for (int i = 0; i < hermandades.getLength(); i++) {
                Node hermandadNode = hermandades.item(i);
                if (hermandadNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) hermandadNode;
                    
                    String idHermandad = elemento.getElementsByTagName("id_hermandad").item(0).getTextContent();
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    String servidor = elemento.getElementsByTagName("servidor").item(0).getTextContent();
                    String numeroMiembros = elemento.getElementsByTagName("numero_miembros").item(0).getTextContent();
                    
                    // Crea un nuevo objeto Hermandad y lo añade a la lista
                    Hermandad hermandad = new Hermandad();
                    hermandad.setIdHermandad(idHermandad);
                    hermandad.setNombreHermandad(nombre);
                    hermandad.setServidorHermandad(servidor);
                    hermandad.setNumeroMiembros(Integer.parseInt(numeroMiembros));
                    
                    // Crea un nuevo array de personajes para añadirlo a la hermandad
                    ArrayList<Personaje> listaMiembros = new ArrayList<>();
                    NodeList miembros = elemento.getElementsByTagName("miembro");
                    for (int j = 0; j < miembros.getLength(); j++) {
                        Node miembroNode = miembros.item(j);
                        if (miembroNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementoMiembro = (Element) miembroNode;
                            String idPersonaje = elementoMiembro.getElementsByTagName("id_personaje").item(0).getTextContent();
                            for (Personaje personaje : getArrayDePersonajesDeSistema()) {
                                if (personaje.getIdPersonaje().equals(idPersonaje)) {
                                    listaMiembros.add(personaje);
                                }
                            }
                        }
                    }
                    hermandad.setListaMiembros(listaMiembros);
                    getArrayDeHermandadesSistema().add(hermandad);
                }
            }
           
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarHermandadesEnTabla(ArrayList ArrayDeHermandadesSistema){
        DefaultTableModel model = (DefaultTableModel) vista.jTable_hermandad.getModel();

        // Limpia la tabla
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        // Llena el modelo con los datos de tu ArrayList
        for (Hermandad hermandad : getArrayDeHermandadesSistema()) {
            Object[] rowData = {hermandad.getNombreHermandad(), hermandad.getServidorHermandad(), hermandad.getNumeroMiembros()};
            model.addRow(rowData);
        }

        // Asigna el modelo a la JTable
        vista.jTable_hermandad.setModel(model);
    }

    public void cargarPersonajesHermandadEnTabla(String idHermandad){
        DefaultTableModel model = (DefaultTableModel) vista.jTable_personajes_hermandad.getModel();
        while(model.getRowCount()  > 0){
            model.removeRow(0);
        }
        
        for(Hermandad hermandad : getArrayDeHermandadesSistema()){
            if(hermandad.getIdHermandad().equals(idHermandad)){
                for(Personaje personaje : hermandad.getListaMiembros()){
                    Object[] rowData = {personaje.getNombre(), personaje.getServidor(), personaje.getRaza(), personaje.getNivel()};
                    model.addRow(rowData);
                }
            }
        }
         vista.jTable_personajes_hermandad.setModel(model);
    }
    
    
    //**********************************************************************************************************************************************************
    public void escribirXMLObjetos(ArrayList<Objeto> arrayListObjetos){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();
            
                Element nodoObjetos = documento.createElement("objetos");
                documento.appendChild(nodoObjetos);
                for (Objeto objeto : arrayListObjetos) {
                Element nodoObjeto = documento.createElement("objeto");
                nodoObjetos.appendChild(nodoObjeto);
                agregarElementoConTexto(documento, nodoObjeto, "id_objeto", objeto.getIdObjeto());
                agregarElementoConTexto(documento, nodoObjeto, "nombre_objeto", objeto.getNombreObjeto());
                agregarElementoConTexto(documento, nodoObjeto, "rareza_objeto", objeto.getRareza());
                agregarElementoConTexto(documento, nodoObjeto, "precio_objeto", Double.toString(objeto.getPrecio()));
                agregarElementoConTexto(documento, nodoObjeto, "descripcion_objeto", objeto.getDescripcion());
                }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("objetosSistema.xml"));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void escribirXMLInventarios(ArrayList <Inventario> arrayListInventarios){
            try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

                Element nodoInventarios = documento.createElement("inventarios");
                documento.appendChild(nodoInventarios);
                for (Inventario inventario : arrayListInventarios) {
                Element nodoInventario = documento.createElement("inventario");
                nodoInventarios.appendChild(nodoInventario);
                agregarElementoConTexto(documento, nodoInventario, "id_inventario", inventario.getIdInventario());
                agregarElementoConTexto(documento, nodoInventario, "id_personaje", inventario.getIdPersonaje());
                agregarElementoConTexto(documento, nodoInventario, "capacidad_maxima", Integer.toString(inventario.getCapacidadMaxima()));
                agregarElementoConTexto(documento, nodoInventario, "espacios_ocupados",Integer.toString( inventario.getEspaciosOcupados()));
                Element nodoObjetos = documento.createElement("objetos");
                nodoInventario.appendChild(nodoObjetos);
                for (Objeto objeto : inventario.getObjetosInventario()) {
                    Element nodoObjeto = documento.createElement("objeto");
                    nodoObjetos.appendChild(nodoObjeto);
                    agregarElementoConTexto(documento, nodoObjeto, "id_objeto", objeto.getIdObjeto());
                    agregarElementoConTexto(documento, nodoObjeto, "nombre_objeto", objeto.getNombreObjeto());
                    }
                }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("inventariosSistema.xml"));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
           
        
     
        public void escribirXMLPersonajes(ArrayList<Personaje> listaPersonajes, ArrayList<Objeto> listaObjetos, ArrayList<Inventario> listaInventarios) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();
            
            Element raiz = documento.createElement("personajes");
            documento.appendChild(raiz);
          
            for (Personaje personaje : listaPersonajes) {
                Element nodoPersonaje = documento.createElement("personaje");
                raiz.appendChild(nodoPersonaje);
                
                agregarElementoConTexto(documento, nodoPersonaje, "id_personaje", personaje.getIdPersonaje());
                agregarElementoConTexto(documento, nodoPersonaje, "nombre", personaje.getNombre());
                agregarElementoConTexto(documento, nodoPersonaje, "servidor", personaje.getServidor());
                agregarElementoConTexto(documento, nodoPersonaje, "raza", personaje.getRaza());
                agregarElementoConTexto(documento, nodoPersonaje, "nivel", String.valueOf(personaje.getNivel()));
                agregarElementoConTexto(documento, nodoPersonaje, "faccion", personaje.getFaccion());
                
                Element nodoInventario = documento.createElement("inventario");
                nodoPersonaje.appendChild(nodoInventario);
                for (Inventario inventario : getArrayDeInventariosSistema()) {
                    if(inventario.getIdPersonaje() == personaje.getIdPersonaje() || inventario.getIdInventario() == personaje.getInventario().getIdInventario()){
                        agregarElementoConTexto(documento, nodoInventario, "id_inventario", inventario.getIdInventario());
                        agregarElementoConTexto(documento, nodoInventario, "id_personaje", inventario.getIdPersonaje());
                        agregarElementoConTexto(documento, nodoInventario, "capacidad_maxima", Integer.toString(inventario.getCapacidadMaxima()));
                        agregarElementoConTexto(documento, nodoInventario, "espacios_ocupados", Integer.toString(inventario.getEspaciosOcupados()));
                        // Para cada inventario, crea un nodo "objetos" para la lista de objetos
                        Element nodoObjetos = documento.createElement("objetos");
                        nodoInventario.appendChild(nodoObjetos);
                        for (Objeto objeto : inventario.getObjetosInventario()) {
                            Element nodoObjeto = documento.createElement("objeto");
                            nodoObjetos.appendChild(nodoObjeto);
                            agregarElementoConTexto(documento, nodoObjeto, "id_objeto", objeto.getIdObjeto());
                            agregarElementoConTexto(documento, nodoObjeto, "nombre_objeto", objeto.getNombreObjeto());
                        } 
                    }
                }
                Element nodoHermandades = documento.createElement("hermandades");
                nodoPersonaje.appendChild(nodoHermandades);
                for (Hermandad hermandad : getArrayDeHermandadesSistema()) {
                    for (Personaje personajeHermandad : hermandad.getListaMiembros()) {
                        if (personajeHermandad.getIdPersonaje().equals(personaje.getIdPersonaje())) {
                            Element nodoHermandad = documento.createElement("hermandad");
                            nodoHermandades.appendChild(nodoHermandad);
                            agregarElementoConTexto(documento, nodoHermandad, "id_hermandad", hermandad.getIdHermandad());
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("personajes.xml"));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

   private static void agregarElementoConTexto(Document doc, Element parent, String tagName, String texto) {
       Element element = doc.createElement(tagName);
       parent.appendChild(element);
       Text textNode = doc.createTextNode(texto);
       element.appendChild(textNode);
   }
   
   public void leerXMLPersonajes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File("personajes.xml"));

            NodeList personajes = documento.getElementsByTagName("personaje");

            for (int i = 0; i < personajes.getLength(); i++) {
                Node personajeNode = personajes.item(i);
                if (personajeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) personajeNode;
                    
                    String idPersonaje = elemento.getElementsByTagName("id_personaje").item(0).getTextContent();
                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    String servidor = elemento.getElementsByTagName("servidor").item(0).getTextContent();
                    String raza = elemento.getElementsByTagName("raza").item(0).getTextContent();
                    String nivel = elemento.getElementsByTagName("nivel").item(0).getTextContent();
                    String faccion = elemento.getElementsByTagName("faccion").item(0).getTextContent();
                    
                    // Crea un nuevo objeto Personaje y lo añade a la lista
                    Personaje personaje = new Personaje();
                    if(idPersonaje.isEmpty() || idPersonaje == null){
                        idPersonaje = personaje.generateNewIdPersonaje();
                    }
                    personaje.setIdPersonaje(idPersonaje);
                    personaje.setNombre(nombre);
                    personaje.setServidor(servidor);
                    personaje.setRaza(raza);
                    personaje.setNivel(Integer.parseInt(nivel));
                    personaje.setFaccion(faccion);
                    
                   

                   String idInventario = elemento.getElementsByTagName("id_inventario").item(0).getTextContent();
                   for (Inventario inventarioSistema : this.getArrayDeInventariosSistema()) {
                        if (inventarioSistema.getIdInventario().equals(idInventario)) {
                            personaje.setInventario(inventarioSistema);
                            //Cuidado, puede ser que reviente el programa TODO :)
                        }
                    }
                    //Busca en el array de hermandades, si el personaje pertenece a alguna hermandad, lo añade a la lista de hermandades del personaje
                    ArrayList<Hermandad> listaHermandades = new ArrayList<>();
                    NodeList hermandades = elemento.getElementsByTagName("hermandad");
                    for (int j = 0; j < hermandades.getLength(); j++) {
                        Node hermandadNode = hermandades.item(j);
                        if (hermandadNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementoHermandad = (Element) hermandadNode;
                            String idHermandad = elementoHermandad.getElementsByTagName("id_hermandad").item(0).getTextContent();
                            for (Hermandad hermandad : this.getArrayDeHermandadesSistema()) {
                                if (hermandad.getIdHermandad().equals(idHermandad)) {
                                    personaje.getListaHermandadades().add(hermandad);
                                }
                            }
                        }
                    }

                    ArrayDePersonajesSistema.add(personaje);
                }
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
    }


   
   //Lee el xml de objetosSistema y los mete en el arrayList
   public void leerXMLObjetos(){
       
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;
        
          try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File("objetosSistema.xml"));
            
            NodeList objetos = documento.getElementsByTagName("objeto");
            
            for(int i = 0; i < objetos.getLength(); i++){
                Node objetoNode = objetos.item(i);
                if(objetoNode.getNodeType() == Node.ELEMENT_NODE){
                    Element elemento = (Element) objetoNode;
            
                    String idObjeto = elemento.getElementsByTagName("id_objeto").item(0).getTextContent();
                    String nombreObjeto = elemento.getElementsByTagName("nombre_objeto").item(0).getTextContent();
                    String rarezaObjeto = elemento.getElementsByTagName("rareza_objeto").item(0).getTextContent();
                    String precioObjeto = elemento.getElementsByTagName("precio_objeto").item(0).getTextContent();
                    String descripcionObjeto = elemento.getElementsByTagName("descripcion_objeto").item(0).getTextContent();
                    if(getPosicionObjetoById(idObjeto) == -1){
                        Objeto objetoNuevo = new Objeto();
                        objetoNuevo.setIdObjeto(idObjeto);
                        objetoNuevo.setNombreObjeto(nombreObjeto);
                        objetoNuevo.setRareza(rarezaObjeto);
                        objetoNuevo.setPrecio(Double.parseDouble(precioObjeto));
                        objetoNuevo.setDescripcion(descripcionObjeto);
                        ArrayDeObjetosSistema.add(objetoNuevo);
                    }
                  }
         }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
   }
   
     public void leerInventarioSistema(){
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document documento = null;
        
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          documento = builder.parse(new File("inventariosSistema.xml"));
          
          NodeList inventario = documento.getElementsByTagName("inventario");
          
            for(int i = 0; i < inventario.getLength(); i++){
                Node inventarioNode = inventario.item(i);
                if(inventarioNode.getNodeType() == Node.ELEMENT_NODE){
                    Element inventarioElement = (Element) inventarioNode;
                    
                    Inventario inventarioNuevo = new Inventario();
                 
                    String idInventario = inventarioElement.getElementsByTagName("id_inventario").item(0).getTextContent();
                    String idPersonaje = inventarioElement.getElementsByTagName("id_personaje").item(0).getTextContent();
                    String  capacidadMaxima = inventarioElement.getElementsByTagName("capacidad_maxima").item(0).getTextContent();
                    String espaciosOcupados = inventarioElement.getElementsByTagName("espacios_ocupados").item(0).getTextContent();
                    
                    inventarioNuevo.setIdInventario(idInventario);
                    inventarioNuevo.setIdPersonaje(idPersonaje);
                   
                    NodeList objetos = inventarioElement.getElementsByTagName("objeto");
                    
                    for(int j = 0; j < objetos.getLength(); j++){
                        Node objetoNode = objetos.item(j);
                        if(objetoNode.getNodeType() == Node.ELEMENT_NODE){
                            Element objetoElement = (Element) objetoNode;
                            
                            Objeto objetoNuevo = new Objeto();
                            String idObjeto = objetoElement.getElementsByTagName("id_objeto").item(0).getTextContent();
                            //Busca en el vector de objetos del sistema el objeto por su id y lo inserta en el vector de objetos del inventarioNuevo
                            if(inventarioNuevo.getObjetosInventario() != null){
                                inventarioNuevo.getObjetosInventario().add(getArrayDeObjetosSistema().get(getPosicionObjetoById(idObjeto)));
                            } 
                        }
                    }
                    inventarioNuevo.setEspaciosOcupados(inventarioNuevo.getObjetosInventario().size());
                    ArrayDeInventariosSistema.add(inventarioNuevo);
                 }
        }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
     }
}
  
