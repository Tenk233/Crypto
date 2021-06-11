# 模平方重复计算
# result = pow(b,n,m)
def mrsc(b: int, n: int, m: int) -> int:
    a = 1
    binStr = ''.join(reversed(bin(n)[2:]))  # 将幂指数写成二进制形式
    for i in binStr:  # 根据模平方重复计算法，依次计算
        if i == '1':  # 判断 n[i]是否为一
            a = (a * b) % m
        else:  # n[i] =='0'
            pass  # a' = a
        b = (b * b) % m  # b' = (b*b)mod m
    return a


def main():
    b = 16
    n = 15
    m = 4731
    print('pow(%d,%d,%d)=' % (b, n, m) + str(mrsc(b, n, m)))  # res = 4339
    print(pow(b, n, m) == mrsc(b, n, m))  # 调用python自带模运算检验结果正确性


if __name__ == '__main__':
    main()
