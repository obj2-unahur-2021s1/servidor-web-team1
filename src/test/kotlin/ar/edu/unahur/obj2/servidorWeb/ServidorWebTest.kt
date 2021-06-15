package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("Un servidor web") {
    val url1=URL("http","documents/index","html")
    val pedido1=Pedido("1.10.11:200",url1,LocalDateTime.of(2000,6,20, 1,30, 0))
    val pedido2=Pedi

    ServidorWeb.cumpleConElProtocolo(pedido1).shouldBe(200)

    ServicioWeb.cumpleConElProtocolo(pedido2).shouldBe(501)


  }
})

