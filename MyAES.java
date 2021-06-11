/**
 * description:
 *
 * @Auther Tenk
 * @Date 2021/4/30 22:29
 */
public class MyAES {
    private final short[][] sBox = {
            { 0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76 },
            { 0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0 },
            { 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15 },
            { 0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75 },
            { 0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84 },
            { 0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF },
            { 0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8 },
            { 0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2 },
            { 0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73 },
            { 0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB },
            { 0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79 },
            { 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08 },
            { 0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A },
            { 0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E },
            { 0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF },
            { 0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16 } };
    private final short[][] invSBox = {
            { 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb },
            { 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb },
            { 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e },
            { 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25 },
            { 0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92 },
            { 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84 },
            { 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06 },
            { 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b },
            { 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73 },
            { 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e },
            { 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b },
            { 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4 },
            { 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f },
            { 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef },
            { 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61 },
            { 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d } };
    private final byte[] tool = { 1, 1, 0, 1, 1, 0, 0, 0 }; // 有限域的乘法用到的
    private final byte[] Rcon = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80, 0x1b, 0x36, 0x6c, (byte) 0xd8,
            (byte) 0xb0, 0x60 };

    /**
     * @description AES加密
     * @param msgHexStr 128位16进制明文
     * @param keyHexStr 16进制密钥
     * @return 128位16进制密文
     */
    public String encrypt(String msgHexStr, String keyHexStr) {
        byte[] msg = hexToByteArr(msgHexStr);
        if (msg.length != 16) {
            throw new NumberFormatException("明文长度一定为128bit！");
        }
        byte[][][] roundKey = keyExpansion(keyHexStr);
        byte[][] input = new byte[4][4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                input[i][j] = msg[i + 4 * j];
            }
        }
        int Nr = roundKey.length - 1;
        addRoundKey(input, roundKey[0]);
        System.out.println("---开始加密---");
        System.out.print("第0轮密钥：");
        myPrint(roundKey[0]);
        System.out.print("轮的开始：");
        myPrint(input);
        System.out.print("addRoundKey处理后：");
        myPrint(input);
        System.out.println("*****************");
        for (int i = 1; i < Nr; i++) {
            System.out.print("第" + i + "轮密钥：");
            myPrint(roundKey[i]);

            subBytes(input);
            System.out.print("SubBytes处理后：");
            myPrint(input);

            shiftRows(input);
            System.out.print("shiftRows处理后：");
            myPrint(input);

            mixColumn(input);
            System.out.print("mixColumn处理后：");
            myPrint(input);
            addRoundKey(input, roundKey[i]);

            System.out.print("addRoundKey处理后：");
            myPrint(input);
            System.out.println("*****************");
        }
        System.out.print("第" + Nr + "轮密钥：");
        myPrint(roundKey[Nr]);

        subBytes(input);
        System.out.print("SubBytes处理后：");
        myPrint(input);
        shiftRows(input);
        System.out.print("shiftRows处理后：");
        myPrint(input);
        addRoundKey(input, roundKey[Nr]);
        System.out.print("addRoundKey处理后：");
        myPrint(input);

        System.out.println("*****************");
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                msg[i + 4 * j] = input[i][j];
            }
        }
        return byteArrToHex(msg);
    }

    /**
     * @description AES解密
     * @param cipherHexStr 128位16进制密文
     * @param keyHexStr    16进制密钥
     * @return 128位16进制明文
     */
    public String decrypt(String cipherHexStr, String keyHexStr) {
        byte[] cipher = hexToByteArr(cipherHexStr);
        if (cipher.length != 16) {
            throw new NumberFormatException("密文长度一定为128bit！");
        }
        byte[][][] roundKey = keyExpansion(keyHexStr);
        byte[][] input = new byte[4][4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                input[i][j] = cipher[i + 4 * j];
            }
        }
        int Nr = roundKey.length - 1;
        System.out.println("----开始解密----");
        System.out.print("第0轮密钥：");
        myPrint(roundKey[Nr]);
        System.out.print("轮的开始：");
        myPrint(input);
        addRoundKey(input, roundKey[Nr]);
        System.out.print("addRoundKey处理后：");
        myPrint(input);
        System.out.println("*****************");
        for (int i = Nr - 1; i > 0; i--) {
            System.out.print("第" + (Nr - i) + "轮密钥：");
            myPrint(roundKey[i]);

            invShiftRows(input);
            System.out.print("shiftRows处理后：");
            myPrint(input);

            invSubBytes(input);
            System.out.print("invSubBytes处理后：");
            myPrint(input);

            addRoundKey(input, roundKey[i]);
            System.out.print("addRoundKey处理后：");
            myPrint(input);

            invMixColumn(input);
            System.out.print("invMixColumn处理后：");
            myPrint(input);
            System.out.println("*****************");
        }
        System.out.print("第" + Nr + "轮密钥：");
        myPrint(roundKey[0]);

        invShiftRows(input);
        System.out.print("invShiftRows处理后：");
        myPrint(input);

        invSubBytes(input);
        System.out.print("invSubBytes处理后：");
        myPrint(input);

        addRoundKey(input, roundKey[0]);
        System.out.print("addRoundKey处理后：");
        myPrint(input);
        System.out.println("*****************");
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                cipher[i + 4 * j] = input[i][j];
            }
        }
        return byteArrToHex(cipher);
    }

    /**
     * @description 字节代替
     * @param input 128位明文输入 byte[4][4]
     */
    private void subBytes(byte[][] input) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                input[i][j] = subByte(input[i][j]);
            }
        }
    }

    /**
     * @description 逆字节代替
     * @param input 128位密文输入 byte[4][4]
     */
    private void invSubBytes(byte[][] input) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                input[i][j] = invSubByte(input[i][j]);
            }
        }
    }

    /**
     * @description 列混淆
     */
    private void mixColumn(byte[][] input) {
        byte[] word = new byte[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                word[i] = input[i][j];
            }
            for (int i = 0; i < 4; i++) {
                input[i][j] = (byte) (byteMul((byte) 0x02, word[i]) ^ byteMul((byte) 0x03, word[(i + 1) % 4])
                        ^ word[(i + 2) % 4] ^ word[(i + 3) % 4]);
            }
        }
    }

    /**
     * @description 逆列混淆
     */
    private void invMixColumn(byte[][] input) {
        byte[] word = new byte[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                word[i] = input[i][j];
            }
            for (int i = 0; i < 4; i++) {
                input[i][j] = (byte) (byteMul((byte) 0x0e, word[i]) ^ byteMul((byte) 0x0b, word[(i + 1) % 4])
                        ^ byteMul((byte) 0x0d, word[(i + 2) % 4]) ^ byteMul((byte) 0x09, word[(i + 3) % 4]));
            }
        }
    }

    /**
     * @description 行移位运算
     */
    private void shiftRows(byte[][] input) {
        byte tmp;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < i; j++) { // 第i行循环左移i个字节
                tmp = input[i][0];
                input[i][0] = input[i][1];
                input[i][1] = input[i][2];
                input[i][2] = input[i][3];
                input[i][3] = tmp;
            }
        }
    }

    /**
     * @description 逆行移位运算
     */
    private void invShiftRows(byte[][] input) {
        byte tmp;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < i; j++) { // 第i行循环右移i个字节
                tmp = input[i][3];
                input[i][3] = input[i][2];
                input[i][2] = input[i][1];
                input[i][1] = input[i][0];
                input[i][0] = tmp;
            }
        }
    }

    /**
     * @description 轮秘钥加变换
     */
    private void addRoundKey(byte[][] input, byte[][] key) {
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                input[i][j] ^= key[i][j];
            }
        }
    }

    /**
     * @description 秘钥扩展
     */
    public byte[][][] keyExpansion(String keyHexStr) {
        byte[] keyByteArr = hexToByteArr(keyHexStr);
        int Nr, Nk;
        switch (keyByteArr.length * 8) {
            case 128:
                Nr = 10;
                Nk = 4;
                break;
            case 192:
                Nr = 12;
                Nk = 6;
                break;
            case 256:
                Nr = 14;
                Nk = 8;
                break;
            default:
                throw new NumberFormatException("密钥长度只能为128、192或者256位！");
        }
        byte[][] wordRes = new byte[4 * (Nr + 1)][4];
        for (int i = 0; i < Nk; i++) { // 扩展密钥的前Nk个字简单的从输入中复制
            System.arraycopy(keyByteArr, i * 4, wordRes[i], 0, 4);
        }
        byte[] wordTmp = new byte[4];
        for (int i = Nk; i < 4 * (Nr + 1); i++) {
            System.arraycopy(wordRes[i - 1], 0, wordTmp, 0, 4);
            if (i % Nk == 0) {
                rotWord(wordTmp);
                for (int j = 0; j < 4; j++) {
                    wordTmp[j] = subByte(wordTmp[j]);
                }
                wordTmp[0] ^= Rcon[i / Nk - 1];
            } else if (Nk == 8 && i % Nk == 4) {
                for (int j = 0; j < 4; j++) {
                    wordTmp[j] = subByte(wordTmp[j]);
                }
            }
            wordXOR(wordRes[i - Nk], wordTmp, wordRes[i]);
        }
        byte[][][] res = new byte[Nr + 1][4][4];
        for (int k = 0; k < Nr + 1; k++) { // 将字密钥分为状态密钥
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    res[k][i][j] = wordRes[k * 4 + j][i];
                }
            }
        }
        return res;
    }

    /**
     * @description 字的异或，src1 ^ src2 -> des
     */
    private void wordXOR(byte[] src1, byte[] src2, byte[] des) {
        for (int i = 0; i < 4; i++) {
            des[i] = (byte) (src1[i] ^ src2[i]);
        }
    }

    /**
     * @description 以字节为单位的对字的循环向上移位
     */
    private void rotWord(byte[] word) {
        byte tmp = word[0];
        word[0] = word[1];
        word[1] = word[2];
        word[2] = word[3];
        word[3] = tmp;
    }

    /**
     * @description 有限域上的乘法运算
     */
    private byte byteMul(byte a, byte b) {
        byte[] x = byteToBits(a);
        byte[] y = byteToBits(b);
        byte flag;
        byte[] res = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] tmp = new byte[8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(y, 0, tmp, 0, 8);
            if (x[i] == 1) {
                for (int j = 0; j < i; j++) {
                    flag = tmp[7];
                    System.arraycopy(tmp, 0, tmp, 1, 7);
                    tmp[0] = 0;
                    if (flag == 1) {
                        bitsXOR(tmp, tool);
                    }
                }
                bitsXOR(res, tmp);
            }
        }
        return bitsToByte(res);
    }

    /**
     * @description 有限域上的字节的加法运算
     * @param des 运算的结果存在des中
     */
    private void bitsXOR(byte[] des, byte[] src) {
        for (int i = 0; i < 8; i++) {
            des[i] = (byte) (des[i] & 0b1 ^ src[i] & 0b1);
        }
    }

    /**
     * @description s盒代替
     */
    private byte subByte(byte in) {
        return (byte) sBox[(in >> 4) & 0xF][in & 0xF];
    }

    private byte invSubByte(byte in) {
        return (byte) invSBox[(in >> 4) & 0xF][in & 0xF];
    }

    /**
     * @description 用字节代替二进制位，将字节转为二进制数组
     */
    private byte[] byteToBits(byte in) {
        byte[] bits = new byte[8];
        for (int i = 0; i < 8; i++) {
            bits[i] = (byte) (in >> i & 0b1);
        }
        return bits;
    }

    /**
     * @description 将二进制位数组转为字节
     */
    private byte bitsToByte(byte[] bits) {
        byte res = 0;
        for (int i = 0; i < 8; i++) {
            res += (bits[i] << i) & 0xFF;
        }
        return res;
    }

    /**
     * @description 打印一个状态（4个字）
     */
    public void myPrint(byte[][] input) {
        byte[] tmp = new byte[4];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                tmp[i] = input[i][j];
            }
            System.out.print(byteArrToHex(tmp));
        }
        System.out.println();
    }

    /**
     * @description 16进制字符串转字节数组
     */
    public byte[] hexToByteArr(String hexMsg) {
        if (hexMsg.length() % 2 == 1)
            throw new NumberFormatException("输入了无法转化为完整字节数组的十六进制字符串！");
        byte[] tmp = new byte[hexMsg.length() / 2];
        StringBuilder buffer = new StringBuilder(hexMsg);
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (byte) Integer.parseInt("" + buffer.charAt(2 * i) + buffer.charAt(2 * i + 1), 16);
        }
        return tmp;
    }

    /**
     * @description 字节数组转 16进制字符串
     */
    public String byteArrToHex(byte[] in) {
        StringBuilder builder = new StringBuilder();
        for (byte i : in)
            builder.append(String.format("%02x", i));
        return builder.toString();
    }

    /**
     * @description 测试用例
     */
    public void main() {
        String msgHexStr = "3243f6a8885a308d313198a2e0370734";
        String keyHexStr = "2b7e151628aed2a6abf7158809cf4f3c";
        System.out.println("明文：" + msgHexStr);
        System.out.println("密钥：" + keyHexStr);
        String cipher = encrypt(msgHexStr, keyHexStr);
        System.out.println("密文：" + cipher);
        System.out.println("\n++++++++++++++++\n");
        System.out.println("解密明文：" + decrypt(cipher, keyHexStr));
    }

    public static void main(String[] args) {
        MyAES aes = new MyAES();
        aes.main();
    }
}
