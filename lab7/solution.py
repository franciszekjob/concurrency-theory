from concurrent.futures import ThreadPoolExecutor


def read_input_file(file_name):
    """Wczytuje wymiar i macierz rozszerzoną M z pliku."""
    with open(file_name, 'r') as file:
        dim = int(file.readline())

        matrix = []
        for i in range(dim):
            matrix.append(list(map(float, file.readline().split())))

        rhs = list(map(float, file.readline().split()))
        for i in range(dim):
            matrix[i].append(rhs[i])

        return dim, matrix


def compute_A(matrix, k, i):
    """Operacja A_k_i: Obliczenie współczynnika skalującego m[k][i]."""
    return matrix[k][i] / matrix[i][i]


def compute_B(matrix, m, i, k, j):
    """Operacja B_k_j_i: Obliczenie wartości pośredniej n[k][j][i]."""
    return matrix[i][j] * m


def compute_C(matrix, k, j, n):
    """Operacja C_k_j_i: Aktualizacja wartości macierzy."""
    matrix[k][j] -= n


def execute_FA_parallel(matrix, pivot_row, dim):
    """Klasa Foaty FA: Obliczanie współczynników skalujących z równoległością."""
    m = [0] * dim

    def compute(k):
        m[k] = compute_A(matrix, k, pivot_row)

    with ThreadPoolExecutor() as executor:
        executor.map(compute, range(pivot_row + 1, dim))

    return m


def execute_FB_parallel(matrix, m, pivot_row, dim):
    """Klasa Foaty FB: Obliczanie wartości pośrednich z równoległością."""
    n = [[0] * (dim + 1) for _ in range(dim)]

    def compute(k, j):
        n[k][j] = compute_B(matrix, m[k], pivot_row, k, j)

    with ThreadPoolExecutor() as executor:
        # Tworzymy pary (k, j) dla każdej kombinacji wierszy i kolumn
        executor.map(lambda args: compute(*args),
                     ((k, j) for k in range(pivot_row + 1, dim)
                              for j in range(pivot_row + 1, dim + 1)))

    return n


def execute_FC_parallel(matrix, n, pivot_row, dim):
    """Klasa Foaty FC: Aktualizacja wartości macierzy z równoległością."""
    def update(k, j):
        compute_C(matrix, k, j, n[k][j])

    with ThreadPoolExecutor() as executor:
        # Tworzymy pary (k, j) dla każdej kombinacji wierszy i kolumn
        executor.map(lambda args: update(*args),
                     ((k, j) for k in range(pivot_row + 1, dim)
                              for j in range(pivot_row + 1, dim + 1)))


def gauss_elimination_parallel(matrix, dim):
    """Eliminacja Gaussa z równoległością: Redukuje macierz do postaci trójkątnej górnej."""
    for pivot_row in range(dim - 1):
        # Klasa FA: Obliczamy współczynniki skalujące
        m = execute_FA_parallel(matrix, pivot_row, dim)

        # Klasa FB: Obliczamy wartości pośrednie
        n = execute_FB_parallel(matrix, m, pivot_row, dim)

        # Klasa FC: Aktualizujemy macierz
        execute_FC_parallel(matrix, n, pivot_row, dim)


def backward_substitution(matrix, dim):
    """Rozwiązuje układ równań po eliminacji Gaussa."""

    # Tablica wyników
    x = [0.0] * dim

    # Iterujemy od ostatniego wiersza do pierwszego
    for i in range(dim - 1, -1, -1):
        # Rozpoczynamy od wyrazu wolnego (ostatnia kolumna)
        rhs = matrix[i][dim]

        # Odejmujemy iloczyny elementów macierzy i wcześniejszych wyników x
        for j in range(i + 1, dim):
            rhs -= matrix[i][j] * x[j]

        # Dzielimy przez współczynnik na głównej przekątnej
        x[i] = rhs / matrix[i][i]

    return x


def gauss_solver(input_file, output_file):
    # Wczytujemy macierz rozszerzoną M
    dim, matrix = read_input_file(input_file)

    # Eliminacja Gaussa
    gauss_elimination_parallel(matrix, dim)

    # Rozwiązanie układu
    solution = backward_substitution(matrix, dim)

    write_output_file(output_file, dim, solution)


def write_output_file(output_file, dim, solution):
    """Zapisuje wynik eliminacji Gaussa do pliku wyjściowego."""
    with open(output_file, 'w') as file:
        file.write(f"{dim}\n")
        print(f"{dim}\n")

        for i in range(dim):
            row = [f"{1.0 if i == j else 0.0:.1f}" for j in range(dim)]
            file.write(" ".join(row) + "\n")
            print(" ".join(row) + "\n")

        file.write(" ".join([f"{x}" for x in solution]) + "\n")
        print(" ".join([f"{x}" for x in solution]) + "\n")


if __name__ == "__main__":
    test_cases = (
        ("input.txt", "output.txt"),
        ("input2.txt", "output2.txt"),
    )

    for input_file, output_file in test_cases:
        gauss_solver(input_file, output_file)
