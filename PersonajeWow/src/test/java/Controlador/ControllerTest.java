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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.xml.sax.SAXException;

/**
 *
 * @author AdrianNS
 */
public class ControllerTest {

    private Ventana1 ventana;
    static Controller controlador;
    static  String nombreBd = "dbTest.sqlite";

    @BeforeEach
    public void setUp() {
        try {
            ventana = new Ventana1();
            controlador = new Controller(ventana, nombreBd);
            ventana.setControlador(controlador);  // Establece el controlador en la vista
            ventana.setVisible(false);
            controlador.conector.crearBaseDatos();
        } catch (IOException | ClassNotFoundException | SAXException e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDownClass() {
        controlador.conector.borrarDb();
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
}
