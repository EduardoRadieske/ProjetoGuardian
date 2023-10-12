# Projeto Guardian - Leitor de Placas para Raspberry Pi 3B+

## Descrição

O Projeto Guardian é um sistema desenvolvido em Java 8 para Raspberry Pi 3B+ que utiliza o Tesseract OCR para a leitura e processamento de placas de veículos a partir de imagens capturadas pela câmera.

## Requisitos

- Raspberry Pi 3B+ com sistema operacional Raspbian instalado.
- Java Runtime Environment (JRE) instalado no Raspberry Pi.
- Câmera configurada e funcional no Raspberry Pi.
- Possuir as linguagens OCR devidamente parametrizadas no diretório "./ocr"

## Estrutura de Diretórios


/projetoguardian
    /imagens
        # Pasta para armazenar as imagens capturadas pela câmera
    /lib
        # Pasta para armazenar bibliotecas e dependências
    /ocr 
	# Pasta para armanezar os arquivos de linguagem do OCR
    Guardian.jar


## Uso

1. Para executar o projeto utilize: java -Xmx512m -jar Guardian.jar 
2. O Projeto Guardian irá capturar da câmera, processar automaticamente as imagens e exibir os resultados na saída digital (GPIO) do raspberry.

## Notas Adicionais

- Caso haja necessidade de adicionar novas bibliotecas ou dependências, coloque-as na pasta `lib`.

---

*Importante:* Certifique-se de ter as configurações do ambiente e as dependências necessárias antes de executar o Projeto Guardian.

Para mais informações, visite o [repositório do Projeto Guardian](https://github.com/EduardoRadieske/ProjetoGuardian).