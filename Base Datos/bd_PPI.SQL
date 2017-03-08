DELETE TABLE DETALLE_COTIZACION_CLIENTE;
DELETE TABLE DETALLE_PEDIDO_CLIENTE;
DELETE TABLE DETALLE_PEDIDO_PROVEEDOR;
DELETE TABLE PROVEEDOR_INSUMO;
DELETE TABLE PEDIDO_PROVEEDOR;
DELETE TABLE PROVEEDOR;
DELETE TABLE PEDIDO;
DELETE TABLE COTIZACION;
DELETE TABLE ESTADO_PRODUCCION;
DELETE TABLE USUARIO;
DELETE TABLE DETALLE_PERMISO;
DELETE TABLE PERFIL;
DELETE TABLE PERMISO;
DELETE TABLE TIPO_IDENTIFICACION;
DELETE TABLE RECETA;
DELETE TABLE INSUMO;
DELETE TABLE PRODUCTO;

DROP TABLE DETALLE_COTIZACION_CLIENTE;
DROP TABLE DETALLE_PEDIDO_CLIENTE;
DROP TABLE DETALLE_PEDIDO_PROVEEDOR;
DROP TABLE PROVEEDOR_INSUMO;
DROP TABLE PEDIDO_PROVEEDOR;
DROP TABLE PROVEEDOR;
DROP TABLE PEDIDO;
DROP TABLE COTIZACION;
DROP TABLE ESTADO_PRODUCCION;
DROP TABLE USUARIO;
DROP TABLE DETALLE_PERMISO;
DROP TABLE PERFIL;
DROP TABLE PERMISO;
DROP TABLE TIPO_IDENTIFICACION;
DROP TABLE RECETA;
DROP TABLE INSUMO;
DROP TABLE PRODUCTO;

DROP SEQUENCE cot;
DROP SEQUENCE ins;
DROP SEQUENCE ped;
DROP SEQUENCE eProduc;
DROP SEQUENCE tIdent;
DROP SEQUENCE pProveedor;
DROP SEQUENCE perf;
DROP SEQUENCE perm;
DROP SEQUENCE prod;
DROP SEQUENCE prov;
DROP SEQUENCE usu;



