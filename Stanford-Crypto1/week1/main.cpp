#include <iostream>
#include <string>
#include <vector>

using namespace std;
#define D 10

int convert(char ch)
{
    if(ch >= '0' && ch <= '9') return ch-'0';
    else return ch-'a'+10;
}

int main()
{
    string ciphertext[10] = {
        "315c4eeaa8b5f8aaf9174145bf43e1784b8fa00dc71d885a804e5ee9fa40b16349c146fb778cdf2d3aff021dfff5b403b510d0d0455468aeb98622b137dae857553ccd8883a7bc37520e06e515d22c954eba5025b8",
        "234c02ecbbfbafa3ed18510abd11fa724fcda2018a1a8342cf064bbde548b12b07df44ba7191d9606ef4081ffde5ad46a5069d9f7f543bedb9c861bf29c7e205132eda9382b0bc2c5c4b45f919cf3a9f1cb74151f6",
        "32510ba9a7b2bba9b8005d43a304b5714cc0bb0c8a34884dd91304b8ad40b62b07df44ba6e9d8a2368e51d04e0e7b207b70b9b8261112bacb6c866a232dfe257527dc29398f5f3251a0d47e503c66e935de81230b5",
        "32510ba9aab2a8a4fd06414fb517b5605cc0aa0dc91a8908c2064ba8ad5ea06a029056f47a8ad3306ef5021eafe1ac01a81197847a5c68a1b78769a37bc8f4575432c198ccb4ef63590256e305cd3a9544ee4160ea",
        "3f561ba9adb4b6ebec54424ba317b564418fac0dd35f8c08d31a1fe9e24fe56808c213f17c81d9607cee021dafe1e001b21ade877a5e68bea88d61b93ac5ee0d562e8e9582f5ef375f0a4ae20ed86e935de81230b5",
        "32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd2061bbde24eb76a19d84aba34d8de287be84d07e7e9a30ee714979c7e1123a8bd9822a33ecaf512472e8e8f8db3f9635c1949e640c621854eba0d79ec",
        "32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd90f1fa6ea5ba47b01c909ba7696cf606ef40c04afe1ac0aa8148dd066592ded9f8774b529c7ea125d298e8883f5e9305f4b44f915cb2bd05af51373fd",
        
        "315c4eeaa8b5f8bffd11155ea506b56041c6a00c8a08854dd21a4bbde54ce56801d943ba708b8a3574f40c00fff9e00fa1439fd0654327a3bfc860b92f89ee04132ecb9298f5fd2d5e4b45e40ecc3b9d59e9417df7",
        
        "271946f9bbb2aeadec111841a81abc300ecaa01bd8069d5cc91005e9fe4aad6e04d513e96d99de2569bc5e50eeeca709b50a8a987f4264edb6896fb537d0a716132ddc938fb0f836480e06ed0fcd6e9759f40462f9",
        
        "466d06ece998b7a2fb1d464fed2ced7641ddaa3cc31c9941cf110abbf409ed39598005b3399ccfafb61d0315fca0a314be138a9f32503bedac8067f03adbf3575c3b8edc9ba7f537530541ab0f9f3cd04ff50d66f1"
    };
    
    unsigned long int len = ciphertext[0].size();
    
    vector<vector<int> > ciphers(10, vector<int>((int)len/2, 0));
    
    int i = 0;
    while(i < len)
    {
        for(int j = 0;j < 10;j++)
        {
            int ch = convert(ciphertext[j][i])*16 + convert(ciphertext[j][i+1]);
            ciphers[j][i/2] = ch;
        }
        i += 2;
    }
    
    int A[10][len/2];
    for(i = 0;i < len/2;i++)
    {
        for(int j = 0; j < 10;j++)
        {
            int sum(0);
            int c = ciphers[j][i];
            for(int k = 0;k < 10;k++)
            {
                if(j == k) continue;
                if((c ^ ciphers[k][i]) > 0x40) sum++;
            }
            A[j][i] = sum;
        }
    }
    
    vector<int> keys((int)len/2, -1);
    for(int i = 0;i < 10;i++)
        for(int j = 0;j < len/2;j++)
            if(A[i][j] > 5)
                keys[j] = (' ' ^ ciphers[i][j]);
    
    
    for(int i = 0;i < 10;i++)
    {
        cout << i << ":";
        for(int j = 0;j < len/2;j++)
        {
            if(keys[j] >= 0)
            {
                char c = keys[j] ^ ciphers[i][j];
                cout << c;
            } else
                cout << '?';
        }
        cout << endl;
    }
    
    /*
     keys[0] = 'T' ^ ciphers[5][0];
     keys[6] = 'k' ^ ciphers[4][6];
     keys[8] = 'e' ^ ciphers[2][8];
     keys[10] = 'i' ^ ciphers[0][10];
     keys[17] = 'e' ^ ciphers[0][17];
     keys[20] = 'e' ^ ciphers[0][20];
     keys[29] = 'n' ^ ciphers[0][29];
     keys[30] = '.' ^ ciphers[0][30];
     */
    cout << "--------------------------------------------------------------------------" << endl;
    
    for(int i = 0;i < 10;i++)
    {
        cout << i << ":";
        for(int j = 0;j < len/2;j++)
        {
            if(keys[j] >= 0)
            {
                char c = keys[j] ^ ciphers[i][j];
                cout << c;
            } else
                cout << '?';
        }
        cout << endl;
    }
    
    cout << "***********************************" << endl;
    
    string target = "32510ba9babebbbefd001547a810e67149caee11d945cd7fc81a05e9f85aac650e9052ba6a8cd8257bf14d13e6f0a803b54fde9e77472dbff89d71b57bddef121336cb85ccb8f3315f4b52e301d16e9f52f904";
    vector<int> cc;
    for(i = 0;i < target.length();i += 2)
    {
        int ch = convert(target[i])*16 + convert(target[i+1]);
        cc.push_back(ch);
    }

    for(i = 0;i < cc.size();i++)
    {
        if(keys[i] >= 0)
        {
            char c = keys[i] ^ cc[i];
            cout << c;
        } else
            cout << '?';
    }
}