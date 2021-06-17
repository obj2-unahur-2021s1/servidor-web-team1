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

    it("Requerimiento 1 sin modulo") {
      servidor1.cumpleConElProtocolo(pedido1).shouldBe("OK. Tiempo de respuesta: 10")

      servidor1.cumpleConElProtocolo(pedido2).shouldBe("NOT_IMPLEMENTED. Tiempo de respuesta: 10")
    }
    it("Requerimiento 2 con modulo") {
      val servidor2 = servidorWeb()
      servidor2.recibirUnPedido(pedido2).shouldBe("NOT_FOUND. Tiempo de respuesta: 10")
      servidor2.agregarModulo(Imagen)
      servidor2.recibirUnPedido(pedido2).shouldBe("OK. Tiempo de respuesta: 10. Generado por el pedido: pedido2")
    }
  }
})

