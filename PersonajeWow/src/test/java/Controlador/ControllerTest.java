package Controlador;

import Modelo.Conector;
import Modelo.Objeto;
import Vista.Ventana1;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ControllerTest {

    @Mock
    private Conector mockConector;
    @Mock
    private Ventana1 mockVentana;

    @InjectMocks
    private Controller controlador;

@BeforeEach
public void setUp() {
    mockVentana = Mockito.mock(Ventana1.class);

    // Creamos los mocks para las tablas de la vista que vamos a necesitar
    JTable mockTableObjeto = Mockito.mock(JTable.class);
    JTable mockTableInventario = Mockito.mock(JTable.class);
    JTable mockTablePersonaje = Mockito.mock(JTable.class);
    JTable mockTableHermandad = Mockito.mock(JTable.class);

    // Creamos el mock para el Default Table Model
    DefaultTableModel mockModel = Mockito.mock(DefaultTableModel.class);

    // Cuando se accede a alguna de estas tablas devuelve el mock de JTable
    mockVentana.jTable_objeto_objeto = mockTableObjeto;
    mockVentana.jTable_inventario_objetos = mockTableInventario;
    mockVentana.jTable_personaje = mockTablePersonaje;
    mockVentana.jTable_hermandad = mockTableHermandad;
    

    // Cuando se llama al modulo getModel() devuelve el mock de DefaultTableModel
    Mockito.when(mockTableObjeto.getModel()).thenReturn(mockModel);
    Mockito.when(mockTableInventario.getModel()).thenReturn(mockModel);
    Mockito.when(mockTablePersonaje.getModel()).thenReturn(mockModel);
    Mockito.when(mockTableHermandad.getModel()).thenReturn(mockModel);

    //Creamos un mock estatico de getInstance del Conector, para poder aislarlo y  que las pruebas no se hagan sobre la base de datos real
    mockConector = Mockito.mock(Conector.class);
    try (MockedStatic<Conector> mocked = Mockito.mockStatic(Conector.class)) {
        mocked.when(Conector::getInstancia).thenReturn(mockConector);
        controlador = new Controller(mockVentana);
    }
}

    /**
     * Test de aniadirObjeto, de la clase Controller.
     */

    @Test
    public void testAniadirObjeto() {
       
        String nombreObjeto = "Espada";
        String rareza = "Comun";
        String precio = "10.5";
        String descripcion = "Es una espada";
        String IdObjeto = "OB2";

        double precioDouble = Double.parseDouble(precio);

       
        controlador.aniadirObjeto(nombreObjeto, rareza, precio, descripcion, IdObjeto);

         //Creamos un captor de argumentos que nos va a permitir capturar el objeto que se le pasa al metodo insertarObjetoEnBd
        ArgumentCaptor<Objeto> argumentCaptor = ArgumentCaptor.forClass(Objeto.class);

        // Verificamos que se ha llamado al metodo insertarObjetoEnBd con un objeto y capturamos el argumento
        verify(mockConector).insertarObjetoEnBd(argumentCaptor.capture());

        // Obtenemos el objeto capturado
        Objeto capturedObjeto = argumentCaptor.getValue();

        //Comprobamos que el objeto capturado tiene los valores esperados
        assertEquals(nombreObjeto, capturedObjeto.getNombreObjeto());
        assertEquals(rareza, capturedObjeto.getRareza());
        assertEquals(precioDouble, capturedObjeto.getPrecio(), 0.001);
        assertEquals(descripcion, capturedObjeto.getDescripcion());
        assertEquals(IdObjeto, capturedObjeto.getIdObjeto());
    }

   @Test
    public void testAniadirObjetoSinNombre() {
       
        String nombreObjeto = null;
        String rareza = "Comun";
        String precio = "10.5";
        String descripcion = "Es una espada";
        String IdObjeto = "OB2";

        controlador.aniadirObjeto(nombreObjeto, rareza, precio, descripcion, IdObjeto);

        // Verificamos que se ha llamado al metodo insertarObjetoEnBd con un objeto y capturamos el argumento
        verify(mockConector, never()).insertarObjetoEnBd(any(Objeto.class));
    }

    @Test
    public void testAniadirObjetoSinRareza() {
       
        String nombreObjeto = "Espada";
        String rareza = null;
        String precio = "10.5";
        String descripcion = "Es una espada";
        String IdObjeto = "OB2";

        controlador.aniadirObjeto(nombreObjeto, rareza, precio, descripcion, IdObjeto);

        // Verificamos que se ha llamado al metodo insertarObjetoEnBd con un objeto y capturamos el argumento
        verify(mockConector, never()).insertarObjetoEnBd(any(Objeto.class));
    }

    @Test
    public void testAniadirObjetoSinPrecio() {
       
        String nombreObjeto = "Espada";
        String rareza = "Comun";
        String precio = null;
        String descripcion = "Es una espada";
        String IdObjeto = "OB2";

        controlador.aniadirObjeto(nombreObjeto, rareza, precio, descripcion, IdObjeto);

        // Verificamos que se ha llamado al metodo insertarObjetoEnBd con un objeto y capturamos el argumento
        verify(mockConector, never()).insertarObjetoEnBd(any(Objeto.class));
    }

    @Test
    public void testAniadirObjetoSinDescripcion() {
       
        String nombreObjeto = "Espada";
        String rareza = "Comun";
        String precio = "10.5";
        String descripcion = null;
        String IdObjeto = "OB2";

        controlador.aniadirObjeto(nombreObjeto, rareza, precio, descripcion, IdObjeto);

        // Verificamos que se ha llamado al metodo insertarObjetoEnBd con un objeto y capturamos el argumento
        verify(mockConector, never()).insertarObjetoEnBd(any(Objeto.class));
    }

   
    @Test
    public void testBorrarObjeto() {
        //Insertamos en el array el objeto
        String IdObjeto = "OB1";
        Objeto objeto = new Objeto();
        objeto.setIdObjeto(IdObjeto);
        objeto.setNombreObjeto("Espada");
        objeto.setRareza("Comun");
        objeto.setPrecio(10.5);
        objeto.setDescripcion("Es una espada");
        controlador.getArrayDeObjetosSistema().add(objeto);

       //Borramos el objeto
        controlador.borrarObjeto(IdObjeto);

        // Capturamos los valores
        ArgumentCaptor<Objeto> argumentCaptor = ArgumentCaptor.forClass(Objeto.class);
        //Verificamos que se llame al metodo
        verify(mockConector).borrarObjetoDeBd(argumentCaptor.capture());
        
        //Verficamos que los valores son correctos
        Objeto capturedObjeto = argumentCaptor.getValue();
        assertEquals(IdObjeto, capturedObjeto.getIdObjeto());
        assertEquals("Espada", capturedObjeto.getNombreObjeto());
        assertEquals("Comun", capturedObjeto.getRareza());
        assertEquals("10.5", String.valueOf(capturedObjeto.getPrecio()));
        assertEquals("Es una espada", capturedObjeto.getDescripcion());
    }
        
   }


