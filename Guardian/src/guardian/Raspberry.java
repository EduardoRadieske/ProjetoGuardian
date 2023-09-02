package guardian;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author eduardo.radieske
 */
public class Raspberry
{
    private static Raspberry INSTANCE;
    private GpioController gpio;
    private List<GpioPinDigitalOutput> pinos;
    
    private Raspberry()
    {
        gpio = GpioFactory.getInstance();
        
        pinos = new ArrayList<>();
        pinos.add(0, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "BIT1", PinState.LOW));
        pinos.add(1, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "BIT2", PinState.LOW));
        pinos.add(2, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "BIT3", PinState.LOW));
        pinos.add(3, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "FINALIZADO", PinState.LOW));
        
        System.out.println("Iniciou pinos!");
    }
    
    public static synchronized Raspberry getInstance() 
    {
        if (Objects.isNull(INSTANCE))
        {
            INSTANCE = new Raspberry();
            System.out.println("Criou novo Raspberry!");
        }
        
        return INSTANCE;
    }
    
    public List<GpioPinDigitalOutput> getPinos()
    {
        return pinos;
    }
    
    public void setPinosLow()
    {
        pinos.get(0).low();
        pinos.get(1).low();
        pinos.get(2).low();
        pinos.get(3).low();
    }
}
