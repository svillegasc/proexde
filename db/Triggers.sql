/*TRIGGER QUE SE EJECUTA DESPUES DE LA ACTUALIZACION DEL ESTADO DE LA TABLA
PERFIL PARA DESACTIVAR TODOS LOS PERFILES CON ESE ID EN LA TABLA DETALLE_PERMISO*/
create or replace TRIGGER TRG_DesactivarPerfil
AFTER UPDATE
ON perfil
FOR EACH ROW
DECLARE
BEGIN
    if updating then
      if (:NEW.estado = 'I') then
        UPDATE detalle_permiso SET estado = 'I' WHERE id_Perfil = :OLD.id_Perfil;
      end if;
    end if;
END;

/
  
/*TRIGGER QUE SE EJECUTA DESPUES DE LA ACTUALIZACION DEL ESTADO DE LA TABLA
PERMISO PARA DESACTIVAR TODOS LOS PERMISOS CON ESE ID EN LA TABLA DETALLE_PERMISO*/
create or replace TRIGGER TRG_DesactivarPermiso
AFTER UPDATE
ON permiso
FOR EACH ROW
DECLARE
BEGIN
    if updating then
      if (:NEW.estado = 'I') then
        UPDATE detalle_permiso SET estado = 'I' WHERE id_Permiso = :OLD.id_Permiso;
      end if;
    end if;
END;
  
/

/*FUNCION QUE SE ENCARGA DE VALIDAR SI HAY STOCK DISPONIBLE EN EL INVENTARIO DEL PRODUCTO
RESTANDO LA CANTIDAD DISPONIBLE EN EL INVENTARIO Y DEVOLVIENDO LA CANTIDAD FALTANTE 
PARA LA PRODUCCION*/
create or replace FUNCTION FN_validarStockProducto(pId_producto number, pCantidadPedida number)
return number
IS
totalStock number;
stock number;
cantidadPedida number;
BEGIN
    SELECT stock INTO stock FROM producto WHERE id_producto = pId_producto;
    IF(stock > 0) THEN
      IF(pCantidadPedida > stock) THEN
        cantidadPedida := pCantidadPedida - stock;
        UPDATE producto SET stock = 0 WHERE id_Producto = pId_producto;
        RETURN cantidadPedida;
      ELSE
        totalStock := stock - pCantidadPedida;
        UPDATE producto SET stock = totalStock WHERE id_Producto = pId_producto;
        RETURN 0;
      END IF;
    END IF;
    RETURN pCantidadPedida;
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
        DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*FUNCION QUE SE ENCARGA DE VALIDAR SI HAY STOCK SUFICIENTE EN INSUMO PARA
FABRICAR LA CANTIDAD NECESARIA PARA EL PRODUCTO DESEADO*/
create or replace FUNCTION FN_validarStockInsumo(pIdPedido number)
RETURN number
IS
idInsumo number;
cantidadUtilizada number;
cantidad number;
stock number;
sw number;
estadoProduccion number;
CURSOR c_DPedido IS
SELECT id_producto,cantidad_pedida FROM DETALLE_PEDIDO_CLIENTE WHERE id_pedido = pIdPedido;
BEGIN
  sw := 0;
  cantidad := 0;
  SELECT estado_produccion INTO estadoProduccion FROM ESTADO_PRODUCCION WHERE DESCRIPCION = 'PENDIENTE';
  FOR r_DPedido IN c_DPedido  LOOP
    SELECT id_insumo,cantidad_utilizada into idInsumo,cantidadUtilizada  FROM receta WHERE id_producto = r_DPedido.id_producto;
    cantidad := r_DPedido.cantidad_pedida * cantidadUtilizada;   
    SELECT stock INTO stock FROM insumo WHERE id_insumo = idInsumo;
    IF cantidad > stock THEN
      sw := 1;
      DBMS_OUTPUT.PUT_LINE('estadoProduccion: ' || estadoProduccion);
      UPDATE PEDIDO SET estado_produccion = 6 WHERE id_pedido = pIdPedido;
      RETURN sw;
    END IF;
  END LOOP;
  RETURN sw;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*PROCEDIMIENTO QUE MANDA LOS PEDIDOS CON STOCK INSUFICIENTE A ESTADO PENDIENTE*/
