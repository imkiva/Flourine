let fact = [](a) -> { a == 1 ? 1 : a * fact(a - 1) }
solve print(fact(5))
