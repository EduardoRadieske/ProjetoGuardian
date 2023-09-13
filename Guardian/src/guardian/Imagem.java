package guardian;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;

/**
 *
 * @author eduardo.radieske
 */
public class Imagem
{   
    private final String CAMINHO_IMAGENS = "projetoguardian/imagens";
    private File arquivoJpg;
    private File arquivoJpgFinal;
    
    private Future<File> arquivoSuavizado;
    private ExecutorService executor;
    
    public void tirarFoto()
    { 
        try 
        {
            Process process = Runtime.getRuntime().exec("raspistill -o " + this.CAMINHO_IMAGENS + "/imagem.jpg -t 2000");
            process.waitFor();
            System.out.println("Foto tirada com sucesso.");
        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
        }
        
        this.arquivoJpg = new File(this.CAMINHO_IMAGENS + "/imagem.jpg");
        
        this.arquivoJpgFinal = new File(this.CAMINHO_IMAGENS + "/imagem_final.jpg");
        
        if (this.arquivoJpgFinal.exists())
        {
            this.arquivoJpgFinal.delete();
        } 
    }
    
    public void aplicarPretoBranco()
    {    
        try 
        {
            if (this.arquivoJpgFinal.exists())
            {
                this.arquivoJpgFinal.delete();
            }
            
            BufferedImage image = ImageIO.read(this.arquivoJpg);
            
            // Cria uma imagem em tons de cinza usando a operação ColorConvertOp
            BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            ColorConvertOp colorConvert = new ColorConvertOp(null);
            colorConvert.filter(image, grayImage);

            ImageIO.write(grayImage, "jpg", this.arquivoJpgFinal);
        } 
        catch (IOException e) 
        {
            System.err.println("Erro ao aplicar preto e branco na imagem: " + e.getMessage());
        }
    }
    
    public File getImagem()
    {
        if (Objects.nonNull(this.arquivoJpgFinal))
        {
            if (this.arquivoJpgFinal.exists())
            {
                return this.arquivoJpgFinal;
            }
        } 
        
        return this.arquivoJpg;
    } 
    
    public File getImagemSuavizada()
    {
        File arquivoProcessado = getImagem();
        
        try
        {
            arquivoProcessado = this.arquivoSuavizado.get();
        } 
        catch (InterruptedException | ExecutionException ex)
        {
            System.err.println("A operação suavizar imagem foi interrompida. Detalhes: " + ex.getMessage());
        }
        finally
        {
           this.executor.shutdown();
        }
        
        return arquivoProcessado;
    }
    
    public void suavizarImagem()
    {
        if (!this.arquivoJpgFinal.exists())
        {
            aplicarPretoBranco();
        }
        
        this.executor = Executors.newSingleThreadExecutor(); 
        
        this.arquivoSuavizado = this.executor.submit(() ->
        {
            File arquivoProcessado = new File(this.CAMINHO_IMAGENS + "/imagem_suavizada.jpg");
            
            if (arquivoProcessado.exists())
            {
                arquivoProcessado.delete();
            }
            
            try 
            {
                BufferedImage imagem = ImageIO.read(this.arquivoJpgFinal);

                // Aplica um filtro de média para reduzir o ruído
                BufferedImage imagemSuavizada = aplicarFiltroMedia(imagem);

                ImageIO.write(imagemSuavizada, "jpg", arquivoProcessado);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            
            return arquivoProcessado;
        }); 
    }
    
    private BufferedImage aplicarFiltroMedia(BufferedImage imagem) {
        int largura = imagem.getWidth();
        int altura = imagem.getHeight();
        BufferedImage resultado = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int corMedia = calcularMediaVizinhanca(imagem, x, y);
                resultado.setRGB(x, y, corMedia);
            }
        }

        return resultado;
    }

    private int calcularMediaVizinhanca(BufferedImage imagem, int x, int y) {
        int somaR = 0, somaG = 0, somaB = 0;
        int totalPixels = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int novoX = Math.min(Math.max(x + i, 0), imagem.getWidth() - 1);
                int novoY = Math.min(Math.max(y + j, 0), imagem.getHeight() - 1);

                int cor = imagem.getRGB(novoX, novoY);
                int r = (cor >> 16) & 0xFF;
                int g = (cor >> 8) & 0xFF;
                int b = cor & 0xFF;

                somaR += r;
                somaG += g;
                somaB += b;
                totalPixels++;
            }
        }

        int mediaR = somaR / totalPixels;
        int mediaG = somaG / totalPixels;
        int mediaB = somaB / totalPixels;

        return (mediaR << 16) | (mediaG << 8) | mediaB;
    }
}
