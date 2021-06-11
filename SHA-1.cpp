#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <cstring>
#include <string>

using namespace std;
typedef unsigned long long Length_t;
typedef unsigned int Word;
typedef unsigned char Byte;

class SHA_1
{
private:
    //初始向量IV,K
    Word IVsrc[5] = {0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0};
    Word Ksrc[4] = {0x5a827999, 0x6ed9eba1, 0x8f1bbcdc, 0xca62c1d6};
    //IV和K的副本，每一次计算都使用IV和K而不改变其源值
    Word IV[5];
    Word K[4];
    //拷贝IV和K的初值以使用
    void init()
    {
        for (int i = 0; i < 5; i++)
            IV[i] = IVsrc[i];
        for (int i = 0; i < 4; i++)
            K[i] = Ksrc[i];
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
    // 将512bit明文分组
    void init(Byte *m, Word *w)
    {
        int i;
        for (i = 0; i < 16; i++)
        {
            Byte tmp[4];
            for (int j = 0; j < 4; j++)
            {
                tmp[j] = m[i * 4 + j];
            }
            w[i] = tmp[0] << 24 | tmp[1] << 16 | tmp[2] << 8 | tmp[3];
        }
        for (i = 16; i < 80; i++)
            w[i] = ROTL(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
    }
    //逻辑函数
    Word lf(int t)
    {
        Word res;
        if (t >= 0 && t <= 19)
            res = IV[1] & IV[2] | ~IV[1] & IV[3];
        else if (t >= 40 && t <= 59)
            res = IV[1] & IV[2] | IV[1] & IV[3] | IV[2] & IV[3];
        else
            res = IV[1] ^ IV[2] ^ IV[3];
        return res;
    }
    //SHA压缩变换
    void cndfunc(Word w, int t)
    {
        Word tmp = lf(t) + IV[4] + ROTL(IV[0], 5) + w + K[t / 20];
        IV[4] = IV[3];
        IV[3] = IV[2];
        IV[2] = ROTL(IV[1], 30);
        IV[1] = IV[0];
        IV[0] = tmp;
        // 输出变换结果
        printf("%2d  %08x ", t, w);
        for (int i = 0; i < 5; i++)
            printf("%08x ", IV[i]);
        cout << endl;
    }
    //拼接五个向量以获得最终结果
    void getres(Byte *res)
    {
        for (size_t i = 0; i < 5; i++)
        {
            Byte tmp[4];
            for (int j = 0; j < 4; j++)
                tmp[j] = (IV[i] >> (24 - 8 * j)) & 0xff;
            memcpy(res + 4 * i, tmp, 4);
        }
    }
    //将16进制字符串转为Byte数组
    Byte *hexToBytes(string hex, Length_t len)
    {
        Byte *bytes = (Byte *)malloc(len);
        string strByte;
        Word n;
        for (int i = 0; i < len; i++)
        {
            strByte = hex.substr(i * 2, 2);
            sscanf(strByte.c_str(), "%x", &n);
            bytes[i] = n;
        }
        return bytes;
    }

public:
    Byte *msg = NULL;
    Length_t length;

    ~SHA_1()
    {
        if (msg != NULL)
            free(msg);
    }

    //添加消息
    void addMsg(string hexMsg)
    {
        if (msg != NULL)
            free(msg);
        if (hexMsg.length() % 2 != 0)
        {
            cout << "Length Error\n";
            exit(-1);
        }
        length = hexMsg.length() / 2;
        msg = hexToBytes(hexMsg, length);
    }

    // 做散列变换
    void doFinal(Byte *res)
    {
        init();
        Length_t len = length;
        Byte *buf = padding(msg, &len);    //填充
        for (int i = 0; i < len / 64; i++) //每512bit消息进行变换
        {
            Byte tmp[64];
            Word w[80] = {0};
            memcpy(tmp, buf + i * 64, 64); //获取每个512bit块
            init(tmp, w);                  //初始化w[80]
            Word IVcpy[5];
            for (int i = 0; i < 5; i++) //记录每个512bit块变化前的初始向量值
                IVcpy[i] = IV[i];
            for (int t = 0; t < 80; t++) //80轮压缩变换
                cndfunc(w[t], t);
            for (int i = 0; i < 5; i++) //模2^32加
                IV[i] += IVcpy[i];
        }
        getres(res); //拼接结果
        free(buf);
    }
    //BYTE数组转16进制字符串
    string bytesToHex(Byte *bytes, const int length)
    {
        string buf;
        for (int i = 0; i < length; i++)
        {
            int high = bytes[i] / 16, low = bytes[i] % 16;
            buf += (high < 10) ? ('0' + high) : ('a' + high - 10);
            buf += (low < 10) ? ('0' + low) : ('a' + low - 10);
        }
        return buf;
    }
};
//测试
int main()
{
    SHA_1 sha;
    sha.addMsg("616263");
    Byte res[20];
    sha.doFinal(res);
    cout << "SHA-1 res: " << sha.bytesToHex(res, 20) << endl;
}
