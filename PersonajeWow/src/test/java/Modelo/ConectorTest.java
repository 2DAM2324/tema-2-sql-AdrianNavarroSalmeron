/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author AdrianNs
 */
public class ConectorTest {
    private static Conector conector;
    public ConectorTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        conector = Conector.getInstancia("dbTest.sqlite");

    }
    
    @AfterAll
    public static void tearDownClass() {
        conector.cerrarConexion("dbTest.sqlite");
    }
    
    @BeforeEach
    public void setUp() {
       
        conector.borrarDb();
        conector.crearBaseDatos();
        crearInstanciasBd();


    }

    public void crearInstanciasBd(){

         //Creamos una instancia de Objeto
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto("Espada");
        objeto.setDescripcion("Arma");
        objeto.setPrecio(12);
        objeto.setRareza("Comun");
        objeto.setIdObjeto("OB50");
        try{
            conector.insertarObjetoEnBd(objeto);
        }
        catch(Exception e){
            fail("No se ha podido insertar el objeto");
        }

        //Creamos una instancia de Personaje
        Personaje personaje = new Personaje();
        personaje.setNombre("Victarion");
        personaje.setServidor("Los Errantes");
        personaje.setRaza("Humano");
        personaje.setNivel(14);
        personaje.setFaccion("Alianza");
        try{
            conector.insertarPersonajeYInventarioEnBD(personaje, personaje.getInventario());
        }
        catch(Exception e){
            fail("No se ha podido insertar el personaje");
        }
        personaje.setIdPersonaje(1);

        //Creamos una instancia de Hermandad
        Hermandad hermandad = new Hermandad();
        hermandad.setNombreHermandad("Los Errantes");
        hermandad.setServidorHermandad("Los Errantes");
        hermandad.setNumeroMiembros(0);
        try{
            conector.insertarHermandadEnBD(hermandad);
        }
        catch(Exception e){
            fail("No se ha podido insertar la hermandad");
        }
        
        //Insertamos el objeto en el inventario del personaje
        try{
            conector.insertarObjetoEnInventario(objeto, personaje.getInventario());
        }
        catch(Exception e){
            fail("No se ha podido insertar el objeto en el inventario");
        }

        //Insertamos el personaje en la hermandad
        try{
            conector.insertarPersonajeHermandad(personaje, hermandad);
        }
        catch(Exception e){
            fail("No se ha podido insertar el personaje en la hermandad");
        }
    }
    

    /**
     * Test of insertarObjetoEnBd method, of class Conector.
     */
    @Test
    public void testInsertarObjetoEnBd() throws SQLException {
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto("Espada");
        objeto.setDescripcion("Arma");
        objeto.setPrecio(12);
        objeto.setRareza("Comun");
        objeto.setIdObjeto("OB1");
        try{
            conector.insertarObjetoEnBd(objeto);
        }
        catch(Exception e){
            fail("No se ha podido insertar el objeto");
        }
        //Traemos el objeto de la bd
        Objeto objetoBd = null;
        try{
            objetoBd = conector.getObjeto("OB1");
        }
        catch(SQLException e){
            fail("No se ha podido traer el objeto de la bd");
        }
       
        assertEquals(objeto.getNombreObjeto(), objetoBd.getNombreObjeto());

    }

    /**
     * Test insertar objeto cuando se intenta insertar dos veces el mismo objeto
     */
    @Test
    public void testInsertarObjetoEnBdFail() throws SQLException {
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto("Espada");
        objeto.setDescripcion("Arma");
        objeto.setPrecio(12);
        objeto.setRareza("Comun");
        try{
            conector.insertarObjetoEnBd(objeto);
            conector.insertarObjetoEnBd(objeto);
        }
        catch(SQLException e){
            assertEquals("UNIQUE constraint failed: Objeto.nombreObjeto", e.getMessage());
        }             
    }

    /**
     * @brief Test insertar objeto cuando se intenta insertar con campos nulos
     */
    @Test
    public void testInsertarObjetoEnBdFail2() throws SQLException {
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto(null);
        objeto.setDescripcion("Arma");
        objeto.setPrecio(12);
        objeto.setRareza("Comun");
        try{
            conector.insertarObjetoEnBd(objeto);
            fail("No se ha lanzado la excepcion");
        }
        catch(IllegalArgumentException e){
            assertEquals("Uno o mas campos estan vacios", e.getMessage());
        }             
    }
    
