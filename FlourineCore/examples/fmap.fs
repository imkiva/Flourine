solve listAdd(listOf(0, 0, 0), 1)
solve listAdd(1, listOf(0, 0, 0))

let fmap = [](f, head, ...) -> {
    listSize(listOf(...)) == 0
            ? listOf(f(head))
            : listAdd(f(head), fmap(f, ...))
}

let square = [](x) -> { x * x }

solve fmap(square, 1, 2, 3, 4, 5)

