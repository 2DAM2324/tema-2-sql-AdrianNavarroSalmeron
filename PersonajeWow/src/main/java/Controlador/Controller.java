/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Modelo.*;
import Vista.Ventana1;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AdrianNS
 */
public class Controller {
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
        Conector conector = Conector.getInstancia();
        this.conector = conector;
        conector.crearBaseDatos();
        
        
       // leerXMLObjetos();
       conector.leerObjetosDeBd(ArrayDeObjetosSistema);
       conector.leerInventario(ArrayDeInventariosSistema);
       conector.leerInventarioObjeto(ArrayDeInventariosSistema, ArrayDeObjetosSistema);
       conector.leerPersonajeDeBd(ArrayDePersonajesSistema, ArrayDeInventariosSistema, ArrayDeHermandadesSistema);
       conector.leerHermandad(ArrayDeHermandadesSistema);
       conector.leerHermandadPersonaje(ArrayDeHermandadesSistema, ArrayDePersonajesSistema);
        cargarObjetoEnTabla(ArrayDeObjetosSistema);
        cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
        cargarPersonajesEnTabla(ArrayDePersonajesSistema);
        cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
    }
    
  
    
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
    //****************************************Funciones de los objetos******************************************************************************************
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
                conector.modificarInventario(inventario);
            }
        }
        conector.borrarObjetoDeBd(objetoaBorrar);
        ArrayDeObjetosSistema.remove(objetoaBorrar);
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
                conector.modificarInventario(getArrayDeInventariosSistema().get(posicionInventario));
                conector.borrarObjetoEnInventario(getArrayDeObjetosSistema().get(posicionObjeto), getArrayDeInventariosSistema().get(posicionInventario));
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                cargarPersonajesEnTabla(ArrayDePersonajesSistema);
                cargarInventarioPersonajeEnTabla(getArrayDeInventariosSistema().get(posicionInventario).getIdPersonaje());
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

    public void cargarInventarioPersonajeEnTabla(Integer idPersonaje){
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
                //getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().add(getArrayDeObjetosSistema().get(posicionObjeto));
                 conector.insertarObjetoEnInventario(getArrayDeObjetosSistema().get(posicionObjeto), getArrayDeInventariosSistema().get(posicionInventario));
                getArrayDeInventariosSistema().get(posicionInventario).setEspaciosOcupados(getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().size());
                conector.modificarInventario(getArrayDeInventariosSistema().get(posicionInventario));
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
            //personajeAniadir.setAniadirInventarioaPersonaje(inventarioNuevo);
            
            conector.insertarPersonajeYInventarioEnBD(personajeAniadir, inventarioNuevo);
            //Traemos el personaje y lo añadimos al vector de personajes del sistema
             conector.leerPersonaje(nombre, servidor, ArrayDePersonajesSistema);
             int posicionPersonaje = buscarPersonajeEnSistema(nombre, servidor);
             //Añadimos el inventario al personaje que hemos introducido en el vector
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setAniadirInventarioaPersonaje(inventarioNuevo);
            //Añadimos el inventario al vector de inventarios
            getArrayDeInventariosSistema().add(inventarioNuevo);
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
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
    public int buscarPersonajeEnSistemaPorId(Integer idPersonaje){
        int posicion = -1;
        for (Personaje personaje : getArrayDePersonajesDeSistema()) {
            if (personaje.getIdPersonaje().equals(idPersonaje)) {
                posicion = getArrayDePersonajesDeSistema().indexOf(personaje);
            }
        }
        return posicion;
    }
    
   
    
    
    
    public void modificarPersonaje(Integer idPersonaje, String nombre, String servidor, String raza, int nivel, String faccion){
  
        int posicionPersonaje = buscarPersonajeEnSistemaPorId(idPersonaje);
         if(posicionPersonaje != -1){
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNombre(nombre);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setServidor(servidor);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setRaza(raza);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNivel(nivel);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setFaccion(faccion);
            conector.modificarPersonajeBd(getArrayDePersonajesDeSistema().get(posicionPersonaje));
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
         }
    }
    
    public void borrarPersonaje(String nombre, String servidor){
        
        int posicionPersonaje = buscarPersonajeEnSistema(nombre, servidor);
        if(posicionPersonaje != -1){
            //Borramos el personaje de la base de datos
             conector.borrarPersonajeIventarioBd(getArrayDePersonajesDeSistema().get(posicionPersonaje));
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
                        //Actualiza el numero de miembros de las hermandades a las que pertenecia el personaje
                        conector.modificarHermandadEnBd(hermandad);
                    }
                }
            }
            getArrayDePersonajesDeSistema().remove(posicionPersonaje);
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
            conector.insertarHermandadEnBD(hermandad);
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
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

     public boolean comprobarSiPersonajeEnHermandad(Integer idPersonaje, String idHermandad){
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
                    //Añadimos la hermandad a la lista de hermandades de este personaje
                    getArrayDePersonajesDeSistema().get(posicionPersonaje).getListaHermandadades().add(getArrayDeHermandadesSistema().get(posicionHermandad));
                    conector.insertarPersonajeHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje), getArrayDeHermandadesSistema().get(posicionHermandad));
                    conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
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
                        //Borramos el personaje de la tabla hermandadPersonaje
                        conector.borrarPersonajeHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje), getArrayDeHermandadesSistema().get(posicionHermandad));
                        getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                        getArrayDeHermandadesSistema().get(posicionHermandad).setNumeroMiembros(getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().size());
                        getArrayDePersonajesDeSistema().get(posicionPersonaje).getListaHermandadades().remove(getArrayDeHermandadesSistema().get(posicionHermandad));
                        //Actualizamos el numero de miembros de la hermnadad
                        conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
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
                    //Una vez que se han cambiado el nombre y el servidor en el controlador lo cambiamos en la bd.
                    conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
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
     public void borrarHermandad(String nombreHermandad, String servidorHermandad) {
        int posicionHermandad = buscarHermandadPorNombre(nombreHermandad, servidorHermandad);

        if (posicionHermandad != -1) {
            Hermandad hermandadABorrar = ArrayDeHermandadesSistema.get(posicionHermandad);
            for (Personaje personaje : getArrayDePersonajesDeSistema()) {
                //Añadimos un iterador 
                Iterator<Hermandad> iterator = personaje.getListaHermandadades().iterator();
                //Itera sobre el iterador en el que hemos almacenado la lista de hermandades
                while (iterator.hasNext()) {
                    Hermandad hermandad = iterator.next();
                    if (hermandad.getIdHermandad().equals(hermandadABorrar.getIdHermandad())) {
                        //Mediante el iterador borramos los elementos 
                        iterator.remove();
                    }
                }
            }
            getArrayDeHermandadesSistema().remove(posicionHermandad);
            conector.borrarHermandadBd(hermandadABorrar);
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
        } else {
            System.out.println("No se ha encontrado la hermandad");
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
        
        for(Hermandad hermanadCargar : getArrayDeHermandadesSistema()){
            if(hermanadCargar.getIdHermandad().equals(idHermandad)){
                for(Personaje personajeCargar : hermanadCargar.getListaMiembros()){
                    Object[] rowData = {personajeCargar.getNombre(), personajeCargar.getServidor(), personajeCargar.getRaza(), personajeCargar.getNivel()};
                    model.addRow(rowData);
                }
            }
        }
         vista.jTable_personajes_hermandad.setModel(model);
    }
}
  