/**
 * Test of modificarObjetoEnBd method, of class Conector.
 */
@Test
public void testModificarObjetoEnBd() throws Exception {
    Objeto objeto = null;
    try{
        objeto = conector.getObjeto("OB50");
    }
    catch(SQLException e){
        fail("No se ha podido traer el objeto de la bd");
    }
    objeto.setNombreObjeto("Guantazo");
    try{
        conector.modificarObjetoEnBd(objeto);
    }
    catch(SQLException e){
        fail("No se ha podido modificar el objeto");
    }
    Objeto objetoBd = null;
    try{
        objetoBd = conector.getObjeto("OB50");
    }
    catch(SQLException e){
        fail("No se ha podido traer el objeto de la bd");
    }
    assertEquals(objeto.getNombreObjeto(), objetoBd.getNombreObjeto());
}

/**
 * Test modificarObjetoEnBd cuando se intenta modificar con campos nulos
 */
@Test
public void testModificarObjetoEnBdFail() throws SQLException {
    Objeto objeto = null;
    try{
        objeto = conector.getObjeto("OB50");
    }
    catch(SQLException e){
        fail("No se ha podido traer el objeto de la bd");
    }
    objeto.setNombreObjeto(null);
    try{
        conector.modificarObjetoEnBd(objeto);
        fail("No se ha lanzado la excepcion");
    }
    catch(IllegalArgumentException e){
        assertEquals("Uno o mas campos estan vacios", e.getMessage());
    }             
}

/**
 * Test of borrarObjetoDeBd method, of class Conector.
 */
@Test
public void testBorrarObjetoDeBd() throws Exception {
    Objeto objeto = null;
    try{
        objeto = conector.getObjeto("OB50");
    }
    catch(SQLException e){
        fail("No se ha podido traer el objeto de la bd");
    }
    try{
        conector.borrarObjetoDeBd(objeto);
    }
    catch(SQLException e){
        fail("No se ha podido borrar el objeto");
    }
    Objeto objetoBd = null;
    try{
        objetoBd = conector.getObjeto("OB50");
        
    }
    catch(SQLException e){
        assertEquals("No existe el objeto", e.getMessage());
        fail("Se ha lanzado la excepcion");
    }
    assertNull(objetoBd);
}

/**
 * Test borrar objeto cuando se intenta borrar un objeto que no existe
 */
@Test
public void testBorrarObjetoDeBdFail() throws SQLException {
    Objeto objeto = null;
    try{
        objeto = conector.getObjeto("OB50");
    }
    catch(SQLException e){
        fail("No se ha podido traer el objeto de la bd");
    }
    try{
        conector.borrarObjetoDeBd(objeto);
        conector.borrarObjetoDeBd(objeto);
    }
    catch(SQLException e){
        fail("Se ha lanzado la excepcion");
    }

}

/**
 * Test of leerObjetosDeBd method, of class Conector.
 */
@Test
public void testLeerObjetosDeBd() throws Exception {
    ArrayList<Objeto> ObjetosSistema = new ArrayList<>();
    try{
        conector.leerObjetosDeBd(ObjetosSistema);
    }
    catch(SQLException e){
        fail("No se ha podido leer los objetos");
    }
    assertEquals(1, ObjetosSistema.size());
}

/**
 * Test of leerObjetosDeBd cuando no hay objetos en la bd
 */
@Test
public void testLeerObjetosDeBdFail() throws SQLException {
    conector.borrarDb();
    ArrayList<Objeto> ObjetosSistema = new ArrayList<>();
    try{
        conector.leerObjetosDeBd(ObjetosSistema);
    }
    catch(SQLException e){
        assertEquals("No hay objetos en la base de datos", e.getMessage());
    }
}

