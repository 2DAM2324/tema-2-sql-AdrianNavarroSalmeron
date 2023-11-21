/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
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
                        + "idPersonaje INTEGER PRIMARY KEY AUTOINCREMENT, "
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
}


