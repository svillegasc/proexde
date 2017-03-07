ALTER TABLE usuario add token varchar2(16) null;
ALTER TABLE usuario add token_fecha varchar2(13) null;

/

CREATE OR REPLACE FUNCTION validar_token(var_token VARCHAR2)
RETURN NUMBER IS
  formato_fecha varchar2(12) := 'yyyyMMddhhmi';
  fechaAct_token varchar2(12);
  diferencia number(10);
  cont number(10);
BEGIN
   
  SELECT count(*) INTO cont FROM usuario WHERE token = var_token;

  
  IF ( cont = 0 )THEN
     return -1;
  ELSE 
     SELECT u.token_fecha
       INTO fechaAct_token
       FROM usuario u
      WHERE u.token = var_token;
      
     SELECT ((to_date(sysdate, formato_fecha) -
              to_date(fechaAct_token, formato_fecha)) * 24 * 60)
       INTO diferencia
       FROM DUAL;
     
     IF (diferencia > 30)THEN
        return -1;
     ELSE
       return 1;
     END IF;  
  END IF;
END;
