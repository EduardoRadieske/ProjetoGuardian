package guardian;

import java.util.Optional;

/**
 *
 * @author eduardo.radieske
 */
public class Guardian
{
    
    private static String strPlaca;
    private static Raspberry rp;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {    
        rp = Raspberry.getInstance();
        
        while (true) 
        {
            strPlaca = "XEF1C26";

            //iniciarProcessamento();
            processarPlacaAutorizada();
            
            aguardar(1000, "main");
        }
    }
    
    private static void iniciarProcessamento() 
    {
        Imagem imgPlaca = Imagem.getInstance();
        imgPlaca.tirarFoto();
        
        Ocr ocrImg = Ocr.getInstance();
        
        Optional<String> placa = ocrImg.extrairPlaca(imgPlaca.getImagem());
   
        if (!placa.isPresent())
        {
            imgPlaca.aplicarPretoBranco();
            placa = ocrImg.extrairPlaca(imgPlaca.getImagem());
            
            if (placa.isPresent()) 
            {   
                strPlaca = placa.get();
                System.out.println("PLACA 2: " + strPlaca);
                System.out.println("Processou!");
            }
            
            return;
        }
        
        strPlaca = placa.get();
        System.out.println("PLACA: " + strPlaca);       
    }
    
    private static void processarPlacaAutorizada()
    {
        if (!"".equals(strPlaca))
        {
            Placas vagaPlaca = Placas.validarPlaca(strPlaca);
            vagaPlaca.enviarBinario(rp.getPinos());
            
            aguardar(5000, "processarPlacaAutorizada");
            
            rp.setPinosLow();
        }
    }
    
    private static void aguardar(int tempo, String metodo) 
    {
        try 
        {
            Thread.sleep(tempo);
        } 
        catch(InterruptedException e) 
        {
            System.err.println("Erro " + metodo + "(): " + e.getMessage());
        }
    }
}
