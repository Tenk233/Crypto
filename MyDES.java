/**
 * description:
 * 运算过程中一个byte表示一个bit
 * @Coding UTF-8
 * @Auther Tenk
 * @Date 2021/4/10 19:12
 */
public class MyDES {
    private final byte[] IP = { //初始置换IP表
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};
    private final byte[] reIP = { //逆初始置换IP**-1
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};
    private final byte[] transE = { //扩展变换E
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};
    private final byte[] transP = { //P置换
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};
    private final byte[][][] sBox = { //8个s盒
            {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
            {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
            {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
            {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
            {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
            {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
            {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
            {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};
    private final byte[] shiftLeft = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private final byte[] rplSlOneC0={ //密钥置换选择1 C0
            57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36
    };
    private final byte[] rplSlOneD0={ //密钥置换选择1 D0
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4
    };
    private final byte[] rplSlTwo={ //密钥置换选择2
            14,17,11,24,1,5,
            3,28,15,6,21,10,
            23,19,12,4,26,8,
            16,7,27,20,13,2,
            41,52,31,37,47,55,
            30,40,51,45,33,48,
            44,49,39,56,34,53,
            46,42,50,36,29,32
    };
    //@description: 加密
    //@param msg:明文输入，大小任意
    //@param key:秘钥输入，由exKey()方法生成
    public byte[] encrypt(byte[] msg,byte[][] key){
        return crypt(msg, key);
    }
    //@description: 解密
    //@param msg:明文输入，大小任意
    //@param key:秘钥输入，由exKey()方法生成
    public byte[] decrypt(byte[] msg,byte[][] key){
        return rePadding(crypt(msg, key));
    }

    //@description: 加解密一段话
    private byte[] crypt(byte[] input,byte[][] key){
        input=paddingMsg(input);
        byte[] tmp;
        int i;
        for (i = 0; i < input.length; i+=8) {
            tmp=new byte[8];
            System.arraycopy(input,i,tmp,0,8);
            tmp=bitsToBytes(encode(bytesToBits(tmp),key));
            System.arraycopy(tmp,0,input,i,8);
        }
        return input;
    }

    //description: 使输入保持64bit的大小
    private byte[] paddingMsg(byte[] msg){
        int n=8-msg.length%8;
        if(n==8) return msg;
        byte[] tmp=new byte[msg.length+n];
        System.arraycopy(msg,0,tmp,0,msg.length);
        for (int i = 0; i < n; i++)
            tmp[msg.length+i]=0;
        return tmp;
    }
    //@description: 删去解密后明文被paddingMsg()方法添加的零字节
    private byte[] rePadding(byte[] in){
        int i;
        for (i = in.length-1; i>=0; i--)
            if (in[i]!=0) break;
        if(i+1==in.length) return in;
        byte[] out=new byte[i+1];
        System.arraycopy(in,0,out,0,i+1);
        return out;
    }
    //@description: 秘钥扩展
    //@param key:64bit
    public byte[][] exKey(byte[] key){
        if(key.length==0) return null;
        byte[] C0=new byte[28],D0=new byte[28];
        byte[][] out=new byte[16][48];
        if(key.length!=8){
            byte[] nKey=new byte[8];
            for (int i = 0; i < key.length && i<8; i++)
                nKey[i]=key[i];
            for (int i = key.length; i < 8; i++) { // padding the key!
                nKey[i]=0;
            }
            key=nKey;
        }
        key=bytesToBits(key);
        for (int i = 0; i < 28; i++) { // 置换选择1
            C0[i]=key[rplSlOneC0[i]-1];
            D0[i]=key[rplSlOneD0[i]-1];
        }
        key=new byte[56];
        for (int i = 0; i < 16; i++) {
            C0=rot(C0,shiftLeft[i]); // 循环左移
            D0=rot(D0,shiftLeft[i]); // 循环左移
            System.arraycopy(C0,0,key,0,28);
            System.arraycopy(D0,0,key,28,28);
            for (int j = 0; j < 48; j++)  //置换选择2
                out[i][j]=key[rplSlTwo[j]-1];
        }
        return out;
    }
    //@description: 密钥选择时，循环左移n位
    //@param in:28bit
    private byte[] rot(byte[] in,int n){
        byte[] out=new byte[28];
        for (int i = 0; i < 28; i++) {
            out[i]=in[(i+n)%28];
        }
        return out;
    }

    //@description: DES加解密处理64位明文
    //@param msg:64位输入
    //param key:16轮48位密钥
    private byte[] encode(byte[] msg,byte[][] key){
        byte[][] tmp = new byte[2][32];
        msg=initIP(msg);
        System.arraycopy(msg,0,tmp[0],0,32);
        System.arraycopy(msg,32,tmp[1],0,32);
        System.out.println("分组:"+bytesToHex(bitsToBytes(tmp[0]))+"  "+bytesToHex(bitsToBytes(tmp[1])));
        for (int i = 0; i < 16; i++) {
            tmp = oneRndItrPrc(tmp[0], tmp[1], key[i]);
            // 打印加密过程
            System.out.println("第"+(i+1)+"轮:"+bytesToHex(bitsToBytes(tmp[0]))+"  "+bytesToHex(bitsToBytes(tmp[1])) +"  "+bytesToHex(bitsToBytes(key[i])));
        }
        System.arraycopy(tmp[1],0,msg,0,32);
        System.arraycopy(tmp[0],0,msg,32,32);
        return reInitIP(msg);
    }
    // @description: 初始置换IP（64bit->64bit）
    // @param in:64位明文
    private byte[] initIP(byte[] in){
        byte[] out=new byte[64];
        for (int i = 0; i < 64; i++)
            out[i]=in[IP[i]-1];
        return out;
    }
    //@description: 逆初始置换
    //@param in:64位输入
    private byte[] reInitIP(byte[] in){
        byte[] out=new byte[64];
        for (int i = 0; i < 64; i++)
            out[i]=in[reIP[i]-1];
        return out;
    }
    //@description: 一轮迭代处理
    //@param L:明文左32位
    //@param R:明文右32位
    //@param key:本轮加密需要的48位密钥
    private byte[][] oneRndItrPrc(byte[] L,byte[] R, byte[] key){
        byte[][] out=new byte[2][32];
        out[0]=R;
        R=exTrans(R);
        for (int i = 0; i < 48; i++)
            R[i]=cstXOR(R[i],key[i]);
        R=rplOpr(slCmpTrans(R));
        for (int i = 0; i < 32; i++)
            R[i]=cstXOR(L[i],R[i]);
        out[1]=R;
        return out;
    }
    //@description: 扩展变换E
    //@param in: 32bit
    //@return out: 48bit
    private byte[] exTrans(byte[] in){
        byte[] out=new byte[48];
        for (int i = 0; i < 48; i++)
            out[i]=in[transE[i]-1];
        return out;
    }
    //description: 自定义异或运算
    private byte cstXOR(byte a,byte b){
        return (byte)((a & 0b1) ^ (b & 0b1));
    }
    //@description: 选择压缩变换S盒代替
    //@param in: 48bit
    //@return out: 32bit
    private byte[] slCmpTrans(byte[] in){
        int n;
        byte[] out=new byte[32];
        byte[] tmp=new byte[6];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(in,i*6,tmp,0,6);
            n=sBox[i][tmp[0]<<1 | tmp[5]][tmp[1]<<3 | tmp[2]<<2 | tmp[3]<<1 |tmp[4]];
            out[4*i]= (byte) (n>>3&0b1);
            out[4*i+1]=(byte) (n>>2&0b1);
            out[4*i+2]=(byte) (n>>1&0b1);
            out[4*i+3]=(byte) (n&0b1);
        }
        return out;
    }
    //@description: 置换运算P
    //@param in: 32bit
    //@return tmp: 32bit
    private byte[] rplOpr(byte[] in){
        byte[] out=new byte[32];
        for (int i = 0; i < 32; i++)
            out[i]=in[transP[i]-1];
        return out;
    }
    //@description: 用字节代替二进制位，将字节数组转为二进制数组
    private byte[] bytesToBits(byte[] in){
        int n;
        byte[] bits=new byte[in.length*8];
        for(int i=0;i<in.length;i++){
            n = 8*i;
            for (int j = 0; j < 8; j++)
                bits[n+j]= (byte) (in[i]>>(7-j)&0b1);
        }
        return bits;
    }
    //description: 将二进制转为字节数组
    private byte[] bitsToBytes(byte[] in){
        int n;
        byte[] out=new byte[in.length/8];
        for (int i = 0; i < out.length; i++) {
            n=8*i;
            for (int j = 0; j < 8; j++)
                out[i]+=in[n+j]<<(7-j);
        }
        return out;
    }

    //@description: 16进制字符串转字节数组
    public static byte[] hexToBytes(String hexMsg){
        if(hexMsg.length()%2==1)
            throw new NumberFormatException("输入了无法转化为完整字节数组的十六进制字符串！");
        byte[] tmp=new byte[hexMsg.length()/2];
        StringBuilder buffer=new StringBuilder(hexMsg);
        for (int i = 0; i < tmp.length; i++) {
            tmp[i]= (byte) Integer.parseInt(""+buffer.charAt(2*i)+buffer.charAt(2*i+1),16);
        }
        return tmp;
    }
    //@description: 字节数组转 16进制字符串
    public static String bytesToHex(byte[] in){
        StringBuilder builder=new StringBuilder();
        for(byte i:in)
            builder.append(String.format("%02x", i));
        return builder.toString();
    }
    //@description: 密钥翻转
    public static byte[][] reserve(byte[][] in){
        byte[][] out=new byte[16][48];
        for (int i = 0; i < 16; i++) {
            out[i]=in[15-i];
        }
        return out;
    }
}