create or replace PROCEDURE SP_pedidoPendiente(pIdPedido number,pIdProducto number, pCantidad number)
IS
estadoProduccion number;
BEGIN
  SELECT ESTADO_PRODUCCION INTO estadoProduccion FROM estado_produccion 
  WHERE descripcion = 'PENDIENTE';
  UPDATE PEDIDO SET estado_produccion = estadoProduccion WHERE id_pedido = pIdPedido;
 -- INSERT INTO DETALLE_PEDIDO_CLIENTE VALUES (pIdPedido,pIdProducto,pCantidad);
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*PROCEDIEMTO QUE SE ENCARGA DE ACTUALIZAR EL STOCK DE INSUMOS, CON LOS VALORES
DEL DETALLE DEL PEDIDO DEL CLIENTE*/
create or replace PROCEDURE SP_restarStockInsumo(pIdPedido number)
IS
idInsumo number;
cantidadUtilizada number;
cantidad number;
stock number;
CURSOR c_DPedido IS
SELECT id_producto,cantidad_pedida FROM DETALLE_PEDIDO_CLIENTE WHERE id_pedido = pIdPedido;
BEGIN
  FOR r_DPedido IN c_DPedido  LOOP
    SELECT id_insumo,cantidad_utilizada into idInsumo,cantidadUtilizada  FROM receta WHERE id_producto = r_DPedido.id_producto;
    cantidad := r_DPedido.cantidad_pedida * cantidadUtilizada;   
    UPDATE insumo SET stock = stock - cantidad WHERE id_insumo = idInsumo;
  END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*TRIGGER QUE SE ENCARGA DE GESTIONAR EL STOCK DE LOS PRODUCTO PARA LA VENTA,
VALIDA PRIMERO SI LA CANTIDAD PEDIDA SE ENCUENTRA DISPONIBLE EN INVENTARIO,
SI ESTA DISPONIBLE ACTUALIZA EL STOCK DEL PRODUCTO Y REALIZA EL PEDIDO*/
create or replace TRIGGER TRG_GestionarStockPedido
AFTER UPDATE
ON pedido
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
no_stock EXCEPTION;
sw number;
descrip varchar(50);
BEGIN
  SELECT descripcion INTO descrip FROM ESTADO_PRODUCCION WHERE ESTADO_PRODUCCION = :new.estado_produccion;
    IF descrip = 'PRODUCCION' THEN
      sw := FN_validarStockInsumo(:new.id_pedido);
      IF sw = 1 THEN
       -- RAISE no_stock;
       apex_application.g_print_success_message := 'No tiene stock suficiente, el pedido paso a estar PENDIENTE por stock';
      ELSE
        SP_restarStockInsumo(:new.id_pedido);
      END IF;
    END IF;
  EXCEPTION
      WHEN no_stock THEN
       RAISE_APPLICATION_ERROR(-20004, 'No tiene stock suficiente, el pedido paso a estar PENDIENTE por stock');
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
        DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*TRIGGER QUE SE ENCARGA DE GESTION EL TOTAL (EN DINERO) DE LOS PRODUCTOS QUE SE PEDIRAN AL PROVEEDOR*/
create or replace TRIGGER TRG_GestionarPrecioCompra
BEFORE INSERT OR UPDATE
ON detalle_pedido_proveedor
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
total number;
total1 number;
totalGen number;
precio_invalido EXCEPTION;
precio_igual EXCEPTION;
  BEGIN
    IF :new.precio <= 0 THEN
      RAISE precio_invalido;
    ELSIF :new.precio = :old.precio THEN
      RAISE precio_igual;
    ELSE
      SELECT total INTO total FROM pedido_proveedor WHERE id_pedido_proveedor = :new.id_pedido_proveedor;
      IF inserting THEN
        totalGen := total + (:new.precio * :new.cantidad);
        UPDATE pedido_proveedor SET total = totalGen WHERE id_pedido_proveedor = :new.id_pedido_proveedor;
      ELSIF updating THEN
        total1 := total - (:old.precio * :old.cantidad);
        totalGen := total1 + (:new.precio * :new.cantidad);
        UPDATE pedido_proveedor SET total = totalGen WHERE id_pedido_proveedor = :new.id_pedido_proveedor;
      END IF;
    END IF;
  EXCEPTION
    WHEN precio_invalido THEN
      RAISE_APPLICATION_ERROR(-20002, 'El precio que ingreso no es valido.');
    WHEN precio_igual THEN
        RAISE_APPLICATION_ERROR(-20003, 'El precio que ingreso es el mismo que tenia.');
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;
  
/

/*TRIGGER QUE SE EJECUTA DESPUES DE LA ACTUALIZACION DEL ESTADO DE LA TABLA
PROVEEDOR PARA ELIMINAR TODOS LOS PROVEEDORES CON ESE ID EN LA TABLA PROVEEDOR_INSUMO*/
create or replace TRIGGER TRG_EliminarProveedor
AFTER UPDATE
ON proveedor
FOR EACH ROW
DECLARE
BEGIN
    if updating then
      if (:NEW.estado = 'I') then
        DELETE FROM proveedor_insumo WHERE id_proveedor = :OLD.id_proveedor;
      end if;
    end if;
