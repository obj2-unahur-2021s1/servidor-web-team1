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

data class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido) {

    fun esRespuestaDemorada(tiempoAnalizadores: Int) = tiempo > tiempoAnalizadores
}


class URL(val protocolo: String, val ruta: String, val extension: String)
{
  fun cumpleConProtocolo() = protocolo=="http"

    fun esCompatibleConModulo(modulo:Modulo): Boolean {
        return modulo.estaLaExtension(extension)
    }
}

class servidorWeb {

    var analizadores= mutableListOf<Analizadores>()

    var modulos = mutableListOf<Modulo>()

  fun agregarModulo(modulo: Modulo) {
    modulos.add(modulo)
  }

  fun sacarModulo(modulo: Modulo) {
    modulos.remove(modulo)
  }

    fun agregarAnalizador(analizador: Analizadores) {
       analizadores.add(analizador)
    }

    fun sacarAnalizador(analizador: Analizadores) {
        analizadores.remove(analizador)
    }

  fun cumpleConElProtocolo(pedido: Pedido): Respuesta {
    return if(!pedido.url.cumpleConProtocolo())
    {
      Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
    }
    else {
      Respuesta(CodigoHttp.OK, "", 10, pedido)
    }
  }

  fun puedeResponder(pedido: Pedido) = modulos.any{m->m.puedeAtenderAlPedido(pedido)}

  fun recibirUnPedido(pedido: Pedido): Respuesta {
    if(puedeResponder(pedido) and pedido.url.cumpleConProtocolo()){
      val respuesta = Respuesta(CodigoHttp.OK, "Funciona",5, pedido)
      analizadores.forEach { it.agregarRespuesta(respuesta) }

      return respuesta
    }
    else
    {
      return Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)

    }
  }
}

interface Modulo
{
  fun puedeAtenderAlPedido(pedido: Pedido): Boolean
  fun estaLaExtension(extension: String): Boolean
}

object Imagen: Modulo {
  var extenciones = mutableListOf<Any>("jpg", "png", "gif")
  override fun puedeAtenderAlPedido(pedido: Pedido) = extenciones.any { extencion->extencion==pedido.url.extension }

    fun tieneExtension(extension: Any): Boolean {
        return extenciones.contains(extension)
    }

    override fun estaLaExtension(extension: String) = extenciones.any{c->c==extension}

}

object Texto: Modulo {
  var extenciones = mutableListOf<Any>("docx", "odt")
  override fun puedeAtenderAlPedido(pedido: Pedido)= extenciones.any { extencion->extencion==pedido.url.extension }

    override fun estaLaExtension(extension: String) = Imagen.extenciones.any{ c->c==extension}
}

class Analizadores(val demoraMinima:Int,var ipsPeligrosas:List<Any>)
{
    var respuestas = mutableListOf<Respuesta>()
    var respuestasDeIpsSospechosas=mutableListOf<Respuesta>()

    fun agregarRespuesta(respuesta: Respuesta) {
        respuestas.add(respuesta)
    }

    fun cantidadDeRespuestasDemoradasDelModulo(modulo: Modulo): Int {
        return respuestas.filter { c-> c.pedido.url.esCompatibleConModulo(modulo)}.count{c->c.esRespuestaDemorada(demoraMinima)}

    }



    fun respuestasConIpsSospechosas(): Int {
        val ips = respuestas.map{it.pedido.ip}
        return ips.intersect(ipsPeligrosas).size
    }

    fun elModuloMasConsultadoPorIps():Modulo
    {
        val pedidosImagen = respuestas.map{it.pedido.url.esCompatibleConModulo(Imagen)}.size
        val pedidosTexto = respuestas.map{it.pedido.url.esCompatibleConModulo(Texto)}.size
        if(pedidosImagen>pedidosTexto)
        {return Imagen}
        else{return Texto}
    }



}