package ru.arinae_va.lensa.utils.ext

fun <E>List<E?>.appendListToNullableN(
    n: Int = 1,
    emptyValue: E? = null,
): List<E?> {
    return if (this.size >= n) this.take(n)
    else {
        val mL = this.toMutableList()
        repeat(n - this.size) { mL.add(emptyValue) }
        mL
    }
}

fun <E>List<E>.appendListToN(
    n: Int = 1,
    emptyValue: E,
): List<E> {
    return if (this.size >= n) this.take(n)
    else {
        val mL = this.toMutableList()
        repeat(n - this.size) { mL.add(emptyValue) }
        mL
    }
}