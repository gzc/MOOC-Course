/*************************************************************************
	> File Name: main.c
	> Author: Louis1992
	> Mail: zhenchaogan@gmail.com
	> Blog: http://gzc.github.io
	> Created Time: Fri May 29 16:18:18 2015
 ************************************************************************/
#include <stdio.h>
#include <gmp.h>


int main()
{
    
    mpz_t N;
    mpz_init_set_str(N, "648455842808071669662824265346772278726343720706976263060439070378797308618081116462714015276061417569195587321840254520655424906719892428844841839353281972988531310511738648965962582821502504990264452100885281673303711142296421027840289307657458645233683357077834689715838646088239640236866252211790085787877", 10);
    gmp_printf("N =           %Zd\n\n", N);
    
    mpz_t A;
    mpz_init (A);
    int r = mpz_root(A, N, 2);
    gmp_printf("root A =      %Zd\n\n", A);
    if(r == 0) mpz_add_ui(A, A, 1);
    gmp_printf("cell root A = %Zd\n\n", A);
    
    for(unsigned int i = 0;i < (2 << 20);i++)
    {
    
        mpz_t x;
        mpz_init(x);
        mpz_pow_ui(x, A, 2);
        //gmp_printf("square A =    %Zd\n\n", x);
        mpz_sub(x, x, N);
        //gmp_printf("square A - N= %Zd\n\n", x);
        r = mpz_root(x, x, 2);
        //gmp_printf("x =           %Zd\n\n", x);
    
        mpz_t p;
        mpz_init(p);
        mpz_t q;
        mpz_init(q);
        mpz_sub(p, A, x);
        mpz_add(q, A, x);
        
        mpz_mul(x, p, q);
        
        r = mpz_cmp(x, N);
        if(r == 0)
        {
            gmp_printf("p =           %Zd\n\n", p);
            gmp_printf("q =           %Zd\n\n", q);
            break;
        }
        
        
        mpz_add_ui(A, A, 1);
    }

}
