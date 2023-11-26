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
        
        }
        catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            System.err.println("SE HA INTENTADO INSERTAR UNA PK REPETIDA EN OBJETO:" + e.getMessage());
        }
        catch(SQLException e){
            e.printStackTrace();
            System.err.println("Error al insertar Objeto:" + e.getMessage());
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.err.println("Objeto NULL:" + e.getMessage());

        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERROR generico en OBJETO" + e.getMessage());
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
    
    public void borrarObjetoDeBd(Objeto objeto) {
        String sqlTablaObjeto = "DELETE FROM objeto WHERE idObjeto = ?";
        String sqlTablaInventarioObjeto = "DELETE FROM InventarioObjeto WHERE idObjeto = ?";
        try (Connection conn = Conector.conectar()) {
            PreparedStatement consultaInventarioObjeto = conn.prepareStatement(sqlTablaInventarioObjeto);
            PreparedStatement consultaObjeto = conn.prepareStatement(sqlTablaObjeto);
            consultaInventarioObjeto.setString(1, objeto.getIdObjeto());
            consultaInventarioObjeto.executeUpdate();
            consultaObjeto.setString(1, objeto.getIdObjeto());
            consultaObjeto.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al borrar Objeto:" + e.getMessage());
        }
    }
    
    public void leerObjetosDeBd(ArrayList ObjetosSistema){
        String sql = "SELECT * FROM objeto";
        try(Connection conn = Conector.conectar()){
            Statement statementObjeto = conn.createStatement();
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
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al leer Objetos:" + e.getMessage());
        }
    }
    
    //CRUD PERSONAJE
    
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

    public void leerInventario(ArrayList inventariosSistema){
        String sql = "SELECT * FROM inventario";
        try(Connection conn = Conector.conectar()){
            Statement statementInventario = conn.createStatement();
            ResultSet resulsetInventario = statementInventario.executeQuery(sql);
            while(resulsetInventario.next()){
                String idInventario = resulsetInventario.getString("idInventario");
                int capacidadMaxima = resulsetInventario.getInt("capacidadMaxima");
                int espaciosOcupados = resulsetInventario.getInt("espaciosOcupados");
                Inventario inventario = new Inventario(idInventario, capacidadMaxima, espaciosOcupados);
                inventariosSistema.add(inventario);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al leer Inventario:" + e.getMessage());
        }
    }

    public void modificarPersonajeBd(Personaje personaje){
        String sql = "UPDATE personaje SET nombre = ?, servidor = ?, faccion = ?, raza = ?, nivel = ? WHERE idPersonaje = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, personaje.getNombre());
            consulta.setString(2, personaje.getServidor());
            consulta.setString(3, personaje.getFaccion());
            consulta.setString(4, personaje.getRaza());
            consulta.setInt(5, personaje.getNivel());
            consulta.setString(6, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al modificar Personaje:" + e.getMessage());
        }
    }

    //Borra el personaje de la hermandad y su inventario
    public void borrarPersonajeIventarioBd(Personaje personaje){
        String sqlTablaPersonaje = "DELETE FROM personaje WHERE idPersonaje = ?";
        String sqlTablaInventario = "DELETE FROM inventario WHERE idInventario = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consultaPersonaje = conn.prepareStatement(sqlTablaPersonaje);
            PreparedStatement consultaInventario = conn.prepareStatement(sqlTablaInventario);
            PreparedStatement consultaHermandadPersonaje = conn.prepareStatement("DELETE FROM hermandadPersonaje WHERE idPersonaje = ?");
            
            //Borramos la relacion con la hermandad
            consultaHermandadPersonaje.setString(1, personaje.getIdPersonaje());
            consultaHermandadPersonaje.executeUpdate();
            //Borramos el inventario del personaje
            consultaInventario.setString(1, personaje.getInventario().getIdInventario());
            consultaInventario.executeUpdate();
            //Borramos el personaje
            consultaPersonaje.setString(1, personaje.getIdPersonaje());
            consultaPersonaje.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al borrar Personaje:" + e.getMessage());
        }
    }

    //Lee el personaje de la base de datos
    public void leerPersonajeDeBd(ArrayList personajesSitema, ArrayList inventariosSistema, ArrayList<Hermandad> hermandadesSistema){
        String sql = "SELECT * FROM personaje";
        try(Connection conn = Conector.conectar()){
            Statement statementPersonaje = conn.createStatement();
            ResultSet resulsetPersonaje = statementPersonaje.executeQuery(sql);
            while(resulsetPersonaje.next()){
                String idPersonaje = resulsetPersonaje.getString("idPersonaje");
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
                Personaje personaje = new Personaje(idPersonaje, nombre, servidor, faccion, raza, nivel, inventario);
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
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al leer Personaje:" + e.getMessage());
        }

    }

    //Crud de hermandad

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

    public void modificarHermandadEnBd(Hermandad hermandad){
        String sql = "UPDATE hermandad SET nombreHermandad = ?, servidorHermandad = ?, numeroMiembros = ? WHERE idHermandad = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, hermandad.getNombreHermandad());
            consulta.setString(2, hermandad.getServidorHermandad());
            consulta.setInt(3, hermandad.getNumeroMiembros());
            consulta.setString(4, hermandad.getIdHermandad());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al modificar Hermandad:" + e.getMessage());
        }
    }
    
    public void borrarHermandadBd(Hermandad hermandad) {
        String sqlTablaHermandad = "DELETE FROM hermandad WHERE idHermandad = ?";
        String sqlTablaHermandadPersonaje = "DELETE FROM hermandadPersonaje WHERE idHermandad = ?";
        try (Connection conn = Conector.conectar()) {
            PreparedStatement consultaHermandadPersonaje = conn.prepareStatement(sqlTablaHermandadPersonaje);
            PreparedStatement consultaHermandad = conn.prepareStatement(sqlTablaHermandad);
            consultaHermandadPersonaje.setString(1, hermandad.getIdHermandad());
            consultaHermandadPersonaje.executeUpdate();
            consultaHermandad.setString(1, hermandad.getIdHermandad());
            consultaHermandad.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al borrar Hermandad:" + e.getMessage());
        }
    }

    public void leerHermandad(ArrayList ArrayDeHermandadesSistema){
        String sql = "SELECT * FROM hermandad";
        try(Connection conn = Conector.conectar()){
            Statement statementHermandad = conn.createStatement();
            ResultSet resulsetHermandad = statementHermandad.executeQuery(sql);
            while(resulsetHermandad.next()){
                String idHermandad = resulsetHermandad.getString("idHermandad");
                String nombreHermandad = resulsetHermandad.getString("nombreHermandad");
                String servidorHermandad = resulsetHermandad.getString("servidorHermandad");
                int numeroMiembros = resulsetHermandad.getInt("numeroMiembros");
                Hermandad hermandad = new Hermandad(idHermandad, nombreHermandad, servidorHermandad, numeroMiembros);
                ArrayDeHermandadesSistema.add(hermandad);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al leer Hermandad:" + e.getMessage());
        }
    }

    public void leerHermandadPersonaje(ArrayList<Hermandad> arrayDeHermandadesSistema, ArrayList<Personaje> personajesSistema){
        String sql = "SELECT * FROM hermandadPersonaje";
        try(Connection conn = Conector.conectar()){
            Statement statementHermandadPersonaje = conn.createStatement();
            ResultSet resulsetHermandadPersonaje = statementHermandadPersonaje.executeQuery(sql);
            while(resulsetHermandadPersonaje.next()){
                String idHermandad = resulsetHermandadPersonaje.getString("idHermandad");
                String idPersonaje = resulsetHermandadPersonaje.getString("idPersonaje");
                //Recorremos el vector de hermandades de sistema, para cada hermandad buscamos sus miembros y los añadimos a la lis de miembros de esa hermandad
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
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al leer HermandadPersonaje:" + e.getMessage());
        }
    }

    public void insertarPersonajeHermandad(Personaje personaje, Hermandad hermandad){
        String sql = "INSERT INTO hermandadPersonaje (idHermandad, idPersonaje) VALUES (?, ?)";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, hermandad.getIdHermandad());
            consulta.setString(2, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al insertar PersonajeHermandad:" + e.getMessage());
        }
    }

    public void borrarPersonajeHermandad(Personaje personaje, Hermandad hermandad){
        String sql = "DELETE FROM hermandadPersonaje WHERE idHermandad = ? AND idPersonaje = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, hermandad.getIdHermandad());
            consulta.setString(2, personaje.getIdPersonaje());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al borrar PersonajeHermandad:" + e.getMessage());
        }
    }

    public void insertarObjetoEnInventario(Objeto objeto, Inventario inventario){
        String sql = "INSERT INTO InventarioObjeto (idInventario, idObjeto) VALUES (?, ?)";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, inventario.getIdInventario());
            consulta.setString(2, objeto.getIdObjeto());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al insertar ObjetoInventario:" + e.getMessage());
        }
    }

    public void borrarObjetoEnInventario(Objeto objeto, Inventario inventario){
        String sql = "DELETE FROM InventarioObjeto WHERE idInventario = ? AND idObjeto = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
            consulta.setString(1, inventario.getIdInventario());
            consulta.setString(2, objeto.getIdObjeto());
            consulta.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Error al borrar ObjetoInventario:" + e.getMessage());
        }
    }

    public void modificarInventario(Inventario inventario){
        String sql = "UPDATE inventario SET espaciosOcupados = ? WHERE idInventario = ?";
        try(Connection conn = Conector.conectar()){
            PreparedStatement consulta = conn.prepareStatement(sql);
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
        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERROR en inventario:" + e.getMessage());
        }
    }

    public void leerInventarioObjeto(ArrayList<Inventario> inventario, ArrayList<Objeto> objetos){
        String sql = "SELECT * FROM InventarioObjeto";
        try(Connection conn = Conector.conectar()){
            Statement statementInventarioObjeto = conn.createStatement();
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
        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERROR en inventarioObjeto:" + e.getMessage());
        }
    }
}


