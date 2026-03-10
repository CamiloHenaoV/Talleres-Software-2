package co.unicauca.microkernel.plugins.argentina;

import co.unicauca.microkernel.common.entities.Delivery;
import co.unicauca.microkernel.common.entities.Product;
import co.unicauca.microkernel.common.interfaces.IDeliveryPlugin;
/**
 * Plugin para envios a Argentina
 * @author grupo SOFT2
 */
public class ArgentinaDeliveryPlugin implements IDeliveryPlugin {

    /**
     * Calcular el costo de envío de un producto de la tienda enviado dentro de
     * Argentina.
     *
     */
    @Override
    public double calculateCost(Delivery delivery) {
        double weight = delivery.getProduct().getWeight();
        double distance = delivery.getDistance();

        // Tarifa base de $3, más $0.5 por kg y $0.1 por km
        return 3 + (weight * 0.5) + (distance * 0.5);
    }
}
