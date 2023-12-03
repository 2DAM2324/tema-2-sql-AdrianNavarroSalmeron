/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Controlador;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import Modelo.Inventario;
import Modelo.Objeto;
import Modelo.Personaje;
import Vista.Ventana1;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author AdrianNS
 */
public class ControllerTest {

    private Ventana1 ventana;
    static Controller controlador;
    static  String nombreBd = "dbTest.sqlite";
    public String idObjetoInicialTest = "";
    public String idInventarioTestInicial = "";
   

    @BeforeEach
    public void setUp() {
        try {
            ventana = new Ventana1();
            controlador = new Controller(ventana, nombreBd, true);
            ventana.setControlador(controlador);  // Establece el controlador en la vista
            ventana.setVisible(false);
            controlador.conector.borrarDb();
            controlador.conector.crearBaseDatos();
            
            //Dejamos los arrays del sistema vacios
           controlador.setArrayDeObjetosSistema(new ArrayList<>());
           controlador.setArrayDeInventariosSistema(new ArrayList<>());
           controlador.setArrayDePersonajesDeSistema(new ArrayList<>());
           controlador.setArrayDeHermandadesSistema(new ArrayList<>());
           
            //Setter, personaje, inventario y objeto
            controlador.aniadirObjeto("Espada", "Comun", "41", "Es una espada", "41");
            controlador.aniadirPersonaje("Victarion", "Sanguino", "Elfo", "60", "Horda");
            controlador.aniadirObjetoaInventario(controlador.getArrayDeObjetosSistema().get(0).getIdObjeto(), controlador.getArrayDePersonajesDeSistema().get(0).getInventario().getIdInventario());
            controlador.aniadirHermandad("Baptisterio","Sanguino" );
            controlador.aniadirPersonajeaHermandad("Victarion", "Sanguino", "Baptisterio", "Sanguino");
            
            //Almacenos los ids en la variable de clase
             idObjetoInicialTest = controlador.getArrayDeObjetosSistema().get(0).getIdObjeto();
             idInventarioTestInicial = controlador.getArrayDeInventariosSistema().get(0).getIdInventario();
        } catch (IOException | ClassNotFoundException | SAXException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDownClass() {
        controlador.cerrarConexionDesdeControlador(nombreBd);
    }
    
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testAniadirObjetoValido() {
        
        assertDoesNotThrow(() -> controlador.aniadirObjeto("TestObj", "Rare", "100", "Test Description", "TestId"));
    }
    
    @Test
    public void testAniadirObjetoInvalido() {
        Objeto objeto = new Objeto();
        controlador.aniadirObjeto(null, "Comun", "4.1", "Es una espada", "OB4");
        
        // Retrieve the object from the database
        try{
            objeto = controlador.conector.getObjeto("OB4");
        }
        catch(SQLException e){
            fail("Failed to retrieve object: " + e.getMessage());
        }   
        assertNull(objeto);
    }
    
       
    @Test
    public void testBorrarObjetoValido() {
        
        String idObjetoaBorrar = "testId";
        Objeto objetoaBorrar = new Objeto();
        objetoaBorrar.setNombreObjeto("testName");
        objetoaBorrar.setDescripcion("testDescription");
        objetoaBorrar.setRareza("testRarity");
        objetoaBorrar.setPrecio(14);
        objetoaBorrar.setIdObjeto(idObjetoaBorrar);

        Inventario inventario = new Inventario();
        inventario.getObjetosInventario().add(objetoaBorrar);

        controlador.setArrayDeObjetosSistema(new ArrayList<>(Arrays.asList(objetoaBorrar)));
        controlador.setArrayDeInventariosSistema(new ArrayList<>(Arrays.asList(inventario)));
        
        //Insertamos el objeto en la base de datos
        try {
            controlador.conector.insertarObjetoEnBd(objetoaBorrar);
        } catch (SQLException e) {
            fail("La base de datos ha fallado: " + e.getMessage());
        }

        // Borramos el objeto
        controlador.borrarObjeto(idObjetoaBorrar);

        // Testeamos que el bojeto no este en los arrays del sistema ni en el inventario
        assertFalse(controlador.getArrayDeObjetosSistema().contains(objetoaBorrar));
        assertFalse(inventario.getObjetosInventario().contains(objetoaBorrar));

        // Comprobamos la base de datos
        Objeto objetoFromDb = null;
        try {
            objetoFromDb = controlador.conector.getObjeto(idObjetoaBorrar);
        } catch (SQLException e) {
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(objetoFromDb, "El objeto ha sido borrado de la base de datos");
    }
    
    @Test
    public void testBorrarObjetoNoExistente() {
        // Arrange
        String idObjetoaBorrar = "testId";
        
        //Dejamos los arrays vacios
        controlador.setArrayDeObjetosSistema(new ArrayList<>());
        controlador.setArrayDeInventariosSistema(new ArrayList<>());

        //Borramos el objeto que no existe
        controlador.borrarObjeto(idObjetoaBorrar);

        //  Comprobamos la base de datos
        Objeto objetoFromDb = null;
        try {
            objetoFromDb = controlador.conector.getObjeto(idObjetoaBorrar);
        } catch (SQLException e) {
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(objetoFromDb, "El objeto no debería existir en la base de datos");
    }

    /**
     * Test de modificarObjeto de la clase Controller, cuando el objeto existe y los datos son validos
     */
    @Test
    public void testModificarObjetoExistenteValido(){
        controlador.modificarObjeto(idObjetoInicialTest, "Cumbia", "Legendaria", "2.1", "Tremenda cumbia");
        Objeto objetoModificado = null;
        try{
            objetoModificado = controlador.conector.getObjeto(idObjetoInicialTest);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("Cumbia", objetoModificado.getNombreObjeto(), "El nombre del objeto no se ha modificado");
        assertEquals("Legendaria", objetoModificado.getRareza(), "La rareza del objeto no se ha modificado");
        assertEquals(2.1, objetoModificado.getPrecio(), "El precio del objeto no se ha modificado");
        assertEquals("Tremenda cumbia", objetoModificado.getDescripcion(), "La descripcion del objeto no se ha modificado");
    }

    /**
     * Test de modificarObjeto de la clase Controller, cuando el objeto no existe
     */ 
    @Test
    public void testModificarObjetoNoExistente(){
        controlador.modificarObjeto("idInvalida", "Cumbia", "Legendaria", "2.1", "Tremenda cumbia");
        Objeto objetoModificado = null;
        try{
            objetoModificado = controlador.conector.getObjeto("idInvalida");
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(objetoModificado, "El objeto no debería existir en la base de datos");
    }

    /**
     * Test de modificarObjeto de la clase Controller, cuando el objeto existe y el precio es < 0
     */
    @Test
    public void testModificarObjetoExistentePrecioInvalido(){
        controlador.modificarObjeto(idObjetoInicialTest, "Cumbia", "Legendaria", "-2.1", "Tremenda cumbia");
        Objeto objetoModificado = null;
        try{
            objetoModificado = controlador.conector.getObjeto(idObjetoInicialTest);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("Espada", objetoModificado.getNombreObjeto(), "El nombre del objeto no se ha modificado");
        assertEquals("Comun", objetoModificado.getRareza(), "La rareza del objeto no se ha modificado");
        assertEquals(41, objetoModificado.getPrecio(), "El precio del objeto no se ha modificado");
        assertEquals("Es una espada", objetoModificado.getDescripcion(), "La descripcion del objeto no se ha modificado");
    }

    /**
     * Test de modificarObjeto de la clase Controller, cuando el objeto existe y el precio no es un numero
     * Los datos no se modifican
     */
    @Test
    public void testModificarObjetoExistentePrecioNoNumero(){
        controlador.modificarObjeto(idObjetoInicialTest, "Cumbia", "Legendaria", "2.1a", "Tremenda cumbia");
        Objeto objetoModificado = null;
        try{
            objetoModificado = controlador.conector.getObjeto(idObjetoInicialTest);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("Espada", objetoModificado.getNombreObjeto(), "El nombre del objeto no se ha modificado");
        assertEquals("Comun", objetoModificado.getRareza(), "La rareza del objeto no se ha modificado");
        assertEquals(41, objetoModificado.getPrecio(), "El precio del objeto no se ha modificado");
        assertEquals("Es una espada", objetoModificado.getDescripcion(), "La descripcion del objeto no se ha modificado");
    }

    /**
     * Test de modificarObjeto de la clase Controller, cuando el objeto existe y el nombre es null
     * Los datos no se modifican
     */
    @Test
    public void testModificarObjetoExistenteNombreNull(){
        controlador.modificarObjeto(idObjetoInicialTest, null, "Legendaria", "2.1", "Tremenda cumbia");
        Objeto objetoModificado = null;
        try{
            objetoModificado = controlador.conector.getObjeto(idObjetoInicialTest);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("Espada", objetoModificado.getNombreObjeto(), "El nombre del objeto no se ha modificado");
        assertEquals("Comun", objetoModificado.getRareza(), "La rareza del objeto no se ha modificado");
        assertEquals(41, objetoModificado.getPrecio(), "El precio del objeto no se ha modificado");
        assertEquals("Es una espada", objetoModificado.getDescripcion(), "La descripcion del objeto no se ha modificado");
    }
    /**
     * Test de borrarObjetoInventario de la clase Controller, cuando el objeto si existe
     */
    
    @Test
    public void testAniadirObjetoaInventarioValido(){
        
        String idObjetoPruebaAniadir = "";
        //Creamos un objeto 
        controlador.aniadirObjeto("Daga", "Rara", "41", "Es una daga", "2.1");
        idObjetoPruebaAniadir = controlador.getArrayDeObjetosSistema().get(1).getIdObjeto();
        
       
        //Insertamos el objeto en el inventario
        controlador.aniadirObjetoaInventario(idObjetoPruebaAniadir, idInventarioTestInicial);
        
        //Comprobamos que el objeto se ha añadido al inventario
        assertTrue(controlador.getArrayDeInventariosSistema().get(0).getObjetosInventario().contains(controlador.getArrayDeObjetosSistema().get(1)), "El objeto no se ha añadido al inventario");
        
        //Comprobamos que el objeto se ha añadido a la base de datos
        String idObjetoComprobado = "";
        try{
            idObjetoComprobado = controlador.conector.getIdObjetoEnTablaInventarioObjeto(idObjetoPruebaAniadir, idInventarioTestInicial);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals(idObjetoPruebaAniadir, idObjetoComprobado, "El objeto no se ha añadido a la base de datos");
    }

    /**
     * Añadir un objeto que no existe a un inventario
     * Comprueba que el tamaño sigue siendo 1 despues de añadir el objeto, de manera que sabemos que no se ha añadido
     */

    @Test 
    public void testAniadirObjetoaInventarioInvalido(){
        controlador.aniadirObjetoaInventario("PATATA", idInventarioTestInicial);
        assertTrue(controlador.getArrayDeInventariosSistema().get(0).getObjetosInventario().size() ==1, "El objeto no se ha añadido al inventario");
    }
    
    

    @Test
    public void testBorrarObjetoInventarioValido(){
        String idObjetoComprobado = "";
        controlador.borrarObjetoInventario(idObjetoInicialTest, idInventarioTestInicial);
        assertTrue(controlador.getArrayDeObjetosSistema().size() ==1, "El array de objetos debería de tener solo 1 objeto");
        assertTrue(controlador.getArrayDeInventariosSistema().get(0).getObjetosInventario().isEmpty(), "El array de inventarios deberia de tener solo 1 inventario");
        
        // Comprobamos la base de datos
       try{
           //Si el objeto se ha borrado correctamente el metodo nos devuelve null
           idObjetoComprobado =controlador.conector.getIdObjetoEnTablaInventarioObjeto(idObjetoInicialTest, idInventarioTestInicial);
       }
       catch(SQLException e){
                fail("La base de datos ha fallado: " + e.getMessage());
           }
       
        assertNull(idObjetoComprobado, "El objeto no debería existir en la base de datos");
    }
    
    /**
     * Test de borrarObjetoInventario de la clase Controller, cuando el objeto no existe
     */
    
    @Test
    public void testBorrarObjetoInventarioInvalido() {
        // Arrange
        String idObjetoInvalido = "idInvalida";
        String idInventarioInvalido = "idInvalida";
        
   
        //Borramos un objeto invalido
        controlador.borrarObjetoInventario(idObjetoInvalido, idInventarioInvalido);
        assertTrue(controlador.getArrayDeObjetosSistema().size() == 1, "El array de objetos debería de tener solo 1 objeto");
        assertTrue(controlador.getArrayDeInventariosSistema().size() == 1, "El array de inventarios deberia de tener solo 1 inventario");

        // Comprobamos la base de datos
        Objeto objetoBd = null;
        try {
            objetoBd = controlador.conector.getObjeto(idObjetoInvalido);
        } catch (SQLException e) {
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(objetoBd, "El objeto no debería existir en la base de datos");
    }

    /**
     * Test de vaciarInventario de la clase Controller, cuando el inventario existe
     */
    @Test
    public void testVaciarInventarioValido(){
        String idObjetoBorradoString = "";
        controlador.vaciarInventario(idInventarioTestInicial);
        try{
           idObjetoBorradoString = controlador.conector.getIdObjetoEnTablaInventarioObjeto(idObjetoInicialTest, idInventarioTestInicial);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(idObjetoBorradoString, "El objeto no debería existir en la base de datos");
        assertTrue(controlador.getArrayDeInventariosSistema().get(0).getObjetosInventario().isEmpty(), "El inventario se ha vaciado");
    }

    /**
     * Test de vaciarInventario de la clase Controller, cuando el inventario no existe
     */
    @Test
    public void testVaciarInventarioInvalido(){
        String idInventarioInvalido = "idInvalida";
        String idObjetoBorradoString = "";
        controlador.vaciarInventario(idInventarioInvalido);
        try{
            idObjetoBorradoString = controlador.conector.getIdObjetoEnTablaInventarioObjeto(idObjetoInicialTest, idInventarioTestInicial);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals(idObjetoBorradoString, idObjetoInicialTest, "El objeto debería existir en la base de datos");
        assertTrue(controlador.getArrayDeInventariosSistema().get(0).getObjetosInventario().size() == 1, "El inventario no se ha vaciado");
    }

    /**
     * Test de aniadirPersonaje de la clase Controller, cuando el personaje no existe
     */
    @Test
    public void testAniadirPersonajeValido(){
        controlador.aniadirPersonaje("TestName", "TestRaza", "TestRaza", "12", "TestFaccion");
        for(int i = 0; i < controlador.getArrayDePersonajesDeSistema().size(); i++){
            System.out.println(controlador.getArrayDePersonajesDeSistema().get(i).getNombre());
        }
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(2);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("TestName", personaje.getNombre(), "El nombre del personaje no se ha añadido correctamente");
        assertEquals("TestRaza", personaje.getRaza(), "La raza del personaje no se ha añadido correctamente");
        assertEquals(12, personaje.getNivel(), "El nivel del personaje no se ha añadido correctamente");
        assertEquals("TestFaccion", personaje.getFaccion(), "La faccion del personaje no se ha añadido correctamente");
        assertTrue(controlador.getArrayDePersonajesDeSistema().size() == 2, "El personaje se ha añadido al array de personajes");
        assertTrue(controlador.getArrayDeInventariosSistema().size() == 2, "El personaje se ha añadido al array de inventarios");
        assertTrue(controlador.getArrayDePersonajesDeSistema().get(1).getInventario().getObjetosInventario().isEmpty(), "El personaje se ha añadido al array de personajes");
    }

    /**
     * Test de aniadirPersonaje de la clase Controller, cuando el nivel no es un numero
     * El personaje no se añade a la bd
     */
    @Test
    public void testAniadirPersonajeNivelNoNumero(){
        controlador.aniadirPersonaje("TestName", "TestRaza", "TestRaza", "12a", "TestFaccion");
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(2);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(personaje, "El personaje no debería existir en la base de datos");
        assertTrue(controlador.getArrayDePersonajesDeSistema().size() == 1, "El personaje no se ha añadido al array de personajes");
        assertTrue(controlador.getArrayDeInventariosSistema().size() == 1, "El personaje no se ha añadido al array de inventarios");
    }

    /**
     * Test de modificarPersonaje cuando es valido y el personaje existe
     */
    @Test
    public void testModificarPersonajeValido(){
        controlador.modificarPersonaje(1, "TestName", "TestRaza", "TestRaza", 12, "TestFaccion");
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("TestName", personaje.getNombre(), "El nombre del personaje no se ha modificado correctamente");
        assertEquals("TestRaza", personaje.getRaza(), "La raza del personaje no se ha modificado correctamente");
        assertEquals(12, personaje.getNivel(), "El nivel del personaje no se ha modificado correctamente");
        assertEquals("TestFaccion", personaje.getFaccion(), "La faccion del personaje no se ha modificado correctamente");
    }

    /**
     * Test de modificarPersonaje cuando el personaje no existe
     */
    @Test
    public void testModificarPersonajeNoExistente(){
        controlador.modificarPersonaje(2, "TestName", "TestRaza", "TestRaza", 12, "TestFaccion");
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(2);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(personaje, "El personaje no debería existir en la base de datos");
    }

  /**
   * Test borrarPersonaje cuando es valido y existe
   * 
   */
    @Test
    public void testBorrarPersonajeValido(){
        controlador.borrarPersonaje("Victarion","Sanguino");
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertNull(personaje, "El personaje no debería existir en la base de datos");
        assertTrue(controlador.getArrayDePersonajesDeSistema().isEmpty(), "El personaje no se ha borrado del array de personajes");
        assertTrue(controlador.getArrayDeInventariosSistema().isEmpty(), "El personaje no se ha borrado del array de inventarios");
    }

    /**
     * Test borrarPersonaje cuando se introduce el nombre correcto pero el servidor es invalido
     * El personaje no se borra y sigue existiendo en la bd
     */
    @Test
    public void testBorrarPersonajeNombreInvalido(){
        controlador.borrarPersonaje("Victarion","España");
        Personaje personaje = null;
        try{
            personaje = controlador.conector.getPersonaje(1);
        }
        catch(SQLException e){
            fail("La base de datos ha fallado: " + e.getMessage());
        }
        assertEquals("Victarion", personaje.getNombre(), "El personaje no debería existir en la base de datos");
        assertEquals("Sanguino", personaje.getServidor(), "El personaje no debería existir en la base de datos");
        assertTrue(controlador.getArrayDePersonajesDeSistema().size() == 1, "El personaje no se ha borrado del array de personajes");
        assertTrue(controlador.getArrayDeInventariosSistema().size() == 1, "El personaje no se ha borrado del array de inventarios");
    }
}
