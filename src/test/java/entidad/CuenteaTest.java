package entidad;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class CuenteaTest {
    @Test
void test(){
    
}

    @Test
    void testNombreCuenta(){
    Cuenta cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
   // cuenta.setPersona("Damian");
    String esperado = "Damian";
    String real = cuenta.getPersona();
        Assertions.assertEquals(esperado, real);
      //  assertTrue(real.equals("Damian"));|
    }
    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
        assertEquals(1000.200, cuenta.getSaldo().doubleValue() );
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) <0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Gonzalo", new BigDecimal("100.20"));
        Cuenta cuenta2 = new Cuenta("Gonzalo", new BigDecimal("100.20"));
//        assertNotEquals(cuenta2,cuenta);
        assertEquals(cuenta2,cuenta);
    }
}