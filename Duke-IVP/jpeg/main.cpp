#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include<iostream>
#include<fstream>

#include "JPEG.h"

using namespace cv;

const int M = 256;

int main( int, char** argv )
{
  	Mat image(M, M, CV_8U);
  	
  	std::cout << argv[1] << std::endl;
  	//image = imread( argv[1], CV_LOAD_IMAGE_COLOR);
  	
  	char buffer[M][M];  
    std::ifstream in(argv[1]);  
    if (!in.is_open())  
    { 
    	std::cout << "Error opening file"; 
    	exit (-1); 
    }  
    
	for(int i = 0;i < M;i++)
	{
		for(int j = 0;j < M;j++)
		{
			int tmp;
			in >> tmp;
			image.at<unsigned char>(i,j)=tmp;
		}
	}
	in.close();


  	
  	if( !image.data )
  		return -1;
  		
  	//cvtColor(image, gray, CV_BGR2GRAY);
	imshow("original image",image);

  	waitKey(0);
  	return 0;
}
