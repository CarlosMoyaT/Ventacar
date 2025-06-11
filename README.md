Ventacar - Proyecto WEB

![Captura de pantalla](src/main/assets/Captura.png)
![Captura de pantalla](assets/Captura2.png)


Ventacar es una aplicación web para la compra y reserva de coches. Ofrece una plataforma sencilla y accesible donde los usuarios pueden registrarse o iniciar sesión para explorar los vehículos, ver detalles específicos y realizar compras o reservas. Está diseñada tanto para clientes como para administradores que gestionarán los vehículos disponibles.

-Funcionalidades básicas-

Registro y autenticación de usuarios (login/logout)

Listado de vehículos con filtros de búsqueda.

Visualización de los detalles del coche (modelo, precio, características).

Reserva o compra del vehículo.

Generación de facturas (PDF) personalizadas para el usuario.

Envío de correos con factura integrada tanto para compras como para reservas de coches.

-Tecnologías y lenguajes de programación-

Versión: JDK22, Jakarta EE 9

Frontend: HTML, CSS, JavaScript

Backend: Java, Spring Boot

Bases de datos: PostgreSQL

Servicios web: RESTful API

Servidor web: Tomcat 10.1

Editor: IntelliJ IDEA

Seguridad: Spring Security (roles User y Admin)

PDF y correo: Itext y Mailtrap. Mailersend y SMTP (para el envío de correos a los clientes)

-Diseño de plantillas UI-

Link al diseño en Figma: https://www.figma.com/design/6uRpn3rJaPPZN0Za9UJrs3/tea3?node-id=0-1&p=f&t=uwbky68QuhGPjc9K-0

Interfaz dividida entre: index.html, login.html, resultados.html, nosotros.html y detalle.html


Endpoints:
GET /api/vehiculos/buscar

GET /api/vehiculos/marca{marca}

GET /api/vehiculos/tipo{tipo}

GET /api/vehiculos/precio

Documentación del código fuente y Comentarios JavaDoc
Estructura organizada por (Config, Model, Controller, Service, Repository)

Rutas REST definidas en los controladores
