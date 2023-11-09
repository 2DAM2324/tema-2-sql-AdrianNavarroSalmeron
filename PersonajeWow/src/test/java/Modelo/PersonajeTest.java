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
     */
    @Test
    public void testGenerateNewIdPersonajeWith0LastId() {
        Personaje instance = new Personaje();
        instance.setLastIdPersonaje(0);
        String expResult = "PJ" +1;
        String result = instance.generateNewIdPersonaje();
        assertEquals(expResult, result);
    }

    @Test
    public void testGenerateNewIdPersonajeWithLastIdGreatterThan0(){
        Personaje instance = new Personaje();
        instance.setLastIdPersonaje(1);
        String expResult = "PJ" +2;
        String result = instance.generateNewIdPersonaje();
        assertEquals(expResult, result);
    }

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
