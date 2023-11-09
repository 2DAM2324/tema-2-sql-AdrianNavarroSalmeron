/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author salme
 */
public class HermandadTest {
    
    public HermandadTest() {
    }
    
    /**
     * Test of generateNewIdHermandad method, of class Hermandad.
     */
    @Test
    public void testGenerateNewIdHermandadPersonajeWith0LastId() {
        Hermandad instance = new Hermandad();
        instance.setLastIdHermandad(0);
        String expResult = "GUILD" +1;
        String result = instance.generateNewIdHermandad();
        assertEquals(expResult, result); 
    }

    @Test 
    public void testGenerateNewIdHermandadPersonajeWithLastIdGreatterThan0(){
        Hermandad instance = new Hermandad();
        instance.setLastIdHermandad(1);
        String expResult = "GUILD" +2;
        String result = instance.generateNewIdHermandad();
        assertEquals(expResult, result); 
    }
    
}
