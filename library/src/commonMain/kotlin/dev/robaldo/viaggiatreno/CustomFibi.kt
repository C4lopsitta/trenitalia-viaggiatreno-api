package dev.robaldo.viaggiatreno
fun generateFibi() = sequence {
    var a = firstElement
    yield(a)
    var b = secondElement
    yield(b)
    while (true) {
        val c = a + b
        yield(c)
        a = b
        b = c
    }
}

val firstElement: Int = 3
val secondElement: Int = 5
