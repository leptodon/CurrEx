package ru.cactus.currex.data.response_models

import org.simpleframework.xml.*

@Root(strict = false)
data class ValCurs(
    @field:ElementList(name = "Valute", inline = true)
    var valuteList: List<Valute>? = null
)

