import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
/**
 * description:
 * SMS4 encryption and decryption algorithm
 * @Auther Tenk
 * @Date 2021/4/9 11:14
 */
public class MySms4 {
    public static final int DECRYPT = 1;
    public static final int ENCRYPT = 0;
    private final short[] sBox = {  // 16进制表示S盒
            0xd6, 0x90, 0xe9, 0xfe, 0xcc, 0xe1, 0x3d, 0xb7, 0x16, 0xb6, 0x14, 0xc2, 0x28, 0xfb, 0x2c, 0x05,
            0x2b, 0x67, 0x9a, 0x76, 0x2a, 0xbe, 0x04, 0xc3, 0xaa, 0x44, 0x13, 0x26, 0x49, 0x86, 0x06, 0x99,
            0x9c, 0x42, 0x50, 0xf4, 0x91, 0xef, 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43, 0xed, 0xcf, 0xac, 0x62,
            0xe4, 0xb3, 0x1c, 0xa9, 0xc9, 0x08, 0xe8, 0x95, 0x80, 0xdf, 0x94, 0xfa, 0x75, 0x8f, 0x3f, 0xa6,
            0x47, 0x07, 0xa7, 0xfc, 0xf3, 0x73, 0x17, 0xba, 0x83, 0x59, 0x3c, 0x19, 0xe6, 0x85, 0x4f, 0xa8,
            0x68, 0x6b, 0x81, 0xb2, 0x71, 0x64, 0xda, 0x8b, 0xf8, 0xeb, 0x0f, 0x4b, 0x70, 0x56, 0x9d, 0x35,
            0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, 0xd1, 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, 0x87,
            0xd4, 0x00, 0x46, 0x57, 0x9f, 0xd3, 0x27, 0x52, 0x4c, 0x36, 0x02, 0xe7, 0xa0, 0xc4, 0xc8, 0x9e,
            0xea, 0xbf, 0x8a, 0xd2, 0x40, 0xc7, 0x38, 0xb5, 0xa3, 0xf7, 0xf2, 0xce, 0xf9, 0x61, 0x15, 0xa1,
            0xe0, 0xae, 0x5d, 0xa4, 0x9b, 0x34, 0x1a, 0x55, 0xad, 0x93, 0x32, 0x30, 0xf5, 0x8c, 0xb1, 0xe3,
            0x1d, 0xf6, 0xe2, 0x2e, 0x82, 0x66, 0xca, 0x60, 0xc0, 0x29, 0x23, 0xab, 0x0d, 0x53, 0x4e, 0x6f,
            0xd5, 0xdb, 0x37, 0x45, 0xde, 0xfd, 0x8e, 0x2f, 0x03, 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b, 0x51,
            0x8d, 0x1b, 0xaf, 0x92, 0xbb, 0xdd, 0xbc, 0x7f, 0x11, 0xd9, 0x5c, 0x41, 0x1f, 0x10, 0x5a, 0xd8,
            0x0a, 0xc1, 0x31, 0x88, 0xa5, 0xcd, 0x7b, 0xbd, 0x2d, 0x74, 0xd0, 0x12, 0xb8, 0xe5, 0xb4, 0xb0,
            0x89, 0x69, 0x97, 0x4a, 0x0c, 0x96, 0x77, 0x7e, 0x65, 0xb9, 0xf1, 0x09, 0xc5, 0x6e, 0xc6, 0x84,
            0x18, 0xf0, 0x7d, 0xec, 0x3a, 0xdc, 0x4d, 0x20, 0x79, 0xee, 0x5f, 0x3e, 0xd7, 0xcb, 0x39, 0x48
    };
    private final int[] FK = {0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc}; // 系统参数取值
    private final int[] CK = { // 固定参数CK，CKij= (4i+j)×7（mod 256） ，i=0,1,2…31,j=0,1,…3
            0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
            0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
            0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
            0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
            0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
            0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
            0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
            0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279
    };

    public MySms4(){}
    // 解密
    public byte[] decrypt(byte[] cipher, int[] rrk){
        return crypt(cipher,rrk);
    }
    // 加密
    public byte[] encrypt(byte[] msg, int[] rk){
        return crypt(msg,rk);
    }

    private byte[] crypt(byte[] input, int[] rk){ // 加密解密
        if(input.length==0 || rk.length==0) return null;
        byte[] tmp= padding(input,false);
        byte[] tmp2=new byte[16];
        for(int i=0;i<tmp.length;i+=16){
            System.arraycopy(tmp,i,tmp2,0,16);
            tmp2=crypt1Sg(tmp2,rk);
            System.arraycopy(tmp2,0,tmp,i,16);
        }
        return tmp;
    }