CREATE TABLE Cotizacion
(
	id_Cotizacion   NUMBER(5) NOT NULL,
	id_Usuario      NUMBER(5) NOT NULL,
	fecha_Creacion  DATE NOT NULL,
	estado          VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Detalle_Cotizacion_Cliente
(
	id_Cotizacion  NUMBER(5) NOT NULL,
	id_Producto    NUMBER(5) NOT NULL,
	precio         NUMBER(8,2) NOT NULL
)
;


CREATE TABLE Detalle_Pedido_Cliente
(
	id_Pedido        NUMBER(5) NOT NULL,
	id_Producto      NUMBER(5) NOT NULL,
	cantidad_Pedida  NUMBER(5) NOT NULL
)
;


CREATE TABLE Detalle_Pedido_Proveedor
(
	id_Pedido_Proveedor  NUMBER(5) NOT NULL,
	id_Insumo            NUMBER(5) NOT NULL,
	cantidad             NUMBER(5) NOT NULL,
	precio               NUMBER(8,2) NOT NULL
)
;


CREATE TABLE Detalle_Permiso
(
	id_Permiso  NUMBER(5) NOT NULL,
	id_Perfil   NUMBER(5) NOT NULL,
	estado	    VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Estado_Produccion
(
	estado_Produccion  NUMBER(2) NOT NULL,
	descripcion        VARCHAR2(50) NOT NULL
)
;


CREATE TABLE Insumo
(
	id_Insumo       NUMBER(5) NOT NULL,
	nombre          VARCHAR2(50) NOT NULL,
	descripcion     VARCHAR2(50) NOT NULL,
	precio_Compra   NUMBER(8,2) NOT NULL,
	unidad_Medida   VARCHAR2(5) NOT NULL,
	stock           NUMBER(8) NOT NULL,
	ultima_Entrada  DATE NOT NULL,
	ultima_Salida   DATE NOT NULL,
	estado          VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Pedido
(
	id_Pedido           NUMBER(5) NOT NULL,
	id_Cotizacion       NUMBER(5) NOT NULL,
	fecha_Pedido        DATE NOT NULL,
	cantidad_Producida  NUMBER(8),
	fecha_Inicio        DATE NOT NULL,
	fecha_Terminacion   DATE NOT NULL,
	estado_Produccion   NUMBER(2) NOT NULL,
	valor_Total			NUMBER(8)
)
;

CREATE TABLE Pedido_Proveedor
(
	id_Pedido_Proveedor  NUMBER(5) NOT NULL,
	id_Proveedor         NUMBER(5) NOT NULL,
	fecha_Pedido         DATE NOT NULL,
	estado_Produccion	 NUMBER(2) NOT NULL,
	total                NUMBER(8,2) NOT NULL
)
;


CREATE TABLE Perfil
(
	id_Perfil   NUMBER(5) NOT NULL,
	nombre      VARCHAR2(50) NOT NULL,
	estado      VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Permiso
(
	id_Permiso   NUMBER(5) NOT NULL,
	descripcion  VARCHAR2(50) NOT NULL,
	estado       VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Producto
(
	id_Producto     NUMBER(5) NOT NULL,
	nombre          VARCHAR2(50) NOT NULL,
	descripcion     VARCHAR2(50) NOT NULL,
	stock           NUMBER(8) NOT NULL,
	ultima_Entrada  DATE NOT NULL,
	ultima_Salida   DATE NOT NULL,
	estado          VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Proveedor
(
	id_Proveedor     NUMBER(5) NOT NULL,
	nombre           VARCHAR2(50) NOT NULL,
	nit              VARCHAR2(15) NOT NULL,
	direccion        VARCHAR2(70) NOT NULL,
	telefono         VARCHAR2(15) NOT NULL,
	nombre_Contacto  VARCHAR2(50) NOT NULL,
	email            VARCHAR2(50) NOT NULL,
	estado           VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Proveedor_Insumo
(
	id_Insumo		 NUMBER(5) NOT NULL,
	id_Proveedor     NUMBER(5) NOT NULL
)
;


CREATE TABLE Receta
(
	id_Producto         NUMBER(5) NOT NULL,
	id_Insumo           NUMBER(5) NOT NULL,
	cantidad_Utilizada  NUMBER(5) NOT NULL
)
;


CREATE TABLE tipo_Identificacion
(
	tipo_Identificacion  NUMBER(2) NOT NULL,
	descripcion          VARCHAR2(50) NOT NULL,
	estado               VARCHAR2(1) NOT NULL
)
;


CREATE TABLE Usuario
(
	id_Usuario           NUMBER(5) NOT NULL,
	cuenta               VARCHAR2(50) NOT NULL,
	primer_Nombre        VARCHAR2(50) NOT NULL,
	segundo_Nombre       VARCHAR2(50),
	primer_Apellido      VARCHAR2(50) NOT NULL,
	segundo_Apellido     VARCHAR2(50),
	identificacion       VARCHAR2(15) NOT NULL,
	tipo_Identificacion  NUMBER(2) NOT NULL,
	telefono             VARCHAR2(15),
	password             VARCHAR2(50) NOT NULL,
	id_Perfil            NUMBER(5) NOT NULL,
	estado               VARCHAR2(1) NOT NULL,
	token                VARCHAR2(15),
	token_fecha 		 varchar2(13)
)
;


CREATE SEQUENCE cot START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE ins START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE ped START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE eProduc START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE tIdent START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pProveedor START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE perf START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE perm START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE prod START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE prov START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE usu START WITH 1 INCREMENT BY 1;


ALTER TABLE Cotizacion ADD CONSTRAINT PK_Cotizacion 
	PRIMARY KEY (id_Cotizacion)
;

ALTER TABLE Detalle_Cotizacion_Cliente ADD CONSTRAINT PK_Detalle_Cotizacion_Cliente 
	PRIMARY KEY (id_Cotizacion, id_Producto)
;

ALTER TABLE Detalle_Pedido_Cliente ADD CONSTRAINT PK_Detalle_Pedido_Cliente 
	PRIMARY KEY (id_Pedido, id_Producto)
;

ALTER TABLE Detalle_Pedido_Proveedor ADD CONSTRAINT PK_Detalle_Pedido_Proveedor 
	PRIMARY KEY (id_Pedido_Proveedor, id_Insumo)
;

ALTER TABLE Detalle_Permiso ADD CONSTRAINT PK_Detalle_Permiso 
	PRIMARY KEY (id_Perfil, id_Permiso)
;

ALTER TABLE Estado_Produccion ADD CONSTRAINT PK_Estado_Produccion 
	PRIMARY KEY (estado_Produccion)
;

ALTER TABLE Insumo ADD CONSTRAINT PK_Insumo 
	PRIMARY KEY (id_Insumo)
;

ALTER TABLE Pedido ADD CONSTRAINT PK_Pedido 
	PRIMARY KEY (id_Pedido)
;

ALTER TABLE Pedido_Proveedor ADD CONSTRAINT PK_Pedido_Proveedor 
	PRIMARY KEY (id_Pedido_Proveedor)
;

ALTER TABLE Perfil ADD CONSTRAINT PK_Perfil 
	PRIMARY KEY (id_Perfil)
;

ALTER TABLE Permiso ADD CONSTRAINT PK_Permiso 
	PRIMARY KEY (id_Permiso)
;

ALTER TABLE Producto ADD CONSTRAINT PK_Producto 
	PRIMARY KEY (id_Producto)
;

ALTER TABLE Proveedor ADD CONSTRAINT PK_Proveedor 
	PRIMARY KEY (id_Proveedor)
;

ALTER TABLE Proveedor_Insumo ADD CONSTRAINT PK_Proveedor_Insumo
	PRIMARY KEY (id_Insumo, id_Proveedor)
;

ALTER TABLE Receta ADD CONSTRAINT PK_Receta 
	PRIMARY KEY (id_Producto, id_Insumo)
;

ALTER TABLE tipo_Identificacion ADD CONSTRAINT PK_tipo_Identificacion 
	PRIMARY KEY (tipo_Identificacion)
;

ALTER TABLE Usuario ADD CONSTRAINT PK_Usuario 
	PRIMARY KEY (id_Usuario)
;



ALTER TABLE Cotizacion ADD CONSTRAINT FK_Cotizacion_Usuario 
	FOREIGN KEY (id_Usuario) REFERENCES Usuario (id_Usuario)
;

ALTER TABLE Detalle_Cotizacion_Cliente ADD CONSTRAINT FK_Detalle_P_Cliente_Cotiz 
	FOREIGN KEY (id_Cotizacion) REFERENCES Cotizacion (id_Cotizacion)
;

ALTER TABLE Detalle_Cotizacion_Cliente ADD CONSTRAINT FK_Detalle_P_Cliente_Producto 
	FOREIGN KEY (id_Producto) REFERENCES Producto (id_Producto)
;

ALTER TABLE Detalle_Pedido_Cliente ADD CONSTRAINT FK_Detalle_P_Cliente_Pedido 
	FOREIGN KEY (id_Pedido) REFERENCES Pedido (id_Pedido)
;

ALTER TABLE Detalle_Pedido_Cliente ADD CONSTRAINT FK_Detalle_P_Cliente_Prod 
	FOREIGN KEY (id_Producto) REFERENCES Producto (id_Producto)
;

ALTER TABLE Detalle_Pedido_Proveedor ADD CONSTRAINT FK_Detalle_P_Proveedor_Insumo 
	FOREIGN KEY (id_Insumo) REFERENCES Insumo (id_Insumo)
;

ALTER TABLE Detalle_Pedido_Proveedor ADD CONSTRAINT FK_Detalle_Pedido_Proveedor
	FOREIGN KEY (id_Pedido_Proveedor) REFERENCES Pedido_Proveedor (id_Pedido_Proveedor)
;

ALTER TABLE Detalle_Permiso ADD CONSTRAINT FK_Detalle_Permiso_Perfil 
	FOREIGN KEY (id_Perfil) REFERENCES Perfil (id_Perfil)
;

ALTER TABLE Detalle_Permiso ADD CONSTRAINT FK_Detalle_Permiso_Permiso 
	FOREIGN KEY (id_Permiso) REFERENCES Permiso (id_Permiso)
;

ALTER TABLE Pedido ADD CONSTRAINT FK_Pedido_Cotizacion 
	FOREIGN KEY (id_Cotizacion) REFERENCES Cotizacion (id_Cotizacion)
;

ALTER TABLE Pedido ADD CONSTRAINT FK_Pedido_Estado_Produccion 
	FOREIGN KEY (estado_Produccion) REFERENCES Estado_Produccion (estado_Produccion)
;

ALTER TABLE Pedido_Proveedor ADD CONSTRAINT FK_Pedido_Proveedor_Proveedor 
	FOREIGN KEY (id_Proveedor) REFERENCES Proveedor (id_Proveedor)
;

ALTER TABLE Pedido_Proveedor ADD CONSTRAINT FK_Pedido_Proveedor_Estado
	FOREIGN KEY (estado_Produccion) REFERENCES estado_Produccion (estado_Produccion)
;

ALTER TABLE Proveedor_Insumo ADD CONSTRAINT FK_Proveedor_Insumo_Prov 
	FOREIGN KEY (id_Proveedor) REFERENCES Proveedor (id_Proveedor)
;

ALTER TABLE Proveedor_Insumo ADD CONSTRAINT FK_Proveedor_Insumo_Ins
	FOREIGN KEY (id_Insumo) REFERENCES Insumo (id_Insumo)
;

ALTER TABLE Receta ADD CONSTRAINT FK_Receta_Insumo 
	FOREIGN KEY (id_Insumo) REFERENCES Insumo (id_Insumo)
;

ALTER TABLE Receta ADD CONSTRAINT FK_Receta_Producto 
	FOREIGN KEY (id_Producto) REFERENCES Producto (id_Producto)
;

ALTER TABLE Usuario ADD CONSTRAINT FK_Usuario_Perfil 
	FOREIGN KEY (id_Perfil) REFERENCES Perfil (id_Perfil)
;

ALTER TABLE Usuario ADD CONSTRAINT FK_Usuario_tipo_Identificacion 
	FOREIGN KEY (tipo_Identificacion) REFERENCES tipo_Identificacion (tipo_Identificacion)
;