import sys
import numpy as np
import matplotlib.pyplot as plt

a = float(sys.argv[1])
b = float(sys.argv[2])
c = float(sys.argv[3])

x = np.arange(-10, 10, 0.1)
plt.plot(x, x**3 + a*x**2 + b*x + c)
plt.axvline(x=0, c='black')
plt.axhline(y=0, c='black')
plt.show()

