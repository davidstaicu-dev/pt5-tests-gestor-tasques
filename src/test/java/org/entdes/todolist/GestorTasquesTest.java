package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GestorTasquesTest {

    private GestorTasques gestor;

    class NotificadorStub implements INotificador {
        public boolean notificar(String missatge) {
            return true;
        }
    }

    @BeforeEach
    void setUp() {
        gestor = new GestorTasques(new NotificadorStub());
    }

    @Test
    void testAfegirTascaCorrectament() throws Exception {

        int id = gestor.afegirTasca(
                "Comprar pa",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);

        assertTrue(id > 0);
        assertEquals(1, gestor.getNombreTasques());
    }

    @Test
    void testAfegirTascaAmbDataIniciPosterior() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(
                    "Comprar pa",
                    LocalDate.now().plusDays(3),
                    LocalDate.now().plusDays(1),
                    3);
        });
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }

    @Test
    void testAfegirTascaAmbDataIniciAnterior() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(
                    "Comprar pa",
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    3);
        });
        assertEquals("La data d'inici no pot ser anterior a la data actual.", exception.getMessage());
    }

    @Test
    void testAfegirTascaAmbDescripcioBuida() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(
                    "",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(3),
                    3);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }

    @Test
    void testEliminarTascaCorrectament() throws Exception {
        int id = gestor.afegirTasca(
                "Eliminar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        gestor.eliminarTasca(id);
        assertEquals(0, gestor.getNombreTasques());

        Exception e = assertThrows(Exception.class, () -> {
            gestor.obtenirTasca(id);
        });

        assertEquals("La tasca no existeix", e.getMessage());
    }

    @Test
    void testEliminarTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.eliminarTasca(999);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    void testMarcarCompletadaCorrectament() throws Exception {
        int id = gestor.afegirTasca(
                "Marcar com completada",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        gestor.marcarCompletada(id);
        assertTrue(gestor.obtenirTasca(id).isCompletada());
    }

    @Test
    void testMarcarCompletadaTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.marcarCompletada(999);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    void testModificarTascaCorrectament() throws Exception {
        int id = gestor.afegirTasca(
                "Modificar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        gestor.modificarTasca(id, "Tasca modificada", true, LocalDate.now().plusDays(3), LocalDate.now().plusDays(4),
                4);
        Tasca tasca = gestor.obtenirTasca(id);
        assertEquals("Tasca modificada", tasca.getDescripcio());
        assertTrue(tasca.isCompletada());
        assertEquals(LocalDate.now().plusDays(3), tasca.getDataInici());
        assertEquals(LocalDate.now().plusDays(4), tasca.getDataFiPrevista());
        assertEquals(4, tasca.getPrioritat());
    }

    @Test
    void testModificarTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(999, "Tasca modificada", true, LocalDate.now().plusDays(3),
                    LocalDate.now().plusDays(4), 4);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    void testModificarTascaAmbDescripcioBuida() throws Exception {
        int id = gestor.afegirTasca(
                "Modificar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "", true, LocalDate.now().plusDays(3), LocalDate.now().plusDays(4), 4);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }

    @Test
    void testModificarTascaAmbDataIniciPosterior() throws Exception {
        int id = gestor.afegirTasca(
                "Modificar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Tasca modificada", true, LocalDate.now().plusDays(4),
                    LocalDate.now().plusDays(3), 4);
        });
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }

    @Test
    void testModificarTascaAmbDataIniciAnterior() throws Exception {
        int id = gestor.afegirTasca(
                "Modificar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Tasca modificada", true, LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(3), 4);
        });
        assertEquals("La data d'inici no pot ser anterior a la data actual.", exception.getMessage());
    }

    @Test
    void testModificarTascaAmbPrioritatInvalida() throws Exception {
        int id = gestor.afegirTasca(
                "Modificar tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Tasca modificada", true, LocalDate.now().plusDays(3),
                    LocalDate.now().plusDays(4), 6);
        });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    void testObtenirTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.obtenirTasca(999);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    void testObtenirTascaCorrectament() throws Exception {
        int id = gestor.afegirTasca(
                "Obtenir tasca",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                3);
        Tasca tasca = gestor.obtenirTasca(id);
        assertEquals("Obtenir tasca", tasca.getDescripcio());
        assertFalse(tasca.isCompletada());
        assertEquals(LocalDate.now().plusDays(1), tasca.getDataInici());
        assertEquals(LocalDate.now().plusDays(3), tasca.getDataFiPrevista());
        assertEquals(3, tasca.getPrioritat());
    }

    @Test
    void testModificarTascaAmbDescripcioDuplicada() throws Exception {

        gestor.afegirTasca("Tasca 1", null, null, null);
        int id2 = gestor.afegirTasca("Tasca 2", null, null, null);

        Exception e = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id2, "Tasca 1", true, null, null, 3);
        });

        assertEquals("Ja existeix una altra tasca amb aquesta descripció.", e.getMessage());
    }

    @Test
    void testAfegirTascaDuplicada() throws Exception {

        gestor.afegirTasca("Comprar pa", null, null, null);

        Exception e = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("comprar pa", null, null, null);
        });

        assertEquals("La tasca ja existeix", e.getMessage());
    }

    @Test
    void testFiltrarPerDescripcio() throws Exception {

        gestor.afegirTasca("Comprar pa", null, null, null);
        gestor.afegirTasca("Estudiar", null, null, null);

        var resultat = gestor.llistarTasquesPerDescripcio("pa");

        assertEquals(1, resultat.size());
    }

    @Test
    void testFiltrarPerDescripcioSenseResultats() throws Exception {

        gestor.afegirTasca("Comprar pa", null, null, null);

        var resultat = gestor.llistarTasquesPerDescripcio("xyz");

        assertEquals(0, resultat.size());
    }

    @Test
    void testFiltrarPerComplecio() throws Exception {

        int id = gestor.afegirTasca("Estudiar", null, null, null);

        gestor.marcarCompletada(id);

        var resultat = gestor.llistarTasquesPerComplecio(true);

        assertEquals(1, resultat.size());
    }

    @Test
    void testFiltrarPerComplecioFalse() throws Exception {

        gestor.afegirTasca("T1", null, null, null);
        int id = gestor.afegirTasca("T2", null, null, null);

        gestor.marcarCompletada(id);

        var resultat = gestor.llistarTasquesPerComplecio(false);

        assertEquals(0, resultat.size());
    }

    @Test
    void testDesmarcarTascaEliminaDataFiReal() throws Exception {

        int id = gestor.afegirTasca("Test", null, null, null);

        gestor.marcarCompletada(id);

        gestor.modificarTasca(id, "Test", false, null, null, 3);

        Tasca t = gestor.obtenirTasca(id);

        assertFalse(t.isCompletada());
        assertNull(t.getDataFiReal());
    }

    @Test
    void testModificarTascaAmbCompletadaNull() throws Exception {

        int id = gestor.afegirTasca("Test", null, null, null);

        gestor.modificarTasca(id, "Test", null, null, null, 3);

        Tasca t = gestor.obtenirTasca(id);

        assertFalse(t.isCompletada());
    }

    @Test
    void testLlistarTasques() throws Exception {

        gestor.afegirTasca("T1", null, null, null);
        gestor.afegirTasca("T2", null, null, null);

        assertEquals(2, gestor.llistarTasques().size());
    }

    @Test
    void testModificarTascaIdNoExisteixAmbAltresTasques() throws Exception {

        gestor.afegirTasca("T1", null, null, null);

        Exception e = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(999, "Nova", true, null, null, 3);
        });

        assertEquals("La tasca no existeix", e.getMessage());
    }

    @Test
    void testAfegirTascaQuanNotificadorFalla() {

        GestorTasques gestorFail = new GestorTasques(new INotificador() {
            public boolean notificar(String missatge) {
                return false;
            }
        });

        Exception e = assertThrows(Exception.class, () -> {
            gestorFail.afegirTasca("Test", null, null, null);
        });

        assertEquals("No s'ha pogut notificar la creació de la tasca", e.getMessage());
    }

    @Test
    void testGuardarICarregarTasques() throws Exception {

        gestor.afegirTasca("Tasca 1", null, null, null);

        gestor.guardar();

        GestorTasques nouGestor = new GestorTasques(new NotificadorStub());
        nouGestor.carregar();

        assertEquals(1, nouGestor.getNombreTasques());
    }
}