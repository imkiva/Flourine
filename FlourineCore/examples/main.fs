let fact = [](a) -> { a == 1 ? 1 : a * fact(a - 1) }
let r = fact(5)
solve concat("factorial number of 5 is ", fact(5), " (should be 120)")

let A = (1, 2, 3)
let B = (4, 5, 6)
solve distance
solve distance(A, B)
