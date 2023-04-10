package entidad;

import excepctions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

class CuenteaTest {
    Cuenta cuenta;
    private TestInfo testInfo;
    private TestReporter testReporter;

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
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo= testInfo;
        this.testReporter = testReporter;
        this.cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
        //System.out.println("ejecutando"+ testInfo.getDisplayName()+ " " + testInfo.getTestMethod().get().getName() + " "+ testInfo.getTags());
        testReporter.publishEntry("ejecutando"+ testInfo.getDisplayName()+ " " + testInfo.getTestMethod().get().getName() + " "+ testInfo.getTags());
        System.out.println("iniciando el metodo");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo");
    }

    /**
     * testiamos nombre de cuenta
     */

@Tag("cuenta")
    @RepeatedTest(value = 5, name = " {displayName} - repeticion numero: {currentRepetition}  de {totalRepetitions}")
    @DisplayName("Probando nombre de la c/c")
    void testNombreCuenta(RepetitionInfo info) {
    if (testInfo.getTags().contains("cuenta")){
        System.out.println("Este metodo contiene la etiqueta cuenta");
    }
        if (info.getCurrentRepetition() == 2) {
            System.out.println("Esta es la repeticion 2");
        }

        // cuenta.setPersona("Damian");
        String esperado = "Damian";
        String real = cuenta.getPersona();
        assertNotNull(real, () -> "la cuenta no puede ser nula");
        Assertions.assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba");
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

    @Nested
    @DisplayName("test moviemiento de dinero")
    class moviemientoDineroTest {
        /**
         * test debito cuenta
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


        @Test
        @Tag("cuenta")
        @Tag("error")
        void testDineroInsuficienteException() {
            /**
             * test manejo de error de dinero insuficiente
             */
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
                                () -> "El valor de la cuenta 2 no es la que se esperaba"),

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

    @Nested
    @DisplayName("testeando sistema operativo")
    class sistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
            System.out.println("Solo windows");

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
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
    }

    @Nested
    @DisplayName("testeando version de java")
    class javaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void SoloJDK8() {

        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        void noJDK8() {

        }
    }

    @Nested
    @DisplayName("testeando propiedades de sistema")
    class sistemPropertiesTest {
        @Test
        void imprimirPropiedadesDeSistema() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });

        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*19.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "java.version", matches = ".*19.*")
        void testJavaVersionDisabled() {
        }

    }

    @Nested
    @DisplayName("testeando variables de ambiente")
    class variableDeAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + "=" + v));

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-19.0.2.*")
        void testJavaHomeVariable() {
            System.out.println("Se ejecuta si JAVA_HOME");
        }
    }

    @Nested
    @DisplayName("test de variables de produccion y desarrollo")
    class variableDevAndProd {
        @Test
        @DisplayName("Probando el saldo de la cuenta en Dev")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            System.out.println(System.getProperty("ENV"));
            assumeTrue(esDev);
            cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
            System.out.println("en desarrollo");
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.200, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Probando el saldo de Cuente Dev 2")
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            System.out.println(System.getProperty("ENV"));
            assumingThat(esDev, () -> {

                cuenta = new Cuenta("Damian", new BigDecimal("1000.200"));
                System.out.println("en desarrollo");
                assertNotNull(cuenta.getSaldo());
                assertEquals(1000.200, cuenta.getSaldo().doubleValue());
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
                assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            });
            System.out.println("En produccion");


        }

    }

    @Tag("param")
    @Nested
    @DisplayName("pruebas paratremizadas")
    class testParatremizadas {
        @ParameterizedTest(name = " numero:  {index} ejecutando con valor {0}")
        @ValueSource(strings = {"10", "5", "20"})
        @DisplayName("probando el debito de la cuenta con parametros")
        void testDebitoCuenta(String monto) {
            System.out.println("Monto : " + monto);
            cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }

        @ParameterizedTest(name = " numero:  {index} ejecutando con valor {0}")
        @CsvSource({"1,10", "2,5", "3,50"})
        @DisplayName("probando el debito de la cuenta con parametros con indice y valor")
        void testDebitoCuenta1(String index, String monto) {
            System.out.println(index + "-> " + monto);
            cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }

        @ParameterizedTest(name = " numero:  {index} ejecutando con valor {0}")
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("probando el debito de la cuenta con parametros en archivos")
        void testDebitoCuenta2(String monto) {
            System.out.println("Monto: -> " + monto);
            cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }

        @ParameterizedTest(name = " numero:  {index} ejecutando con valor {0}")
        @MethodSource("montoList")
        @DisplayName("probando el debito de la cuenta con parametros por metodo")
        void testDebitoCuenta3(String monto) {
            System.out.println("Monto: -> " + monto);
            cuenta = new Cuenta("Damian", new BigDecimal("100.20"));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }


        @ParameterizedTest(name = " numero:  {index} ejecutando con valor {0}")
        @CsvSource({"20,10", "100,5", "50,50"})
        @DisplayName("probando el debito de la cuenta con parametros con Saldo y valor")
        void testDebitoCuenta3(String saldo, String monto) {
            System.out.println(saldo + "-> " + monto);
            cuenta = new Cuenta("Damian", new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }
        static List<String> montoList() {
            return Arrays.asList("10", "20", "5");
        }


    }
@Nested
@Disabled
@Tag("timeout")
   class ejemploTimeOut{
       @Test
       @Timeout(5)
       void pruebaTimeOut() throws InterruptedException {
           TimeUnit.SECONDS.sleep(2);
       }
       @Test
       @Timeout(value = 500,unit = TimeUnit.MILLISECONDS)
       void pruebaTimeOut1() throws InterruptedException {
           TimeUnit.SECONDS.sleep(2);
       }
   }
}