END;

/
  
/*TRIGGER QUE SE EJECUTA DESPUES DE LA ACTUALIZACION DEL ESTADO DE LA TABLA
INSUMO PARA ELIMINAR TODOS LOS INSUMOS CON ESE ID EN LA TABLA PROVEEDOR_INSUMO*/
create or replace TRIGGER TRG_EliminarInsumo
AFTER UPDATE
ON insumo
FOR EACH ROW
DECLARE
BEGIN
    if updating then
      if (:NEW.estado = 'I') then
        DELETE FROM proveedor_insumo WHERE id_insumo = :OLD.id_insumo;
      end if;
    end if;
END;
 
 /
 
 /*FUNCION QUE SE ENCARGA DE TRAER EL VALOR TOTAL QUE COSTO EL PRODUCTO QUE NECESITO
COMPARAR*/
create or replace FUNCTION FN_compararPrecios(pIdProducto number)
return number
IS
precio_compra number;
total number;
CURSOR c_insumo IS
SELECT id_insumo,cantidad_utilizada FROM receta WHERE id_producto = pIdProducto;
BEGIN
  total := 0;
  FOR r_insumo IN c_insumo LOOP
    SELECT precio_compra INTO precio_compra FROM insumo WHERE id_insumo = r_insumo.id_insumo;
      total := total + (r_insumo.cantidad_utilizada * precio_compra);
  END LOOP;
return total;
END;

/

/*TRIGGER QUE SE ENCARGA DE REALIZAR LA COMPARACION ENTRE EL PRECIO DE COMPRA
CON EL PRECIO DE COTIZACION PARA EL CLIENTE, PARA SABER SI EL PRECIO DE COMPRA ES
MENOR QUE EL PRECIO DE COTIZACION, DE SER ASÍ DEBE CAMBIAR EL PRECIO DE COTIZACION*/
create or replace TRIGGER TRG_GestionarPrecioCotizacion
BEFORE INSERT OR UPDATE
ON detalle_cotizacion_cliente
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
total_insumos number;
precio_invalido EXCEPTION;
precio_igual EXCEPTION;
valor_no_valido EXCEPTION;
  BEGIN
    IF :new.precio <= 0 THEN
      RAISE precio_invalido;
    ELSIF updating THEN
      IF :new.precio = :old.precio THEN
        RAISE precio_igual;
      END IF;
    ELSE
      total_insumos := FN_compararPrecios(:new.id_producto);
      IF :new.precio <= total_insumos THEN
        RAISE valor_no_valido;
      END IF;
    END IF;
  EXCEPTION
    WHEN precio_invalido THEN
      RAISE_APPLICATION_ERROR(-20002, 'El precio que ingreso no es valido.');
    WHEN precio_igual THEN
      RAISE_APPLICATION_ERROR(-20003, 'El precio que ingreso es el mismo que tenia.');
    WHEN valor_no_valido THEN
      RAISE_APPLICATION_ERROR(-20005, 'El precio ingresado es menor a los gastos del producto.');
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*PROCEDIMIENTO ALMACENADO QUE SE ENCARGA DE ACTUALIZAR EL STOCK DE INSUMO
CUANDO EL PEDIDO QUE SE LE HIZO AL PROVEEDOR SE ENCUENTRA TERMINADO*/
create or replace PROCEDURE SP_actualizarStockInsumo(pIdPedidoProveedor number)
IS
CURSOR c_insumo IS
SELECT id_insumo,cantidad FROM detalle_pedido_proveedor WHERE id_pedido_proveedor = pIdPedidoProveedor;
BEGIN
  FOR r_insumo IN c_insumo  LOOP
    UPDATE insumo SET stock = stock + r_insumo.cantidad WHERE id_insumo = r_insumo.id_insumo;
  END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
      DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;

/

/*TRIGGER QUE SE ACTIVA CUANDO SE INSERTA O SE ACTUALIZA UN PEDIDO AL PROVEEDOR
SI EL PEDIDO PASA A ESTAR TERMINADO ACTUALIZA LOS STOCK DE LOS INSUMOS PEDIDOS*/
create or replace TRIGGER TRG_GestionarStockPedidoProv
BEFORE UPDATE
ON pedido_proveedor
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
cont number;
BEGIN
  cont := 0;
  SELECT COUNT(*) INTO cont FROM estado_produccion 
  WHERE estado_produccion = :new.estado_produccion AND descripcion = 'Terminado';
  IF cont > 0 THEN
    SP_actualizarStockInsumo(:new.id_pedido_proveedor);
  END IF;
  EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('El SQLCODE ES: ' || SQLCODE);
        DBMS_OUTPUT.PUT_LINE('LA DESCRIPCION ES: ' || SQLERRM);
END;