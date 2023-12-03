/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import Modelo.Conector;
import Modelo.Hermandad;
import Modelo.Inventario;
import Modelo.Objeto;
import Modelo.Personaje;
import Vista.Ventana1;

/**
 *
 * @author AdrianNS
 */
public class Controller {
    Conector conector;
    private Ventana1 ventana;
    public ArrayList <Personaje> ArrayDePersonajesSistema;
    private ArrayList <Hermandad> ArrayDeHermandadesSistema;
    private ArrayList <Objeto> ArrayDeObjetosSistema;
    private ArrayList<Inventario> ArrayDeInventariosSistema;
    private Ventana1 vista;
    private boolean estoyEnTest;
    
   /**
    * @brief Constructor de Controller
    * @param vistaSet instacia de la ventana
    * @param nombreBd nombre de la base de datos a la que nos conectamos
    * @param estoyTestando booleano que indica si estamos en test o no
    */
    public Controller(Ventana1 vistaSet, String nombreBd, boolean estoyTestando){
        ArrayList<Personaje> nuevoArray = new ArrayList<>();
        ArrayList<Objeto> arrayObjeto = new ArrayList<>();
        ArrayList<Inventario> arrayInventario = new ArrayList<>();
        ArrayList<Hermandad> arrayHermandad = new ArrayList<>();
        this.vista = vistaSet;
        this.ArrayDePersonajesSistema = nuevoArray;
        this.ArrayDeObjetosSistema = arrayObjeto;
        this.ArrayDeHermandadesSistema = arrayHermandad;
        this.ArrayDeInventariosSistema = arrayInventario;
        Conector conectorDb = Conector.getInstancia(nombreBd);
        this.conector = conectorDb;
        conector.crearBaseDatos();
        estoyEnTest = estoyTestando;
        
        
       //Leemos los objetos de la base de datos
       try{
              conector.leerObjetosDeBd(ArrayDeObjetosSistema);
         }
        catch(SQLException e){
            mostrarMensajesError("ERROR AL LEER OBJETOS DE LA BASE DE DATOS");
           
        }
        catch(NullPointerException e){
            mostrarMensajesError("ERROR AL LEER OBJETOS DE LA BASE DE DATOS");
        }

        //Leemos el inventario de la base de datos
        try{
           conector.leerInventario(ArrayDeInventariosSistema); 
        }
        catch(SQLException e){
            mostrarMensajesError("ERROR AL LEER INVENTARIOS DE LA BASE DE DATOS");
        }
        catch(NullPointerException e){
            mostrarMensajesError("ERROR AL LEER INVENTARIOS DE LA BASE DE DATOS");
        }

        //Leemos inventario Objeto
        try{
            conector.leerInventarioObjeto(ArrayDeInventariosSistema, ArrayDeObjetosSistema);
        }
        catch(SQLException e){
            mostrarMensajesError("ERROR AL LEER INVENTARIO-OBJETO DE LA BASE DE DATOS");
        }
        catch(NullPointerException e){
            mostrarMensajesError("ERROR AL LEER INVENTARIO-OBJETO DE LA BASE DE DATOS");
        }

         //Leemos los personajes de la base de datos
         try{
            conector.leerPersonajeDeBd(ArrayDePersonajesSistema, ArrayDeInventariosSistema, ArrayDeHermandadesSistema);
        }
        catch(SQLException | NullPointerException e){
            mostrarMensajesError("ERROR AL LEER PERSONAJES DE LA BASE DE DATOS");
        }

        //Leemos las hermandades de la base de datos
        try{
            conector.leerHermandad(ArrayDeHermandadesSistema);
        }
        catch(SQLException e){
            mostrarMensajesError("ERROR AL LEER HERMANDADES DE LA BASE DE DATOS");
        }
        catch(NullPointerException e){
            mostrarMensajesError("ERROR AL LEER HERMANDADES DE LA BASE DE DATOS");
        }

        //Leemos la tabla hermandadPersonaje de la base de datos
        try{
            conector.leerHermandadPersonaje(ArrayDeHermandadesSistema, ArrayDePersonajesSistema);
        }
        catch(SQLException e){
            mostrarMensajesError("ERROR AL LEER HERMANDAD-PERSONAJE DE LA BASE DE DATOS");
        }
        catch(NullPointerException e){
            mostrarMensajesError("ERROR AL LEER HERMANDAD-PERSONAJE DE LA BASE DE DATOS");
        }
        cargarObjetoEnTabla(ArrayDeObjetosSistema);
        cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
        cargarPersonajesEnTabla(ArrayDePersonajesSistema);
        cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
    }
    
