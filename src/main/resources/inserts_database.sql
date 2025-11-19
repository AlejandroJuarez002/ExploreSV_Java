--ARCHIVO QUE TIENE LOS INSERT QUE SE DEBEN HACER EN HEIDISQL PARA PODER INICIAR SESION

--Insercion del rol ADMIN
INSERT INTO roles (nombre)
VALUES ('ADMIN');

--Insercion de un usuario
--El 1 en status significa 'Activo' y el 1 en rol_id significa el rol que se creo 'ADMIN'
--Recuerda que despues de crearlo debes encriptar la clave por la seguridad del proyecto "Spring Security"
--La pagina donde puedes encriptar el texto plano es: https://bcrypt-generator.com/
INSERT INTO usuarios (apellido, clave, nombre, nombre_usuario, status, rol_id)
VALUES ('Flores', '161204', 'ALejandro', 'aflores', 1, 1);

--Cuando inicies sesion en el sitio, el username que te pide debe ser el nombre_usuario que colocaste y no el nombre o apellido
--La clave sera la misma que pusiste en el insert y NO la encriptada

/*# ACCESO A LA BASE DE DATOS MYSQL
  #spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  #spring.datasource.url=jdbc:mysql://localhost:3306/dbexploresv?useSSL=true
  #spring.datasource.username=root
  #spring.datasource.password=yjcf1612Juarez$
  #
  #spring.jpa.hibernate.ddl-auto=update
  #spring.jpa.show-sql=true
  #spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
  #server.port=8081
*/