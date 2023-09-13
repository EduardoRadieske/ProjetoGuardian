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
    
    private static int tentativas;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {    
        rp = Raspberry.getInstance();
        tentativas = 0;
        
        while (true) 
        {
            strPlaca = "";

            iniciarProcessamento();
            processarPlacaAutorizada();
            
            // Validação para deixar 5 segundos parado caso não tenha resposta na imagem.
            if (tentativas > 3)
            {
                aguardar(5000, "main");
                tentativas = 0;
            } 
            else 
            {
                aguardar(1000, "main");
            }
        }
    }
    
    private static void iniciarProcessamento() 
    {
        Imagem imgPlaca = new Imagem();
        imgPlaca.tirarFoto();
        imgPlaca.aplicarPretoBranco();
        imgPlaca.suavizarImagem();
           
        Ocr ocrImg = Ocr.getInstance();
        
        Optional<String> placa = ocrImg.extrairPlaca(imgPlaca.getImagem());

        if (!placa.isPresent())
        {
            System.out.println("SEGUNDA TENTANTIVA LEITURA!");
            placa = ocrImg.extrairPlaca(imgPlaca.getImagemSuavizada());
            
            if (placa.isPresent()) 
            {   
                strPlaca = placa.get();
                tentativas = 0;
                System.out.println("PLACA 2: " + strPlaca);
            } 
            else 
            {
                tentativas++;
            }
            
            return;
        }
        
        tentativas = 0;
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
