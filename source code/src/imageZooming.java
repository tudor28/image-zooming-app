import java.io.IOException;

public abstract class imageZooming extends imageProcessing {
	protected static String path;//calea de unde citim imaginea care va fi procesata si unde scriem imaginea care a fost deja procesata
	protected abstract void setWidth(int w);
	protected abstract void setHeight(int h);
	protected abstract void Read() throws IOException;
	protected abstract myImage imageResizes(myImage image, int n);
}