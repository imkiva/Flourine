// def y_combinator(f):
//       return (lambda u: u(u)) ( lambda x: f(lambda *args: x(x)(*args)) )

let Y = [](f) -> { ([](u)->{u(u)}) ( [](x) -> { f([](...) -> {x(x)(...)}) } ) }

let _ = Y([](fact) -> { [](n) -> {n * fact(n - 1)}})

solve _(5)

