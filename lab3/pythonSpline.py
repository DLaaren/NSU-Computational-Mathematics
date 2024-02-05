import numpy as np
import matplotlib.pyplot as plt
from scipy.interpolate import CubicSpline
 
x = np.array([-1, -0.6, -0.2, 0.2, 0.6, 1])
y = np.abs(x)
 
spline = CubicSpline(x, y)
 
# График функции и интерполяции сплайнами
x_values = np.linspace(min(x), max(x), 100)
y_values = [spline(x) for x in x_values]
 
plt.plot(x, y, label='|x|', marker='o')
plt.plot(x_values, y_values, label='Spline Interpolation', linestyle='--')
plt.legend()
plt.xlabel('x')
plt.ylabel('y')
plt.title('PySpline Interpolation')
plt.grid()
plt.show()