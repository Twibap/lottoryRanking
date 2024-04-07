package org.example

import org.jsoup.Jsoup

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
fun main() {
    val targetUrl = "https://dhlottery.co.kr/store.do?method=topStore&pageGubun=L645"

    val action = "/store.do?method=topStore&pageGubun=L645"

    val method = "topStore"
    val nowPage = 1
    val gameNo = 5133
//    val drwNo = 1114

    val stores = HashMap<Int, Store>()

    for (drwNo in 1000 .. 1114) {
        val doc = Jsoup.connect(targetUrl)
            .data("method", method)
            .data("nowPage", nowPage.toString())
            .data("drwNo", drwNo.toString())
            .post()

        val table = doc.body().getElementsByClass("tbl_data tbl_data_col")

        val rows = table.first()?.select("tr")

        rows?.forEach {

            val cols = it.select("td")

            val regex = "\\d+".toRegex()

            val link = cols.last()?.select("a")?.first()
            val id = link?.attr("onClick")?.let { it1 -> regex.find(it1)?.value?.toInt() }

            id?.let { rltlId ->
                val store = stores[rltlId]
                    ?.apply {
                        this.countOfFirst.add(drwNo)
                    }
                    ?: Store(
                        cols[1].text(),
                        cols[3].text(),
                        rltlId,
                    ).apply {
                        this.countOfFirst.add(drwNo)
                        stores[rltlId] = this
                    }
                println("Store : $store get winner $drwNo and winner count is ${store.countOfFirst.size}")
            }
        }

        println("drwNo $drwNo is done")

    }

    stores.values.sortedByDescending { it.countOfFirst.size }.forEach {
        println("$it wins ${it.countOfFirst.size}")
    }

}