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

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)


class Analizadores(val demoraMinima:Int,var ipsPeligrosas:List<Any>)

interface Modulo
object Imagen: Modulo {
  var extenciones = ["jpg", "png", "gif"]
  fun devuelve(){}
  fun cuantoTarda(){}
}

object Texto: Modulo {
  var extenciones = ["docx", "odt"]
  fun devuelve(){}
  fun cuantoTarda(){}
}

class servidorWeb(val modulo: Modulo ) //= null
{

  fun cumpleConElProtocolo(pedido: Pedido): CodigoHttp {
    if(pedido.url.cumpleConProtocolo()) {
      return CodigoHttp.OK
    }
    else {
      return CodigoHttp.NOT_IMPLEMENTED
    }
  }
  /*
  fun recibirPedido(pedido: Pedido) {
    if(modulo == null) {

    }
  }*/
}

class URL(val protocolo: String, val ruta: String, val extension: String)
{
  fun cumpleConProtocolo()= protocolo=="http"

}


