let f = [](...) -> { toString(...) }
let g = [](fmt, ...) -> { concat(toString(fmt), ": ", toString(...)) }
let h = [](...) -> { println(w(...)) }
let w = [](...) -> { listOf(...) }

solve f(1)
solve f(1, 2, 3)
solve f(4, 5, 6, 7, "hello", "world", 0)

solve toString()

solve g(1)
solve g(1, 2, 3)
solve g(4, 5, 6, 7, "hello", "world", 0)

solve toString()

solve h(1)
solve h(1, 2, 3)
solve h(4, 5, 6, 7, "hello", "world", 0)

solve toString()

solve listHead(w(1)) == listTail(w(1))
solve listTail(w(1, 2, 3))
solve listSize(w(4, 5, 6, 7, "hello", "world", 0))
solve join(", ", "a", "b", "C", "d", 5, 6)
