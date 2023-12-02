/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Controlador;

import Modelo.Conector;
import Modelo.Hermandad;
import Modelo.Inventario;
import Modelo.Objeto;
import Modelo.Personaje;
import Vista.Ventana1;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.xml.sax.SAXException;

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
            controlador = new Controller(ventana, nombreBd);
            ventana.setControlador(controlador);  // Establece el controlador en la vista
            ventana.setVisible(false);
            controlador.conector.borrarDb();
            controlador.conector.crearBaseDatos();
            
            //Dejamos los arrays del sistema vacios
           controlador.setArrayDeObjetosSistema(new ArrayList<>());
           controlador.setArrayDeInventariosSistema(new ArrayList<>());
           controlador.setArrayDePersonajesDeSistema(new ArrayList<>());
           
            //Setter, personaje, inventario y objeto
            controlador.aniadirObjeto("Espada", "Comun", "41", "Es una espada", "41");
            controlador.aniadirPersonaje("Victarion", "Sanguino", "Elfo", "60", "Horda");
            controlador.aniadirObjetoaInventario(controlador.getArrayDeObjetosSistema().get(0).getIdObjeto(), controlador.getArrayDePersonajesDeSistema().get(0).getInventario().getIdInventario());
            
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
}
