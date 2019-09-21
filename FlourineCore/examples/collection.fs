let myJoin = [](delimiter, head, ...) -> {
    listSize(listOf(...)) == 0
        ? toString(head)
        : concat(head, delimiter, myJoin(delimiter, ...))
}

solve   join(", ", 1, 2, 3)
solve myJoin(", ", 1, 2, 3)
