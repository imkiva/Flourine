let F = [](x) -> {
    x == 1 ? 0.5 : 0.5 * (1 + F(x - 1))
}

solve F(120)