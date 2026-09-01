package com.lalynk.lalynk_backend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

public class ModulithTest
{

    @Test
    public void verifyModularity() {
        ApplicationModules.of(LalynkBackendApplication.class).verify();
    }



}
