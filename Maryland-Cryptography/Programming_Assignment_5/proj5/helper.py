def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    else:
        g, y, x = egcd(b % a, a)
        return (g, x - (b // a) * y, y)

def modinv(a, m):
    gcd, x, y = egcd(a, m)
    if gcd != 1:
        return None  # modular inverse does not exist
    else:
        return x % m

def ascii_to_int(m):
    val = ""
    for x in m:
        val += hex(ord(x))[2:]
    return int("0x" + val,16)

def ascii_to_bin(m):
    val = ""
    for x in m:
        val += bin(ord(x))[2:].zfill(8)
    return val