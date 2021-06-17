package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: URL, val fechaHora: LocalDateTime)

data class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)


class URL(val protocolo: String, val ruta: String, val extension: String)
{
  fun cumpleConProtocolo() = protocolo=="http"
}

class servidorWeb {

  var modulos = mutableListOf<Modulo>()

  fun agregarModulo(modulo: Modulo) {
    modulos.add(modulo)
  }

  fun sacarModulo(modulo: Modulo) {
    modulos.remove(modulo)
  }

  fun cumpleConElProtocolo(pedido: Pedido): String {
    return if(!pedido.url.cumpleConProtocolo()) {
      Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido).darRespuesta()
    }
    else {
      Respuesta(CodigoHttp.OK, "", 10, pedido).darRespuesta()
    }
  }

  fun puedeResponder(pedido: Pedido) = modulos.any{m->m.puedeAtenderAlPedido(pedido)}

  fun recibirUnPedido(pedido: Pedido): String {
    return if(puedeResponder(pedido) and pedido.url.cumpleConProtocolo())  {
      Respuesta(CodigoHttp.OK, "", 10, pedido).darRespuesta()
    }
    else
    {
      Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido).darRespuesta()
    }
  }
}

interface Modulo
{
  fun puedeAtenderAlPedido(pedido: Pedido): Boolean
}

object Imagen: Modulo {
  var extenciones = mutableListOf<Any>("jpg", "png", "gif")
  override fun puedeAtenderAlPedido(pedido: Pedido) = extenciones.any { extencion->extencion==pedido.url.extension }

}

object Texto: Modulo {
  var extenciones = mutableListOf<Any>("docx", "odt")
  override fun puedeAtenderAlPedido(pedido: Pedido)= extenciones.any { extencion->extencion==pedido.url.extension }

}

class Analizadores(val demoraMinima:Int,var ipsPeligrosas:List<Any>)