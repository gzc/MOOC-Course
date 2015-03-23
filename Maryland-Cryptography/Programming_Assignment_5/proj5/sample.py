from oracle import *
from helper import *

n = 119077393994976313358209514872004186781083638474007212865571534799455802984783764695504518716476645854434703350542987348935664430222174597252144205891641172082602942313168180100366024600206994820541840725743590501646516068078269875871068596540116450747659687492528762004294694507524718065820838211568885027869

Oracle_Connect()

msg = "Crypto is hard --- even schemes that look complex can be broken"

m = ascii_to_int(msg)

# All other arbitrary messages <= 504 bits should be accepted by the oracle
sigma1 = Sign(1)

m1 = 2
m2 = m/2

sigmal = Sign(m1)
sigmar = Sign(m2)
sigma = (sigmal * sigmar * modinv(sigma1, n) )%n

print Verify(m,sigma)

print hex(sigma)

Oracle_Disconnect()
