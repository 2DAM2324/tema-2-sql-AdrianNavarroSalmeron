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
 * @author salme
 */
public class InventarioTest {
    
    public InventarioTest() {
    }

 
    
    /**
     * Test of getCapacidadMaxima method, of class Inventario.
     */
    @Test
    public void testGetCapacidadMaxima() {
        System.out.println("getCapacidadMaxima");
        Inventario instance = new Inventario();
        int expResult = 20;
        int result = instance.getCapacidadMaxima();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEspaciosOcupados method, of class Inventario.
     */
    @Test
    public void testSetEspaciosOcupados() {
        System.out.println("setEspaciosOcupados");
        int ocupados = 0;
        Inventario instance = new Inventario();
        instance.setEspaciosOcupados(ocupados);
        assertEquals(ocupados, instance.espaciosOcupados);
    }

    /**
     * Test of getEspaciosOcupados method, of class Inventario.
     */
    @Test
    public void testGetEspaciosOcupadosCuandoSeCreaElInventario() {
        Inventario instance = new Inventario();
        int expResult = 0;
        int result = instance.getEspaciosOcupados();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEspaciosOcupados method, of class Inventario.
     */
    @Test
    public void testGetEspaciosOcupadosCuandoSeAnadeUnObjeto() {
        Inventario instance = new Inventario();
        Objeto objeto = new Objeto();
        objeto.setNombreObjeto("Espada");
        instance.setAnadirObjetoAInventario(objeto);
        int expResult = 1;
        int result = instance.getEspaciosOcupados();
        assertEquals(expResult, result);
    }

    /**
     * Test of setObjetosInventario method, of class Inventario.
     */
    @Test
    public void testSetObjetosInventario() {
        ArrayList<Objeto> vectorObjetos = new ArrayList<>();
        Inventario instance = new Inventario();
        instance.setObjetosInventario(vectorObjetos);
        assertEquals(vectorObjetos, instance.objetosInventario);
    }

    /**
     * Test of getObjetosInventario method, of class Inventario.
     */
    @Test
    public void testGetObjetosInventario() {
        Inventario instance = new Inventario();
        ArrayList<Objeto> expResult = new ArrayList<>();
        ArrayList<Objeto> result = instance.getObjetosInventario();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIdPersonaje method, of class Inventario.
     */
    @Test
    public void testSetIdPersonaje() {
        System.out.println("setIdPersonaje");
        String idPersonajeSet = "Prueba";
        Inventario instance = new Inventario();
        instance.setIdPersonaje(idPersonajeSet);
        assertEquals(idPersonajeSet, instance.idPersonaje);
    }

    /**
     * Test of getIdPersonaje method, of class Inventario.
     */
    @Test
    public void testGetIdPersonaje() {
        Inventario instance = new Inventario();
        String expResult = "Hola";
        instance.setIdPersonaje(expResult);
        String result = instance.getIdPersonaje();
        assertEquals(expResult, result);
    }

    /**
     * Test of comprobarSiEspacio method, of class Inventario.
     */
    @Test
    public void testComprobarSiEspacioCuandoHayEspacio() {
        int maxima = 20;
        int ocupados = 15;
        Inventario instance = new Inventario();
        Boolean expResult = true;
        Boolean result = instance.comprobarSiEspacio(maxima, ocupados);
        assertEquals(expResult, result);
    }


     @Test
    public void testComprobarSiEspacioCuandoNoHayEspacio() {
        int maxima = 20;
        int ocupados = 20;
        Inventario instance = new Inventario();
        Boolean expResult = false;
        Boolean result = instance.comprobarSiEspacio(maxima, ocupados);
        assertEquals(expResult, result);
    }

    /**
     * Test of comprobarSiObjetoEnInventario method, of class Inventario.
     */
    @Test
    public void testComprobarSiObjetoEnInventarioCuandoObjetoYaEstaEnInventario() {
        Objeto objetoAnadir = new Objeto();
        objetoAnadir.setNombreObjeto("Espada");
        Inventario instance = new Inventario();
        instance.setAnadirObjetoAInventario(objetoAnadir);
        boolean expResult = true;
        boolean result = instance.comprobarSiObjetoEnInventario(objetoAnadir);
        assertEquals(expResult, result);
    }


     @Test
    public void testComprobarSiObjetoEnInventarioCuandoObjetoNoEstaEnInventario() {
        Objeto objetoAnadir = new Objeto();
        objetoAnadir.setNombreObjeto("Espada");
        Inventario instance = new Inventario();
        boolean expResult = false;
        boolean result = instance.comprobarSiObjetoEnInventario(objetoAnadir);
        assertEquals(expResult, result);
    }


    /**
     * Test of setAnadirObjetoAInventario method, of class Inventario.
     */
    @Test
    public void testSetAnadirObjetoAInventario() {
        Objeto objetoAnadir = new Objeto();
        objetoAnadir.setNombreObjeto("Espada");
        Inventario instance = new Inventario();
        String expResult = "Espada";
        instance.setAnadirObjetoAInventario(objetoAnadir);
        String result = instance.objetosInventario.get(0).getNombreObjeto();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPosicionObjetoEnInventario method, of class Inventario.
     */
    @Test
    public void testGetPosicionObjetoEnInventarioSiElObjetoNoEstaEnElInventario() {
        String idObjeto = "OB1";
        Inventario instance = new Inventario();
        int expResult = -1;
        int result = instance.getPosicionObjetoEnInventario(idObjeto);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPosicionObjetoEnInventarioSiElObjetoSiEstaEnElInventario(){
        Inventario instance = new Inventario();
        Objeto objetoAnadir1 = new Objeto();
        Objeto objetoAnadir2 = new Objeto();
        String idObjeto = objetoAnadir2.getIdObjeto();
        instance.setAnadirObjetoAInventario(objetoAnadir1);
        instance.setAnadirObjetoAInventario(objetoAnadir2);
        int expResult = 1;
        int result = instance.getPosicionObjetoEnInventario(idObjeto);
        assertEquals(expResult, result);
    }

    /**
     * Test of setBorrarObjetoDeInventario method, of class Inventario.
     */
    @Test
    public void testSetBorrarObjetoDeInventarioSiObjetoNoEstaEnInventario() {
        String idObjetoAborrar = "IdObjetoQueNoEstaEnInventario";
        Inventario instance = new Inventario();
        Objeto objetoAnadir1 = new Objeto();
        instance.setAnadirObjetoAInventario(objetoAnadir1);
        int expResult = 1;
        instance.setBorrarObjetoDeInventario(idObjetoAborrar);
        int result = instance.getObjetosInventario().size();
        assertEquals(expResult, result);
    }

    @Test 
    public void testSetBorrarObjetoDeInventarioSiObjetoSiEstaEnInventario(){
        Inventario instance = new Inventario();
        Objeto objetoAnadir1 = new Objeto();
        Objeto objetoAnadir2 = new Objeto();
        String idObjeto = objetoAnadir2.getIdObjeto();
        instance.setAnadirObjetoAInventario(objetoAnadir1);
        instance.setAnadirObjetoAInventario(objetoAnadir2);
        int expResult = 1;
        instance.setBorrarObjetoDeInventario(idObjeto);
        int result = instance.getObjetosInventario().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of generateNewIdInventario method, of class Inventario.
     */
    @Test
    public void testGenerateNewIdInventarioWith0LastId() {
        Inventario instance = new Inventario();
        instance.setLastIdInventario(0);
        String expResult = "INV"+1;
        String result = instance.generateNewIdInventario();
        assertEquals(expResult, result);
    }

    @Test
    public void testGenerateNewIdInventarioWithGreatterThan0LastId(){
      Inventario instance = new Inventario();
      String expResult = "INV" + (instance.getLastIdInventario() +1);
      String result = instance.generateNewIdInventario();
      assertEquals(expResult,result);
    }    
}
