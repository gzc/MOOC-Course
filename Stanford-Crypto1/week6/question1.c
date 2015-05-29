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
    mpz_init_set_str(N, "179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581", 10);
    gmp_printf("N =           %Zd\n\n", N);
    
    mpz_t A;
    mpz_init (A);
    int r = mpz_root(A, N, 2);
    gmp_printf("root A =      %Zd\n\n", A);
    if(r == 0) mpz_add_ui(A, A, 1);
    gmp_printf("cell root A = %Zd\n\n", A);
    
    mpz_t x;
    mpz_init(x);
    mpz_pow_ui(x, A, 2);
    gmp_printf("square A =    %Zd\n\n", x);
    mpz_sub(x, x, N);
    gmp_printf("square A - N= %Zd\n\n", x);
    r = mpz_root(x, x, 2);
    gmp_printf("x =           %Zd\n\n", x);
    
    mpz_t p;
    mpz_init(p);
    mpz_t q;
    mpz_init(q);
    mpz_sub(p, A, x);
    mpz_add(q, A, x);

    gmp_printf("p =           %Zd\n\n", p);
    gmp_printf("q =           %Zd\n\n", q);
    

    mpz_clear(N);
    mpz_clear(A);
    mpz_clear(x);
    mpz_clear(p);
    mpz_clear(q);

}
