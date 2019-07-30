let fact = [](a) -> { a == 1 ? 1 : a * fact(a - 1) }
let r = fact(5)
solve concat("factorial number of 5 is ", fact(5), " (should be 120)")
