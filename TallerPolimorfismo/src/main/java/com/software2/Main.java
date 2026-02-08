package com.software2;

import com.software2.figures.Figure;
import com.software2.figures.Circle;
import com.software2.figures.Square;
import com.software2.figures.Triangle;
import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Figure fig1 = new Circle(1.0);
        Figure fig2 = new Square(2.3);
        Figure fig3 = new Triangle(4.2, 4.5);

        List<Figure> figures = new ArrayList<>();
        figures.add(fig1);
        figures.add(fig2);
        figures.add(fig3);

        for (Figure fig : figures) {
            System.out.println("Area: " + fig.calculateArea());
            System.out.println("Perimeter: " + fig.calculatePerimeter());
            
        }
    }
}