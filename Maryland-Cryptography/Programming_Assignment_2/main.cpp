#include <iostream>
#include <string>
#include <vector>

using namespace std;

int convert(char ch)
{
    if(ch >= '0' && ch <= '9') return ch-'0';
    else return ch-'A'+10;
}

int main()
{
    string ciphertext[7] = {
        "BB3A65F6F0034FA957F6A767699CE7FABA855AFB4F2B520AEAD612944A801E",
        "BA7F24F2A35357A05CB8A16762C5A6AAAC924AE6447F0608A3D11388569A1E",
        "A67261BBB30651BA5CF6BA297ED0E7B4E9894AA95E300247F0C0028F409A1E",
        "A57261F5F0004BA74CF4AA2979D9A6B7AC854DA95E305203EC8515954C9D0F",
        "BB3A70F3B91D48E84DF0AB702ECFEEB5BC8C5DA94C301E0BECD241954C831E",
        "A6726DE8F01A50E849EDBC6C7C9CF2B2A88E19FD423E0647ECCB04DD4C9D1E",
        "BC7570BBBF1D46E85AF9AA6C7A9CEFA9E9825CFD5E3A0047F7CD009305A71E"
    };
    
    unsigned long int len = ciphertext[0].size();
    
    vector<vector<int>> ciphers(7, vector<int>((int)len/2, 0));
    
    int i = 0;
    while(i < len)
    {
        for(int j = 0;j < 7;j++)
        {
            int ch = convert(ciphertext[j][i])*16 + convert(ciphertext[j][i+1]);
            ciphers[j][i/2] = ch;
        }
        i += 2;
    }
    
    int A[7][len/2];
    for(i = 0;i < len/2;i++)
    {
        for(int j = 0; j < 7;j++)
        {
            int sum(0);
            int c = ciphers[j][i];
            for(int k = 0;k < 7;k++)
            {
                if(j == k) continue;
                if((c ^ ciphers[k][i]) > 0x40) sum++;
            }
            A[j][i] = sum;
        }
    }
    
    vector<int> keys((int)len/2, -1);
    for(int i = 0;i < 7;i++)
        for(int j = 0;j < len/2;j++)
            if(A[i][j] > 3)
                keys[j] = (' ' ^ ciphers[i][j]);
    
    
    for(int i = 0;i < 7;i++)
    {
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
    
    keys[0] = 'T' ^ ciphers[5][0];
    keys[6] = 'k' ^ ciphers[4][6];
    keys[8] = 'e' ^ ciphers[2][8];
    keys[10] = 'i' ^ ciphers[0][10];
    keys[17] = 'e' ^ ciphers[0][17];
    keys[20] = 'e' ^ ciphers[0][20];
    keys[29] = 'n' ^ ciphers[0][29];
    keys[30] = '.' ^ ciphers[0][30];
    cout << "--------------------------" << endl;
    
    for(int i = 0;i < 7;i++)
    {
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
    

}