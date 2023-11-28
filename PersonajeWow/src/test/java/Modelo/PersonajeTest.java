/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author AdrianNS
 */
public class PersonajeTest {
    
    public PersonajeTest() {
    }
  
    /**
     * Test of generateNewIdPersonaje method, of class Personaje.
     
    /**
     * Test of setAniadirInventarioaPersonaje method, of class Personaje.
     */
    @Test
    public void testSetAniadirInventarioaPersonaje() {
        Inventario inventario = new Inventario();
        Personaje instance = new Personaje();
        instance.setAniadirInventarioaPersonaje(inventario);
        assertEquals(inventario, instance.getInventario());
    }    
}
