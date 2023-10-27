import numpy as np
import matplotlib.pyplot as plt
 
 
def cubic_spline_interpolation(x, y):
    n = len(x)
    h = [x[i] - x[i - 1] for i in range(1, n)]
 
    # Решение системы уравнений для определения коэффициентов полиномов
    A = np.zeros((n, n))
    B = np.zeros(n)
 
    for i in range(1, n - 1):
        A[i, i - 1] = h[i - 1]
        A[i, i] = 2 * (h[i - 1] + h[i])
        A[i, i + 1] = h[i]
        B[i] = 6 * ((y[i + 1] - y[i]) / h[i] - (y[i] - y[i - 1]) / h[i - 1])
 
    # Задание граничных условий (например, естественный сплайн)
    A[0, 0] = 1
    A[n - 1, n - 1] = 1
 
    # Решение системы линейных уравнений для определения вторых производных
    second_derivatives = np.linalg.solve(A, B)
 
    # Вычисление коэффициентов полиномов
    coefficients = []
    for i in range(n - 1):
        a = (second_derivatives[i + 1] - second_derivatives[i]) / (6 * h[i])
        b = second_derivatives[i] / 2
        c = (y[i + 1] - y[i]) / h[i] - (2 * h[i] * second_derivatives[i] + h[i] * second_derivatives[i + 1]) / 6
        d = y[i]
        coefficients.append([a, b, c, d])
 
    def interpolate_spline(t):
        for i in range(n - 1):
            if x[i] <= t <= x[i + 1]:
                a, b, c, d = coefficients[i]
                delta_x = t - x[i]
                return a * delta_x ** 3 + b * delta_x ** 2 + c * delta_x + d
 
    return interpolate_spline
 
 
# Example usage
x = np.array([-1, -0.6, -0.2, 0.2, 0.6, 1])
y = np.abs(x)
 
interpolating_function = cubic_spline_interpolation(x, y)
 
# График функции и интерполяции сплайнами
x_values = np.linspace(min(x), max(x), 100)
y_values = [interpolating_function(x) for x in x_values]
 
plt.plot(x, y, label='defined function', marker='o')
plt.plot(x_values, y_values, label='Spline Interpolation', linestyle='--')
plt.legend()
plt.xlabel('x')
plt.ylabel('y')
plt.title('Spline Interpolation')
plt.grid()
plt.show()