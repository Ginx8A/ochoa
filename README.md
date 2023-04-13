### Ochoa, Claudia Gisela (claudia.gisela.ochoa@gmail.com)

## Challenge


1- Levantar app. 

### LISTA DE COMENTARIOS
2- Desde POSTMAN acceder al siguiente metodo GET: localhost:{puerto}/getComments


## AGREGAR UN COMENTARIO
2- Desde POSTMAN acceder al siguiente metodo POST: localhost:{puerto}/putComments/{id}
3- En la pestania body se agrega un comentario de tipo JSON, por ejemplo:
{ "comment":"COMENTARIO DE EJEMPLO"}

## LISTAR TICKETS
2- Desde POSTMAN acceder al siguiente metodo GET: localhost:{puerto}/getTickets

## LISTA DE CARRERAS
2- Desde POSTMAN acceder al siguiente metodo GET: localhost:{puerto}/getCareers


## VER UNA CARRERA
2- Desde POSTMAN acceder al siguiente metodo GET: localhost:{puerto}/getCareers/{id} 


## ELIMINAR UNA CARRERA
2- Desde POSTMAN acceder al siguiente metodo POST: localhost:{puerto}/deleteCareer/{id}  


## AGREGAR UNA CARRERA
2- Desde POSTMAN acceder al siguiente metodo POST: localhost:{puerto}/putCareer/{id}  
3- En la pestania body se agrega un JSON, con la siguiente estructura:
```
{
    "career": 
        {
			"usuarioAlta": "admin",
            "nombre": "Tecnico en programacion",
		    "objetivo": "objetivo",
		    "modalidad": "online",
		    "detalle": {
                 "duracion": "3",
                 "unidadTiempoDuracion": "anios",
                 "costo": "200",
                 "vigente": true,
            }
		    "estado": "ACTIVO",
		    "imagen": null,
        }
}
```
			
## MODIFICAR UNA CARRERA
2- Seguir los pasos del punto VER UNA CARRERA  
3- Copiar el json resultante.
4- Desde POSTMAN acceder al siguiente metodo POST: localhost:{puerto}/putCareer/{id}  
5- En la pestania body pegar el json copiado desde el punto 3, modificar lo deseado, por ejemplo:  
```
{  
    "career":   
        {  
			"usuarioModificacion": "admin",  
			"usuarioAlta": "admin",  
			"fechaAlta": "13-04-2023",  
            "nombre": "Tecnico/Auxiliar en programacion",  
		    "objetivo": "Introducir al alumno en lenguaje de programacion",  
		    "modalidad": "hibrido",  
		    "detalle": {  
                 "duracion": "3",  
                 "unidadTiempoDuracion": "anios",  
                 "costo": "200",  
                 "vigente": true,  
            }  
		    "estado": "ACTIVO",  
		    "imagen": null,  
        }  
}
```

