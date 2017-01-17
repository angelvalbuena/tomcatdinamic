#Anotaciones

####[28/07/2016]:[19:14]

Hemos optado por cambiar el driver de la base de datos de mongodb de la versión 3.2.0 a la 2.14.0 razones:

	Se genera una excepción en la versión 3.2.0 llamada MongoSocketOpenException cuando se intenta utilizar la conexión con la base de datos estando esta desconectada, Esta excepción no la pudimos capturar.
	
	La versión 2.14 viene de la versión 2.11 y de esta se encuentra mayor información sobre su implementación que la 3.2.0.

####[29/07/2016]:[0:11]

* Hemos logrado conectar la base de datos con la clase del BRT Bus para consultar los detalles precisos del bus.

* Definimos que todos los nombres de los nombres clave, variables, campos y parámetros tanto de JSON como de JAVA las vamos a escribir en minúscula, mientras que en la Base de datos los String Key tendrán mayúscula inicial.

* El bus debe existir para poder modificar sus valores.

* Se implementaron los métodos cerrarConexion(), actualizaMDB, insertarMDB y consultarMDB, donde los 3 últimos son transacciones simuladas con mongo para parecerse a las bases de datos relacionales.

* Cuando se hace una consulta utilizando un objeto DBCursor, los elementos lo almacena desde cero, pero en la posición cero esta vacía, es por esto que siempre que se vaya leer una variable se requiere comenzar aplicando el método next() al objeto DBCursor y posteriormente el método get(String Key); esta esta cadena de métodos retorna un objeto tipo Object casteable u otro DBCursor, ahora bien una vez utilizado el next() para leer los elementos del mismo documento se debe usar objeto.curr().get(String Key).

* Queda pendiente hacer resistente a fallos todas las Clases implementadas y documentar las clases restantes y nuevas.

* El Bus ahora tiene la capacidad de crear un Objeto Json de sí mismo y actualizarlo con el método actualizarJsonBus(), en caso de que reciba modificaciones.

* UbicacionBus ya no retorna un String con formato Json con las coordenadas promedio, ahora retorna un String con formato Json con la información del bus obtenida en el post y consultada en la base de datos.

* Se modificó el formato de envió de ubicacionBus por el siguiente:
```json
{
  	"placa": "ZOE 101",
	 	"coordenada":{
    		"latitud": "7.113633",
		"longitud":"-73.114842"
		}
}
```
Teniendo en cuenta que la placa debe existir en la base de datos para no lanzar una excepción.

####RECORDATORIO: 

	Cuando se haga un CLONE al repositorio, CAMBIAR el nombre WebServicesTest por rutasBuses


####[30/07/2016]:[14:26]

* Se añade un servicio post de prueba para Wilson testear la galileo.

####[06/08/2016]:[11:00]

* El bus ahora solo crea un objeto json si se lo piden y se modificó el método consultarMDB para que este devuelva un objeto DBOBject y no un apuntador. Esto con finalidad de facilitar el código y evitar confusiones con el método .next(), también para identificar cuando el objeto que quiere consultar no está en la base de datos.

* Se creó la clase BusDB que es la clase que se encargara de hacer interactuar el bus con la base de datos.

* Se creó el paquete baseDeDatosMDB para colocar las clases que intervienen con la base de datos y se movió 
BusDB y ConectarMongo

Clases que se modificaron:

	BusDB
	ConectarMongo
	Bus
	UbicacionBus

####Tareas pendientes:

	Implementar la clase BusDB.
	
	Hacer refactor para trabajar la libreria que sera puesta debajo.
	↓↓↓↓
	
