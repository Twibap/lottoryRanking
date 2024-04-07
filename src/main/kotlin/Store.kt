package org.example

data class Store(
    val name: String,
    val address: String,
    val rltlId: Int,
) {

    val countOfFirst = ArrayList<Int>()
    val countOfSecond = ArrayList<Int>()

}
