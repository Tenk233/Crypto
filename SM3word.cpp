#include <cstdlib>
#include <cstring>
#include <string>

typedef unsigned long long Length_t;
typedef unsigned int Word;
typedef unsigned char Byte;

class SM3
{
private:
    //常量
    Word T(int j)
    {
        if (j < 16)
            return 0x79cc4519;
        return 0x7a879d8a;
    }
    //布尔函数
    Word FF(Word x, Word y, Word z, int j)
    {
        if (j < 16)
            return x ^ y ^ z;
        return (x & y) | (x & z) | (y & z);
    }
    Word GG(Word x, Word y, Word z, int j)
    {
        if (j < 16)
            return x ^ y ^ z;
        return (x & y) | (~x & z);
    }
    //置换函数
    Word P0(Word X)
    {
        return X ^ ROTL(X, 9) ^ ROTL(X, 17);
    }
    Word P1(Word X)
    {
        return X ^ ROTL(X, 15) ^ ROTL(X, 23);
    }
    //x循环左移n位
    Word ROTL(Word x, int n)
    {
        return x << n | x >> (32 - n);
    }
    //将消息长度转为字节数组
    void getbytes(Byte *bytes, Length_t l)
    {
        for (int i = 0; i < 8; i++)
            bytes[i] = l >> (56 - i * 8);
    }
    //为消息填充填1、若干0以及长度
    Byte *padding(Byte *input, Length_t *len)
    {
        int padlen, length = *len;
        int tmp = length % 64;
        if (tmp == 56)
            padlen = 64;
        else if (tmp < 56)
            padlen = 56 - tmp;
        else
            padlen = 120 - tmp; //padlen = 64 - (tmp - 56);
        Byte *output = (Byte *)malloc(length + padlen + 8);
        memcpy(output, input, length);
        output[length] = 128;
        for (int i = 0; i < padlen - 1; i++)
            output[length + i + 1] = 0;
        Byte bytes[8];
        getbytes(bytes, length * 8);
        length += padlen;
        memcpy(output + length, bytes, 8);
        *len = length + 8;
        return output;
    }
    // 消息扩展
    void expend(Byte *m, Word *w)
    {
        int i;
        for (i = 0; i < 16; i++)
            w[i] = m[i * 4] << 24 | m[i * 4 + 1] << 16 | m[i * 4 + 2] << 8 | m[i * 4 + 3];
        for (i = 16; i < 68; i++)
            w[i] = P1(w[i - 16] ^ w[i - 9] ^ ROTL(w[i - 3], 15)) ^ ROTL(w[i - 13], 7) ^ w[i - 6];
    }

public:
    void doFinal(Byte *msg)
    {
        Word IV[8] = {0x7380166f, 0x4914b2b9, 0x172442d7, 0xda8a0600, 0xa96f30bc, 0x163138aa, 0xe38dee4d, 0xb0fb0e4e};
        Length_t len = strlen((char *)msg);
        Byte *nmsg = padding(msg, &len);
        for (int i = 0; i < len / 64; i++)
        {
            Byte tmp[64];
            Word w[68] = {0}, nw[64] = {0};
            memcpy(tmp, nmsg + i * 64, 64); //获取每个512bit块
            expend(tmp, w);
            for (int j = 0; j < 64; j++)
                nw[j] = w[j] ^ w[j + 4];
            Word A = IV[0], B = IV[1], C = IV[2], D = IV[3], E = IV[4], F = IV[5], G = IV[6], H = IV[7];
            printf("process:\n   ");
            for (int i = 0; i < 8; i++)
                printf("%08x ", IV[i]);
            printf("\n");
            for (int j = 0; j < 64; j++)
            {
                Word SS1 = ROTL(ROTL(A, 12) + E + ROTL(T(j), j), 7);
                Word SS2 = SS1 ^ ROTL(A, 12);
                Word TT1 = FF(A, B, C, j) + D + SS2 + nw[j];
                Word TT2 = GG(E, F, G, j) + H + SS1 + w[j];
                D = C;
                C = ROTL(B, 9);
                B = A;
                A = TT1;
                H = G;
                G = ROTL(F, 19);
                F = E;
                E = P0(TT2);
                printf("%2d ", j);
                printf("%08x %08x %08x %08x %08x %08x %08x %08x", A, B, C, D, E, F, G, H);
                printf("\n");
            }
            IV[0] = IV[0] ^ A;
            IV[1] = IV[1] ^ B;
            IV[2] = IV[2] ^ C;
            IV[3] = IV[3] ^ D;
            IV[4] = IV[4] ^ E;
            IV[5] = IV[5] ^ F;
            IV[6] = IV[6] ^ G;
            IV[7] = IV[7] ^ H;
        }
        printf("hash: ");
        for (int i = 0; i < 8; i++)
            printf("%08x ", IV[i]);
        free(nmsg);
    }
};

int main()
{
    char *msg = "abc";
    SM3 sm3;
    sm3.doFinal((Byte *)msg);
    printf("\n");
    return 0;
}
