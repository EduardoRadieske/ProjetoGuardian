package guardian;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
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
    private static Imagem INSTANCE;

    private final String CAMINHO_IMAGENS = "imagens";
    private File arquivoJpg;
    private File arquivoJpgFinal;
    
    private Webcam webcam;
    
    
    private Imagem()
    {
        //this.webcam = Webcam.getDefault();
        
        if (Objects.isNull(this.webcam))
        {   
            System.out.println("Nenhuma webcam encontrada.");
            return;
        }
        
        this.webcam.setViewSize(WebcamResolution.VGA.getSize());
    }
    
    public static synchronized Imagem getInstance() 
    {
        if (Objects.isNull(INSTANCE))
        {
            INSTANCE = new Imagem();
            System.out.println("Criou nova conexão com a câmera!");
        }
        
        return INSTANCE;
    }
    
    public void tirarFoto()
    {
        /*webcam.open();
        
        BufferedImage imagem = webcam.getImage();*/

        this.arquivoJpg = new File(this.CAMINHO_IMAGENS + "/imagem.jpg");
        
        /*if (this.arquivoJpg.exists())
        {
            this.arquivoJpg.delete();
        }*/
        
        this.arquivoJpgFinal = new File(this.CAMINHO_IMAGENS + "/imagem_final.jpg");
        
        if (this.arquivoJpgFinal.exists())
        {
            this.arquivoJpgFinal.delete();
        }
        
        /*try 
        {
            ImageIO.write(imagem, "jpg", this.arquivoJpg);
            System.out.println("Imagem Salva!");
        } 
        catch (IOException e) 
        {
            System.err.println("Erro ao obter a imagem: " + e.getMessage());
        }
        
        webcam.close();*/
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
