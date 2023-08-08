package guardian;

import java.util.Arrays;

/**
 *
 * @author eduardo.radieske
 */
public enum Placas
{
    KGA9435(1), 
    XEF1C26(2), 
    DSD8G61(3),
    NEGADO(999);
    
    private int codVaga;
    
    Placas(int vaga)
    {
        codVaga = vaga;
    }
    
    public int getVaga()
    {
        return codVaga;
    }
    
    public static Placas validarPlaca(String strPlaca)
    {
        return Arrays.stream(values())
          .filter(placa -> placa.name().equalsIgnoreCase(strPlaca.trim()))
          .findFirst()
          .orElse(Placas.NEGADO);
    }
}
