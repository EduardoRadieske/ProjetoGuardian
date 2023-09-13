package guardian;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
        this.tesseract.setDatapath("./projetoguardian/ocr");
        this.tesseract.setVariable("user_defined_timeout", "5000");
        this.tesseract.setLanguage("eng");
        System.out.println("Criou novo Processamento!");
    }
    
    public static synchronized Ocr getInstance() 
    {
        if (Objects.isNull(INSTANCE))
        {
            INSTANCE = new Ocr();
        }
        
        return INSTANCE;
    }
    
    public Optional<String> extrairPlaca(File imagem)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Optional<String> resultadoPlaca = Optional.empty();
        
        Future<Optional<String>> future = executor.submit(() ->
        {
            Optional<String> placa = Optional.empty();

            try
            {
                // Convertendo a imagem em texto usando Tesseract
                String text = tesseract.doOCR(imagem);
                
                if (Thread.interrupted())
                {
                    return placa;
                }
                
                text = text.replaceAll("[^a-zA-Z0-9 ]", "").toUpperCase();
                
                // Extraindo os resíduos de leitura incompleta.
                Matcher matcher = regex.matcher(text.trim());
                System.out.println("DEBUG OCR: " + text.trim());

                if (matcher.find())
                {
                    placa = Optional.of(matcher.group().toUpperCase());
                }
            } 
            catch (TesseractException e) 
            {
                e.printStackTrace();
            }

            return placa;
        });
            
        try 
        {
            resultadoPlaca = future.get(5, TimeUnit.SECONDS); // Timeout 5 segundos
        } 
        catch (TimeoutException e) 
        {
            System.err.println("A operação OCR excedeu o tempo limite.");
            future.cancel(true); // Cancela a operação
        } 
        catch (InterruptedException | ExecutionException ex) 
        {
            System.err.println("A operação OCR foi interrompida. Detalhes: " + ex.getMessage());
        } 
        finally 
        {
            if (!executor.isTerminated())
            {
                //Necessário para evitar bloqueio da execução por parte da JVM no Raspberry PI 3 B+
                executor.shutdown();
                try 
                {
                    System.out.println("Tentativa 1 de encerrar OCR!");
                    if (!executor.awaitTermination(30, TimeUnit.SECONDS)) 
                    {
                        System.out.println("Tentativa 2 de encerrar OCR!");

                        executor.shutdownNow(); // Cancela tarefas em execução se a espera exceder 30 segundos
                    }
                } 
                catch (InterruptedException e) 
                {
                    executor.shutdownNow(); // Interrompe se houver interrupção
                }
            }
        }
        
        return resultadoPlaca;
    }
}
