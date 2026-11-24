package com.digiyak;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

/**
 * Architectural fitness test enforced in CI via Spring Modulith.
 *
 * <p>Scans every package under {@code com.digiyak} and fails the build immediately if:
 * <ul>
 *   <li>Any module imports another module's {@code internal} package.</li>
 *   <li>Any module violates {@code allowedDependencies} declared in its {@code package-info.java}.</li>
 *   <li>Any cyclic dependency exists between modules.</li>
 * </ul>
 */
class ModularityTests {

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(DigiyakApplication.class);
        modules.verify();
    }
}
