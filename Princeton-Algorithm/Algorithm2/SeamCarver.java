import java.awt.Color;



public class SeamCarver {
	
	private Picture currentPicture;
	private int width;
	private int height;
	private final double maxEnergy = 3*255.0*255.0;
	private Color[][] colors;
	private double[][] energies;
	
	public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	{
		currentPicture = new Picture(picture);
		width = currentPicture.width();
		height = currentPicture.height();
		colors = new Color[width][height];
		energies = new double[width][height];
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				colors[i][j] = currentPicture.get(i, j);
			}
		}
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				energies[i][j] = energy(i,j);
			}
		}
		
		
	}
	
	public Picture picture()                          // current picture
	{
		Picture newPic = new Picture(width,height);
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				newPic.set(i, j, colors[i][j]);
			}
		}
		currentPicture = newPic;
		return currentPicture;
	}
	
	public int width()                            // width of current picture
	{
		return width;
	}
	
	public int height()                           // height of current picture
	{
		return height;
	}
	
	public  double energy(int x, int y)               // energy of pixel at column x and row y
	{
		if(x < 0 || x > (width-1) || y < 0 || y > (height-1))
			throw new IndexOutOfBoundsException();
		else if(x == 0)
			return maxEnergy;
		else if(x == width-1)
			return maxEnergy;
		else if(y == 0)
			return maxEnergy;
		else if(y == height-1)
			return maxEnergy;
		else
		{
			double r1 = colors[x+1][y].getRed() - colors[x-1][y].getRed();
			double g1 = colors[x+1][y].getGreen() - colors[x-1][y].getGreen();
			double b1 = colors[x+1][y].getBlue() - colors[x-1][y].getBlue();
			
			double r2 = colors[x][y+1].getRed() - colors[x][y-1].getRed();
			double g2 = colors[x][y+1].getGreen() - colors[x][y-1].getGreen();
			double b2 = colors[x][y+1].getBlue() - colors[x][y-1].getBlue();
			
			double s1 = r1*r1+g1*g1+b1*b1;
			double s2 = r2*r2+g2*g2+b2*b2;
			
			return (s1+s2);
		}
	}
	
	public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	{
		double[][] copyEnergies = new double[width][height];
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				copyEnergies[i][j] = energies[i][j];
			}
		}
		
		energies = new double[height][width];
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				energies[j][i] =  copyEnergies[i][j];
			}
		}
		
		int temp = width;
		width = height;
		height = temp;
		
		int[] result = findVerticalSeam();
		energies = copyEnergies;
		temp = width;
		width = height;
		height = temp;
		return result;
	}
	
	public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
	{
		int edgeto[][] = new int[width][height];
		double total[][] = new double[width][height];
		
		for(int i = 0;i < width;i++)
		{
			total[i][0] = maxEnergy;
		}
		
		for(int j = 0;j < (height-1);j++)
		{
			for(int i = 0;i < width;i++)
			{
				if(i == 0)
				{
					if(total[i][j] <= total[i+1][j])
					{
						total[i][j+1] = total[i][j] + energies[i][j+1];
						edgeto[i][j+1] = 0;
					} else {
						total[i][j+1] = total[i+1][j] + energies[i][j+1];
						edgeto[i][j+1] = 1;
					}
				}
				
				else if(i == (width - 1) )
				{
					if(total[i-1][j] <= total[i][j])
					{
						total[i][j+1] = total[i-1][j] + energies[i][j+1];
						edgeto[i][j+1] = -1;
					} else {
						total[i][j+1] = total[i][j] + energies[i][j+1];
						edgeto[i][j+1] = 0;
					}
				}
				
				else {
					int index = maxInThreeNum(total[i-1][j], total[i][j], total[i+1][j]);
					edgeto[i][j+1] = index;
					total[i][j+1] = total[i+index][j] + energies[i][j+1];
				}
			}
		}
		
		int index = 0;
		double minEnergy = total[0][height-1];
		for(int i = 1;i < width;i++)
		{
			if(total[i][height-1] < minEnergy )
			{
				index = i;
				minEnergy = total[i][height-1];
			}
		}
		
		int[] path = new int[height];
		for(int j = height-1; j>= 0;j--)
		{
			path[j] = index;
			int key = edgeto[index][j];
			index = index+key;
		}
		
		return path;
	}
	
	public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
		double[][] copyEnergies = new double[width][height];
		Color[][] copyColors = new Color[width][height];
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				copyEnergies[i][j] = energies[i][j];
				copyColors[i][j] = colors[i][j];
			}
		}
		
		energies = new double[height][width];
		colors = new Color[height][width];
		
		for(int i = 0;i < width;i++)
		{
			for(int j = 0;j < height;j++)
			{
				energies[j][i] =  copyEnergies[i][j];
				colors[j][i] = copyColors[i][j];
			}
		}
		
		int temp = width;
		width = height;
		height = temp;
		
		removeVerticalSeam(seam);
		
		temp = width;
		width = height;
		height = temp;
		
		copyEnergies = energies;
		copyColors = colors;
		
		energies = new double[width][height];
		colors = new Color[width][height];
		
		for(int i = 0;i < height;i++)
		{
			for(int j = 0;j < width;j++)
			{
				energies[j][i] = copyEnergies[i][j];
				colors[j][i] = copyColors[i][j];
			}
		}
	}
	
	public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
	{
		if(seam.length != height)
			throw new RuntimeException();
		
		/*
		Color[][] newColors = new Color[width-1][height];
		double[][] newEnergies = new double[width-1][height];
		for(int j = 0;j < height;j++)
		{
			int index = 0;
			for(int i = 0;i < width;i++)
			{
				if(seam[j] != i)
				{
					newColors[index][j] = colors[i][j];
					newEnergies[index][j] = energies[i][j];
					index++;
				} 
			}
		}
		colors = newColors;
		energies = newEnergies;*/
		for(int j = 0;j < height;j++)
		{
			for(int n = seam[j];n < (width-1);n++)
			{
				colors[n][j] = colors[n+1][j];
				energies[n][j] = energies[n+1][j];
			}
		}
		
		
		
		width = width-1;
		
		for(int i = 0;i < height;i++)
		{
			if(seam[i] == 0)
			{
				energies[0][i] = energy(0,i);
			} else if(seam[i] == width )
			{
				energies[seam[i]-1][i] = energy(seam[i]-1,i);
			} else
			{
				energies[seam[i]-1][i] = energy(seam[i]-1,i);
				energies[seam[i]][i] = energy(seam[i],i);
			}
		}	
		
	}
	
	
	private int maxInThreeNum(double a, double b, double c)
	{
		if(a <= b && a <= c)
			return -1;
		else if (b <= a && b <= c)
			return 0;
		return 1;
	}
	
}