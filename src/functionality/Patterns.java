package functionality;

public class Patterns {

	
	public static void Caro(int x1,int y1,int x2,int y2,int h,int b,int[] c1,int[] c2){
		
		int ny=(y2-y1)/h;
		int nyc=1;								//ny-counter
		int y=y1;
		while (nyc<ny+2){						//y-Schlaufe
			for (;y<y1+nyc*h;y++){			//y-color1
				int nx=(x2-x1)/b;
				int nxc=1;
				int x=x1;
				while (nxc<nx+2){				//x-Schlaufe
					for (;x<x1+nxc*b;x++){
						file.Input.image_data[x][y][0]=c1[0];
						file.Input.image_data[x][y][1]=c1[1];
						file.Input.image_data[x][y][2]=c1[2];
					}
					nxc++;
					for (;x<x1+nxc*b;x++){
						file.Input.image_data[x][y][0]=c2[0];
						file.Input.image_data[x][y][1]=c2[1];
						file.Input.image_data[x][y][2]=c2[2];
					}
					nxc++;
				}
			}
			nyc++;
			for (;y<y1+nyc*h;y++){			//y-color2
				int nx=(x2-x1)/b;
				int nxc=1;
				int x=x1;
				while (nxc<nx+2){				//x-Schlaufe
					for (;x<x1+nxc*b;x++){
						file.Input.image_data[x][y][0]=c2[0];
						file.Input.image_data[x][y][1]=c2[1];
						file.Input.image_data[x][y][2]=c2[2];
					}
					nxc++;
					for (;x<x1+nxc*b;x++){
						file.Input.image_data[x][y][0]=c1[0];
						file.Input.image_data[x][y][1]=c1[1];
						file.Input.image_data[x][y][2]=c1[2];
					}
					nxc++;
				}
			}
			nyc++;
		}
	}
	
	
	public static void triangle(int x1,int y1,int x2,int y2,int h,int b,int m,int[] c1, int[] c2){
		//--ganze Fläche mit c1 füllen......................
		for(int y=y1;y<y2+1;y++){
			for(int x=x1;x<x2+1;x++){
				file.Input.image_data[x][y][0]=c1[0];
				file.Input.image_data[x][y][1]=c1[1];
				file.Input.image_data[x][y][2]=c1[2];
			}
		}
		//--Dreiecke zeichnen---------------------------------
		int bt=x2-x1;
		int n=bt/b;
		int nl=(y2-y1)/h;
		for (int l=0;m<nl;m++){
			for(int i=1;i<h+1;i++){
				int b1=b-(b/h*i);
				int b2=b/h*i;
				int x=x1+b2/2;
				int y=i+y1-1;
	
	
	
				if(y<y2+1){
					for(int j=0;j<n;j++){
						for(int k=0;k<b1;k++){
							if(x<bt+1){
								file.Input.image_data[x][y][0]=c2[0];
								file.Input.image_data[x][y][1]=c2[1];
								file.Input.image_data[x][y][2]=c2[2];
							}
							x++;
						}
						x=x+b2;
					}
				}
			}
			y1=y1+h;
			if(m%2==0) x1=x1+b/2;
			else x1=x1-b/2;
		}	
	}

	

	public static void PythagorasBaum(int x1,int y1,int x2,int y2,int h,int b,int[] c1,int[] c2){
		
		
	}
}