// /**
//  * Test of insertarPersonajeYInventarioEnBD method, of class Conector.
//  */
// @Test
// public void testInsertarPersonajeYInventarioEnBD() throws Exception {
//     System.out.println("insertarPersonajeYInventarioEnBD");
//     Personaje personaje = null;
//     Inventario inventario = null;
//     Conector instance = null;
//     instance.insertarPersonajeYInventarioEnBD(personaje, inventario);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerPersonaje method, of class Conector.
//  */
// @Test
// public void testLeerPersonaje() throws Exception {
//     System.out.println("leerPersonaje");
//     String nombre = "";
//     String servidor = "";
//     ArrayList<Personaje> ArrayListDePersonajesSistema = null;
//     Conector instance = null;
//     instance.leerPersonaje(nombre, servidor, ArrayListDePersonajesSistema);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerInventario method, of class Conector.
//  */
// @Test
// public void testLeerInventario() throws Exception {
//     System.out.println("leerInventario");
//     ArrayList<Inventario> inventariosSistema = null;
//     Conector instance = null;
//     instance.leerInventario(inventariosSistema);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of modificarPersonajeBd method, of class Conector.
//  */
// @Test
// public void testModificarPersonajeBd() throws Exception {
//     System.out.println("modificarPersonajeBd");
//     Personaje personaje = null;
//     Conector instance = null;
//     instance.modificarPersonajeBd(personaje);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of borrarPersonajeIventarioBd method, of class Conector.
//  */
// @Test
// public void testBorrarPersonajeIventarioBd() throws Exception {
//     System.out.println("borrarPersonajeIventarioBd");
//     Personaje personaje = null;
//     Conector instance = null;
//     instance.borrarPersonajeIventarioBd(personaje);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerPersonajeDeBd method, of class Conector.
//  */
// @Test
// public void testLeerPersonajeDeBd() throws Exception {
//     System.out.println("leerPersonajeDeBd");
//     ArrayList<Personaje> personajesSitema = null;
//     ArrayList<Inventario> inventariosSistema = null;
//     ArrayList<Hermandad> hermandadesSistema = null;
//     Conector instance = null;
//     instance.leerPersonajeDeBd(personajesSitema, inventariosSistema, hermandadesSistema);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of insertarHermandadEnBD method, of class Conector.
//  */
// @Test
// public void testInsertarHermandadEnBD() throws Exception {
//     System.out.println("insertarHermandadEnBD");
//     Hermandad hermandad = null;
//     Conector instance = null;
//     instance.insertarHermandadEnBD(hermandad);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of modificarHermandadEnBd method, of class Conector.
//  */
// @Test
// public void testModificarHermandadEnBd() throws Exception {
//     System.out.println("modificarHermandadEnBd");
//     Hermandad hermandad = null;
//     Conector instance = null;
//     instance.modificarHermandadEnBd(hermandad);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of borrarHermandadBd method, of class Conector.
//  */
// @Test
// public void testBorrarHermandadBd() throws Exception {
//     System.out.println("borrarHermandadBd");
//     Hermandad hermandad = null;
//     Conector instance = null;
//     instance.borrarHermandadBd(hermandad);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerHermandad method, of class Conector.
//  */
// @Test
// public void testLeerHermandad() throws Exception {
//     System.out.println("leerHermandad");
//     ArrayList<Hermandad> ArrayDeHermandadesSistema = null;
//     Conector instance = null;
//     instance.leerHermandad(ArrayDeHermandadesSistema);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerHermandadPersonaje method, of class Conector.
//  */
// @Test
// public void testLeerHermandadPersonaje() throws Exception {
//     System.out.println("leerHermandadPersonaje");
//     ArrayList<Hermandad> arrayDeHermandadesSistema = null;
//     ArrayList<Personaje> personajesSistema = null;
//     Conector instance = null;
//     instance.leerHermandadPersonaje(arrayDeHermandadesSistema, personajesSistema);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of insertarPersonajeHermandad method, of class Conector.
//  */
// @Test
// public void testInsertarPersonajeHermandad() throws Exception {
//     System.out.println("insertarPersonajeHermandad");
//     Personaje personaje = null;
//     Hermandad hermandad = null;
//     Conector instance = null;
//     instance.insertarPersonajeHermandad(personaje, hermandad);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of borrarPersonajeHermandad method, of class Conector.
//  */
// @Test
// public void testBorrarPersonajeHermandad() throws Exception {
//     System.out.println("borrarPersonajeHermandad");
//     Personaje personaje = null;
//     Hermandad hermandad = null;
//     Conector instance = null;
//     instance.borrarPersonajeHermandad(personaje, hermandad);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of insertarObjetoEnInventario method, of class Conector.
//  */
// @Test
// public void testInsertarObjetoEnInventario() throws Exception {
//     System.out.println("insertarObjetoEnInventario");
//     Objeto objeto = null;
//     Inventario inventario = null;
//     Conector instance = null;
//     instance.insertarObjetoEnInventario(objeto, inventario);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of borrarObjetoEnInventario method, of class Conector.
//  */
// @Test
// public void testBorrarObjetoEnInventario() throws Exception {
//     System.out.println("borrarObjetoEnInventario");
//     Objeto objeto = null;
//     Inventario inventario = null;
//     Conector instance = null;
//     instance.borrarObjetoEnInventario(objeto, inventario);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of modificarInventario method, of class Conector.
//  */
// @Test
// public void testModificarInventario() throws Exception {
//     System.out.println("modificarInventario");
//     Inventario inventario = null;
//     Conector instance = null;
//     instance.modificarInventario(inventario);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of leerInventarioObjeto method, of class Conector.
//  */
// @Test
// public void testLeerInventarioObjeto() throws Exception {
//     System.out.println("leerInventarioObjeto");
//     ArrayList<Inventario> inventario = null;
//     ArrayList<Objeto> objetos = null;
//     Conector instance = null;
//     instance.leerInventarioObjeto(inventario, objetos);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of cerrarConexion method, of class Conector.
//  */
// @Test
// public void testCerrarConexion() {
//     System.out.println("cerrarConexion");
//     String dbName = "";
//     Conector instance = null;
//     instance.cerrarConexion(dbName);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of getObjeto method, of class Conector.
//  */
// @Test
// public void testGetObjeto() throws Exception {
//     System.out.println("getObjeto");
//     String idObjeto = "";
//     Conector instance = null;
//     Objeto expResult = null;
//     Objeto result = instance.getObjeto(idObjeto);
//     assertEquals(expResult, result);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of getIdObjetoEnTablaInventarioObjeto method, of class Conector.
//  */
// @Test
// public void testGetIdObjetoEnTablaInventarioObjeto() throws Exception {
//     System.out.println("getIdObjetoEnTablaInventarioObjeto");
//     String idObjeto = "";
//     String idInventario = "";
//     Conector instance = null;
//     String expResult = "";
//     String result = instance.getIdObjetoEnTablaInventarioObjeto(idObjeto, idInventario);
//     assertEquals(expResult, result);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of getPersonaje method, of class Conector.
//  */
// @Test
// public void testGetPersonaje() throws Exception {
//     System.out.println("getPersonaje");
//     int idPersonaje = 0;
//     Conector instance = null;
//     Personaje expResult = null;
//     Personaje result = instance.getPersonaje(idPersonaje);
//     assertEquals(expResult, result);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of getHermandad method, of class Conector.
//  */
// @Test
// public void testGetHermandad() throws Exception {
//     System.out.println("getHermandad");
//     String nombreHermandad = "";
//     String servidorHermandad = "";
//     Conector instance = null;
//     Hermandad expResult = null;
//     Hermandad result = instance.getHermandad(nombreHermandad, servidorHermandad);
//     assertEquals(expResult, result);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }

// /**
//  * Test of getPersonajesEnHermandad method, of class Conector.
//  */
// @Test
// public void testGetPersonajesEnHermandad() throws Exception {
//     System.out.println("getPersonajesEnHermandad");
//     String idHermandad = "";
//     Conector instance = null;
//     ArrayList<Integer> expResult = null;
//     ArrayList<Integer> result = instance.getPersonajesEnHermandad(idHermandad);
//     assertEquals(expResult, result);
//     // TODO review the generated test code and remove the default call to fail.
//     fail("The test case is a prototype.");
// }
    
}
