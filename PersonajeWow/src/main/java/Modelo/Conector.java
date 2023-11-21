/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author AdrianNS
 */
public class Conector {
    
    public static Connection conectar() {
        Connection conn = null;
        try {
            // Ruta a la base de datos SQLite
            String url = "jdbc:sqlite:db.sqlite";
            
            // Conectar a la base de datos
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión establecida a SQLite.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }
    
public void crearBaseDatos() {
        // Conectar a la base de datos
        try (Connection conn = Conector.conectar();
             Statement stmt = conn.createStatement()) {
            
            // Crear tabla si no existe
            String sqlObjeto = "CREATE TABLE IF NOT EXISTS objeto ("
                    + "idObjeto TEXT PRIMARY KEY,"
                    + "rareza TEXT NOT NULL,"
                    + "descripcion TEXT NOT NULL,"
                    + "precio DOUBLE NOT NULL,"
                    + " nombreObjeto TEXT NOT NULL )";
           
            String sqlHermandad = "CREATE TABLE IF NOT EXISTS hermandad ("
                    +"IdHermandad TEXT PRIMARY KEY NOT NULL,"
                    + "nombreHermandad TEXT NOT NULL,"
                    + "servidorHermandad TEXT NOT NULL,"
                    + "numeroMiembros INTEGER NOT NULL)";
       
                     String sqlInventario = "CREATE TABLE IF NOT EXISTS inventario ("
                    + "idInventario TEXT PRIMARY KEY, "
                    + "idPersonaje INTEGER , "
                    + "capacidadMaxima INTEGER NOT NULL, "
                    + "espaciosOcupados INTEGER NOT NULL "
                     +")";
                     
                String sqlPersonaje = "CREATE TABLE IF NOT EXISTS personaje ("
                        + "idPersonaje TEXT PRIMARY KEY, "
                        + "nombre TEXT NOT NULL, "
                        + "servidor TEXT NOT NULL, "
                        + "faccion TEXT NOT NULL, "
                        + "raza TEXT NOT NULL, "
                        + "idInventario TEXT,"
                        + "nivel INTEGER NOT NULL,"
                        + "FOREIGN KEY (idInventario) REFERENCES inventario(idInventario)" 
                        +")";
                     
                   String relacionInventarioObjeto = "CREATE TABLE IF NOT EXISTS InventarioObjeto ("
                    + "idInventario TEXT NOT NULL,"
                    + "idObjeto TEXT NOT NULL,"
                    + "PRIMARY KEY (idInventario, idObjeto),"
                    + "FOREIGN KEY (idInventario) REFERENCES inventario(idInventario),"
                    + "FOREIGN KEY (idObjeto) REFERENCES objeto(idObjeto)"
                    + ")";
                   
                   String relacionHermandadPersonaje = "CREATE TABLE IF NOT EXISTS hermandadPersonaje("
                           + "idHermandad TEXT NOT NULL,"
                           + "idPersonaje INTEGER NOT NULL, "
                           + "PRIMARY KEY (idHermandad, idPersonaje),"
                           + "FOREIGN KEY (idHermandad) REFERENCES hermandad(idHermandad),"
                           + "FOREIGN KEY (idPersonaje) REFERENCES personaje(idPersonaje)"
                           + ") ";
           
                   
           /**
           stmt.execute("DROP TABLE IF EXISTS relacionHermandadPersonaje");
           stmt.execute("DROP TABLE IF EXISTS InventarioObjeto");
           stmt.execute("DROP TABLE IF EXISTS personaje");
           stmt.execute("DROP TABLE IF EXISTS inventario");
           stmt.execute("DROP TABLE IF EXISTS hermandad");
           stmt.execute("DROP TABLE IF EXISTS objeto");
           * */
           
                              
            // Ejecutar la consulta SQL
            stmt.execute(sqlObjeto);
            stmt.execute(sqlHermandad);
            stmt.execute(sqlPersonaje);
            stmt.execute(sqlInventario);
            stmt.execute(relacionInventarioObjeto);
            stmt.execute(relacionHermandadPersonaje);
            System.out.println("Base de datos y tabla creadas correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    //CRUD de OBJETO
    public void insertarObjetoEnBd(Objeto objeto) {
         String sql = "INSERT INTO objeto (idObjeto, rareza, descripcion, precio, nombreObjeto) VALUES (?, ?, ?, ?, ?)";
        // Conectar a la base de datos
        try (Connection conn = Conector.conectar()) {
                PreparedStatement  consulta = conn.prepareStatement(sql);
                consulta.setString(1, objeto.getIdObjeto());
                consulta.setString(2, objeto.getRareza());
                consulta.setString(3, objeto.getDescripcion());
                consulta.setDouble(4, objeto.getPrecio());
                consulta.setString(5, objeto.getNombreObjeto()); 
                consulta.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al insertar Objeto:" + e.getMessage());
        }
    }

    public void modificarObjetoEnBd(Objeto objeto){
        String sql = "UPDATE objeto SET rareza = ?, descripcion = ?, precio = ?, nombreObjeto = ? WHERE idObjeto = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, objeto.getRareza());
            consulta.setString(2, objeto.getDescripcion());
            consulta.setDouble(3, objeto.getPrecio());
            consulta.setString(4, objeto.getNombreObjeto());
            consulta.setString(5, objeto.getIdObjeto());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al modificar Objeto:" + e.getMessage());
        }
    }
    
    public void insertarPersonajeYInventarioEnBD(Personaje personaje, Inventario inventario){
        String sqlInventario = "INSERT INTO inventario (idInventario, idPersonaje, capacidadMaxima, espaciosOcupados) VALUES (?, ?, ?, ?)";
         String sqlPersonaje = "INSERT INTO personaje (IdPersonaje, nombre, servidor, faccion, raza, idInventario, nivel) VALUES (?, ?, ?, ?, ?, ?, ?)";
         
         try(Connection conn = Conector.conectar()){
             PreparedStatement consultaInventario = conn.prepareStatement(sqlInventario);
             PreparedStatement consultaPersonaje = conn.prepareStatement(sqlPersonaje);
             consultaInventario.setString(1, inventario.getIdInventario());
             consultaInventario.setString(2, personaje.getIdPersonaje());
             consultaInventario.setInt(3, inventario.getCapacidadMaxima());
             consultaInventario.setInt(4, inventario.getEspaciosOcupados());
             consultaInventario.executeUpdate();
             
             consultaPersonaje.setString(1, personaje.getIdPersonaje());
             consultaPersonaje.setString(2, personaje.getNombre());
             consultaPersonaje.setString(3, personaje.getServidor());
             consultaPersonaje.setString(4, personaje.getFaccion());
             consultaPersonaje.setString(5, personaje.getRaza());
             consultaPersonaje.setString(6, inventario.getIdInventario());
             consultaPersonaje.setInt(7, personaje.getNivel());
             consultaPersonaje.executeUpdate();
             
             
         }catch(Exception e){
             e.printStackTrace();
             System.err.println("Error al insertar Personaje:" + e.getMessage());
         }     
    }

    public void insertarHermandadEnBD(Hermandad hermandad) {
        String sql = "INSERT INTO hermandad (idHermandad, nombreHermandad, servidorHermandad, numeroMiembros) VALUES (?, ?, ?, ?)";
        // Conectar a la base de datos
        try (Connection conn = Conector.conectar()) {
                PreparedStatement  consulta = conn.prepareStatement(sql);
                consulta.setString(1, hermandad.getIdHermandad());
                consulta.setString(2, hermandad.getNombreHermandad());
                consulta.setString(3, hermandad.getServidorHermandad());
                consulta.setInt(4, hermandad.getNumeroMiembros());
                consulta.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al insertar Hermandad:" + e.getMessage());
        }
    }
}