[JSR 353 (JSON Processing) API 1.0 API](https://json-processing-spec.java.net/nonav/releases/1.0/fcs/javadocs/index.html "libreria")



####[06/08/2016]:[18:00]

* Se creó la clase BusDB que es la encargada de obtener los valores de la base de datos del bus.

* Se removió la excepción de MongoTimeOut de Conectar mongo y se creara el try catch solo cuando se invoque el servicio que la lance.

* Se removió el método existe() de BusDB y su funcionalidad se delegó a valoresBaseDatos().

Clases que se modificaron:

	BusDB
	ConectarMongo
	Bus
	UbicacionBus
	MensajeError

####Tareas Pendientes:

	Implementar querys en BusDB

####[23/08/2016]:[23:21]

* Se implementó la funcionalidad para determinar si un bus se encuentra dentro o fuera de una estación (área circular), Para lograrlo se creó una clase de utilidad que contiene el radio de la circunferencia y calcula si el punto (bus) esta dentro o fuera de este radio.
* Se creó un nuevo servicio que hace uso de la funcionalidad descrita anteriormente. El formato es:

```json
{
    "coordenada1" : {"latitud":"7.137157","longitud":"-73.122247"},
    "coordenada2" : {"latitud":"7.136681","longitud":"-73.122551"}
}
```
Y la URI del servicio es: http://localhost:8080/NOMBREDEPROYECTO/CARPETADELWEBXML/Proximidad/estaDentro
Ej. http://localhost:8080/rutasBuses/apirutas/Proximidad/estaDentro

Se crearon las siguientes Clases:

	AreaEstacion (Clase de Utilidad)
	Proximidad (Servicio)

####[24/08/2016]:[15:56]

* Se creó una documentación inicial en Excel acerca de todos los servicios implementados hasta el momento. Por el momento
Se llevara en el Excel. Más adelante se creara una documentación con un framework.

####[24/08/2016]:[19:11]

* Se implementó un nuevo paquete clientes con una clase llamada cliente que permite crear un cliente que consume un servicio desde una url. Además se modificó el servicio Dbtest para probar el funcionamiento de esta clase.

Clases que se modificaron:
	
	Dbtest (Servicio)

Clases que se crearon:

	HTTPClient ( Junto con su paquete Clientes)

####[24/08/2016]:[19:47]

* Se modificó el servicio que utiliza el cliente.

####[25/08/2016]:[12:10]

* Reestructuración del proyecto para seguir el estándar de maven.

####[26/08/2016]:[14:20]

* Se crea el directorio src/test/java para que se puedan hacer pruebas al código. Además se actualizo la documentación con las nuevas url.

####AVISO

	Angel, En este directorio es donde usted puede crear las clases para hacer pruebas.

#### [31/08/2016]:[00:02]

* Se creó la función que permite proyectar un punto sobre una recta y una clase para probar cosas de la base de datos.

Clases que se crearon:

	Geomatematicas (Clase de Utilidad)
	Insertar (BaseDeDatosMongoDB)

####[31/08/2016]:[17:51]

####Tareas Pendientes:

	Crear un método que permita añadir paradas intermedias sobre rutas previamente creadas.

####[01/09/2016]:[11:35]

* Interacciones con la base de datos dentro de la clase Insertar.

####[20/09/2016]:[16:15]

* Corrección de un bug que impedía el acceso a los servicios de forma correcta. Esto se corrigió creando dentro de la carpeta WebContent archivos necesarios para la configuración de los servicios, gracias a una consulta de angel y la ayuda del profesor Gabriel.

####[20/09/2016]:[19:23]

* Se crearon métodos para insertar elementos en distintas posiciones o al final.

* Se implementaron los métodos eliminarRuta para eliminar una ruta completa y eliminarXPosicionParada, esta ultima elimina una parada de una ruta por medio de un índice llamado posición.

* Se implementó imprimirRutas que imprime las rutas con información detallada, sin embargo aún está en revisión.

* A partir de este momento se empezó a utilizar una UI llamada [Robomongo](https://robomongo.org/ "Link de la pagina oficial de Robomongo") para visualizar la base de datos MongoDB.

Clases que se crearon:

	Eliminar

Clases que se modificaron:

	Insertar
	
####Tareas Pendientes:

	Implementar un metodo para listar los contenidos de la base de datos.	

####[21/09/2016]:[15:10] 

* Se agregó el método eliminarParada.

* Se realizó cambios den los métodos eliminarRuta y paradas para que retornen un mensaje que avise si pudo ser eliminado.

* Se ha añadido una nueva clase de utilidad llamada FormtearDatos, para tratar las entradas de usuario.

Clases que se Crearon:

	FormatearDatos (Clase de Utilidad)

Clases que se modificaron
	
	Eliminar


####[21/09/2016]:[19:33]

* Se corrigió la documentación ahora la placa no lleva espacios.

* En BusDB se corrigió el casteo doble de double a int, ahora solo se hace el casteo a int, esto gracias a que logramos insertar enteros en la base de datos.

* Se hizo refactor a ConectarMongo para que las transacciones básicas no soliciten la base de datos, el nombre de la base de datos GeneralBRT está definido en conectar mongo como final.

* Se creó TransaccionesBus y TransaccionesRutas son clases con métodos estáticos que se encargan de manejar las operaciones validas que se le pueden aplicar a un bus y una ruta en una base de datos.

* En las clasesDelBRT se agregó la clase paradas.

* En baseDeDatosMDB se agregaron ParadaDB y RutaDB para ser utilizadas de la misma manera que BusDB.

Clases que se crearon:

	TransaccionesBus (Clase de Base de Datos)
	TransaccionesRutas  (Clase de Base de Datos)
	ParadaDB  (Clase de Base de Datos)
	RutaDB  (Clase de Base de Datos)
	Paradas (Clase del BRT)
	
Clases que se modificaron:
	
	Eliminar
	BusDB
	ConectarMongo
	

####[21/09/2016]:[20:44]

* Se eliminaron las clases Eliminar e Insertar, debido a que sus metodos fueron llevados a las clases de transacciones.

* Se creo la clase TransaccionesParada.

* Se creó otro método a FormatearDatos, el cual hace que una oración tenga mayuscala inicial por cada palabra.

* Se creó una clase main para probar cambios en las clases y nuevas clases antes de ponerlas a interactuar con otras.

Clases que se crearon:

	TransaccionesParada	
	ClasesDePruebaCloud

Clases que se Modificaron:

	FormatearDatos

Clases que se eliminaron:
	
	Insertar
	Eliminar


####Tareas Pendientes

	Crear servicios que retorne una Ruta, una Parada y Un Bus con toda su información estatica.
	Crear servicios que retorne todas las Rutas, Paradas y Buses con la informacion estatica de cada uno de ellos.
	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.


####[21/09/2016]:[23:19]
	
* Se crearon dos nuevos directorios que contendran las clases de prueba de Angel, Andres y Giovanni.

* Se modifico el nombre de la clase de pruebas de ClasesDePruebaCloud a Test (Sujeto a futuros cambios).

* Se movio la clase Test a src/test/java/AndresGiovanni.

* se movio la clase ClasesDePruebaAca a src/test/java/Angel.

####[22/09/2016]:[03:53]

* Se crearon las clases GetServicioBus, GetServicioRuta, GetServicioParada y se implementaron los cascarones de los servicios de obtencion con y sin parametros encargados de recibir informacion desde la base de datos.

####Tareas Pendientes

	Implementar los cascarones de los servicios para la obtencion de informacion tanto para bus, parada y ruta.
	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.
	
####[22/09/2016]:[18:42]

* Se modificaron todas las url para que sus iniciales sean en minuscula.( ver documentacion.xlsx )
* Se implemento el servicio http://localhost:8080/cloudBRT/api/rutas/consultar.
* Se agrego un path a DistanciaEntreCoordenadas (/distancia).
* Se crearon las clases PostServicioBus, PostServicioParada, PostServicioRuta encargadas de interactuar con la base de datos mediante POST.
* Se elimino la clase Dbtest con la cual se probaba un cliente.
* Se modifico el extractor para que cuando digite la plata en minuscula el servidor siempre la tome en mayuscula.

Clases que se Crearon:

	PostServicioBus
	PostServicioParada
	PostServicioRuta
	
Clases que se Modificaron:

	EnvioCoor
	GetServicioRuta
	Test
	DistanciaEntreCoordenadas
	Proximidad
	Extractor
	
Clases que se Eliminaron:

	Dbtest
	
####Tareas Pendientes

	Implementar los cascarones de los servicios para la obtencion de informacion tanto para bus y parada.
	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.

####[22/09/2016]:[19:43]

* Se implementaron los servicios que permiten listar todos los Buses(GetServicioBus) y todas las Paradas(GetServicioParada)

* Se recompilo el War 

Clases que se Modificaron:

	GetServicioBus
	GetServicioParada
	documentacion

####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.
	
####[22/09/2016]:[02:40]

* Se creo un nuevo metodo en MensajeError (noEncontroElElemento) que avisa cuando se produce un error al no encontrar un elemento generando un json de respuesta.

* Se especifico el charset de respuesta a todos los servicios como UTF-8.

* Se implementaron los servicios de consulta especificando mediante parametros en las 3 clases (Bus,Parada,Ruta)

####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	
	Documentar los nuevos servicios que se implementen.
	
	CORREGIR EL BUG DEL CHARSET(PRIORIDAD)
	
	Crear la documentacion de los nuevos servicios
	
Clases que se Modificaron:

	GetServicioBus
	GetServicioParada
	GetServicioRuta
	MensajeError
	
####[26/09/2016]:[07:05]

* Se cambiaron los nombres de los paquetes
* Se omitio el error del charset y decidimos no utilizar caracteres especiales en lugar dejamos, Placa, Nombre y Clave como llaves para Bus, Ruta y Parada respectivamente,los cuales se usaran para identificarlos y no usaran caracteres epeciales.
* Se añadio un atributo llamado clave para las paradas y se modifico tanto TransaccionesParada como Transaccionesruta para que la base de datos tenga el atributo clave para paradas.
* Se borro el charset en los produces.
* Apartir de ahora se añadira una actualización de la base de datos para que los demas hagan pruebas, solo sera la data y sera almacenada en un fichero llamado db.rar

Clases que se Modificaron:

	GetServicioBus
	GetServicioParadaa
	GetServicioRuta
	TransaccionesParada
	TransaccionesRutaa
	
####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.
	Crear la documentacion de los nuevos servicio
	Modificar eliminar parada para que cuando se borre una parada tambien elimine la asignación a la rutas
	
####[05/10/2016]:[22:18]

* Se creo un metodo toString en la clase coordenadas que permite devolver una representacion Json de si mismo en String.
* Fecha ahora se crea mediante un Singleton.
* Se empezo a implementar el metodo para crear recorridos de la clase TransaccionesRecorrido.
* Se definieron las velocidades en metros sobre segundo.
* Se crearon nuevos metodos en la clase FormatearDatos para convertir desde horas a minutos y/o segundos y convertir velocidades
  desde kilometros por hora a metros por segundo.
* en la clase GeoMatematicas se creo un metodo "hallarTiempo" que permite calcular cuanto tiempo tarda a una velocidad determinada
  recorrer una distancia.
* Se creo un nuevo constructor con parametros para Coordenadas.

Clases que se Modificaron:

	TransaccionesRecorrido
	Coordenadas
	Fecha
	GeoMatematicas
	FormatearDatos
	
####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.
	Modificar eliminar parada para que cuando se borre una parada tambien elimine la asignación a la rutas y de recorridos
	Colocar una etiqueta de hora de recepcion de un json.
	
####[06/10/2016]:[23:31]

* Se adiciono un control adicional en la clase parada que permite comprobar si ya existe una parada con el mismo nombre impidiendo su adicion en caso positivo.
* En la clase TransaccionesRecorrido se creo un metodo que permite generar un recorrido con unas horas de forma automatica apoyandose en metodos asistentes como construirHorario (Genera un linkedhashmap de paradas y horas) y removerFormatoDeTiempo (convierte una hora dada en formato de string 24 horas ej: 03:00:20 a segundos contados a partir de 00:00:00)
* Se creo un metodo en la clase TransaccionesRuta "eliminarParadas" que permite eliminar todas las paradas de una ruta
* Se creo un metodo en la clase FormatoDatos "formatoDeTiempo". Este permite recibir una hora dada en segundos y convertirla al formato 24 horas en String ej 3600 lo convierte a 01:00:00
* Se agrego la nueva version de la base de datos con las nuevas paradas y un recorrido de prueba"
	
Clases que se Modificaron:

	TransaccionesRuta
	TransaccionesParada
	TransaccionesRecorrido
	FormatearDatos
	
####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos.
	Documentar los nuevos servicios que se implementen.
	Modificar eliminar parada para que cuando se borre una parada tambien elimine la asignación a la rutas y de recorridos
	Colocar una etiqueta de hora de recepcion de un json.
	
####[14/10/2016]:[17:19]

* Se crearon las Api en las 6 clases tanto get como post para agregar, editar, eliminar y reemplazar valores de la base de datos.
* Se actualizo la documentacion de la api con los servicios actuales

Clases que se Modificaron:

	GetServicioParada
	GetServicioRuta
	GetServicioBus
	PostServicioParada
	PostServicioRuta
	PostServicioBus
	
####Tareas Pendientes

	Crear servicios para que usen los metodos que creamos para interactuar con la base de datos de recorrido.
	Documentar los nuevos servicios que se implementen.
	Modificar eliminar parada para que cuando se borre una parada tambien elimine la asignación a la rutas y de recorridos
	Colocar una etiqueta de hora de recepcion de un json.

####[17/10/2016]:[11:40]

* Se adecuo la clase bus para que se encargue de contener informacion relevante al bus en tiempo de ejecucion
* Se adecuo la clase conductor con metodos para poder crear un conductor y obtener sus atributos
* Se adecuo una parada como un objeto que contiene un id de parada y unas cordenadas. Este tambien posee la geocerca que se empleara para determinar si un bus esta fuera o dentro de esa estacion mediante su metodo.
* Se modifico la clase ruta que contiene un arreglo de todas las paradas.
* Se modifico la clase recorrido que da la posibilidad de obtener una parada dada una posicion o obtener datos del hashmap del horario por sus indices.
* Se modifico la visibilidad del metodo de construirHorario para emplearlo en la clase Recorrido.
* Se creo una clase llamada parque automotor que contiene todos los buses del sistema y sus ultimo valor de coordeanadas recibido desde el servicio de obtencion de datos. Se implemento con singleton. Esta contiene un metodo para acceder a un bus en especifico.
* Se creo la clase itinerario que se encargara de relacionar: conductor,recorrido y bus.
* Se agrego una coordeanda a la consulta de los buses. Esta tiene la ultima coordenada que llega desde el servicio post de wilson para ese bus.

Clases que se modificaron:

	Bus
	Conductor
	Parada
	Recorrido
	Ruta
	TransaccionesRecorrido
	UbicacionBus
	getServicioBus

Clases que se crearon:

	Itinerario
	ParqueAutomotor

Clases que se eliminaron:

	Tiempo
	
####Tareas Pendientes

	Implementar la logica de Itinerario con metodos que permitan ubicar el bus de forma porcentual y metodos para el manejo del conductor.
	Crear la clase despacho que contendra todos los itinerarios y el manejo de los mismos.


####Dudas

	¿Se implementara la logica de adelantos o atrasos?
	
####[17/10/2016]:[11:40]

* Se refactorizo el sistema de recoleccion de datos historico. Ahora se almacena en la base de datos y no en un archivo de texto.
* Se modifico la clase BusDB. Ahora tambien se encarga de crear un historico para cada bus en otra base de datos.
* Se modifico la documentacion para separar los servicios de administracion, colector y monitoreo.
* Se creo una nueva clase llamada Despacho que es la encargada de contener todos los itinerarios creados.
* Se hizo refactor a Recorrido. Ahora no vuelve a construir el horario sino que lo lee desde la base de datos.
* Se hizo refactor general a todos los nombres de las clases.
* Se creo una nueva clase llamada DBColector que es la encargada de interactuar con la base de datos de recoleccion.

Clases que se modificaron:

	BusDB
	UbicacionBus
	TBus
	TColectorBus
	TParada
	TRecorrido
	TRuta
	DBGeneralBRT
	Recorrido
	

Clases que se crearon:
	
	DBColector
	Despacho

####[1/11/2016]:[4:43]

* Se implementaron unos cambios a peticion de antonio.
* Se le han añadido los parametros categoria y descricion a las rutas.
* Las rutas se siguen creando como siempre, solo que ahora almacenan vacios categoria y descripcion.
* Se implementaron dos apis para modificar la categoria y la descripcion
* Se añadieron esos datos a la consulta de las rutas.

Clases que se modificaron:

	TRuta
	GetServicioRuta
	PostServicioRuta
	Ruta

####[1/11/2016]:[21:30]

* Se hizo refactor a las URI de los servicios. Consultar la documentacion en el excel.

Clases que se modificaron:

	GetServicioBus
	GetServicioParada
	GetServicioRuta
	GetServicioRecorrido
	PostServicioBus
	PostServicioParada
	PostServicioRuta
	PostServicioRecorrido


####[3/11/2016]:[22:33]

* Se refactorizo el metodo para actualizar en la base de datos. Ya no se utiliza UpdateMulti puesto que puede modificar multiples valores si se repite la clave.
* Reduccion de codigo en la clase TBus
* Se creo la clase TConductor y los servicios correspondientes al conductor.
* Se refactorizo el proyecto en general para generar url mas legibles. 
* Se corrigio una excepcion que impide la creacion de un recorrido utilizando una ruta sin paradas.
* Se corrigio el servicio de monitoreo de paradas para que liste la clave de las mismas.

Clases que se Eliminaron:

	GetServicioBus
	GetServicioParada
	GetServicioRuta
	GetServicioRecorrido
	PostServicioBus
	PostServicioParada
	PostServicioRuta
	PostServicioRecorrido

Clases que se Crearon:

	Admin
	Monitoreo

Clases que se modificaron:

	Se movieron las clases de probar algunos metodos por servicios a src/test/java/andres.giovanni

Tareas Pendientes:

	Ver como agregar mas directorios de codigo a la compilacion.
	Metodo para eliminar recorrido.
	
####[15/11/2016]:[22:15]
	
* Refactor de nombre de la clase ParqueAutomotor. Ahora todas las clases que coleccionen todos los objetos de un tipo se llaman
 TipoRT donde RT significa RunTime (Ej. BusesRT)
* Se crearon dos clases que coleccionan objetos de los tipos conductor llamado ConductoresRT y RecorridosRT que tienen todos los objetos 
 de dichos tipos.
* Se implemento el patron de diseno observer entre Itinerario y bus. Si uno de los buses llega a una geocerca de una parada el bus que
 llega a la misma notifica al sistema. Cada itinerario solo escucha al objeto de tipo bus declarado como busDesignado en itinerario.
 
Clases que se Crearon:

	RecorridosRT
	ConductoresRT
	Observer
	Subject
	
Clases que se modificaron.

	ParqueAutomotor
	Itinerario
	UbicacionBus
	
####[30/11/2016]:[17:49]
	
* Se creo una clase de utilidad llamada diccionario, esta contiene hasmaps para llevar el control de parametros a modificar.
* Se hicieron modificaciones generales a las clases RT, para que actualicen sus datos cuando hallan cambios en la db.
* Se crearon los servicios relacionados con Itinerario, uno de monitoreo para consultarlo y los de administración.
* Se creo el metodo de eliminar recorrido que estaba pendiente.
* Se creo el servicio de iniciar itinerario.
* Se corrigio el formato de hora cuando se marca una estación en el itinerario
 
Clases que se Crearon:

	Diccionario
	
Clases que se modificaron.

	Fecha
	RecorridosRT
	BusTR
	ConductoresRT
	TColectorBus
	TItinerario
	TRecorrido
	Admin
	UbicacionBuss

Tareas Pendientes:

	Realizar controles para evitar la creación de itinerarios cuano no existe un bus, un conductor o un recorrido.
	Realizar controles para evitar que se elimien buses, conductores y recorridos cuando pertenecen a un itinerario.
	Realizar control para evitar que se elimine una parada si esta pertenece a un una ruta.
	Realizar control para evitar que se elimine una ruta si esta pertenece a un recorrido.
	Realizar un control para cerrar itinerarios por falta de iniciacion.
	Realizar un control para que una vez iniciado un itinerario no puede ser reinicado hasta que termine.

####[21/12/2016]:[22:00]

* Se optimizo la busqueda del parque automotor mediante la creacion de la misma en un hashmap y no en un arreglo.
* Se refactorizo el registro a la base de datos. Ahora se hace registro a la base de datos cada cierta cantidad de entradas.
* Se refactorizo el punto de vista de la carga de los itinerarios y su finalizacion. Ahora un itinerario en false indica que inicio y en true que termino
* Ahora no se hace refresh desde la base de datos en el itinerario si no que solo se lleva una copia del indice en cada bus para tolerancia a fallos y se 
  añade funcionalidad para corregirse en caso de que falle.
* Se refactorizo el despacho. Ahora contiene un mapa en vez de una coleccion para hacer las operaciones con los itinerarios.
* Se acorto la url para la busqueda de la ubicacion de un bus de un itinerario.
* Ahora las operaciones se hacen en paralelo en RT como en base de datos para evitar el exceso de consultas a DB.

Clases que se modificaron:

	
	Bus
	Despacho
	Itinerario
	TColectorBus
	TItinerario
	Monitoreo
	UbicacionBus


####[27/12/2016]:[14:00]

* Se agrego un nuevo metodo que sirve para calcular cuantos minutos teoricamente llevaria un bus hasta un punto de su recorrido 
  y el servicio que devuelve lo que se tarda a las paradas y el tiempo hasta un punto. Estos tiempos son teoricos calculados.
  en base a una velocidad modificable.
* Se asigno a la clase Bus una variable estatica VelMed que permite asignar una velocidad Teorica a los buses.

Clases que se modificaron.

	Monitoreo
	Bus
	Itinerario
	Recorrido
	TRecorrido
	FormatearDatos
	GeoMatematicas

####[29/12/2016]:[23:15]

* Se añade una nueva dependencia llamada guava de google que permite trabajar con Multihashmap
* Se crea un nuevo mapa en Despacho conocido como MultiHashMap. Esto es para poder relacionar
  varios valores con una misma clave.
* Se crea un nuevo servicio que mediante la ruta regresa los tiempos teoricos de cada bus por todos
  los itinerarios.

Clases que se modificaron:

	Despacho
	Admin
	Monitoreo
	Pom.xml
	documentacion.xlsx

####[09/01/2017]:[23:42]

* Se añadio un traductor que convierte de Fechas a tiempos en ms
* Se añadio un atributo nuevo al itinerario llamado fecha que es util para calcular el tiempo en ms
* Se agrego un nuevo campo al bus llamado Operador. Contiene el operador del bus.
* Se modifico el servicio de consultar buses a partir de la ruta.
* Se refactorizo el metodo que permite calcular Duraciones para que devuelva en segundos y a su vez el de avancebus.

Clases que se modificaron:

	Bus
	Itinerario
	BusDB
	TBus
	TItinerario
	Admin
	Monitoreo
	UbicacionBus
	Geomatematicas
	
####[10/01/2017]:[19:09]

* Se implemento un nuevo servicio que permite conocer los buses que le pertenecen a una ruta determinada segun el parametro idicado

Clases que se modificaron:

	Monitoreo
	












	
		
	
	