    /**
     * @brief Muestra las ventanas emergente en caso de no estar en test
     * @param message mensaje que se muestra en la ventana emergente
     */
    public final void mostrarMensajesError(String message) {
        if (!estoyEnTest) {
            JOptionPane.showMessageDialog(vista, message, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @brief Muestra las ventanas emergente en caso de no estar en test
     * @param message mensaje que se muestra en la ventana emergente
     */
    public void mostrarMensajesOk(String message){
        if(!estoyEnTest){
            JOptionPane.showMessageDialog(vista, message, "OK", JOptionPane.INFORMATION_MESSAGE);

        }
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
    /**
     * @brief Añade un objeto al sistema
     * @param nombreObjeto 
     * @param rareza
     * @param precio
     * @param descipcion
     * @param IdObjeto
     */
    public void aniadirObjeto(String nombreObjeto, String rareza, String precio, String descipcion, String IdObjeto){
        boolean correcto = true;
        //Comprobamos si el objeto ya existe en el sistema
        for(int i = 0; i < getArrayDeObjetosSistema().size(); i++){
            if(getArrayDeObjetosSistema().get(i).getNombreObjeto().equals(nombreObjeto) && getArrayDeObjetosSistema().get(i).getRareza().equals(rareza)){
                mostrarMensajesError("El objeto ya existe");
                i = getArrayDeObjetosSistema().size();
                correcto = false;
            }
        }
        try{
            if(nombreObjeto != null && rareza != null && precio != null && descipcion != null && correcto == true){
                     Objeto objeto = new Objeto();
                     ArrayList<String> listaIdsObjetos = new ArrayList<>();
                        try{
                            listaIdsObjetos = conector.getListaIdsObjetos();
                        }
                        catch(SQLException | NullPointerException e){
                            mostrarMensajesError("ERROR AL LEER IDS DE OBJETOS DE LA BASE DE DATOS");
                        }
                    //En caso de que haya una id repetida, se genera una nueva
                    for (int i = 0; i < listaIdsObjetos.size(); i++) {
                        if (listaIdsObjetos.get(i).equals(objeto.getIdObjeto())) {
                            String newId = objeto.generateNewIdObjeto();
                            objeto.setIdObjeto(newId);
                            System.out.println("Cambiando id de OBJETO" + objeto.getIdObjeto() + "por id" + newId + "porque ya existe");
                            }
                    }
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
                        //Bloque que captura las exepciones de la bd y muestra un mensaje en la vista
                        ArrayDeObjetosSistema.add(objeto);
                        try{
                             conector.insertarObjetoEnBd(objeto);
                        }
                        catch(SQLIntegrityConstraintViolationException e){
                            mostrarMensajesError("PK de objeto repetida en BD");
                     }
                       catch(SQLException | NullPointerException e ){
                            mostrarMensajesError("ERROR AL INSERTAR OBJETO EN BD");
                       }
                        cargarObjetoEnTabla(ArrayDeObjetosSistema);
                        mostrarMensajesOk("Objeto añadido con exito");
                    }
                    else{
                        mostrarMensajesError("El precio debe ser mayor que 0");
                    }     
            }
            else{ 
                mostrarMensajesError("No puede haber campos vacios");
            }
        }
         catch(NumberFormatException formatoIncorrecto){
            mostrarMensajesError("El precio debe de ser un numero");
        }
        catch(NullPointerException campoVacio){
            mostrarMensajesError("No puede haber campos vacios");
        }
    }
    
    /**
     * @brief Borra un objeto del sistema
     * @param idObjetoaBorrar
     */
    public void borrarObjeto(String idObjetoaBorrar) {
        Objeto objetoaBorrar = null;
        for (Objeto objeto : ArrayDeObjetosSistema) {
            if (objeto.getIdObjeto().equals(idObjetoaBorrar)) {
                objetoaBorrar = objeto;
            }
        }

        if (objetoaBorrar != null) {
            for (Inventario inventario : getArrayDeInventariosSistema()) {
                if (inventario.getObjetosInventario().contains(objetoaBorrar)) {
                    inventario.getObjetosInventario().remove(objetoaBorrar);
                    inventario.setEspaciosOcupados(inventario.getObjetosInventario().size());

                    //Bloque para capturar las excepciones a la hora de modificar el inventario en la BD
                    try{
                        conector.modificarInventario(inventario);
                    }
                    catch(SQLException | NullPointerException e){
                        mostrarMensajesError("NO SE HA PODIDO MODIFICAR EL INVENTARIO EN LA BASE DE DATOS");
                    }
                }
            }
            //Bloque para capturar las excepciones a la hora de borrar de la BD
            try{
                conector.borrarObjetoDeBd(objetoaBorrar);
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("NO SE HA PODIDO BORRAR DE LA BASE DE DATOS");
            }
            
            
            ArrayDeObjetosSistema.remove(objetoaBorrar);
            cargarObjetoEnTabla(ArrayDeObjetosSistema);
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
        }
    }

    /**
     * @brief Borra un objeto del inventario
     * @param idObjeto
     * @param idInventario
     */
    public void borrarObjetoInventario(String idObjeto, String idInventario){
        int posicionObjeto = getPosicionObjetoById(idObjeto);
        int posicionInventario = buscarInventarioPorId(idInventario);
        if(posicionObjeto != -1 && posicionInventario != -1){
            if(getArrayDeInventariosSistema().get(posicionInventario).comprobarSiObjetoEnInventario(getObjetoById(idObjeto))){
                getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().remove(getObjetoById(idObjeto));
                getArrayDeInventariosSistema().get(posicionInventario).setEspaciosOcupados(getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().size());
                //Actualizamos el inventario en la base de datos
                try{
                    conector.modificarInventario(getArrayDeInventariosSistema().get(posicionInventario));
                }
                catch(SQLException | NullPointerException e){
                    mostrarMensajesError("NO SE HA PODIDO MODIFICAR EL INVENTARIO EN LA BASE DE DATOS");
                }
                //Borramos el objeto del inventario en la base de datos
                try{
                    conector.borrarObjetoEnInventario(getArrayDeObjetosSistema().get(posicionObjeto), getArrayDeInventariosSistema().get(posicionInventario));
                }
                catch(SQLException | NullPointerException e){
                    mostrarMensajesError("NO SE HA PODIDO BORRAR EL OBJETO DEL INVENTARIO EN LA BASE DE DATOS");
                }
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                cargarPersonajesEnTabla(ArrayDePersonajesSistema);
                cargarInventarioPersonajeEnTabla(getArrayDeInventariosSistema().get(posicionInventario).getIdPersonaje());
                mostrarMensajesOk("Objeto borrado correctamente");
            }
            else{
                //Mensaje de error de que el objeto no existe en el inventario
                mostrarMensajesError("El objeto no existe en el inventario");
            }
        }
        else{
            mostrarMensajesError("No se ha encontrado el objeto o el inventario");
        }
    }

    /**
     * @brief Busca un objeto por su id
     * @param id
     * @return objeto encontrado
     */
    public Objeto getObjetoById(String id){
        for(Objeto objeto : ArrayDeObjetosSistema){
            if(objeto.getIdObjeto().equals(id)){
                return objeto;
            }
        }
        return null;
    }
    
    /**
     * @brief Busca la posicion de un objeto por su id
     * @param id
     * @return posicion del objeto
     */
     public int getPosicionObjetoById(String id){
         int posicion = -1;
        for(Objeto objeto : ArrayDeObjetosSistema){
            if(objeto.getIdObjeto().equals(id)){
                posicion = ArrayDeObjetosSistema.indexOf(objeto);
                
            }
        }
        return posicion;
     }
    
    /**
     * @brief Modifica un objeto del sistema
     * @param id
     * @param nombre
     * @param rareza
     * @param precio
     * @param descripcion
     */
    public void modificarObjeto(String id, String nombre, String rareza, String precio, String descripcion){
         boolean correcto = true;
        //Comprobamos si el objeto ya existe en el sistema
        for(int i = 0; i < getArrayDeObjetosSistema().size(); i++){
            if(getArrayDeObjetosSistema().get(i).getNombreObjeto().equals(nombre) && getArrayDeObjetosSistema().get(i).getRareza().equals(rareza)){
                mostrarMensajesError("El objeto ya existe");
                i = getArrayDeObjetosSistema().size();
                correcto = false;
            }
        }
        if(getPosicionObjetoById(id) !=-1 && nombre != null && rareza != null && precio != null && descripcion != null && correcto == true){
            try{
                double precioParseado = Double.parseDouble(precio);
                if(getArrayDeObjetosSistema().get(getPosicionObjetoById(id)) != null && precioParseado >= 1){
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setNombreObjeto(nombre);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setRareza(rareza);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setPrecio(precioParseado);
                    getArrayDeObjetosSistema().get(getPosicionObjetoById(id)).setDescripcion(descripcion);
                    try{
                        conector.modificarObjetoEnBd(getArrayDeObjetosSistema().get(getPosicionObjetoById(id)));
                    }
                    catch(SQLException | NullPointerException e){
                        mostrarMensajesError("NO SE HA PODIDO MODIFICAR EL OBJETO EN LA BASE DE DATOS");
                    }
                    cargarObjetoEnTabla(ArrayDeObjetosSistema);
                    mostrarMensajesOk("Objeto modificado correctamente");
                }
                 else{
                    mostrarMensajesError("El precio debe ser mayor que 0");
                }
            }
            catch(NumberFormatException formatoIncorrecto){
                mostrarMensajesError("El precio debe de ser un numero");
            }
            catch(NullPointerException campoVacio){
                mostrarMensajesError("No puede haber campos vacios");
            }
        }
    }
    
    /**
     * @brief Carga los objetos en la tabla
     * @param ArrayDeObjetos 
     */
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
    
    /**
     * @brief Carga las hermandades en la tabla
     * @param arrayDeInventarios
     */
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
    
    /**
     * @brief Carga el inventario del personaje en la tabla
     * @param nombrePersonaje 
     * @param servidorPersonaje
     */
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

    /**
     * @brief Carga el inventario en la tabla
     * @param idPersonaje del personaje
     */
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
    
    /**
     * @brief agrega un personaje a la hermandad
     * @param personaje 
     * @param hermandad
     */
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
    /**
     * @brief Añade un inventario al sistema
     * @param nombrePersonaje
     * @param servidorPersonaje
     */
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
                mostrarMensajesError("No se ha encontrado el personaje");
            } 
    }
    
   /**
    * @brief añade un objeto a un inventario
    * @param idObjeto 
    * @param idInventario
    */
    //Se realizan comprobaciones para ver si el objeto existe, si el inventario existe y si el objeto existe dentro del inventario
    public void aniadirObjetoaInventario(String idObjeto, String idInventario){
        int posicionObjeto =  getPosicionObjetoById(idObjeto);
        int posicionInventario = buscarInventarioPorId(idInventario);
        if(posicionObjeto != -1 && posicionInventario != -1){
            if(!getArrayDeInventariosSistema().get(posicionInventario).comprobarSiObjetoEnInventario(getObjetoById(idObjeto))){
                getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().add(getArrayDeObjetosSistema().get(posicionObjeto));

                //Insertamos el objeto en el inventario en la base de datos
                try{
                    conector.insertarObjetoEnInventario(getArrayDeObjetosSistema().get(posicionObjeto), getArrayDeInventariosSistema().get(posicionInventario));
                }
                catch(SQLIntegrityConstraintViolationException e){
                    mostrarMensajesError("PK de objeto-inventario repetida en BD");
                }
                catch(SQLException | NullPointerException e ){
                    mostrarMensajesError("ERROR AL INSERTAR OBJETO-INVENTARIO EN BD");
                }
                getArrayDeInventariosSistema().get(posicionInventario).setEspaciosOcupados(getArrayDeInventariosSistema().get(posicionInventario).getObjetosInventario().size());

                //Actualizamos el inventario en la base de datos
                try{
                    conector.modificarInventario(getArrayDeInventariosSistema().get(posicionInventario));
                }
                catch(SQLException | NullPointerException e){
                    mostrarMensajesError("NO SE HA PODIDO MODIFICAR EL INVENTARIO EN LA BASE DE DATOS");
                }
                cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                cargarPersonajesEnTabla(ArrayDePersonajesSistema);
                //Mensaje informativo de que se ha añadido correctamente
                mostrarMensajesOk("Objeto añadido correctamente");
            }
            else{
                //Mensaje de error de que el objeto ya existe en el inventario
                mostrarMensajesError("El objeto ya existe en el inventario");
            }
        }
        else{
            mostrarMensajesError("No se ha encontrado el objeto o el inventario");
        }
    }
    
    /**
     * @brief Vacia un inventario
     * @param idInventario
     */
    public void vaciarInventario(String idInventario){
        
        for(Inventario inventario : getArrayDeInventariosSistema()){
            if(inventario.getIdInventario().equals(idInventario)){
                for(int i = 0; i < inventario.getObjetosInventario().size(); i++){
                    try{
                        conector.borrarObjetoEnInventario(inventario.getObjetosInventario().get(i), inventario);
                        conector.modificarInventario(inventario);
                         inventario.getObjetosInventario().removeAll(inventario.getObjetosInventario());
                         inventario.setEspaciosOcupados(0);
                         cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
                    }
                    catch(SQLException | NullPointerException e){
                        mostrarMensajesError("NO SE HA PODIDO VACIAR EL INVENTARIO EN LA BASE DE DATOS");
                    }  
                }
               
            }
        }
        
    }
    
    /**
     * @brief Busca un inventario por su id
     * @param idInventario
     * @return posicion del inventario
     */
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

    /**
     * @brief comprueba que el nivel este entre 1-60
     * @param nivel
     * @return true si el nivel es correcto false si no lo es
     */
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

    /**
     * @brief añade un personaje al sistema, crea su inventario y se lo asigna al personaje. Añade el personaje y el inventario a la base de datos
     * @param nombre
     * @param servidor
     * @param Raza
     * @param nivel
     * @param faccion
     */
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

            //Bloque para capturar las excepciones que puede causar la base de datos
            try{
                conector.insertarPersonajeYInventarioEnBD(personajeAniadir, inventarioNuevo);
            }
            catch(SQLIntegrityConstraintViolationException e){
                mostrarMensajesError("PK de personaje repetida en BD");
            }
            catch(SQLException | NullPointerException e ){
                mostrarMensajesError("ERROR AL INSERTAR PERSONAJE EN BD");
            }
           
            //Traemos el personaje y lo añadimos al vector de personajes del sistema
            try{
                conector.leerPersonaje(nombre, servidor, ArrayDePersonajesSistema);
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("ERROR AL LEER PERSONAJE DE LA BASE DE DATOS");
            }
             int posicionPersonaje = buscarPersonajeEnSistema(nombre, servidor);
             //Añadimos el inventario al personaje que hemos introducido en el vector
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setAniadirInventarioaPersonaje(inventarioNuevo);
            //Añadimos el inventario al vector de inventarios
            getArrayDeInventariosSistema().add(inventarioNuevo);
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
            mostrarMensajesOk("Personaje añadido correctamente");
        }
        catch(NumberFormatException formatoIncorrecto){
            System.out.println(formatoIncorrecto.getMessage());
        }
    }
    
    /**
     * @brief busca un personaje en el sistema
     * @param nombre
     * @param servidor
     * @return posicion del personaje
     */
    public int buscarPersonajeEnSistema(String nombre, String servidor){
        for (int i = 0; i < this.getArrayDePersonajesDeSistema().size(); i++) {
            if (nombre.equals(this.getArrayDePersonajesDeSistema().get(i).getNombre()) && servidor.equals(this.getArrayDePersonajesDeSistema().get(i).getServidor())) {
                return i; // Si se encuentra el personaje, devuelve la posición
            }
        }
        return -1; // Si no se encuentra el personaje, devuelve -1
    }
    
    /**
     * @brief busca un personaje en el sistema por su id
     * @param idPersonaje
     * @return posicion del personaje
     */
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
    
   /**
    * @brief modifica un personaje del sistema
    * @param idPersonaje 
    * @param nombre
    * @param servidor
    * @param raza
    * @param nivel
    * @param faccion
    */
    public void modificarPersonaje(Integer idPersonaje, String nombre, String servidor, String raza, int nivel, String faccion){
        boolean correcto = true;
        int posicionPersonaje = buscarPersonajeEnSistemaPorId(idPersonaje);
        //Comprobar si el nombre y el servidor que quiere introducir ya existe en el sistema
        for(int i = 0; i < getArrayDePersonajesDeSistema().size(); i++){
            if(getArrayDePersonajesDeSistema().get(i).getNombre().equals(nombre) && getArrayDePersonajesDeSistema().get(i).getServidor().equals(servidor) && posicionPersonaje != i){
                mostrarMensajesError("El personaje ya existe");
                i = getArrayDePersonajesDeSistema().size();
                correcto = false;
            }
        }
         if(posicionPersonaje != -1 && correcto == true){
            
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNombre(nombre);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setServidor(servidor);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setRaza(raza);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setNivel(nivel);
            getArrayDePersonajesDeSistema().get(posicionPersonaje).setFaccion(faccion);
            try{
                conector.modificarPersonajeBd(getArrayDePersonajesDeSistema().get(posicionPersonaje));
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("NO SE HA PODIDO MODIFICAR EL PERSONAJE EN LA BASE DE DATOS");
            }
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
         }
    }
    
    /**
     * @brief Borra un personaje del sistema
     * @param nombre
     * @param servidor
     */
    public void borrarPersonaje(String nombre, String servidor){
        
        int posicionPersonaje = buscarPersonajeEnSistema(nombre, servidor);
        if(posicionPersonaje != -1){
            //Borramos el personaje de la base de datos
            try{
                conector.borrarPersonajeIventarioBd(getArrayDePersonajesDeSistema().get(posicionPersonaje));
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("NO SE HA PODIDO BORRAR EL PERSONAJE DE LA BASE DE DATOS");
            }
            //Primero borra todos los objetos que contiene el inventario
            vaciarInventario(getArrayDePersonajesDeSistema().get(posicionPersonaje).getInventario().getIdInventario());
            //Borra el inventario del sistema
            getArrayDeInventariosSistema().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje).getInventario());
            //Busca en el array de hermandades de sistema si el personaje pertenece a alguna hermandad y lo borra de la lista de miembros
            for(Hermandad hermandad : getArrayDeHermandadesSistema()){
               for(int i = 0; i < hermandad.getListaMiembros().size(); i++){
                    if(hermandad.getListaMiembros().get(i).getIdPersonaje().equals(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje())){
                        hermandad.getListaMiembros().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                        hermandad.setNumeroMiembros(hermandad.getListaMiembros().size());
                        //Actualiza el numero de miembros de las hermandades a las que pertenecia el personaje
                        try{
                            conector.modificarHermandadEnBd(hermandad);
                        }
                        catch(SQLException | NullPointerException e){
                            mostrarMensajesError("NO SE HA PODIDO MODIFICAR LA HERMANDAD EN LA BASE DE DATOS");
                        }
                    }
                }
            }
            getArrayDePersonajesDeSistema().remove(posicionPersonaje);
            cargarInventariosSistmemaEnTabla(ArrayDeInventariosSistema);
            cargarPersonajesEnTabla(ArrayDePersonajesSistema);
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
        }
        else{
            mostrarMensajesError("No se ha encontrado el personaje");
        }
    }
    
    /**
     * @brief Carga los personajes en la tabla
     * @param ArrayDePersonajes
     */
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
     /**
      * @brief añade una hermandad al sistema y a la bd
      * @pre los datos vienen filtrados de la vista
      * @param nombre
      * @param servidor
      */
     public void aniadirHermandad(String nombre, String servidor){
        if(buscarHermandadPorNombre(nombre, servidor) == -1){
            Hermandad hermandad = new Hermandad();
            ArrayList<String> listaIdsHermandad = new ArrayList<>();
            try{
                listaIdsHermandad = conector.getListaIdsHermandades();
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("ERROR AL LEER IDS DE HERMANDADES DE LA BASE DE DATOS");
            }
            //En caso de que haya una id repetida, se genera una nueva
            for (int i = 0; i < listaIdsHermandad.size(); i++) {
                if (listaIdsHermandad.get(i).equals(hermandad.getIdHermandad())) {
                    String newId = hermandad.generateNewIdHermandad();
                    hermandad.setIdHermandad(newId);
                    System.out.println("Cambiando id de hermandad" + hermandad.getIdHermandad() + "por id" + newId + "porque ya existe");
                }
            }
            hermandad.setNombreHermandad(nombre);
            hermandad.setServidorHermandad(servidor);
            ArrayDeHermandadesSistema.add(hermandad);
            //Insertamos la hermandad 
            try{
                conector.insertarHermandadEnBD(hermandad);
            }
            catch(SQLIntegrityConstraintViolationException e){
                mostrarMensajesError("PK de hermandad repetida en BD");
            }
            catch(SQLException | NullPointerException e ){
                mostrarMensajesError("ERROR AL INSERTAR HERMANDAD EN BD");
            }
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
        }
        else{
            System.out.println("La hermandad ya existe");
        }
     }
     /**
      * @brief Busca una hermandad por su nombre y servidor
      * @param nombre
      * @param servidor
      * @return Devuelve la posicion en la que se encuentra la hermandad en el array de hermandades de sistema o -1 si no la encuentra
      */
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

     /**
      * @brief añade un personaje a la hermandad
        * @param nombrePersonaje
        * @param servidorPersonaje
        * @param nombreHermandad
        * @param servidorHermandad
        *@pre los datos vienen filtrados de la vista
      */
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
                    
                    //Insertamos la hermandad-personaje en la base de datos
                    try{
                        conector.insertarPersonajeHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje), getArrayDeHermandadesSistema().get(posicionHermandad));
                    }
                    catch(SQLIntegrityConstraintViolationException e){
                        mostrarMensajesError("PK de hermandad-personaje repetida en BD");
                    }
                    catch(SQLException | NullPointerException e ){
                        mostrarMensajesError("ERROR AL INSERTAR HERMANDAD-PERSONAJE EN BD");
                    }
                    
                    //Actualizamos el numero de miembros de la hermnadad
                    try{
                        conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
                    }
                    catch(SQLException | NullPointerException e){
                        mostrarMensajesError("NO SE HA PODIDO MODIFICAR LA HERMANDAD EN LA BASE DE DATOS");
                    }
                    cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
                    cargarPersonajesHermandadEnTabla(getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad());
                    mostrarMensajesOk("Personaje añadido correctamente");

                }
                else{
                    mostrarMensajesError("La hermandad esta llena o el personaje ya esta en la hermandad");
                }
            }
            else{
                
                 System.out.println("No se ha encontrado la hermandad o el personaje");
            }                
        }

        /**
         * @brief borra personajes de una hermandad y de la lista de hermandades del personaje tambien de la bd
         * @pre los datos vienen filtrados de la vista
         * @param nombrePersonaje
         * @param servidorPersonaje
         * @param nombreHermandad
         * @param servidorHermandad
         */
     
     //Recibe el nombre y el servidor del personaje y lo borra de la lista de miembros de la hermandad, y de la lista de hermandades del personaje borra esa hermnadad
        public void borrarPersonajeDeHermandad(String nombrePersonaje, String servidorPersonaje, String nombreHermandad, String servidorHermandad){
                int posicionPersonaje = buscarPersonajeEnSistema(nombrePersonaje, servidorPersonaje);
                int posicionHermandad = buscarHermandadPorNombre(nombreHermandad, servidorHermandad);
                if(posicionPersonaje != -1 && posicionHermandad != -1){
                    //Comprueba si el personaje esta en la hermandad
                    if(comprobarSiPersonajeEnHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje).getIdPersonaje(), getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad())){
                        //Borramos el personaje de la tabla hermandadPersonaje
                        try{
                            conector.borrarPersonajeHermandad(getArrayDePersonajesDeSistema().get(posicionPersonaje), getArrayDeHermandadesSistema().get(posicionHermandad));
                        }
                        catch(SQLException | NullPointerException e){
                            mostrarMensajesError("NO SE HA PODIDO BORRAR EL PERSONAJE-HERMANDAD DE LA BASE DE DATOS");
                        }
                        getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().remove(getArrayDePersonajesDeSistema().get(posicionPersonaje));
                        getArrayDeHermandadesSistema().get(posicionHermandad).setNumeroMiembros(getArrayDeHermandadesSistema().get(posicionHermandad).getListaMiembros().size());
                        getArrayDePersonajesDeSistema().get(posicionPersonaje).getListaHermandadades().remove(getArrayDeHermandadesSistema().get(posicionHermandad));
                        //Actualizamos el numero de miembros de la hermnadad
                        try{
                            conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
                        }
                        catch(SQLException | NullPointerException e){
                            mostrarMensajesError("NO SE HA PODIDO MODIFICAR LA HERMANDAD EN LA BASE DE DATOS");
                        }
                        cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
                        cargarPersonajesHermandadEnTabla(getArrayDeHermandadesSistema().get(posicionHermandad).getIdHermandad());
                        mostrarMensajesOk("Personaje borrado correctamente");
                    }
                    else{
                        mostrarMensajesError("El personaje no esta en la hermandad");
                    }
                }
                else{
                    System.out.println("No se ha encontrado la hermandad o el personaje");
                }
        }

        /**
         * @brief modifica una hermandad
         * @param nombreOriginal
         * @param servidorOriginal
         * @param nombreCambiar
         * @param servidorCambiar
         */
     //Modifica una hermandad
     public void modificarHermandad(String nombreOriginal, String servidorOriginal, String nombreCambiar, String servidorCambiar){
            boolean correcto = true;
            //Comprobamos si la hermandad ya existe en el sistema
            for(int i = 0; i < getArrayDeHermandadesSistema().size(); i++){
                if(getArrayDeHermandadesSistema().get(i).getNombreHermandad().equals(nombreCambiar) && getArrayDeHermandadesSistema().get(i).getServidorHermandad().equals(servidorCambiar)){
                    mostrarMensajesError("La hermandad ya existe");
                    i = getArrayDeHermandadesSistema().size();
                    correcto = false;
                }
            }
            //Te busca la posicion de la hermandad que quieres modificar
            int posicionHermandad = buscarHermandadPorNombre(nombreOriginal, servidorOriginal);
            int posicionHermandadCambiar = buscarHermandadPorNombre(nombreCambiar, servidorCambiar);
            if(posicionHermandad != -1){
                //Comprueba si ya existe una hermandad con el mismo nombre y el mismo servidor
                if(posicionHermandadCambiar == -1 && correcto == true){
                    getArrayDeHermandadesSistema().get(posicionHermandad).setNombreHermandad(nombreCambiar);
                    getArrayDeHermandadesSistema().get(posicionHermandad).setServidorHermandad(servidorCambiar);
                    //Una vez que se han cambiado el nombre y el servidor en el controlador lo cambiamos en la bd.
                    try{
                        conector.modificarHermandadEnBd(getArrayDeHermandadesSistema().get(posicionHermandad));
                    }
                    catch(SQLException | NullPointerException e){
                        mostrarMensajesError("NO SE HA PODIDO MODIFICAR LA HERMANDAD EN LA BASE DE DATOS");
                    }
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

     /**
      * @brief borra una hermandad
      * @param nombreHermandad
      * @param servidorHermandad
      */

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
            try{
                conector.borrarHermandadBd(hermandadABorrar);
            }
            catch(SQLException | NullPointerException e){
                mostrarMensajesError("NO SE HA PODIDO BORRAR LA HERMANDAD DE LA BASE DE DATOS");
            }
            cargarHermandadesEnTabla(ArrayDeHermandadesSistema);
        } else {
            System.out.println("No se ha encontrado la hermandad");
        }
    }


    /**
     * @brief Carga las hermandades en la tabla
     * @param ArrayDeHermandadesSistema
     */
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

    /**
     * @brief Carga los personajes de la hermandad en la tabla
     * @param idHermandad
     */
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
    
    /**
     * @brief cierra la conexion con la base de datos
     * @param nombreDb de la base de datos a la que se cierra la conexion
     */
    public void cerrarConexionDesdeControlador(String nombreDb){
          Conector.getInstancia(nombreDb).cerrarConexion(nombreDb);
    }
}
  
