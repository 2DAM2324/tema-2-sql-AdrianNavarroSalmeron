/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *
 * @author AdrianNS
 */
public class Conector {

    private  static Conector instancia = null;
    private Connection conn = null;

    private Conector() {
    }
    
   
    public static Conector getInstancia(){
        if(instancia == null){
            instancia = new Conector();
            instancia.conn = instancia.conectar();
            
        }
        return instancia;
    }
    
    public Connection getConexion(){
        return conn;
    }
    
    public  Connection conectar() {
        try {
            if(conn == null){
                 // Ruta a la base de datos SQLite
                String url = "jdbc:sqlite:db.sqlite";

                // Conectar a la base de datos
                conn = DriverManager.getConnection(url);
                System.out.println("Conexión establecida a SQLite.");
            }
           
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }

    /**
     * @brief Crea la base de datos y las tablas si no existen
     */
public void crearBaseDatos() {
    Connection conexion = instancia.getConexion();
    // Conectar a la base de datos

    //Crea la tabla de objetos
        try (PreparedStatement stmtObjeto = conexion.prepareStatement(   
        "CREATE TABLE IF NOT EXISTS objeto ("
        + "idObjeto TEXT PRIMARY KEY NOT NULL,"
        + "rareza TEXT NOT NULL,"
        + "descripcion TEXT NOT NULL,"
        + "precio DOUBLE NOT NULL,"
        + " nombreObjeto TEXT NOT NULL )")) {
            stmtObjeto.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla Objeto:" + e.getMessage());
        }
            
        // Crear tabla hermandad
        try (PreparedStatement stmtHermandad = conexion.prepareStatement(
                "CREATE TABLE IF NOT EXISTS hermandad ("
                +"IdHermandad TEXT PRIMARY KEY NOT NULL,"
                + "nombreHermandad TEXT NOT NULL,"
                + "servidorHermandad TEXT NOT NULL,"
                + "numeroMiembros INTEGER NOT NULL)")) {
            stmtHermandad.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla Hermandad:" + e.getMessage());
        }
            
        // Crear tabla inventario
        try (PreparedStatement stmtInventario = conexion.prepareStatement(
                "CREATE TABLE IF NOT EXISTS inventario ("
                + "idInventario TEXT PRIMARY KEY, "
                + "idPersonaje INTEGER , "
                + "capacidadMaxima INTEGER NOT NULL, "
                + "espaciosOcupados INTEGER NOT NULL "
                    +")")) {
            stmtInventario.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla Inventario:" + e.getMessage());
        }

        // Crear tabla personaje
        try (PreparedStatement stmtPersonaje = conexion.prepareStatement(
                "CREATE TABLE IF NOT EXISTS personaje ("
                + "idPersonaje INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "servidor TEXT NOT NULL, "
                + "faccion TEXT NOT NULL, "
                + "raza TEXT NOT NULL, "
                + "idInventario TEXT,"
                + "nivel INTEGER NOT NULL,"
                + "FOREIGN KEY (idInventario) REFERENCES inventario(idInventario)" 
                +")")) {
            stmtPersonaje.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla Personaje:" + e.getMessage());
        }

        //Crear tabla relacion inventario objeto
        try(PreparedStatement stmtInventarioObjeto = conexion.prepareStatement(
            "CREATE TABLE IF NOT EXISTS InventarioObjeto ("
            + "idInventario TEXT NOT NULL,"
            + "idObjeto TEXT NOT NULL,"
            + "PRIMARY KEY (idInventario, idObjeto),"
            + "FOREIGN KEY (idInventario) REFERENCES inventario(idInventario),"
            + "FOREIGN KEY (idObjeto) REFERENCES objeto(idObjeto)"
            + ")")){
            stmtInventarioObjeto.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al crear la tabla InventarioObjeto:" + e.getMessage());
        }
            
        try(PreparedStatement stmtHermandadPersonaje = conexion.prepareStatement(
            "CREATE TABLE IF NOT EXISTS hermandadPersonaje("
            + "idHermandad TEXT NOT NULL,"
            + "idPersonaje INTEGER NOT NULL, "
            + "PRIMARY KEY (idHermandad, idPersonaje),"
            + "FOREIGN KEY (idHermandad) REFERENCES hermandad(idHermandad),"
            + "FOREIGN KEY (idPersonaje) REFERENCES personaje(idPersonaje)"
            + ") ")){
        stmtHermandadPersonaje.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al crear la tabla hermandadPersonaje:" + e.getMessage());
        }
        
        System.out.println("Base de datos y tabla creadas correctamente.");
    }

    /**
     * @brief Borra la base de datos y las tablas
     */
    public void borrarDb(){
        Connection conexion = instancia.getConexion();

        //Borrar relacionHermandadPersonaje
        try(PreparedStatement stmtBorrarHermandadPersonaje = conexion.prepareStatement(
            "DROP TABLE IF EXISTS hermandadPersonaje"
            )){
        stmtBorrarHermandadPersonaje.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }

        //Borrar inventarioObjeto
        try(PreparedStatement stmtBorrarInventarioObjeto = conexion.prepareStatement(
            "DROP TABLE IF EXISTS InventarioObjeto"
            )){
        stmtBorrarInventarioObjeto.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }

        //Borrar personaje
        try(PreparedStatement stmtBorrarPersonaje = conexion.prepareStatement(
            "DROP TABLE IF EXISTS personaje"
            )){
        stmtBorrarPersonaje.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }

        //Borrar inventario
        try(PreparedStatement stmtBorrarInventario = conexion.prepareStatement(
            "DROP TABLE IF EXISTS inventario"
            )){
        stmtBorrarInventario.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }

        //Borrar hermandad
        try(PreparedStatement stmtBorrarHermandad = conexion.prepareStatement(
            "DROP TABLE IF EXISTS hermandad"
            )){
        stmtBorrarHermandad.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }

        //Borrar objeto
        try(PreparedStatement stmtBorrarObjeto = conexion.prepareStatement(
            "DROP TABLE IF EXISTS objeto"
            )){
        stmtBorrarObjeto.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar las tablas:" + e.getMessage());
        }
    }
    
    //CRUD de OBJETO
    /**
     * @brief Inserta un objeto en la base de datos
     * @param objeto Objeto a insertar
     * @throws SQLException
     * @throws SQLIntegrityConstraintViolationException
     * @throws NullPointerException
     */
    public void insertarObjetoEnBd(Objeto objeto) throws SQLException, SQLIntegrityConstraintViolationException, NullPointerException {
         String sql = "INSERT INTO objeto (idObjeto, rareza, descripcion, precio, nombreObjeto) VALUES (?, ?, ?, ?, ?)";
         Connection conexion = instancia.getConexion();
        // Conectar a la base de datos
        try{
            PreparedStatement  consulta = conexion.prepareStatement(sql);
            consulta.setString(1, objeto.getIdObjeto());
            consulta.setString(2, objeto.getRareza());
            consulta.setString(3, objeto.getDescripcion());
            consulta.setDouble(4, objeto.getPrecio());
            consulta.setString(5, objeto.getNombreObjeto()); 
            consulta.executeUpdate();
    
        }
        catch(SQLIntegrityConstraintViolationException e){
        }
        catch(SQLException | NullPointerException e){
        }
    }

    /**
     * @brief Modifica un objeto en la base de datos
     * @param objeto Objeto a modificar
     */

    public void modificarObjetoEnBd(Objeto objeto){
        String sql = "UPDATE objeto SET rareza = ?, descripcion = ?, precio = ?, nombreObjeto = ? WHERE idObjeto = ?";
        Connection conexion = instancia.getConexion();
        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, objeto.getRareza());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setDouble(3, objeto.getPrecio());
            consulta.setString(4, objeto.getNombreObjeto());
            consulta.setString(5, objeto.getIdObjeto());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al modificar Objeto:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Objeto NULL:" + e.getMessage());
        }
    }
    
    /**
     * @brief Borra un objeto de la base de datos
     * @param objeto Objeto a borrar
     * @throws SQLException
     * @throws NullPointerException
     */
    public void borrarObjetoDeBd(Objeto objeto) throws SQLException, NullPointerException {
        String sqlTablaObjeto = "DELETE FROM objeto WHERE idObjeto = ?";
        String sqlTablaInventarioObjeto = "DELETE FROM InventarioObjeto WHERE idObjeto = ?";
        Connection conexion = instancia.getConexion();
        try {
            PreparedStatement consultaInventarioObjeto = conexion.prepareStatement(sqlTablaInventarioObjeto);
            PreparedStatement consultaObjeto = conn.prepareStatement(sqlTablaObjeto);
            consultaInventarioObjeto.setString(1, objeto.getIdObjeto());
            consultaInventarioObjeto.executeUpdate();
            consultaObjeto.setString(1, objeto.getIdObjeto());
            consultaObjeto.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al borrar Objeto:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Objeto NULL:" + e.getMessage());
        }
    }
    
    /**
     * @brief Lee un objeto de la base de datos
     * @param  ObjetosSistema ArrayList donde se almacenarán los objetos
     */
    public void leerObjetosDeBd(ArrayList ObjetosSistema){
        String sql = "SELECT * FROM objeto";
        Connection conexion = instancia.getConexion();
        try{
            Statement statementObjeto = conexion.createStatement();
            ResultSet resulsetObjeto = statementObjeto.executeQuery(sql);
            while(resulsetObjeto.next()){
                String idObjeto = resulsetObjeto.getString("idObjeto");
                String rareza = resulsetObjeto.getString("rareza");
                String descripcion = resulsetObjeto.getString("descripcion");
                double precio = resulsetObjeto.getDouble("precio");
                String nombreObjeto = resulsetObjeto.getString("nombreObjeto");
                Objeto objeto = new Objeto(idObjeto, rareza, descripcion, precio, nombreObjeto);
                ObjetosSistema.add(objeto);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer Objetos:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Objeto NULL:" + e.getMessage());
        }
    }
    
    //CRUD PERSONAJE
    
    /**
     * @brief Inserta un personaje en la base de datos y su inventario
     * @param personaje Personaje a insertar
     * @param inventario Inventario del personaje a insertar
     */
    public void insertarPersonajeYInventarioEnBD(Personaje personaje, Inventario inventario) {
        String sqlInventario = "INSERT INTO inventario (idInventario, capacidadMaxima, espaciosOcupados) VALUES (?, ?, ?)";
        String sqlPersonaje = "INSERT INTO personaje (nombre, servidor, faccion, raza, idInventario, nivel) VALUES (?, ?, ?, ?, ?, ?)";
        String modificacionInventario = "UPDATE inventario SET idPersonaje = ? WHERE idInventario = ?";
        Connection conexion = instancia.getConexion();
        PreparedStatement consultaInventario = null;
        PreparedStatement consultaPersonaje = null;
        PreparedStatement consultaInventarioModificacion = null;

        try {
            conexion.setAutoCommit(false);

            // Insertamos el inventario
            consultaInventario = conexion.prepareStatement(sqlInventario);
            consultaInventario.setString(1, inventario.getIdInventario());
            consultaInventario.setInt(2, inventario.getCapacidadMaxima());
            consultaInventario.setInt(3, inventario.getEspaciosOcupados());
            consultaInventario.executeUpdate();

            // Insertamos el personaje
            consultaPersonaje = conexion.prepareStatement(sqlPersonaje);
            consultaPersonaje.setString(1, personaje.getNombre());
            consultaPersonaje.setString(2, personaje.getServidor());
            consultaPersonaje.setString(3, personaje.getFaccion());
            consultaPersonaje.setString(4, personaje.getRaza());
            consultaPersonaje.setString(5, inventario.getIdInventario());
            consultaPersonaje.setInt(6, personaje.getNivel());
            consultaPersonaje.executeUpdate();

            // Recuperamos la id que ha creado la base de datos para nuestro personaje
            ResultSet generatedKeys = consultaPersonaje.getGeneratedKeys();
            int idPersonaje = -1;
            if (generatedKeys.next()) {
                idPersonaje = generatedKeys.getInt(1);
            }

            // Añadimos el id del personaje al inventario
            consultaInventarioModificacion = conexion.prepareStatement(modificacionInventario);
            consultaInventarioModificacion.setInt(1, idPersonaje);
            consultaInventarioModificacion.setString(2, inventario.getIdInventario());
            consultaInventarioModificacion.executeUpdate();

            // Validamos la transacción
            conexion.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            System.err.println("CLAVE PRIMARIA REPETIDA EN PERSONAJE");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR EN LA CONSULTA PARA INSERTAR PERSONAJE");
        } finally {
            try {
                if (consultaInventario != null) {
                    consultaInventario.close();
                }
                if (consultaPersonaje != null) {
                    consultaPersonaje.close();
                }
                if (consultaInventarioModificacion != null) {
                    consultaInventarioModificacion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
            catch(NullPointerException e){
                e.printStackTrace();
                System.err.println("Objeto NULL:" + e.getMessage());
            }
        }
    }


    /**
     * 
 /
     *
     **
     * @param nombre del personaje
     * @param servidor servidor del personaje
     * @param ArrayListDePersonajesSistema ArrayList donde se almacenarán los
     * personajes
     */
    public void leerPersonaje(String nombre, String servidor, ArrayList<Personaje> ArrayListDePersonajesSistema) {
        String sql = "SELECT idPersonaje, nombre, servidor, faccion, raza, idInventario, nivel FROM personaje WHERE nombre = ? AND servidor = ?";
        Connection conexion = instancia.getConexion();

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, servidor);

            ResultSet resulsetPersonaje = preparedStatement.executeQuery();

            while (resulsetPersonaje.next()) {
                Personaje personaje = new Personaje();
                Integer idPersonaje = resulsetPersonaje.getInt("idPersonaje");
                String nombrePersonaje = resulsetPersonaje.getString("nombre");
                String servidorPersonaje = resulsetPersonaje.getString("servidor");
                String faccionPersonaje = resulsetPersonaje.getString("faccion");
                String razaPersonaje = resulsetPersonaje.getString("raza");
                Integer idInventario = resulsetPersonaje.getInt("idInventario");
                Integer nivelPersonaje = resulsetPersonaje.getInt("nivel");

                personaje.setIdPersonaje(idPersonaje);
                personaje.setNombre(nombrePersonaje);
                personaje.setServidor(servidorPersonaje);
                personaje.setFaccion(faccionPersonaje);
                personaje.setRaza(razaPersonaje);
                personaje.setNivel(nivelPersonaje);

                ArrayListDePersonajesSistema.add(personaje);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Personaje NULL:" + e.getMessage());
        }
    }

    /**
     * @brief lee un personaje de la base de datos y cada inventario que lee lo mete en el array de inventarios
     * @param inventariosSistema ArrayList donde se almacenarán los inventarios
     */
    public void leerInventario(ArrayList inventariosSistema){
        String sql = "SELECT * FROM inventario";
        Connection conexion = instancia.getConexion();
        try{
            Statement statementInventario = conexion.createStatement();
            ResultSet resulsetInventario = statementInventario.executeQuery(sql);
            while(resulsetInventario.next()){
                String idInventario = resulsetInventario.getString("idInventario");
                int capacidadMaxima = resulsetInventario.getInt("capacidadMaxima");
                int espaciosOcupados = resulsetInventario.getInt("espaciosOcupados");
                Inventario inventario = new Inventario(idInventario, capacidadMaxima, espaciosOcupados);
                inventariosSistema.add(inventario);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer Inventario:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Inventario NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Modifica un personaje en la base de datos
     * @param personaje Personaje a modificar
     */
    public void modificarPersonajeBd(Personaje personaje){
        String sql = "UPDATE personaje SET nombre = ?, servidor = ?, faccion = ?, raza = ?, nivel = ? WHERE idPersonaje = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, personaje.getNombre());
            consulta.setString(2, personaje.getServidor());
            consulta.setString(3, personaje.getFaccion());
            consulta.setString(4, personaje.getRaza());
            consulta.setInt(5, personaje.getNivel());
            consulta.setInt(6, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al modificar Personaje:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Objeto NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Borra un personaje de la base de datos. Primero borra el personaje de la hermandad, luego su inventario y por ultimo el personaje
     * @param personaje Personaje a borrar
     */
    public void borrarPersonajeIventarioBd(Personaje personaje){
        String sqlTablaPersonaje = "DELETE FROM personaje WHERE idPersonaje = ?";
        String sqlTablaInventario = "DELETE FROM inventario WHERE idInventario = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consultaPersonaje = conexion.prepareStatement(sqlTablaPersonaje);
            PreparedStatement consultaInventario = conexion.prepareStatement(sqlTablaInventario);
            PreparedStatement consultaHermandadPersonaje = conexion.prepareStatement("DELETE FROM hermandadPersonaje WHERE idPersonaje = ?");
            
            //Borramos la relacion con la hermandad
            consultaHermandadPersonaje.setInt(1, personaje.getIdPersonaje());
            consultaHermandadPersonaje.executeUpdate();
            //Borramos el inventario del personaje
            consultaInventario.setString(1, personaje.getInventario().getIdInventario());
            consultaInventario.executeUpdate();
            //Borramos el personaje
            consultaPersonaje.setInt(1, personaje.getIdPersonaje());
            consultaPersonaje.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar Personaje:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Personaje NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Lee un personaje de la base de datos y lo añade al array de personajes
     * @param personajesSitema ArrayList donde se almacenarán los personajes
     * @param inventariosSistema ArrayList donde esta el inventario del personaje
     * @param hermandadesSistema ArrayList donde se busca las hermandades donde esta el personaje
     */
    public void leerPersonajeDeBd(ArrayList personajesSitema, ArrayList inventariosSistema, ArrayList<Hermandad> hermandadesSistema){
        String sql = "SELECT * FROM personaje";
        Connection conexion = instancia.getConexion();

        try{
            Statement statementPersonaje = conexion.createStatement();
            ResultSet resulsetPersonaje = statementPersonaje.executeQuery(sql);
            while(resulsetPersonaje.next()){
                Integer idPersonaje = resulsetPersonaje.getInt("idPersonaje");
                String nombre = resulsetPersonaje.getString("nombre");
                String servidor = resulsetPersonaje.getString("servidor");
                String faccion = resulsetPersonaje.getString("faccion");
                String raza = resulsetPersonaje.getString("raza");
                int nivel = resulsetPersonaje.getInt("nivel");
                String idInventario = resulsetPersonaje.getString("idInventario");
                Inventario inventario = new Inventario();
                for(int i = 0; i < inventariosSistema.size(); i++){
                    if(((Inventario)inventariosSistema.get(i)).getIdInventario().equals(idInventario)){
                        inventario = (Inventario)inventariosSistema.get(i);
                    }
                }
                //Creamos el personaje con los parametros de la bd 
               Personaje personaje = new Personaje();
               personaje.setNombre(nombre);
               personaje.setServidor(servidor);
               personaje.setFaccion(faccion);
               personaje.setRaza(raza);
               personaje.setNivel(nivel);
               personaje.setAniadirInventarioaPersonaje(inventario);
               personaje.setIdPersonaje(idPersonaje);
                //Leemos las hermandades del personaje
                for(int i = 0; i < hermandadesSistema.size(); i++){
                    for(int j = 0; j < hermandadesSistema.get(i).getListaMiembros().size(); j++){
                        if(hermandadesSistema.get(i).getListaMiembros().get(j).getIdPersonaje().equals(idPersonaje)){
                            personaje.getListaHermandadades().add(hermandadesSistema.get(i));
                        }
                    }
                }
                personaje.getInventario().setIdPersonaje(personaje.getIdPersonaje());
                personajesSitema.add(personaje);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer Personaje:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Personaje NULL:" + e.getMessage());
        }

    }

    //Crud de hermandad

    /**
     * @brief Inserta una hermandad en la base de datos
     * @param hermandad Hermandad a insertar
     */
    public void insertarHermandadEnBD(Hermandad hermandad) {
        String sql = "INSERT INTO hermandad (idHermandad, nombreHermandad, servidorHermandad, numeroMiembros) VALUES (?, ?, ?, ?)";
        Connection conexion = instancia.getConexion();
        
        try{
                PreparedStatement  consulta = conexion.prepareStatement(sql);
                consulta.setString(1, hermandad.getIdHermandad());
                consulta.setString(2, hermandad.getNombreHermandad());
                consulta.setString(3, hermandad.getServidorHermandad());
                consulta.setInt(4, hermandad.getNumeroMiembros());
                consulta.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al insertar Hermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Hermandad NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Modifica una hermandad en la base de datos, no se puede modificar la ID
     * @param hermandad Hermandad a modificar
     */
    public void modificarHermandadEnBd(Hermandad hermandad){
        String sql = "UPDATE hermandad SET nombreHermandad = ?, servidorHermandad = ?, numeroMiembros = ? WHERE idHermandad = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, hermandad.getNombreHermandad());
            consulta.setString(2, hermandad.getServidorHermandad());
            consulta.setInt(3, hermandad.getNumeroMiembros());
            consulta.setString(4, hermandad.getIdHermandad());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al modificar Hermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Hermandad NULL:" + e.getMessage());
        }
    }
    
    /**
     * @brief Borra una hermandad de la base de datos
     * @param hermandad Hermandad a borrar
     */
    public void borrarHermandadBd(Hermandad hermandad) {
        String sqlTablaHermandad = "DELETE FROM hermandad WHERE idHermandad = ?";
        String sqlTablaHermandadPersonaje = "DELETE FROM hermandadPersonaje WHERE idHermandad = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consultaHermandadPersonaje = conexion.prepareStatement(sqlTablaHermandadPersonaje);
            PreparedStatement consultaHermandad = conexion.prepareStatement(sqlTablaHermandad);
            consultaHermandadPersonaje.setString(1, hermandad.getIdHermandad());
            consultaHermandadPersonaje.executeUpdate();
            consultaHermandad.setString(1, hermandad.getIdHermandad());
            consultaHermandad.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar Hermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Hermandad NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Lee una hermandad de la base de datos y la añade al array de hermandades
     * @param ArrayDeHermandadesSistema ArrayList donde se almacenarán las hermandades
     */
    public void leerHermandad(ArrayList<Hermandad> ArrayDeHermandadesSistema){
        String sql = "SELECT * FROM hermandad";
        Connection conexion = instancia.getConexion();

        try{
            Statement statementHermandad = conexion.createStatement();
            ResultSet resulsetHermandad = statementHermandad.executeQuery(sql);
            while(resulsetHermandad.next()){
                String idHermandad = resulsetHermandad.getString("idHermandad");
                String nombreHermandad = resulsetHermandad.getString("nombreHermandad");
                String servidorHermandad = resulsetHermandad.getString("servidorHermandad");
                int numeroMiembros = resulsetHermandad.getInt("numeroMiembros");
                Hermandad hermandad = new Hermandad(idHermandad, nombreHermandad, servidorHermandad, numeroMiembros);
                ArrayDeHermandadesSistema.add(hermandad);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer Hermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Hermandad NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Lee una hermandad de la base de datos y la añade al array de hermandades
     * @param arrayDeHermandadesSistema ArrayList de hermandades en la que buscamos los personajes para saber si son miembros de esa hermandad
     * @param personajesSistema ArrayList de personajes en el que buscamos las hermandades para saber si son miembros de esa hermandad
     */
    public void leerHermandadPersonaje(ArrayList<Hermandad> arrayDeHermandadesSistema, ArrayList<Personaje> personajesSistema){
        String sql = "SELECT * FROM hermandadPersonaje";
        Connection conexion = instancia.getConexion();

        try{
            Statement statementHermandadPersonaje = conexion.createStatement();
            ResultSet resulsetHermandadPersonaje = statementHermandadPersonaje.executeQuery(sql);
            while(resulsetHermandadPersonaje.next()){
                String idHermandad = resulsetHermandadPersonaje.getString("idHermandad");
                Integer idPersonaje = resulsetHermandadPersonaje.getInt("idPersonaje");
                //Recorremos el vector de hermandades de sistema, para cada hermandad buscamos sus miembros y los añadimos a la lista de miembros de esa hermandad
                for(int i = 0; i < arrayDeHermandadesSistema.size(); i++){
                    if(arrayDeHermandadesSistema.get(i).getIdHermandad().equals(idHermandad)){
                        for(int j = 0; j < personajesSistema.size(); j++){
                            if(personajesSistema.get(j).getIdPersonaje().equals(idPersonaje)){
                                arrayDeHermandadesSistema.get(i).getListaMiembros().add(personajesSistema.get(j));
                            }
                        }
                    }
                }
                //Recorremos el vector de personajes de sistema, para cada personaje buscamos sus hermandades y las añadimos a la lista de hermandades de ese personaje
                for(int i = 0; i < personajesSistema.size(); i++){
                    if(personajesSistema.get(i).getIdPersonaje().equals(idPersonaje)){
                        for(int j = 0; j < arrayDeHermandadesSistema.size(); j++){
                            if(arrayDeHermandadesSistema.get(j).getIdHermandad().equals(idHermandad)){
                                personajesSistema.get(i).getListaHermandadades().add(arrayDeHermandadesSistema.get(j));
                            }
                        }
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer HermandadPersonaje:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("HermandadPersonaje NULL:" + e.getMessage());
        }
    }

    /**
     * @brief Inserta un personaje en una hermandad
     * @param personaje Personaje a insertar
     * @param hermandad Hermandad en la que se inserta el personaje
     */
    public void insertarPersonajeHermandad(Personaje personaje, Hermandad hermandad){
        String sql = "INSERT INTO hermandadPersonaje (idHermandad, idPersonaje) VALUES (?, ?)";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, hermandad.getIdHermandad());
            consulta.setInt(2, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al insertar PersonajeHermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("PersonajeHermandad NULL:" + e.getMessage());

        }
    }

    /**
     * @brief Borra un personaje de una hermandad
     * @param personaje Personaje a borrar
     * @param hermandad Hermandad de la que se borra el personaje
     */
    public void borrarPersonajeHermandad(Personaje personaje, Hermandad hermandad){
        String sql = "DELETE FROM hermandadPersonaje WHERE idHermandad = ? AND idPersonaje = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, hermandad.getIdHermandad());
            consulta.setInt(2, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar PersonajeHermandad:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("PersonajeHermandad NULL:" + e.getMessage());

        }
    }

    //CRUD inventarioObjeto

    /**
     * @brief Inserta un objeto en un inventario
     * @param objeto Objeto a insertar
     * @param inventario Inventario en el que se inserta el objeto
     */
    public void insertarObjetoEnInventario(Objeto objeto, Inventario inventario){
        String sql = "INSERT INTO InventarioObjeto (idInventario, idObjeto) VALUES (?, ?)";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, inventario.getIdInventario());
            consulta.setString(2, objeto.getIdObjeto());
            inventario.getObjetosInventario().add(objeto);
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al insertar ObjetoInventario:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("ObjetoInventario NULL:" + e.getMessage());

        }
    }

    /**
     * @brief Borra un objeto de un inventario
     * @param objeto Objeto a borrar
     * @param inventario Inventario del que se borra el objeto
     */
    public void borrarObjetoEnInventario(Objeto objeto, Inventario inventario){
        String sql = "DELETE FROM InventarioObjeto WHERE idInventario = ? AND idObjeto = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setString(1, inventario.getIdInventario());
            consulta.setString(2, objeto.getIdObjeto());
            consulta.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al borrar ObjetoInventario:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("ObjetoInventario NULL:" + e.getMessage());

        }
    }

    /**
     * @brief Modifica un inventario en la base de datos
     * @param inventario Inventario a modificar
     */
    public void modificarInventario(Inventario inventario){
        String sql = "UPDATE inventario SET espaciosOcupados = ? WHERE idInventario = ?";
        Connection conexion = instancia.getConexion();

        try{
            PreparedStatement consulta = conexion.prepareStatement(sql);
            consulta.setInt(1, inventario.getEspaciosOcupados());
            consulta.setString(2, inventario.getIdInventario());
            consulta.executeUpdate();
         }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al modificar inventario:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Inventario NULL:" + e.getMessage());

        }
    }

    /**
     * @brief Lee un inventario de la base de datos y lo añade al array de inventarios
     * @param inventario ArrayList de inventarios de donde se lee
     * @param objetos ArrayList de objetos donde se buscan los objetos que se van a añadir al inventario
     */
    public void leerInventarioObjeto(ArrayList<Inventario> inventario, ArrayList<Objeto> objetos){
        String sql = "SELECT * FROM InventarioObjeto";
        Connection conexion = instancia.getConexion();

        try{
            Statement statementInventarioObjeto = conexion.createStatement();
            ResultSet resulsetInventarioObjeto = statementInventarioObjeto.executeQuery(sql);
            while(resulsetInventarioObjeto.next()){
                String idInventario = resulsetInventarioObjeto.getString("idInventario");
                String idObjeto = resulsetInventarioObjeto.getString("idObjeto");
                //Recorremos el array de inventarios añadiendo los objetos que este tenga a cada uno
                for(int i = 0; i < inventario.size(); i++){
                    if(inventario.get(i).getIdInventario().equals(idInventario)){
                        for(int j = 0; j < objetos.size(); j++){
                            if(objetos.get(j).getIdObjeto().equals(idObjeto)){
                                inventario.get(i).getObjetosInventario().add(objetos.get(j));
                            }
                        }
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al leer InventarioObjeto:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("InventarioObjeto NULL:" + e.getMessage());

        }
    }

    /**
     * @brief Cierra la conexion a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


