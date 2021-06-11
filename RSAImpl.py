import random
import gmpy2
from Crypto.Util.number import long_to_bytes
from Crypto.Util.number import bytes_to_long

# 得到key_size位的大素数
def get_prime(key_size:int)->int:
    while True:
        num = random.randrange(pow(2,key_size-1), pow(2,key_size))
        if gmpy2.is_prime(num): #素性检验
            return num

#将字节数组转为16进制字符串
def bytes2hex(byte_arr:bytes)->str:
    res=''
    for i in byte_arr:
        res += '%02x' % i
    return res

# 加解密实现
def main():
    # 初始化密钥对
    p=gmpy2.mpz(get_prime(128))
    q=gmpy2.mpz(get_prime(132))
    n=p*q
    e=gmpy2.mpz(get_prime(16))
    d=gmpy2.invert(e,(p-1)*(q-1))
    print('公钥：('+bytes2hex(long_to_bytes(e))+','+bytes2hex(long_to_bytes(n))+')')
    print('私钥：('+bytes2hex(long_to_bytes(d))+','+bytes2hex(long_to_bytes(n))+')')
    #输入明文
    msg=input("请输入明文:")
    print('初始明文:'+bytes2hex(msg.encode('UTF-8')))
    m=bytes_to_long(msg.encode('UTF-8'))
    # 加密
    em=pow(m,e,n)
    print('加密密文:'+bytes2hex(long_to_bytes(em)))
    # 解密
    dm=pow(em,d,n)
    print('解密明文:'+bytes2hex(long_to_bytes(dm)))


if __name__=='__main__':
    main()
