package com.software2.figures;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SquareTest {

    @Test
    void calcularArea() {
        Square s = new Square(1.0);
        assertEquals(1.0, s.calculateArea(), 0.0001);
    }

    @Test
    void calcularPerimetro() {
        Square s = new Square(1.0);
        assertEquals(4.0, s.calculatePerimeter(), 0.0001);
    }
}
