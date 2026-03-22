package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GestorTasquesMockTest {

    private INotificador notificadorMock;
    private GestorTasques gestor;

    @BeforeEach
    void setUp() {
        notificadorMock = mock(INotificador.class);
        gestor = new GestorTasques(notificadorMock);
    }

    @Test
    void testAfegirTascaAmbNotificacioCorrecta() throws Exception {

        when(notificadorMock.notificar(anyString())).thenReturn(true);

        gestor.afegirTasca("Tasca Mock", null, null, null);

        verify(notificadorMock).notificar("Nova tasca creada: Tasca Mock");
        assertEquals(1, gestor.getNombreTasques());
    }

    @Test
    void testAfegirTascaQuanNotificacioFalla() {

        when(notificadorMock.notificar(anyString())).thenReturn(false);

        Exception e = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasca Mock", null, null, null);
        });

        assertEquals("No s'ha pogut notificar la creació de la tasca", e.getMessage());

        verify(notificadorMock).notificar("Nova tasca creada: Tasca Mock");

        assertEquals(1, gestor.getNombreTasques());
    }
}