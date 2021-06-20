package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.awt.Image
import java.time.LocalDate
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("Un servidor web") {
    val servidor1=servidorWeb()
      val servidor4=servidorWeb()

    val url1=URL("http","documents/index","jpg")
    val url2=URL("https","desktops/download","jpg")
    val url5=URL("http","desktops/objetos2","html")
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
      val servidor2 = servidorWeb()
      servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido1))
      servidor2.agregarModulo(Imagen)
      servidor2.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK,"Funciona",5,pedido1))
    }
      it("Requerimiento 3 con analizadores ips Sopechosas ")
      {
        val analizador1=Analizadores(4, listOf("12312", "123124334") )
        val servidor3 = servidorWeb()
        servidor3.agregarModulo(Imagen)
          servidor3.agregarAnalizador(analizador1)
          servidor3.recibirUnPedido(pedido1).shouldBe(Respuesta(CodigoHttp.OK, "Funciona", 5, pedido1))
          analizador1.cantidadDeRespuestasDemoradasDelModulo(Imagen).shouldBe(1)
      }
      it("Analizadores-> tiempo de respuesta promedio")
      {
          servidor1. agregarModulo(Imagen)
          servidor1.agregarAnalizador(analizador2)
          servidor1.recibirUnPedido(pedido5)
          servidor1.recibirUnPedido(pedido1)
          servidor1.recibirUnPedido(pedido2)
          servidor1.recibirUnPedido(pedido6)
          analizador2.tiempoDeRepuestaPromedio().shouldBe(5)

      }
      it("Analizadores ->cantidad de pedidos entre dos momentos")
      {

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

