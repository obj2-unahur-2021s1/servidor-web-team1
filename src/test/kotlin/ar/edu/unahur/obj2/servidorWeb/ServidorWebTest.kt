package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("Un servidor web") {
      val servidor1=servidorWeb()
    val url1=URL("http","documents/index","html")
      val url2=URL("https","desktops/download","jpg")
    val pedido1=Pedido("1.10.11:200",url1,LocalDateTime.of(2021,6,20, 1,30, 0))
    val pedido2=Pedido("2.1.10:2010",url2,LocalDateTime.of(2021,4,20,5,2,10))

      //Requerimiento1
    servidor1.cumpleConElProtocolo(pedido1).shouldBe(CodigoHttp.OK)

    servidor1.cumpleConElProtocolo(pedido2).shouldBe(CodigoHttp.NOT_IMPLEMENTED)

      //Requerimiento2
      val servidor2=servidorWeb(Imagen)
    servidor2.cumpleConElProtocolo(pedido1).shouldBe(CodigoHttp.OK)
      servidor1.cumpleConElProtocolo(pedido2).shouldBe(CodigoHttp.NOT_IMPLEMENTED)

  }
})

