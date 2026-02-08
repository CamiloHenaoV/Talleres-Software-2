package com.software2.figures;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CircleTest {

    @Test
    void calcularArea() {
        Circle c = new Circle(1.0);
        assertEquals(Math.PI, c.calculateArea(), 0.0001);
    }

    @Test
    void calcularPerimetro() {
        Circle c = new Circle(1.0);
        assertEquals(2 * Math.PI, c.calculatePerimeter(), 0.0001);
    }
}