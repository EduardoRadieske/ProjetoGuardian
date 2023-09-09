package guardian;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 *
 * @author eduardo.radieske
 */
public class Imagem
{   
    private final String CAMINHO_IMAGENS = "imagens";
    private File arquivoJpg;
    private File arquivoJpgFinal;
    
    public void tirarFoto()
    { 
        try 
        {
            Process process = Runtime.getRuntime().exec("raspistill -o " + this.CAMINHO_IMAGENS + "/imagem.jpg -t 1000");
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
}
