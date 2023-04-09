package entidad;

import excepctions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

class CuenteaTest {
    Cuenta cuenta;
    @Test
    void test() {

    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
        System.out.println("iniciando el metodo");
    }
    @AfterEach
    void tearDown(){
        System.out.println("Finalizando el metodo");
    }

    /**
     * testiamos nombre de cuenta
     */


    @Test
    @DisplayName("Probando nombre de la c/c")
    void testNombreCuenta() {

        // cuenta.setPersona("Damian");
        String esperado = "Damian";
        String real = cuenta.getPersona();
        assertNotNull(real, ()->"la cuenta no puede ser nula");
        Assertions.assertEquals(esperado, real,()-> "el nombre de la cuenta no es el que se esperaba");
        //  assertTrue(real.equals("Damian"));|
    }

    /**
     * test sobre saldo
     */
    @Test
    @DisplayName("Probando el saldo de la cuenta")
    void testSaldoCuenta() {
         cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.200, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    /**
     * test sobre instancias sean iguales
     */
    @Test
    @DisplayName("probando que si son iguales las instancias de las cuentas")
    void testReferenciaCuenta() {
         cuenta = new Cuenta("Gonzalo", new BigDecimal("100.20"));
        Cuenta cuenta2 = new Cuenta("Gonzalo", new BigDecimal("100.20"));
//        assertNotEquals(cuenta2,cuenta);
        assertEquals(cuenta2, cuenta);
    }

    /**
     * test sobre debito
     */
    @Test
    @DisplayName("probando el debito de la cuenta")
    void testDebitoCuenta() {
         cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
        cuenta.debito(new BigDecimal(10));
        assertNotNull(cuenta.getSaldo());
        assertEquals(90, cuenta.getSaldo().intValue());
        assertEquals("90.20", cuenta.getSaldo().toPlainString());
    }

    /**
     * test sobre credito
     */
    @Test
    void testCreditoCuenta() {
         cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
        cuenta.credito(new BigDecimal(10));
        assertNotNull(cuenta.getSaldo());
        assertEquals(110, cuenta.getSaldo().intValue());
        assertEquals("110.20", cuenta.getSaldo().toPlainString());
    }

    /**
     * test manejo de error de dinero insuficiente
     */
    @Test
    void testDineroInsuficienteException() {
         cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(200));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    @DisplayName("este test va a fallar por fail()")
    @Disabled
    void testTransferirDineroCuentas() {
        fail();
        cuenta = new Cuenta("Damian", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("camila", new BigDecimal("1000"));
        Banco banco = new Banco();
        banco.setNombre("Banco Rio");
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));
        assertEquals("1500", cuenta2.getSaldo().toPlainString());
        assertEquals("2000", cuenta.getSaldo().toPlainString());

    }

    @Test
    void testRelacionBancoCuenta() {
        cuenta = new Cuenta("Damian", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("camila", new BigDecimal("1000"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco Rio");
        banco.transferir(cuenta, cuenta2, new BigDecimal(500));
        assertAll(() ->
                        assertEquals("1500", cuenta2.getSaldo().toPlainString(),
                                ()->"El valor de la cuenta 2 no es la que se esperaba"),

                () ->
                        assertEquals("2000", cuenta.getSaldo().toPlainString()),

                () ->
                        assertEquals("2000", cuenta.getSaldo().toPlainString()),

                () ->
                        assertEquals(2, banco.getCuentas().size()),
                () ->
                        assertEquals("Banco Rio", cuenta.getBanco().getNombre()),

                () ->
                        assertEquals("Banco Rio", cuenta2.getBanco().getNombre()),

                () ->
                        assertEquals("Damian", banco.getCuentas().stream()
                                .filter(c -> c.getPersona().equals("Damian"))
                                .findFirst().get().getPersona()),

                () ->
                        assertTrue(banco.getCuentas().stream().
                                anyMatch(c -> c.getPersona().equals("camila"))));


    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
        System.out.println("Solo windows");

    }
    @Test
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void testSoloLinuxOMax() {
        System.out.println("Linux o Mac");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows() {

    }
    @Test
    @DisabledOnOs(OS.LINUX)
    void testNoLinux() {
        System.out.println("Este metodo se desabilita en linux ");

    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void SoloJDK8() {

    }
    @Test
    @DisabledOnJre(JRE.JAVA_8)
    void noJDK8() {

    }

    @Test
    void imprimirPropiedadesDeSistema() {
        Properties properties = System.getProperties();
        properties.forEach((k,v)->{
            System.out.println(k+ ":"+ v);
        });

    }

    @Test
    @EnabledIfSystemProperty(named = "java.version",matches = ".*19.*")
    void testJavaVersion() {
    }
    @Test
    @DisabledIfSystemProperty(named = "java.version",matches = ".*19.*")
    void testJavaVersionDisabled() {
    }

    @Test
    void imprimirVariablesAmbiente() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k,v)-> System.out.println(k + "=" + v));

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches =".*jdk-19.0.2.*" )
    void testJavaHomeVariable() {
        System.out.println("Se ejecuta si JAVA_HOME");
    }
}