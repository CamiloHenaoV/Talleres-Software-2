package com.software2.figures;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TriangleTest {

    @Test
    void calcularArea() {
        Triangle t = new Triangle(3.0, 4.0);
        assertEquals(6.0, t.calculateArea(), 0.0001);
    }

    @Test
    void calcularPerimetro() {
        Triangle t = new Triangle(3.0, 4.0);
        assertEquals(12.0, t.calculatePerimeter(), 0.0001);
    }
}
