package guardian;

import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author eduardo.radieske
 */
public class Ocr
{
    private static Ocr INSTANCE;
    
    private Tesseract tesseract;
    
    private Pattern regex = Pattern.compile("[a-zA-Z]{3}[0-9][a-zA-Z0-9]{3}");
    
    private Ocr()
    {
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath("D:\\OCR");
    }
    
    public static synchronized Ocr getInstance() 
    {
        if (INSTANCE == null)
        {
            INSTANCE = new Ocr();
            System.out.println("Criou novo Processamento!");
        }
        
        return INSTANCE;
    }
    
    public Optional<String> extrairPlaca(File imagem)
    {
        Optional<String> placa = Optional.empty();
        
        try 
        {
            // Convertendo a imagem em texto usando Tesseract
            String text = this.tesseract.doOCR(imagem);

            // Extraindo os resíduos de leitura incompleta.
            Matcher matcher = regex.matcher(text.trim());
            //System.out.println("DEBUG: " + text.trim());
            
            if (matcher.find())
            {
                placa = Optional.of(matcher.group().toUpperCase());
            }
        } 
        catch (TesseractException e) 
        {
            System.err.println("Erro ao processar a imagem: " + e.getMessage());
        }
        
        return placa;
    }
}
