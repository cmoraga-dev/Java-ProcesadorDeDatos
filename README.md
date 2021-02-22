# Aplicación simple para procesar búsqueda de datos

## Introducción 

Esta aplicación fue hecha como ejercicio en un proceso de postulación a un cargo de programación.

Consta de un aplicativo java de línea de comandos que debe procesar data en formato .csv, realizar ciertas búsquedas de información de acuerdo a intrucciones dadas, y exportar los resultados en archivos .txt.


## Funcionamiento

La aplicación consta del siguiente flujo de acciones:

### Punto de entrada de consola

La aplicación se ejecuta desde la clase main/dialog/MainClass, la cual llama a MainDialog (del mismo package). Aquí se le pregunta al usuario si desea procesar datos. En caso de obtener respuesta positiva, se procede a la ejecución del programa.
Aquí también se manejan los posibles errores, desplegando un mensaje genérico y guardando el texto completo del error en un archivo de log.

## Ejecución del programa

#### DataProcessor

Desde MainDialog se llama a data/DataProcessor, específicamente al método run(). Aquí se crea una base de datos temporal SQLite, se crean las tablas que contendrán la información de los archivos csv, se ejecutan las queries para obtener los resultados solicitados, y se generan los archivos de texto para entregar la información.

#### Package database

Dentro de este package se encuentras dos clases que gestionan la base de datos. DataBase, que solo contiene métodos genéricos, y DataBaseProxy, con las operaciones específicas que son usadas por DataProcessor.

#### Package util

Aquí se encuentran clases auxiliares que apoyan la operación. 
DateParser sólo se encarga de transformar los formatos de fecha entre el fichero csv y la base de datos.
MyLogger implementa la clase util.Logger, a fin de guardar posibles errores.
Finalizar es una clase que contiene la operación de borrado de la base de datos.

## Demostración

Para usar la aplicación, se puede ejecutar el .jar ubicado en el directorio /jar, en conjunto con los 2 archivos .csv ubicados en el mismo directorio.