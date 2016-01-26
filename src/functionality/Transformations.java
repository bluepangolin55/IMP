package functionality;

import java.awt.image.BufferedImage;

import org.jtransforms.fft.FloatFFT_2D;

import main.IMP;
import sun.security.util.Length;


public class Transformations {

	static BufferedImage DFT(BufferedImage image){
		BufferedImage fourier = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		double[] drow = new double[w*h];
		
		
		return fourier;
	}

	public static float[][] fourier(BufferedImage image) {
		System.out.println("--------------------------------");
		int w = image.getWidth();
		int h = image.getHeight();
		int w2p = 1;
		int h2p = 1;
		while(w2p < w){
			w2p *= 2;
		}
		while(h2p < h){
			h2p *= 2;
		}
		int mask = 0x000000ff;
		float[][] data = new float[w2p][h2p];
		for(int x=0; x<w; x++){
			for(int y=0; y<h; y++){
				int a = image.getRGB(x, y);
				a = (a & mask) + (a>>8 & mask) + (a>>16 & mask);
				data[x][y] = a/3;
			}
		}
		
		FloatFFT_2D fft = new FloatFFT_2D(w2p,h2p);
		fft.realForward(data);
		System.out.println("3:" + data[100][100]);
		return data;
	}
	
	BufferedImage inverse_fourier(float[][] data, BufferedImage image){
		int w = image.getWidth();
		int h = image.getHeight();
		FloatFFT_2D fft = new FloatFFT_2D(data.length,data[0].length);
		fft.realInverse(data, true);
		for(int x=0; x<w; x++){
			for(int y=0; y<h; y++){
//				IMP.preview_image.setRGB(x, y, (int) ((data1[i]/norm*128)+127));
//				IMP.preview_image.setRGB(x, y, (int) ((data[x][y]/norm)*128) + 127);
				IMP.preview_image.setRGB(x, y, (int) data[x][y]);
//				i++;
			}
//				System.out.println(((data[x][100]/norm)*128));
				//System.out.println((int) (data1[i]/norm*255));
//				System.out.println((int)((data[x][100]/norm)*128));
		}
		return image;
	}
	
	
	
	public static float[][] circshift(float[][] data){
		int h = data.length;
		int w = data[0].length;
		int xshift = w/2;
		int yshift = h/2;
		float[][] result = new float[w][h];
		for (int x = 0; x < w; x++) {
			int ii = (x + xshift) % w;
			for (int y = 0; y < h; y++) {
				int jj = (y + yshift) % h;
				result[ii][jj] = data[x][y];
			}
		}
		return result;
	}
}