    // 密钥扩展，加密密钥 key一定为16byte
    public int[] extKey(byte[] key, int cryptFlag){
        int[] MK= inputDiv(padding(key,true)); // 加密密钥分组，由4个32位的字组成
        int[] K=new int[36];
        int[] rk=new int[32]; // 轮密钥，32个字
        for (int i=0;i<4;i++)
            K[i]=MK[i]^FK[i]; // (K0,K1,K2,K3)=(MK0^FK0,MK1^FK1,MK2^MK2,MK3^FK3)
        // 生成轮秘钥
        for(int i=0;i<32;i++){
            K[i+4]=K[i] ^ nwLTr(tau(K[i+1]^K[i+2]^K[i+3]^CK[i])); // 轮函数F
            rk[i]=K[i+4];
        }
        if(cryptFlag==DECRYPT) {
            int tmp;
            for(int i=0;i<16;i++){
                tmp=rk[i];
                rk[i]=rk[31-i];
                rk[31-i]=tmp;
            }
        }else if(cryptFlag!=ENCRYPT)
            rk=null;
        return rk;
    }

    // 加解密input<-(X0,X1,X2,X3), rk<-int[32], X为一个字(32bit)
    private byte[] crypt1Sg(byte[] input,int[] rk){ // 加密4个字（128bit）
        byte[] output=new byte[32];
        int[] X=new int[36];

        System.arraycopy(inputDiv(input),0,X,0,4);
        for (int i = 0; i < 32; i++) { // 进行32轮的加密变换操作
            X[i+4]=X[i]^lTr(tau(X[i+1]^X[i+2]^X[i+3]^rk[i])); // 轮函数F
        }
        for (int i = 0; i < 4; i ++) { // 反序变换
            output[4*i] = (byte) (X[35 - i] >>> 24);
            output[4*i + 1] = (byte) (X[35 - i] >>> 16);
            output[4*i + 2] = (byte) (X[35 - i] >>> 8);
            output[4*i + 3] = (byte) (X[35 - i]);
        }
        return output;
    }

    private byte[] padding(byte[] input, boolean flag){ // flag为true时表示补全密钥，反之表示补全明文
        byte[] output;
        if(flag){
            if (input.length==16) return input;
            output=new byte[16];
            for(int i=0;i<16&&i<input.length;i++)
                output[i]=input[i];
            for(int i=input.length;i<16;i++)
                output[i]=0;  //不够16byte补零
        }else{
            if(input.length%16==0) return input;
            output=new byte[input.length/16*16+16];
            System.arraycopy(input, 0, output, 0, input.length);
            for(int i=input.length;i<output.length;i++)
                output[i]=0; //不够16byte补零
        }
        return output;
    }
    // 16bit->4字
    private int[] inputDiv(byte[] input){
        int[] x = new int[4];
        for (int i = 0; i < 4; i++) {
            x[i] = (input[4 * i] & 0xff)<<24
                    | (input[1 + 4 * i] & 0xff)<<16
                    | (input[2 + 4 * i] & 0xff)<<8
                    | (input[3 + 4 * i] & 0xff);
        }
        return x;
    }
    private int lTr(int src) {  // 轮函数使用的线性变换 L
        return src ^ rot(src, 2) ^ rot(src, 10) ^ rot(src, 18) ^ rot(src, 24);
    }
    private int nwLTr(int src) { //密钥扩展时使用的线性变换 L'
        return src ^ rot(src, 13) ^ rot(src, 23);
    }
    private int rot(int src, int n) { // 将src循环左移y位
        return src << n | src >>> (32 - n);
    }
    private int tau(int a) { // 非线性变换 Tau(τ)
        return (sBox[a >>> 24 & 0xFF] & 0xFF) << 24
                | (sBox[a >>> 16 & 0xFF] & 0xFF) << 16
                | (sBox[a >>> 8 & 0xFF] & 0xFF) << 8
                | (sBox[a & 0xFF] & 0xFF);
    }

    public static void main(String[] args) {
        MySms4 sms4 = new MySms4();
        byte[] msg = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(msg));
        int[] key = sms4.extKey("peng bo".getBytes(StandardCharsets.UTF_8), MySms4.ENCRYPT);
        int[] rKey = sms4.extKey("peng bo".getBytes(StandardCharsets.UTF_8), MySms4.DECRYPT);
        byte[] t = sms4.encrypt(msg, key);
        System.out.println(new String(t));
        String e = new BASE64Encoder().encode(t);
        try {
            byte[] m = new BASE64Decoder().decodeBuffer(e);
            m=sms4.decrypt(m, rKey);
            System.out.println(Arrays.toString(m));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
