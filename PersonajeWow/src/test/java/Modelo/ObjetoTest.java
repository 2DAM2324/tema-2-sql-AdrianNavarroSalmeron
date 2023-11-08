/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Modelo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author salme
 */
public class ObjetoTest {
    
    public ObjetoTest() {
    }
    
    /**
     * Test of setIdObjeto method, of class Objeto.
     */
    @Test
    public void testSetIdObjeto() {
        String id = "idObjeto";
        Objeto instance = new Objeto();
        instance.setIdObjeto(id);
        assertEquals(id, instance.idObjeto);
    }

    /**
     * Test of getIdObjeto method, of class Objeto.
     */
    @Test
    public void testGetIdObjeto() {
        Objeto instance = new Objeto();
        instance.setIdObjeto("id");
        String expResult = "id";
        String result = instance.getIdObjeto();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRareza method, of class Objeto.
     */
    @Test
    public void testSetRareza() {
       
        String rarezaSet = "raro";
        Objeto instance = new Objeto();
        instance.setRareza(rarezaSet);
        assertEquals(rarezaSet, instance.rareza);
    }

    /**
     * Test of getRareza method, of class Objeto.
     */
    @Test
    public void testGetRareza() {
        Objeto instance = new Objeto();
        String expResult = null;
        String result = instance.getRareza();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescripcion method, of class Objeto.
     */
    @Test
    public void testSetDescripcion() {
        String descripcionSet = "";
        Objeto instance = new Objeto();
        instance.setDescripcion(descripcionSet);
        assertEquals(descripcionSet, instance.descripcion);
    }

    /**
     * Test of getDescripcion method, of class Objeto.
     */
    @Test
    public void testGetDescripcion() {
        Objeto instance = new Objeto();
        String expResult = null;
        String result = instance.getDescripcion();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPrecio method, of class Objeto.
     */
    @Test
    public void testSetPrecio() {
        double precioSet = 0.0;
        Objeto instance = new Objeto();
        instance.setPrecio(precioSet);
        assertEquals(precioSet, instance.precio);
    }

    /**
     * Test of getPrecio method, of class Objeto.
     */
    @Test
    public void testGetPrecio() {
        Objeto instance = new Objeto();
        double expResult = 0.0;
        double result = instance.getPrecio();
        assertEquals(expResult, result, 0);

    }

    /**
     * Test of setNombreObjeto method, of class Objeto.
     */
    @Test
    public void testSetNombreObjeto() {
        String objetoSet = "";
        Objeto instance = new Objeto();
        instance.setNombreObjeto(objetoSet);
        assertEquals(objetoSet, instance.nombreObjeto);
    }

    /**
     * Test of getNombreObjeto method, of class Objeto.
     */
    @Test
    public void testGetNombreObjeto() {
        Objeto instance = new Objeto();
        String expResult = null;
        String result = instance.getNombreObjeto();
        assertEquals(expResult, result);
    }

    /**
     * Test of generateNewIdObjeto method, of class Objeto.
     */
    @Test
    public void testGenerateNewIdObjetoWith0LastId() {
        //Si la lastIdObjeto es 0, el id generado será OB0
        Objeto instance = new Objeto();
        instance.setLastIdObjeto(0);
        String expResult = "OB" + 1;
        String result = instance.generateNewIdObjeto();
        assertEquals(expResult, result);
    }

    @Test
    public void testGenerateNewIdObjetoWithLastIdGreatterThan0(){
        //Si la lastIdObjeto es mayor que 0, el id generado será OB + lastIdObjeto + 1
        Objeto instance = new Objeto();
        String expResult = "OB" + (instance.getLastIdObjeto()+1);
        String result = instance.generateNewIdObjeto();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Objeto.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Objeto instance = new Objeto();
        String expResult = "Id Objeto: " + instance.getIdObjeto() + "\nRareza: " + instance.getRareza()
            + "\nDescripcion: " + instance.getDescripcion() + "\nPrecio: " + instance.getPrecio()
            + "\nNombre Objeto: " + instance.getNombreObjeto();
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
