import numpy as np
import matplotlib.pyplot as plt
 
 
def newton_interpolation(x, y):
    n = len(x)
    coefficients = np.zeros(n)
    for j in range(n):
        coefficients[j] = y[j]
        for i in range(j):
            coefficients[j] = (coefficients[j] - coefficients[i]) / (x[j] - x[i])
 
    def polynomial(t):
        result = coefficients[0]
        product = 1
        for i in range(1, n):
            product *= t - x[i - 1]
            result += coefficients[i] * product
        return result
 
    return polynomial
 
 
#x = np.array([-1, -0.6, -0.2, 0.2, 0.6, 1])
x = np.array([-1, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1])
y = np.abs(x)
 
interpolating_polynomial = newton_interpolation(x, y)
 
# Generate points for the plot
x_values = np.linspace(min(x), max(x), 100)
y_values = [interpolating_polynomial(x_val) for x_val in x_values]
 
# Plot the original function and the interpolating polynomial
plt.plot(x, y, label='defined function', marker='o')
plt.plot(x_values, y_values, label='Interpolating Polynomial', linestyle='--')
plt.legend()
plt.xlabel('x')
plt.ylabel('y')
plt.title('Interpolation with Newton Polynomial')
plt.grid()
plt.show()