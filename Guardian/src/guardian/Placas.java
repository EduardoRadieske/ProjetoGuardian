package guardian;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author eduardo.radieske
 */
public enum Placas
{   
    AUTORIZADO {
        private int[] binaryArray = new int[3];
        
        @Override
        public void enviarBinario(List<GpioPinDigitalOutput> pinos) {

            for (int i = 0; i < 3; i++)
            {
                if (binaryArray[i] > 0) 
                {
                    System.out.println(" PINO: " + pinos.get(i).getName());
                    pinos.get(i).high();
                }
                else
                {
                    pinos.get(i).low();
                }
                System.out.println("I: " + binaryArray[i]);
            }
            
            pinos.get(3).high();
        }
        
        @Override
        public void setBinaryArray(int vaga)
        {
            for (int i = 2; i >= 0; i--) {
                binaryArray[2 - i] = (vaga >> i) & 1;
            }
        }
    },
    NEGADO {
        @Override
        public void enviarBinario(List<GpioPinDigitalOutput> pinos) {
            System.out.println("Negado");
            pinos.get(0).high();
            pinos.get(1).high();
            pinos.get(2).high();
            pinos.get(3).high();
        }
        
        @Override
        public void setBinaryArray(int vaga)
        {}
    };
    
    private static final HashMap<String, Integer> placas = new HashMap<>();
   
    static
    {
        placas.put("KMA9435", 1);
        placas.put("XEF1C26", 2);
        placas.put("DSD8F61", 3);
    }
    
    public static Placas validarPlaca(String strPlaca)
    {
        if (placas.containsKey(strPlaca.toUpperCase().trim()))
        {
            Placas iPlaca = AUTORIZADO;
            iPlaca.setBinaryArray(placas.get(strPlaca.toUpperCase().trim()));
            
            return iPlaca;
        }
        
        return NEGADO;
    }
    
    public abstract void enviarBinario(List<GpioPinDigitalOutput> pinos);
    public abstract void setBinaryArray(int vaga);
}
