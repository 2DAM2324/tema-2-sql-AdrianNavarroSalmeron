/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
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
        conector.borrarObjetoDeBd(conector.getObjeto("OB50"));
        ArrayList<Objeto> ObjetosSistema = new ArrayList<>();
        try{
            conector.leerObjetosDeBd(ObjetosSistema);
        }
        catch(SQLException e){
            assertEquals("No hay objetos en la base de datos", e.getMessage());
        }
    }

    /**
     * Test of insertarPersonajeYInventarioEnBD method, of class Conector.
     */
    @Test
    public void testInsertarPersonajeYInventarioEnBD() throws Exception {
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
        //Traemos el personaje de la bd
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        }
        assertEquals(personaje.getNombre(), personajeBd.getNombre());
    }

    /**
     * Test insertar personaje cuando se intenta insertar dos veces el mismo personaje
     */
    @Test
    public void testInsertarPersonajeYInventarioEnBDFail() throws SQLException {
        Personaje personaje = new Personaje();
        personaje.setNombre("Victarion");
        personaje.setServidor("Los Errantes");
        personaje.setRaza("Humano");
        personaje.setNivel(14);
        personaje.setFaccion("Alianza");
        try{
            conector.insertarPersonajeYInventarioEnBD(personaje, personaje.getInventario());
        }
        catch(SQLIntegrityConstraintViolationException e){
            assertEquals("UNIQUE constraint failed: Personaje.nombre, Personaje.servidor", e.getMessage());
        }             
    }

    /**
     * Test insertar personaje cuando se intenta insertar con campos nulos
     */
    @Test
    public void testInsertarPersonajeYInventarioEnBDFail2() throws SQLException {
        Personaje personaje = new Personaje();
        personaje.setNombre(null);
        personaje.setServidor("Los Errantes");
        personaje.setRaza("Humano");
        personaje.setNivel(14);
        personaje.setFaccion("Alianza");
        try{
            conector.insertarPersonajeYInventarioEnBD(personaje, personaje.getInventario());
            fail("No se ha lanzado la excepcion");
        }
        catch(IllegalArgumentException e){
            assertEquals("Uno o mas campos estan vacios", e.getMessage());
        }             
    }

    /**
     * Test leer personaje de la bd
     */
    @Test
    public void testLeerPersonajeDeBd() throws SQLException {
        ArrayList<Personaje> personajesSistema = new ArrayList<>();
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        ArrayList<Hermandad> hermandadesSistema = new ArrayList<>();
        try{
            conector.leerPersonajeDeBd(personajesSistema, inventariosSistema, hermandadesSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer los personajes");
        }
        assertEquals(1, personajesSistema.size());
    }

    /**
     * Test leer personaje de la bd cuando no hay personajes
     */
    @Test
    public void testLeerPersonajeDeBdFail() throws SQLException {
       conector.borrarDb();
        conector.crearBaseDatos();
        ArrayList<Personaje> personajesSistema = new ArrayList<>();
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        ArrayList<Hermandad> hermandadesSistema = new ArrayList<>();
        try{
            conector.leerPersonajeDeBd(personajesSistema, inventariosSistema, hermandadesSistema);
        }
        catch(SQLException e){
            assertEquals("No hay personajes en la base de datos", e.getMessage());
        }
    }


    /**
     * Test de leerInventario de la bd
     */
    @Test
    public void testLeerInventario() throws SQLException {
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        try{
            conector.leerInventario(inventariosSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer los inventarios");
        }
        assertEquals(1, inventariosSistema.size());
    }

    /**
     * Test leer inventario de la bd cuando no hay inventarios
     */
    @Test
    public void testLeerInventarioFail() throws SQLException {
        conector.borrarDb();
        conector.crearBaseDatos();
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        try{
            conector.leerInventario(inventariosSistema);
        }
        catch(SQLException e){
            assertEquals("No hay inventarios en la base de datos", e.getMessage());
        }
    }

    /**
     * Test de modificarPersonajeBd de la bd
     */
    @Test
    public void testModificarPersonajeBd() throws SQLException {
        Personaje personaje = null;
        try{
            personaje = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        }
        personaje.setNombre("Victarion Greyjoy");
        try{
            conector.modificarPersonajeBd(personaje);
        }
        catch(SQLException e){
            fail("No se ha podido modificar el personaje");
        }
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        }
        assertEquals(personaje.getNombre(), personajeBd.getNombre());
    }

    /**
     * Test modificar personaje de la bd cuando se intenta modificar con campos nulos
     */
    @Test
    public void testModificarPersonajeBdFail() throws SQLException {
        Personaje personaje = null;
        try{
            personaje = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        }
        personaje.setNombre(null);
        try{
            conector.modificarPersonajeBd(personaje);
            fail("No se ha lanzado la excepcion");
        }
        catch(IllegalArgumentException e){
            assertEquals("Uno o mas campos estan vacios", e.getMessage());
        }             
    }

    /**
     * Test borrarPersonajeInventarioBd de la bd
     */
    @Test
    public void testBorrarPersonajeInventarioBd() throws SQLException {
        Personaje personaje = null;
        try{
            personaje = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        }
        try{
            conector.borrarPersonajeIventarioBd(personaje);
        }
        catch(SQLException e){
            fail("No se ha podido borrar el personaje");
        }

        //Recuperamos el personaje de la bd
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(1);  
        }
        catch(SQLException e){
            assertEquals("No existe el personaje", e.getMessage());
            fail("Se ha lanzado la excepcion");
        }
        //Comprobamos que no existe el personaje
        assertNull(personajeBd);
    }

    /**
     * Test insertarHermandadEnBd
     */

    @Test
    public void testInsertarHermandadEnBd() throws SQLException {
        Hermandad hermandad = new Hermandad();
        hermandad.setNombreHermandad("Los Errantes");
        hermandad.setServidorHermandad("Sanguino");
        hermandad.setNumeroMiembros(0);
        try{
            conector.insertarHermandadEnBD(hermandad);
        }
        catch(Exception e){
            fail("No se ha podido insertar la hermandad");
        }
        //Traemos la hermandad de la bd
        Hermandad hermandadBd = null;
        try{
            hermandadBd = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        assertEquals(hermandad.getNombreHermandad(), hermandadBd.getNombreHermandad());
    }

    /**
     * Test insertar hermandad cuando se intenta insertar dos veces la misma hermandad
     */
    @Test
    public void testInsertarHermandadEnBdFail() throws SQLException {
        Hermandad hermandad = new Hermandad();
        hermandad.setNombreHermandad("Los Errantes");
        hermandad.setServidorHermandad("Los Errantes");
        hermandad.setNumeroMiembros(0);
        try{
            conector.insertarHermandadEnBD(hermandad);
        }
        catch(SQLIntegrityConstraintViolationException e){
            assertEquals("UNIQUE constraint failed: Hermandad.nombreHermandad, Hermandad.servidorHermandad", e.getMessage());
        }             
    }

    /**
     * Test insertar hermandad cuando se intenta insertar con campos nulos
     */
    @Test
    public void testInsertarHermandadEnBdFail2() throws SQLException {
        Hermandad hermandad = new Hermandad();
        hermandad.setNombreHermandad(null);
        hermandad.setServidorHermandad("Los Errantes");
        hermandad.setNumeroMiembros(0);
        try{
            conector.insertarHermandadEnBD(hermandad);
            fail("No se ha lanzado la excepcion");
        }
        catch(IllegalArgumentException e){
            assertEquals("Uno o mas campos estan vacios", e.getMessage());
        }             
    }

    /**
     * Test modificarHermandadEnBd de la bd
     */
    @Test
    public void testModificarHermandadEnBd() throws SQLException {
        //Traemos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        //Cambiamos el nombre
        hermandad.setNombreHermandad("Ana, me voy a matar");
        try{
            //La modificamos en la bd
            conector.modificarHermandadEnBd(hermandad);
        }
        catch(SQLException e){
            fail("No se ha podido modificar la hermandad");
        }
        //Traemos la hermandad de la bd
        Hermandad hermandadBd = null;
        try{
            hermandadBd = conector.getHermandad("Ana, me voy a matar", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        //Comprobamos que el nombre se ha modificado
        assertEquals(hermandad.getNombreHermandad(), hermandadBd.getNombreHermandad());
    }

    /**
     * Test de borrarHermandadBd()
     */
    @Test
    public void testBorrarHermandadBd() throws SQLException {
        //Traemos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        try{
            conector.borrarHermandadBd(hermandad);
        }
        catch(SQLException e){
            fail("No se ha podido borrar la hermandad");
        }
        //Traemos la hermandad de la bd
        Hermandad hermandadBd = null;
        try{
            hermandadBd = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            assertEquals("No existe la hermandad", e.getMessage());
            fail("Se ha lanzado la excepcion");
        }
        //Comprobamos que no existe la hermandad
        assertNull(hermandadBd);
    }

    /**
     * Test de leerHermandad de la bd
     */
    @Test
    public void testLeerHermandad() throws SQLException {
        ArrayList<Hermandad> hermandadesSistema = new ArrayList<>();
        try{
            conector.leerHermandad(hermandadesSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer las hermandades");
        }
        assertEquals(1, hermandadesSistema.size());
    }

    /**
     * Test leer hermandad de la bd cuando no hay hermandades
     */
    @Test
    public void testLeerHermandadFail() throws SQLException {
        conector.borrarDb();
        conector.crearBaseDatos();
        ArrayList<Hermandad> hermandadesSistema = new ArrayList<>();
        try{
            conector.leerHermandad(hermandadesSistema);
        }
        catch(SQLException e){
            assertEquals("No hay hermandades en la base de datos", e.getMessage());
        }
    }

    /**
     * Test leerHermandadPersonaje
     */
    @Test
    public void testLeerHermandadPersonaje() throws SQLException {
        ArrayList<Hermandad> hermandadesSistema = new ArrayList<>();
        ArrayList<Personaje> personajesSistema = new ArrayList<>();
        try{
            conector.leerHermandadPersonaje(hermandadesSistema, personajesSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer las hermandades");
        }
        //Leemos la hermandad
        try{
            conector.leerHermandad(hermandadesSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer las hermandades");
        }
        //Leemos el personaje
        try{
            conector.leerPersonajeDeBd(personajesSistema, new ArrayList<Inventario>(), new ArrayList<Hermandad>());
        }
        catch(SQLException e){
            fail("No se ha podido leer los personajes");
        }
        try{
            conector.leerHermandadPersonaje(hermandadesSistema, personajesSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer las hermandades");
        }
        assertEquals(1, personajesSistema.get(0).getListaHermandadades().size());
        assertEquals(1, hermandadesSistema.get(0).getListaMiembros().size());
    }

    /**
     * Test insertarPersonajeEnHermandad
     */
    @Test
    public void testInsertarPersonajeEnHermandad() throws SQLException {
        //Creamos un nuevo personaje
        Personaje personaje = new Personaje();
        personaje.setNombre("Tyrion");
        personaje.setServidor("Exodar");   
        personaje.setRaza("Humano");
        personaje.setNivel(14);
        personaje.setFaccion("Alianza");
        try{
            conector.insertarPersonajeYInventarioEnBD(personaje, personaje.getInventario());
        }
        catch(Exception e){
            fail("No se ha podido insertar el personaje");
        }
        personaje.setIdPersonaje(2);
        //Recuperamos el personaje
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(2);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        } 
       //Recuperamos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(Exception e){
            fail("No se ha podido insertar la hermandad");
        }
        //Insertamos el personaje en la hermandad
        try{
            conector.insertarPersonajeHermandad(personajeBd, hermandad);
        }
        catch(Exception e){
            fail("No se ha podido insertar el personaje en la hermandad");
        }
        //Traemos la hermandad de la bd
        ArrayList<Integer> personajesEnHermandad = null;
        try{
            personajesEnHermandad = conector.getPersonajesEnHermandad(hermandad.getIdHermandad());
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        //Comprobamos que el personaje se ha insertado en la hermandad
        assertEquals(2, personajesEnHermandad.size());
    }

    /**
     * Test insertar personaje en hermandad cuando se intenta insertar dos veces el mismo personaje
     */
    @Test
    public void testInsertarPersonajeEnHermandadFail() throws SQLException {
        //Recuperamos el personaje
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        } 
       //Recuperamos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido insertar la hermandad");
        }
        //Insertamos el personaje en la hermandad
        try{
            conector.insertarPersonajeHermandad(personajeBd, hermandad);
        }
        catch(SQLException e){
            assertTrue(e.getMessage().startsWith("Error al insertar PersonajeHermandad: UNIQUE constraint failed: PersonajeHermandad.idPersonaje, PersonajeHermandad.idHermandad"));

        }
    }

    /**
     * Test insertar personaje en hermandad cuando se intenta insertar con campos nulos
     */
    @Test
    public void testInsertarPersonajeEnHermandadFail2() throws SQLException {
        //Creamos un personaje con el id en null
        Personaje personaje = new Personaje();
        personaje.setNombre("Tyrion");
        personaje.setServidor("Exodar");
        personaje.setRaza("Humano");
        personaje.setNivel(14);
        personaje.setFaccion("Alianza");
        personaje.setIdPersonaje(null);

       //Recuperamos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido insertar la hermandad");
        }
        //Insertamos el personaje en la hermandad
        try{
            conector.insertarPersonajeHermandad(personaje, hermandad);
            fail("No se ha lanzado la excepcion");
        }
        catch(IllegalArgumentException e){
            assertEquals("Uno o mas campos estan vacios", e.getMessage());
        }             
    }

    /**
     * Test borrarPersonajeHermandad de la bd
     */ 
    @Test
    public void testBorrarPersonajeHermandad() throws SQLException {
        //Recuperamos el personaje
        Personaje personajeBd = null;
        try{
            personajeBd = conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("No se ha podido traer el personaje de la bd");
        } 
       //Recuperamos la hermandad de la bd
        Hermandad hermandad = null;
        try{
            hermandad = conector.getHermandad("Los Errantes", "Los Errantes");
        }
        catch(SQLException e){
            fail("No se ha podido insertar la hermandad");
        }
        //Borramos el personaje de la hermandad
        try{
            conector.borrarPersonajeHermandad(personajeBd, hermandad);
        }
        catch(SQLException e){
            fail("No se ha podido borrar el personaje de la hermandad");
        }
        //Traemos la hermandad de la bd
        ArrayList<Integer> personajesEnHermandad = null;
        try{
            personajesEnHermandad = conector.getPersonajesEnHermandad(hermandad.getIdHermandad());
        }
        catch(SQLException e){
            fail("No se ha podido traer la hermandad de la bd");
        }
        //Comprobamos que el personaje se ha borrado de la hermandad
        assertEquals(0, personajesEnHermandad.size());
    }


    /**
     * test insertarObjetoEnInventario
     */
    @Test
    public void testInsertarObjetoEnInventario() throws SQLException {
        //Creamos un nuevo objeto
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto("Cuchillo");
        objeto.setDescripcion("Arma");
        objeto.setPrecio(12);
        objeto.setRareza("Comun");
        objeto.setIdObjeto("OB5");
        try{
            conector.insertarObjetoEnBd(objeto);
        }
        catch(Exception e){
            fail("No se ha podido insertar el objeto");
        }
        //Traemos el objeto de la bd
        Objeto objetoBd = null;
        try{
            objetoBd = conector.getObjeto("OB5");
        }
        catch(SQLException e){
            fail("No se ha podido traer el objeto de la bd");
        }
        //Leermos los inventarios
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        try{
            conector.leerInventario(inventariosSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer los inventarios");
        }
        //Insertamos el objeto en el inventario del personaje
        try{
            conector.insertarObjetoEnInventario(objetoBd, inventariosSistema.get(0));
        }
        catch(SQLException e){
            fail("No se ha podido insertar el objeto en el inventario");
        }
        //Traemos el inventario de la bd
        ArrayList<String> objetosEnInventario = null;
        try{
            objetosEnInventario = conector.getObjetosEnInventario(inventariosSistema.get(0).getIdInventario());
        }
        catch(SQLException e){
            fail("No se ha podido traer el inventario de la bd");
        }
        //Comprobamos que el objeto se ha insertado en el inventario
        assertEquals(2, objetosEnInventario.size());
    }

    /**
     * Test borrarObjetoEnInventario de la bd
     */
    @Test
    public void testBorrarObjetoEnInventario() throws SQLException {
        //Traemos el objeto de la bd
        Objeto objetoBd = null;
        try{
            objetoBd = conector.getObjeto("OB50");
        }
        catch(SQLException e){
            fail("No se ha podido traer el objeto de la bd");
        }
        //Leermos los inventarios
        ArrayList<Inventario> inventariosSistema = new ArrayList<>();
        try{
            conector.leerInventario(inventariosSistema);
        }
        catch(SQLException e){
            fail("No se ha podido leer los inventarios");
        }
        //Borramos el objeto del inventario del personaje
        try{
            conector.borrarObjetoEnInventario(objetoBd, inventariosSistema.get(0));
        }
        catch(SQLException e){
            fail("No se ha podido borrar el objeto del inventario");
        }
        //Traemos el inventario de la bd
        ArrayList<String> objetosEnInventario = null;
        try{
            objetosEnInventario = conector.getObjetosEnInventario(inventariosSistema.get(0).getIdInventario());
        }
        catch(SQLException e){
            fail("No se ha podido traer el inventario de la bd");
        }
        //Comprobamos que el objeto se ha borrado del inventario
        assertEquals(0, objetosEnInventario.size());
    }
  }
