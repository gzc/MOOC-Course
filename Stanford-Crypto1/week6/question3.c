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
    mpz_init_set_str(N, "720062263747350425279564435525583738338084451473999841826653057981916355690188337790423408664187663938485175264994017897083524079135686877441155132015188279331812309091996246361896836573643119174094961348524639707885238799396839230364676670221627018353299443241192173812729276147530748597302192751375739387929", 10);
    //gmp_printf("N =           %Zd\n\n", N);
    
    mpz_t A;
    mpz_init (A);
    mpz_mul_ui(A, N, 24);
    int r = mpz_root(A, A, 2);
    if(r == 0) mpz_add_ui(A, A, 1);
    
    
    while(1)
    {
    
        mpz_t p;
        mpz_init (p);
        mpz_t q;
        mpz_init (q);
        mpz_t tmp;
        mpz_init (tmp);

        mpz_mul(p,A,A);
        mpz_mul_ui(tmp, N, 24);
        mpz_sub(p, p, tmp);

        
        r = mpz_root(p, p, 2);
        if(r != 0) {printf("OK!!!!\n\n\n\n\n");}
        
        mpz_add(p, A, p);
        mpz_cdiv_q_ui(p, p, 4);

        mpz_mod (tmp, N, p);
        
        r = mpz_cmp_ui(tmp, 0);
        if(r == 0)
        {
            mpz_cdiv_q(q, N, p);
            gmp_printf("p =           %Zd\n\n", p);
            gmp_printf("q =           %Zd\n\n", q);
            break;
        }
        
        
        mpz_add_ui(A, A, 1);
    }

}
