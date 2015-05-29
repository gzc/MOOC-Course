/*************************************************************************
	> File Name: main.c
	> Author: Louis1992
	> Mail: zhenchaogan@gmail.com
	> Blog: http://gzc.github.io
	> Created Time: Fri May 29 16:18:18 2015
 ************************************************************************/
#include <stdio.h>
#include <gmp.h>
#include <string.h>

int convert(char ch)
{
    if(ch >= '0' && ch <= '9') return ch-'0';
    else return ch-'a'+10;
}


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
    
    
    
    
    mpz_t e;
    mpz_init_set_str(e, "65537", 10);
    mpz_t d,phi,tp,tq;
    mpz_init(d);
    mpz_init(phi);
    mpz_init(tp);
    mpz_init(tq);
    mpz_sub_ui(tp,p,1);
    mpz_sub_ui(tq,q,1);
    mpz_mul(phi,tp,tq);
    mpz_invert(d, e, phi);
    
    gmp_printf("phi=          %Zd\n\n", phi);
    gmp_printf("d =           %Zd\n\n", d);
    
    mpz_t c;
    mpz_init_set_str(c,"22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540",10);
    
    mpz_powm (c, c, d, N);
    
    gmp_printf("c =           %Zd\n\n", c);
    //get c
    char C[500];
    char *ch;
    ch = mpz_get_str (C , 16, c);
    printf("ciphertext : %s\n\n", C);
    
    
    ch = strstr(C, "00");
    ch += 2;
    printf("plaintext  : %s\n\n", ch);
    
    while(*ch != '\0')
    {
        int ttt = 16*convert(*ch)+convert(*(ch+1));
        printf("%c",ttt);
        ch += 2;
    }
    
    
}
