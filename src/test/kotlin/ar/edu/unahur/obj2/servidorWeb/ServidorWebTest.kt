package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.awt.Image
import java.time.LocalDate
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("Un servidor web") {
      val servidor1=ServidorWeb()
      val servidor4=ServidorWeb() //

      val url1=URL("http","documents/index","jpg")
      val url2=URL("https","desktops/download","jpg")
      val url5=URL("http","desktops/objetos2","docs")
      val url6=URL("htttp","desktop/unaHur","html")

      val pedido1=Pedido("1.10.11:200",url1,LocalDateTime.of(2021,6,20, 1,30))
      val pedido2=Pedido("2.1.10:2010",url2,LocalDateTime.of(2021,4,20,5,2))
      val pedido5=Pedido("20.3.24:2650",url5,LocalDateTime.of(2021,3,4,20,5))
      val pedido6=Pedido("30.36.45:4020",url6,LocalDateTime.of(2021,6,8,4,6))

      val analizador2= Analizadores(5, listOf("2.3.2:210","5.2.7:25000"))
      val analizador3=Analizadores(6, listOf("3.6.8:9888","65.54.7:1233"))

      it("Requerimiento 1 sin modulo") {
        servidor1.cumpleConElProtocolo(pedido1).shouldBe(Respuesta(CodigoHttp.OK, "", 10, pedido1))

        servidor1.cumpleConElProtocolo(pedido2).shouldBe(Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido2))
      }
      it("Requerimiento 2 con modulo") {
        val servidor2 = ServidorWeb()
        servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido1))
        servidor2.agregarModulo(Imagen)
        servidor2.recibirUnPedido(pedido5).shouldBe(Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido5))
        servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK,"Esto es una imagen",3,pedido1))
      }
      it("Requerimiento 3 con analizadores -> cantidad de respuestas demoradas ") {
        val analizador1=Analizadores(2, listOf("12312", "123124334") )
        val servidor3 = ServidorWeb()
        servidor3.agregarModulo(Imagen)

        servidor3.agregarAnalizador(analizador1)
        servidor3.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK, "Esto es una imagen", 3, pedido1))
        analizador1.cantidadDeRespuestasDemoradasDelModulo(Imagen).shouldBe(1)
      }
      it("Analizadores-> tiempo de respuesta promedio") {
          servidor1. agregarModulo(Imagen)
          servidor1.agregarAnalizador(analizador2)
          servidor1.recibirUnPedido(pedido5)
          servidor1.recibirUnPedido(pedido1)
          servidor1.recibirUnPedido(pedido2)
          servidor1.recibirUnPedido(pedido6)
          analizador2.tiempoDeRepuestaPromedio().shouldBe(3)

      }
      it("Analizadores ->cantidad de pedidos entre dos momentos")
      {
          val servidor5 = ServidorWeb()
          val url7=URL("http","documents/index","jpg")
          val url8=URL("http","desktops/download","jpg")
          val url9=URL("http","desktops/objetos2","docx")
          val url10=URL("http","desktop/unaHur","jpg")
          val pedido8=Pedido("1.10.11:200",url7,LocalDateTime.of(2021,6,20, 1,30))
          val pedido9=Pedido("2.1.10:2010",url8,LocalDateTime.of(2021,4,20,5,2))
          val pedido10=Pedido("20.3.24:2650",url9,LocalDateTime.of(2021,3,4,20,5))
          val pedido11=Pedido("30.36.45:4020",url10,LocalDateTime.of(2021,6,8,4,6))

          servidor5.agregarModulo(Imagen)
          servidor5.agregarModulo(Texto)
          servidor5.agregarAnalizador(analizador3)
          servidor5.recibirUnPedido(pedido8)
          servidor5.recibirUnPedido(pedido9)
          servidor5.recibirUnPedido(pedido10)
          servidor5.recibirUnPedido(pedido11)
          analizador3.cantidadDePedidosEntre(LocalDateTime.of(2021, 4, 10, 0, 0), LocalDateTime.of(2021, 6, 10, 0, 0)).shouldBe(2)
          analizador3.cantidadDePedidosEntre(LocalDateTime.of(2021, 3, 1, 0, 0), LocalDateTime.of(2021, 6, 10, 0, 0)).shouldBe(3)
      }
      it("Analizadores->cantidad de respuestas cuyo body incluye un determinado String")
      {
          servidor1. agregarModulo(Imagen)
          servidor1.agregarAnalizador(analizador2)
          servidor1.recibirUnPedido(pedido5)
          servidor1.recibirUnPedido(pedido1)
          servidor1.recibirUnPedido(pedido2)
          servidor1.recibirUnPedido(pedido6)
          analizador2.cantidadDeRespuetaCutoBodyIncluye(Regex("hola")).shouldBe(0)

      }
      it("Analizadores -> porcentaje de pedidos con respuesta exitosa")
      {
          servidor1. agregarModulo(Imagen)
          servidor1.agregarAnalizador(analizador2)
          servidor1.recibirUnPedido(pedido5)
          servidor1.recibirUnPedido(pedido1)
          servidor1.recibirUnPedido(pedido2)
          servidor1.recibirUnPedido(pedido6)
            analizador2.porcentajeDeRespuestasExitosas().shouldBe(1)
      }
  }
})

