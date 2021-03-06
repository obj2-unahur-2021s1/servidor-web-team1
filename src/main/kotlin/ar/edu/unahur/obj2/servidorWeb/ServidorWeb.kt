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

    fun eSRespuestaExistosa()=this.codigo== CodigoHttp.OK

}


class URL(val protocolo: String, val ruta: String, val extension: String) {
    fun cumpleConProtocolo() = protocolo=="http"

    fun esCompatibleConModulo(modulo:Modulo): Boolean {
        return modulo.estaLaExtension(extension)
    }
    fun esLaMismaRuta(ruta: String)= this.ruta==ruta

}

class ServidorWeb {

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
        return if(!pedido.url.cumpleConProtocolo()) {
            Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, pedido)
        } else {
            Respuesta(CodigoHttp.OK, "", 10, pedido)
        }
    }

    fun puedeResponder(pedido: Pedido) = modulos.any{m->m.puedeAtenderAlPedido(pedido)}

    fun recibirUnPedido(pedido: Pedido): Respuesta {
        if(this.puedeResponder(pedido) and pedido.url.cumpleConProtocolo()){
            val moduloDelPedido = modulos.find { it.puedeAtenderAlPedido(pedido) }!!
            val respuesta = Respuesta(CodigoHttp.OK, moduloDelPedido.texto, moduloDelPedido.tiempo, pedido)
            analizadores.forEach { it.agregarRespuesta(respuesta) }
            return respuesta
        } else {
            return Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)

    }
  }
}

abstract class Modulo(val texto: String, val tiempo: Int) {

    abstract fun puedeAtenderAlPedido(pedido: Pedido): Boolean

    abstract fun estaLaExtension(extension: String): Boolean
}

object Imagen: Modulo("Esto es una imagen", 3) {

    var extenciones = mutableListOf<Any>("jpg", "png", "gif")

    override fun puedeAtenderAlPedido(pedido: Pedido) = extenciones.any { extencion->extencion==pedido.url.extension }

    fun tieneExtension(extension: Any): Boolean {  ////
        return extenciones.contains(extension)
    }

    override fun estaLaExtension(extension: String) = extenciones.any{c->c==extension} //

}

object Texto: Modulo("Esto es un texto", 8) {

    var extenciones = mutableListOf<Any>("docx", "odt")

    override fun puedeAtenderAlPedido(pedido: Pedido)= extenciones.any { extencion->extencion==pedido.url.extension }

    override fun estaLaExtension(extension: String) = Imagen.extenciones.any{ c->c==extension}  //
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
        val ipsDeRespuestas = respuestas.map{it.pedido.ip}
        return ipsDeRespuestas.intersect(ipsPeligrosas).size
    }

    fun elModuloMasConsultadoPorIps(): Modulo {
        val pedidosImagen = respuestas.map{it.pedido.url.esCompatibleConModulo(Imagen)}.size
        val pedidosTexto = respuestas.map{it.pedido.url.esCompatibleConModulo(Texto)}.size
        if(pedidosImagen>pedidosTexto) {
            return Imagen
        } else {
            return Texto}
    }
    // el conjunto de IPs sospechosas que requirieron una cierta ruta.

    fun ipsSospechosasConRuta(ruta:String):Set<Any> {
        //respuestas con la misma ruta
        return respuestas.filter{c->c.pedido.url.esLaMismaRuta(ruta)}.map{it.pedido.ip}.intersect(ipsPeligrosas)
    }
    fun tiempoDeRepuestaPromedio():Int {
        //tiempo de respuesta promedio
        return respuestas.sumBy{c->c.tiempo}/respuestas.size
    }


    fun cantidadDePedidosEntre(fecha1: LocalDateTime, fecha2: LocalDateTime):Int {
        val pedidosHasta= respuestas.filter { it.pedido.fechaHora > fecha1 }
        val pedidosEntreFechas = pedidosHasta.filter { it.pedido.fechaHora < fecha2 }
        return pedidosEntreFechas.size
    }
    //cantidad de respuestas cuyo body incluye un determinado
    fun cantidadDeRespuetaCutoBodyIncluye(string:Regex):Int
    {
        return respuestas.count{it.body.matches(string)}

    }
    //porcentaje de pedidos con respuesta exitosa
    fun porcentajeDeRespuestasExitosas() :Int
    {
        val diviendo= respuestas.count{it.eSRespuestaExistosa()}
        val divisor= respuestas.size
        return divisor/diviendo
    }
}