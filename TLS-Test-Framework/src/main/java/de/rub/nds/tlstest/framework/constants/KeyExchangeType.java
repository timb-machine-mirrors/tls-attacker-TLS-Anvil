package de.rub.nds.tlstest.framework.constants;

import de.rub.nds.tlsattacker.core.constants.AlgorithmResolver;
import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.constants.KeyExchangeAlgorithm;
import de.rub.nds.tlsscanner.report.SiteReport;
import de.rub.nds.tlstest.framework.TestContext;
import de.rub.nds.tlstest.framework.annotations.KeyExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class KeyX implements KeyExchange {

    private KeyExchangeType providedKx = KeyExchangeType.NOT_SPECIFIED;
    private KeyExchangeType[] supportedKxs = new KeyExchangeType[0];
    private boolean mergeSupportedWithClassSupported = true;

    KeyX() {
        super();
    }

    KeyX(KeyExchange exchange) {
        super();
        this.providedKx = exchange.provided();
        this.supportedKxs = exchange.supported();
        this.mergeSupportedWithClassSupported = exchange.mergeSupportedWithClassSupported();
    }

    @Override
    public KeyExchangeType provided() {
        return this.providedKx;
    }

    @Override
    public KeyExchangeType[] supported() {
        return this.supportedKxs;
    }

    @Override
    public boolean mergeSupportedWithClassSupported() {
        return mergeSupportedWithClassSupported;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    public void setProvidedKx(KeyExchangeType providedKx) {
        this.providedKx = providedKx;
    }

    public void setSupportedKxs(KeyExchangeType[] supportedKxs) {
        this.supportedKxs = supportedKxs;
    }

    public void filterSupportedKexs() {
        TestContext context = TestContext.getInstance();
        SiteReport report = context.getConfig().getSiteReport();
        Set<CipherSuite> ciphers = report.getCipherSuites();
        if (ciphers == null) {
            ciphers = new HashSet<>();
        }

        Set<KeyExchangeType> filtered = new HashSet<>();

        for (CipherSuite i : ciphers) {
            KeyExchangeAlgorithm kexalg = AlgorithmResolver.getKeyExchangeAlgorithm(i);
            for (KeyExchangeType t : this.supported()) {
                if (kexalg == null) {
                    continue;
                }
                if (kexalg.isKeyExchangeEcdh() && t == KeyExchangeType.ECDH) {
                    filtered.add(t);
                }
                else if (kexalg.isKeyExchangeRsa() && t == KeyExchangeType.RSA) {
                    filtered.add(t);
                }
                else if (kexalg.isKeyExchangeDh() && t == KeyExchangeType.DH) {
                    filtered.add(t);
                }
            }
        }

        if (Arrays.asList(this.supported()).contains(KeyExchangeType.TLS13) &&
                report.getSupportedTls13CipherSuites() != null &&
                report.getSupportedTls13CipherSuites().size() > 0) {
            filtered.add(KeyExchangeType.TLS13);
        }


        if (Arrays.asList(this.supported()).contains(KeyExchangeType.ALL12)) {
            filtered.add(KeyExchangeType.DH);
            filtered.add(KeyExchangeType.ECDH);
            filtered.add(KeyExchangeType.RSA);
        }

        KeyExchangeType[] filteredA = new KeyExchangeType[filtered.size()];
        filtered.toArray(filteredA);
        setSupportedKxs(filteredA);
    }
}

public enum KeyExchangeType {
    RSA,
    DH,
    ECDH,
    ALL12,
    TLS13,
    NOT_SPECIFIED;

    private static final Logger LOGGER = LogManager.getLogger();


    @Nonnull
    public static KeyExchange resolveKexAnnotation(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        Class<?> testClass = context.getRequiredTestClass();
        KeyX resolvedKeyExchange = new KeyX();
        String identifier = testClass.getName() + "." + testMethod.getName();

        // annotation on method level
        if (testMethod.isAnnotationPresent(KeyExchange.class)) {
            if (!testClass.isAnnotationPresent(KeyExchange.class)) {
                // annotation only on method level present
                KeyExchange existing = testMethod.getAnnotation(KeyExchange.class);
                resolvedKeyExchange = new KeyX(existing);
            } else if (testClass.isAnnotationPresent(KeyExchange.class)) {
                // annotation on method AND class level present
                KeyExchange method = testMethod.getAnnotation(KeyExchange.class);
                resolvedKeyExchange = new KeyX(method);

                KeyExchange cls = testClass.getAnnotation(KeyExchange.class);
                Set<KeyExchangeType> supportedKexsSet = new HashSet<>(Arrays.asList(method.supported()));

                if (method.mergeSupportedWithClassSupported()) {
                    supportedKexsSet.addAll(Arrays.asList(cls.supported()));
                }

                KeyExchangeType[] supportedKexs = new KeyExchangeType[supportedKexsSet.size()];
                supportedKexsSet.toArray(supportedKexs);

                KeyExchangeType provided = KeyExchangeType.NOT_SPECIFIED;
                if (method.provided() != KeyExchangeType.NOT_SPECIFIED) {
                    provided = method.provided();
                } else if (cls.provided() != KeyExchangeType.NOT_SPECIFIED) {
                    provided = cls.provided();
                }

                resolvedKeyExchange.setProvidedKx(provided);
                resolvedKeyExchange.setSupportedKxs(supportedKexs);
            }
        } else if (testClass.isAnnotationPresent(KeyExchange.class)) {
            KeyExchange existing = testClass.getAnnotation(KeyExchange.class);
            resolvedKeyExchange = new KeyX(existing);
        } else {
            LOGGER.error("No KeyExchange annotation could not be found on class or method level for " + identifier + ", thus the test is disabled.");
        }

        if (resolvedKeyExchange.provided() == KeyExchangeType.NOT_SPECIFIED && resolvedKeyExchange.supported().length == 1) {
            resolvedKeyExchange.setProvidedKx(resolvedKeyExchange.supported()[0]);
        }

        if ((testClass.isAnnotationPresent(KeyExchange.class) || testMethod.isAnnotationPresent(KeyExchange.class))
                && resolvedKeyExchange.provided() == KeyExchangeType.NOT_SPECIFIED) {
            LOGGER.warn("KeyExchange annotation used on method or class of " + identifier + ", but KeyExchange property 'provided' is not set, thus cannot transform.");
        }

        if (resolvedKeyExchange.supported().length > 0 || resolvedKeyExchange.provided() != KeyExchangeType.NOT_SPECIFIED) {
            Set<KeyExchangeType> supportedKexsSet = new HashSet<>(Arrays.asList(resolvedKeyExchange.supported()));
            if (resolvedKeyExchange.provided() != KeyExchangeType.NOT_SPECIFIED)
                supportedKexsSet.add(resolvedKeyExchange.provided());

            KeyExchangeType[] supportedKexs = new KeyExchangeType[supportedKexsSet.size()];
            supportedKexsSet.toArray(supportedKexs);

            resolvedKeyExchange.setSupportedKxs(supportedKexs);

            resolvedKeyExchange.filterSupportedKexs();
        } else {
            resolvedKeyExchange.setSupportedKxs(new KeyExchangeType[0]);
        }

        return resolvedKeyExchange;
    }
}
