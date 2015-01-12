/*************************************************************************
	> File Name: JPEG.cpp
	> Author: 
	> Mail: 
	> Created Time: Mon 12 Jan 2015 10:20:53 PM CST
 ************************************************************************/
#include<iostream>
#include "JPEG.h"

std::vector<std::vector<int> > JPEG::change_Color_Space(cv::Mat *image)
{
    int w = image->cols;
    int h = image->rows;
    std::vector<std::vector<int> > YUV_image(h, std::vector<int>(w));

    

    return YUV_image;
}

