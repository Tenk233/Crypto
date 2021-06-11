import hashlib
import random
import gmpy2
from Crypto.Util.number import bytes_to_long

def genKey(p:int,q:int,h:int):
    g=pow(h,int((p-1)/q),p)
    x=random.randrange(0,p)
    y=pow(g,x,p)
    return g,x,y

def sign(M:bytes,k:int,p:int,q:int,g:int,x:int):
    r=pow(g,k,p)%q
    rk=gmpy2.invert(k,q)
    s=(rk* (bytes_to_long(hashlib.sha1(M).digest())+x*r)) % q
    return r,s

def verify(M:bytes,r:int,s:int,y:int,p:int,q:int,g:int):
    if not ( 0<r<q and 0<s<q ):
        return False
    w=gmpy2.invert(s,q)
    u1=(bytes_to_long(hashlib.sha1(M).digest())*w) % q
    u2=(r*w) %q
    v=(((pow(g,u1)*pow(y,u2))%p)%q)
    print('r\'=%d' % v)
    return v==r

def main():
    q,h=101,3
    p=7879 # p=78*101+1
    M = 'Hello LZU'.encode()
    g,x,y=genKey(p,q,h)
    k=random.randrange(0,q)
    r,s=sign(M,k,p,q,g,x)
    print('r=%d s=%d' % (r, s))
    res=verify(M,r,s,y,p,q,g)
    print('verify '+str(res))

if __name__=='__main__':
    main()