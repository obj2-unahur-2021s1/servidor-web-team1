package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("Un servidor web") {
    val servidor1=servidorWeb()

    val url1=URL("http","documents/index","jpg")
    val url2=URL("https","desktops/download","jpg")

    val pedido1=Pedido("1.10.11:200",url1,LocalDateTime.of(2021,6,20, 1,30, 0))
    val pedido2=Pedido("2.1.10:2010",url2,LocalDateTime.of(2021,4,20,5,2,10))

    it("Requerimiento 1 sin modulo") {
      servidor1.cumpleConElProtocolo(pedido1).shouldBe(Respuesta(CodigoHttp.OK, "", 10, pedido1))

      servidor1.cumpleConElProtocolo(pedido2).shouldBe(Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido2))
    }
    it("Requerimiento 2 con modulo") {
      val servidor2 = servidorWeb()
      servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido1))
      servidor2.agregarModulo(Imagen)
      servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK,"Funciona",5,pedido1))
    }
      it("Requerimiento 3 con analizadores ips Sopechosas")
      {
        val analizador1=Analizadores(4, listOf("12312", "123124334") )
        val servidor3 = servidorWeb()
        servidor3.agregarModulo(Imagen)
          servidor3.agregarAnalizador(analizador1)
          servidor3.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK, "Funciona", 5, pedido1))
          analizador1.cantidadDeRespuestasDemoradasDelModulo(Imagen).shouldBe(1)
      }
      it("Requerimiento 3 con analizadores estadisticas")
      {

      }
  }
})

