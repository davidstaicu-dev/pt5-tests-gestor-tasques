package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TascaTest {
    @Test
    void testCrearTascaAmbDescripcioCorrecta() {
        Tasca tasca = new Tasca("Comprar pa");

        assertEquals("Comprar pa", tasca.getDescripcio());
        assertFalse(tasca.isCompletada());
    }

    @Test
    void testMarcarTascaComCompletada() {
        Tasca tasca = new Tasca("Estudiar");

        tasca.setCompletada(true);

        assertTrue(tasca.isCompletada());
    }

    @Test
    void testModificarDescripcioFunciona() {
        Tasca tasca = new Tasca("Antiga");

        tasca.setDescripcio("Nova");

        assertEquals("Nova", tasca.getDescripcio());
    }

    @Test
    void testAssignarDataInici() {
        Tasca tasca = new Tasca("Prova");

        LocalDate avui = LocalDate.now();
        tasca.setDataInici(avui);

        assertEquals(avui, tasca.getDataInici());
    }

    @Test
    void testAssignarDataFiPrevista() {
        Tasca tasca = new Tasca("Prova");

        LocalDate data = LocalDate.now().plusDays(5);
        tasca.setDataFiPrevista(data);

        assertEquals(data, tasca.getDataFiPrevista());
    }

    @Test
    void testAssignarDataFiReal() {
        Tasca tasca = new Tasca("Prova");

        LocalDate data = LocalDate.now();
        tasca.setDataFiReal(data);

        assertEquals(data, tasca.getDataFiReal());
    }

    @Test
    void testAssignarPrioritat() {
        Tasca tasca = new Tasca("Prova");

        tasca.setPrioritat(3);

        assertEquals(3, tasca.getPrioritat());
    }

    @Test
    void testToStringMostraPendent() {
        Tasca tasca = new Tasca("Comprar pa");

        String resultat = tasca.toString();

        assertTrue(resultat.contains("Pendent"));
    }

    @Test
    void testToStringMostraCompletada() {
        Tasca tasca = new Tasca("Comprar pa");

        tasca.setCompletada(true);

        String resultat = tasca.toString();

        assertTrue(resultat.contains("Completada"));
    }

    @Test
    void testActualitzarIdCounterModifica() {
        Tasca.actualitzarIdCounter(100);

        Tasca tasca = new Tasca("Nova");

        assertTrue(tasca.getId() > 100);
    }

    @Test
    void testActualitzarIdCounterNoModifica() {
        Tasca.actualitzarIdCounter(50);

        Tasca tasca = new Tasca("Nova");

        assertTrue(tasca.getId() > 50);
    }
}