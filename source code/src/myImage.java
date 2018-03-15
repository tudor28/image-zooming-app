import java.io.File;//lucrul cu fisiere
import java.io.IOException;//pentru tratarea erorilor
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;//salvarea imaginii intr-un buffer, pentru procesare
import javax.imageio.ImageIO;//citire si scriere de imagini
import java.util.Scanner;//citire de la tastatura

public class myImage extends imageZooming{
	//atributele de identificare ale imaginii
	private int width;
	private int height;
	byte [] header;
	byte [] body;
	private int n;//rata de zoom a imaginii
	public myImage(){}
	public myImage(int width, int height, int n){
		this.width=width;
		this.height=height;
		this.n=n;
	}
	public myImage(int height, int width, byte[] header, byte[] body) {
		this.height = height;
        this.width = width;
        this.header = header;
        this.body = body;
	}
	//getters
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public int getRate(){
		return this.n;
	}
	//setters
	public void setWidth(int w){
		this.width=w;
	}
	public void setHeight(int h){
		this.height=h;
	}
	public void setRate(int n){
		this.n=n;
	}
	public static void main(String args[])throws IOException{
		long startTime = System.currentTimeMillis();
		myImage imag = new myImage();
		imag.Read();
		BufferedImage modifiedImage;
		modifiedImage = imag.imageResize(img,imag.getRate());
		imag.Write(modifiedImage);
		long endTime = System.currentTimeMillis();
		long execTime = endTime - startTime;
        System.out.println("Timpul de executie pentru main: " + execTime + " milisecunde");
	}
	//citirea imaginii pixel cu pixel intr-un array de bytes
	public void Read() throws IOException{
		long startTime = System.currentTimeMillis();
		File f=null;
		Scanner scan = new Scanner(System.in);
		/*System.out.println("Introduceti latimea imaginii: ");
		setWidth(scan.nextInt());
		System.out.println("Introduceti inaltimea imaginii: ");
		setHeight(scan.nextInt());*/
		System.out.println("Introduceti calea completa a fisierului .bmp: ");
		path=scan.next();
		System.out.println("Introduceti rata de zoom pentru imagine: ");
		setRate(scan.nextInt());
		f = new File(path);
		while ( !f.exists() ){
            System.out.println("Imaginea nu a fost gasita! Mai incercati!");
            path=scan.next();
            f = new File(path);
        }
		byte[] b = Files.readAllBytes(Paths.get(path));
        byte[] header = new byte[54];
        for ( int i = 0 ; i < 54 ; i ++ )
            header[i] = b[i];
        
        // Formarea dimensiunilor folosind cei 8 biti din headerul imaginii
        width = ((header[21]&0xFF)<<24)|((header[20]&0xFF)<<16)|((header[19]&0xFF)<<8)|((header[18]&0xFF));
        height= ((header[25]&0xFF)<<24)|((header[24]&0xFF)<<16)|((header[23]&0xFF)<<8)|((header[22]&0xFF));
        
        // Pixelii se retin in body
        byte[] body = new byte[3*width*height];
        for ( int i = 54 ; i < 3*width*height ; i++ )
            body[i-54] = b[i];
        
       // myImage Imag = new myImage(height, width, header, body); 
		try{
			f = new File(path);
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			img = ImageIO.read(f);
			System.out.println("Reading complete.");
			}catch(IOException e){
			System.out.println("Error: "+e);
			}
		
		long endTime = System.currentTimeMillis();
		long execTime = endTime - startTime;
        System.out.println("Timpul de executie pentru citire: " + execTime + " milisecunde");
	}
	public myImage imageResizes(myImage image, int n){
		long startTime = System.currentTimeMillis();
        byte[] header = new byte[54];
        header[21] = (byte) (width>>24); 
        header[20] = (byte) (width>>16); 
        header[19] = (byte) (width>>8); 
        header[18] = (byte) (width); 
        header[25] = (byte) (height>>24); 
        header[24] = (byte) (height>>16); 
        header[23] = (byte) (height>>8); 
        header[22] = (byte) (height);
       
        byte[] body = new byte[3*n*width*height];
        byte[] originalbody = body;
        for ( int i = 0 ; i < 3* height * width ; i ++ ) 
            body[i] = originalbody[i];
        for (int i=0; i < height; ++i){
        	for (int j=0; j < width; ++j)
        		for (int k=j*n;k<j*(n+1);k++)
            		body[n*width*i+k]=originalbody[i*width+j];
        }
        byte[] body2 = new byte[3*n*width*n*height];
        	for(int i=0;i<height;i++)
            	for(int c=0;c<n;c++)
            		for(int j=0;j<width*n;j++)
            			body2[c*width*n+j]=body[i*width*n+j];
        height=height*n;
        width=width*n;       
        myImage modifiedImage = new myImage(height,width, header, body);
        long endTime = System.currentTimeMillis();
        long execTime = endTime - startTime;
        System.out.println("Timpul de executie pentru functia de resize: " + execTime + " milisecunde");
		return modifiedImage;
}
	public void Write(BufferedImage modifiedImage){
		long startTime = System.currentTimeMillis();
		Scanner scan = new Scanner(System.in);
		System.out.println("Introduceti calea completa unde va fi salvat fisierul .bmp prelucrat: ");
		path=scan.next();
		File f;
		scan.close();
		try{
		      f = new File(path);
		      ImageIO.write(modifiedImage, "bmp", f);
		      System.out.println("Writing complete.");
		      }catch(IOException e){
		      System.out.println("Error: "+e);
		}
		long endTime = System.currentTimeMillis();
		long execTime = endTime - startTime;
        System.out.println("Timpul de executie pentru scriere: " + execTime + " milisecunde");
	}
	
	
	
	
	public BufferedImage imageResize(BufferedImage image, int n){
		long startTime = System.currentTimeMillis();
        int w = n * image.getWidth();
        int h = n * image.getHeight();
        BufferedImage modifiedImage = new BufferedImage(w, h, image.getType());
        for (int y=0; y < h; ++y)
            for (int x=0; x < w; ++x)
                modifiedImage.setRGB(x, y, image.getRGB(x/n, y/n));
		long endTime = System.currentTimeMillis();
		long execTime = endTime - startTime;
        System.out.println("Timpul de executie pentru functia de resize: " + execTime + " milisecunde");
		return modifiedImage;
	}

}
