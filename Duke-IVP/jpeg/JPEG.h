/*************************************************************************
	> File Name: JPEG.h
	> Author: 
	> Mail: 
	> Created Time: Mon 12 Jan 2015 10:00:14 PM CST
 ************************************************************************/

#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <vector>
#include "HUFFMAN.h"

#ifndef _JPEG_H
#define _JPEG_H

class JPEG
{
public:
    static std::vector<std::vector<int> > change_Color_Space(cv::Mat*);
};

#endif
